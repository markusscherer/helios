package at.helios.dataview.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import at.helios.common.PathHelper;
import at.helios.model.dao.ShiftplanDAO;

/**
 * 
 * Stellt das Downloadimage dar, wird 
 * auf das Symbol geklickt wird das 
 * File zum Download angeboten.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   02.03.2009  Neuerstellung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class DownloadImageListener extends Image
{
    private int   _iPlanId;
    private int   _iShiftTyp;
    private Label _oInfoLabel;

    /**
     * 
     * Konstruktor, setzt das Bild, die
     * PlanId und den Schicht Typ
     * 
     * @param sImagePath
     * @param iPlanId
     * @param iShiftTyp
     *
     */
    public DownloadImageListener(String sImagePath, int iPlanId, int iShiftTyp, Label oInfoLabel)
    {
        super(sImagePath);
        _iPlanId = iPlanId;
        _iShiftTyp = iShiftTyp;
        _oInfoLabel = oInfoLabel;
    }

    /**
     * 
     * Wird auf das Symbol geklickt,
     * wird das entsprechende File zum Download
     * angeboten.
     * @throws IOException 
     *
     */
    public void onClick() throws IOException
    {
        ShiftplanDAO oShiftPlanDAO = new ShiftplanDAO();

        String sFilepath = oShiftPlanDAO.getFilepathByShiftplanId(_iPlanId)[_iShiftTyp];

        String sPath = PathHelper.getRootPath() + "WEB-INF/jasper/Output/pdf/" + sFilepath
            + ".pdf";

        File oFile = new File(sPath);

        if (oFile.exists())
        {
            try
            {
                _oInfoLabel.setValue("");
                Filedownload.save(oFile, "application/pdf");
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            _oInfoLabel.setStyle("color: red");
            _oInfoLabel.setValue("File existiert nicht mehr, Plan muss erneut gedruckt werden.");
        }
    }
}
