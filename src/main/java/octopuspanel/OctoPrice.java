package octopuspanel;
import java.time.*;

public class OctoPrice {
    public ZonedDateTime StartTime, EndTime;
    public double UnitPrice;

    public String toString() {
        return StartTime + "::" + EndTime + ":" + UnitPrice;
    }

    } 
    