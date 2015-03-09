package at.helios.dataview.jasper;

import java.util.GregorianCalendar;
import at.helios.calendar.helper.DateHelper;

/**
 * 
 * Datenklasse für ein Eintrag im Schichtplan
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   04.02.2009  Neuerstellung
 * </PRE>
 *
 */
public class ShiftplanBean
{
    private GregorianCalendar _oDate;
    private int               _iDriver;
    private int               _iMedi;
    private int               _iAdditional;

    /**
     * 
     * Setzt die Werte der Klasse
     * 
     * @param oDate        - Arbeitsdatum
     * @param iDriver      - Fahrer
     * @param iMedi        - Sanitäter
     * @param iAdditional  - Probehelfer
     *
     */
    public ShiftplanBean(GregorianCalendar oDate, int iDriver, int iMedi, int iAdditional)
    {
        setDate(oDate);
        setDriver(iDriver);
        setMedi(iMedi);
        setAdditional(iAdditional);
    }

    /**
     * Getter für _oDate.
     * @return _oDate
     **/
    public GregorianCalendar getDate()
    {
        return _oDate;
    }

    public int getDay()
    {
        return _oDate.get(GregorianCalendar.DATE);
    }

    /**
     * Setter für _oDate
     * @param date
     **/
    public void setDate(GregorianCalendar oDate)
    {
        _oDate = (GregorianCalendar) oDate.clone();
    }

    /**
     * Getter für _iMedi.
     * @return _iMedi
     **/
    public int getMedi()
    {
        return _iMedi;
    }

    /**
     * Setter für _iMedi
     * @param medi
     **/
    public void setMedi(int iMedi)
    {
        _iMedi = iMedi;
    }

    /**
     * Getter für _iAdditional.
     * @return _iAdditional
     **/
    public int getDriver()
    {
        return _iDriver;
    }

    /**
     * Setter für _iAdditional
     * @param additional
     **/
    public void setDriver(int iDriver)
    {
        _iDriver = iDriver;
    }

    /**
     * Getter für _iAdditional.
     * @return _iAdditional
     **/
    public int getAdditional()
    {
        return _iAdditional;
    }

    /**
     * Setter für _iAdditional
     * @param additional
     **/
    public void setAdditional(int iAdditional)
    {
        _iAdditional = iAdditional;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return DateHelper.getFormatedDate(_oDate, "yyyyMMdd") + " " + _iMedi + " " + _iDriver + " "
            + _iAdditional;
    }
}