public class Resource {
    private int resourceId;
    private String resourceName;
    private String description;
    private String unit;

    public Resource() {}

    public Resource(int resourceId, String resourceName, String description, String unit) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.description = description;
        this.unit = unit;
    }

    public Resource(String resourceName, String description, String unit) {
        this.resourceName = resourceName;
        this.description = description;
        this.unit = unit;
    }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    @Override
    public String toString() {
        return "Resource #" + resourceId + " (" + resourceName + " [" + unit + "])";
    }
}
