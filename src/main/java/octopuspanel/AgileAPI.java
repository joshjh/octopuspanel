package octopuspanel;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.http.client.utils.URIBuilder;

public class AgileAPI {
    final static String AGILE_PRODUCT_CODE = "AGILE-FLEX-BB-23-02-08";
    // there are different pricings in the UK depending on area.  Who knew?  The South West is 'L'
    final static String AGILE_TARIFF_CODE = String.format("E-1R-%s-L", AGILE_PRODUCT_CODE);
    final static ZoneId timezone = ZoneId.systemDefault();
    public OctoPrice[] octoPrices;
    public OctoProduct[] octoProducts;
    private CloseableHttpClient InitHttp() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return httpclient;

    }
    
    /** 
     * @return URI
     * @throws URISyntaxException
     */
    public static URI AgileProductsURI() throws URISyntaxException {
            URI uri = new URIBuilder().setScheme("https")
            .setHost("api.octopus.energy")
            .setPath("v1/products/")
            .build();
            return uri;
        
    }
    public static URI AgilePriceURI() throws URISyntaxException {
            URI uri = new URIBuilder().setScheme("https")
            .setHost("api.octopus.energy")
            .setPath("v1/products/"+AGILE_PRODUCT_CODE+"/electricity-tariffs/"+AGILE_TARIFF_CODE+"/standard-unit-rates/")  
            .build();
            return uri;
        
    }
    
    public static URI IndiviualProductURI() throws URISyntaxException {
            URI uri = new URIBuilder().setScheme("https")
            .setHost("api.octopus.energy")
            .setPath("v1/products/"+AGILE_PRODUCT_CODE)
            .build();
            return uri;
        
    }
    
    public AgileAPI() throws URISyntaxException, java.io.IOException {
        
        HttpContext context = new BasicHttpContext();
        CloseableHttpClient httpclient = InitHttp();
        URI productsuri, priceuri, indiviual_product_URI;

        // Products
            productsuri = AgileProductsURI();
            HttpGet products_httpget = new HttpGet(productsuri);
            octoProducts = ResponseHandlers.ProductResponseHandle(httpclient, products_httpget, context);
        
        // Try prices
            priceuri = AgilePriceURI();
            HttpGet prices_httpget = new HttpGet(priceuri);
            octoPrices = ResponseHandlers.AgilePriceResponseHandle(httpclient, prices_httpget, context);
        }

    public String GetCurrentPrice() {
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(datetime_now) & octoprice.EndTime.isAfter(datetime_now)) {
                    return octoprice.toString();
                }
            
        }
        return "Cannot resolve price";
    }
    public void PrintProducts() {
        for (OctoProduct product : octoProducts) {
            System.out.println(product);
        }
    }
}