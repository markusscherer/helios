package at.helios.sheduling;

import at.helios.model.Person;

/**
 * Bildet Person-Person-Beziehungen ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class FriendRule extends Rule {
	
	private Person _oFriend;
	private static final String PROLOG_FRIENDRULE = "person_%M1%friend(%P1%, %P2%).";	
	
	/**
     * Konstruktor mit Feldern
     * @param iRuleId
     * @param bLikes
     * @param oFriend
     **/
    public FriendRule(int iRuleId, boolean bLikes, Person oFriend)
    {
        super(iRuleId, bLikes);
        _oFriend = oFriend;
    }

    /* (non-Javadoc)
	 * @see at.helios.sheduling.Prologable#getPrologStatement()
	 */
	public String getPrologStatement()
	{
	    String sStatement = "";
	    
	    if(isLikes())
	    {
	        sStatement = PROLOG_FRIENDRULE.replaceAll("%M1%", "");
	    }
	    else
	    {
	        sStatement = PROLOG_FRIENDRULE.replaceAll("%M1%", "not");
	    }
	    
	    sStatement = sStatement.replaceAll("%P1%", "p"+getRuleOwner().getPersonId());
	    sStatement = sStatement.replaceAll("%P2%", "p"+getFriend().getPersonId());
	    
	    return sStatement;
	}
	
    /**
     * Getter für _oFriend.
     * @return _oFriend
     **/
    public Person getFriend()
    {
        return _oFriend;
    }

    /**
     * Setter für _oFriend
     * @param oFriend
     **/
    public void setFriend(Person oFriend)
    {
        _oFriend = oFriend;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return super.toString() + " Person: " + _oFriend.getPersonId();
	}
	
	/* (non-Javadoc)
	 * @see at.helios.sheduling.Rule#equals(java.lang.Object)
	 */
	public boolean equals(Object oRule)
	{
	    if (oRule instanceof FriendRule)
	    {
    	    if (((FriendRule)oRule).getRuleOwner().equals(this.getRuleOwner())
                && ((FriendRule)oRule).getFriend().equals(this._oFriend))
                return true;
	    }
	    
	    return false;
	    
	}

}
