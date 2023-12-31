package octopuspanel;

import java.io.IOException;
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
import org.apache.hc.core5.io.CloseMode;
import org.apache.http.client.utils.URIBuilder;

public class AgileAPI extends Thread{
    final static String AGILE_PRODUCT_CODE = "AGILE-FLEX-BB-23-02-08";
    // there are different pricings in the UK depending on area.  Who knew?  The South West is 'L'
    final static String AGILE_TARIFF_CODE = String.format("E-1R-%s-L", AGILE_PRODUCT_CODE);
    final static ZoneId timezone = ZoneId.systemDefault();
    public ZonedDateTime lastAPIUpdate;
    public OctoPrice[] octoPrices;
    public OctoProduct[] octoProducts;
    public int agileRefreshInterval;
    public boolean apiWarnFlag = false;
    HttpContext context = new BasicHttpContext();
    CloseableHttpClient httpclient = InitHttp();
    URI productsuri, priceuri;
    
    private CloseableHttpClient InitHttp() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return httpclient;
    }

    public AgileAPI restartAPI() throws URISyntaxException, IOException {
        httpclient.close(CloseMode.GRACEFUL);
        // need to let the httpclient close down
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new AgileAPI(4);
    }

    public static double RoundResult(double d) {
        d = Math.round(d * 100.00D);
        d = d / 100.00D;
        return d;
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
        // CloseableHttpClient is an AutoClosable so we can try with resources to tidy this call up.
        try (CloseableHttpClient httpclient = InitHttp()) {
            // Products
            productsuri = AgileProductsURI();
            HttpGet products_httpget = new HttpGet(productsuri);
            octoProducts = ResponseHandlers.ProductResponseHandle(httpclient, products_httpget, context);
        
        // Try prices
            priceuri = AgilePriceURI();
            HttpGet prices_httpget = new HttpGet(priceuri);
            octoPrices = ResponseHandlers.AgilePriceResponseHandle(httpclient, prices_httpget, context);
        } catch (Exception e) {
            
        }
        
            lastAPIUpdate =  LocalDateTime.now().atZone(timezone);
    }
    

    public AgileAPI(int agileRefreshInterval) throws URISyntaxException, java.io.IOException {
        // agile refresh interval (minutes)
        this.agileRefreshInterval = agileRefreshInterval;
        GetAPIData();
        this.setDaemon(true);
        // return the running API thread
        this.start();
    
    }

    public double GetCurrentPrice() {
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(datetime_now) & octoprice.EndTime.isAfter(datetime_now)) {
                    return RoundResult(octoprice.UnitPrice);
                }
            
            }
            return 0.00D;
    }

    public double NextPrice() {
        ZonedDateTime next_segment = LocalDateTime.now().atZone(timezone).plusMinutes(30);
         for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(next_segment) & octoprice.EndTime.isAfter(next_segment)) {
                    return RoundResult(octoprice.UnitPrice);
                }
            }
        return 0.00D;
    }
        
    public double NextHour() {
        ZonedDateTime next_segment = LocalDateTime.now().atZone(timezone).plusHours(1);
         for (OctoPrice octoprice : this.octoPrices) {
                if (octoprice.StartTime.isBefore(next_segment) & octoprice.EndTime.isAfter(next_segment)) {
                    return RoundResult(octoprice.UnitPrice);
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
                        lowString = String.valueOf(RoundResult(octoPrice.UnitPrice)) + " @"+ octoPrice.StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime();
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
                    lowString = String.valueOf(RoundResult(lowestDouble)) +":" + octoPrices[x].StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime() +
                    ":" + octoPrices[x-2].EndTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime();
                }
            }
        }
    return lowString;
    }
    
    
    /**  Check if there is plunge pricing in the known future.  We regard plunge as < 2.00D, rather than the strict negative
     * as we still like charging cards and stuff.  Returns a boolean if there is a matching OctoPrice object in the loaded API array.
     * @return boolean
     */
    public boolean isPlugePrice() {
        
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        for (int x = 0; x < octoPrices.length; x++) {
            if (octoPrices[x].UnitPrice < 2.00D & octoPrices[x].EndTime.isAfter(datetime_now)) {
                return true;
            }
        }
        return false;
    }

    public String PlungeSegment() {
    ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
    double lowestDouble = 2.00D;
    String lowString = "Plunge Price fail";
    ZoneId zuluzone = ZoneId.of("Z"); // prices are stored in ZULU time in the OctoPrice Object.
    for (OctoPrice octoPrice : this.octoPrices) {
            if (octoPrice.EndTime.isAfter(datetime_now)) {
                if (octoPrice.UnitPrice <= lowestDouble) {
                    lowestDouble = octoPrice.UnitPrice;
                    // move to local datetime, at zuro, then create with current timezone, then local date time, then local time.  etc etc.
                    lowString = String.valueOf(RoundResult(octoPrice.UnitPrice)) + " @"+ octoPrice.StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime();
                }
            }
        }
    return lowString;
    }   

     
    
    /** 
     * run method for the threaded implementation of the API
     * examines the time via LocalDateTime.now().atZone(timezone);
     * we will look for updates after 4 pm.
     * @throws APIConnectionException
     */
    public void run() {
        System.out.println("Agile API Thread Running");
        
        while (true) {
     
                try {
                    Thread.sleep(agileRefreshInterval*60000);
                    LocalDateTime datetime_now = LocalDateTime.now();
                    ZoneId zuluzone = ZoneId.of("Z");
                    LocalDateTime setRefreshPoint = LocalDateTime.of(datetime_now.getYear(), datetime_now.getMonth(), datetime_now.getDayOfMonth(), 17, 00);
                    // if now is after the pricecreated time of the [0] price in teh octPrices array plus ten hours, it's time for refreshing, otherwise dont make a wasted call to the Octpus API.  If it's successful octoPrices.priceCreateTime will reset to now and the if condition is false.
                    if (datetime_now.isAfter(octoPrices[0].priceCreateTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().plusHours(10))) {
                        GetAPIData();
                    }
                    // if it's between 5 and 5:30 PM lets get always
                    // TODO can we test if we hold a price far enough in the future?  new function?
                    if (datetime_now.isAfter(setRefreshPoint) && (datetime_now.isBefore(setRefreshPoint.plusMinutes(30)))) {
                        if (!octoPrices[0].priceCreateTime.withZoneSameInstant(timezone).toLocalDateTime().isAfter(setRefreshPoint)) {
                        System.out.println("Triggered on time");
                        GetAPIData();
                        }
                    }            
                } 
                catch (IOException e) {
                    Thread.currentThread().interrupt(); // set the interupted thread that the ticker will test.
                    this.apiWarnFlag = true;
                }
                catch (InterruptedException | URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("Interrupted!!!!!!");
                    this.apiWarnFlag = true;
                    System.out.println(String.format("Setting the  API warn flag: %b", apiWarnFlag));
                    
                }
        }
    }
    
    public void PrintProducts() {
        for (OctoProduct product : octoProducts) {
            System.out.println(product);
        }
    
    }

    public String CheapestTonight() {
        ZoneId zuluzone = ZoneId.of("Z"); // prices are stored in ZULU time in the OctoPrice Object.
        ZonedDateTime datetime_now = LocalDateTime.now().atZone(timezone);
        double cost = 30000.00D;
        String lowString = "lowest tonight failed";
        for (OctoPrice octoPrice : octoPrices) {
            // look between 22:00 tonight and 05:00 tomorrow AM.
            if (octoPrice.StartTime.isAfter(datetime_now.withHour(22)) && (octoPrice.StartTime.isBefore(datetime_now.withHour(5).plusDays(1)))) {
                if (octoPrice.UnitPrice < cost) {
                    cost = octoPrice.UnitPrice;
                    lowString = String.valueOf(RoundResult(octoPrice.UnitPrice) + ":" + octoPrice.StartTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime() +
                    ":" + octoPrice.EndTime.toLocalDateTime().atZone(zuluzone).withZoneSameInstant(timezone).toLocalDateTime().toLocalTime());
                }
            }
        }
    return lowString;
    }


}
