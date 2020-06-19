/*
NOTE: this class is basically just a deployment class. I was having trouble deploying via multiple yaml files and trying
to match the correct java file to the correct yaml file, so what I ended up doing is just having this one class and one
yaml file. When I want to deploy Finder, I just copy and paste the Finder code over to this class and change the service
name in the app.yaml file to "finder" and deploy in terminal. When I want to deploy Supplier, I just copy and paste that
class in to here to deploy via the same process. It's admittedly just a lazy workaround, but it works so whatever.

 */

package com.example.GoogleStarterProject;

import io.opencensus.common.Scope;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
@RestController

public class GoogleStarterProjectApplication extends SpringBootServletInitializer {

	private static URL foodSupplierURL;
	private static URL foodVendorURL;
	private static String INGREDIENT;
	private static final Tracer tracer = Tracing.getTracer();

	public GoogleStarterProjectApplication() {

	}

	@GetMapping("/")
	String defaultMapping(){
		return "Please type '/' and then the ingredient you are searching for in the URL." + "\n"
				+ "Here is the list of available ingredients: sugar, salt, flour, yeast, egg, butter";
	}


	@GetMapping("/{name}")
	String getIngredient(@PathVariable String name) throws IOException {
		try (Scope ss = tracer.spanBuilder("Finder service: getMapping for ingredient " + name).setSampler(Samplers.alwaysSample()).startScopedSpan()){
			System.out.print("THE INGREDIENT IS " + name + " ");
			INGREDIENT = name;
			foodSupplierURL = new URL("https://supplier-dot-chessegg.wl.r.appspot.com/" + INGREDIENT);
			foodVendorURL = new URL("https://vendor-dot-chessegg.wl.r.appspot.com/" + INGREDIENT);
			return getSupplierInfo() + getVendorInfo();
		}
	}

	public static String getSupplierInfo() throws IOException {
		try (Scope ss = tracer.spanBuilder("Supplier service: " + INGREDIENT + " supplierInfo").setSampler(Samplers.alwaysSample()).startScopedSpan()){
			System.out.print("INSIDE GET METHOD ");
			URL url = foodSupplierURL;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			System.out.print("MADE HTTP CONNECTION ");
			// Enable output for the connection.
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
			//conn.setRequestProperty("Accept", "text/plain");
			System.out.print("SET PROPERTIES ");
			Span span = tracer.getCurrentSpan();
			span.addAnnotation("About to read input stream from supplier service");

			String vendorList = readInputStream(conn);
			System.out.println("THE VENDOR LIST IS  " + vendorList + " ");
			return new StringBuilder().append("The vendors with ingredient " ).append(INGREDIENT).append(" are: ").append(vendorList).append(". ").toString();
			//return vendorList;
		}

	}

	public static String getVendorInfo() throws IOException {
		try (Scope ss = tracer.spanBuilder("Vendor service: " + INGREDIENT + " vendorInfo").setSampler(Samplers.alwaysSample()).startScopedSpan()){
			URL url = foodVendorURL;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Enable output for the connection.
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
			Span span = tracer.getCurrentSpan();
			span.addAnnotation("About to read input stream from vendor service");

			String ingredientInfo = readInputStream(conn);
			if (ingredientInfo.equals("[ Please try again. Here is the list of available ingredients: sugar, salt, flour, yeast, egg, butter]"))
				return ingredientInfo;
			else
				return new StringBuilder().append("The market price and inventory near you for ingredient " ).append(INGREDIENT).append(" are: ").append(ingredientInfo).toString();
		}

	}

	public static String readInputStream(HttpURLConnection conn) throws IOException {
		try (Scope ss = tracer.spanBuilder("readingHTTPInput").setSampler(Samplers.alwaysSample()).startScopedSpan()){
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		}
	}

	private static void setupOpenCensusAndStackdriverExporter() throws IOException {
		String gcpProjectId = "chessegg";
		// For demo purposes, always sample
		TraceConfig traceConfig = Tracing.getTraceConfig();
		traceConfig.updateActiveTraceParams(traceConfig.getActiveTraceParams().toBuilder().setSampler(Samplers.alwaysSample()).build());

		StackdriverTraceExporter.createAndRegister(StackdriverTraceConfiguration.builder().setProjectId(gcpProjectId).build());
		//String gcpProjectID = System.getenv().get(key);
		//StackdriverTraceExporter.createAndRegister(StackdriverTraceConfiguration.builder().setProjectId(gcpProjectId).build());
	}

	public static void main(String[] args) throws IOException {
		try {
			setupOpenCensusAndStackdriverExporter();
		} catch (IOException e) {
			System.err.println("Failed to create and register OpenCensus Stackdriver Trace exporter "+ e);
			return;
		}
		System.out.print("FINDER TEST ");
		GoogleStarterProjectApplication application = new GoogleStarterProjectApplication();
		SpringApplication.run(GoogleStarterProjectApplication.class, args);

	}


}