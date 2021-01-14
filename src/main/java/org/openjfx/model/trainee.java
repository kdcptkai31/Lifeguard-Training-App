package org.openjfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a trainee, should include all personal information, as well as any comments on performance,
 */
public class trainee implements Serializable {

    //All data is in order of the google forms trainees fill out.

    //Initial Information
    private String           firstName;
    private String           middleName;
    private String           lastName;
    private Date             birthDate;
    private String           city;
    private String           state;
    private String           phoneNumber;
    private String           email;
    private String           districtChoice;
    private boolean          isLodging;
    private EmergencyContact emergencyContact;

    //Extra Information
    public boolean isQuestionnaire1Complete;
    public boolean isQuestionnaire2Complete;

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

    /**
     * Constructor
     */
    public trainee(){



    }


}
