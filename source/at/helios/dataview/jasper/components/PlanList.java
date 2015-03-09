package at.helios.dataview.jasper.components;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import at.helios.dataview.jasper.DownloadImageListener;
import at.helios.dataview.jasper.JasperShiftPlanDAO;
import at.helios.email.MailButtonListener;

/**
 * 
 * Diese Klasse ist eine Listbox die
 * alle gedruckten Plänen enhält
 * 
 * @author
 * <PRE>
 *         ID      date        description
 *         mab     29.03.2009  Neuerstellung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class PlanList extends Listbox
{
    private static final String PIC_PATH = "/design/pdf_logo.png";
    private Label               _oInfoLabel;

    public PlanList(Label oInfoLabel)
    {
        _oInfoLabel = oInfoLabel;
        try
        {
            initGui();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Initialisiert die GUI
     * 
     * @throws FileNotFoundException
     * @throws SQLException
     *
     */
    public void onCreate() throws FileNotFoundException, SQLException
    {
        initGui();
    }

    /**
     * 
     * Initialisiert die GUI und trägt
     * die Pläne ein.
     * 
     * @throws SQLException
     * @throws FileNotFoundException
     *
     */
    public void initGui() throws SQLException, FileNotFoundException
    {
        Listhead oListHead;
        Listheader oListHeader;

        Listitem oListitem;
        Listcell oListCell;

        oListHead = new Listhead();
        oListHeader = new Listheader();
        oListHeader.setLabel("Plan");
        oListHeader.setSort("auto");
        oListHeader.setParent(oListHead);

        oListHeader = new Listheader();
        oListHeader.setLabel("RTW-1");
        oListHeader.setStyle("min-width: 40px");
        oListHeader.setParent(oListHead);
        oListHead.setParent(this);

        oListHeader = new Listheader();
        oListHeader.setLabel("RTW-2");
        oListHeader.setStyle("min-width: 40px");
        oListHeader.setParent(oListHead);
        oListHead.setParent(this);

        oListHeader = new Listheader();
        oListHeader.setLabel("NEF");
        oListHeader.setStyle("min-width: 40px");
        oListHeader.setParent(oListHead);
        oListHead.setParent(this);

        oListHeader = new Listheader();
        oListHeader.setLabel("E-Mail");
        oListHeader.setStyle("min-width: 40px");
        oListHeader.setParent(oListHead);
        oListHead.setParent(this);

        /*------------Header-End----------------*/

        //String sDeployPath = System.getProperty("wtp.deploy") + "/Helios/";
        Collection<String[]> coShifts = JasperShiftPlanDAO.getShiftPlans();

        Div oDiv = new Div();
        int iPlanId;

        for (String[] saPlan : coShifts)
        {
            if (saPlan[2] != null && !saPlan[2].equalsIgnoreCase(""))
            {
                oListitem = new Listitem();
                iPlanId = Integer.valueOf(saPlan[1]);

                //Plan Name
                oListCell = new Listcell();
                oListCell.setLabel(saPlan[0]);
                oListCell.setParent(oListitem);

                //RTW-1
                oListCell = new Listcell();
                oDiv = new Div();
                oDiv.appendChild(new DownloadImageListener(PIC_PATH, iPlanId, 0, _oInfoLabel));
                oDiv.setParent(oListCell);
                oListCell.setParent(oListitem);

                //RTW-2
                oListCell = new Listcell();
                oDiv = new Div();
                oDiv.appendChild(new DownloadImageListener(PIC_PATH, iPlanId, 1, _oInfoLabel));
                oDiv.setParent(oListCell);
                oListCell.setParent(oListitem);

                //NEF
                oListCell = new Listcell();
                oDiv = new Div();
                oDiv.appendChild(new DownloadImageListener(PIC_PATH, iPlanId, 2, _oInfoLabel));
                oDiv.setParent(oListCell);
                oListCell.setParent(oListitem);

                //Send E-Mail
                oListCell = new Listcell();
                Button oButton = new MailButtonListener("Send", saPlan, _oInfoLabel);
                oButton.setParent(oListCell);
                oListCell.setParent(oListitem);

                oListitem.setParent(this);
            }
        }
    }
}
