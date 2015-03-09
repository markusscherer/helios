package at.helios.sheduling;

import at.helios.model.Person;

/**
 * Abstrakte Basisklasse für Regeln
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public abstract class Rule implements Prologable
{
    private int     _iRuleId;
    private Person  _oRuleOwner;
    private boolean _bLikes;

    /**
     * Konstruktor mit Feldern
     * @param iRuleId
     * @param bLikes
     **/
    Rule(int iRuleId, boolean bLikes)
    {
        _iRuleId = iRuleId;
        _bLikes = bLikes;
    }

    /**
     * Getter für _iRuleId.
     * @return _iRuleId
     **/
    public int getRuleId()
    {
        return _iRuleId;
    }

    /**
     * Setter für _iRuleId
     * @param iRuleId
     **/
    public void setRuleId(int iRuleId)
    {
        _iRuleId = iRuleId;
    }

    /**
     * Getter für oRuleOwner.
     * @return oRuleOwner
     **/
    public Person getRuleOwner()
    {
        return _oRuleOwner;
    }

    /**
     * Setter für oRuleOwner
     * @param oRuleOwner
     **/
    public void setRuleOwner(Person oRuleOwner)
    {
        _oRuleOwner = oRuleOwner;
    }

    /**
     * Getter für _bLikes.
     * @return _bLikes
     **/
    public boolean isLikes()
    {
        return _bLikes;
    }

    /**
     * Setter für _bLikes
     * @param bLikes
     **/
    public void setLikes(boolean bLikes)
    {
        _bLikes = bLikes;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String sTemp = "[Rule] ";

        if (_bLikes)
        {
            sTemp += "likes ";
        }
        else
        {
            sTemp += "likes not ";
        }

        return sTemp;
    }

    public boolean equals(Object oRule)
    {
        if (oRule instanceof FriendRule)
        {
            return ((FriendRule) this).equals(oRule);
        }

        else if (oRule instanceof DateRule)
        {
            return ((DateRule) this).equals(oRule);
        }

        else if (oRule instanceof DayRule)
        {
            return ((DayRule) this).equals(oRule);
        }
        else if (oRule instanceof Rule)
        {
            if (_oRuleOwner.equals(((Rule)oRule).getRuleOwner()) && _bLikes == ((Rule)oRule).isLikes())
                return true;
        }
        
        return false;
    }

}