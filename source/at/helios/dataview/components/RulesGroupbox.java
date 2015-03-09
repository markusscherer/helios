package at.helios.dataview.components;

import java.util.Collection;
import java.util.Date;

import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Vbox;

import at.helios.calendar.helper.DateHelper;
import at.helios.common.PersonComboBox;
import at.helios.common.PersonPanel;
import at.helios.dataview.PersonDataRules;
import at.helios.dataview.RuleboxHelper;
import at.helios.model.Person;
import at.helios.sheduling.DateRule;
import at.helios.sheduling.DayRule;
import at.helios.sheduling.FriendRule;
import at.helios.sheduling.Rule;
import at.helios.sheduling.Shifts;

/**
 * 
 * Diese Klasse stellt eine Oberfläche bereit,
 * mit der es möglich ist neue Regelen aller Art
 * hinzuzufügen.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   28.03.2009  Neuerstellung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class RulesGroupbox extends Groupbox implements PersonDataRules
{
    private Listbox        _oListbox;
    private Hbox           _oFriendRule;
    private Hbox           _oDateRule;
    private Hbox           _oDayRule;
    private Hbox           _oHoliday;
    private PersonComboBox _oFriendbox;
    private Checkbox       _oFriendV;
    private Datebox        _oDatebox;
    private Datebox        _oHolidayDateboxStart;
    private Datebox        _oHolidayDateboxEnd;
    private Checkbox       _oDayshiftDate;
    private Checkbox       _oNightshiftDate;
    private Checkbox       _oDayshiftDay;
    private Checkbox       _oNightshiftDay;
    private Checkbox       _oDateV;
    private Checkbox       _oDayV;
    private RuleboxHelper  _oRuleboxDAO;
    private PersonPanel    _oPersonPanel;
    private Person         _oOwnerPerson;
    private RuleboxHelper  _oRuleboxHelper;

    /**
     * Setzt die übergebenen Werte als Instanzvariablen
     */
    public RulesGroupbox(RuleboxHelper oRuleboxDAO, PersonPanel oPersonPanel, RuleboxHelper oRuleboxHelper)
    {
        _oPersonPanel = oPersonPanel;
        _oRuleboxDAO = oRuleboxDAO;
        _oRuleboxHelper = oRuleboxHelper;

        initGui();
    }

    /**
     * Initialisiert die GUI
     */
    public void initGui()
    {
        new Caption("Regeln").setParent(this);
        this.setMold("3d");

        Hbox oRuleBox = new Hbox();

        Div oRuleDiv = new Div();
        oRuleDiv.setStyle("padding: 2px;");

        _oListbox = new Listbox();
        _oListbox.addForward("onSelect", this, "onRuleChange");
        _oListbox.setRows(1);
        _oListbox.setMold("select");

        Listitem oListitem = new Listitem("keine");
        oListitem.setValue("keine");
        oListitem.setParent(_oListbox);
        _oListbox.setSelectedItem(oListitem);

        oListitem = new Listitem("Mitarbeiter");
        oListitem.setValue("friend");
        oListitem.setParent(_oListbox);

        oListitem = new Listitem("Datum");
        oListitem.setValue("date");
        oListitem.setParent(_oListbox);

        oListitem = new Listitem("Tag");
        oListitem.setValue("day");
        oListitem.setParent(_oListbox);

        oListitem = new Listitem("Urlaub");
        oListitem.setValue("holiday");
        oListitem.setParent(_oListbox);

        _oListbox.setParent(oRuleDiv);
        oRuleDiv.setParent(oRuleBox);

        //FriendRule
        _oFriendRule = new Hbox();
        _oFriendRule.setVisible(false);

        _oFriendbox = new PersonComboBox();
        _oFriendbox.setPersonPanel(_oPersonPanel);
        //        _oFriendbox.addForward("onFocus", _oPersonPanel, "onChoosePerson");
        _oFriendbox.setParent(_oFriendRule);

        _oFriendV = new Checkbox();
        _oFriendV.setLabel("Verneinung?");
        _oFriendV.setParent(_oFriendRule);
        _oFriendRule.setParent(oRuleBox);
        //
        Button oButton = new Button("Einfügen");
        oButton.addForward("onClick", this, "onAddFriend");
        //        oButton.addForward("onClick", _oPersonPanel, "onChoosePerson");
        oButton.setParent(_oFriendRule);

        //DateRule
        _oDateRule = new Hbox();
        _oDateRule.setVisible(false);

        _oDatebox = new Datebox();
        _oDatebox.setStyle("padding: 20px");
        _oDatebox.setParent(_oDateRule);

        Vbox oVbox = new Vbox();
        oVbox.setStyle("padding-left: 40px");

        _oDayshiftDate = new Checkbox();
        _oDayshiftDate.setLabel("Tagschicht");
        _oDayshiftDate.setParent(oVbox);

        _oNightshiftDate = new Checkbox();
        _oNightshiftDate.setLabel("Nachtschicht");
        _oNightshiftDate.setParent(oVbox);

        oVbox.setParent(_oDateRule);

        _oDateV = new Checkbox();
        _oDateV.setStyle("padding-left: 40px");
        _oDateV.setLabel("Verneinung?");
        _oDateV.setParent(_oDateRule);

        oButton = new Button("Einfügen");
        oButton.addForward("onClick", this, "onAddDateRule");
        oButton.setParent(_oDateRule);

        _oDateRule.setParent(oRuleBox);

        //DayRule      
        _oDayRule = new Hbox();
        _oDayRule.setVisible(false);
        Vbox oDayVbox = new Vbox();
        String[] sWeekDays = DateHelper.getWeekDays();

        Checkbox oCheckbox;

        for (int i = 0; i < sWeekDays.length; i++)
        {
            if (i == 4)
            {
                oDayVbox.setParent(_oDayRule);
                oDayVbox = new Vbox();
            }
            oCheckbox = new Checkbox();
            oCheckbox.setLabel(sWeekDays[i]);
            oCheckbox.setId("WKD_" + sWeekDays[i]);
            oCheckbox.setParent(oDayVbox);
        }
        oDayVbox.setParent(_oDayRule);

        _oDayshiftDay = new Checkbox();
        _oDayshiftDay.setLabel("Tagschicht");
        _oDayshiftDay.setParent(_oDayRule);

        _oNightshiftDay = new Checkbox();
        _oNightshiftDay.setLabel("Nachtschicht");
        _oNightshiftDay.setParent(_oDayRule);

        _oDayV = new Checkbox();
        _oDayV.setStyle("padding-left: 40px");
        _oDayV.setLabel("Verneinung?");
        _oDayV.setParent(_oDayRule);

        oButton = new Button("Einfügen");
        oButton.addForward("onClick", this, "onAddDayRule");
        oButton.setParent(_oDayRule);

        _oDayRule.setParent(oRuleBox);

        //Holiday
        _oHoliday = new Hbox();
        _oHoliday.setVisible(false);

        oVbox = new Vbox();
        oVbox.setStyle("padding-left: 40px");

        Hbox oHbox = new Hbox();
        oHbox.appendChild(new Label("Urlaub Start"));
        _oHolidayDateboxStart = new Datebox();
        _oHolidayDateboxStart.setStyle("padding: 20px");
        _oHolidayDateboxStart.setParent(oHbox);
        oHbox.setParent(oVbox);

        oHbox = new Hbox();
        oHbox.appendChild(new Label("Urlaub Ende"));
        _oHolidayDateboxEnd = new Datebox();
        _oHolidayDateboxEnd.setStyle("padding: 20px");
        _oHolidayDateboxEnd.setParent(oHbox);
        oHbox.setParent(oVbox);

        oVbox.setParent(_oHoliday);

        oButton = new Button("Einfügen");
        oButton.addForward("onClick", this, "onAddHoliday");
        oButton.setParent(_oHoliday);

        _oHoliday.setParent(oRuleBox);

        //Urlaub Ende

        oRuleBox.setParent(this);
    }

    /**
     * 
     * Wird aufgerufen wenn ein anderer Regeltyp
     * ausgewählt wird, und zeigt die den gewünschten
     * Regeltyp an
     *
     */
    public void onRuleChange()
    {
        String sSelItem = (String) _oListbox.getSelectedItem().getValue();

        if (sSelItem.equalsIgnoreCase("keine"))
        {
            _oDateRule.setVisible(false);
            _oFriendRule.setVisible(false);
            _oDayRule.setVisible(false);
        }

        else if (sSelItem.equalsIgnoreCase("friend"))
        {
            _oDateRule.setVisible(false);
            _oDayRule.setVisible(false);
            _oHoliday.setVisible(false);
            _oFriendRule.setVisible(true);
        }

        else if (sSelItem.equalsIgnoreCase("date"))
        {
            _oFriendRule.setVisible(false);
            _oDayRule.setVisible(false);
            _oHoliday.setVisible(false);
            _oDateRule.setVisible(true);
        }

        else if (sSelItem.equalsIgnoreCase("day"))
        {
            _oDateRule.setVisible(false);
            _oFriendRule.setVisible(false);
            _oHoliday.setVisible(false);
            _oDayRule.setVisible(true);
        }

        else if (sSelItem.equalsIgnoreCase("holiday"))
        {
            _oDateRule.setVisible(false);
            _oFriendRule.setVisible(false);
            _oDayRule.setVisible(false);
            _oHoliday.setVisible(true);
        }
    }

    /**
     * 
     * Setzt die personenspezifischen Daten
     * im RuleEditor
     * 
     * @param oPerson - ausgewählte Person
     *
     */
    public void setValues(Person oPerson)
    {
        _oOwnerPerson = oPerson;
        _oFriendbox.setValue("");
        _oFriendbox.removeAttribute("PFriend");
        _oHolidayDateboxStart.setValue(null);
        _oHolidayDateboxEnd.setValue(null);
    }

    /**
     * 
     * Fügt eine bevorzugte bzw. nicht
     * bevorzugte Person der Liste hinzu
     *
     */
    public void onAddFriend()
    {
        if (_oRuleboxDAO.checkRule(_oFriendbox.getSelectedPerson(), _oOwnerPerson))
        {
            Rule oRule = new FriendRule(0, !_oFriendV.isChecked(), (Person) _oFriendbox.getSelectedItem()
                .getValue());
            oRule.setRuleOwner(_oOwnerPerson);

            _oRuleboxDAO.addRule(oRule);
        }
    }

    /**
     * 
     * wird aufgerufen wenn eine DateRule
     * hinzugefügt wird und anschließen in die
     * Liste eingefügt
     *
     */
    public void onAddDateRule()
    {
        Date oDate = _oDatebox.getValue();

        if (oDate != null)
        {
            if (_oDayshiftDate.isChecked())
            {
                addDateRuletoList("ds", oDate);
            }

            if (_oNightshiftDate.isChecked())
            {
                addDateRuletoList("ns", oDate);
            }

            if (!_oNightshiftDate.isChecked() && !_oDayshiftDate.isChecked())
            {
                addDateRuletoList("ds", oDate);
                addDateRuletoList("ns", oDate);
            }
        }
    }

    /**
     * 
     * Fügt die alle ausgewählten Regeln
     * der Liste hinzu
     * 
     * @param sShift
     * @param oDate
     *
     */
    private void addDateRuletoList(String sShift, Date oDate)
    {
        //Auf die Schicht prüfen
        if (sShift.equals("ds"))
        {
            if (_oRuleboxDAO.checkRule(DateHelper.getCalendarFromDate(oDate), Shifts.dayshift, _oOwnerPerson))
            {
                Rule oRule = new DateRule(0, !_oDateV.isChecked(), DateHelper.getCalendarFromDate(oDate),
                    Shifts.dayshift);
                oRule.setRuleOwner(_oOwnerPerson);
                _oRuleboxDAO.addRule(oRule);
            }
        }
        else if (sShift.equals("ns"))
        {
            if (_oRuleboxDAO.checkRule(DateHelper.getCalendarFromDate(oDate), Shifts.nightshift,
                _oOwnerPerson))
            {
                Rule oRule = new DateRule(0, !_oDateV.isChecked(), DateHelper.getCalendarFromDate(oDate),
                    Shifts.nightshift);
                oRule.setRuleOwner(_oOwnerPerson);
                _oRuleboxDAO.addRule(oRule);
            }
        }
    }

    /**
     * 
     * Fügt alle ausgewählten Tagesregeln der
     * Liste hinzu
     *
     */
    public void onAddDayRule()
    {
        String[] sWeekDays = DateHelper.getWeekDays();
        Rule oRule;
        for (int i = 0; i < sWeekDays.length; i++)
        {
            if (((Checkbox) this.getFellowIfAny("WKD_" + sWeekDays[i])).isChecked()
                && _oNightshiftDay.isChecked())
            {
                if (_oRuleboxDAO.checkRule(i + 1, Shifts.nightshift, _oOwnerPerson))
                {
                    oRule = new DayRule(0, !_oDayV.isChecked(), i + 1, Shifts.nightshift);
                    oRule.setRuleOwner(_oOwnerPerson);
                    _oRuleboxDAO.addRule(oRule);
                }
            }
            if (((Checkbox) this.getFellowIfAny("WKD_" + sWeekDays[i])).isChecked()
                && _oDayshiftDay.isChecked())
            {
                if (_oRuleboxDAO.checkRule(i + 1, Shifts.dayshift, _oOwnerPerson))
                {
                    oRule = new DayRule(0, !_oDayV.isChecked(), i + 1, Shifts.dayshift);
                    oRule.setRuleOwner(_oOwnerPerson);
                    _oRuleboxDAO.addRule(oRule);
                }
            }
            if (((Checkbox) this.getFellowIfAny("WKD_" + sWeekDays[i])).isChecked()
                && !_oNightshiftDay.isChecked() && !_oDayshiftDay.isChecked())
            {
                if (_oRuleboxDAO.checkRule(i + 1, Shifts.nightshift, _oOwnerPerson))
                {
                    oRule = new DayRule(0, !_oDayV.isChecked(), i + 1, Shifts.nightshift);
                    oRule.setRuleOwner(_oOwnerPerson);
                    _oRuleboxDAO.addRule(oRule);
                }

                if (_oRuleboxDAO.checkRule(i + 1, Shifts.dayshift, _oOwnerPerson))
                {
                    oRule = new DayRule(0, !_oDayV.isChecked(), i + 1, Shifts.dayshift);
                    oRule.setRuleOwner(_oOwnerPerson);
                    _oRuleboxDAO.addRule(oRule);
                }
            }
        }
    }

    /**
     * 
     * Fügt den angegebenen Urlaub Bereich
     * als Regeln hinzu.
     *
     */
    @SuppressWarnings("deprecation")
    public void onAddHoliday()
    {
        Date oStartDate = (Date) _oHolidayDateboxStart.getValue();
        Date oEndDate = (Date) _oHolidayDateboxEnd.getValue();

        if (oStartDate != null && oEndDate != null && oStartDate.before(oEndDate))
        {
            //StartDatum hinzufügen
            addDateRuletoList("ds", oStartDate);
            addDateRuletoList("ns", oStartDate);

            //Alle bis zum EndDatum hinzufügen
            do
            {
                oStartDate.setDate(oStartDate.getDate() + 1);

                addDateRuletoList("ds", oStartDate);
                addDateRuletoList("ns", oStartDate);
            }
            while (!oStartDate.equals(oEndDate));
        }
        else
            _oRuleboxHelper.setInfo("Datum nicht korrekt eingetragen");
    }

    /**
     * Speichert die alle Regeln
     */
    public void save()
    {
        Collection<Rule> coDeletedRules = _oRuleboxDAO.getDeletedRules();
        Collection<Rule> coCurrentRules = _oRuleboxDAO.getRulesOfPerson();

        for (Rule oRule : coCurrentRules)
        {
            if (oRule.getRuleId() == 0 && !_oOwnerPerson.getRules().contains(oRule))
                _oOwnerPerson.addRule(oRule);
        }

        for (Rule oRule : coDeletedRules)
        {
            _oOwnerPerson.removeRule(oRule);
        }
    }
}