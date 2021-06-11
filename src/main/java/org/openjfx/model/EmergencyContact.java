package org.openjfx.model;

/**
 * Represents an Emergency Contact object. Full information about the associated Trainee's contact.
 */
public class EmergencyContact{

    //Information
    private int traineeID;
    private String fullName;
    private String relationship;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipcode;

    /**
     * Default Constructor
     */
    public EmergencyContact(){}

    /**
     * Constructor
     * @param tid
     * @param fName
     * @param relation
     * @param pNumber
     * @param add
     * @param city
     * @param state
     * @param zip
     */
    public EmergencyContact(int tid, String fName, String relation, String pNumber, String add, String city,
                            String state, String zip){

        traineeID = tid;
        fullName = fName;
        relationship = relation;
        phoneNumber = pNumber;
        address = add;
        this.city = city;
        this.state = state;
        zipcode = zip;

    }

    public EmergencyContact(String fName, String relation, String pNumber, String add, String city, String state,
                            String zip){

        this(0, fName, relation, pNumber, add, city, state, zip);

    }

    //Getters
    public int getTraineeID() {
        return traineeID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    //Setters
    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
