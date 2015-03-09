package at.helios.calendar.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Helper für Feiertage
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   29.03.2009 Erstkommentierung
 * </PRE>
 **/
public class HolidayHelper
{
    
    private List<GregorianCalendar> _coDates = new ArrayList<GregorianCalendar>();
    
    /**
     * Fügt Feiertag hinzu
     * @param oDate Datem des Feiertages
     **/
    public void addHoliday(Date oDate)
    {
        GregorianCalendar oCalendar = new GregorianCalendar();
        oCalendar.setTime(oDate);
        _coDates.add(oCalendar);
    }
    
    /**
     * Entfernt Feiertag
     * @param oCalendar    Datum des Feiertages
     **/
    public void removeHoliday(GregorianCalendar oCalendar)
    {
        _coDates.remove(oCalendar);
    }
    
    /**
     * Überprüft ob Tag ein Feiertag ist
     * @param oCalendar
     * @return    Ist der Tag ein Feiertag?
     **/
    public boolean isHoliday(GregorianCalendar oCalendar)
    {
        return _coDates.contains(oCalendar);
    }

}
