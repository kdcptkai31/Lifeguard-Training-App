package org.openjfx.model;

import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * Represents a Trainee, should include all personal information, as well as any comments on performance,
 */
public class Trainee implements Serializable {

    private int id;
    //Initial Information
    private String           firstName;
    private String           middleName;
    private String           lastName;
    private String           birthDate;
    private String           city;
    private String           state;
    private String           phoneNumber;
    private String           email;
    private String           districtChoice;
    private boolean          isLodging;
    private EmergencyContact emergencyContact;

    private int hoursAttended;
    private Image image;

    //Extra Information
    private boolean isQuestionnaire1Complete;
    private boolean isQuestionnaire2Complete;
    private boolean isActive; //False if Trainee failed
    private int    year;
    private int    session;

    //Questionnaire 1 Information
    private String  shirtSize;
    private String  shortSize;
    private String  swimSuitSize;
    private boolean isReturningTrainee;
    private String  whyReturning;
    private String  whyBeStateLG;
    private String  whatWantLearnTraining;
    private boolean isJG;
    private String  jgInfo;
    private boolean isOpenWaterLG;
    private String  openWaterLGInfo;
    private boolean isPoolLG;
    private String  poolLGInfo;
    private boolean isEMT;
    private String  emtInfo;
    private boolean isOtherAdvancedMedicalTraining;
    private String  advancedMedicalTrainingInfo;
    private boolean isFirstJob;
    private String  jobExperienceInfo;
    private String  anyExtraInfo;

    //Questionnaire 2 Information
    private String  expectedBiggestTrainingChallengeInfo;
    private String  preparationInfo;
    private int     medicalConfidence;
    private int     cprConfidence;
    private int     physicalConfidence;
    private int     mentalConfidence;
    private int     preTrainingSeminarsAttended;
    private String  organizedSwimPoloFreq;
    private String  personalSwimFreq;
    private String  gymFreq;
    private String  oceanSwimFreq;
    private String  runningFreq;
    private String  surfingFreq;
    private boolean isDisabled;

    //Constructors

    /**
     * Default Constructor
     */
    public Trainee(){emergencyContact = null; image = null;}

    /**
     * Constructor
     * @param id
     * @param fName
     * @param mName
     * @param lName
     * @param bDay
     * @param city
     * @param state
     * @param pNumber
     * @param email
     * @param dChoice
     * @param isLodge
     * @param ec
     * @param hAttended
     * @param isQ1Com
     * @param isQ2Com
     * @param isA
     * @param year
     * @param session
     */
    public Trainee(int id, String fName, String mName, String lName, String bDay, String city, String state,
                   String pNumber, String email, String dChoice, boolean isLodge, EmergencyContact ec, int hAttended,
                   Image imag, boolean isQ1Com, boolean isQ2Com, boolean isA, int year, int session){

        this.id = id;
        firstName = fName;
        middleName = mName;
        lastName = lName;
        birthDate = bDay;
        this.city = city;
        this.state = state;
        phoneNumber = pNumber;
        this.email = email;
        districtChoice = dChoice;
        isLodging = isLodge;
        emergencyContact = ec;
        hoursAttended = hAttended;
        image = imag;
        isQuestionnaire1Complete = isQ1Com;
        isQuestionnaire2Complete = isQ2Com;
        isActive = isA;
        this.year = year;
        this.session = session;

    }

    /**
     * Initial Info Constructor: No id parameter, as it has yet to be produced by the db
     * @param fName
     * @param mName
     * @param lName
     * @param bDay
     * @param city
     * @param state
     * @param pNumber
     * @param email
     * @param dChoice
     * @param isLodge
     * @param ec
     * @param hAttended
     * @param isQ1Com
     * @param isQ2Com
     * @param isA
     * @param year
     * @param session
     */
    public Trainee(String fName, String mName, String lName, String bDay, String city, String state,
                   String pNumber, String email, String dChoice, boolean isLodge, EmergencyContact ec, int hAttended,
                   boolean isQ1Com, boolean isQ2Com, boolean isA, int year, int session){

        this(0, fName, mName, lName, bDay, city, state, pNumber, email, dChoice, isLodge, ec, hAttended, null,
                isQ1Com, isQ2Com, isA, year, session);

    }

    //Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDistrictChoice() {
        return districtChoice;
    }

    public boolean isLodging() {
        return isLodging;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public int getHoursAttended() {
        return hoursAttended;
    }

    public Image getImage() {return image;}

    public boolean isQuestionnaire1Complete() {
        return isQuestionnaire1Complete;
    }

    public boolean isQuestionnaire2Complete() {
        return isQuestionnaire2Complete;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getYear() {
        return year;
    }

    public int getSession() {
        return session;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public String getShortSize() {
        return shortSize;
    }

    public String getSwimSuitSize() {
        return swimSuitSize;
    }

    public boolean isReturningTrainee() {
        return isReturningTrainee;
    }

    public String getWhyReturning() {
        return whyReturning;
    }

    public String getWhyBeStateLG() {
        return whyBeStateLG;
    }

    public String getWhatWantLearnTraining() {
        return whatWantLearnTraining;
    }

    public boolean isJG() {
        return isJG;
    }

    public String getJgInfo() {
        return jgInfo;
    }

    public boolean isOpenWaterLG() {
        return isOpenWaterLG;
    }

    public String getOpenWaterLGInfo() {
        return openWaterLGInfo;
    }

    public boolean isPoolLG() {
        return isPoolLG;
    }

    public String getPoolLGInfo() {
        return poolLGInfo;
    }

    public boolean isEMT() {
        return isEMT;
    }

    public String getEmtInfo() {
        return emtInfo;
    }

    public boolean isOtherAdvancedMedicalTraining() {
        return isOtherAdvancedMedicalTraining;
    }

    public String getAdvancedMedicalTrainingInfo() {
        return advancedMedicalTrainingInfo;
    }

    public boolean isFirstJob() {
        return isFirstJob;
    }

    public String getJobExperienceInfo() {
        return jobExperienceInfo;
    }

    public String getAnyExtraInfo() {
        return anyExtraInfo;
    }

    public String getExpectedBiggestTrainingChallengeInfo() {
        return expectedBiggestTrainingChallengeInfo;
    }

    public String getPreparationInfo() {
        return preparationInfo;
    }

    public int getMedicalConfidence() {
        return medicalConfidence;
    }

    public int getCprConfidence() {
        return cprConfidence;
    }

    public int getPhysicalConfidence() {
        return physicalConfidence;
    }

    public int getMentalConfidence() {
        return mentalConfidence;
    }

    public int getPreTrainingSeminarsAttended() {
        return preTrainingSeminarsAttended;
    }

    public String getOrganizedSwimPoloFreq() {
        return organizedSwimPoloFreq;
    }

    public String getPersonalSwimFreq() {
        return personalSwimFreq;
    }

    public String getGymFreq() {
        return gymFreq;
    }

    public String getOceanSwimFreq() {
        return oceanSwimFreq;
    }

    public String getRunningFreq() {
        return runningFreq;
    }

    public String getSurfingFreq() {
        return surfingFreq;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDistrictChoice(String districtChoice) {
        this.districtChoice = districtChoice;
    }

    public void setLodging(boolean lodging) {
        isLodging = lodging;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setHoursAttended(int hoursAttended) {
        this.hoursAttended = hoursAttended;
    }

    public void setImage(Image i){image = i;}

    public void setQuestionnaire1Complete(boolean questionnaire1Complete) {
        isQuestionnaire1Complete = questionnaire1Complete;
    }

    public void setQuestionnaire2Complete(boolean questionnaire2Complete) {
        isQuestionnaire2Complete = questionnaire2Complete;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public void setShortSize(String shortSize) {
        this.shortSize = shortSize;
    }

    public void setSwimSuitSize(String swimSuitSize) {
        this.swimSuitSize = swimSuitSize;
    }

    public void setReturningTrainee(boolean returningTrainee) {
        isReturningTrainee = returningTrainee;
    }

    public void setWhyReturning(String whyReturning) {
        this.whyReturning = whyReturning;
    }

    public void setWhyBeStateLG(String whyBeStateLG) {
        this.whyBeStateLG = whyBeStateLG;
    }

    public void setWhatWantLearnTraining(String whatWantLearnTraining) {
        this.whatWantLearnTraining = whatWantLearnTraining;
    }

    public void setJG(boolean JG) {
        isJG = JG;
    }

    public void setJgInfo(String jgInfo) {
        this.jgInfo = jgInfo;
    }

    public void setOpenWaterLG(boolean openWaterLG) {
        isOpenWaterLG = openWaterLG;
    }

    public void setOpenWaterLGInfo(String openWaterLGInfo) {
        this.openWaterLGInfo = openWaterLGInfo;
    }

    public void setPoolLG(boolean poolLG) {
        isPoolLG = poolLG;
    }

    public void setPoolLGInfo(String poolLGInfo) {
        this.poolLGInfo = poolLGInfo;
    }

    public void setEMT(boolean EMT) {
        isEMT = EMT;
    }

    public void setEmtInfo(String emtInfo) {
        this.emtInfo = emtInfo;
    }

    public void setOtherAdvancedMedicalTraining(boolean otherAdvancedMedicalTraining) {
        isOtherAdvancedMedicalTraining = otherAdvancedMedicalTraining;
    }

    public void setAdvancedMedicalTrainingInfo(String advancedMedicalTrainingInfo) {
        this.advancedMedicalTrainingInfo = advancedMedicalTrainingInfo;
    }

    public void setFirstJob(boolean firstJob) {
        isFirstJob = firstJob;
    }

    public void setJobExperienceInfo(String jobExperienceInfo) {
        this.jobExperienceInfo = jobExperienceInfo;
    }

    public void setAnyExtraInfo(String anyExtraInfo) {
        this.anyExtraInfo = anyExtraInfo;
    }

    public void setExpectedBiggestTrainingChallengeInfo(String expectedBiggestTrainingChallengeInfo) {
        this.expectedBiggestTrainingChallengeInfo = expectedBiggestTrainingChallengeInfo;
    }

    public void setPreparationInfo(String preparationInfo) {
        this.preparationInfo = preparationInfo;
    }

    public void setMedicalConfidence(int medicalConfidence) {
        this.medicalConfidence = medicalConfidence;
    }

    public void setCprConfidence(int cprConfidence) {
        this.cprConfidence = cprConfidence;
    }

    public void setPhysicalConfidence(int physicalConfidence) {
        this.physicalConfidence = physicalConfidence;
    }

    public void setMentalConfidence(int mentalConfidence) {
        this.mentalConfidence = mentalConfidence;
    }

    public void setPreTrainingSeminarsAttended(int preTrainingSeminarsAttended) {
        this.preTrainingSeminarsAttended = preTrainingSeminarsAttended;
    }

    public void setOrganizedSwimPoloFreq(String organizedSwimPoloFreq) {
        this.organizedSwimPoloFreq = organizedSwimPoloFreq;
    }

    public void setPersonalSwimFreq(String personalSwimFreq) {
        this.personalSwimFreq = personalSwimFreq;
    }

    public void setGymFreq(String gymFreq) {
        this.gymFreq = gymFreq;
    }

    public void setOceanSwimFreq(String oceanSwimFreq) {
        this.oceanSwimFreq = oceanSwimFreq;
    }

    public void setRunningFreq(String runningFreq) {
        this.runningFreq = runningFreq;
    }

    public void setSurfingFreq(String surfingFreq) {
        this.surfingFreq = surfingFreq;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

}
