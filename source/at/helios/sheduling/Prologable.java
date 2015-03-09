package at.helios.sheduling;

/**
 * Interface für in Prolog implementierte Klassen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   29.03.2009 Erstkommentierung
 * </PRE>
 **/
public interface Prologable
{
    /**
     * Gibt Prolog-Statement zurück
     * @return    Prolog-Statement
     **/
    public String getPrologStatement();
}