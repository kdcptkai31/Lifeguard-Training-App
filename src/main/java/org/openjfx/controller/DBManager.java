package org.openjfx.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.openjfx.model.*;
import org.openjfx.model.Event;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.*;
import java.util.Collections;
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
                + "startDate TEXT,\n"
                + "endDate TEXT,\n"
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
                + "PRIMARY KEY(year, session, district)"
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
                + "image BLOB,\n"
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
                + "image BLOB,\n"
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
                + "commentID INTEGER,\n"
                + "traineeID INTEGER,\n"
                + "date TEXT,\n"
                + "rotation TEXT,\n"
                + "instructorName TEXT,\n"
                + "traineeName TEXT,\n"
                + "incidentType TEXT,\n"
                + "incidentDescription TEXT,\n"
                + "instructorActions TEXT,\n"
                + "nextSteps TEXT,\n"
                + "year INTEGER,\n"
                + "session INTEGER,\n"
                + "FOREIGN KEY(traineeID) REFERENCES trainees(tid),\n"
                + "PRIMARY KEY (commentID)"
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
                + "place INTEGER,\n"
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

        //Adds
        String sqlFindDefaultTrainee = "SELECT * FROM trainees WHERE tid = 1";
        try{

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlFindDefaultTrainee);
            if(!rs.next()){

                addInitialTrainee(new Trainee(null, null, null, null, null, null,
                        null, null, null, false, null, -1,
                        false, false, false, -1, -1));

            }

        }catch(SQLException e){
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

    /**
     * Returns the trainee from a given tID.
     * @param tID
     * @return
     */
    public static Trainee getTraineeFromTID(int tID){

        String sql = "SELECT * FROM trainees WHERE tid = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tID);
            ResultSet rs = stmt.executeQuery();
            return getTraineesHelper(rs).elementAt(0);

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns the trainee from the given data.
     * @param fName
     * @param mName
     * @param lName
     * @param year
     * @param session
     * @return
     */
    public static Trainee getTraineeFromData(String fName, String mName, String lName, int year, int session){

        String sql = "SELECT * FROM trainees WHERE firstName = ? AND middleName = ? AND lastName = ? AND year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, fName);
            stmt.setString(2, mName);
            stmt.setString(3, lName);
            stmt.setInt(4, year);
            stmt.setInt(5, session);
            ResultSet rs = stmt.executeQuery();
            return getTraineesHelper(rs).elementAt(0);

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * For use with the Comment Constructor, takes the name from the comment google form and figures out if it is a full
     * name, or last name, and finds the corresponding TID for the trainee, also with the given year and session.
     * @param name
     * @param year
     * @param session
     * @return The tid of the corresponding trainee, 0 if not found.
     */
    public static int getTIDFromNameAndSession(String name, int year, int session){

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tid FROM trainees WHERE year = ? AND session = ?");
        String[] nameAR = name.split(" ");
        Vector<String> names = new Vector<>();
        Collections.addAll(names, nameAR);

        try{

            if(names.size() == 0)
                throw new Exception("Bad name");
            else if(names.size() == 1)
                sql.append("AND lastName = ?");
            else if(names.size() >= 2)
                sql.append("AND lastName = ? AND firstName = ?");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, year);
            stmt.setInt(2, session);
            if(names.size() >= 2)
                stmt.setString(4, names.get(0));
            stmt.setString(3, names.get(names.size() - 1));
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return rs.getInt("tid");


        }catch (Exception e){
            e.printStackTrace();
        }

        //Default value
        return 1;

    }

    /*
     ****************************** EmergencyContact ***************************************
     */

    /*
     ****************************** Comment ***************************************
     */

    /**
     * Returns all comments from a specific day
     * @param date
     * @return
     */
    public static Vector<Comment> getAllCommentsFromDay(int date, int year, int session){

        String sql = "SELECT * FROM comments WHERE date = ? AND year = ? AND session = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, date);
            stmt.setInt(2, year);
            stmt.setInt(3, session);
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

    /**
     * Returns all comments from a given Trainee ID.
     * @param tid
     * @return
     */
    public static Vector<Comment> getAllCommentsFromTID(int tid){

        String sql = "SELECT * FROM comments WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tid);
            ResultSet rs = stmt.executeQuery();

            return getCommentsHelper(rs);

        }catch (SQLException e){
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

    /**
     * Returns all tests from a list of test ids. ids cannot be empty.
     * @param ids
     * @return
     */
    public static Vector<Test> getAllTestFromTestIDs(Vector<Integer> ids){

        StringBuilder sql = new StringBuilder("SELECT * FROM tests WHERE ");

        try{

            for(Integer id : ids)
                sql.append("testID = ? OR ");

            sql.delete(sql.length() - 4, sql.length() - 1);
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            for(int i = 0; i < ids.size(); i++)
                stmt.setInt(i + 1, ids.get(i));

            ResultSet rs = stmt.executeQuery();
            return getTestsHelper(rs);


        }catch (SQLException e){
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

    /**
     * Returns all events from a list of event ids. ids cannot be empty.
     * @param ids
     * @return
     */
    public static Vector<Event> getAllEventsFromEventIDs(Vector<Integer> ids){

        StringBuilder sql = new StringBuilder("SELECT * FROM events WHERE ");

        try{

            for(Integer id : ids)
                sql.append("eventID = ? OR ");

            sql.delete(sql.length() - 4, sql.length() - 1);
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            for(int i = 0; i < ids.size(); i++)
                stmt.setInt(i + 1, ids.get(i));

            ResultSet rs = stmt.executeQuery();
            return getEventsHelper(rs);


        }catch (SQLException e){
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
     * Returns all sessions and years in the database.
     * @return
     */
    public static Vector<Session> getAllSessions(){

        String sql = "SELECT * FROM sessions";
        try{

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            Vector<Session> results = new Vector<>();
            while(rs.next())
                results.add(new Session(rs.getInt("year"), rs.getInt("session"),
                                        rs.getString("startDate"), rs.getString("endDate")));

            return results;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Returns the most current session, used to initialize the application.
     * @return a pair containing the most current session.
     */
    public static Session getCurrentSession(){

        String sql = "SELECT * FROM sessions";
        try{
            //Extract all years and sessions
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set
            Vector<Session> results = new Vector<>();
            while(rs.next())
                results.add(new Session(rs.getInt("year"), rs.getInt("session"),
                                        rs.getString("startDate"), rs.getString("endDate")));

            //Find newest session
            Session maxSession = new Session(0, 0, "", "");
            for(int i = 0; i < results.size(); i++){

                if(results.elementAt(i).getYear() >= maxSession.getYear()){
                    if(results.elementAt(i).getSession() > maxSession.getSession())
                        maxSession = results.elementAt(i);
                }

            }
            if(maxSession.getYear() != 0 && maxSession.getSession() != 0)
                return maxSession;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;

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

            while(rs.next()){

                if(rs.getBinaryStream("image") == null)
                    results.add(new Instructor(rs.getInt("year"), rs.getInt("session"),
                            rs.getString("name")));
                else
                    results.add(new Instructor(rs.getInt("year"), rs.getInt("session"),
                            rs.getString("name"), new Image(rs.getBinaryStream("image"))));

            }

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
            if(rs.getBinaryStream("image") == null)
                tmp.setImage(null);
            else
                tmp.setImage(new Image(rs.getBinaryStream("image")));

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
            tmp.setId(rs.getInt("commentID"));
            tmp.setTraineeID(rs.getInt("traineeID"));
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
                    rs.getString("notes"), rs.getInt("year"), rs.getInt("session")));

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
                                       rs.getInt("place")));

        return results;

    }

    /**********************************************************************
     ******************************SETTERS*********************************
     **********************************************************************/

    /*
     ****************************** TRAINEE ***************************************
     */

    /**
     * Adds the initial data from the google form to populate the initial data in the trainee db. A tid is generated
     * when the row is added, and all questionnaire 1 and 2 values are defaulted to null.
     * @param tToAdd
     * @return true if successful, false if not
     */
    public static boolean addInitialTrainee(Trainee tToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO trainees(tid, firstName, middleName, lastName, birthDate, city, state, "
                + "phoneNumber, email, districtChoice, isLodging, hoursAttended, image, isQuestionnaire1Complete, "
                + "isQuestionnaire2Complete, isActive, year, session, shirtSize, shortSize, swimSuitSize, "
                + "isReturningTrainee, whyReturning, whyBeStateLG, whatWantLearnTraining, isJG, jgInfo, "
                + "isOpenWaterLG, openWaterLGInfo, isPoolLG, poolLGInfo, isEMT, emtInfo, "
                + "isOtherAdvancedMedicalTraining, advancedMedicalTrainingInfo, isFirstJob, jobExperienceInfo, "
                + "anyExtraInfo, expectedBiggestTrainingChallengeInfo, preparationInfo, medicalConfidence, "
                + "cprConfidence, physicalConfidence, mentalConfidence, preTrainingSeminarsAttended, "
                + "organizedSwimPoloFreq, personalSwimFreq, gymFreq, oceanSwimFreq, runningFreq, surfingFreq, "
                + "isDisabled) VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null, ?, ?, ?, ?, ?, null, null, null, null, "
                + "null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "
                + "null, null, null, null, null, null, null, null, null, null, null, null, null, null)");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, tToAdd.getFirstName());
            stmt.setString(2, tToAdd.getMiddleName());
            stmt.setString(3, tToAdd.getLastName());
            stmt.setString(4, tToAdd.getBirthDate());
            stmt.setString(5, tToAdd.getCity());
            stmt.setString(6, tToAdd.getState());
            stmt.setString(7, tToAdd.getPhoneNumber());
            stmt.setString(8, tToAdd.getEmail());
            stmt.setString(9, tToAdd.getDistrictChoice());
            stmt.setInt(10, tToAdd.isLodging() ? 1 : 0);
            stmt.setInt(11, tToAdd.getHoursAttended());
            stmt.setInt(12, tToAdd.isQuestionnaire1Complete() ? 1 : 0);
            stmt.setInt(13, tToAdd.isQuestionnaire2Complete() ? 1 : 0);
            stmt.setInt(14, tToAdd.isActive() ? 1 : 0);
            stmt.setInt(15, tToAdd.getYear());
            stmt.setInt(16, tToAdd.getSession());
            stmt.executeUpdate();

            //Skips the EC addition if it doesn't exist
            if(tToAdd.getEmergencyContact() == null)
                throw new Exception("Skip EC addition");

            Trainee tmp = getTraineeFromData(tToAdd.getFirstName(), tToAdd.getMiddleName(), tToAdd.getLastName(),
                                             tToAdd.getYear(), tToAdd.getSession());
            if(tToAdd.getEmergencyContact() != null){

                tToAdd.getEmergencyContact().setTraineeID(tmp.getId());
                addEmergencyContact(tToAdd.getEmergencyContact());

            }


            return true;

        }catch(Exception e){
            System.out.println("Skip EC addition");
        }

        return false;

    }

    /**
     * Updates the given trainee with its new general information.
     * @param tToAdd
     * @return true if successful, false if not
     */
    public static boolean updateTrainee(Trainee tToAdd){

        String sql = "UPDATE trainees SET firstName = ?, middleName = ?, lastName = ?, birthDate = ?, city = ?, " +
                " state = ?, phoneNumber = ?, email = ?, districtChoice = ?, isLodging = ? WHERE tid = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tToAdd.getFirstName());
            stmt.setString(2, tToAdd.getMiddleName());
            stmt.setString(3, tToAdd.getLastName());
            stmt.setString(4, tToAdd.getBirthDate());
            stmt.setString(5, tToAdd.getCity());
            stmt.setString(6, tToAdd.getState());
            stmt.setString(7, tToAdd.getPhoneNumber());
            stmt.setString(8, tToAdd.getEmail());
            stmt.setString(9, tToAdd.getDistrictChoice());
            stmt.setInt(10, tToAdd.isLodging() ? 1 : 0);
            stmt.setInt(11, tToAdd.getId());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Adds the given image to the database for the trainee.
     * @param tToAdd
     * @return true if successful, false if not
     */
    public static boolean addTraineeProfileImage(Trainee tToAdd){

        String sql = "UPDATE trainees SET image = ? WHERE tid = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            if(tToAdd.getImage() == null)
                stmt.setBytes(1, null);
            else{

                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(tToAdd.getImage(), null), "png", byteOutput);
                stmt.setBytes(1, byteOutput.toByteArray());

            }

            stmt.setInt(2, tToAdd.getId());
            stmt.executeUpdate();

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Adds the questionnaire 1 data to the given trainee.
     * @param tToAdd
     * @return true if successful, false if not
     */
    public static boolean addExistingTraineeQuestionnaire1Data(Trainee tToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE trainees SET shirtSize = ?, shortSize = ?, swimSuitSize = ?, isReturningTrainee = ?, "
                    + "whyReturning = ?, whyBeStateLG = ?, whatWantLearnTraining = ?, isJG = ?, jgInfo = ?, "
                    + "isOpenWaterLG = ?, openWaterLGInfo = ?, isPoolLG = ?, poolLGInfo = ?, isEMT = ?, emtInfo = ?, "
                    + "isOtherAdvancedMedicalTraining = ?, advancedMedicalTrainingInfo = ?, isFirstJob = ?, "
                    + "jobExperienceInfo = ?, anyExtraInfo = ?, isQuestionnaire1Complete = ? WHERE tid = ?");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, tToAdd.getShirtSize());
            stmt.setString(2, tToAdd.getShortSize());
            stmt.setString(3, tToAdd.getSwimSuitSize());
            stmt.setInt(4, tToAdd.isReturningTrainee() ? 1 : 0);
            stmt.setString(5, tToAdd.getWhyReturning());
            stmt.setString(6, tToAdd.getWhyBeStateLG());
            stmt.setString(7, tToAdd.getWhatWantLearnTraining());
            stmt.setInt(8, tToAdd.isJG() ? 1 : 0);
            stmt.setString(9, tToAdd.getJgInfo());
            stmt.setInt(10, tToAdd.isOpenWaterLG() ? 1 : 0);
            stmt.setString(11, tToAdd.getOpenWaterLGInfo());
            stmt.setInt(12, tToAdd.isPoolLG() ? 1 : 0);
            stmt.setString(13, tToAdd.getPoolLGInfo());
            stmt.setInt(14, tToAdd.isEMT() ? 1 : 0);
            stmt.setString(15, tToAdd.getEmtInfo());
            stmt.setInt(16, tToAdd.isOtherAdvancedMedicalTraining() ? 1 : 0);
            stmt.setString(17, tToAdd.getAdvancedMedicalTrainingInfo());
            stmt.setInt(18, tToAdd.isFirstJob() ? 1 : 0);
            stmt.setString(19, tToAdd.getJobExperienceInfo());
            stmt.setString(20, tToAdd.getAnyExtraInfo());
            stmt.setInt(21, 1);
            stmt.setInt(22, tToAdd.getId());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Adds the questionnaire 2 data to the given trainee.
     * @param tToAdd
     * @return true if successful, false if not
     */
    public static boolean addExistingTraineeQuestionnaire2Data(Trainee tToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE trainees SET expectedBiggestTrainingChallengeInfo = ?, preparationInfo = ?, "
                    + "medicalConfidence = ?, cprConfidence = ?, physicalConfidence = ?, mentalConfidence = ?, "
                    + "preTrainingSeminarsAttended = ?, organizedSwimPoloFreq = ?, personalSwimFreq = ?, gymFreq = ?, "
                    + "oceanSwimFreq = ?, runningFreq = ?, surfingFreq = ?, isDisabled = ?, isQuestionnaire2Complete = ?"
                    + " WHERE tid = ?");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, tToAdd.getExpectedBiggestTrainingChallengeInfo());
            stmt.setString(2, tToAdd.getPreparationInfo());
            stmt.setInt(3, tToAdd.getMedicalConfidence());
            stmt.setInt(4, tToAdd.getCprConfidence());
            stmt.setInt(5, tToAdd.getPhysicalConfidence());
            stmt.setInt(6, tToAdd.getMentalConfidence());
            stmt.setInt(7, tToAdd.getPreTrainingSeminarsAttended());
            stmt.setString(8, tToAdd.getOrganizedSwimPoloFreq());
            stmt.setString(9, tToAdd.getPersonalSwimFreq());
            stmt.setString(10, tToAdd.getGymFreq());
            stmt.setString(11, tToAdd.getOceanSwimFreq());
            stmt.setString(12, tToAdd.getRunningFreq());
            stmt.setString(13, tToAdd.getSurfingFreq());
            stmt.setInt(14, tToAdd.isDisabled() ? 1 : 0);
            stmt.setInt(15, 1);
            stmt.setInt(16, tToAdd.getId());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** EMERGENCY CONTACT ***************************************
     */

    /**
     * Adds the given emergency contact to the database.
     * @param ecToAdd
     * @return true if successful, false if not.
     */
    public static boolean addEmergencyContact(EmergencyContact ecToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO emergencyContacts (traineeID, fullName, relationship, phoneNumber, "
                 + "address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, ecToAdd.getTraineeID());
            stmt.setString(2, ecToAdd.getFullName());
            stmt.setString(3, ecToAdd.getRelationship());
            stmt.setString(4, ecToAdd.getPhoneNumber());
            stmt.setString(5, ecToAdd.getAddress());
            stmt.setString(6, ecToAdd.getCity());
            stmt.setString(7, ecToAdd.getState());
            stmt.setString(8, ecToAdd.getZipcode());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            System.out.println(ecToAdd.getFullName() + " not added!(Duplicate)");
        }

        return false;

    }

    /**
     * Updates the given emergency contact which contains the trainee's ID already
     * @param ecToAdd
     * @return
     */
    public static boolean updateEmergencyContact(EmergencyContact ecToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE emergencyContacts SET fullName = ?, relationship = ?, phoneNumber = ?, address = ?, "
                + "city = ?, state = ?, zipcode = ? WHERE traineeID = ?");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, ecToAdd.getFullName());
            stmt.setString(2, ecToAdd.getRelationship());
            stmt.setString(3, ecToAdd.getPhoneNumber());
            stmt.setString(4, ecToAdd.getAddress());
            stmt.setString(5, ecToAdd.getCity());
            stmt.setString(6, ecToAdd.getState());
            stmt.setString(7, ecToAdd.getZipcode());
            stmt.setInt(8, ecToAdd.getTraineeID());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** COMMENT ***************************************
     */

    /**
     * Adds a given comment to the database.
     * @param cToAdd
     * @return true if successful, false if not.
     */
    public static boolean addComment(Comment cToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO comments(commentID, traineeID, date, rotation, instructorName, traineeName, incidentType, "
                 + "incidentDescription, instructorActions, nextSteps, year, session) VALUES (NULL, ?, ?, ?, ?, ?, ?, "
                 + "?, ?, ?, ?, ?)");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, cToAdd.getTraineeID());
            stmt.setString(2, cToAdd.getDate());
            stmt.setString(3, cToAdd.getRotation());
            stmt.setString(4, cToAdd.getInstructorName());
            stmt.setString(5, cToAdd.getTraineeName());
            stmt.setString(6, cToAdd.getIncidentType());
            stmt.setString(7, cToAdd.getIncidentDescription());
            stmt.setString(8, cToAdd.getInstructorActions());
            stmt.setString(9, cToAdd.getNextSteps());
            stmt.setInt(10, cToAdd.getYear());
            stmt.setInt(11, cToAdd.getSession());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates a given comment with the new data, only the commentID, year, and session cannot be tampered with.
     * @param cToAdd
     * @return true if successful, false if not
     */
    public static boolean updateComment(Comment cToAdd){

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE comments SET traineeID = ?, date = ?, rotation = ?, instructorName = ?, traineeName = ?," +
                " incidentType = ?, incidentDescription = ?, instructorActions = ?, nextSteps = ? WHERE commentID = ?");

        try{

            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, cToAdd.getTraineeID());
            stmt.setString(2, cToAdd.getDate());
            stmt.setString(3, cToAdd.getRotation());
            stmt.setString(4, cToAdd.getInstructorName());
            stmt.setString(5, cToAdd.getTraineeName());
            stmt.setString(6, cToAdd.getIncidentType());
            stmt.setString(7, cToAdd.getIncidentDescription());
            stmt.setString(8, cToAdd.getInstructorActions());
            stmt.setString(9, cToAdd.getNextSteps());
            stmt.setInt(10, cToAdd.getId());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** TEST ***************************************
     */

    /**
     * Adds a given test to the database, generating the testID within the db.
     * @param tToAdd
     * @return true if successful, false if not.
     */
    public static boolean addTest(Test tToAdd){

        String sql = "INSERT INTO tests (testID, name, points, year, session) VALUES (null, ?, ?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tToAdd.getName());
            stmt.setInt(2, tToAdd.getPoints());
            stmt.setInt(3, tToAdd.getYear());
            stmt.setInt(4, tToAdd.getSession());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates a given test with the new name and point values.
     * @param tToAdd
     * @return true if successful, false if not.
     */
    public static boolean updateTest(Test tToAdd){

        String sql = "UPDATE tests SET name = ?, points = ? WHERE testID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tToAdd.getName());
            stmt.setInt(2, tToAdd.getPoints());
            stmt.setInt(3, tToAdd.getTestID());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** TEST SCORES ***************************************
     */

    /**
     * Adds a test score to the db.
     * @param tsToAdd
     * @return true if successful, false if not.
     */
    public static boolean addTestScore(TestScore tsToAdd){

        String sql = "INSERT INTO testScores (testID, traineeID, score) VALUES (?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tsToAdd.getTestID());
            stmt.setInt(2, tsToAdd.getTraineeID());
            stmt.setInt(3, tsToAdd.getScore());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates a trainee's test score.
     * @param tsToAdd
     * @return true if successful, false if not.
     */
    public static boolean updateTestScore(TestScore tsToAdd){

        String sql = "UPDATE testScores SET score = ? WHERE testID = ? AND traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tsToAdd.getScore());
            stmt.setInt(2, tsToAdd.getTestID());
            stmt.setInt(3, tsToAdd.getTraineeID());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** EVENT ***************************************
     */

    /**
     * Adds the initial event data to the database. A eventID is generated by the db.
     * @param eToAdd
     * @return true if successful, false if not.
     */
    public static boolean addEvent(Event eToAdd){

        String sql = "INSERT INTO events (eventID, name, notes, year, session) VALUES (null, ?, ?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, eToAdd.getName());
            stmt.setString(2, eToAdd.getNotes());
            stmt.setInt(3, eToAdd.getYear());
            stmt.setInt(4, eToAdd.getSession());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates a given event with the new information.
     * @param eToAdd
     * @return true if successful, false if not.
     */
    public static boolean updateEvent(Event eToAdd){

        String sql = "UPDATE events SET name = ?, notes = ? WHERE eventID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, eToAdd.getName());
            stmt.setString(2, eToAdd.getNotes());
            stmt.setInt(3, eToAdd.getEventID());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** EVENT SCORES ***************************************
     */

    /**
     * Adds a event score to the db.
     * @param esToAdd
     * @return true if successful, false if not.
     */
    public static boolean addEventScore(EventScore esToAdd){

        String sql = "INSERT INTO eventScores (eventID, traineeID, place) VALUES (?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, esToAdd.getEventID());
            stmt.setInt(2, esToAdd.getTraineeID());
            stmt.setInt(3, esToAdd.getPlace());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates an event score with the new data.
     * @param esToAdd
     * @return true if successful, false if not.
     */
    public static boolean updateEventScore(EventScore esToAdd){

        String sql = "UPDATE eventScores SET place = ? WHERE eventID = ? AND traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, esToAdd.getPlace());
            stmt.setInt(2, esToAdd.getEventID());
            stmt.setInt(3, esToAdd.getTraineeID());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** SESSION ***************************************
     */

    /**
     * Adds a new year and session to the db.
     * @param newYear
     * @param newSession
     * @return true if successful, false if not.
     */
    public static boolean addNewSession(int newYear, int newSession, String sDate, String eDate){

        String sql = "INSERT INTO sessions(year, session, startDate, endDate) VALUES(?, ?, ?, ?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, newYear);
            stmt.setInt(2, newSession);
            stmt.setString(3, sDate);
            stmt.setString(4, eDate);
            stmt.executeUpdate();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** DISTRICTS ***************************************
     */

    /**
     * Adds a district to the db.
     * @param dToAdd
     * @return true if successful, false if not.
     */
    public static boolean addDistrict(District dToAdd){

        String sql = "INSERT INTO districts (year, session, district, superEmail) VALUES (?, ?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, dToAdd.getYear());
            stmt.setInt(2, dToAdd.getSession());
            stmt.setString(3, dToAdd.getName());
            stmt.setString(4, dToAdd.getSupervisorEmail());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Updates a given district with a new supervisor email address.
     * @param dToAdd
     * @return true if successful, false if not.
     */
    public static boolean updateDistrict(District dToAdd){

        String sql = "UPDATE districts SET superEmail = ? WHERE year = ?, session = ?, district = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, dToAdd.getSupervisorEmail());
            stmt.setInt(2, dToAdd.getYear());
            stmt.setInt(3, dToAdd.getSession());
            stmt.setString(4, dToAdd.getName());
            stmt.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /*
     ****************************** INSTRUCTORS ***************************************
     */

    /**
     * Adds the given instructor to the db.
     * @param iToAdd
     * @return true if successful, false if not.
     */
    public static boolean addInstructor(Instructor iToAdd){

        String sql = "INSERT INTO instructors (year, session, name, image) VALUES (?, ?, ?, ?)";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, iToAdd.getYear());
            stmt.setInt(2, iToAdd.getSession());
            stmt.setString(3, iToAdd.getName());

            if(iToAdd.getImage() == null)
                stmt.setBytes(4, null);
            else{

                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(iToAdd.getImage(), null), "png", byteOutput);
                stmt.setBytes(4, byteOutput.toByteArray());

            }

            stmt.executeUpdate();

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    /**********************************************************************
     ******************************DELETES*********************************
     **********************************************************************/

    /**
     * Deletes the given trainee and all associated data in the database, including test scores, event scores, all
     * comments, and their emergency contact.
     * @param tToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteTrainee(Trainee tToDelete){

        if(!deleteAllTestScoresOfATrainee(tToDelete.getId()) || !deleteAllOfATraineeComments(tToDelete.getId()) ||
           !deleteEmergencyContact(tToDelete.getId()) || !deleteAllEventScoresOfATrainee(tToDelete.getId()))
            return false;

        String sql = "DELETE FROM trainees WHERE tid = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tToDelete.getId());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Replaces the trainee's pfp with the default pfp.
     * @param tToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteTraineeProfileImage(Trainee tToDelete){

        String sql = "UPDATE trainees SET image = ? where tid = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(new Image("resources/org/openjfx/images/blankpfp.png"), null),
                                                   "jpg", byteOutput);
            Blob imageBlob = connection.createBlob();
            imageBlob.setBytes(0, byteOutput.toByteArray());
            stmt.setBlob(1, imageBlob);
            stmt.setInt(2, tToDelete.getId());
            stmt.executeUpdate();

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the emergency contact that matches the given trainee ID.
     * @param tid
     * @return true if successful, false if not.
     */
    public static boolean deleteEmergencyContact(int tid){

        String sql = "DELETE FROM emergencyContacts WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tid);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes all comments associated with the given trainee ID.
     * @param tid
     * @return true if successful, false if not.
     */
    public static boolean deleteAllOfATraineeComments(int tid){

        String sql = "DELETE FROM comments WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tid);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the given comment.
     * @param cToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteComment(Comment cToDelete){

        String sql = "DELETE FROM comments WHERE traineeID = ?, commentID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cToDelete.getTraineeID());
            stmt.setInt(2, cToDelete.getId());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the given test from the database.
     * @param tToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteTest(Test tToDelete){

        if(!deleteAllTestScoresOfATest(tToDelete.getTestID()))
            return false;

        String sql = "DELETE FROM tests WHERE testID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tToDelete.getTestID());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes all scores from a given test ID.
     * @param testID
     * @return true if successful, false if not.
     */
    public static boolean deleteAllTestScoresOfATest(int testID){

        String sql = "DELETE FROM testScores WHERE testID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, testID);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes all scores of a given trainee ID.
     * @param traineeID
     * @return true if successful, false if not.
     */
    public static boolean deleteAllTestScoresOfATrainee(int traineeID){

        String sql = "DELETE FROM testScores WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, traineeID);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes a specific test score.
     * @param tsToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteTestScore(TestScore tsToDelete){

        String sql = "DELETE FROM testScores WHERE testID = ?, traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tsToDelete.getTestID());
            stmt.setInt(2, tsToDelete.getTraineeID());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes a given event, first deleting all event scores associated.
     * @param eToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteEvent(Event eToDelete){

        if(!deleteAllEventScoresOfAnEvent(eToDelete.getEventID()))
            return false;

        String sql = "DELETE FROM events WHERE eventID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, eToDelete.getEventID());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes all event scores from a given event ID.
     * @param eventID
     * @return true if successful, false if not.
     */
    public static boolean deleteAllEventScoresOfAnEvent(int eventID){

        String sql = "DELETE FROM eventScores WHERE eventID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, eventID);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes all event scores from a given trainee ID.
     * @param traineeID
     * @return true if successful, false if not.
     */
    public static boolean deleteAllEventScoresOfATrainee(int traineeID){

        String sql = "DELETE FROM eventScores WHERE traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, traineeID);
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the given event score.
     * @param esToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteEventScore(EventScore esToDelete){

        String sql = "DELETE FROM eventScores WHERE eventID = ?, traineeID = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, esToDelete.getEventID());
            stmt.setInt(2, esToDelete.getTraineeID());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the given district.
     * @param dToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteDistrict(District dToDelete){

        String sql = "DELETE FROM districts WHERE year = ?, session = ?, district = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, dToDelete.getYear());
            stmt.setInt(2, dToDelete.getSession());
            stmt.setString(3, dToDelete.getName());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Deletes the given instructor.
     * @param iToDelete
     * @return true if successful, false if not.
     */
    public static boolean deleteInstructor(Instructor iToDelete){

        String sql = "DELETE FROM instructors WHERE year = ?, session = ?, name = ?";

        try{

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, iToDelete.getYear());
            stmt.setInt(2, iToDelete.getSession());
            stmt.setString(3, iToDelete.getName());
            stmt.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

}
