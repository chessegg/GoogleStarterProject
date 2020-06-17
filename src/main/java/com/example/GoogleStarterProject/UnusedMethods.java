package com.example.GoogleStarterProject;

/*
These are methods that I spent time creating and looking at while trying to get this application working, but turns out
that I did not need them via the way I decided to get my application to work.
However, I will keep these here just in case I want to tweak the application in the future and these methods might prove
to be useful.
 */
public class UnusedMethods {
    //In Finder class:

    //private static HashMap<String, String> urlParams;
    //urlParams = new HashMap<>();
    //urlParams.put("ingredient", INGREDIENT);

    //(in getSupplierInfo() method, for output)
//     adding "ingredient" to the URL string, as well as its value.
    //for POST method
//		OutputStream out = conn.getOutputStream();
//		out.write(INGREDIENT.getBytes());
    //for GET method
//		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//		out.writeBytes(addParamsURL(urlParams));
//		System.out.print("WROTE TO OUTPUT STREAM ");
//		out.flush();
//		out.close();
//
//    public static String addParamsURL(HashMap<String, String> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//
//        for (HashMap.Entry<String, String> entry : params.entrySet()) {
//            result.append("/");
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("/");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
//        String resultString = result.toString();
//        System.out.print("PARAMS URL IS " + resultString + " ");
//        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
//    }

    //to run with HttpServer (in main)
//		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//		HttpContext context = server.createContext("/");
//		System.out.print("SUCCESSFULLY CREATED SERVER ");
//		context.setHandler(GoogleStarterProjectApplication::handleRequest);
//		server.start();

    //handleRequest() method
//    private static void handleRequest(HttpExchange exchange) throws IOException {
//    		InputStream input = exchange.getRequestBody();
//		StringBuilder textBuilder = new StringBuilder();
//		try (Reader reader = new BufferedReader(new InputStreamReader
//				(input, Charset.forName(StandardCharsets.UTF_8.name())))) {
//			int c = 0;
//			while ((c = reader.read()) != -1) {
//				textBuilder.append((char) c);
//			}
//		}
    //String response = textBuilder.toString() + " hello";

//        String vendors = getSupplierInfo();
//        String response = new StringBuilder().append("The vendors with ingredient " ).append(INGREDIENT).append(" are: ").append(vendors).toString();
//        System.out.print("PAST GET ");
//        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }



//In Supplier class:

//    @PostMapping("/ingredient")
//    String postIngredient(@RequestBody String ingredient) {
//        System.out.print("THE INGREDIENT IS " + ingredient);
//        return Arrays.toString(getVendorList(ingredient, ingredientVendors));
//    }

    //This method listens in for incoming requests, and then sends back a response
//	@Override
//	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		resp.setContentType("text/plain");
//		//My goal is to be able to have user type in an ingredient like "flour", then save that as String ingredient.
//		String ingredient = req.getParameter("ingredient");
//		String[] ingrVendor = getVendorList(ingredient, ingredientVendors);
//		//From what I understand, the below message is getting printed to the original (FoodFinder) server.
//		resp.getWriter().write("Available Vendors: ");
//		for (String vendor : ingrVendor){
//			resp.getWriter().write(vendor + ", ");
//		}
//		resp.getWriter().write("\n");
//
//	}

//    private static void handleRequest(HttpExchange exchange) throws IOException {
//        System.out.print("INSIDE HANDLE REQUEST ");
//        InputStream input = exchange.getRequestBody();
//        System.out.print("GOT REQUEST BODY ");
//        StringBuilder textBuilder = new StringBuilder();
//        try (Reader reader = new BufferedReader(new InputStreamReader
//                (input, Charset.forName(StandardCharsets.UTF_8.name())))) {
//            int c = 0;
//            while ((c = reader.read()) != -1) {
//                textBuilder.append((char) c);
//            }
//        }
//        String ingredient = textBuilder.toString();
//        //String ingredient = input.toString();
//        //String ingredient = "sugar";
//        //System.out.print("The ingredient is: " + ingredient);
//        String[] responseArray = getVendorList(ingredient, ingredientVendors);
//        String vendorList = Arrays.toString(responseArray);
//        String response = new StringBuilder().append("The vendors with ingredient " ).append(ingredient).append(" are: ").append(vendorList).toString();
//        //System.out.print("Response being sent by Supplier: " + response);
//        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//        input.close();
//    }

//    private static void handleResponse(HttpExchange exchange)  throws IOException {
//        OutputStream outputStream = exchange.getResponseBody();
//        StringBuilder htmlBuilder = new StringBuilder();
//
//        htmlBuilder.append("<html>").
//                append("<body>").
//                append("<h1>").
//                append("Hello ").
//                append("there.").
//                append("</h1>").
//                append("</body>").
//                append("</html>");
//
//        // maybe try something diff than "StringEscapeUtils" later
//        String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
//        exchange.sendResponseHeaders(200, htmlResponse.length());
//        outputStream.write(htmlResponse.getBytes());
//        outputStream.flush();
//        outputStream.close();
//    }



}
