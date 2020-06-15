package com.example.GoogleStarterProject;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootApplication
@RestController

public class Finder extends SpringBootServletInitializer {

    private static URL foodSupplierURL;
    private static URL foodVendorURL;
    private static HashMap<String, String> urlParams;
    private static final String INGREDIENT = "sugar";

    public Finder() throws MalformedURLException {
        //I don't think I add "ingredient" at the end, because later I add it as a param with DataOutputStream
        foodSupplierURL = new URL("https://supplier-dot-chessegg.wl.r.appspot.com");
        //foodSupplierURL = new URL("https://localhost:8080");
        foodVendorURL = new URL("https://vendor-dot-chessegg.wl.r.appspot.com");
        urlParams = new HashMap<String, String>();
        urlParams.put("ingredient", "sugar");
    }

    public static void postSupplierInfo() throws IOException {
        System.out.print("INSIDE POST METHOD ");
        URL url = foodSupplierURL;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        System.out.print("MADE HTTP CONNECTION ");
        // Enable output for the connection.
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        //conn.setRequestProperty("Accept", "text/plain");
        //conn.setRequestProperty("ingredient", "sugar");
        System.out.print("SET PROPERTIES ");
        //adding "ingredient" to the URL string, as well as its value.
        OutputStream out = conn.getOutputStream();
        out.write(INGREDIENT.getBytes());
        System.out.print("WROTE TO OUTPUT STREAM ");
        //out.writeBytes(addParamsURL(urlParams));
        out.flush();
        out.close();
        System.out.print("DONE WITH POST METHOD ");

//    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//    String inputLine;
//    StringBuffer response = new StringBuffer();
//
//    while ((inputLine = in.readLine()) != null) {
//       response.append(inputLine);
//    }
//    in.close();
//
//    // print result
//    String vendorList = response.toString();
//    //how to actually make the list show up on the server?
//    System.out.println(vendorList);

    }

    public static String addParamsURL(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }
        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }

    public static void getVendorInfo() throws IOException {
        URL url = foodVendorURL;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Enable output for the connection.
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        conn.setRequestProperty("Accept", "text/plain");

    }

    public static void main(String[] args) throws IOException {
        System.out.print("FINDER TEST ");
       Finder application = new Finder();
        //SpringApplication.run(GoogleStarterProjectApplication.class, args);
        System.out.print("PAST SPRING APPLICATION ");
        postSupplierInfo();
        System.out.print("PAST POST ");
        HttpServer server = HttpServer.create(new InetSocketAddress("https://finder-dot-chessegg.wl.r.appspot.com", 8080), 0);
        HttpContext context = server.createContext("/");
        System.out.print("SUCCESSFULLY CREATED SERVER ");
        context.setHandler(Finder::handleRequest);
        server.start();
    }

    //This is for getting the reply that supplier or vendor sends.
    //HttpExchange "encapsulates a HTTP request received and a response to be generated in one exchange".
    //The request received is from supplier or vendor, and the response is writing info to the webpage.
    private static void handleRequest(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (input, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        String response = textBuilder.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


}

