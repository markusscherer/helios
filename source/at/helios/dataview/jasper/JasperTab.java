package at.helios.dataview.jasper;

import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tabpanel;

import at.helios.dataview.jasper.components.PlanList;
import at.helios.dataview.jasper.components.ShiftPrinter;

/**
 * 
 * Diese Klasse stellt eine Oberfläche
 * für das senden und verschicken von Plänen bereit.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   29.03.2009  Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class JasperTab extends Tabpanel
{
    Label    _oInfoLabel = new Label();
    Groupbox _oPlanListDiv;
    PlanList _oPlanList;

    /**
     * 
     * Inizialisiert die GUI
     *
     */
    public JasperTab()
    {
        initGui();
    }

    /**
     * 
     * Inizialisiert die GUI
     *
     */
    public void initGui()
    {
        this.appendChild(new ShiftPrinter(this));

        _oPlanList = new PlanList(_oInfoLabel);
        _oPlanListDiv = new Groupbox();

        new Caption("Plan download / versenden").setParent(_oPlanListDiv);
        _oPlanListDiv.setMold("3d");
        _oPlanListDiv.setStyle("padding-top: 20px;");
        _oPlanListDiv.setWidth("800px");
        _oPlanListDiv.appendChild(_oPlanList);

        Div oDiv = new Div();
        oDiv.setStyle("margin-top: 20px;");
        _oInfoLabel.setStyle("color: red");
        _oInfoLabel.setParent(oDiv);
        oDiv.setParent(_oPlanListDiv);

        _oPlanListDiv.setParent(this);

    }

    /**
     * 
     * Baut die List aller gedruckten Plänen neu auf.
     *
     */
    public void rebuildPlanList()
    {
        _oPlanListDiv.removeChild(_oPlanList);
        _oPlanList = new PlanList(_oInfoLabel);
        _oPlanList.setParent(_oPlanListDiv);
    }
}
