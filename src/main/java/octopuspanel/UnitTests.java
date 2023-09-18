package octopuspanel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class UnitTests {
    
    static OctoProduct octoProductOne = new OctoProduct();
    static OctoProduct octoProductTwo = new OctoProduct();
    static OctoPrice octoPriceOne = new OctoPrice();
    static OctoPrice octoPriceTwo = new OctoPrice();

    public static void main(String[] args) throws URISyntaxException, IOException {
        buildOctoProducts();
        System.out.println(" Equality tests: "+ equalsTrueTest(octoProductOne, octoProductTwo));
        System.out.println(" Equality tests: "+ equalsTrueTest(octoPriceOne, octoPriceTwo));
        buildOctoProducts();
        HashcodeTest(octoProductOne, octoProductTwo);
        buildOctoProducts();
        HashcodeTest(octoPriceOne, octoPriceTwo);
        AgileAPI api = new AgileAPI(4);
        Thread t1 = new Thread(api);
        System.out.println(api.CheapestTonight());
        
        System.out.println(t1);
        System.out.println(api);
        System.out.println(String.format("is plunge price: %b", api.isPlugePrice()));
        System.out.println(api.PlungeSegment());
        testPlungeColours();
    }

    public static void apiInterruptTest(AgileAPI api) {
        api.interrupt();
        
        
    }

    public static void HashcodeTest(OctoProduct octo1, OctoProduct octo2) {
        System.out.println("HashcodeTests - OctoProducts");
        System.out.println("Object One: "+ octo1.hashCode() + " Object Two: " +octo2.hashCode());
    }

    public static void HashcodeTest(OctoPrice octo1, OctoPrice octo2) {
        System.out.println("HashcodeTests - OctoPrices");
        System.out.println("Object One: "+ octo1.hashCode() + " Object Two: " +octo2.hashCode());
    }

    public static boolean equalsTrueTest(OctoProduct octo1, OctoProduct octo2) {
       
        if (octo1.equals(octo2)) {
            // passed and continue
            octo1.is_business = true;
            return (!octo1.equals(octo2));
        }
        else {return false; }
    }   

    public static boolean equalsTrueTest(OctoPrice octo1, OctoPrice octo2) {

        if (octo1.equals(octo2)) {
            octo1.UnitPrice = 325.32D;
                return (!octo1.equals(octo2)) ? true :  false; 
    }

    else { return false;}
    }

    public static void buildOctoProducts() {
        
        ZonedDateTime datetime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        octoProductOne.available_from = "2023-08-06T02:30Z";
        octoProductTwo.available_from = "2023-08-06T02:30Z";
        octoProductOne.available_to = "2023-08-06T02:30Z";
        octoProductTwo.available_to = "2023-08-06T02:30Z";
        octoProductOne.brand = "somebrand";
        octoProductTwo.brand = "somebrand";
        octoProductOne.code = "code";
        octoProductTwo.code = "code";
        octoProductOne.description = "some description";
        octoProductTwo.description = "some description";
        octoProductOne.display_name = "some display name";
        octoProductTwo.display_name = "some display name";
        octoProductOne.full_name = "big and long full name";
        octoProductTwo.full_name = "big and long full name";
        octoProductOne.is_business = false;
        octoProductTwo.is_business = false;
        octoProductOne.is_green = false;
        octoProductTwo.is_green= false;
        octoProductOne.is_prepay = true;
        octoProductTwo.is_prepay = true;
        octoProductOne.is_restricted = false;
        octoProductTwo.is_restricted = false;
        octoProductOne.is_tracker = true;
        octoProductTwo.is_tracker = true;
        octoProductOne.is_variable = false;
        octoProductTwo.is_variable = false;


        octoPriceOne.UnitPrice = 35.32D;
        octoPriceTwo.UnitPrice = 35.32D;
        octoPriceOne.priceCreateTime = datetime;
        octoPriceTwo.priceCreateTime = datetime;
        octoPriceOne.StartTime = ZonedDateTime.of(2023, 11, 21, 14, 11, 02, 22, ZoneId.of("Z"));
        octoPriceTwo.StartTime = ZonedDateTime.of(2023, 11, 21, 14, 11, 02, 22, ZoneId.of("Z"));
        octoPriceOne.EndTime = ZonedDateTime.of(2023, 01, 21, 2, 10, 02, 22, ZoneId.of("Z"));
        octoPriceTwo.EndTime = ZonedDateTime.of(2023, 01, 21, 2, 10, 02, 22, ZoneId.of("Z"));
    }

    public static void testPlungeColours() {
        int [][] colours = ColourSetter.PlungeColours();
        for (int x = 0; x < 3; x++) {
            System.out.println(Arrays.toString(colours[x]));
        }
    }

}
