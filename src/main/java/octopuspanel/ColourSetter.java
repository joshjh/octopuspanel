package octopuspanel;

public class ColourSetter {
      
    
    /** 
     * This function takes a double as the current price, and will return a RGB value array to allow the color of the LCD to change, based on the current price.
     * Over 30p/kwh is red zone
     * 20 - 30 amber colour
     * 10 - 20 blue
     * < 10 - green
     * 
     * @param price
     * @return int[]
     */
    public static int[] GetColour(double price) {
        // TODO replace this with a sliding scale of colours?
        int[] colour = {0, 0, 0};
        
        //red
        if (price > 30D){
            colour[0] = 255;
        }

        //orange
        else if (price >=20 & price <=30) {
            colour[0] = 255;
            colour[1] = 170;
            colour[2] = 0;
        }
        // blue
        else if (price >=10D & price <=20D) {
            colour[0] = 60;
            colour[1] = 60;
            colour[2] = 250;
        }
        // green
        else if (price <=10D ) {
            colour[0] = 1;
            colour[1] = 250;
            colour[2] = 1;
        }
        return colour;
       
        
    }
}
