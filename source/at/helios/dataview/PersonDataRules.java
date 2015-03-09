package at.helios.dataview;

import org.zkoss.zk.ui.Component;

import at.helios.model.Person;

/**
 * 
 * Dieses Interface definiert benötigte
 * Methoden, die für die in der Data View im Bereich
 * Regeln benötigt werden.
 * 
 * @author
 * <PRE>
 *         ID      date          description
 *         mab     28.03.2009    Erstkommentierung
 * </PRE>
 *
 */
public interface PersonDataRules
{
    /**
     * 
     * baut die Gui auf
     *
     */
    public void initGui();

    /**
     * 
     * Speichert die eingegebenen Daten
     *
     */
    public void save();

    /**
     * 
     * Setzt die Werte der aufgerufenen Person
     *
     */
    public void setValues(Person oPerson);

    /**
     * 
     * Setzt den Style
     * 
     * @param sString - Style
     *
     */
    public void setStyle(String sString);

    /**
     * 
     * Setzt den Parent
     * 
     * @param oComponent - Vaterkomponente
     *
     */
    public void setParent(Component oComponent);
}
