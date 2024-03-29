package org.openjfx.controller;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicDirectoryModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openjfx.model.*;
import org.openjfx.model.Comment;
import org.openjfx.model.Event;
import org.openjfx.view.OverviewView;

/**
 * Creates Word documents or Excel documents, which are needed for operational or report purposes.
 */
public class DocumentGenerator {

    private Controller controller;

    /**
     * Default Constructor
     */
    public DocumentGenerator(){

        controller = LifeguardTrainingApplication.getController();
        createDirectoriesIfNeeded();

    }

    /**
     * Checks for and creates the needed directories for documents for the current session, if needed.
     */
    public void createDirectoriesIfNeeded(){

        try{

            //Checks if the PrintOuts folder exists
            File printoutDir = new File(System.getProperty("user.dir") + "\\Reports\\");
            if(!printoutDir.exists()) {
                if (!printoutDir.mkdir())
                    System.exit(1);
            }

            File templateDir = new File(System.getProperty("user.dir") + "\\Templates\\");
            if(!templateDir.exists()){
                if(!templateDir.mkdir())
                    System.exit(1);
            }

            //Checks for and creates the PrintOuts folder contains the current session's output folder, if needed
            File currentSession = new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession());
            if(!currentSession.exists()){
                if(!currentSession.mkdir())
                    System.exit(1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Creates an excel doc that prints out each trainee's profile picture, name, and district. 4 profiles per page
     */
    public void generateTraineeProfiles(){

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //Styling
        XSSFFont nameFont = workbook.createFont();
        nameFont.setBold(true);
        nameFont.setFontHeightInPoints((short)18);
        nameFont.setFontName("Arial");
        XSSFFont smallNameFont = workbook.createFont();
        smallNameFont.setBold(true);
        smallNameFont.setFontHeightInPoints((short)14);
        smallNameFont.setFontName("Arial");
        XSSFFont districtFont = workbook.createFont();
        districtFont.setBold(true);
        districtFont.setFontHeightInPoints((short)14);
        districtFont.setFontName("Arial");

        CellStyle nameStyle = workbook.createCellStyle();
        nameStyle.setFont(nameFont);
        CellStyle smallNameStyle = workbook.createCellStyle();
        smallNameStyle.setFont(smallNameFont);
        CellStyle districtStyle = workbook.createCellStyle();
        districtStyle.setFont(districtFont);

        try{

            //Used to correctly offset the excel rows to fit 4 profiles per printable page.
//            int alternator = 0;

            //Used to format the page to fit 2 profiles per page
            int subtractor = 0;
            for(int i = 0; i < controller.getCurrentTrainees().size(); i+=2){

                ByteArrayOutputStream byteIn = new ByteArrayOutputStream();
                ImageIO.write(Objects.requireNonNull(SwingFXUtils.fromFXImage(controller.getCurrentTrainees().get(i).getActualImage(), null)), "png", byteIn);
                int pictureIndex = workbook.addPicture(byteIn.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                byteIn.close();
                CreationHelper helper = workbook.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1((i * 23) - subtractor + i / 4);
                anchor.setCol2(4);
                anchor.setRow2(16 + (i * 23) - subtractor + i / 4);
                Picture pic = drawing.createPicture(anchor, pictureIndex);

                if(i + 1 < controller.getCurrentTrainees().size()){

                    ByteArrayOutputStream byteIn1 = new ByteArrayOutputStream();
                    ImageIO.write(Objects.requireNonNull(SwingFXUtils.fromFXImage(controller.getCurrentTrainees().get(i + 1).getActualImage(), null)), "png", byteIn1);
                    int pictureIndex1 = workbook.addPicture(byteIn1.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                    byteIn1.close();
                    CreationHelper helper1 = workbook.getCreationHelper();
                    Drawing drawing1 = sheet.createDrawingPatriarch();
                    ClientAnchor anchor1 = helper1.createClientAnchor();
                    anchor1.setCol1(5);
                    anchor1.setRow1((i * 23) - subtractor + i / 4);
                    anchor1.setCol2(9);
                    anchor1.setRow2(16 + (i * 23) - subtractor + i / 4);
                    Picture pic1 = drawing1.createPicture(anchor1, pictureIndex1);

                }

                Row row = sheet.createRow(17 + (i * 23) - subtractor + i / 4);
                Cell cell;
                String nameText = controller.getCurrentTrainees().get(i).getFirstName() + " " +
                        controller.getCurrentTrainees().get(i).getLastName();
                if(nameText.length() < 14) {
                    cell = row.createCell(1);
                    cell.setCellStyle(nameStyle);
                }else {
                    cell = row.createCell(0);
                    if(nameText.length() < 19)
                        cell.setCellStyle(nameStyle);
                    else
                        cell.setCellStyle(smallNameStyle);
                }
                cell.setCellValue(nameText);

                if(i + 1 < controller.getCurrentTrainees().size()){

                    nameText = controller.getCurrentTrainees().get(i + 1).getFirstName() + " " +
                                      controller.getCurrentTrainees().get(i + 1).getLastName();
                    if(nameText.length() < 14) {
                        cell = row.createCell(6);
                        cell.setCellStyle(nameStyle);
                    }else {
                        cell = row.createCell(5);
                        if(nameText.length() < 19)
                            cell.setCellStyle(nameStyle);
                        else
                            cell.setCellStyle(smallNameStyle);
                    }
                    cell.setCellValue(nameText);

                }

                Row row1 = sheet.createRow(19 + (i * 23) - subtractor + i / 4);
                Row row2 = sheet.createRow(20 + (i * 23) - subtractor + i / 4);
                String districtText = controller.getCurrentTrainees().get(i).getDistrictChoice();

                if(districtText.length() > 25 && districtText.contains("-")){

                    String[] districtInfo = districtText.split(" - ");
                    cell = row1.createCell(0);
                    cell.setCellValue(districtInfo[0]);
                    cell.setCellStyle(districtStyle);

                    cell = row2.createCell(0);
                    cell.setCellValue(districtInfo[1]);
                    cell.setCellStyle(districtStyle);

                }else{

                    if(districtText.length() < 13)
                        cell = row1.createCell(1);
                    else
                        cell = row1.createCell(0);
                    cell.setCellValue(districtText);
                    cell.setCellStyle(districtStyle);

                }

                if(i + 1 < controller.getCurrentTrainees().size()){

                    districtText = controller.getCurrentTrainees().get(i + 1).getDistrictChoice();

                    if(districtText.length() > 25 && districtText.contains("-")){

                        String[] districtInfoAlso = districtText.split(" - ");
                        cell = row1.createCell(5);
                        cell.setCellValue(districtInfoAlso[0]);
                        cell.setCellStyle(districtStyle);
                        cell = row2.createCell(5);
                        cell.setCellValue(districtInfoAlso[1]);
                        cell.setCellStyle(districtStyle);

                    }else{

                        if(districtText.length() < 13)
                            cell = row1.createCell(6);
                        else
                            cell = row1.createCell(5);
                        cell.setCellValue(districtText);
                        cell.setCellStyle(districtStyle);

                    }
                }


                if(i / 15 == 1 || i / 30 == 1)
                    subtractor += 5;

                if((i-1) / 15 == 1 || (i-1) / 30 == 1)
                    subtractor -= 5;

//                if(i != 0 && i % 2 == 0)
//                    subtractor++;

//                if(i != 0 && i % 3 == 0)
//                    subtractor--;
//                alternator = (alternator + 1) % 2;

            }

            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Trainee_Profiles.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Trainee_Profiles.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Handles the Individual Summary process. To reduce execution time, this function will run first always, and then
     * will either generate the selected trainee's individual summary, or it will create the individual summary for
     * every trainee. This function will find the overall rankings, as well as percent of course complete, and
     * @param selectedTrainee
     */
    public void preProcessIndividualSummaries(Trainee selectedTrainee){

        //Gather data from trainees to create the correct placement/////////////////////////////////////////////////////////
        Vector<Trainee> traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                                                                            controller.getCurrentSession().getSession());

        Vector<Double> traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        Vector<Pair<Integer, Double>> courseCompletedCount = new Vector<>(); //Holds the count of completed events and tests for each trainee
        int counter = 0;
        for(Trainee trainee : Objects.requireNonNull(traineeVector)){

            int completedEventsTests = 0;

            double totalScore = 0;
            Vector<TestScore> traineeTestScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
            Vector<EventScore> traineeEventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
            if(!traineeTestScores.isEmpty()) {
                for (TestScore score : traineeTestScores) {
                    totalScore += score.getScore();
                    completedEventsTests++;
                }

            }
            traineeTotalScores.add(totalScore);
            int totalPlace = 0;
            if(!traineeEventScores.isEmpty()) {
                for (EventScore score : traineeEventScores) {
                    if(score.getPlace() == 0)
                        totalPlace += traineeVector.size();
                    else
                        totalPlace += score.getPlace();

                    completedEventsTests++;
                }

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;
            courseCompletedCount.add(new Pair<>(trainee.getId(), completedEventsTests * 1.0));

        }

        averagePlacement.sort(new OverviewView.SortByAveragePlacement());
        Collections.reverse(averagePlacement);
        //Put inactive trainees at the back of the list here
        List<Pair<Integer, Double>> tmpList = averagePlacement.stream()
                                                .filter(b -> !traineeVector.get(b.getKey()).isActive())
                                                .collect(Collectors.toList());
        averagePlacement.removeAll(tmpList);
        averagePlacement.addAll(tmpList);

        int num = 100;
        //Holds the trainee ID and the physical events' point value for later use
        Vector<Pair<Integer, Integer>> physicalEventsPoints = new Vector<>();
        for(Pair<Integer, Double> pair : averagePlacement) {

            physicalEventsPoints.add(new Pair<>(traineeVector.get(pair.getKey()).getId(), num));
            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        Vector<Pair<Integer, Integer>> finalEvalPoints = new Vector<>();
        //Take into account the final evaluation score modifications, if applicable
        if(controller.getCurrentSession().getCurrentDay() == 10){

            counter = 0;
            for(Pair<Trainee, Double> pair : sortTraineeVector){

                //Skips this step for inactive trainees
                if(!pair.getKey().isActive())
                    continue;

                Vector<Comment> traineeComments = new Vector<>(controller.getCurrentComments());
                traineeComments.removeIf(comment -> !comment.getIncidentType().equals("Final Evaluation"));
                for(Comment comment : traineeComments){

                    if(comment.getTraineeID() == pair.getKey().getId()){

                        finalEvalPoints.add(new Pair<>(pair.getKey().getId(), Integer.parseInt(comment.getInstructorActions())));
                        Double tmpDouble = pair.getValue() + Integer.parseInt(comment.getInstructorActions());
                        sortTraineeVector.set(counter, new Pair<>(pair.getKey(), tmpDouble));
                        break;

                    }
                }
                counter++;
            }
        }

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));

        //Moves the inactive trainees to the back of the ranking
        List<Pair<Trainee, Double>> inactiveTrainees = sortTraineeVector.stream()
                                                        .filter(b -> !b.getKey().isActive())
                                                        .collect(Collectors.toList());
        sortTraineeVector.removeAll(inactiveTrainees);
        sortTraineeVector.addAll(inactiveTrainees);

        //END correct placement calculation/////////////////////////////////////////////////////////////////////////////////

        //Process Course Completed Percentage for each trainee
        int eventCount = Objects.requireNonNull(DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession())).size();
        int testCount = Objects.requireNonNull(DBManager.getAllEventsFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession())).size();
        if(eventCount > 0 && testCount > 0){

            int counter1 = 0;
            for(Pair<Integer, Double> pair : courseCompletedCount){

                courseCompletedCount.set(counter1, new Pair<>(pair.getKey(), pair.getValue() / (eventCount + testCount)));
                counter1++;

            }

        }

        //Generate All Trainee Summaries
        double physPointValue = 0;
        int districtFinalEvalPoints = 0;
        if(selectedTrainee == null){

            for(int i = 0; i < sortTraineeVector.size(); i++){

                districtFinalEvalPoints = 0;

                for(Pair<Integer, Integer> pair : physicalEventsPoints){
                    if(pair.getKey() == sortTraineeVector.get(i).getKey().getId()){
                        physPointValue = pair.getValue();
                        break;
                    }
                }
                double percentComplete = 0;
                for(Pair<Integer, Double> pair : courseCompletedCount){
                    if(pair.getKey() == sortTraineeVector.get(i).getKey().getId()){
                        percentComplete = pair.getValue();
                        break;
                    }
                }

                for(Pair<Integer, Integer> pair1 : finalEvalPoints)
                    if(pair1.getKey().equals(sortTraineeVector.get(i).getKey().getId())){
                        districtFinalEvalPoints = pair1.getValue();
                        break;
                    }

                int tmpRank = -1;
                if(sortTraineeVector.get(i).getKey().isActive())
                    tmpRank = i + 1;

                generateIndividualSummary(sortTraineeVector.get(i).getKey(), physPointValue, tmpRank, percentComplete, districtFinalEvalPoints);

            }

            //Generate the selected Trainee Summary
        }else{

            for(Pair<Integer, Integer> pair : physicalEventsPoints){
                if(pair.getKey() == selectedTrainee.getId()){
                    physPointValue = pair.getValue();
                    break;
                }
            }

            int tmpRank = -1;
            if(selectedTrainee.isActive()) {
                for (int i = 0; i < sortTraineeVector.size(); i++) {
                    if (sortTraineeVector.get(i).getKey().getId() == selectedTrainee.getId()){
                        tmpRank = i + 1;
                        break;
                    }
                }
            }

            for(Pair<Integer, Integer> pair1 : finalEvalPoints)
                if(pair1.getKey().equals(selectedTrainee.getId())){
                    districtFinalEvalPoints = pair1.getValue();
                    break;
                }

            double percentComplete = 0;
            for(Pair<Integer, Double> pair : courseCompletedCount){
                if(pair.getKey() == selectedTrainee.getId()){
                    percentComplete = pair.getValue();
                    break;
                }
            }

            generateIndividualSummary(selectedTrainee, physPointValue, tmpRank, percentComplete, districtFinalEvalPoints);

        }

        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\IndividualSummaries\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Generates a individual summary of the selected trainee, with scores, comments, and overall performance.
     * @param trainee
     * @param physicalEventPoints
     * @param classRank
     * @param percentComplete
     * @param finalEvalPoints
     */
    private void generateIndividualSummary(Trainee trainee, double physicalEventPoints, int classRank,
                                          double percentComplete, int finalEvalPoints){

        Session currentSession = controller.getCurrentSession();
        Vector<Test> tests = DBManager.getAllTestsFromSession(currentSession.getYear(), currentSession.getSession());
        Vector<Event> events = DBManager.getAllEventsFromSession(currentSession.getYear(), currentSession.getSession());
        Vector<TestScore> testScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
        Vector<EventScore> eventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
        Vector<Comment> comments = DBManager.getAllCommentsFromTID(trainee.getId());

        String districtNameNoSpace = trainee.getDistrictChoice().split(" - ")[0].replaceAll("\\s", "_");

        File otherCheckDir = new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                "\\District_Summaries\\" + districtNameNoSpace + "_Individual_Summaries\\");

        if(!otherCheckDir.exists()){
            if(!otherCheckDir.mkdir())
                System.exit(1);
        }

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(trainee.getFirstName() + "_" + trainee.getLastName() +
                                                              "_Report");

            //Styling
            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Calibri");

            XSSFFont headerFont = workbook.createFont();
            headerFont.setFontName("Calibri");
            headerFont.setBold(true);

            XSSFFont otherHeaderFont = workbook.createFont();
            otherHeaderFont.setFontName("Calibri");
            otherHeaderFont.setBold(true);
            otherHeaderFont.setColor(IndexedColors.WHITE.getIndex());

            XSSFFont commentTitleFont = workbook.createFont();
            commentTitleFont.setBold(true);
            commentTitleFont.setFontHeightInPoints((short)14);
            commentTitleFont.setFontName("Calibri");

            CellStyle nameStyle = workbook.createCellStyle();
            nameStyle.setFont(nameFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle notesStyleEven = workbook.createCellStyle();
            notesStyleEven.setWrapText(true);
            notesStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle notesStyleOdd = workbook.createCellStyle();
            notesStyleOdd.setWrapText(true);
            notesStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            notesStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            notesStyleOdd.setAlignment(HorizontalAlignment.CENTER);

            CellStyle boldCenterStyle = workbook.createCellStyle();
            boldCenterStyle.setFont(headerFont);
            boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle boldCenterPercentStyle = workbook.createCellStyle();
            boldCenterPercentStyle.setFont(headerFont);
            boldCenterPercentStyle.setAlignment(HorizontalAlignment.CENTER);
            XSSFDataFormat df = workbook.createDataFormat();
            boldCenterPercentStyle.setDataFormat(df.getFormat("0.00%"));

            CellStyle centerStyleEven = workbook.createCellStyle();
            centerStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle tableHeaderStyle = workbook.createCellStyle();
            tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
            tableHeaderStyle.setFont(otherHeaderFont);

            CellStyle commentTitleStyle = workbook.createCellStyle();
            commentTitleStyle.setFont(commentTitleFont);

            CellStyle commentStyle = workbook.createCellStyle();
            commentStyle.setWrapText(true);
            commentStyle.setBorderBottom(BorderStyle.DASHED);

            CellStyle commentCenterStyle = workbook.createCellStyle();
            commentCenterStyle.setAlignment(HorizontalAlignment.CENTER);
            commentCenterStyle.setBorderBottom(BorderStyle.DASHED);

            CellStyle totalsStyle = workbook.createCellStyle();
            totalsStyle.setAlignment(HorizontalAlignment.RIGHT);
            totalsStyle.setFont(headerFont);


            //Start Document Construction
            int rowCount = 0;
            int columnCount = 0;

            //Output Trainee Info
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(trainee.getFullName());
            cell.setCellStyle(nameStyle);

            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("District: " + trainee.getDistrictChoice());
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            String[] dates = trainee.getBirthDate().split("/");
            cell.setCellValue("Age: " + Period.between(LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[0]),
                                                        Integer.parseInt(dates[1])), LocalDate.now()).getYears());
            cell.setCellStyle(headerStyle);

            //Output Trainee Physical Event Placements
            rowCount++;
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Physical Event");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Place");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Notes");
            cell.setCellStyle(tableHeaderStyle);

            double averageScore = 0;
            int counter = 0;
            int currentIndex = 0;
            for(Event event : Objects.requireNonNull(events)){

                columnCount = 0;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(event.getName());
                cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleEven : centerStyleOdd);
                cell = row.createCell(columnCount++);
                cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleEven : centerStyleOdd);
                boolean isFound = false;
                for(EventScore score : eventScores){
                    if(score.getEventID() == event.getEventID()){
                        if(score.getPlace() == 0){
                            cell.setCellValue("DNF");
                            averageScore += controller.getCurrentTrainees().size();
                        }else{
                            cell.setCellValue(score.getPlace());
                            averageScore += score.getPlace();
                        }
                        counter++;
                        isFound = true;
                        break;
                    }
                }
                if(!isFound)
                    cell.setCellValue("N/A");

                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(event.getNotes());
                cell.setCellStyle(currentIndex % 2 == 0 ? notesStyleEven : notesStyleOdd);

                currentIndex++;

            }
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Average Place:");
            cell.setCellStyle(totalsStyle);
            cell = row.createCell(columnCount);
            if(counter == 0)
                cell.setCellValue("ERROR");
            else
                cell.setCellValue(new BigDecimal(averageScore / counter * 1.0).round(new MathContext(4))
                                                                                .doubleValue());
            cell.setCellStyle(boldCenterStyle);

            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Physical Events Points:");
            cell.setCellStyle(totalsStyle);
            cell = row.createCell(columnCount, CellType.NUMERIC);
            cell.setCellValue(physicalEventPoints);
            cell.setCellStyle(boldCenterStyle);

            rowCount++;
            columnCount = 0;

            //Output Trainee Total Points
            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Course Section");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Points");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Points Possible");
            cell.setCellStyle(tableHeaderStyle);

            columnCount = 0;
            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Physical Events Points");
            cell.setCellStyle(centerStyleEven);
            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(physicalEventPoints);
            cell.setCellStyle(centerStyleEven);
            double totalPoints = physicalEventPoints;
            cell = row.createCell(columnCount, CellType.NUMERIC);
            double totalPossiblePoints = 100;
            cell.setCellValue(totalPossiblePoints);
            cell.setCellStyle(centerStyleEven);

            currentIndex = 0;
            for(Test test : Objects.requireNonNull(tests)){

                columnCount = 0;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(test.getName());
                cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleOdd : centerStyleEven);

                boolean testScoreFound = false;
                for(TestScore score : testScores){
                    if(score.getTestID() == test.getTestID()){
                        cell = row.createCell(columnCount++, CellType.NUMERIC);
                        cell.setCellValue(score.getScore());
                        cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleOdd : centerStyleEven);
                        totalPoints += score.getScore();
                        testScoreFound = true;
                        break;
                    }
                }
                if(!testScoreFound){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue("N/A");
                    cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleOdd : centerStyleEven);

                }

                totalPossiblePoints += test.getPoints();
                cell = row.createCell(columnCount, CellType.NUMERIC);
                cell.setCellValue(test.getPoints());
                cell.setCellStyle(currentIndex % 2 == 0 ? centerStyleOdd : centerStyleEven);

                currentIndex++;

            }

            columnCount = 0;

            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Instructor Final Eval");
            cell.setCellStyle(totalsStyle);

            cell = row.createCell(columnCount, CellType.NUMERIC);
            cell.setCellValue(finalEvalPoints);
            totalPoints += finalEvalPoints;
            cell.setCellStyle(boldCenterStyle);

            columnCount = 0;
            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total Points:");
            cell.setCellStyle(totalsStyle);
            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(totalPoints);
            cell.setCellStyle(boldCenterStyle);
            cell = row.createCell(columnCount, CellType.NUMERIC);
            cell.setCellValue(totalPossiblePoints);
            cell.setCellStyle(boldCenterStyle);
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Class Rank:");
            cell.setCellStyle(totalsStyle);
            cell = row.createCell(columnCount);
            if(classRank == -1)
                cell.setCellValue("INACTIVATED");
            else
                cell.setCellValue(classRank);
            cell.setCellStyle(boldCenterStyle);
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("% of Course Completed:");
            cell.setCellStyle(totalsStyle);
            cell = row.createCell(columnCount);
            cell.setCellValue(Double.valueOf(BigDecimal.valueOf(percentComplete)
                                                            .setScale(4, RoundingMode.HALF_UP).toString()));
            cell.setCellStyle(boldCenterPercentStyle);

            rowCount++;

            //Sizes the columns to fit the data
            sheet.setColumnWidth(0, 15500);
            sheet.autoSizeColumn(1);
            sheet.setColumnWidth(2, 5000);

            //Output Trainee's Comments
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Instructor Comments");
            cell.setCellStyle(commentTitleStyle);
            row = sheet.createRow(rowCount++);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Comment");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Date");
            cell.setCellStyle(tableHeaderStyle);
            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Instructor");
            cell.setCellStyle(tableHeaderStyle);

            for(Comment comment : Objects.requireNonNull(comments)){

                columnCount = 0;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue("Day " + comment.getCurrentDay() + ": " + comment.getIncidentType() + "\n" +
                                  comment.getIncidentDescription() + "\n" + comment.getInstructorActions() + "\n" +
                                  comment.getNextSteps());
                cell.setCellStyle(commentStyle);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comment.getDate());
                cell.setCellStyle(commentCenterStyle);
                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(comment.getInstructorName());
                cell.setCellStyle(commentCenterStyle);

            }

            sheet.autoSizeColumn(1);

            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\District_Summaries\\" + districtNameNoSpace + "_Individual_Summaries\\" + trainee.getLastName()
                    + "_" + trainee.getFirstName() + "_Report.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            System.out.println(trainee.getFullName() + " Complete");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates and saves an excel sheet with all trainee first names, last names, and district/sector.
     */
    public void generateAveryList(){

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("AveryList");

            //Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setAlignment(HorizontalAlignment.CENTER);

            //Makes header row
            int rowCount = 0;
            int columnCount = 0;
            Row headerRow = sheet.createRow(rowCount);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("FIRST NAME");
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("LAST NAME");
            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("DISTRICT/SECTOR");

            //Makes the data entry rows
            for(Trainee trainee : controller.getCurrentTrainees()){

                Row row = sheet.createRow(++rowCount);
                columnCount = 0;
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellStyle(normalStyle);
                cell.setCellValue(trainee.getFirstName());
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellStyle(normalStyle);
                cell.setCellValue(trainee.getLastName());
                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellStyle(normalStyle);
                cell.setCellValue(trainee.getDistrictChoice());

            }

            for(int i = 0; i < 3; i++)
                sheet.autoSizeColumn(i);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Avery_NameTent_NameTag_List.xlsx");
            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Avery_NameTent_NameTag_List.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Handles sorting the trainees into their district or all districts before generating the summaries.
     * @param selectedDistrict
     */
    public void preProcessDistrictSummaries(District selectedDistrict){

        File checkCertDir = new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                "\\District_Summaries\\");
        if(!checkCertDir.exists()){
            if(!checkCertDir.mkdir())
                System.exit(1);
        }

        //Gather data from trainees to create the correct placement/////////////////////////////////////////////////////////
        Vector<Trainee> traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession());

        Vector<Double> traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        Vector<Pair<Integer, Double>> courseCompletedCount = new Vector<>(); //Holds the count of completed events and tests for each trainee
        int counter = 0;
        for(Trainee trainee : Objects.requireNonNull(traineeVector)){

            int completedEventsTests = 0;

            double totalScore = 0;
            Vector<TestScore> traineeTestScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
            Vector<EventScore> traineeEventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
            if(!traineeTestScores.isEmpty()) {
                for (TestScore score : traineeTestScores) {
                    totalScore += score.getScore();
                    completedEventsTests++;
                }

            }
            traineeTotalScores.add(totalScore);
            int totalPlace = 0;
            if(!traineeEventScores.isEmpty()) {
                for (EventScore score : traineeEventScores) {
                    if(score.getPlace() == 0)
                        totalPlace += traineeVector.size();
                    else
                        totalPlace += score.getPlace();

                    completedEventsTests++;
                }

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;
            courseCompletedCount.add(new Pair<>(trainee.getId(), completedEventsTests * 1.0));

        }

        averagePlacement.sort(new OverviewView.SortByAveragePlacement());
        Collections.reverse(averagePlacement);
        //Put inactive trainees at the back of the list here
        List<Pair<Integer, Double>> tmpList = averagePlacement.stream()
                .filter(b -> !traineeVector.get(b.getKey()).isActive())
                .collect(Collectors.toList());
        averagePlacement.removeAll(tmpList);
        averagePlacement.addAll(tmpList);

        int num = 100;
        //Holds the trainee ID and the physical events' point value for later use
        Vector<Pair<Integer, Integer>> physicalEventsPoints = new Vector<>();
        for(Pair<Integer, Double> pair : averagePlacement) {

            physicalEventsPoints.add(new Pair<>(traineeVector.get(pair.getKey()).getId(), num));
            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        Vector<Pair<Integer, Integer>> finalEvalPoints = new Vector<>();
        //Take into account the final evaluation score modifications, if applicable
        if(controller.getCurrentSession().getCurrentDay() == 10){

            counter = 0;
            for(Pair<Trainee, Double> pair : sortTraineeVector){

                //Skips this step for inactive trainees
                if(!pair.getKey().isActive())
                    continue;

                Vector<Comment> traineeComments = new Vector<>(controller.getCurrentComments());
                traineeComments.removeIf(comment -> !comment.getIncidentType().equals("Final Evaluation"));
                for(Comment comment : traineeComments){

                    if(comment.getTraineeID() == pair.getKey().getId()){

                        finalEvalPoints.add(new Pair<>(pair.getKey().getId(), Integer.parseInt(comment.getInstructorActions())));
                        Double tmpDouble = pair.getValue() + Integer.parseInt(comment.getInstructorActions());
                        sortTraineeVector.set(counter, new Pair<>(pair.getKey(), tmpDouble));
                        break;

                    }
                }
                counter++;
            }
        }

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));

        //Moves the inactive trainees to the back of the ranking
        List<Pair<Trainee, Double>> inactiveTrainees = sortTraineeVector.stream()
                .filter(b -> !b.getKey().isActive())
                .collect(Collectors.toList());
        sortTraineeVector.removeAll(inactiveTrainees);
        sortTraineeVector.addAll(inactiveTrainees);
        //END correct placement calculation/////////////////////////////////////////////////////////////////////////////////

        //Records the class ranks
        Vector<Pair<Integer, Integer>> classRanks = new Vector<>(); //Holds the traineeID and their rank
        for(int i = 0; i < sortTraineeVector.size(); i++)
            classRanks.add(new Pair<>(sortTraineeVector.get(i).getKey().getId(), i + 1));

        //Make All District Summaries and exit method at end of this if statement.
        if(selectedDistrict == null){

            Vector<District> districts = DBManager.getAllDistrictsFromSession(controller.getCurrentSession().getYear(),
                    controller.getCurrentSession().getSession());

            for(District district : Objects.requireNonNull(districts)){

                Vector<Trainee> districtTrainees = new Vector<>();
                Vector<Integer> districtPhysPointValues = new Vector<>();
                Vector<Double>  districtPercentCompletes = new Vector<>();
                Vector<Integer> districtClassRanks = new Vector<>();
                Vector<Integer> districtFinalEvalPoints = new Vector<>();
                for(Pair<Trainee, Double> pair : Objects.requireNonNull(sortTraineeVector))
                    if(pair.getKey().getDistrictChoice().split(" - ")[0].equals(district.getName())){

                        districtTrainees.add(pair.getKey());

                        boolean isFound = false;
                        for(Pair<Integer, Integer> pair1 : physicalEventsPoints){
                            if(pair1.getKey() == pair.getKey().getId()){
                                districtPhysPointValues.add(pair1.getValue());
                                isFound = true;
                                break;
                            }
                        }
                        if(!isFound)
                            districtPhysPointValues.add(0);

                        boolean found = false;
                        for(Pair<Integer, Double> pair1 : courseCompletedCount){
                            if(pair1.getKey() == pair.getKey().getId()){
                                districtPercentCompletes.add(pair1.getValue());
                                found = true;
                                break;
                            }
                        }
                        if(!found)
                            districtPercentCompletes.add(0.0);

                        for(Pair<Integer, Integer> pair1 : classRanks){
                            if(pair1.getKey().equals(pair.getKey().getId())){
                                districtClassRanks.add(pair1.getValue());
                                break;
                            }
                        }

                        found = false;
                        for(Pair<Integer, Integer> pair1 : finalEvalPoints)
                            if(pair1.getKey().equals(pair.getKey().getId())){
                                districtFinalEvalPoints.add(pair1.getValue());
                                found = true;
                                break;
                            }
                        if(!found)
                            districtFinalEvalPoints.add(0);

                    }

                if(districtTrainees.size() > 0)
                    generateDistrictSummaries(districtTrainees, districtPhysPointValues, districtPercentCompletes,
                                                districtClassRanks, districtFinalEvalPoints);

            }

            try {
                Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                        controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                        "\\District_Summaries\\"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;

        }

        //Make the specific district's summary
        Vector<Trainee> districtTrainees = new Vector<>();
        Vector<Integer> districtPhysPointValues = new Vector<>();
        Vector<Double>  districtPercentCompletes = new Vector<>();
        Vector<Integer> districtClassRanks = new Vector<>();
        Vector<Integer> districtFinalEvalPoints = new Vector<>();
        for(Pair<Trainee, Double> traineePair : Objects.requireNonNull(sortTraineeVector))
            if(traineePair.getKey().getDistrictChoice().split(" - ")[0].equals(selectedDistrict.getName())) {

                districtTrainees.add(traineePair.getKey());

                boolean isFound = false;
                for(Pair<Integer, Integer> pair1 : physicalEventsPoints){
                    if(pair1.getKey() == traineePair.getKey().getId()){
                        districtPhysPointValues.add(pair1.getValue());
                        isFound = true;
                        break;
                    }
                }
                if(!isFound)
                    districtPhysPointValues.add(0);

                boolean found = false;
                for(Pair<Integer, Double> pair1 : courseCompletedCount){
                    if(pair1.getKey() == traineePair.getKey().getId()){
                        districtPercentCompletes.add(pair1.getValue());
                        found = true;
                        break;
                    }
                }
                if(!found)
                    districtPercentCompletes.add(0.0);

                for(Pair<Integer, Integer> pair1 : classRanks){
                    if(pair1.getKey().equals(traineePair.getKey().getId())){
                        districtClassRanks.add(pair1.getValue());
                        break;
                    }
                }

                found = false;
                for(Pair<Integer, Integer> pair1 : finalEvalPoints)
                    if(pair1.getKey().equals(traineePair.getKey().getId())){
                        districtFinalEvalPoints.add(pair1.getValue());
                        found = true;
                        break;
                    }
                if(!found)
                    districtFinalEvalPoints.add(0);

            }

        if(districtTrainees.size() > 0)
            generateDistrictSummaries(districtTrainees, districtPhysPointValues, districtPercentCompletes,
                                        districtClassRanks, districtFinalEvalPoints);

        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\District_Summaries\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Generates a district summary for the given district. If the given district is null, generate all summaries.
     * Trainee Listings are in order of rank.
     * @param trainees
     * @param physPointValues
     * @param percentCompletes
     * @param classRanks
     * @param finalEvalPoints
     */
    private void generateDistrictSummaries(Vector<Trainee> trainees, Vector<Integer> physPointValues,
                                           Vector<Double> percentCompletes, Vector<Integer> classRanks,
                                           Vector<Integer> finalEvalPoints){

        String districtName = trainees.get(0).getDistrictChoice().split(" - ")[0];
        Vector<Event> events = DBManager.getAllEventsFromSession(controller.getCurrentSession().getYear(),
                                                                 controller.getCurrentSession().getSession());
        Vector<Test> tests = DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                                                              controller.getCurrentSession().getSession());

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(districtName + " Summary");

            String districtNameNoSpace = districtName.replaceAll("\\s", "_");

            //Style
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short)20);
            titleFont.setFontName("Calibri");

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Calibri");

            XSSFFont eventStyleFont = workbook.createFont();
            eventStyleFont.setBold(true);
            eventStyleFont.setFontName("Calibri");
            eventStyleFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(headerFont);

            CellStyle percentStyleOdd = workbook.createCellStyle();
            percentStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            XSSFDataFormat df = workbook.createDataFormat();
            percentStyleOdd.setDataFormat(df.getFormat("0.00%"));
            percentStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            percentStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle percentStyleEven = workbook.createCellStyle();
            percentStyleEven.setDataFormat(df.getFormat("0.00%"));
            percentStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle centerStyleEven = workbook.createCellStyle();
            centerStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle boldCenterStyleOdd = workbook.createCellStyle();
            boldCenterStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            boldCenterStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            boldCenterStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            boldCenterStyleOdd.setFont(headerFont);

            CellStyle boldCenterStyleEven = workbook.createCellStyle();
            boldCenterStyleEven.setAlignment(HorizontalAlignment.CENTER);
            boldCenterStyleEven.setFont(headerFont);

            CellStyle eventStyle = workbook.createCellStyle();
            eventStyle.setRotation((short)90);
            eventStyle.setFont(headerFont);
            eventStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
            eventStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            eventStyle.setFont(eventStyleFont);

            CellStyle pointsHeader = workbook.createCellStyle();
            pointsHeader.setRotation((short)90);
            pointsHeader.setFont(headerFont);

            XSSFFont pointsPossibleFont = workbook.createFont();
            pointsPossibleFont.setColor(IndexedColors.WHITE.index);
            pointsPossibleFont.setFontName("Calibri");

            CellStyle pointsPossibleStyle = workbook.createCellStyle();
            pointsPossibleStyle.setAlignment(HorizontalAlignment.CENTER);
            pointsPossibleStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
            pointsPossibleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            pointsPossibleStyle.setFont(pointsPossibleFont);

            int rowCount = 2;
            int columnCount = 1;

            //Makes header row
            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("First");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Last");
            cell.setCellStyle(headerStyle);

            for(Event event : Objects.requireNonNull(events)){

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(" " + event.getName() + " Place");
                cell.setCellStyle(eventStyle);

            }

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Average Event Place");
            cell.setCellStyle(eventStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Physical Event Points");
            cell.setCellStyle(pointsHeader);

            for(Test test : Objects.requireNonNull(tests)){

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(" " + test.getName());
                cell.setCellStyle(pointsHeader);

            }

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Instructor Final Eval");
            cell.setCellStyle(pointsHeader);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Rank");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("% of Course Completed");
            cell.setCellStyle(headerStyle);

            //Make points possible row
            Row row = sheet.createRow(rowCount++);
            columnCount = 1;
            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Points Possible");
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);

            for(int i = 0; i < events.size(); i++){

                cell = row.createCell(columnCount++);
                cell.setCellStyle(pointsPossibleStyle);

            }

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(100);
            cell.setCellStyle(pointsPossibleStyle);

            int totalPossiblePoints = 100;
            for(Test test : Objects.requireNonNull(tests)){

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(test.getPoints());
                cell.setCellStyle(pointsPossibleStyle);
                totalPossiblePoints += test.getPoints();

            }

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(0);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(totalPossiblePoints);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);
            cell = row.createCell(columnCount);
            cell.setCellStyle(pointsPossibleStyle);

            //Fill columns with each trainee's data
            for(int i = 0; i < trainees.size(); i++){

                columnCount = 1;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getFirstName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getLastName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills with Event Values
                Vector<EventScore> eventScores = DBManager.getAllEventScoresFromTraineeID(trainees.get(i).getId());
                double eventSum = 0;
                int counter = 0;
                for(Event event : events){

                    cell = row.createCell(columnCount++);
                    cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                    boolean found = false;
                    for(EventScore score : eventScores)
                        if(score.getEventID() == event.getEventID()){
                            if(score.getPlace() == 0){
                                cell.setCellValue("DNF");
                                eventSum += controller.getCurrentTrainees().size();
                            }else{
                                cell.setCellValue(score.getPlace());
                                eventSum += score.getPlace();
                            }

                            counter++;
                            found = true;
                            break;
                        }
                    if(!found)
                        cell.setCellValue("N/A");

                }

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellStyle(i % 2 == 0 ? boldCenterStyleEven : boldCenterStyleOdd);
                if(counter == 0)
                    cell.setCellValue(0);
                else
                    cell.setCellValue(new BigDecimal(eventSum / counter).round(new MathContext(4)).doubleValue());

                double traineeTotalPoints = physPointValues.get(i);
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(physPointValues.get(i));
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills with Test Values
                Vector<TestScore> testScores = DBManager.getAllTestScoresFromTraineeID(trainees.get(i).getId());
                for(Test test : tests){

                    cell = row.createCell(columnCount++);
                    cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                    boolean found = false;
                    for(TestScore score : testScores)
                        if(score.getTestID() == test.getTestID()){
                            cell.setCellValue(score.getScore());
                            traineeTotalPoints += score.getScore();
                            found = true;
                            break;
                        }
                    if(!found)
                        cell.setCellValue("N/A");

                }

                //Fills Instructor Final Eval Points
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(finalEvalPoints.get(i));
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills Total Points
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(traineeTotalPoints);
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills Class Rank
                cell = row.createCell(columnCount++);
                if(trainees.get(i).isActive())
                    cell.setCellValue(classRanks.get(i));
                else
                    cell.setCellValue("INACTIVATED");
                cell.setCellStyle(i % 2 == 0 ? boldCenterStyleEven : boldCenterStyleOdd);

                //Fills % of Course Completed
                cell = row.createCell(columnCount);
                cell.setCellValue(Double.valueOf(BigDecimal.valueOf(percentCompletes.get(i) / (tests.size() + events.size()))
                                                            .setScale(4, RoundingMode.HALF_UP).toString()));
                cell.setCellStyle(i % 2 == 0 ? percentStyleEven : percentStyleOdd);

            }

            for(int i = 1; i < 9 + tests.size() + events.size(); i++)
                sheet.autoSizeColumn(i);

            //Makes title row
            Row titleRow = sheet.createRow(0);
            cell = titleRow.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " " + districtName + " Summary");
            cell.setCellStyle(titleStyle);

            //Save
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\District_Summaries\\" + districtNameNoSpace + "_Summary.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            System.out.println(districtName + " Complete");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Pre-processes all data needed in the overall summary report.
     */
    public void preProcessOverallSummary(){

        //Gather data from trainees to create the correct placement/////////////////////////////////////////////////////////
        Vector<Trainee> traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession());

        Vector<Double> traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        Vector<Pair<Integer, Double>> courseCompletedCount = new Vector<>(); //Holds the count of completed events and tests for each trainee
        int counter = 0;
        for(Trainee trainee : Objects.requireNonNull(traineeVector)){

            int completedEventsTests = 0;

            double totalScore = 0;
            Vector<TestScore> traineeTestScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
            Vector<EventScore> traineeEventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
            if(!traineeTestScores.isEmpty()) {
                for (TestScore score : traineeTestScores) {
                    totalScore += score.getScore();
                    completedEventsTests++;
                }

            }
            traineeTotalScores.add(totalScore);
            int totalPlace = 0;
            if(!traineeEventScores.isEmpty()) {
                for (EventScore score : traineeEventScores) {
                    if(score.getPlace() == 0)
                        totalPlace += traineeVector.size();
                    else
                        totalPlace += score.getPlace();

                    completedEventsTests++;
                }

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;
            courseCompletedCount.add(new Pair<>(trainee.getId(), completedEventsTests * 1.0));

        }

        averagePlacement.sort(new OverviewView.SortByAveragePlacement());
        Collections.reverse(averagePlacement);
        //Put inactive trainees at the back of the list here
        List<Pair<Integer, Double>> tmpList = averagePlacement.stream()
                .filter(b -> !traineeVector.get(b.getKey()).isActive())
                .collect(Collectors.toList());
        averagePlacement.removeAll(tmpList);
        averagePlacement.addAll(tmpList);

        int num = 100;
        //Holds the trainee ID and the physical events' point value for later use
        Vector<Pair<Integer, Integer>> physicalEventsPoints = new Vector<>();
        for(Pair<Integer, Double> pair : averagePlacement) {

            physicalEventsPoints.add(new Pair<>(traineeVector.get(pair.getKey()).getId(), num));
            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        Vector<Pair<Integer, Integer>> finalEvalPoints = new Vector<>();
        //Take into account the final evaluation score modifications, if applicable
        if(controller.getCurrentSession().getCurrentDay() == 10){

            counter = 0;
            for(Pair<Trainee, Double> pair : sortTraineeVector){

                //Skips this step for inactive trainees
                if(!pair.getKey().isActive())
                    continue;

                Vector<Comment> traineeComments = new Vector<>(controller.getCurrentComments());
                traineeComments.removeIf(comment -> !comment.getIncidentType().equals("Final Evaluation"));
                for(Comment comment : traineeComments){

                    if(comment.getTraineeID() == pair.getKey().getId()){

                        finalEvalPoints.add(new Pair<>(pair.getKey().getId(), Integer.parseInt(comment.getInstructorActions())));
                        Double tmpDouble = pair.getValue() + Integer.parseInt(comment.getInstructorActions());
                        sortTraineeVector.set(counter, new Pair<>(pair.getKey(), tmpDouble));
                        break;

                    }
                }
                counter++;
            }
        }

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));

        //Moves the inactive trainees to the back of the ranking
        List<Pair<Trainee, Double>> inactiveTrainees = sortTraineeVector.stream()
                .filter(b -> !b.getKey().isActive())
                .collect(Collectors.toList());
        sortTraineeVector.removeAll(inactiveTrainees);
        sortTraineeVector.addAll(inactiveTrainees);
        //END correct placement calculation/////////////////////////////////////////////////////////////////////////////////

        //Records the class ranks
        Vector<Integer> classRanks = new Vector<>(); //Holds the traineeID and their rank
        for(int i = 0; i < sortTraineeVector.size(); i++)
            classRanks.add(i + 1);


        Vector<Trainee> trainees = new Vector<>();
        Vector<Integer> physPointValues = new Vector<>();
        Vector<Double>  percentCompletes = new Vector<>();
        Vector<Integer> newFinalEvalPoints = new Vector<>();

        for(Pair<Trainee, Double> pair : Objects.requireNonNull(sortTraineeVector)){

            trainees.add(pair.getKey());

            boolean isFound = false;
            for(Pair<Integer, Integer> pair1 : physicalEventsPoints){
                if(pair1.getKey() == pair.getKey().getId()){
                    physPointValues.add(pair1.getValue());
                    isFound = true;
                    break;
                }
            }
            if(!isFound)
                physPointValues.add(0);

            boolean found = false;
            for(Pair<Integer, Double> pair1 : courseCompletedCount){
                if(pair1.getKey() == pair.getKey().getId()){
                    percentCompletes.add(pair1.getValue());
                    found = true;
                    break;
                }
            }
            if(!found)
                percentCompletes.add(0.0);

            found = false;
            for(Pair<Integer, Integer> pair1 : finalEvalPoints)
                if(pair1.getKey().equals(pair.getKey().getId())){
                    newFinalEvalPoints.add(pair1.getValue());
                    found = true;
                    break;
                }
            if(!found)
                newFinalEvalPoints.add(0);

        }

        if(sortTraineeVector.size() > 0)
            generateOverallSummary(trainees, physPointValues, percentCompletes, classRanks, newFinalEvalPoints);

        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Overall_Summary.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates a summary of all trainee scores and stats.
     * @param trainees
     * @param physPointValues
     * @param percentCompletes
     * @param classRanks
     * @param finalEvalPoints
     */
    public void generateOverallSummary(Vector<Trainee> trainees, Vector<Integer> physPointValues,
                                       Vector<Double> percentCompletes, Vector<Integer> classRanks,
                                       Vector<Integer> finalEvalPoints){

        Vector<Event> events = DBManager.getAllEventsFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession());
        Vector<Test> tests = DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession());

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Overall Summary");

            //Style
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short)20);
            titleFont.setFontName("Calibri");

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Calibri");

            XSSFFont eventStyleFont = workbook.createFont();
            eventStyleFont.setBold(true);
            eventStyleFont.setFontName("Calibri");
            eventStyleFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(headerFont);

            CellStyle percentStyleOdd = workbook.createCellStyle();
            percentStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            XSSFDataFormat df = workbook.createDataFormat();
            percentStyleOdd.setDataFormat(df.getFormat("0.00%"));
            percentStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            percentStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle percentStyleEven = workbook.createCellStyle();
            percentStyleEven.setDataFormat(df.getFormat("0.00%"));
            percentStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle centerStyleEven = workbook.createCellStyle();
            centerStyleEven.setAlignment(HorizontalAlignment.CENTER);

            CellStyle boldCenterStyleOdd = workbook.createCellStyle();
            boldCenterStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            boldCenterStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            boldCenterStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            boldCenterStyleOdd.setFont(headerFont);

            CellStyle boldCenterStyleEven = workbook.createCellStyle();
            boldCenterStyleEven.setAlignment(HorizontalAlignment.CENTER);
            boldCenterStyleEven.setFont(headerFont);

            CellStyle eventStyle = workbook.createCellStyle();
            eventStyle.setRotation((short)90);
            eventStyle.setFont(headerFont);
            eventStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
            eventStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            eventStyle.setFont(eventStyleFont);

            CellStyle pointsHeader = workbook.createCellStyle();
            pointsHeader.setRotation((short)90);
            pointsHeader.setFont(headerFont);

            XSSFFont pointsPossibleFont = workbook.createFont();
            pointsPossibleFont.setColor(IndexedColors.WHITE.index);
            pointsPossibleFont.setFontName("Calibri");

            CellStyle pointsPossibleStyle = workbook.createCellStyle();
            pointsPossibleStyle.setAlignment(HorizontalAlignment.CENTER);
            pointsPossibleStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
            pointsPossibleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            pointsPossibleStyle.setFont(pointsPossibleFont);

            int rowCount = 2;
            int columnCount = 0;

            //Makes header row
            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Cap#");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("First");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Last");
            cell.setCellStyle(headerStyle);

            for(Event event : Objects.requireNonNull(events)){

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(" " + event.getName() + " Place");
                cell.setCellStyle(eventStyle);

            }

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Average Event Place");
            cell.setCellStyle(eventStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Physical Event Points");
            cell.setCellStyle(pointsHeader);

            for(Test test : Objects.requireNonNull(tests)){

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(" " + test.getName());
                cell.setCellStyle(pointsHeader);

            }

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(" Instructor Final Eval");
            cell.setCellStyle(pointsHeader);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Rank");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("% of Course Completed");
            cell.setCellStyle(headerStyle);

            //Make points possible row
            Row row = sheet.createRow(rowCount++);
            columnCount = 0;
            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Points Possible");
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);

            for(int i = 0; i < events.size(); i++){

                cell = row.createCell(columnCount++);
                cell.setCellStyle(pointsPossibleStyle);

            }

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(100);
            cell.setCellStyle(pointsPossibleStyle);

            int totalPossiblePoints = 100;
            for(Test test : Objects.requireNonNull(tests)){

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(test.getPoints());
                cell.setCellStyle(pointsPossibleStyle);
                totalPossiblePoints += test.getPoints();

            }

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(0);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++, CellType.NUMERIC);
            cell.setCellValue(totalPossiblePoints);
            cell.setCellStyle(pointsPossibleStyle);

            cell = row.createCell(columnCount++);
            cell.setCellStyle(pointsPossibleStyle);
            cell = row.createCell(columnCount);
            cell.setCellStyle(pointsPossibleStyle);

            //Fill columns with each trainee's data
            for(int i = 0; i < trainees.size(); i++){

                columnCount = 0;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(trainees.get(i).getCapNumber());
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getFirstName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getLastName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills with Event Values
                Vector<EventScore> eventScores = DBManager.getAllEventScoresFromTraineeID(trainees.get(i).getId());
                double eventSum = 0;
                int counter = 0;
                for(Event event : events){

                    cell = row.createCell(columnCount++);
                    cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                    boolean found = false;
                    for(EventScore score : eventScores)
                        if(score.getEventID() == event.getEventID()){
                            if(score.getPlace() == 0){
                                cell.setCellValue("DNF");
                                eventSum += controller.getCurrentTrainees().size();
                            }else{
                                cell.setCellValue(score.getPlace());
                                eventSum += score.getPlace();
                            }

                            counter++;
                            found = true;
                            break;
                        }
                    if(!found)
                        cell.setCellValue("N/A");

                }

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellStyle(i % 2 == 0 ? boldCenterStyleEven : boldCenterStyleOdd);
                if(counter == 0)
                    cell.setCellValue(0);
                else
                    cell.setCellValue(new BigDecimal(eventSum / counter).round(new MathContext(4)).doubleValue());

                double traineeTotalPoints = physPointValues.get(i);
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(physPointValues.get(i));
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills with Test Values
                Vector<TestScore> testScores = DBManager.getAllTestScoresFromTraineeID(trainees.get(i).getId());
                for(Test test : tests){

                    cell = row.createCell(columnCount++);
                    cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                    boolean found = false;
                    for(TestScore score : testScores)
                        if(score.getTestID() == test.getTestID()){
                            cell.setCellValue(score.getScore());
                            traineeTotalPoints += score.getScore();
                            found = true;
                            break;
                        }
                    if(!found)
                        cell.setCellValue("N/A");

                }

                //Fills Instructor Final Eval Points
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(finalEvalPoints.get(i));
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills Total Points
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(traineeTotalPoints);
                cell.setCellStyle(i % 2 == 0 ? centerStyleEven : centerStyleOdd);

                //Fills Class Rank
                cell = row.createCell(columnCount++);
                if(trainees.get(i).isActive())
                    cell.setCellValue(classRanks.get(i));
                else
                    cell.setCellValue("INACTIVATED");
                cell.setCellStyle(i % 2 == 0 ? boldCenterStyleEven : boldCenterStyleOdd);

                //Fills % of Course Completed
                cell = row.createCell(columnCount);
                cell.setCellValue(Double.valueOf(BigDecimal.valueOf(percentCompletes.get(i) / (tests.size() + events.size()))
                        .setScale(4, RoundingMode.HALF_UP).toString()));
                cell.setCellStyle(i % 2 == 0 ? percentStyleEven : percentStyleOdd);

            }

            for(int i = 1; i < 9 + tests.size() + events.size(); i++)
                sheet.autoSizeColumn(i);

            //Makes title row
            Row titleRow = sheet.createRow(0);
            cell = titleRow.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Overall Summary");
            cell.setCellStyle(titleStyle);

            //Save
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Overall_Summary.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * Creates an excel sheet with all trainees and their info, a sheet with all instructor comments, and a sheet with
     * the scores and placements of all tests and events.
     */
    public void exportData(){

        Session currentSession = controller.getCurrentSession();

        File checkCertDir = new File(System.getProperty("user.dir") + "\\Reports\\Data_Exports\\");
        if(!checkCertDir.exists()){
            if(!checkCertDir.mkdir())
                System.exit(1);
        }

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();

            //Style
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFont(headerFont);

            CellStyle centerStyleEven = workbook.createCellStyle();
            centerStyleEven.setAlignment(HorizontalAlignment.CENTER);
            centerStyleEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);

            //Build Trainee Info Sheet
            XSSFSheet traineeSheet = workbook.createSheet("Trainee Info");

            //Pre-processing
            Vector<Trainee> trainees = Objects.requireNonNull(DBManager.getAllTraineesFromYear(currentSession.getYear()));
            Vector<String> columnNames = Objects.requireNonNull(DBManager.getTraineeTableColumnNames());

            HashMap<Integer, Double> attendanceHours = new HashMap<>(); //For id to hours lookup purposes
            HashMap<Integer, Pair<String, String>> traineeNamesByID = new HashMap<>(); //For id to name lookup purposes
            for(Trainee trainee : trainees){

                traineeNamesByID.put(trainee.getId(), new Pair<>(trainee.getFirstName(), trainee.getLastName()));

                Vector<AttendanceDay> traineeDays = DBManager.getAllAttendanceDaysFromTID(trainee.getId());
                if(traineeDays == null){
                    attendanceHours.put(trainee.getId(), 0.0);
                    continue;
                }
                double sum = 0;
                for(AttendanceDay day : traineeDays)
                    sum += day.getHours();
                attendanceHours.put(trainee.getId(), sum);

            }

            //Header row
            int rowCount = 0;
            int columnCount = 0;

            //List of indecies to skip, not needed in the data dump.
            HashSet<Integer> skipIndex = new HashSet<>(Arrays.asList(0, 12));

            Row headerRow = traineeSheet.createRow(rowCount++);
            for(int i = 0; i < columnNames.size(); i++){

                if(skipIndex.contains(i))
                    continue;

                Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(columnNames.get(i));
                cell.setCellStyle(headerStyle);

            }
            Cell cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("attendanceHours");
            cell.setCellStyle(headerStyle);

            for(int i = 0; i < 51; i++)
                traineeSheet.autoSizeColumn(i);

            traineeSheet.setColumnWidth(23, 2000);

            //Fill Table

            for(int i = 0; i < trainees.size(); i++){

                columnCount = 0;
                Row row = traineeSheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getFirstName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getMiddleName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getLastName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getBirthDate());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getCity());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getState());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getPhoneNumber());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getEmail());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainees.get(i).getDistrictChoice());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.BOOLEAN);
                cell.setCellValue(trainees.get(i).isLodging());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(trainees.get(i).getCapNumber());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.BOOLEAN);
                cell.setCellValue(trainees.get(i).isQuestionnaire1Complete());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.BOOLEAN);
                cell.setCellValue(trainees.get(i).isQuestionnaire2Complete());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.BOOLEAN);
                cell.setCellValue(trainees.get(i).isActive());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(trainees.get(i).getYear());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(trainees.get(i).getSession());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                if(trainees.get(i).isQuestionnaire1Complete()){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getShirtSize());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getShortSize());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getSwimSuitSize());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isReturningTrainee());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isReturningTrainee()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getWhyReturning());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getWhyBeStateLG());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getWhatWantLearnTraining());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isJG());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isJG()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getJgInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isOpenWaterLG());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isOpenWaterLG()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getOpenWaterLGInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isPoolLG());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isPoolLG()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getPoolLGInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isEMT());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isEMT()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getEmtInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isOtherAdvancedMedicalTraining());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isOtherAdvancedMedicalTraining()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getAdvancedMedicalTrainingInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isFirstJob());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    if(trainees.get(i).isFirstJob()){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellValue(trainees.get(i).getJobExperienceInfo());
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }else{

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getAnyExtraInfo());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                }else{

                    for(int j = 0; j < 20; j++){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                }

                if(trainees.get(i).isQuestionnaire2Complete()){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getExpectedBiggestTrainingChallengeInfo());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getPreparationInfo());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(trainees.get(i).getMedicalConfidence());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(trainees.get(i).getCprConfidence());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(trainees.get(i).getPhysicalConfidence());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(trainees.get(i).getMentalConfidence());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(trainees.get(i).getPreTrainingSeminarsAttended());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getOrganizedSwimPoloFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getPersonalSwimFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getGymFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getOceanSwimFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getRunningFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(trainees.get(i).getSurfingFreq());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    cell = row.createCell(columnCount++, CellType.BOOLEAN);
                    cell.setCellValue(trainees.get(i).isDisabled());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                }else{

                    for(int j = 0; j < 14; j++){

                        cell = row.createCell(columnCount++, CellType.STRING);
                        cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    }

                }

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(attendanceHours.get(trainees.get(i).getId()));
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

            }


            //Build Instructor Comments Sheet
            XSSFSheet instructorCommentSheet = workbook.createSheet("Instructor Comments");

            Vector<Comment> comments = DBManager.getAllCommentsFromYear(currentSession.getYear());

            //Header row
            rowCount = 0;
            columnCount = 0;
            headerRow = instructorCommentSheet.createRow(rowCount++);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Trainee First Name");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Trainee Last Name");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Date");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Day of Training");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Rotation");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Instructor Name");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Incident Type");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Incident Description");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Instructor Actions");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Next Steps");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Year");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Session");
            cell.setCellStyle(headerStyle);

            for(int i = 0; i < 12; i++)
                instructorCommentSheet.autoSizeColumn(i);

            //Fill rows with comments
            for(int i = 0; i < comments.size(); i++){

                if(comments.get(i).getTraineeID() == 1)
                    continue;

                columnCount = 0;
                Row row = instructorCommentSheet.createRow(rowCount++);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(traineeNamesByID.get(comments.get(i).getTraineeID()).getKey());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(traineeNamesByID.get(comments.get(i).getTraineeID()).getValue());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getDate());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getCurrentDay());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getRotation());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getInstructorName());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getIncidentType());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getIncidentDescription());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getInstructorActions());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getNextSteps());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getYear());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(comments.get(i).getSession());
                cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

            }

            instructorCommentSheet.autoSizeColumn(2);
            instructorCommentSheet.autoSizeColumn(5);
            instructorCommentSheet.autoSizeColumn(6);
            instructorCommentSheet.autoSizeColumn(9);

            //Build Scores and Placements Sheets for each Session
            Vector<Session> sessions = DBManager.getAllSessionsFromYear(currentSession.getYear());

            for(int j = 0; j < Objects.requireNonNull(sessions).size(); j++){

                //Pre-process each session's scores and placements
                currentSession = sessions.get(j);

                Vector<Test> currentTests = DBManager.getAllTestsFromSession(currentSession.getYear(), currentSession.getSession());
                List<Test> tmpTestList = Objects.requireNonNull(currentTests).stream().filter(b -> !b.isScored()).collect(Collectors.toList());
                currentTests.removeAll(tmpTestList);

                Vector<Event> currentEvents = DBManager.getAllEventsFromSession(currentSession.getYear(), currentSession.getSession());
                List<Event> tmpEventList = Objects.requireNonNull(currentEvents).stream().filter(b -> !b.isScored()).collect(Collectors.toList());
                currentEvents.removeAll(tmpEventList);



                XSSFSheet scorePlacementSheet = workbook.createSheet("Session " + currentSession.getSession()
                                                                                            + " Scores and Placements");
                rowCount = 0;
                columnCount = 1;

                //Build header row
                Row row = scorePlacementSheet.createRow(rowCount++);

                int counter = 1;
                for(Test test : currentTests){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(test.getName() + "(" + test.getPoints() + ")");
                    cell.setCellStyle(headerStyle);
                    scorePlacementSheet.autoSizeColumn(counter++);

                }

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue("Graduates' Average Test Score");
                cell.setCellStyle(headerStyle);
                scorePlacementSheet.autoSizeColumn(counter++);


                for(Event event : currentEvents){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(event.getName());
                    cell.setCellStyle(headerStyle);
                    scorePlacementSheet.autoSizeColumn(counter++);

                }

                //Fill sheet with test and event scores for each trainee from that session.
                final Session curSesCopy = currentSession;
                List<Trainee> currentSessionTrainees = Objects.requireNonNull(trainees).stream().filter
                                        (b -> b.getSession() == curSesCopy.getSession()).collect(Collectors.toList());

                double[] scoreAverages = new double[currentTests.size()];
                int[] scoreCounts = new int[currentTests.size()];
                Arrays.fill(scoreAverages, 0);
                Arrays.fill(scoreCounts, 0);

                double totalAverages = 0;
                int activeCount = 0;

                for(int i = 0; i < currentSessionTrainees.size(); i++){

                    columnCount = 0;
                    row = scorePlacementSheet.createRow(rowCount++);
                    cell = row.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(currentSessionTrainees.get(i).getFullName());
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                    Vector<TestScore> tmpTestScores = DBManager.getAllTestScoresFromTraineeID(currentSessionTrainees.get(i).getId());
                    Vector<EventScore> tmpEventScores = DBManager.getAllEventScoresFromTraineeID(currentSessionTrainees.get(i).getId());

                    double traineeTestAvg = 0;
                    double traineeTestPossible = 0;

                    int averageCounter = 0;
                    for(Test test : currentTests){

                        TestScore tmpScore = null;
                        for(TestScore score : tmpTestScores){

                            if(score.getTestID() == test.getTestID()){
                                tmpScore = score;
                                break;
                            }

                        }
                        if(tmpScore != null){

                            cell = row.createCell(columnCount++);
                            cell.setCellValue(tmpScore.getScore());
                            cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);
                            traineeTestAvg += tmpScore.getScore();
                            traineeTestPossible += test.getPoints();
                            scoreAverages[averageCounter] += tmpScore.getScore();
                            scoreCounts[averageCounter]++;

                        }else{

                            cell = row.createCell(columnCount++);
                            cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                        }
                        averageCounter++;

                    }

                    cell = row.createCell(columnCount++, CellType.STRING);
                    cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);
                    if(traineeTestPossible != 0 && currentSessionTrainees.get(i).isActive()){

                        double tmp = traineeTestAvg / traineeTestPossible * 100;
                        totalAverages += tmp;
                        cell.setCellValue(new DecimalFormat("#.##").format(tmp) + "%");
                        activeCount++;
                    }


                    for(Event event : currentEvents){

                        EventScore tmpScore = null;
                        for(EventScore score : tmpEventScores){

                            if(score.getEventID() == event.getEventID()){
                                tmpScore = score;
                                break;
                            }

                        }
                        if(tmpScore != null){

                            cell = row.createCell(columnCount++);
                            cell.setCellValue(tmpScore.getPlace());
                            cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                        }else{

                            cell = row.createCell(columnCount++);
                            cell.setCellStyle(i % 2 == 0 ? centerStyleOdd : centerStyleEven);

                        }

                    }

                }

                columnCount = 0;
                row = scorePlacementSheet.createRow(rowCount);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellStyle(headerStyle);
                cell.setCellValue("Class Test Averages:");

                for(int i = 0; i < scoreAverages.length; i++){

                    cell = row.createCell(columnCount++);
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(new DecimalFormat("#.##").format(scoreAverages[i] / scoreCounts[i]));

                }

                if(activeCount != 0){

                    cell = row.createCell(columnCount);
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(new DecimalFormat("#.##").format(totalAverages / activeCount) + "%");

                }

                scorePlacementSheet.autoSizeColumn(0);

            }

            //Save
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Data_Exports"
                    + "\\Year_" + currentSession.getYear() + "_Data.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            //Open for user
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Data_Exports"
                    + "\\Year_" + currentSession.getYear() + "_Data.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Creates a sheet that displays statistics about each taken test.
     */
    public void generateTestAnalysis(){

        Session currentSession = controller.getCurrentSession();

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet( "Test Analysis");

            //Style
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short)20);
            titleFont.setFontName("Calibri");

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFont(headerFont);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle centerStyleEven = workbook.createCellStyle();
            centerStyleEven.setAlignment(HorizontalAlignment.CENTER);

            int rowCount = 3;
            int columnCount = 1;

            //Table header row
            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Test Name");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Points Possible");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Lowest");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Highest");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Average");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Median");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Standard Deviation");
            cell.setCellStyle(headerStyle);

            //Calculate and print out each test's statistics
            Row row;
            Vector<Test> tests = Objects.requireNonNull(DBManager.getAllTestsFromSession(currentSession.getYear(),
                    currentSession.getSession()));
            tests.removeIf(b -> !b.isScored());

            int counter = 0;
            for(Test test : tests){

                //Calculate Statistics
                Vector<TestScore> scores = DBManager.getAllTestScoresFromTestID(test.getTestID());
                double minScore = 1000;
                double maxScore = -1;
                double averageScore = 0;
                double standardDeviation = 0;
                double sum = 0;
                for(TestScore score : Objects.requireNonNull(scores)){

                    sum += score.getScore();
                    if(score.getScore() < minScore)
                        minScore = score.getScore();
                    if(score.getScore() > maxScore)
                        maxScore = score.getScore();

                }

                averageScore = sum / scores.size();
                for(TestScore score : Objects.requireNonNull(scores))
                    standardDeviation += Math.pow((score.getScore() - averageScore), 2);
                standardDeviation = Math.sqrt(standardDeviation / scores.size());

                Objects.requireNonNull(scores).sort(Comparator.comparing(TestScore::getScore));
                double median = median(scores);
                double iqr = IQR(scores);

                //Print to the table
                columnCount = 1;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(test.getName());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(test.getPoints());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(minScore);
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(maxScore);
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(new BigDecimal(averageScore).round(new MathContext(4)).doubleValue());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(new BigDecimal(median).round(new MathContext(4)).doubleValue());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                cell = row.createCell(columnCount, CellType.NUMERIC);
                cell.setCellValue(new BigDecimal(standardDeviation).round(new MathContext(4)).doubleValue());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleEven : centerStyleOdd);

                counter++;

            }

            for(int i = 0; i < 9; i++)
                sheet.autoSizeColumn(i);

            Row titleRow = sheet.createRow(1);
            cell = titleRow.createCell(1, CellType.STRING);
            cell.setCellValue(currentSession.getYear() + " Session " + currentSession.getSession() +
                    " Scored Test Score Analysis");
            cell.setCellStyle(titleStyle);

            //Save
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    currentSession.getYear() + "_Session_" + currentSession.getSession() +  "\\Test_Analysis.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Test_Analysis.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates and saves an excel sheet with all trainee names, as well as their attendance hours.
     */
    public void generateAttendanceList(){

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Attendance Sheet");

            //Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle fnStyleOdd = workbook.createCellStyle();
            fnStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            fnStyleOdd.setBorderLeft(BorderStyle.THIN);
            fnStyleOdd.setBorderRight(BorderStyle.THIN);
            fnStyleOdd.setBorderBottom(BorderStyle.DASHED);

            CellStyle fnStyleEven = workbook.createCellStyle();
            fnStyleEven.setAlignment(HorizontalAlignment.CENTER);
            fnStyleEven.setBorderLeft(BorderStyle.THIN);
            fnStyleEven.setBorderRight(BorderStyle.THIN);
            fnStyleEven.setBorderBottom(BorderStyle.DASHED);
            fnStyleEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            fnStyleEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle lnStyleOdd = workbook.createCellStyle();
            lnStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            lnStyleOdd.setBorderBottom(BorderStyle.DASHED);
            lnStyleOdd.setBorderRight(BorderStyle.THIN);

            CellStyle lnStyleEven = workbook.createCellStyle();
            lnStyleEven.setAlignment(HorizontalAlignment.CENTER);
            lnStyleEven.setBorderBottom(BorderStyle.DASHED);
            lnStyleEven.setBorderRight(BorderStyle.THIN);
            lnStyleEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            lnStyleEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle hoursOdd = workbook.createCellStyle();
            hoursOdd.setAlignment(HorizontalAlignment.CENTER);
            hoursOdd.setBorderLeft(BorderStyle.THIN);
            hoursOdd.setBorderRight(BorderStyle.THIN);
            hoursOdd.setBorderBottom(BorderStyle.DASHED);

            CellStyle hoursEven = workbook.createCellStyle();
            hoursEven.setAlignment(HorizontalAlignment.CENTER);
            hoursEven.setBorderLeft(BorderStyle.THIN);
            hoursEven.setBorderRight(BorderStyle.THIN);
            hoursEven.setBorderBottom(BorderStyle.DASHED);
            hoursEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            hoursEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            //Makes header row
            int rowCount = 1;
            int columnCount = 0;
            Row headerRow = sheet.createRow(rowCount);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("FIRST NAME");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("LAST NAME");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("DISTRICT");
            cell.setCellStyle(headerStyle);

            if(controller.getCurrentSession().isWeekends()){

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue("Saturday " +
                        getPlaceSuffix(Integer.parseInt(controller.getCurrentSession().getStartDate().split("/")[1])));
                cell.setCellStyle(headerStyle);

                for(int i = 0; i < 3; i++){

                    cell = headerRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue("Sunday");
                    cell.setCellStyle(headerStyle);

                    cell = headerRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue("Saturday");
                    cell.setCellStyle(headerStyle);

                }

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue("Sunday " +
                        getPlaceSuffix(Integer.parseInt(controller.getCurrentSession().getEndDate().split("/")[1])));
                cell.setCellStyle(headerStyle);

            }else{

                String dayOfWeek = controller.getCurrentSession().getFirstDay();

                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue( dayOfWeek + " " +
                        getPlaceSuffix(Integer.parseInt(controller.getCurrentSession().getStartDate().split("/")[1])));
                cell.setCellStyle(headerStyle);

                for(int i = 0; i < 6; i++){

                    dayOfWeek = controller.getNextDayOfWeek(dayOfWeek);
                    cell = headerRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(dayOfWeek);
                    cell.setCellStyle(headerStyle);

                }

                dayOfWeek = controller.getNextDayOfWeek(dayOfWeek);
                cell = headerRow.createCell(columnCount++, CellType.STRING);
                cell.setCellValue( dayOfWeek + " " +
                        getPlaceSuffix(Integer.parseInt(controller.getCurrentSession().getEndDate().split("/")[1])));
                cell.setCellStyle(headerStyle);

            }

            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("TOTAL HOURS");
            cell.setCellStyle(headerStyle);

            class SortByLastName implements Comparator<Trainee>{
                @Override
                public int compare(Trainee o1, Trainee o2) {
                    return o1.getLastName().compareTo(o2.getLastName());
                }
            }

            Vector<Trainee> trainees = Objects.requireNonNull(DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                    controller.getCurrentSession().getSession()));

            Objects.requireNonNull(trainees).sort(new SortByLastName());

            //Makes the data entry rows
            boolean swapper = false;
            for(Trainee trainee : trainees){

                Row row = sheet.createRow(++rowCount);
                columnCount = 0;
                cell = row.createCell(columnCount++, CellType.STRING);
                if(swapper)
                    cell.setCellStyle(fnStyleOdd);
                else
                    cell.setCellStyle(fnStyleEven);
                cell.setCellValue(trainee.getFirstName());

                cell = row.createCell(columnCount++, CellType.STRING);
                if(swapper)
                    cell.setCellStyle(lnStyleOdd);
                else
                    cell.setCellStyle(lnStyleEven);
                cell.setCellValue(trainee.getLastName());

                cell = row.createCell(columnCount++, CellType.STRING);
                if(swapper)
                    cell.setCellStyle(lnStyleOdd);
                else
                    cell.setCellStyle(lnStyleEven);
                cell.setCellValue(trainee.getDistrictChoice().split(" - ")[0]);

                Vector<AttendanceDay> days = DBManager.getAllAttendanceDaysFromTID(trainee.getId());
                double totalHours = 0;
                for(AttendanceDay day : Objects.requireNonNull(days)){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    if(swapper)
                        cell.setCellStyle(hoursOdd);
                    else
                        cell.setCellStyle(hoursEven);
                    cell.setCellValue(day.getHours());
                    totalHours += day.getHours();

                }

                for(int i = 0; i < 8 - days.size(); i++){

                    cell = row.createCell(columnCount++, CellType.STRING);
                    if(swapper)
                        cell.setCellStyle(hoursOdd);
                    else
                        cell.setCellStyle(hoursEven);
                    cell.setCellValue("N/A");

                }

                cell = row.createCell(columnCount, CellType.NUMERIC);
                if(swapper)
                    cell.setCellStyle(hoursOdd);
                else
                    cell.setCellStyle(hoursEven);
                cell.setCellValue(totalHours);

                swapper ^= true;

            }

            for(int i = 0; i < 12; i++)
                sheet.autoSizeColumn(i);

            //Make title row
            Row row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Attendance List " + controller.getCurrentSession().getStartDate() + " - " + controller.getCurrentSession().getEndDate());
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Attendance_List.xlsx");
            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Attendance_List.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates a list displaying the trainees in order of ranking, with their total scores and total percentage.
     */
    public void generateCurrentRankings(){

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //Gather data from trainees to create the correct placement/////////////////////////////////////////////////////////
        Vector<Trainee> traineeVector = DBManager.getAllTraineesFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession());
        class SortByLastName implements Comparator<Trainee>{
            @Override
            public int compare(Trainee o1, Trainee o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        }
        Objects.requireNonNull(traineeVector).sort(new SortByLastName());

        Vector<Double> traineeTotalScores = new Vector<>();
        Vector<Pair<Integer, Double>> averagePlacement = new Vector<>();
        int counter = 0;
        for(Trainee trainee : Objects.requireNonNull(traineeVector)){

            double totalScore = 0;
            Vector<TestScore> traineeTestScores = DBManager.getAllTestScoresFromTraineeID(trainee.getId());
            Vector<EventScore> traineeEventScores = DBManager.getAllEventScoresFromTraineeID(trainee.getId());
            if(!traineeTestScores.isEmpty()) {
                for (TestScore score : traineeTestScores)
                    totalScore += score.getScore();

            }
            traineeTotalScores.add(totalScore);
            int totalPlace = 0;
            if(!traineeEventScores.isEmpty()) {
                for (EventScore score : traineeEventScores)
                    if(score.getPlace() == 0)
                        totalPlace += traineeVector.size();
                    else
                        totalPlace += score.getPlace();

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;

        }

        averagePlacement.sort(new OverviewView.SortByAveragePlacement());
        Collections.reverse(averagePlacement);
        List<Pair<Integer, Double>> tmpList = averagePlacement.stream()
                                                .filter(b -> !traineeVector.get(b.getKey()).isActive())
                                                .collect(Collectors.toList());
        averagePlacement.removeAll(tmpList);
        averagePlacement.addAll(tmpList);

        int num = 100;
        for(Pair<Integer, Double> pair : averagePlacement) {

            traineeTotalScores.setElementAt(traineeTotalScores.get(pair.getKey()) + num, pair.getKey());
            num--;

        }

        Vector<Pair<Trainee, Double>> sortTraineeVector = new Vector<>();
        for(int i = 0; i < traineeVector.size(); i++)
            sortTraineeVector.add(new Pair<>(traineeVector.get(i), traineeTotalScores.get(i)));

        sortTraineeVector.removeIf(b -> !b.getKey().isActive());

        //Take into account the final evaluation score modifications, if applicable
        if(controller.getCurrentSession().getCurrentDay() == 10){

            counter = 0;
            for(Pair<Trainee, Double> pair : sortTraineeVector){

                Vector<org.openjfx.model.Comment> traineeComments = new Vector<>(controller.getCurrentComments());
                traineeComments.removeIf(comment -> !comment.getIncidentType().equals("Final Evaluation"));
                for(Comment comment : traineeComments){

                    if(comment.getTraineeID() == pair.getKey().getId()){

                        Double tmpDouble = pair.getValue() + Integer.parseInt(comment.getInstructorActions());
                        sortTraineeVector.set(counter, new Pair<>(pair.getKey(), tmpDouble));
                        break;

                    }
                }
                counter++;
            }
        }

        sortTraineeVector.sort(Comparator.comparing(p -> -p.getValue()));
        //END correct placement calculation/////////////////////////////////////////////////////////////////////////////////

        double totalScorePossible = 100;
        for(Test test : Objects.requireNonNull(DBManager.getAllTestsFromSession(controller.getCurrentSession().getYear(),
                controller.getCurrentSession().getSession()))) {
            if (test.isScored())
                totalScorePossible += test.getPoints();
        }

        //sortTraineeVector holds the order of placement, as well as the score
        //totalScorePossible holds the total possible points at this point in the session

        //Styling
        CellStyle centerStyleOdd = workbook.createCellStyle();
        centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
        centerStyleOdd.setBorderLeft(BorderStyle.THIN);
        centerStyleOdd.setBorderRight(BorderStyle.THIN);
        centerStyleOdd.setBorderBottom(BorderStyle.DASHED);
        centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle centerStyleEven = workbook.createCellStyle();
        centerStyleEven.setAlignment(HorizontalAlignment.CENTER);
        centerStyleEven.setBorderLeft(BorderStyle.THIN);
        centerStyleEven.setBorderRight(BorderStyle.THIN);
        centerStyleEven.setBorderBottom(BorderStyle.DASHED);

        CellStyle placeStyleOdd = workbook.createCellStyle();
        placeStyleOdd.setAlignment(HorizontalAlignment.CENTER);
        placeStyleOdd.setBorderLeft(BorderStyle.THIN);
        placeStyleOdd.setBorderBottom(BorderStyle.DASHED);
        placeStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        placeStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle placeStyleEven = workbook.createCellStyle();
        placeStyleEven.setAlignment(HorizontalAlignment.CENTER);
        placeStyleEven.setBorderLeft(BorderStyle.THIN);
        placeStyleEven.setBorderBottom(BorderStyle.DASHED);

        CellStyle percentageStyleOdd = workbook.createCellStyle();
        percentageStyleOdd.setAlignment(HorizontalAlignment.CENTER);
        percentageStyleOdd.setBorderRight(BorderStyle.THIN);
        percentageStyleOdd.setBorderBottom(BorderStyle.DASHED);
        XSSFDataFormat df = workbook.createDataFormat();
        percentageStyleOdd.setDataFormat(df.getFormat("0.00%"));
        percentageStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        percentageStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle percentageStyleEven = workbook.createCellStyle();
        percentageStyleEven.setAlignment(HorizontalAlignment.CENTER);
        percentageStyleEven.setBorderRight(BorderStyle.THIN);
        percentageStyleEven.setBorderBottom(BorderStyle.DASHED);
        percentageStyleEven.setDataFormat(df.getFormat("0.00%"));

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        XSSFFont nameFont = workbook.createFont();
        nameFont.setBold(true);
        nameFont.setFontHeightInPoints((short)20);
        nameFont.setFontName("Arial");

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(nameFont);

        try{

            int rowCount = 2;
            int columnCount = 2;

            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Place");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Trainee");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Points");
            cell.setCellStyle(headerStyle);
            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Percentage");
            cell.setCellStyle(headerStyle);

            //Table
            boolean swapper = false;
            for(int i = 0; i < sortTraineeVector.size(); i++){

                columnCount = 2;
                XSSFRow row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(i + 1);
                if(swapper)
                    cell.setCellStyle(placeStyleEven);
                else
                    cell.setCellStyle(placeStyleOdd);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(sortTraineeVector.get(i).getKey().getFirstName() + " " +
                                  sortTraineeVector.get(i).getKey().getLastName());
                if(swapper)
                    cell.setCellStyle(centerStyleEven);
                else
                    cell.setCellStyle(centerStyleOdd);
                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(sortTraineeVector.get(i).getValue());
                if(swapper)
                    cell.setCellStyle(centerStyleEven);
                else
                    cell.setCellStyle(centerStyleOdd);
                cell = row.createCell(columnCount);
                cell.setCellValue(Double.valueOf(BigDecimal.valueOf(sortTraineeVector.get(i).getValue() / totalScorePossible)
                                            .setScale(4, RoundingMode.HALF_UP).toString()));
                if(swapper)
                    cell.setCellStyle(percentageStyleEven);
                else
                    cell.setCellStyle(percentageStyleOdd);

                swapper ^= true;

            }

            for(int i = 2; i < 6; i++)
                sheet.autoSizeColumn(i);

            //Makes Header
            Row titleRow = sheet.createRow(1);
            cell = titleRow.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Current Rankings");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Current_Rankings.xlsx");
            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Current_Rankings.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates an email list for the session.
     */
    public void generateEmailList(){

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Email List");

            //Style
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle mainStyle = workbook.createCellStyle();
            mainStyle.setAlignment(HorizontalAlignment.CENTER);

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            int rowCount = 1;
            int columnCount = 1;

            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("First");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Last");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Email");
            cell.setCellStyle(headerStyle);


            for(Trainee trainee : controller.getCurrentTrainees()){

                columnCount = 1;
                row = sheet.createRow(rowCount++);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName());
                cell.setCellStyle(mainStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getLastName());
                cell.setCellStyle(mainStyle);

                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getEmail() + ";");
                cell.setCellStyle(mainStyle);

            }

            for(int i = 1; i < 4; i++)
                sheet.autoSizeColumn(i);

            //Make title row
            row = sheet.createRow(0);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Email List");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Email_List.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Email_List.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates a list of all trainees.
     */
    public void generateRoster(){

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Roster");

            //Style
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle mainStyle = workbook.createCellStyle();
            mainStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleOdd = workbook.createCellStyle();
            styleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            styleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            CellStyle nullStyle = workbook.createCellStyle();

            int rowCount = 1;
            int columnCount = 1;

            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("First");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Last");
            cell.setCellStyle(headerStyle);

            int counter = 0;
            for(Trainee trainee : controller.getCurrentTrainees()){

                columnCount = 0;
                row = sheet.createRow(rowCount++);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(counter + 1);
                cell.setCellStyle(counter % 2 == 0 ? styleOdd : nullStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getLastName());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                counter++;

            }

            for(int i = 1; i < 4; i++)
                sheet.autoSizeColumn(i);

            //Make title row
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Roster");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Roster.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Roster.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates list of all trainees who need lodging and their information for the session.
     */
    public void generateLodgingList(){

        //Pre-process trainee data
        Vector<Trainee> trainees = new Vector<>(controller.getCurrentTrainees());
        trainees.removeIf(b -> !b.isLodging());

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Lodging List");

            //Style
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle mainStyle = workbook.createCellStyle();
            mainStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle oddStyle = workbook.createCellStyle();
            oddStyle.setAlignment(HorizontalAlignment.CENTER);
            oddStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            oddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleOdd = workbook.createCellStyle();
            styleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            styleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            CellStyle nullStyle = workbook.createCellStyle();

            int rowCount = 1;
            int columnCount = 1;

            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Name");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Email");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("District");
            cell.setCellStyle(headerStyle);

            int counter = 0;
            for(Trainee trainee : trainees){

                columnCount = 0;
                row = sheet.createRow(rowCount++);

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(counter + 1);
                cell.setCellStyle(counter % 2 == 0 ? styleOdd : nullStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName() + " " + trainee.getLastName());
                cell.setCellStyle(counter % 2 == 0 ? oddStyle : mainStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getEmail() + ";");
                cell.setCellStyle(counter % 2 == 0 ? oddStyle : mainStyle);

                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getDistrictChoice());
                cell.setCellStyle(counter % 2 == 0 ? oddStyle : mainStyle);

                counter++;

            }

            for(int i = 1; i < 4; i++)
                sheet.autoSizeColumn(i);

            //Make title row
            row = sheet.createRow(0);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Lodging List");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Lodging_List.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Lodging_List.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates a roster of trainees with their emergency contact information next to it.
     */
    public void generateEmergencyContactRoster(){

        //Pre-process trainee data
        Vector<Trainee> trainees = controller.getCurrentTrainees();

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Emergency Contact Roster");

            //Style
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle mainStyle = workbook.createCellStyle();
            mainStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle centerStyleOdd = workbook.createCellStyle();
            centerStyleOdd.setAlignment(HorizontalAlignment.CENTER);
            centerStyleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            centerStyleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleOdd = workbook.createCellStyle();
            styleOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            styleOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle nullStyle = workbook.createCellStyle();

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            int rowCount = 1;
            int columnCount = 1;

            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Name");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Emergency Contact");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Relation");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Phone Number");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Address");
            cell.setCellStyle(headerStyle);

            int counter = 0;
            for(Trainee trainee : trainees){

                columnCount = 0;
                row = sheet.createRow(rowCount++);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(counter + 1);
                cell.setCellStyle(counter % 2 == 0 ? styleOdd : nullStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName() + " " + trainee.getLastName());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                if(trainee.getEmergencyContact() == null){
                    counter++;
                    continue;
                }


                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getEmergencyContact().getFullName());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getEmergencyContact().getRelationship());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getEmergencyContact().getPhoneNumber());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getEmergencyContact().getAddress() + ", " + trainee.getEmergencyContact().getCity()
                + ", " + trainee.getEmergencyContact().getZipcode());
                cell.setCellStyle(counter % 2 == 0 ? centerStyleOdd : mainStyle);

                counter++;

            }

            for(int i = 1; i < 6; i++)
                sheet.autoSizeColumn(i);

            //Make title row
            row = sheet.createRow(0);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Emergency Contact Roster");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Emergency_Contact_Roster.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Emergency_Contact_Roster.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates the uniform order list for the session.
     */
    public void generateUniformForm(){

        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("UniformList");

            //Pre-process uniform information
            //These hash maps hold the sizes as the key and their quantity as the value.
            HashMap<String, Integer> shirtSizeInfo = new HashMap<>();
            HashMap<String, Integer> shortSizeInfo = new HashMap<>();
            HashMap<String, Integer> swimSuitInfo = new HashMap<>();
            //Holds the trainees who have not completed the questionnaire with uniform info.
            Vector<Trainee> missingUniformInfoTrainees = new Vector<>();

            for(Trainee trainee : controller.getCurrentTrainees()){

                //Skip trainee if they have not completed the questionnaire 1
                if(!trainee.isQuestionnaire1Complete()){
                    missingUniformInfoTrainees.add(trainee);
                    continue;
                }

                //Handles Shirt Size
                if(shirtSizeInfo.containsKey(trainee.getShirtSize()))
                    shirtSizeInfo.put(trainee.getShirtSize(), shirtSizeInfo.get(trainee.getShirtSize()) + 1);
                else
                    shirtSizeInfo.put(trainee.getShirtSize(), 1);

                //Handles Short Size
                if(shortSizeInfo.containsKey(trainee.getShortSize()))
                    shortSizeInfo.put(trainee.getShortSize(), shortSizeInfo.get(trainee.getShortSize()) + 1);
                else
                    shortSizeInfo.put(trainee.getShortSize(), 1);

                //Handles Swim Suit Size
                if(swimSuitInfo.containsKey(trainee.getSwimSuitSize()))
                    swimSuitInfo.put(trainee.getSwimSuitSize(), swimSuitInfo.get(trainee.getSwimSuitSize()) + 1);
                else
                    swimSuitInfo.put(trainee.getSwimSuitSize(), 1);

            }

            //Styling
            XSSFFont font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFont(font);

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle bothStyle = workbook.createCellStyle();
            bothStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            bothStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bothStyle.setAlignment(HorizontalAlignment.CENTER);
            bothStyle.setFont(font);

            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short)20);
            nameFont.setFontName("Arial");

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(nameFont);

            //Make header row
            int rowCount = 1;
            int columnCount = 0;
            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("CAP #");
            cell.setCellStyle(bothStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("FIRST NAME");
            cell.setCellStyle(style);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("LAST NAME");
            cell.setCellStyle(bothStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Shirt Size");
            cell.setCellStyle(bothStyle);
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Shorts Size");
            cell.setCellStyle(bothStyle);
            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Swim Suit Size");
            cell.setCellStyle(bothStyle);

            //Make the data entry rows
            for(Trainee trainee : controller.getCurrentTrainees()){

                if(!trainee.isQuestionnaire1Complete())
                    continue;

                Row row = sheet.createRow(rowCount++);
                columnCount = 0;

                cell = row.createCell(columnCount++, CellType.NUMERIC);
                cell.setCellValue(trainee.getCapNumber());
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName());
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getLastName());
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getShirtSize());
                cell.setCellStyle(centerStyle);
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getShortSize());
                cell.setCellStyle(centerStyle);
                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getSwimSuitSize());

            }

            rowCount += 2;
            columnCount = 0;
            Row totalsTitleRow = sheet.createRow(rowCount++);
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Shirt Sizes:");
            cell.setCellStyle(bothStyle);
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total Count:");
            cell.setCellStyle(bothStyle);
            columnCount++;
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Shorts Sizes:");
            cell.setCellStyle(bothStyle);
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total Count:");
            cell.setCellStyle(bothStyle);
            columnCount++;
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Swim Suit Size:");
            cell.setCellStyle(bothStyle);
            cell = totalsTitleRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("Total Count:");
            cell.setCellStyle(bothStyle);
            columnCount++;
            cell = totalsTitleRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("Trainees with Missing Uniform Info:");
            cell.setCellStyle(style);


            //Sorts values and prints them out
            Map<String, Integer> shirtSizeMap = new TreeMap<>(shirtSizeInfo);
            List<String> shirtKeys = new ArrayList<>(shirtSizeMap.keySet());
            Map<String, Integer> shortSizeMap = new TreeMap<>(shortSizeInfo);
            List<String> shortKeys = new ArrayList<>(shortSizeMap.keySet());
            Map<String, Integer> swimSuitMap = new TreeMap<>(swimSuitInfo);
            List<String> swimSuitKeys = new ArrayList<>(swimSuitMap.keySet());
            int maxSize = Math.max(Math.max(shirtSizeMap.size(), shortSizeMap.size()),
                                   Math.max(swimSuitMap.size(), missingUniformInfoTrainees.size()));

            for(int i = 0; i < maxSize; i++){

                columnCount = 0;
                Row totalsRow = sheet.createRow(rowCount++);

                //Handle Shirt Sizes
                if(i < shirtSizeMap.size()){

                    cell = totalsRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(shirtKeys.get(i));
                    cell.setCellStyle(centerStyle);
                    cell = totalsRow.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(shirtSizeMap.get(shirtKeys.get(i)));
                    cell.setCellStyle(centerStyle);
                    columnCount++;

                }else
                    columnCount += 3;

                //Handle Short Sizes
                if(i < shortSizeMap.size()){

                    cell = totalsRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(shortKeys.get(i));
                    cell.setCellStyle(centerStyle);
                    cell = totalsRow.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(shortSizeMap.get(shortKeys.get(i)));
                    cell.setCellStyle(centerStyle);
                    columnCount++;

                }else
                    columnCount += 3;

                //Handles SwimSuit Sizes
                if(i < swimSuitMap.size()){

                    cell = totalsRow.createCell(columnCount++, CellType.STRING);
                    cell.setCellValue(swimSuitKeys.get(i));
                    cell = totalsRow.createCell(columnCount++, CellType.NUMERIC);
                    cell.setCellValue(swimSuitMap.get(swimSuitKeys.get(i)));
                    cell.setCellStyle(centerStyle);
                    columnCount++;

                }else
                    columnCount += 3;

                if(i < missingUniformInfoTrainees.size()){

                    cell = totalsRow.createCell(columnCount, CellType.STRING);
                    cell.setCellValue(missingUniformInfoTrainees.get(i).getFullName());

                }

            }

            //Fits the columns to the input data
            for(int i = 0; i < 8; i++)
                sheet.autoSizeColumn(i);
            sheet.autoSizeColumn(9);

            //Make title row
            Row row = sheet.createRow(0);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(controller.getCurrentSession().getYear() + " Session " + controller.getCurrentSession().getSession() +
                    " Uniform Orders");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Uniform_Orders.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\Uniform_Orders.xlsx"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates the Certificates for all trainees in the session. Uses a template certificate file to insert the names.
     */
    public void generateCertificates() {

        try{

            File checkCertDir = new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\District_Summaries\\");
            if(!checkCertDir.exists()){
                if(!checkCertDir.mkdir())
                    System.exit(1);
            }

            XWPFDocument doc = new XWPFDocument(OPCPackage.open(System.getProperty("user.dir") + "\\Templates\\LifeguardCert.docx"));

            Vector<String> names = new Vector<>();
            names.add("First Name, Last Name");
            Vector<Trainee> currentTrainees = new Vector<>(controller.getCurrentTrainees());

            for(int i = 0; i < controller.getCurrentTrainees().size(); i++){

                String districtNameNoSpace = controller.getCurrentTrainees().get(i).getDistrictChoice().split(" - ")[0].replaceAll("\\s", "_");
                File otherDir = new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                        controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                        "\\District_Summaries\\" + districtNameNoSpace + "_Certificates\\");
                if(!otherDir.exists()){
                    if(!otherDir.mkdir())
                        System.exit(1);
                }

                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text != null && text.contains(names.get(i))) {
                                text = text.replace(names.get(i), currentTrainees.get(i).getFirstName() +
                                                            " " + currentTrainees.get(i).getLastName());
                                r.setText(text, 0);
                            }
                        }
                    }
                }

                names.add(currentTrainees.get(i).getFirstName() + " " + currentTrainees.get(i).getLastName());

                FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Year_" +
                        controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                        "\\District_Summaries\\" + districtNameNoSpace + "_Certificates\\" +
                        currentTrainees.get(i).getLastName() + "_" + currentTrainees.get(i).getFirstName() + "_Cert.docx");

                doc.write(tmp);
                tmp.close();

            }

            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\Reports\\Year_" +
                    controller.getCurrentSession().getYear() + "_Session_" + controller.getCurrentSession().getSession() +
                    "\\District_Summaries\\"));

        }catch (IOException | InvalidFormatException e){
            e.printStackTrace();
        }

    }

    /**
     * Returns the index of the given TestScore vector's median.
     * @param array
     * @param l
     * @param r
     * @return
     */
    private int medianIndex(Vector<TestScore> array, int l, int r){

        int n = r - l + 1;
        n = ((n + 1) / 2) - 1;
        return n + 1;

    }

    /**
     * Calculates the Interquartile Range of the given TestScore vector.
     * @param array
     * @return
     */
    private double IQR(Vector<TestScore> array){

        int middleIndex = medianIndex(array, 0, array.size());

        double q1 = array.get(medianIndex(array, 0, middleIndex)).getScore();
        double q3 = array.get(middleIndex + medianIndex(array, middleIndex + 1, array.size())).getScore();

        return q3 - q1;

    }

    /**
     * Returns the median of the given TestScore vector
     * @param array
     * @return
     */
    private double median(Vector<TestScore> array){

        if(array.size() % 2 != 0)
            return (double)array.get(array.size() / 2).getScore();

        return (double)(array.get((array.size() - 1) / 2).getScore() + array.get(array.size() / 2).getScore()) / 2.0;

    }

    /**
     * Returns the given placement as a string with the correct suffix behind it.
     * @param place
     * @return true if successful, false if not.
     */
    private String getPlaceSuffix(int place){

        int j = place % 10;
        int k = place % 100;
        if (j == 1 && k != 11) {
            return place + "st";
        }
        if (j == 2 && k != 12) {
            return place + "nd";
        }
        if (j == 3 && k != 13) {
            return place + "rd";
        }
        return place + "th";

    }

}
