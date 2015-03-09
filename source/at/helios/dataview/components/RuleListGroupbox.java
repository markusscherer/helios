package at.helios.dataview.components;

import java.util.Collection;
import java.util.GregorianCalendar;

import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import at.helios.calendar.helper.DateHelper;
import at.helios.dataview.RuleboxHelper;
import at.helios.model.Person;
import at.helios.model.dao.RuleDAO;
import at.helios.sheduling.DateRule;
import at.helios.sheduling.DayRule;
import at.helios.sheduling.FriendRule;
import at.helios.sheduling.Rule;
import at.helios.sheduling.Shifts;

/**
 * 
 * Stellt eine Groubox mit allen
 * Regeln der Person dar.
 * 
 * @author
 * <PRE>
 *         ID    date         description
 *         mab   28.03.2009   Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class RuleListGroupbox extends Groupbox
{
    private RuleboxHelper _oRuleboxHelper;
    private Listbox       _oListBox;
    private Person        _oPerson;

    /**
     * 
     * erzeugt die Box für alle Regeln
     *
     */
    public RuleListGroupbox(RuleboxHelper oRuleBean)
    {
        _oRuleboxHelper = oRuleBean;
        _oRuleboxHelper.setRuleListBox(this);
        new Caption("Alle Regeln").setParent(this);
        this.setMold("3d");
        _oListBox = new Listbox();
    }

    /**
     * 
     * erzeugt eine neue personenabhängige Liste
     * mit deren Rollen
     * 
     * @param oPerson
     *
     */
    public void generateDefinedRules(Person oPerson)
    {
        _oListBox = new Listbox();
        _oListBox.setStyle("width: 710px; margin: 0px");

        Listhead oListhead = new Listhead();
        Listheader oListheader = new Listheader();

        oListheader.setSort("auto");
        oListheader.setLabel("Modus");
        oListheader.setStyle("min-width: 20px");
        oListheader.setParent(oListhead);

        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Mitarbeiter");
        oListheader.setStyle("min-width: 30px");
        oListheader.setParent(oListhead);

        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Schicht");
        oListheader.setStyle("min-width: 50px");
        oListheader.setParent(oListhead);

        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Wochentag");
        oListheader.setStyle("min-width: 50px");
        oListheader.setParent(oListhead);

        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setStyle("min-width: 50px");
        oListheader.setLabel("Datum");
        oListheader.setParent(oListhead);

        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setStyle("width: 25px");
        oListheader.setLabel("");
        oListheader.setParent(oListhead);

        oListhead.setParent(_oListBox);

        RuleDAO oRuleDAO = new RuleDAO();

        Collection<Rule> coRules = oRuleDAO.findByRuleOwner(oPerson);

        for (Rule oRule : coRules)
        {
            addRuleToListBox(oRule);
            _oRuleboxHelper.fillList(oRule);
        }
        _oListBox.setParent(this);
    }

    /**
     * 
     * Fügt der Liste eine Regel hinzu
     * 
     * @param oRule
     *
     */
    public void addRuleToListBox(Rule oRule)
    {
        if (oRule instanceof FriendRule)
        {
            _oListBox.appendChild(createListitem(((FriendRule) oRule).getFriend(), ((FriendRule) oRule)
                .isLikes(), null, null, 0, oRule));
        }
        else if (oRule instanceof DayRule)
        {
            _oListBox.appendChild(createListitem(null, ((DayRule) oRule).isLikes(), null, ((DayRule) oRule)
                .getShift() == Shifts.dayshift ? "Tagschicht" : "Nachtschicht", ((DayRule) oRule).getDayId(),
                oRule));
        }
        else if (oRule instanceof DateRule)
        {
            _oListBox.appendChild(createListitem(null, ((DateRule) oRule).isLikes(), ((DateRule) oRule)
                .getDate(), ((DateRule) oRule).getShift() == Shifts.dayshift ? "Tagschicht" : "Nachtschicht",
                0, oRule));
        }
    }

    /**
     * 
     * Gibt ein gefülltes Listitem zurück
     * @param oPerson
     * @param bLikes
     * @param oCal
     * @param sShift
     * @param iDay
     * @return gefülltes Listitemarg0
     *
     */
    public Listitem createListitem(Person oFriend, boolean bLikes, GregorianCalendar oCal, String sShift,
        int iDay, Rule oRule)
    {
        String sInput;
        Image oImage;
        Listitem oListitem = new Listitem();
        oListitem.setAttribute("Rule", oRule);

        if (bLikes)
            oImage = new Image("/design/status_ok.png");
        else
            oImage = new Image("/design/status_notok.png");

        Listcell oListcell = new Listcell();
        oListcell.setValue(bLikes);
        oListcell.appendChild(oImage);
        //oListcell.setId("like");
        oListcell.setParent(oListitem);

        if (oFriend == null)
            sInput = "";
        else
            sInput = oFriend.getEMTNumber();

        oListcell = new Listcell();
        oListcell.setValue(oFriend);
        oListcell.setLabel(sInput);
        //oListcell.setId("emt");
        oListcell.setParent(oListitem);

        oListcell = new Listcell();
        oListcell.setValue(sShift);
        oListcell.setLabel(sShift);
        //oListcell.setId("shift");
        oListcell.setParent(oListitem);

        if (iDay == 0)
            sInput = "";
        else
            sInput = DateHelper.getDayByPK(iDay);

        oListcell = new Listcell();
        oListcell.setValue(iDay);
        oListcell.setLabel(sInput);
        //oListcell.setId("weekday");
        oListcell.setParent(oListitem);

        if (oCal == null)
            sInput = "";
        else
            sInput = DateHelper.getFormatedDate(oCal, "dd.MM.yyyy");

        oListcell = new Listcell();
        oListcell.setValue(oCal);
        oListcell.setLabel(sInput);
        //oListcell.setId("date");
        oListcell.setParent(oListitem);

        oListcell = new Listcell();
        oListcell.addForward("onClick", this, "onDelete");
        oListcell.appendChild(new Image("/design/delete.png"));
        oListcell.setParent(oListitem);

        return oListitem;
    }

    /**
     * Setzt die Werte der übergebenen Person
     * 
     * @param oPerson  - ausgewählte Person
     *
     */
    public void setValues(Person oPerson)
    {
        _oPerson = oPerson;
        this.removeChild(_oListBox);
        _oRuleboxHelper.flushRuleList();
        generateDefinedRules(oPerson);
    }

    /**
     * 
     * Baut die Regelliste neu auf
     *
     */
    public void rebuildList()
    {
        if (_oPerson != null)
        {
            this.removeChild(_oListBox);
            _oRuleboxHelper.flushRuleList();
            generateDefinedRules(_oPerson);
        }
    }

    /**
     * 
     * Löscht ein Item aus der Liste und
     * und setzt diese auf die Liste der zu
     * löschenden
     *
     */
    public void onDelete()
    {
        _oRuleboxHelper.removeRule((Rule) _oListBox.getSelectedItem().getAttribute("Rule"));
        _oListBox.removeChild(_oListBox.getSelectedItem());
    }
}
