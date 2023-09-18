package octopuspanel;

public abstract class ColourSetter {
      
    
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

    
    /** Return a int[][] with orange, blue and green in sets of three.  ie: [[255,170,0], [60, 60, 250], etc]
     * @return int[][]
     */
    public static int[][] PlungeColours() {
            int[] colour = new int[9];
            //orange
            colour[0] = 255;
            colour[1] = 170;
            colour[2] = 0;
           // blue
            colour[3] = 60;
            colour[4] = 60;
            colour[5] = 250;
            // green
            colour[6] = 1;
            colour[7] = 250;
            colour[8] = 1;
            int[][] colour_array_array = {{colour[0], colour[1], colour[2]}, {colour[3], colour[4], colour[5]}, {colour[6], colour[7], colour[8]}};
            return colour_array_array;
    }
}
