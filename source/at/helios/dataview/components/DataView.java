package at.helios.dataview.components;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import at.helios.common.PersonPanel;
import at.helios.dataview.RuleTab;
import at.helios.dataview.jasper.JasperTab;
import at.helios.model.Person;

/**
 * 
 * Diese Klasse stellt die DataView dar
 * Enthaltene Elemente:
 *  - PersonPanel
 *  - Oberfläche für die Änderung der Regeln
 *  - Oberfläche für die bestätigung der Pläne
 *  - Oberfläche für das Drucken der fertigen Pläne
 *    als Pdf und das Versenden per E-Mail
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   28.03.2009  Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class DataView extends Borderlayout
{
    private PersonPanel _oPersonPanel;
    private Tabbox      _oTabbox;
    private Tabpanels   _oTabpanels;

    private RuleTab     _oRuleTab;

    /**
     * 
     * Wird aufgerufen, wenn das Element in 
     * einem zul File erstellt wird.
     *
     */
    public void onCreate()
    {
        Session oSession = Sessions.getCurrent();
        oSession.setAttribute("PersonPanelOwner", this);

        this.setStyle("min-height: 700px;");

        West oWest = new West();
        oWest.setTitle("Freiwillige");
        oWest.setSize("250px");
        oWest.setFlex(true);
        oWest.setSplittable(true);
        oWest.setMinsize(210);
        oWest.setMaxsize(500);
        oWest.setCollapsible(true);

        _oPersonPanel = new PersonPanel();
        _oPersonPanel.setParent(oWest);

        oWest.setParent(this);

        //MainFenser - Center
        Center oCenter = new Center();
        oCenter.setFlex(true);
        oCenter.setAutoscroll(true);

        //Tabs erzeugen
        _oTabbox = new Tabbox();
        Tabs oTabs = new Tabs();
        _oTabpanels = new Tabpanels();

        Tab oTab = new Tab("Präferenzen ändern");
        oTab.setParent(oTabs);

        oTab = new Tab("Pläne bestätigen");
        oTab.setParent(oTabs);

        oTab = new Tab("PDF & E-Mail");
        oTab.setParent(oTabs);

        //DataViewRule
        _oRuleTab = new RuleTab(_oPersonPanel);
        _oRuleTab.setParent(_oTabpanels);

        //Save Plans
        Tabpanel oTabpanel = new ConfirmTab();
        oTabpanel.setParent(_oTabpanels);

        //PDF & E-Mail
        oTabpanel = new JasperTab();
        oTabpanel.setParent(_oTabpanels);

        _oTabpanels.setParent(_oTabbox);
        oTabs.setParent(_oTabbox);
        _oTabbox.setParent(oCenter);

        oCenter.setParent(this);
    }

    /**
     * 
     * Setzt die Daten der angeklickten Person
     * @param oPerson
     *
     */
    public void setPerson(Person oPerson)
    {
        _oRuleTab.setValues(oPerson);
    }

    /**
     * 
     * Wird aufgerufen, wenn eine Person ausgewählt wird.
     *
     */
    public void onSelectPerson()
    {
        if (!_oPersonPanel.hasRegistration())
        {
            setPerson(_oPersonPanel.getSelectedPerson());
        }
    }

    /**
     * 
     * Stellt ein Person Panel zur Verfügung
     * 
     * @return _oPersonPanel
     *
     */
    public PersonPanel getPersonPanel()
    {
        return _oPersonPanel;
    }
}