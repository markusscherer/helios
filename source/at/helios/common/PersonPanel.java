package at.helios.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;

import at.helios.model.Department;
import at.helios.model.Person;

/**
 * Panel zur Auswahl von Personen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   29.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class PersonPanel extends Panel
{
    Listbox                _oListBox;
    Textbox                _oTextbox;
    private PersonBox _oRegistered;

    /**
     * Standard KOnstruktor, füllt GUI-Komponente
     **/
    public PersonPanel()
    {
        this.setId("PersonPanel");
        Toolbar oToolbar = new Toolbar();
        Label oLabel = new Label();
        oLabel.setValue("Suche ");
        oLabel.setParent(oToolbar);

        Component oFutureRoot = (Component) Sessions.getCurrent().getAttribute("PersonPanelOwner");

        _oTextbox = new Textbox();
        _oTextbox.setFocus(true);
        _oTextbox.setParent(oToolbar);

        _oTextbox.addForward("onChanging", this, "onTextboxChange");

        oToolbar.setParent(this);

        List<Person> coPersons = Person.getPersonsByDepartment((Department) Sessions.getCurrent()
            .getAttribute("department"));

        //Panelchildren
        Panelchildren oPanelChildren = new Panelchildren();
        _oListBox = new Listbox();
        _oListBox.setHeight("743px");

        //Header setzten
        Listhead oListhead = new Listhead();
        Listheader oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Nr.");
        oListheader.setParent(oListhead);
        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Nachname");
        oListheader.setParent(oListhead);
        oListheader = new Listheader();
        oListheader.setSort("auto");
        oListheader.setLabel("Vorname");
        oListheader.setParent(oListhead);
        oListhead.setParent(_oListBox);

        Listitem oListitem;
        Listcell oListcell;

        Collections.sort(coPersons, new Comparator<Person>()
        {
            public int compare(Person oPerson1, Person oPerson2)
            {
                return oPerson1.getEMTNumber().compareTo(oPerson2.getEMTNumber());
            }
        });

        for (Person oPerson : coPersons)
        {
            oListitem = new Listitem();
            oListitem.addForward("onClick", oFutureRoot, "onSelectPerson");
            oListitem.addForward("onOK", oFutureRoot, "onSelectPerson");
            oListitem.addForward("onClick", this, "onSelectPerson");
            oListitem.addForward("onOK", this, "onSelectPerson");
            oListitem.setValue(oPerson);
            oListcell = new Listcell();
            oListcell.setLabel(oPerson.getEMTNumber());
            oListcell.setParent(oListitem);
            oListcell = new Listcell();
            oListcell.setLabel(oPerson.getSurname());
            oListcell.setParent(oListitem);

            oListcell = new Listcell();
            oListcell.setLabel(oPerson.getForename());
            oListcell.setParent(oListitem);
            oListitem.setParent(_oListBox);

        }
        _oListBox.setParent(oPanelChildren);
        oPanelChildren.setParent(this);
    }

    @Override
    public void onPageAttached(Page coNewpage, Page coOldpage)
    {
        super.onPageAttached(coNewpage, coOldpage);
    }

    /**
     * Wird ausgeführt, wenn im Suchfenster eine Eingabe getätigt wird
     * @param oEvent    Event mit Informationen
     **/
    @SuppressWarnings("unchecked")
    public void onTextboxChange(Event oEvent)
    {
        oEvent = ((ForwardEvent) oEvent).getOrigin();
        InputEvent oInputEvent;

        if (oEvent instanceof InputEvent)
        {
            oInputEvent = (InputEvent) oEvent;
        }
        else
        {
            return;
        }

        Pattern oPattern = Pattern.compile("[0-9]*");
        Matcher oMatcher = oPattern.matcher(oInputEvent.getValue());

        if (oMatcher.matches())
        {
            for (Component oComponent : (List<Listitem>) _oListBox.getChildren())
            {
                if (oComponent instanceof Listitem)
                {
                    if (!((Person) ((Listitem) oComponent).getValue()).getEMTNumber().startsWith(
                        oInputEvent.getValue()))
                    {
                        oComponent.setVisible(false);
                    }
                    else
                    {
                        oComponent.setVisible(true);
                    }
                }
            }
        }
        else
        {
            for (Component oComponent : (List<Listitem>) _oListBox.getChildren())
            {
                if (oComponent instanceof Listitem)
                {
                    if (!(((Person) ((Listitem) oComponent).getValue()).getForename().toLowerCase()
                        .startsWith(oInputEvent.getValue().toLowerCase()) || ((Person) ((Listitem) oComponent)
                        .getValue()).getSurname().toLowerCase().startsWith(
                        oInputEvent.getValue().toLowerCase())))
                    {
                        oComponent.setVisible(false);
                    }
                    else
                    {
                        oComponent.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Wird ausgeführt, wenn Person ausgewählt wird
     **/
    public void onSelectPerson()
    {
        Person oPerson = (Person) _oListBox.getSelectedItem().getValue();

        if (_oRegistered != null)
        {
            _oRegistered.setSelectedByValue(oPerson);
            _oRegistered = null;
        }
    }

    /**
     * Gibt ausgewählte Person zurück
     * @return    ausgewählte Person
     **/
    public Person getSelectedPerson()
    {
        try
        {
            return (Person) _oListBox.getSelectedItem().getValue();
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    /**
     * Registiert PersonCombobox
     * @param oComboBox    PersonComboBox
     **/
    public void register(PersonBox oComboBox)
    {
        _oRegistered = oComboBox;
    }

    /**
     * Überprüft ob PersonComboBox registriert ist
     * @return    Ist eine PersonComboBox registriert?
     **/
    public boolean hasRegistration()
    {
        return _oRegistered != null;
    }
}
