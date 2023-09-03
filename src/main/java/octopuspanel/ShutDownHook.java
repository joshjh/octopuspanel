package octopuspanel;
import com.raspb.RGB1602;



public class ShutDownHook extends Thread {
    AgileAPI api;
    RGB1602 Display;
    public ShutDownHook(AgileAPI api, RGB1602 Display) {
        this.Display = Display;
        this.api = api;
    } 

    public void showShutDownWarning(RGB1602 Display, AgileAPI api) {
        Display.lcdClearDisplay();
        Display.lcdSetCursor(0, 0);
        char[] firstline = "SHUT-DOWN ".toCharArray();
        char [] secondline = "GOODBYE".toCharArray();
        Display.lcdWrite(firstline);
        Display.lcdSetCursor(0, 1);
        Display.lcdWrite(secondline);
    }
    
    @Override
    public void run() {
        System.out.println("Calling Shutdown Hooks");
        Display.lcdShutDown();
    }


    
}
