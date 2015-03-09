package at.helios.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.zkoss.zk.ui.Sessions;

import at.helios.common.PathHelper;
import at.helios.model.Department;
import at.helios.model.Person;

/**
 * 
 * Mit Hilfe dieser Klasse werden
 * Mails an alle Personen mit den 
 * Schichtplänen versendet.
 * 
 * @author
 * <PRE>
 *         ID     date           description
 *         tom    27.03.2009     Erstkommentierung
 * </PRE>
 **/
public class MailSender extends Thread
{
    private String[] _saPlanNames;

    /**
     * 
     * Der Konstruktor setzt
     * die Namen der zu versendenden
     * PDF Pläne.
     * 
     * @param saPlans
     **/
    public MailSender(String[] saPlanNames)
    {
        _saPlanNames = saPlanNames;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run()
    {
        sendMail();
    }

    /**
     * 
     * Sendet E-Mails mit den gesetzten
     * Schichtplänenen an alle Personen.
     *
     */
    public void sendMail()
    {
        //Properties einlesen
        Properties oMailProperties = new Properties();
        String sRootPath;
        try
        {
            oMailProperties.load(MailSender.class.getClassLoader().getResourceAsStream(
                "resources/email.properties"));
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        sRootPath = PathHelper.getRootPath();

        try
        {
            //Properties definieren
            Properties oProperties = new Properties();
            oProperties.put("mail.smtp.host", oMailProperties.getProperty("mail.smtp.host"));
            oProperties.setProperty("mail.smtp.user", oMailProperties.getProperty("mail.smtp.user"));
            oProperties.setProperty("mail.smtp.password", oMailProperties.getProperty("mail.smtp.password"));
            oProperties.setProperty("mail.smtp.port", oMailProperties.getProperty("mail.smtp.port"));
            oProperties.setProperty("mail.smtp.auth", oMailProperties.getProperty("mail.smtp.auth"));

            //Attachments setzen
            MimeBodyPart oFileRTW1 = this.setAttachment(_saPlanNames[0], sRootPath);
            MimeBodyPart oFileRTW2 = this.setAttachment(_saPlanNames[1], sRootPath);
            MimeBodyPart oFileNEF = this.setAttachment(_saPlanNames[2], sRootPath);

            if (oFileRTW1 != null && oFileRTW2 != null && oFileNEF != null)
            {

                // Session anlegen
                Session oSession = Session.getInstance(oProperties, new Authenticator(oProperties));

                Collection<Person> coPersonTmp = null;

                if (oMailProperties.getProperty("mail.system.status").equals("active"))
                {
                    coPersonTmp = Person.getPersonsByDepartment((Department) Sessions.getCurrent()
                        .getAttribute("department"));
                }
                else
                {
                    coPersonTmp = new ArrayList<Person>();
                    Person oPerson1 = new Person();
                    oPerson1.setEmail(oMailProperties.getProperty("mail.system.test.address"));
                    oPerson1.setForename("Max");
                    oPerson1.setSurname("Mustermann");
                    oPerson1.setEMTNumber("0000");
                    coPersonTmp.add(oPerson1);
                }

                for (Person oPerson : coPersonTmp)
                {
                    if (isValidateEmail(oPerson.getEmail()))
                    {
                        if (oPerson.getEmail() == null || oPerson.getEmail().equalsIgnoreCase("")) continue;

                        // Message anlegen
                        MimeMessage oMessage = new MimeMessage(oSession);

                        // Absender setzen
                        oMessage.setFrom(new InternetAddress(oMailProperties
                            .getProperty("mail.sender.address")));

                        // Empfaenger (primary recipient) setzen
                        // Die Email-Adresse wird zusammengesetzt.
                        oMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(oPerson
                            .getEmail()));

                        // Betreff setzen
                        oMessage.setSubject(this.parseMailText(oMailProperties
                            .getProperty("mail.subject.text"), oPerson.getForename(), oPerson.getSurname()));

                        // Inhalt setzen
                        MimeMultipart oContent = new MimeMultipart("mixed");
                        MimeBodyPart oText = new MimeBodyPart();
                        /*
                                            oText.setText("Hallo " + oPerson.getForename() + " " + oPerson.getSurname()
                                                + ",\n\nIm Anhang befindet sich der Schichtplan"
                                                + " für die nächsten zwei Monate. \n\n\n\n"
                                                + "Mit freundlichen Grüßen\n\nRettungsleitstelle Bludenz");
                        */

                        oText.setText(this.parseMailText(oMailProperties.getProperty("mail.content.text"),
                            oPerson.getForename(), oPerson.getSurname()));

                        oText.setHeader("MIME-Version", "1.0");
                        oText.setHeader("Content-Type", oText.getContentType());

                        oContent.addBodyPart(oText);
                        oContent.addBodyPart(oFileRTW1);
                        oContent.addBodyPart(oFileRTW2);
                        oContent.addBodyPart(oFileNEF);

                        oMessage.setContent(oContent);
                        oMessage.setHeader("MIME-Version", "1.0");
                        oMessage.setHeader("Content-Type", oContent.getContentType());

                        // Message senden
                        Transport.send(oMessage);
                    }
                    else
                    {
                        System.out.println("E-Mail Error: " + oPerson.getEMTNumber() + ": "
                            + oPerson.getEmail() + " not valid");
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Fügt ein Pdf in ein MimeBodyPart hinzu.
     * 
     * @param sName
     * @param sRootPath
     * 
     * @return MimeBodyPart
     * 
     * @throws MessagingException
     **/
    public MimeBodyPart setAttachment(String sName, String sRootPath) throws MessagingException
    {

        String sPath = sRootPath + "/WEB-INF/jasper/Output/pdf/" + sName + ".pdf";

        MimeBodyPart oFile = new MimeBodyPart();

        File oAttachmentFile = new File(sPath);
        if (oAttachmentFile.canRead())
            System.out.println("Datei erfolgreich geladen: " + sPath);
        else
        {
            System.out.println("Fehler beim lesen von: " + sPath);

            return null;
        }

        DataSource oFileDataSource = new FileDataSource(oAttachmentFile);
        oFile.setDataHandler(new DataHandler(oFileDataSource));
        oFile.setFileName(sName + ".pdf"); // gibt dem Anhang einen Namen

        oFile.setHeader("MIME-Version", "1.0");
        oFile.setHeader("Content-Type", "application/pdf");

        return oFile;
    }

    public String parseMailText(String sText, String sForename, String sSurname)
    {
        sText = sText.replaceAll("%FN", sForename);
        sText = sText.replaceAll("%SN", sSurname);

        return sText;
    }

    public boolean isValidateEmail(String sMail)
    {
        Pattern oPattern = Pattern.compile(".+@.+\\.[a-z]+");
        return oPattern.matcher(sMail).matches();
    }
}