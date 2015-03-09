/**
 * 
 */
package at.helios.common;

import org.zkoss.zul.Textbox;

import at.helios.model.Person;

/**
 * ComboBox zum ausfüllen von Personen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   07.09.2009 Erstkommentierung
 *         mas   XX.01.2010 effektivere Datenhaltung erstellt, Interface hinzugefügt
 * </PRE>
 **/
@SuppressWarnings("serial")
public class SmallPersonBox extends Textbox implements PersonBox
{
    private PersonPanel _oPersonPanel = null;
    private Person      _oPerson      = null;

    public SmallPersonBox()
    {
        this.setWidth("60px");
        this.addForward("onOK", this, "onChange");
    }

    /**
     * Setter für _oPersonPanel
     * @param oPersonPanel
     **/
    public void setPersonPanel(PersonPanel oPersonPanel)
    {
        _oPersonPanel = oPersonPanel;
    }

    public void onChange()
    {
        _oPerson = Person.getPersonByEMTNumber(this.getValue());
        
        if(this.getValue().isEmpty())
        {
            this.setStyle("background: #ffffff");
        }
        else
        {
            validate();
        }
    }

    private void validate()
    {
        if(_oPerson == null)
        {
            this.setStyle("background: #ffaaaa");
            this.setValue("nicht gefunden");
        }
        else
        {
            this.setStyle("background: #aaffaa");
            this.setValue(_oPerson.getEMTNumber());
        }
    }

    /**
     * Setzt ausgewähltes Item anhand von Person
     * @param oPerson
     **/
    public void setSelectedByValue(Person oPerson)
    {
        _oPerson = oPerson;
        validate();
    }

    /* (non-Javadoc)
     * @see at.helios.common.PersonBox#getSelectedPerson()
     */
    @Override
    public Person getSelectedPerson()
    {
        return _oPerson;
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

}
