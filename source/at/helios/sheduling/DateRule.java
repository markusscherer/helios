package at.helios.sheduling;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Bildet Wunschdienst/Verhinderung ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class DateRule extends Rule
{

    private GregorianCalendar   _oDate;
    private Shifts              _oShift;
    private final static String PROLOG_DATERULE = "person_%M1%likesdate(p%P1%,d%D1%%D2%,%S1%).";

    /**
     * Konstruktor der Felder benutzt
     * @param iRuleId
     * @param bLikes
     * @param oDate
     **/
    public DateRule(int iRuleId, boolean bLikes, GregorianCalendar oDate, Shifts oShift)
    {
        super(iRuleId, bLikes);
        _oDate = oDate;
        _oShift = oShift;
    }

    /**
     * Getter für _oShift.
     * @return _oShift
     **/
    public Shifts getShift()
    {
        return _oShift;
    }

    /**
     * Setter für _oShift
     * @param oShift
     **/
    public void setShift(Shifts oShift)
    {
        _oShift = oShift;
    }

    /* (non-Javadoc)
     * @see at.helios.sheduling.Prologable#getPrologStatement()
     */
    public String getPrologStatement()
    {
        String sStatement = "";
        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");

        if (isLikes())
        {
            sStatement = PROLOG_DATERULE.replaceAll("%M1%", "");
        }
        else
        {
            sStatement = PROLOG_DATERULE.replaceAll("%M1%", "not");
        }

        sStatement = sStatement.replaceAll("%P1%", "" + getRuleOwner().getPersonId());
        sStatement = sStatement.replaceAll("%D1%", oFormat.format(_oDate.getTime()));

        if (_oShift.equals(Shifts.dayshift))
        {
            sStatement = sStatement.replaceAll("%D2%", "d");
        }
        else if (_oShift.equals(Shifts.nightshift))
        {
            sStatement = sStatement.replaceAll("%D2%", "n");
        }

        sStatement = sStatement.replaceAll("%S1%", _oShift.toString());

        return sStatement;
    }

    /**
     * Getter für _oDate.
     * @return _oDate
     **/
    public GregorianCalendar getDate()
    {
        return _oDate;
    }

    /**
     * Setter für _oDate
     * @param oDate
     **/
    public void setDate(GregorianCalendar oDate)
    {
        _oDate = oDate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + " Date: " + _oDate.get(Calendar.DAY_OF_MONTH) + "."
            + (_oDate.get(Calendar.MONTH) + 1) + "." + _oDate.get(Calendar.YEAR);
    }

    public boolean equals(Object oRule)
    {
        if (oRule instanceof DateRule)
        {
            if (((DateRule) oRule).getRuleOwner().equals(this.getRuleOwner())
                && ((DateRule) oRule).getDate().equals(_oDate)
                && ((DateRule) oRule).getShift().equals(_oShift))
            {
                return true;
            }
        }

        return false;
    }

}
