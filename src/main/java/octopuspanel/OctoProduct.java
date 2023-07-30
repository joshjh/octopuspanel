package octopuspanel;

public class OctoProduct {
    public String code, full_name, display_name, description;
    public Boolean is_variable, is_green, is_tracker, is_prepay, is_business, is_restrcted;
    public int term;
    public String brand, available_from, available_to;

    public String toString() {
        return code + display_name;
    }
    }
