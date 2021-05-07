package org.openjfx.controller;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Pair;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openjfx.model.*;
import org.openjfx.model.Comment;
import org.openjfx.view.OverviewView;

/**
 * This class creates Word documents or Excel documents, which are needed for operational or report purposes.
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
            File currentSession = new File(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear());
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
        XSSFFont districtFont = workbook.createFont();
        districtFont.setBold(true);
        districtFont.setFontHeightInPoints((short)14);
        districtFont.setFontName("Arial");

        CellStyle nameStyle = workbook.createCellStyle();
        nameStyle.setFont(nameFont);
        CellStyle districtStyle = workbook.createCellStyle();
        districtStyle.setFont(districtFont);

        try{

            //Used to correctly offset the excel rows to fit 4 profiles per printable page.
            int alternator = 0;

            for(int i = 0; i < controller.getCurrentTrainees().size(); i+=2){

                ByteArrayOutputStream byteIn = new ByteArrayOutputStream();
                ImageIO.write(Objects.requireNonNull(SwingFXUtils.fromFXImage(controller.getCurrentTrainees().get(i).getActualImage(), null)), "png", byteIn);
                int pictureIndex = workbook.addPicture(byteIn.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                byteIn.close();
                CreationHelper helper = workbook.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1((i * 11) + alternator + i / 4);
                anchor.setCol2(4);
                anchor.setRow2(16 + (i * 11) + alternator + i / 4);
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
                    anchor1.setRow1((i * 11) + alternator + i / 4);
                    anchor1.setCol2(9);
                    anchor1.setRow2(16 + (i * 11) + alternator + i / 4);
                    Picture pic1 = drawing1.createPicture(anchor1, pictureIndex1);

                }

                Row row = sheet.createRow(17 + (i * 11) + alternator + i / 4);
                Cell cell;
                String nameText = controller.getCurrentTrainees().get(i).getFirstName() + " " +
                        controller.getCurrentTrainees().get(i).getLastName();
                if(nameText.length() < 14)
                    cell = row.createCell(1);
                else
                    cell = row.createCell(0);

                cell.setCellValue(nameText);
                cell.setCellStyle(nameStyle);

                if(i + 1 < controller.getCurrentTrainees().size()){

                    nameText = controller.getCurrentTrainees().get(i + 1).getFirstName() + " " +
                                      controller.getCurrentTrainees().get(i + 1).getLastName();
                    if(nameText.length() < 14)
                        cell = row.createCell(6);
                    else
                        cell = row.createCell(5);
                    cell.setCellValue(nameText);
                    cell.setCellStyle(nameStyle);

                }

                Row row1 = sheet.createRow(19 + (i * 11) + alternator + i / 4);
                String districtText = controller.getCurrentTrainees().get(i).getDistrictChoice();
                if(districtText.length() < 13)
                    cell = row1.createCell(1);
                else
                    cell = row1.createCell(0);
                cell.setCellValue(districtText);
                cell.setCellStyle(districtStyle);

                if(i + 1 < controller.getCurrentTrainees().size()){

                    districtText = controller.getCurrentTrainees().get(i + 1).getDistrictChoice();
                    if(districtText.length() < 13)
                        cell = row1.createCell(6);
                    else
                        cell = row1.createCell(5);
                    cell.setCellValue(districtText);
                    cell.setCellStyle(districtStyle);

                }

                alternator = (alternator + 1) % 2;

            }

            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                    "\\Trainee_Profiles.xlsx");
            workbook.write(fileOut);
            fileOut.close();

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

            //Makes header row
            int rowCount = 0;
            int columnCount = 0;
            Row headerRow = sheet.createRow(rowCount);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("FIRST NAME");
            cell = headerRow.createCell(columnCount++, CellType.STRING);
            cell.setCellValue("LAST NAME");
            cell = headerRow.createCell(columnCount, CellType.STRING);
            cell.setCellValue("DISTRICT/SECTOR");

            //Makes the data entry rows
            for(Trainee trainee : controller.getCurrentTrainees()){

                Row row = sheet.createRow(++rowCount);
                columnCount = 0;
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getFirstName());
                cell = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue(trainee.getLastName());
                cell = row.createCell(columnCount, CellType.STRING);
                cell.setCellValue(trainee.getDistrictChoice());

            }

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                    "\\Avery_NameTent_NameTag_List.xlsx");
            workbook.write(tmp);
            tmp.close();
            workbook.close();

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
                    totalPlace += score.getPlace();

            }
            if(traineeEventScores.size() != 0){
                averagePlacement.add(new Pair<>(counter, totalPlace * 1.0 / traineeEventScores.size()));
            }
            counter++;

        }

        averagePlacement.sort(new OverviewView.SortByAveragePlacement());
        Collections.reverse(averagePlacement);

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
            cell = titleRow.createCell(3, CellType.STRING);
            cell.setCellValue("Current Rankings");
            cell.setCellStyle(titleStyle);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                    "\\Current_Rankings.xlsx");
            workbook.write(tmp);
            tmp.close();
            workbook.close();

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
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
            style.setFillPattern(FillPatternType.BIG_SPOTS);
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            CellStyle bothStyle = workbook.createCellStyle();
            bothStyle.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
            bothStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            bothStyle.setAlignment(HorizontalAlignment.CENTER);

            //Make header row
            int rowCount = 0;
            int columnCount = 0;
            Row headerRow = sheet.createRow(rowCount++);
            Cell cell = headerRow.createCell(columnCount++, CellType.STRING);
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
            for(int i = 0; i < 5; i++)
                sheet.autoSizeColumn(i);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            //Saves the file
            FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                    "\\Uniform_Orders.xlsx");

            workbook.write(tmp);
            tmp.close();
            workbook.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generates the Certificates for all trainees in the session.
     */
    public void generateCertificates() throws FileNotFoundException {

        try{

            File checkCertDir = new File(System.getProperty("user.dir") + "\\Reports\\Session_" +
                    controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                    "\\Certificates\\");
            if(!checkCertDir.exists()){
                if(!checkCertDir.mkdir())
                    System.exit(1);
            }

            XWPFDocument doc = new XWPFDocument(OPCPackage.open(System.getProperty("user.dir") + "\\Templates\\LifeguardCert.docx"));

            Vector<String> names = new Vector<>();
            names.add("First Name, Last Name");
            Vector<Trainee> currentTrainees = new Vector<>(controller.getCurrentTrainees());
            for(int i = 0; i < controller.getCurrentTrainees().size(); i++){

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

                FileOutputStream tmp = new FileOutputStream(System.getProperty("user.dir") + "\\Reports\\Session_" +
                        controller.getCurrentSession().getSession() + "_Year_" + controller.getCurrentSession().getYear() +
                        "\\Certificates\\" + currentTrainees.get(i).getFirstName() + currentTrainees.get(i).getLastName()
                        + "Cert.docx");

                doc.write(tmp);
                tmp.close();

            }

        }catch (IOException | InvalidFormatException e){
            e.printStackTrace();
        }

    }

}
