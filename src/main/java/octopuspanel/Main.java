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

    static int screenMoveDelay = 10000;

    public static void ShowCurrentPrice(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Current Price: ".toCharArray();
        char [] secondline = String.valueOf(api.GetCurrentPrice()).toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        int [] colour = ColourSetter.GetColour(api.GetCurrentPrice());
        Display.lcdSetRGB(colour[0], colour[1], colour[2]);
        Thread.sleep(delay);
    }

    public static void ShowCheapest30Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Cheapest 30 minutes:".toCharArray();
        char[] secondline = api.CheapestSegment().toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }
       
    public static void ShowNext30Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Next 30 minutes:".toCharArray();
        char[] secondline = String.valueOf(api.NextPrice()).toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }

    public static void ShowNext60Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Next 60 minutes:".toCharArray();
        char[] secondline = String.valueOf(api.NextHour()).toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }
    public static void ShowCheapest3Segements(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Cheap 3 Segs:".toCharArray();
        char[] secondline = api.Cheapest_3Run().toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }

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
                Display.clearDisplay();
                int iterations = 0;
                while (true) {
                    if (iterations == 500) {
                    // reload the API time and product sets every 500 turns
                        try {
                            api.GetAPIData();
                            iterations = 0;
                        }
                        catch (IOException e) {
                            System.out.println("Something bad happened whilst loading the API datasets!");
                        }
                     }
                    ShowCurrentPrice(Display, api, screenMoveDelay);
                    ShowCheapest30Mins(Display, api, screenMoveDelay);
                    ShowNext30Mins(Display, api, screenMoveDelay);
                    ShowNext60Mins(Display, api, screenMoveDelay);
                    ShowCheapest3Segements(Display, api, screenMoveDelay);
                    iterations++;

                }
            } catch (MultiInstanceError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

    

}