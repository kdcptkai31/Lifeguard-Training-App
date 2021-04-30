package org.openjfx.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openjfx.model.Trainee;

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
