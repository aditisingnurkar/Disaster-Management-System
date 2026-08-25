public class Shelter_Inventory {
    private int shelterId;
    private int resourceId;
    private int quantity;

    // Joined display fields
    private String shelterName;
    private String resourceName;
    private String unit;

    public Shelter_Inventory() {}

    public Shelter_Inventory(int shelterId, int resourceId, int quantity) {
        this.shelterId = shelterId;
        this.resourceId = resourceId;
        this.quantity = quantity;
    }

    public Shelter_Inventory(int shelterId, int resourceId, int quantity, String shelterName, String resourceName, String unit) {
        this.shelterId = shelterId;
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.shelterName = shelterName;
        this.resourceName = resourceName;
        this.unit = unit;
    }

    public int getShelterId() { return shelterId; }
    public void setShelterId(int shelterId) { this.shelterId = shelterId; }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    @Override
    public String toString() {
        return "Inventory [Shelter " + shelterId + ", Resource " + resourceId + ": " + quantity + "]";
    }
}
