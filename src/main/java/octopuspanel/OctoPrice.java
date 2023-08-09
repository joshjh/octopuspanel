package octopuspanel;
import java.time.*;
import java.util.Objects;

public class OctoPrice {
    public ZonedDateTime StartTime, EndTime;
    public ZonedDateTime priceCreateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
    public double UnitPrice;

    public String toString() {
        return StartTime + "::" + EndTime + ":" + UnitPrice + " created: :" + priceCreateTime;
    }
    
    @Override
    public int hashCode() {
        // priceCreateTime is left out; this will differ slightly potentially depending on the amount of time it would take to run the GetAPIData function.
        // what we want is hashCode and .equals() to compare prices that are the same and have the time start and end time, not the create time.

        return Objects.hash(StartTime, EndTime, UnitPrice);
    }
    @Override
    public boolean equals(Object otherobject) {
        // == on objects should cause two objects that share an address on the heap to compare true
        if (this == otherobject) {return true;};

        if (!otherobject.getClass().isInstance(this)) {return false;}

        // its safe to do this because we would have returned false already if the object was anything other that an OctoPrice object
        OctoPrice otherOctoObject = (OctoPrice) otherobject;
        if (otherOctoObject.StartTime.equals(this.StartTime) && (otherOctoObject.EndTime.equals(this.EndTime)) && (otherOctoObject.UnitPrice == this.UnitPrice)) {return true;}

        else {return false;}
    }


} 
    