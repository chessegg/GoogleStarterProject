package com.example.GoogleStarterProject;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootApplication
@RestController

@WebServlet(name = "supplier", description = "takes in ingredient, sends back vendor list", urlPatterns = "/ingredient")
public class Supplier extends HttpServlet {
    private  static HashMap<String, String[]> ingredientVendors = new HashMap<>();

    public Supplier(){
        ingredientVendors.put("sugar", new String[] {"Walmart", "Target", "Costco"});
        ingredientVendors.put("salt", new String[] {"CVS", "Walgreens", "Vons", "Target"});
        ingredientVendors.put("flour", new String[] {"Albertsons", "Ralphs", "Sprouts", "Walmart"});
        ingredientVendors.put("yeast", new String[] {"Costco", "CVS", "Sprouts"});
        //this is pretty basic, just hard coding some data in just to have data to work with.
    }

    //This method listens in for incoming requests, and then sends back a response
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        //My goal is to be able to have user type in an ingredient like "flour", then save that as String ingredient.
        String ingredient = req.getParameter("ingredient");
        String[] ingrVendor = getVendorList(ingredient, ingredientVendors);
        //From what I understand, the below message is getting printed to the original (FoodFinder) server.
        resp.getWriter().write("Available Vendors: ");
        for (String vendor : ingrVendor){
            resp.getWriter().write(vendor + ", ");
        }
        resp.getWriter().write("\n");

    }

    //A test method printing out the hash map to make sure everything was added correctly
    public static void printHashMap(){
        Supplier supplierInstance = new Supplier();
        for (String name: supplierInstance.ingredientVendors.keySet()){
            //String key = name;
            System.out.print(name + ": ");
            String[] value = supplierInstance.ingredientVendors.get(name);
            for (String element : value) {
                System.out.print(element + ", ");
            }
            System.out.println();
        }
    }

    //Returns a String[] containing the list of vendors that sell the ingredient that was queried for
    public static String[] getVendorList(String ingredient, HashMap<String, String[]> vendors){
        if (vendors.containsKey(ingredient)) {
            return vendors.get(ingredient);
        }
        else {
            return new String[] {"No vendor matches found"};
        }
    }

    public static void main(String[] args) throws IOException {
        //instantiating an instance so that info gets added to the static hash map "ingredientVendors"
        Supplier application = new Supplier();
        System.out.print("SUPPLIER TEST ");
        //SpringApplication.run(GoogleStarterProjectApplication.class, args);
        HttpServer server = HttpServer.create(new InetSocketAddress("https://supplier-dot-chessegg.wl.r.appspot.com",8080), 0);
        System.out.print("CREATED SERVER ");
        HttpContext context = server.createContext("/");
        context.setHandler(Supplier::handleRequest);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        System.out.print("INSIDE HANDLE REQUEST ");
        InputStream input = exchange.getRequestBody();
        System.out.print("GOT REQUEST BODY ");
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (input, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        String ingredient = textBuilder.toString();
        //String ingredient = input.toString();
        //String ingredient = "sugar";
        //System.out.print("The ingredient is: " + ingredient);
        String[] responseArray = getVendorList(ingredient, ingredientVendors);
        String vendorList = Arrays.toString(responseArray);
        String response = new StringBuilder().append("The vendors with ingredient " ).append(ingredient).append(" are: ").append(vendorList).toString();
        //System.out.print("Response being sent by Supplier: " + response);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        input.close();
    }

    private static void handleResponse(HttpExchange exchange)  throws  IOException {
        OutputStream outputStream = exchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>").
                append("<body>").
                append("<h1>").
                append("Hello ").
                append("there.").
                append("</h1>").
                append("</body>").
                append("</html>");

        // maybe try something diff than "StringEscapeUtils" later
        String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
        exchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }





}
