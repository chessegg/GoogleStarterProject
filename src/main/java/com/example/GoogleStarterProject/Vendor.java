/*
The Finder service will call thee Supplier service with an ingredient query, and Supplier will return the price and
 inventory of the ingredient. It also runs as a standalone service. To deploy, copy this class over to the
GoogleStarterProjectApplication class, change service to "vendor" in the app.yaml file, and deploy to app engine.

 */

package com.example.GoogleStarterProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

//@SpringBootApplication
//@RestController
public class Vendor extends SpringBootServletInitializer {
    private HashMap<String, String[]> ingredientInfo = new HashMap<>();


    public Vendor() {
        ingredientInfo.put("sugar", new String[] {"0.24 USD/kg", "50,000 kg in inventory"});
        ingredientInfo.put("salt", new String[] {"0.83 USD/kg", "20,000 kg in inventory"});
        ingredientInfo.put("flour", new String[] {"2.00 USD/kg", "5,000 kg in inventory"});
        ingredientInfo.put("yeast", new String[] {"2.10 USD/kg", "7,000 kg in inventory"});
        ingredientInfo.put("egg", new String[] {"1.54 USD/dozen, 6,000 dozen left in inventory"});
        ingredientInfo.put("butter", new String[] {"8.36 USD/kg", "1,500 kg in inventory"});
    }

    @GetMapping("/")
    String defaultMapping(){
        return "Please type '/' and then the ingredient you are searching for in the URL";
    }

    @GetMapping("/{name}")
    String getIngredient(@PathVariable String name) {
        System.out.print("THE INGREDIENT IS " + name + " ");
        return Arrays.toString(getIngredientInfo(name, ingredientInfo));
    }

    //A test method printing out the hash map to make sure everything was added correctly
    public static void printHashMap(){
       Vendor vendorInstance = new Vendor();
        for (String name: vendorInstance.ingredientInfo.keySet()){
            System.out.print(name + ": ");
            String[] value = vendorInstance.ingredientInfo.get(name);
            for (String element : value) {
                System.out.print(element + ", ");
            }
            System.out.println();
        }
    }

    //Returns a String[] containing the list of vendors that sell the ingredient that was queried for
    public static String[] getIngredientInfo(String ingredient, HashMap<String, String[]> info){
        if (info.containsKey(ingredient)) {
            return info.get(ingredient);
        }
        else {
            return new String[] {" Please try again. Here is the list of available ingredients: sugar, salt, flour, yeast, egg, butter"};
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("VENDOR TEST ");
        //printHashMap();
        SpringApplication.run(GoogleStarterProjectApplication.class, args);
        System.out.print("SPRING RUNNING ");
    }
}
