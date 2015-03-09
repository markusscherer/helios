package at.helios.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import at.helios.model.Department;
import at.helios.model.Person;

/**
 * ComboBox zum ausfüllen von Personen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   29.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class PersonComboBox extends Combobox implements PersonBox
{
    private PersonPanel _oPersonPanel = null;

    /**
     * Setter für _oPersonPanel
     * @param oPersonPanel
     **/
    public void setPersonPanel(PersonPanel oPersonPanel)
    {
        _oPersonPanel = oPersonPanel;
    }

    /**
     * Standard Konstruktor, füllt GUI-Komponente
     **/
    public PersonComboBox()
    {
        List<Person> coPersons = Person.getPersonsByDepartment((Department) Sessions.getCurrent()
            .getAttribute("department"));

        Collections.sort(coPersons, new Comparator<Person>()
        {
            public int compare(Person oPerson1, Person oPerson2)
            {
                return oPerson1.getEMTNumber().compareTo(oPerson2.getEMTNumber());
            }
        });

        for (Person oPerson : coPersons)
        {
            Comboitem oComboitem = new Comboitem();
            oComboitem.setLabel(oPerson.getEMTNumber() + " " + oPerson.getForename() + " "
                + oPerson.getSurname());
            oComboitem.setValue(oPerson);
            oComboitem.setParent(this);
        }

        if(this.getChildren().size() != 0)
        {
            this.setSelectedIndex(0);
        }
        this.setReadonly(true);

    }

    /**
     * Konstruktor für platzsparende Variante
     * @param sStyle
     **/
    public PersonComboBox(String sStyle)
    {
        if (sStyle.equals("short"))
        {
            List<Person> coPersons = Person.getPersonsByDepartment((Department) Sessions.getCurrent()
                .getAttribute("department"));

            Collections.sort(coPersons, new Comparator<Person>()
            {
                public int compare(Person oPerson1, Person oPerson2)
                {
                    return oPerson1.getEMTNumber().compareTo(oPerson2.getEMTNumber());
                }
            });

            for (Person oPerson : coPersons)
            {
                Comboitem oComboitem = new Comboitem();
                oComboitem.setLabel(oPerson.getEMTNumber());
                //                oComboitem.setLabel(Integer.toString(oPerson.getPersonId()));
                oComboitem.setValue(oPerson);
                oComboitem.setParent(this);
            }

            this.setWidth("60px");
            this.setReadonly(true);
        }
    }

    
    /* (non-Javadoc)
     * @see at.helios.common.PersonBox#setSelectedByValue(at.helios.model.Person)
     */
    public void setSelectedByValue(Person oPerson)
    {
        for (Object oObject : getChildren())
        {
            if (oObject instanceof Comboitem)
            {
                Comboitem oComboitem = (Comboitem) oObject;
                if (oComboitem.getValue() == oPerson)
                {
                    this.setSelectedItem(oComboitem);
                    return;
                }
            }
        }
    }

    /**
     * Wird ausgeführt, wenn Combobox ausgewählt wird
     **/
    public void onFocus()
    {
        if (_oPersonPanel != null)
        {
            _oPersonPanel.register(this);
        }
    }

    /* (non-Javadoc)
     * @see at.helios.common.PersonBox#getSelectedPerson()
     */
    @Override
    public Person getSelectedPerson()
    {
        return (Person) this.getSelectedItem().getValue();
    }
    
    
}
