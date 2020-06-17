/*
NOTE: this class is basically just a deployment class. I was having trouble deploying via multiple yaml files and trying
to match the correct java file to the correct yaml file, so what I ended up doing is just having this one class and one
yaml file. When I want to deploy Finder, I just copy and paste the Finder code over to this class and change the service
name in the app.yaml file to "finder" and deploy in terminal. When I want to deploy Supplier, I just copy and paste that
class in to here to deploy via the same process. It's admittedly just a lazy workaround, but it works so whatever.

 */

package com.example.GoogleStarterProject;

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

	public GoogleStarterProjectApplication() {

	}

	@GetMapping("/")
	String defaultMapping(){
		return "Please type '/' and then the ingredient you are searching for in the URL." + "\n"
				+ "Here is the list of available ingredients: sugar, salt, flour, yeast, egg, butter";
	}


	@GetMapping("/{name}")
	String getIngredient(@PathVariable String name) throws IOException {
		System.out.print("THE INGREDIENT IS " + name + " ");
		INGREDIENT = name;
		foodSupplierURL = new URL("https://supplier-dot-chessegg.wl.r.appspot.com/" + INGREDIENT);
		foodVendorURL = new URL("https://vendor-dot-chessegg.wl.r.appspot.com/" + INGREDIENT);
		return getSupplierInfo() + getVendorInfo();
	}

	public static String getSupplierInfo() throws IOException {
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

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String vendorList = response.toString();
		System.out.println("THE VENDOR LIST IS  " + vendorList + " ");
		return new StringBuilder().append("The vendors with ingredient " ).append(INGREDIENT).append(" are: ").append(vendorList).append(". ").toString();
		//return vendorList;

	}

	public static String getVendorInfo() throws IOException {
		URL url = foodVendorURL;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Enable output for the connection.
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String ingredientInfo = response.toString();
		if (ingredientInfo.equals("[ Please try again. Here is the list of available ingredients: sugar, salt, flour, yeast, egg, butter]"))
			return ingredientInfo;
		else
			return new StringBuilder().append("The market price and inventory near you for ingredient " ).append(INGREDIENT).append(" are: ").append(ingredientInfo).toString();
	}

	public static void main(String[] args) throws IOException {
		System.out.print("FINDER TEST ");
		GoogleStarterProjectApplication application = new GoogleStarterProjectApplication();
		SpringApplication.run(GoogleStarterProjectApplication.class, args);
	}


}