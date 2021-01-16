package org.openjfx.controller;

import javafx.util.Pair;
import org.openjfx.model.*;

import java.io.File;
import java.sql.*;
import java.util.Vector;

public class DBManager {

    private static Connection connection = getConnection();

    /**
     * Constructor
     */
    public DBManager(){

        checkIfDatabaseExists();

    }

    /**
     * Creates needed tables in the database if they do not exist already.
     */
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
                + "INTEGER,\n"//End of Questionnaire 2 Info
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

        //Creates a new Test scores table
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

        //Creates a new Event scores table
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

    /**********************************************************************
     ******************************GETTERS*********************************
     **********************************************************************/

    /**
     * Creates a connection to the database file.
     * @return
     */
    public static Connection getConnection(){

        try{

            File file = new File(System.getProperty("user.dir") + "\\Save_Files\\");
            if(!file.exists()){
                if(!file.mkdir())
                    System.exit(1);
            }
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:Save_Files/app.db"); //src/main/database/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** TRAINEE ***************************************
     */

    /**
     * Returns all trainees from a given year and session.
     * @param year
     * @param session
     * @return
     */
    public static Vector<Trainee> getAllTraineesFromSession(int year, int session){

        String sql = "SELECT * FROM trainees WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            ResultSet rs = stmt.executeQuery();

            return getTraineesHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns all trainees from a given year, session, and district.
     * @param year
     * @param session
     * @param district
     * @return
     */
    public static Vector<Trainee> getAllTraineesFromSessionAndDistrict(int year, int session, String district){

        String sql = "SELECT * FROM trainees WHERE year = ? AND session = ? AND districtChoice = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            stmt.setString(3, district);
            ResultSet rs = stmt.executeQuery();

            return getTraineesHelper(rs);

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** Comment ***************************************
     */

    /**
     * Returns all comments from a specific day
     * @param date
     * @return
     */
    public static Vector<Comment> getAllCommentsFromDay(int date){

        String sql = "SELECT * FROM comments WHERE date = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, date);
            ResultSet rs = stmt.executeQuery();

            return getCommentsHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns all comments from a given year and session.
     * @param yearNum
     * @param sessionNum
     * @return
     */
    public static Vector<Comment> getAllCommentsFromSession(int yearNum, int sessionNum){

        String sql = "SELECT * FROM comments WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, yearNum);
            stmt.setInt(2, sessionNum);
            ResultSet rs = stmt.executeQuery();

            return getCommentsHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** TEST ***************************************
     */

    /**
     * Returns all tests from a given year and session.
     * @param year
     * @param session
     * @return
     */
    public static Vector<Test> getAllTestsFromSession(int year, int session){

        String sql = "SELECT * FROM tests WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            ResultSet rs = stmt.executeQuery();

            return getTestsHelper(rs);

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** TEST SCORES ***************************************
     */

    /**
     * Returns all Test scores from a given Test.
     * @param testID
     * @return
     */
    public static Vector<TestScore> getAllTestScoresFromTestID(int testID){

        String sql = "SELECT * FROM testScores WHERE testID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, testID);
            ResultSet rs = stmt.executeQuery();

            return getTestScoresHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns all Test scores from a given trainee.
     * @param tID
     * @return
     */
    public static Vector<TestScore> getAllTestScoresFromTraineeID(int tID){

        String sql = "SELECT * FROM testScores WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tID);
            ResultSet rs = stmt.executeQuery();

            return getTestScoresHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** EVENT ***************************************
     */

    /**
     * Returns all events from a given year and session.
     * @param year
     * @param session
     * @return
     */
    public static Vector<Event> getAllEventsFromSession(int year, int session){

        String sql = "SELECT * FROM events WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            ResultSet rs = stmt.executeQuery();

            return getEventsHelper(rs);

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** EVENT SCORES ***************************************
     */

    /**
     * Returns all event scores from a given trainee id.
     * @param tID
     * @return
     */
    public static Vector<EventScore> getAllEventScoresFromTraineeID(int tID){

        String sql = "SELECT * FROM eventScores WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tID);
            ResultSet rs = stmt.executeQuery();

            return getEventScoreHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns all event scores from a given event.
     * @param eID
     * @return
     */
    public static Vector<EventScore> getAllEventScoresFromEventID(int eID){

        String sql = "SELECT * FROM eventScores WHERE eventID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, eID);
            ResultSet rs = stmt.executeQuery();

            return getEventScoreHelper(rs);


        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** Session ***************************************
     */

    /**
     * Returns the most current session, used to initialize the application.
     * @return
     */
    public static Pair<Integer, Integer> getCurrentSession(){

        String sql = "SELECT year, session FROM sessions";
        try{
            //Extract all years and sessions
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Vector<Pair<Integer, Integer>> results = new Vector<>();
            while(rs.next()) {
                Pair<Integer, Integer> tmp = new Pair<>(rs.getInt("year"), rs.getInt("session"));
                results.add(tmp);
            }
            //Find newest session
            Pair<Integer, Integer> maxPair = new Pair<>(0, 0);
            for(int i = 0; i < results.size(); i++){

                if(results.elementAt(i).getKey() >= maxPair.getKey()){
                    if(results.elementAt(i).getValue() > maxPair.getValue())
                        maxPair = results.elementAt(i);
                }

            }
            if(maxPair.getKey() != 0 && maxPair.getValue() != 0)
                return maxPair;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Adds a new year and session to the db.
     * @param newYear
     * @param newSession
     * @return
     */
    public static boolean addNewSession(int newYear, int newSession){

        String sql = "INSERT INTO sessions(year, session) VALUES(?, ?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, newYear);
            stmt.setInt(2, newSession);
            stmt.executeUpdate();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** Districts ***************************************
     */

    /**
     * Returns all districts from a given year and session.
     * @param year
     * @param session
     * @return
     */
    public static Vector<District> getAllDistrictsFromSession(int year, int session){

        String sql = "SELECT * FROM districts WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            ResultSet rs = stmt.executeQuery();
            Vector<District> results = new Vector<>();

            while(rs.next())
                results.add(new District(rs.getInt("year"), rs.getInt("session"),
                                         rs.getString("district"), rs.getString("superEmail")));

            return results;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
     ****************************** Instructors ***************************************
     */

    /**
     * Returns all instructors from a given year and session.
     * @param year
     * @param session
     * @return
     */
    public static Vector<Instructor> getAllInstructorsFromSession(int year, int session){

        String sql = "SELECT * FROM instructors WHERE year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            ResultSet rs = stmt.executeQuery();
            Vector<Instructor> results = new Vector<>();

            while(rs.next())
                results.add(new Instructor(rs.getInt("year"), rs.getInt("session"),
                        rs.getString("name")));

            return results;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /*
    ****************************** HELPERS ***************************************
     */

    /**
     * Helper function to reduce repetition of code for turning the result set of trainees into a vector.
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Vector<Trainee> getTraineesHelper(ResultSet rs) throws SQLException{

        Vector<Trainee> results = new Vector<>();
        while(rs.next()){

            Trainee tmp = new Trainee();
            tmp.setId(rs.getInt("tid"));
            tmp.setFirstName(rs.getString("firstName"));
            tmp.setMiddleName(rs.getString("middleName"));
            tmp.setLastName(rs.getString("lastName"));
            tmp.setBirthDate(rs.getString("birthDate"));
            tmp.setCity(rs.getString("city"));
            tmp.setState(rs.getString("state"));
            tmp.setPhoneNumber(rs.getString("phoneNumber"));
            tmp.setEmail(rs.getString("email"));
            tmp.setDistrictChoice(rs.getString("districtChoice"));
            tmp.setLodging((1 == rs.getInt("isLodging")));

            //Get Emergency Contact
            String sqlEC = "SELECT * FROM emergencyContacts WHERE traineeID = ?";
            PreparedStatement stmtEC = connection.prepareStatement(sqlEC);
            stmtEC.setInt(1, rs.getInt("tid"));
            ResultSet rsEC = stmtEC.executeQuery();

            if(rsEC.next())
                tmp.setEmergencyContact(new EmergencyContact(rsEC.getInt("traineeID"),
                        rsEC.getString("fullName"), rsEC.getString("relationship"),
                        rsEC.getString("phoneNumber"), rsEC.getString("address"),
                        rsEC.getString("city"), rsEC.getString("state"),
                        rsEC.getString("zipcode")));
            else //Does not exist
                tmp.setEmergencyContact(null);

            tmp.setHoursAttended(rs.getInt("hoursAttended"));
            tmp.setQuestionnaire1Complete((1 == rs.getInt("isQuestionnaire1Complete")));
            tmp.setQuestionnaire2Complete((1 == rs.getInt("isQuestionnaire2Complete")));
            tmp.setActive((1 == rs.getInt("isActive")));
            tmp.setYear(rs.getInt("year"));
            tmp.setSession(rs.getInt("session"));

            //Get Questionnaire1 info if needed
            if(tmp.isQuestionnaire1Complete()){

                tmp.setShirtSize(rs.getString("shirtSize"));
                tmp.setShortSize(rs.getString("shortSize"));
                tmp.setSwimSuitSize(rs.getString("swimSuitSize"));
                tmp.setReturningTrainee((1 == rs.getInt("isReturningTrainee")));
                tmp.setWhyReturning(rs.getString("whyReturning"));
                tmp.setWhyBeStateLG(rs.getString("whyBeStateLG"));
                tmp.setWhatWantLearnTraining(rs.getString("whatWantLearnTraining"));
                tmp.setJG((1 == rs.getInt("isJG")));
                tmp.setJgInfo(rs.getString("jgInfo"));
                tmp.setOpenWaterLG((1 == rs.getInt("isOpenWaterLG")));
                tmp.setOpenWaterLGInfo(rs.getString("openWaterLGInfo"));
                tmp.setPoolLG((1 == rs.getInt("isPoolLG")));
                tmp.setPoolLGInfo(rs.getString("poolLGInfo"));
                tmp.setEMT((1 == rs.getInt("isEMT")));
                tmp.setEmtInfo(rs.getString("emtInfo"));
                tmp.setOtherAdvancedMedicalTraining((1 == rs.getInt("isOtherAdvancedMedicalTraining")));
                tmp.setAdvancedMedicalTrainingInfo(rs.getString("advancedMedicalTrainingInfo"));
                tmp.setFirstJob((1 == rs.getInt("isFirstJob")));
                tmp.setJobExperienceInfo(rs.getString("jobExperienceInfo"));
                tmp.setAnyExtraInfo(rs.getString("anyExtraInfo"));

            }

            //Get Questionnaire2 info if needed
            if(tmp.isQuestionnaire2Complete()){

                tmp.setExpectedBiggestTrainingChallengeInfo(
                        rs.getString("expectedBiggestTrainingChallengeInfo"));
                tmp.setPreparationInfo(rs.getString("preparationInfo"));
                tmp.setMedicalConfidence(rs.getInt("medicalConfidence"));
                tmp.setCprConfidence(rs.getInt("cprConfidence"));
                tmp.setPhysicalConfidence(rs.getInt("physicalConfidence"));
                tmp.setMentalConfidence(rs.getInt("mentalConfidence"));
                tmp.setPreTrainingSeminarsAttended(rs.getInt("preTrainingSeminarsAttended"));
                tmp.setOrganizedSwimPoloFreq(rs.getString("organizedSwimPoloFreq"));
                tmp.setPersonalSwimFreq(rs.getString("personalSwimFreq"));
                tmp.setGymFreq(rs.getString("gymFreq"));
                tmp.setOceanSwimFreq(rs.getString("oceanSwimFreq"));
                tmp.setRunningFreq(rs.getString("runningFreq"));
                tmp.setSurfingFreq(rs.getString("surfingFreq"));
                tmp.setDisabled((1 == rs.getInt("isDisabled")));

            }

            results.add(tmp);

        }

        return results;

    }

    /**
     * Helper function to reduce repetition of code for turning the result set of comments into a
     * vector.
     * @param rs
     * @return
     */
    private static Vector<Comment> getCommentsHelper(ResultSet rs) throws SQLException{

        Vector<Comment> results = new Vector<>();
        while(rs.next()){

            Comment tmp = new Comment();
            tmp.setTraineeID(rs.getInt("traineeID"));
            tmp.setId(rs.getInt("id"));
            tmp.setDate(rs.getString("date"));
            tmp.setRotation(rs.getString("rotation"));
            tmp.setInstructorName(rs.getString("instructorName"));
            tmp.setTraineeName(rs.getString("traineeName"));
            tmp.setIncidentType(rs.getString("incidentType"));
            tmp.setIncidentDescription(rs.getString("incidentDescription"));
            tmp.setInstructorActions(rs.getString("instructorActions"));
            tmp.setNextSteps(rs.getString("nextSteps"));
            tmp.setYear(rs.getInt("year"));
            tmp.setSession(rs.getInt("session"));

            results.add(tmp);

        }

        return results;

    }

    /**
     * Helper function to reduce repetition of code for turning the result set of Tests into a vector.
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Vector<Test> getTestsHelper(ResultSet rs) throws SQLException{

        Vector<Test> results = new Vector<>();
        while(rs.next())
            results.add(new Test(rs.getInt("testID"), rs.getString("name"),
                    rs.getInt("points"), rs.getInt("year"),
                    rs.getInt("session")));

        return results;

    }

    /**
     * Helper function to reduce repetition of code for turning the result set of TestScores into a vector.
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Vector<TestScore> getTestScoresHelper(ResultSet rs) throws SQLException{

        Vector<TestScore> results = new Vector<>();
        while(rs.next())
            results.add(new TestScore(rs.getInt("testID"), rs.getInt("traineeID"),
                    rs.getInt("score")));

        return results;

    }

    /**
     * Helper function to reduce repetition of code for turning the result set of Events into a vector.
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Vector<Event> getEventsHelper(ResultSet rs) throws SQLException{

        Vector<Event> results = new Vector<>();
        while(rs.next())
            results.add(new Event(rs.getInt("eventID"), rs.getString("name"),
                    rs.getInt("points"), rs.getString("notes"),
                    rs.getInt("year"), rs.getInt("session")));

        return results;

    }

    /**
     * Helper function to reduce repetition of code for turning the result set of EventScores into a vector.
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Vector<EventScore> getEventScoreHelper(ResultSet rs) throws SQLException{

        Vector<EventScore> results = new Vector<>();
        while(rs.next())
            results.add(new EventScore(rs.getInt("eventID"), rs.getInt("traineeID"),
                                       rs.getInt("score")));

        return results;

    }

    /**********************************************************************
     ******************************SETTERS*********************************
     **********************************************************************/


}
