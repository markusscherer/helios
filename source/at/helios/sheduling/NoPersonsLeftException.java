/**
 * 
 */
package at.helios.sheduling;

/**
 * Wird geworfen, wenn keine validen Personen mehr zur Einteilung zur Verfügung stehen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   21.01.2010 Neuerstellung
 * </PRE>
 */
@SuppressWarnings("serial")
public class NoPersonsLeftException extends Exception
{
    /**
     * Standard-Konstruktor
     */
    public NoPersonsLeftException()
    {
    }
    
    public NoPersonsLeftException(String sMessage)
    {
        super(sMessage);
    }
}
