package at.helios.email;

import java.io.File;
import java.io.IOException;

import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import at.helios.common.PathHelper;
import at.helios.model.dao.ShiftplanDAO;

/**
 * Diese Klasse stellt einen Button
 * für den PDF Versand dar.
 * 
 * @author
 * <PRE>
 *         ID     date           description
 *         tom    27.03.2009     Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class MailButtonListener extends Button
{
    private Label    _oInfoLabel;
    private String[] _saPlans;

    /**
     * 
     * Konstruktor, setzt das PDF-Bild, die
     * PlanId und den Schicht Typ
     * 
     * @param coShifts 
     * @param sName
     * @param oInfoLabel
     *
     */
    public MailButtonListener(String sName, String[] saPlans, Label oInfoLabel)
    {
        super(sName);
        _saPlans = saPlans;
        _oInfoLabel = oInfoLabel;
    }

    /**
     * 
     * Wird aufgerufen wenn auf den Sendenbutton
     * geklickt wurde. Diese Methode leitet den
     * Mail Versand ein.
     * 
     * @throws IOException 
     *
     */
    public void onClick() throws IOException
    {
        sendMail();
    }

    /**
     * Alle Pdfs einer Periode werden per E-Mail an
     * alle Personen versendet.
     * 
     * @throws IOException - Pdf nicht vorhanden --> muss erneut gedruckt werden.
     **/
    public void sendMail() throws IOException
    {
        _oInfoLabel.setValue("");

        ShiftplanDAO oShiftPlanDAO = new ShiftplanDAO();

        //Dateinamen holen
        String[] asPlanData = oShiftPlanDAO.getFilepathByShiftplanId(Integer.valueOf(_saPlans[1]));
        boolean bFileExists = true;

        String sPath = PathHelper.getRootPath();

        for (int i = 0; i < asPlanData.length; i++)
        {
            File oFile = new File(sPath + "WEB-INF/jasper/Output/pdf/" + asPlanData[i] + ".pdf");

            //Prüfen ob alle Dateien vorhanden sind
            if (!oFile.exists())
            {
                bFileExists = false;
                break;
            }
        }

        //Prüft ob eine Datei als Fehlend gesetzt wurde.
        if (bFileExists)
        {
            MailSender oMailSender = new MailSender(asPlanData);
            //Mails senden
            oMailSender.start();
            _oInfoLabel.setStyle("color: black");
            _oInfoLabel.setValue("Mails werden gesendet.");
        }
        else
        {
            _oInfoLabel.setStyle("color: red");
            _oInfoLabel
                .setValue("Ein oder mehrere Dateien nicht vorhanden --> Plan muss erneut gedruck werden.");
        }
    }
}
