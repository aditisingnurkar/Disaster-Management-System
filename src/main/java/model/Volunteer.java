public class Volunteer {
    private int volunteerId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String skills;
    private String availabilityStatus; // 'Available', 'Assigned', 'On Leave'

    public Volunteer() {}

    public Volunteer(int volunteerId, String name, int age, String gender, String contactNumber, String skills, String availabilityStatus) {
        this.volunteerId = volunteerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.skills = skills;
        this.availabilityStatus = availabilityStatus;
    }

    public Volunteer(String name, int age, String gender, String contactNumber, String skills, String availabilityStatus) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.skills = skills;
        this.availabilityStatus = availabilityStatus;
    }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    @Override
    public String toString() {
        return "Volunteer #" + volunteerId + " (" + name + " - " + availabilityStatus + ")";
    }
}
