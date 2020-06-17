/*
The Finder service will call the Supplier service with an ingredient query, and Supplier will return a list of vendors
that are supplying the ingredient. It also runs as a standalone service. To deploy, copy this class over to the
GoogleStarterProjectApplication class, change service to "supplier" in the app.yaml file, and deploy to app engine.

 */
package com.example.GoogleStarterProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


//@SpringBootApplication
//@RestController
@WebServlet(name = "supplier", description = "takes in ingredient, sends back vendor list", urlPatterns = "/ingredient")
public class Supplier extends SpringBootServletInitializer {
    private  static HashMap<String, String[]> ingredientVendors = new HashMap<>();

    public Supplier(){
        ingredientVendors.put("sugar", new String[] {"Walmart", "Target", "Costco"});
        ingredientVendors.put("salt", new String[] {"CVS", "Walgreens", "Vons", "Target"});
        ingredientVendors.put("flour", new String[] {"Albertsons", "Ralphs", "Sprouts", "Walmart"});
        ingredientVendors.put("yeast", new String[] {"Costco", "CVS", "Sprouts"});
        ingredientVendors.put("egg", new String[] {"Safeway", "Kroger", "Food Lion"});
        ingredientVendors.put("butter", new String[] {"Food4Less", "Vons", "Walgreens"});
        //this is pretty basic, just hard coding some data in just to have data to work with.
    }

    @GetMapping("/")
    String defaultMapping(){
        return "Please type '/' and then the ingredient you are searching for in the URL";
    }

    @GetMapping("/{name}")
    String getIngredient(@PathVariable String name) {
        System.out.print("THE INGREDIENT IS " + name + " ");
        return Arrays.toString(getVendorList(name, ingredientVendors));
    }


    //A test method printing out the hash map to make sure everything was added correctly
    public static void printHashMap(){
        Supplier supplierInstance = new Supplier();
        for (String name: supplierInstance.ingredientVendors.keySet()){
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
        System.out.print("SUPPLIER TEST ");
        SpringApplication.run(GoogleStarterProjectApplication.class, args);
        System.out.print("SPRING RUNNING ");
    }


}


