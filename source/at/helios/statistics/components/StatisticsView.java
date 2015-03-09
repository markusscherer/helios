package at.helios.statistics.components;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import at.helios.common.PersonPanel;
import at.helios.dataview.components.ConfirmTab;
import at.helios.model.Person;

/**
 * Ansicht für Statistik
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   30.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class StatisticsView extends Borderlayout
{
    private West                _oWest   = new West();
    private Center              _oCenter = new Center();
    private PersonPanel         _oPanel;
    private Tabpanels           _oTabpanels;
    private Tabbox              _oTabbox;
    private SimpleStatisticsTab _oSimpleStatisticsTab;

    /**
     * Standardkonstruktor, füllt GUI-Komponente
     **/
    public StatisticsView()
    {
        Session oSession = Sessions.getCurrent();
        oSession.setAttribute("PersonPanelOwner", this);

        this.setId("StatisticsView");
        _oWest.setParent(this);
        _oCenter.setParent(this);

        _oWest.setWidth("250px");

        _oPanel = new PersonPanel();
        _oPanel.setParent(_oWest);
        _oWest.setTitle("Freiwillige");
        _oWest.setCollapsible(true);
        _oWest.setSize("250px");

        _oTabbox = new Tabbox();
        Tabs oTabs = new Tabs();
        _oTabpanels = new Tabpanels();

        Tab oTab = new Tab("Einfache Anfragen");
        oTab.setParent(oTabs);
        oTab = new Tab("Komplexe Anfragen");
        oTab.setParent(oTabs);

        _oSimpleStatisticsTab = new SimpleStatisticsTab();
        (_oSimpleStatisticsTab).setParent(_oTabpanels);
        (new ComplexStatisticsTab()).setParent(_oTabpanels);
        (new ConfirmTab()).setParent(_oTabpanels);

        _oTabpanels.setParent(_oTabbox);
        oTabs.setParent(_oTabbox);
        _oTabbox.setParent(_oCenter);
    }

    /**
     * Gibt die im Panel ausgewählte Person zurück
     * @return    ausgewählte Person
     **/
    public Person getSelectedPerson()
    {
        return _oPanel.getSelectedPerson();
    }

    /**
     * Wird ausgeführt, wenn Person im Panel ausgewählt wird
     **/
    public void onSelectPerson()
    {
        _oSimpleStatisticsTab.onPersonSelected();
    }

    /**
     * Getter für _oPanel zurück
     * @return    _oPanel
     **/
    public PersonPanel getPersonPanel()
    {
        return _oPanel;
    }
}
