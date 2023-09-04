/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package octopuspanel;
import java.io.IOException;
import java.net.URISyntaxException;
import com.raspb.RGB1602;
import com.raspb.MultiInstanceError;

/**
 *
 * @author josh harney
 */
public class Main {
    static Thread apiThread;
    static Thread TickerThread;

    public static void selectPressed(RGB1602 Display, AgileAPI api) {
        TickerThread.interrupt();
    }

    public static void buildButtons(RGB1602 Display, AgileAPI api) {
        // button handlers
        // we cannot just run these on event as you'll get two calls, the state going HIGH (button press) and LOW (button release)
        Display.select_button.addListener(event -> {
            if (event.state().isHigh()) {
                System.out.println("SELECT Pressed");
                selectPressed(Display, api);
            }
        });

         Display.up_button.addListener(event -> {
            if (event.state().isHigh()) {
                System.out.println("UP Pressed");
                selectPressed(Display, api);
            }
        });

        Display.down_button.addListener(event -> {
            if (event.state().isHigh()) {
                System.out.println("DOWN Pressed");
                selectPressed(Display, api);
            }
        });

        Display.left_button.addListener(event -> {
             if (event.state().isHigh()) {
                System.out.println("LEFT Pressed");
                selectPressed(Display, api);
             }
        });
    
        Display.right_button.addListener(event -> {
            if (event.state().isHigh()) {
                System.out.println("RIGHT Pressed");
                selectPressed(Display, api);
            }
        });

    }  
    
    public static void main(String[] args) {
        try {
            AgileAPI api = new AgileAPI(4);
            apiThread =  api;
            System.out.println(api.GetCurrentPrice());
            System.out.println(String.format("The thread state is %s", apiThread.getState()));
            try {
                RGB1602 Display = new RGB1602(2, 16);
                Display.displayInit();
                Display.clearDisplay();
                System.out.println("Building button listeners");
                buildButtons(Display, api);
                Ticker Ticker = new Ticker();             
                Ticker.setAPI(api);
                Ticker.setDisplay(Display);
                
                // add the shutdown hooks.
                ShutDownHook shutDownHook = new ShutDownHook(api, Display);
                Thread shutDownHookThread = new Thread(shutDownHook);
                Runtime.getRuntime().addShutdownHook(shutDownHookThread);;

                // start the Ticker - this is the last (only non-daemon thread)
                TickerThread = Ticker;
                Ticker.start();
                   
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