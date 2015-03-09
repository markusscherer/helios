package at.helios.dataview.jasper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import at.helios.calendar.helper.DateHelper;
import at.helios.common.DBHelper;
import at.helios.common.PathHelper;

/**
 * 
 * Mithilfe dieser Klasse ist es 
 * möglich Schichtpläne zu drucken.
 * 
 * @author
 * <PRE>
 *         ID    date         description
 *         mab   28.03.2009   Erstkommentierung
 * </PRE>
 *
 */
public class JasperHelper
{
    /**
     * 
     * Druckt die Schichtpläne für den Plan iPlanId
     * 
     * @param iPlanId
     * @return
     * @throws SQLException
     * @throws IOException
     *
     */
    public static Collection<String> printPlan(Integer iPlanId) throws SQLException, IOException
    {
        JasperReport oJasperReport;
        JasperPrint oJasperPrint;

        String sJasperPath = PathHelper.getRootPath() + "WEB-INF/jasper";

        Properties oJasperPt = new Properties();
        oJasperPt.load(DBHelper.class.getClassLoader().getResourceAsStream("resources/jasper.properties"));

        parseJRXML("Data", "FONT_PATH", sJasperPath);
        parseJRXML("DataNef", "FONT_PATH", sJasperPath);
        parseJRXML("Date", "FONT_PATH", sJasperPath);
        parseJRXML("heading", "FONT_PATH", sJasperPath);
        parseJRXML("headingNef", "FONT_PATH", sJasperPath);
        parseJRXML("NEFMain", "FONT_PATH", sJasperPath);
        parseJRXML("RTWMain", "FONT_PATH", sJasperPath);

        //.jasper Files neu schreiben
        System.out.println("Export: " + new Date() + "\nbuilding Jasper files...");
        buildJasper("DataNef", sJasperPath);
        buildJasper("Date", sJasperPath);
        buildJasper("headingNef", sJasperPath);
        buildJasper("Data", sJasperPath);
        buildJasper("heading", sJasperPath);
        System.out.println("Jasper files built.");

        String sPlanName = JasperShiftPlanDAO.getPlanName(iPlanId);
        Date[] dDate = JasperShiftPlanDAO.getDaysOfMonth(iPlanId);

        HashMap<String, String> moPlanId = new HashMap<String, String>();

        moPlanId.put("PlanId", String.valueOf(iPlanId));

        String[] saQual = { "RTW", "RTW", "NEF"};
        String sPdfName;
        Collection<String> oPdfNames = new ArrayList<String>();

        try
        {
            for (int i = 0; i < saQual.length; i++)
            {
                //Daten für NEF in HashMap einfügen
                if (i > 1)
                {
                    sPdfName = sPlanName + saQual[i] + "_" + DateHelper.getFormatedDate(dDate[0], "MMMM")
                        + "_" + DateHelper.getFormatedDate(dDate[0], "yyyy");

                    moPlanId.put("DateHead", (DateHelper.getFormatedDate(dDate[0], "MMMM")).toUpperCase()
                        + " " + DateHelper.getFormatedDate(dDate[0], "yy") + " + "
                        + (DateHelper.getFormatedDate(dDate[1], "MMMM")).toUpperCase() + " "
                        + DateHelper.getFormatedDate(dDate[1], "yy"));
                    moPlanId.put("Period", String.valueOf(0));

                    moPlanId.put("FirstP", (DateHelper.getFormatedDate(dDate[0], "MMMM")).toUpperCase());
                    moPlanId.put("SecondP", (DateHelper.getFormatedDate(dDate[1], "MMMM")).toUpperCase());
                    moPlanId.put("JasperPath", sJasperPath + "/");
                }
                //Daten für RTW in HashMap einfügen
                else
                {
                    sPdfName = sPlanName + saQual[i] + "_" + DateHelper.getFormatedDate(dDate[i], "MMMM")
                        + "_" + DateHelper.getFormatedDate(dDate[i], "yyyy");

                    moPlanId.put("DateHead", (DateHelper.getFormatedDate(dDate[i], "MMMM")).toUpperCase()
                        + " " + DateHelper.getFormatedDate(dDate[i], "yyyy"));

                    moPlanId.put("Period", String.valueOf(i + 1));
                    moPlanId.put("JasperPath", sJasperPath + "/");
                }

                oJasperReport = JasperCompileManager.compileReport(sJasperPath + "/JRXML/" + saQual[i]
                    + "Main.jrxml");
                oJasperPrint = JasperFillManager.fillReport(oJasperReport, moPlanId, new JREmptyDataSource());

                if (oJasperPt.getProperty("at.helios.jasper.format.pdf").equals("1"))
                {
                    JasperExportManager.exportReportToPdfFile(oJasperPrint, sJasperPath + "/Output/pdf/"
                        + sPdfName + ".pdf");

                    System.out.println(sPdfName + ".pdf printed.");
                }

                if (oJasperPt.getProperty("at.helios.jasper.format.html").equals("1"))
                {
                    JasperExportManager.exportReportToHtmlFile(oJasperPrint, sJasperPath + "/Output/html/"
                        + sPdfName + ".html");

                    System.out.println(sPdfName + ".html printed.");
                }
                if (oJasperPt.getProperty("at.helios.jasper.format.xls").equals("1"))
                {
                    xlsOutput(oJasperPrint, sPdfName, sJasperPath);
                }
                
                oPdfNames.add(sPdfName);
            }

            System.out.println("Printing finished.");
        }

        catch (JRException e)
        {
            e.printStackTrace();
            return null;
        }

        return oPdfNames;
    }

    /**
     * 
     * JasperFile für die Subreports erstellen
     * 
     * @param sFile
     * @param sJasperPath
     *
     */
    public static void buildJasper(String sFile, String sJasperPath)
    {
        try
        {
            JasperCompileManager.compileReportToFile(sJasperPath + "/JRXML/" + sFile + ".jrxml", sJasperPath
                + "/JRXML/" + sFile + ".jasper");
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * Druckt die Pläne als Excel File
     * 
     * @param oPrint
     * @param sPdfName
     * @param sJasperPath
     * @throws JRException
     * @throws IOException
     *
     */
    public static void xlsOutput(JasperPrint oPrint, String sPdfName, String sJasperPath) throws JRException,
        IOException
    {
        OutputStream ouputStream = new FileOutputStream(new File(sJasperPath + "/Output/xls/" + sPdfName
            + ".xls"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, oPrint);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);

        exporterXLS.exportReport();
        ouputStream.write(byteArrayOutputStream.toByteArray());
        ouputStream.flush();
        ouputStream.close();

        System.out.println(sPdfName + ".xls printed.");
    }

    /**
     * 
     * Ersetzt den Pfad zu den Schriften und Bildern in den
     * jrxml Datein mit dem aktuellen Pfad
     * 
     * @param sFileName
     * @param sOldPattern
     * @param sReplPattern
     *
     */
    public static void parseJRXML(String sFileName, String sOldPattern, String sReplPattern)
    {
        String sInputFile = sReplPattern + "/JRXML/pattern/" + sFileName + ".jrxml";
        String sOutputFile = sReplPattern + "/JRXML/" + sFileName + ".jrxml";

        String sLine;

        StringBuffer oStringBuffer = new StringBuffer();

        FileInputStream oFInputStream;
        try
        {
            oFInputStream = new FileInputStream(sInputFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(oFInputStream));

            while ((sLine = reader.readLine()) != null)
            {
                sLine = sLine.replaceAll(sOldPattern, sReplPattern);
                oStringBuffer.append(sLine + "\n");
            }
            reader.close();
            BufferedWriter oWriter = new BufferedWriter(new FileWriter(sOutputFile));
            oWriter.write(oStringBuffer.toString());
            oWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
