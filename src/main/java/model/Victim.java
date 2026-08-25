public class Victim {
    private int victimId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String medicalCondition;
    private int disasterId;
    private Integer shelterId; // nullable

    public Victim() {}

    public Victim(int victimId, String name, int age, String gender, String contactNumber, String medicalCondition, int disasterId, Integer shelterId) {
        this.victimId = victimId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalCondition = medicalCondition;
        this.disasterId = disasterId;
        this.shelterId = shelterId;
    }

    public Victim(String name, int age, String gender, String contactNumber, String medicalCondition, int disasterId, Integer shelterId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalCondition = medicalCondition;
        this.disasterId = disasterId;
        this.shelterId = shelterId;
    }

    public int getVictimId() { return victimId; }
    public void setVictimId(int victimId) { this.victimId = victimId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    public int getDisasterId() { return disasterId; }
    public void setDisasterId(int disasterId) { this.disasterId = disasterId; }

    public Integer getShelterId() { return shelterId; }
    public void setShelterId(Integer shelterId) { this.shelterId = shelterId; }

    @Override
    public String toString() {
        return "Victim #" + victimId + " (" + name + ", " + age + "y)";
    }
}
