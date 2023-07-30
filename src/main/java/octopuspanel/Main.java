/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package octopuspanel;
import java.io.IOException;
import java.net.URISyntaxException;
import raspb.MultiInstanceError;
import raspb.RGB1602;

/**
 *
 * @author josh harney
 */
public class Main {

        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AgileAPI api = new AgileAPI();
            System.out.println(api.GetCurrentPrice());
            try {
                RGB1602 Display = new RGB1602(2, 16);
                System.out.println("initialised the module");
                System.out.println("Sending Display ON");
                Display.displayInit();
                Display.lcdSetRGB(243, 100, 55);
                Display.lcdSetCursor(0, 1);
                char[] firstline = ("Current Price: " + api.GetCurrentPrice()).toCharArray();
                Display.lcdWrite(firstline);

            } catch (MultiInstanceError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

    

}