package org.openjfx.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openjfx.model.Trainee;

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

            //Saves file
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
