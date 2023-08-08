package octopuspanel;

import java.util.Objects;

public class OctoProduct {
    public String code, full_name, display_name, description;
    public Boolean is_variable, is_green, is_tracker, is_prepay, is_business, is_restricted;
    public int term;
    // TODO the dates need to be ZonedDateTime
    public String brand, available_from, available_to;

    @Override
    public String toString() {
        return code + display_name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, full_name, display_name, description, is_variable, is_green, is_tracker, is_business, is_prepay, is_restricted, term, brand, available_from, available_to);
    }

    @Override
    public boolean equals(Object object) {
        // if it's a copy on the heap
        if (object == this) {return true;}

        if (!this.getClass().isInstance(object)) {return false;}

        OctoProduct objectOctoProduct = (OctoProduct) object;
        
        if (objectOctoProduct.available_from.equals(this.available_from) && (objectOctoProduct.available_to.equals(this.available_to)) && (objectOctoProduct.brand.equals(this.brand)) &&
        (objectOctoProduct.code.equals(this.code)) && (objectOctoProduct.description.equals(this.description)) && (objectOctoProduct.display_name.equals(this.display_name)) &&
        (objectOctoProduct.full_name.equals(this.full_name)) && (objectOctoProduct.term == this.term) && (objectOctoProduct.is_business.equals(this.is_business)) && (objectOctoProduct.is_green.equals(this.is_green)) &&
        (objectOctoProduct.is_prepay.equals(this.is_prepay)) && (objectOctoProduct.is_restricted.equals(this.is_restricted)) && (objectOctoProduct.is_tracker.equals(this.is_tracker)) && (objectOctoProduct.is_variable.equals(this.is_variable))
        ) {return true;}
        return false;
    }
    }
