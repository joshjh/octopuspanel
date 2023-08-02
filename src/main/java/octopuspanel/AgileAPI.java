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
    
    public void GetAPIData() throws URISyntaxException, java.io.IOException {
        HttpContext context = new BasicHttpContext();
        CloseableHttpClient httpclient = InitHttp();
        URI productsuri, priceuri;
        
        try {
        // Products
            productsuri = AgileProductsURI();
            HttpGet products_httpget = new HttpGet(productsuri);
            octoProducts = ResponseHandlers.ProductResponseHandle(httpclient, products_httpget, context);
        
        // Try prices
            priceuri = AgilePriceURI();
            HttpGet prices_httpget = new HttpGet(priceuri);
            octoPrices = ResponseHandlers.AgilePriceResponseHandle(httpclient, prices_httpget, context);
            
        }
        finally {
            httpclient.close();
        }
    }

    public AgileAPI() throws URISyntaxException, java.io.IOException {
        
        GetAPIData();
        }

    public double GetCurrentPrice() {
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(datetime_now) & octoprice.EndTime.isAfter(datetime_now)) {
                    return octoprice.UnitPrice;
                }
            
            }
            return 0.00D;
        }

    public double NextPrice() {
        ZonedDateTime next_segment = LocalDateTime.now().atZone(timezone).plusMinutes(30);
         for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(next_segment) & octoprice.EndTime.isAfter(next_segment)) {
                    return octoprice.UnitPrice;
                }
            }
        return 0.00D;
        }
    public double NextHour() {
        ZonedDateTime next_segment = LocalDateTime.now().atZone(timezone).plusHours(1);
         for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(next_segment) & octoprice.EndTime.isAfter(next_segment)) {
                    return octoprice.UnitPrice;
                }
            }
        return 0.00D;
        }
    
     public String CheapestSegment() {
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        double lowestDouble = 100D;
        String lowString = "Cheapest Hour Failed";
        ZoneId zuluzone = ZoneId.of("Z"); // prices are stored in ZULU time in the OctoPrice Object.
        for (OctoPrice octoPrice : this.octoPrices) {
                if (octoPrice.EndTime.isAfter(datetime_now)) {
                    if (octoPrice.UnitPrice <= lowestDouble) {
                        lowestDouble = octoPrice.UnitPrice;
                        // move to local datetime, at zuro, then create with current timezone, then local date time, then local time.  etc etc.
                        lowString = String.valueOf(octoPrice.UnitPrice) + " @"+ octoPrice.StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime();
                    }
                }
            }
        return lowString;
     }   

     public String Cheapest_3Run() {
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        double lowestDouble = 100.00D;
        String lowString = "Cheapest 3 failed";
        ZoneId zuluzone = ZoneId.of("Z"); // prices are stored in ZULU time in the OctoPrice Object.
        // loop up until the last three remain.  octoPrices[] is built in reverse order, so next time segment comes before the current one in the array.  ie [0] is the latest, [-1] the second latest and length-1 the earliest in the array.
        for (int x = 2; x < octoPrices.length; x++) {
            double runTotalAve = (octoPrices[x].UnitPrice + octoPrices[x-1].UnitPrice + octoPrices[x-2].UnitPrice)/3;
            if (octoPrices[x].EndTime.isAfter(datetime_now)) {
                if (runTotalAve < lowestDouble) {
                    lowestDouble = runTotalAve;
                    lowString = String.valueOf(lowestDouble) +":" + octoPrices[x].StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime() +
                    ":" + octoPrices[x-2].EndTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime();
                }
            }
        }
        return lowString;
     }
     

    public void PrintProducts() {
        for (OctoProduct product : octoProducts) {
            System.out.println(product);
        }
    
    }
}
