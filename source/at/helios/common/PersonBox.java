/**
 * 
 */
package at.helios.common;

import org.zkoss.zk.ui.Component;

import at.helios.model.Person;

/**
 * Komponenten-Interface für Personen-Auswahl
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   17.12.2009 Neuerstellun
 * </PRE>
 */
public interface PersonBox extends Component
{
    /**
     * Setzt das zu verwendende PersonPanel
     * @param oPersonPanel
     */
    public void setPersonPanel(PersonPanel oPersonPanel);
    
    
    /**
     * Gibt die ausgewählte Person (oder null) zurück
     * @return ausgewählte Person
     */
    public Person getSelectedPerson();
    
    /**
     * Setzt ausgewähltes Item anhand von Person
     * @param oPerson
     **/
    public void setSelectedByValue(Person oPerson);
}
