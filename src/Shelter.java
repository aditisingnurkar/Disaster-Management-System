public class Shelter {
    private int shelterId;
    private String shelterName;
    private String address;
    private int capacity;
    private String coordinatorName;
    private String contactNumber;
    private int disasterId;

    public Shelter() {}

    public Shelter(int shelterId, String shelterName, String address, int capacity, String coordinatorName, String contactNumber, int disasterId) {
        this.shelterId = shelterId;
        this.shelterName = shelterName;
        this.address = address;
        this.capacity = capacity;
        this.coordinatorName = coordinatorName;
        this.contactNumber = contactNumber;
        this.disasterId = disasterId;
    }

    public Shelter(String shelterName, String address, int capacity, String coordinatorName, String contactNumber, int disasterId) {
        this.shelterName = shelterName;
        this.address = address;
        this.capacity = capacity;
        this.coordinatorName = coordinatorName;
        this.contactNumber = contactNumber;
        this.disasterId = disasterId;
    }

    public int getShelterId() { return shelterId; }
    public void setShelterId(int shelterId) { this.shelterId = shelterId; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getCoordinatorName() { return coordinatorName; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getDisasterId() { return disasterId; }
    public void setDisasterId(int disasterId) { this.disasterId = disasterId; }

    @Override
    public String toString() {
        return "Shelter #" + shelterId + " (" + shelterName + " - Cap: " + capacity + ")";
    }
}
