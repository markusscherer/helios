package at.helios.dataview.jasper.components;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Vbox;

import at.helios.dataview.jasper.JasperHelper;
import at.helios.dataview.jasper.JasperShiftPlanDAO;
import at.helios.dataview.jasper.JasperTab;
import at.helios.model.dao.ShiftplanDAO;

import com.lowagie.text.BadElementException;

/**
 * 
 * Diese Klasse stellt die Funktion bereit,
 * alle eingeteilten Pläne als PDF zu drucken.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   29.03.2009   Neuerstellung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class ShiftPrinter extends Groupbox
{
    private Listbox   _oListBox = null;
    private Label     _oLabel   = null;
    private JasperTab _oDataViewJasper;

    /**
     * 
     * Initialisiert die GUI
     * 
     * @param oDataViewJasper
     *
     */
    public ShiftPrinter(JasperTab oDataViewJasper)
    {
        _oDataViewJasper = oDataViewJasper;

        try
        {
            initGUI();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

   /**
    * 
    * Wird beim erstellen eines ShiftPrinters
    * aus einem zul File augerufen und Initialisiert die GUI
    * 
    * @throws SQLException
    *
    */
    public void onCreate() throws SQLException
    {
        initGUI();
    }

   /**
    * 
    * Initailisiert die GUI und listet alle
    * eingeteilten Pläne auf, die gedruckt werden können.
    * 
    * @throws SQLException
    *
    */
    public void initGUI() throws SQLException
    {
        System.out.println("ShiftPrinter.initGUI()");
        this.setWidth("800px");
        new Caption("Plan drucken").setParent(this);
        this.setMold("3d");
        Collection<String[]> coShifts = JasperShiftPlanDAO.getShiftPlans();
        Vbox oVBox = new Vbox();

        _oListBox = new Listbox();
        _oListBox.setRows(1);
        _oListBox.setMold("select");
        _oListBox.setId("sPlan");

        Listitem oListItem;
        oListItem = new Listitem();
        oListItem.setLabel("Plan wählen");
        oListItem.setParent(_oListBox);

        /* 
         * muss gesetzt werden, dass standardmäßig ein
         * Item ausgewählt ist und beim zugriff kein NullPointer geworfen wird
         */
        _oListBox.setSelectedItem(oListItem);

        for (String[] sPlan : coShifts)
        {
            oListItem = new Listitem();
            oListItem.setLabel(sPlan[0]);
            oListItem.setValue(Integer.valueOf(sPlan[1]));
            oListItem.setParent(_oListBox);
        }

        _oListBox.setParent(oVBox);

        _oLabel = new Label();
        _oLabel.setValue("Es muss ein Plan ausgewählt werden.");
        _oLabel.setParent(oVBox);
        _oLabel.setVisible(false);

        Button oButton = new Button();
        oButton.setLabel("Drucken");
        oButton.addForward("onClick", this, "onPrint");
        oButton.setParent(oVBox);

        oVBox.setParent(this);
    }

    /**
     * 
     * Wird beim Klick auf Drucken aufgerufen
     * und druckt den ausgewählten Plan als Pdf
     * 
     * @throws BadElementException
     * @throws IOException
     * @throws SQLException
     *
     */
    public void onPrint() throws BadElementException, IOException, SQLException
    {
        Integer iPlanId;
        _oLabel.setVisible(false);
        Collection<String> coNames;

        if ((iPlanId = (Integer) _oListBox.getSelectedItem().getValue()) != null)
        {
            if ((coNames = JasperHelper.printPlan(iPlanId)) != null)
            {
                _oLabel.setStyle("color: black");
                _oLabel.setVisible(true);
                _oLabel.setValue("Plan erfolgreich gedruckt.");
                ShiftplanDAO oSPD = new ShiftplanDAO();
                oSPD.updateFilepath(iPlanId, coNames);
                _oDataViewJasper.rebuildPlanList();
            }

        }
        else
        {
            _oLabel.setStyle("color: red");
            _oLabel.setVisible(true);
        }
    }
}
