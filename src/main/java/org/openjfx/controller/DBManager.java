package org.openjfx.controller;

import java.sql.*;

public class DBManager {

    private static Connection connection = getConnection();

    /**
     * Constructor
     */
    public DBManager(){

        checkIfDatabaseExists();

    }

    /**
     * Creates a connection to the database file.
     * @return
     */
    public static Connection getConnection(){

        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/database/app.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static void checkIfDatabaseExists(){

        //Creates a new sessions table
        String sqlSessions = "CREATE TABLE IF NOT EXISTS sessions (\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "PRIMARY KEY (year, session)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlSessions);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new districts table
        String sqlDistricts = "CREATE TABLE IF NOT EXISTS districts (\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "district TEXT,\n"
                + "superEmail TEXT,\n"
                + "FOREIGN KEY(year, session) REFERENCES sessions(year, session),\n"
                + "PRIMARY KEY(year, session)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlDistricts);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new instructors table
        String sqlInstructors = "CREATE TABLE IF NOT EXISTS instructors (\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "name TEXT,\n"
                + "FOREIGN KEY(year, session) REFERENCES sessions(year, session),\n"
                + "PRIMARY KEY(year, session, name)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlInstructors);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new trainees table
        String sqlTrainee = "CREATE TABLE IF NOT EXISTS trainees (\n"
                + "tid INTEGER,\n"
                + "firstName TEXT,\n"
                + "middleName TEXT,\n"
                + "lastName TEXT,\n"
                + "birthDate TEXT,\n"
                + "city TEXT,\n"
                + "state TEXT,\n"
                + "phoneNumber TEXT,\n"
                + "email TEXT,\n"
                + "districtChoice TEXT,\n"
                + "isLodging INTEGER,\n"
                + "hoursAttended INTEGER,\n"
                + "isQuestionnaire1Complete INTEGER,\n"
                + "isQuestionnaire2Complete INTEGER,\n"
                + "isActive INTEGER,\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"//End of Initial Info
                + "shirtSize TEXT,\n"
                + "shortSize TEXT,\n"
                + "swimSuitSize TEXT,\n"
                + "isReturningTrainee INTEGER,\n"
                + "whyReturning TEXT,\n"
                + "whyBeStateLG TEXT,\n"
                + "whatWantLearnTraining TEXT,\n"
                + "isJG INTEGER,\n"
                + "jgInfo TEXT,\n"
                + "isOpenWaterLG INTEGER,\n"
                + "openWaterLGInfo TEXT,\n"
                + "isPoolLG INTEGER,\n"
                + "poolLGInfo TEXT,\n"
                + "isEMT INTEGER,\n"
                + "emtInfo TEXT,\n"
                + "isOtherAdvancedMedicalTraining INTEGER,\n"
                + "advancedMedicalTrainingInfo TEXT,\n"
                + "isFirstJob INTEGER,\n"
                + "jobExperienceInfo TEXT,\n"
                + "anyExtraInfo TEXT,\n"//End of Questionnaire 1 Info
                + "expectedBiggestTrainingChallengeInfo TEXT,\n"
                + "preparationInfo TEXT,\n"
                + "medicalConfidence INTEGER,\n"
                + "cprConfidence INTEGER,\n"
                + "physicalConfidence INTEGER,\n"
                + "mentalConfidence INTEGER,\n"
                + "preTrainingSeminarsAttended INTEGER,\n"
                + "organizedSwimPoloFreq TEXT,\n"
                + "personalSwimFreq TEXT,\n"
                + "gymFreq TEXT,\n"
                + "oceanSwimFreq TEXT,\n"
                + "runningFreq TEXT,\n"
                + "surfingFreq TEXT,\n"
                + "isDisabled INTEGER,\n"//End of Questionnaire 2 Info
                + "PRIMARY KEY(tid)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlTrainee);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new emergency contacts table
        String sqlEmergencyContacts = "CREATE TABLE IF NOT EXISTS emergencyContacts (\n"
                + "traineeID INTEGER,\n"
                + "fullName TEXT,\n"
                + "relationship TEXT,\n"
                + "phoneNumber TEXT,\n"
                + "address TEXT,\n"
                + "city TEXT,\n"
                + "state TEXT,\n"
                + "zipcode TEXT,\n"
                + "FOREIGN KEY(traineeID) REFERENCES trainees(tid),\n"
                + "PRIMARY KEY (traineeID, fullName)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlEmergencyContacts);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new comments table
        String sqlComments = "CREATE TABLE IF NOT EXISTS comments (\n"
                + "traineeID INTEGER,\n"
                + "commentID INTEGER,\n"
                + "date TEXT,\n"
                + "rotation TEXT,\n"
                + "instructorName TEXT,\n"
                + "incidentType TEXT,\n"
                + "incidentDescription TEXT,\n"
                + "instructorActions TEXT,\n"
                + "nextSteps TEXT,\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "FOREIGN KEY(traineeID) REFERENCES trainees(tid),\n"
                + "PRIMARY KEY (traineeID, commentID)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlComments);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new tests table
        String sqlTest = "CREATE TABLE IF NOT EXISTS tests (\n"
                + "testID INTEGER,\n"
                + "name TEXT,\n"
                + "points INTEGER,\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "PRIMARY KEY (testID)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlTest);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new test scores table
        String sqlTestScores = "CREATE TABLE IF NOT EXISTS testScores (\n"
                + "testID INTEGER,\n"
                + "traineeID INTEGER,\n"
                + "score INTEGER,\n"
                + "FOREIGN KEY(testID) REFERENCES tests(testID),\n"
                + "FOREIGN KEY(traineeID) REFERENCES trainees(tid),\n"
                + "PRIMARY KEY (testID, traineeID)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlTestScores);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new events table
        String sqlEvents = "CREATE TABLE IF NOT EXISTS events (\n"
                + "eventID INTEGER,\n"
                + "name TEXT,\n"
                + "points INTEGER,\n"
                + "notes TEXT,\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "PRIMARY KEY (eventID)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlEvents);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Creates a new event scores table
        String sqlEventScores = "CREATE TABLE IF NOT EXISTS eventScores (\n"
                + "eventID INTEGER,\n"
                + "traineeID INTEGER,\n"
                + "score INTEGER,\n"
                + "FOREIGN KEY(eventID) REFERENCES events(eventID),\n"
                + "FOREIGN KEY(traineeID) REFERENCES trainees(tid),\n"
                + "PRIMARY KEY (eventID, traineeID)"
                + ");";

        try{
            Statement stmt = connection.createStatement();
            stmt.execute(sqlEventScores);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
