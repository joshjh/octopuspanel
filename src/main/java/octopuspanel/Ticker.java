package octopuspanel;

import java.io.IOException;
import java.net.URISyntaxException;

import com.raspb.RGB1602;

public class Ticker extends Thread{
    static int screenMoveDelay = 10000;
    static int currentScreen = 0;
    private RGB1602 Display;
    private AgileAPI api;
    
    public void ShowCurrentPrice(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
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

    public void ShowCheapest30Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Cheapest 30 minutes:".toCharArray();
        char[] secondline = api.CheapestSegment().toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }
       
    public void ShowNext30Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Next 30 minutes:".toCharArray();
        char[] secondline = String.valueOf(api.NextPrice()).toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }

    public void ShowNext60Mins(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Next 60 minutes:".toCharArray();
        char[] secondline = String.valueOf(api.NextHour()).toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }
    public void ShowCheapest3Segements(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Cheap 3 Segs:".toCharArray();
        char[] secondline = api.Cheapest_3Run().toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }

    public void showCheapestTonight(RGB1602 Display, AgileAPI api, int delay) throws InterruptedException {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "Cheapest Tonight:".toCharArray();
        char[] secondline = api.CheapestTonight().toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
        Thread.sleep(delay);
    }

    public void setDisplay(RGB1602 Display) {
        this.Display = Display;
    }

    public void setAPI(AgileAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        mainLoop();
    }

    private void megaDeathErrorHandler() {
        try {
            Display.lcdMegaDeathError();
        } catch (InterruptedException e) {
            try {
                api = api.restartAPI();
                Display.lcdAutoScroll(false);
                Display.clearDisplay();
                Display.lcdCursorHome();
                return;
            } catch (URISyntaxException | IOException e1) {
                // just catch it for now.  IOException is a failure to resolve/get (networking generally.  URI is fixed so shouldn't occur.)
                e1.printStackTrace();
            }
        }
    }

    private void mainLoop() {
        while (true) {

            if (!api.apiWarnFlag) {
            nextScreen(Display, api);
            }

            else {
                System.out.print("API INTERRUPT");
                megaDeathErrorHandler();
            }
            mainLoop();
        }
    }

    public void nextScreen(RGB1602 Display, AgileAPI api) {
        
        try {
            
            // if current screen is > 5 reset it to 0, or do nothing
            currentScreen = (currentScreen > 5) ? currentScreen = 0: currentScreen;
     
            switch (currentScreen) {
                
                case 0:
                    ShowCurrentPrice(Display, api, screenMoveDelay);
                    break;
                case 1:
                    ShowCheapest30Mins(Display, api, screenMoveDelay);
                    break;
                case 2:
                    ShowNext30Mins(Display, api, screenMoveDelay);
                    break;
                case 3:
                    ShowNext60Mins(Display, api, screenMoveDelay);
                    break;
                case 4:
                    ShowCheapest3Segements(Display, api, screenMoveDelay);
                    break;
                case 5:
                    showCheapestTonight(Display, api, screenMoveDelay);
                    break;
            }
            currentScreen++;
        }
        // this InterruptedException is triggered by the Ticker Thread being interupted in sleep, normally by the button handlers.
        catch (InterruptedException e) {
            System.out.println("INTERRUPTED");
            currentScreen++;
            nextScreen(Display, api);
        }
        
    }

}
