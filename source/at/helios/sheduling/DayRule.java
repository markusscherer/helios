package at.helios.sheduling;

/**
 * Bildet Wunschwochentag/generelle Verhinderung an Wochentag ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class DayRule extends Rule
{

    private int                 _iDayId;
    private Shifts              _oShift;
    private static final String PROLOG_DAYRULE = "person_%M1%likesday(%P1%,%D1%,%S1%).";

    /**
     * Konstruktor mit Feldern
     * @param iRuleId
     * @param bLikes
     * @param iDayId
     **/
    public DayRule(int iRuleId, boolean bLikes, int iDayId, Shifts oShift)
    {
        super(iRuleId, bLikes);
        _iDayId = iDayId;
        _oShift = oShift;
    }

    /* (non-Javadoc)
     * @see at.helios.sheduling.Prologable#getPrologStatement()
     */
    public String getPrologStatement()
    {
        String sStatement = "";

        if (isLikes())
        {
            sStatement = PROLOG_DAYRULE.replaceAll("%M1%", "");
        }
        else
        {
            sStatement = PROLOG_DAYRULE.replaceAll("%M1%", "not");
        }

        sStatement = sStatement.replaceAll("%P1%", "p" + getRuleOwner().getPersonId());
        sStatement = sStatement.replaceAll("%D1%", Days.values()[_iDayId - 1].toString());
        sStatement = sStatement.replaceAll("%S1%", _oShift.toString());

        return sStatement;
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

    /**
     * Getter für _iDayId.
     * @return _iDayId
     **/
    public int getDayId()
    {
        return _iDayId;
    }

    /**
     * Setter für _iDayId
     * @param iDayId
     **/
    public void setDayId(int iDayId)
    {
        _iDayId = iDayId;
    }

    /* (non-Javadoc)
     * @see at.helios.sheduling.Rule#toString()
     */
    @Override
    public String toString()
    {
        return super.toString() + " Day: " + _iDayId;
    }

    public boolean equals(Object oRule)
    {
        if (oRule instanceof DayRule)
        {
            if (((DayRule) oRule).getRuleOwner().equals(this.getRuleOwner())
                && ((DayRule) oRule).getDayId() == _iDayId && ((DayRule) oRule).getShift().equals(_oShift))
            {
                return true;
            }
        }
        return false;
    }

}
