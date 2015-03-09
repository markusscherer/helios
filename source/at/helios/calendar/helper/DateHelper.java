package at.helios.calendar.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 
 * Diese Klasse stellt nützliche Methoden im
 * Zusammenhang mit dem Datum und
 * Calendar zur Verfügung.
 * 
 * @author
 * <PRE>
 *         ID    date         description
 *         mab   29.03.2009   Erstkommentierung
 * </PRE>
 *
 */
public class DateHelper
{
    private static String _sMonth[] = {"","Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
        "September", "Oktober", "November", "Dezember"};
    
    private static String[] _sWeekDays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    
/*********** alte Methoden für Drag&Drop Calendar - Anfang (nicht getestet) *****************/

    /**
     * 
     * Gibt einen Calendar des Jahres iCalYear und des Monats iMonth zurück
     * @param iCalYear
     * @param iMonth
     * @return oCal
     *
     */
    @Deprecated
    public static Calendar getCal(int iCalYear, int iMonth)
    {
        Calendar oCal = Calendar.getInstance();
        oCal.set(Calendar.YEAR, iCalYear);
        oCal.set(Calendar.MONTH, iMonth);

        return oCal;
    }

    
    /**
     * 
     * Die maximalen Tage Monat iMonth im Jahr iCalYear zurück
     * 
     * @param iCalYear
     * @param iMonth
     * @return maximalen Tage des Monats
     *
     */
    @Deprecated
    public static int getMaxDaysOfMonth(int iCalYear, int iMonth)
    {
        return getCal(iCalYear, iMonth).getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gibt den ersten Tag (Wochentag) des Monats iMonth im Jahr iYear zurück
     * 
     * @param iMonth
     * @param iYear
     * @return Ersten Tag des Monats
     *
     */
    @Deprecated
    public static int getFirstDayOfMonth(int iMonth, int iYear)
    {
        Calendar oCal = getCal(iYear, iMonth);
        oCal.set(Calendar.DATE, 1);

        //Hier muss ein Tag abgezogen werden, da die Woche am Sonntag beginnt.
        return (oCal.get(Calendar.DAY_OF_WEEK)-1);
    }

    /**
     * 
     * Prüft ob der Tag ein Wochentag ist.
     * 
     * @param iDayofWeek
     * @return true --> Wochentag
     *         false --> Wochenende
     *
     */
    @Deprecated
    public static boolean isWeekDay(int iDayofWeek)
    {
        //Ist der aktuelle Tag ein Samstag
        if (iDayofWeek == 6)
        {
            return false;
        }
        //Ist der aktuelle Tag ein Sonntag
        else if (iDayofWeek == 7)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 
     * Gibt das aktuelle Datum speziell zurück
     * 
     * @param iCalYear
     * @param iMonth
     * @return formatiertes Datum
     *
     */
    @Deprecated
    public static String getActualDate(int iCalYear, int iMonth)
    {
        SimpleDateFormat oFormatter = new SimpleDateFormat();
        oFormatter.applyPattern("MMMM yyyy");
        return oFormatter.format(getCal(iCalYear, iMonth).getTime());
    }

    /**
     * 
     * Gibt das aktuelle Datum speziell zurück
     * @param oCal
     * @return formatiertes Datum
     *
     */
    @Deprecated
    public static String getActualDate(Calendar oCal)
    {
        SimpleDateFormat oFormatter = new SimpleDateFormat();
        oFormatter.applyPattern("MMMM yyyy");
        return oFormatter.format(oCal.getTime());
    }

    /**
     * 
     * Gibt den vorhergehenden Monat zurück
     * 
     * @param oCal
     * @return    BESCHREIBUNG_EINFUEGEN
     *
     */
    @Deprecated
    public static String getMonthBefore(Calendar oCal)
    {
        return _sMonth[oCal.get(Calendar.MONTH)];
    }

    /**
     * 
     * Gibt das Jahr als int zurück
     * 
     * @return int Jahr
     *
     */
    @Deprecated
    public static int getCurrentYear()
    {
        Date oDate = new Date();
        SimpleDateFormat oFormatter = new SimpleDateFormat();
        oFormatter.applyPattern("yyyy");
        return Integer.parseInt(oFormatter.format(oDate));
    }

    /**
     * 
     * Gibt alle Monate eines Jahres zurück
     * 
     * @return    BESCHREIBUNG_EINFUEGEN
     *
     */
    @Deprecated
    public static String[] getAllMonth()
    {
        return _sMonth;
    }
        
    /**
     * 
     * Optimiert auf ShiftCalendar (Drag&Drop)
     * 
     * Donnerstag = 1
     * Freitag    = 2
     * Samstag    = 3
     * Sontag     = 4 
     * Montag     = 5
     * Dienstag   = 6
     * Mittwoch   = 7
    **/
    public static boolean isWeekend(GregorianCalendar oDate)
    {
        if (oDate.get(Calendar.DAY_OF_WEEK) == 1 || oDate.get(Calendar.DAY_OF_WEEK) == 7)
            return true;
        else
            return false;
    }
    
    /**
     * Gibt das Datum im Format yyyyMMdd aus
     * 
     * @param oCal
     * @return Datum im Format yyyyMMdd
     */
    @Deprecated /* use getFormatedDate(GregorianCalendar oCal, String, sFormat) */
    public static String getFormatedDate(GregorianCalendar oCal)
    {
        SimpleDateFormat oFormatter = new SimpleDateFormat();
        oFormatter.applyPattern("yyyyMMdd");
        
        return oFormatter.format(oCal.getTime());
    }
    
    /*********** alte Methoden für Drag&Drop Calendar - Ende ************************************/

    
    /**
     * Gibt das Datum im Format sFormat aus
     * 
     * @param oCal
     * @return Datum im Format sFormat
     */
    public static String getFormatedDate(GregorianCalendar oCal, String sFormat)
    {        
        SimpleDateFormat oFormatter = new SimpleDateFormat(sFormat, Locale.CANADA_FRENCH);
        //oFormatter.applyPattern(sFormat);
        
        return oFormatter.format(oCal.getTime());
    }
    
    /**
     * Gibt das Datum im Format sFormat aus
     * 
     * @param oDate 
     * @return Datum im Format sFormat
     */
    public static String getFormatedDate(Date oDate, String sFormat)
    {
        GregorianCalendar oCal = getCalendarFromDate(oDate);
        SimpleDateFormat oFormatter = new SimpleDateFormat();
        oFormatter.applyPattern(sFormat);
        
        return oFormatter.format(oCal.getTime());
    }
    
    /**
     *
     * Gibt die Tage eine Monats zurück
     * 
     * @param oDate
     * @return Tage des Monats
     *
     */
    public static int getMaxDaysOfMonth(Date oDate)
    {   
        return getCalendarFromDate(oDate).getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 
     * Gibt einen GregorianCalendar zurück
     * @param oDate
     * @return GregorianCalendar from oDate
     *
     */
    public static GregorianCalendar getCalendarFromDate(Date oDate)
    {
        GregorianCalendar oCal = (GregorianCalendar)GregorianCalendar.getInstance();
        oCal.setTime(oDate);
        
        return oCal;
    }
    
    /**
     * 
     * Gibt den Wochentag als String zurück,
     * annahme: Montag = 1 und Sonntag = 7
     * 
     * @param iDay
     * @return    BESCHREIBUNG_EINFUEGEN
     *
     */
    public static String getDayByPK(int iDay)
    {
        if (iDay < 1 || iDay > 7)
            return "WrongParameter";
        else
            return _sWeekDays[iDay-1];
    }
    
    /**
     * Gibt alle Wochentage zurück
     * 
     * @return Wochentag als StringArray
     */
    public static String[] getWeekDays()
    {
        return _sWeekDays;
    }
    
    public static boolean before(Calendar oCalendar1, Calendar oCalendar2)
    {
        boolean bTemp = false;
        if(oCalendar1.get(Calendar.YEAR) < oCalendar2.get(Calendar.YEAR))
        {
            bTemp = true;
        }
        else if(oCalendar1.get(Calendar.YEAR) == oCalendar2.get(Calendar.YEAR))
        {
            if(oCalendar1.get(Calendar.MONTH) < oCalendar2.get(Calendar.MONTH))
            {
                bTemp = true;
            }
            else if(oCalendar1.get(Calendar.MONTH) == oCalendar2.get(Calendar.MONTH))
            {
                if(oCalendar1.get(Calendar.DAY_OF_MONTH) < oCalendar2.get(Calendar.DAY_OF_MONTH))
                {
                    bTemp = true;
                }
                else
                {
                    bTemp = false;
                }
            }
            else
            {
                bTemp = false;
            }
        }
        else
        {
            bTemp = false;
        }
        return bTemp;
    }
    
}
