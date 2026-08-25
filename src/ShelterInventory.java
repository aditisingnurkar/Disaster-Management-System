public class ShelterInventory extends Shelter_Inventory {
    public ShelterInventory() { super(); }
    public ShelterInventory(int shelterId, int resourceId, int quantity) { super(shelterId, resourceId, quantity); }
    public ShelterInventory(int shelterId, int resourceId, int quantity, String shelterName, String resourceName, String unit) {
        super(shelterId, resourceId, quantity, shelterName, resourceName, unit);
    }
}
