package octopuspanel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseHandlers {
    public static OctoProduct[] ProductResponseHandle(CloseableHttpClient httpclient, HttpGet httpget, HttpContext context) throws IOException {
        
        HttpClientResponseHandler<OctoProduct[]> OctoProductsHandler = new HttpClientResponseHandler<OctoProduct[]>() {
        @Override
        public OctoProduct[] handleResponse(ClassicHttpResponse httpResponse) throws java.io.IOException, ParseException {
        
        if (httpResponse.getCode() != 200) {
            throw new RuntimeException(String.format("got a response other than 200: %d" ,httpResponse.getCode()));
        }
        
        String productJSONString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Product Response Handler called");
        System.out.println(httpResponse.getEntity().getContentType()); // should be application/json
        final JSONObject JSONAgileProduct = new JSONObject(productJSONString);
        final JSONArray JSONResultsArray = JSONAgileProduct.getJSONArray("results");
        // System.out.println(JSONResultsArray.toString());
        OctoProduct[] OctoProductArray = new OctoProduct[JSONResultsArray.length()];
        for (int x = 0; x < JSONResultsArray.length(); x++) {
            OctoProductArray[x] = new OctoProduct();
            OctoProductArray[x].code = JSONResultsArray.getJSONObject(x).getString("code");
            OctoProductArray[x].full_name = JSONResultsArray.getJSONObject(x).getString("full_name");
            OctoProductArray[x].display_name = JSONResultsArray.getJSONObject(x).getString("display_name");
            OctoProductArray[x].brand = JSONResultsArray.getJSONObject(x).getString("brand");
            OctoProductArray[x].available_from = JSONResultsArray.getJSONObject(x).getString("available_from");
            // sometimes null
            if (!JSONResultsArray.getJSONObject(x).isNull("available_to")) {
            OctoProductArray[x].available_to = JSONResultsArray.getJSONObject(x).getString("available_to");
            }
            OctoProductArray[x].is_variable = JSONResultsArray.getJSONObject(x).getBoolean("is_variable");
            OctoProductArray[x].is_green= JSONResultsArray.getJSONObject(x).getBoolean("is_green");
            OctoProductArray[x].is_tracker= JSONResultsArray.getJSONObject(x).getBoolean("is_tracker");
            OctoProductArray[x].is_business= JSONResultsArray.getJSONObject(x).getBoolean("is_business");
            OctoProductArray[x].is_prepay= JSONResultsArray.getJSONObject(x).getBoolean("is_prepay");
            OctoProductArray[x].is_variable= JSONResultsArray.getJSONObject(x).getBoolean("is_variable");
            OctoProductArray[x].is_restrcted= JSONResultsArray.getJSONObject(x).getBoolean("is_restricted");
            System.out.println("Generated new OctoProduct Object in the array: " + OctoProductArray[x].toString());
        }
        EntityUtils.consumeQuietly(httpResponse.getEntity());
        return OctoProductArray;
        };
        
    };
    return httpclient.execute(httpget, context, OctoProductsHandler);
    
    }

    public static OctoPrice[] AgilePriceResponseHandle(CloseableHttpClient httpclient, HttpGet httpget, HttpContext context) throws IOException {
        
        HttpClientResponseHandler<OctoPrice[]> AgilePriceHandler = new HttpClientResponseHandler<OctoPrice[]>() {
        @Override
        public OctoPrice[] handleResponse(ClassicHttpResponse httpResponse) throws java.io.IOException, ParseException {
        
        if (httpResponse.getCode() != 200) {
            throw new RuntimeException(String.format("got a response other than 200: %d" ,httpResponse.getCode()));
        }
        
        String productJSONString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(httpResponse.getEntity().getContentType()); // should be application/json
        System.out.println(httpResponse.getEntity().toString());
        final JSONObject JSONPriceObject = new JSONObject(productJSONString);
        final JSONArray JSONResultsArray = JSONPriceObject.getJSONArray("results");
        // System.out.println(JSONResultsArray.toString());
        OctoPrice[] OctoPriceArray = new OctoPrice[JSONResultsArray.length()];
        for (int x = 0; x < JSONResultsArray.length(); x++) {
            OctoPriceArray[x] = new OctoPrice();
            int starttimestringlength = JSONResultsArray.getJSONObject(x).getString("valid_from").length();
            int endtimestringlength = JSONResultsArray.getJSONObject(x).getString("valid_to").length();
            ZoneId timezone = ZoneId.of("Z");
            
            OctoPriceArray[x].StartTime = LocalDateTime.parse(JSONResultsArray.getJSONObject(x).getString("valid_from").substring(0, starttimestringlength-1)).atZone(timezone);
            OctoPriceArray[x].EndTime = LocalDateTime.parse(JSONResultsArray.getJSONObject(x).getString("valid_to").substring(0, endtimestringlength-1)).atZone(timezone);
            OctoPriceArray[x].UnitPrice = JSONResultsArray.getJSONObject(x).getDouble("value_inc_vat");
            System.out.println("Generated new OctoPrice Object in the array: " + OctoPriceArray[x].toString());
        }

        EntityUtils.consumeQuietly(httpResponse.getEntity());
        return OctoPriceArray;
        };
        
    };
    return httpclient.execute(httpget, context, AgilePriceHandler);
}

    public static String OctoProductJSONHandle(CloseableHttpClient httpclient, HttpGet httpget, HttpContext context) throws IOException {
        
        HttpClientResponseHandler<String> OctoProductJSONHandler = new HttpClientResponseHandler<String>() {
        @Override
        public String handleResponse(ClassicHttpResponse httpResponse) throws java.io.IOException, ParseException {
        
        if (httpResponse.getCode() != 200) {
            throw new RuntimeException(String.format("got a response other than 200: %d" ,httpResponse.getCode()));
        }
        
        String productJSONString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(httpResponse.getEntity().getContentType()); // should be application/json
        System.out.println(productJSONString);
        EntityUtils.consumeQuietly(httpResponse.getEntity());
        return productJSONString;
        };
        
    };
    return httpclient.execute(httpget, context, OctoProductJSONHandler);
}


}
