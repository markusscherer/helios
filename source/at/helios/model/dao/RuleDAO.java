package at.helios.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

import at.helios.common.DBHelper;
import at.helios.model.Person;
import at.helios.sheduling.DateRule;
import at.helios.sheduling.DayRule;
import at.helios.sheduling.FriendRule;
import at.helios.sheduling.Rule;
import at.helios.sheduling.Shifts;

/**
 * DAO für Rule
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class RuleDAO
{
    private static final String SQL_INSERT_RULE        = "INSERT INTO tRule(nRuleOwnerId, bLikes, nDayId, nFriendId, dRelevant, cShifttype) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_RULE_OWNER = "SELECT nRuleId, nRuleOwnerId, bLikes, nDayId, nFriendId, dRelevant, cShifttype FROM tRule WHERE nRuleOwnerId = ?";
    private static final String SQL_DELETE_RULE        = "DELETE FROM tRule WHERE nRuleId = ?";

    /**
     * Fügt Regel in Datenbank ein
     * @param oRule    einzufügende Regel
     **/
    public void insert(Rule oRule)
    {
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_RULE);
            oStatement.setInt(1, oRule.getRuleOwner().getPersonId());
            oStatement.setBoolean(2, oRule.isLikes());

            if (oRule instanceof DayRule)
            {
                oStatement.setInt(3, ((DayRule) oRule).getDayId());
                oStatement.setString(6, ((DayRule) oRule).getShift().toString());
            }
            else
            {
                oStatement.setNull(3, java.sql.Types.INTEGER);
            }

            if (oRule instanceof FriendRule)
            {
                oStatement.setInt(4, ((FriendRule) oRule).getFriend().getPersonId());
                oStatement.setNull(6, java.sql.Types.VARCHAR);
            }
            else
            {
                oStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (oRule instanceof DateRule)
            {
                oStatement.setDate(5, new java.sql.Date(((DateRule) oRule).getDate().getTime().getTime()));
                oStatement.setString(6, ((DateRule) oRule).getShift().toString());
            }
            else
            {
                oStatement.setNull(5, java.sql.Types.DATE);
            }

            oStatement.execute();
            
            oStatement.close();
            oConnection.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        for (Rule oRuleTmp : findByRuleOwner(oRule.getRuleOwner()))
        {
            if (oRuleTmp.equals(oRule))
            {
                oRule.setRuleId(oRuleTmp.getRuleId());
                break;
            }
        }

    }

    /**
     * Findet Regel anhand von Primary Key
     * @param oRuleOwner
     * @return    Regel mit diesem Primary Key
     **/
    public Collection<Rule> findByRuleOwner(Person oRuleOwner)
    {
        Collection<Rule> coRules = new Vector<Rule>();
        Connection oConnection = DBHelper.getConnection();

        Rule oRule = null;

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_FIND_BY_RULE_OWNER);
            oStatement.setInt(1, oRuleOwner.getPersonId());
            ResultSet oResultSet = oStatement.executeQuery();

            while (oResultSet.next())
            {
                if (oResultSet.getInt("nFriendId") == 0 && oResultSet.getDate("dRelevant") == null)
                {
                    oRule = new DayRule(oResultSet.getInt("nRuleId"), oResultSet.getBoolean("bLikes"),
                        oResultSet.getInt("nDayId"), Shifts.valueOf(oResultSet.getString("cShifttype")));
                    oRule.setRuleOwner(oRuleOwner);
                }
                else if (oResultSet.getInt("nDayId") == 0 && oResultSet.getDate("dRelevant") == null)
                {
                    oRule = new FriendRule(oResultSet.getInt("nRuleId"), oResultSet.getBoolean("bLikes"),
                        Person.getPersonById(oResultSet.getInt("nFriendId")));
                    oRule.setRuleOwner(oRuleOwner);
                }
                else if (oResultSet.getInt("nFriendId") == 0 && oResultSet.getInt("nDayId") == 0)
                {
                    GregorianCalendar oDate = new GregorianCalendar();
                    oDate.setTime(oResultSet.getDate("dRelevant"));
                    oRule = new DateRule(oResultSet.getInt("nRuleId"), oResultSet.getBoolean("bLikes"),
                        oDate, Shifts.valueOf(oResultSet.getString("cShifttype")));
                    oRule.setRuleOwner(oRuleOwner);
                }

                coRules.add(oRule);
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return coRules;
    }

    /**
     * Löscht Regel aus Datenbank
     * @param oRule    Zu löschende Regel
     **/
    public void delete(Rule oRule)
    {
        if (oRule.getRuleId() == 0)
        {
            return;
        }

        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_DELETE_RULE);

            oStatement.setInt(1, oRule.getRuleId());

            oStatement.execute();
            
            oStatement.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Löscht Regel aus Datenbank
     * @param iRule    Primary Key der zu löschenden Regel
     **/
    public void delete(int iRule)
    {
        if (iRule == 0)
        {
            return;
        }

        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_DELETE_RULE);

            oStatement.setInt(1, iRule);

            oStatement.execute();
            
            oStatement.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
