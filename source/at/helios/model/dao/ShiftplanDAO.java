package at.helios.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import at.helios.common.DBHelper;
import at.helios.model.Person;
import at.helios.model.Shift;
import at.helios.model.Shiftplan;
import at.helios.model.Team;
import at.helios.sheduling.Shifts;
import at.helios.sheduling.Teams;

/**
 * DAO für Shiftplans
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   02.02.2009 Neuerstellung
 * </PRE>
 **/
public class ShiftplanDAO
{
    private static final String SQL_INSERT_SHIFTPLAN                             = "INSERT INTO tShiftplan(nDepartmentId, cName) VALUES  (?,?);";
    private static final String SQL_SELECT_PK                                    = "SELECT nShiftplanId FROM tShiftplan WHERE cName = ?";
    private static final String SQL_SELECT_SHIFTTREEID_AND_TEAMCOUNT_BY_TEAMTYPE = "SELECT nShiftTreeId, nTeamCount FROM tShiftplan a, tShiftTree b WHERE a.nShiftplanId = ? AND a.nShiftplanId = b.nShiftplanId AND b.cTeamType = ?";
    private static final String SQL_PERIOD_BORDERS_BY_SHIFTPLAN_ID               = "SELECT DISTINCT dPeriodStart, dPeriodEnd FROM tShiftTree WHERE nShiftplanId = ?";
    private static final String SQL_SELECT_DATES                                 = "SELECT DISTINCT dShift, cShifttype FROM tShiftNode a WHERE nShiftTreeId = ? ORDER BY dShift ASC, cShifttype DESC";
    private static final String SQL_SELECT_TEAM                                  = "SELECT * FROM tShiftNode b WHERE b.nShiftTreeId = ? AND b.dShift = ? AND b.cShifttype = ? ORDER BY b.nTeamNumber";
    private static final String SQL_UPDATE_SHIFTS                                = "UPDATE tShiftNode SET nDriverId = ?, nMediId = ?, nAdditionalId = ? WHERE nShiftId = ?";
    private static final String SQL_UPDATE_FILE_PATH                             = "UPDATE tShiftplan SET cFilepath = ? WHERE nShiftplanId = ?";
    private static final String SQL_UPDATE_CONFIRMATION                          = "UPDATE tShiftplan SET bConfirmed = true WHERE nShiftplanId = ?";
    private static final String SQL_SELECT_FILE_PATH                             = "SELECT cFilepath FROM tShiftplan WHERE nShiftplanId = ?";

    /**
     * Fügt mehrere ShiftTree in einen Shiftplan ein, alle allgemeinen Daten (Department, etc.) werden aus ersten ShiftTree bezogen
     * @param coShiftTrees    BESCHREIBUNG_EINFUEGEN
     **/
//    public void insert(List<ShiftTree> coShiftTrees)
//    {
//        Connection oConnection = DBHelper.getConnection();
//        ResultSet oResultSet = null;
//        try
//        {
//            SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
//            String sTemp = "Schichtplan, " + coShiftTrees.get(0).getDepartment().getName() + ", ";
//            sTemp += oFormat.format(coShiftTrees.get(0).getPeriodStart().getTime()) + " - ";
//            sTemp += oFormat.format(coShiftTrees.get(0).getPeriodEnd().getTime());
//
//            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_SHIFTPLAN);
//
//            oStatement.setInt(1, coShiftTrees.get(0).getDepartment().getDepartmentId());
//            oStatement.setString(2, sTemp);
//
//            oStatement.execute();
//
//            oStatement = oConnection.prepareStatement(SQL_SELECT_PK);
//            oStatement.setString(1, sTemp);
//
//            oResultSet = oStatement.executeQuery();
//
//            if (oResultSet.next())
//            {
//                int iShiftplanId = oResultSet.getInt("nShiftplanId");
//                ShiftTreeDAO oShiftTreeDAO = new ShiftTreeDAO();
//
//                for (ShiftTree oShiftTree : coShiftTrees)
//                {
//                    oShiftTreeDAO.insert(oShiftTree, iShiftplanId);
//                }
//            }
//            
//            oStatement.close();
//            oResultSet.close();
//            oConnection.close();
//
//        }
//        catch (SQLException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     * Findet einen Shiftplan anhand seiner Id
     * @param iShiftplanId
     * @return    Shiftplan
     **/
    public Shiftplan findByShiftplanId(int iShiftplanId)
    {
        Shiftplan oShiftplan = new Shiftplan();
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_PERIOD_BORDERS_BY_SHIFTPLAN_ID);
            oStatement.setInt(1, iShiftplanId);

            ResultSet oResultSet = oStatement.executeQuery();

            if (oResultSet.next())
            {
                GregorianCalendar oCalendar = new GregorianCalendar();
                oCalendar.setTimeInMillis(oResultSet.getDate("dPeriodStart").getTime());
                oShiftplan.setPeriodStart(oCalendar);

                oCalendar = new GregorianCalendar();
                oCalendar.setTimeInMillis(oResultSet.getDate("dPeriodEnd").getTime());

                oShiftplan.setPeriodEnd(oCalendar);
            }

            oShiftplan.setNEFTeams(getShiftsByTraining(iShiftplanId, Teams.nef));
            oShiftplan.setRTWTeams(getShiftsByTraining(iShiftplanId, Teams.rtw));
            oShiftplan.setSTBTeams(getShiftsByTraining(iShiftplanId, Teams.standby));
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return oShiftplan;
    }

    /**
     * Findet alle Schichten eines Schichtplans einer gewissen Teamart
     * @param iShiftplanId
     * @param oTeamType
     * @return    Liste mit Schichten
     **/
    private List<Shift> getShiftsByTraining(int iShiftplanId, Teams oTeamType)
    {
        List<Shift> coShifts = new ArrayList<Shift>();

        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection
                .prepareStatement(SQL_SELECT_SHIFTTREEID_AND_TEAMCOUNT_BY_TEAMTYPE);
            oStatement.setInt(1, iShiftplanId);
            oStatement.setString(2, oTeamType.toString());

            ResultSet oResultSet = oStatement.executeQuery();

            oResultSet.next();
            int iShiftTreeId = oResultSet.getInt("nShiftTreeId");
            int iTeamCount = oResultSet.getInt("nTeamCount");

            oStatement = oConnection.prepareStatement(SQL_SELECT_DATES);
            oStatement.setInt(1, iShiftTreeId);
            oResultSet = oStatement.executeQuery();

            while (oResultSet.next())
            {
                Date oDate = oResultSet.getDate("dShift");
                Shifts oShiftType;

                if (oResultSet.getString("cShiftType").equals("nightshift"))
                {
                    oShiftType = Shifts.nightshift;
                }
                else
                {
                    oShiftType = Shifts.dayshift;
                }

                PreparedStatement oShiftStatement = oConnection.prepareStatement(SQL_SELECT_TEAM);
                oShiftStatement.setInt(1, iShiftTreeId);

                oShiftStatement.setDate(2, oDate);
                oShiftStatement.setString(3, oShiftType.toString());

                GregorianCalendar oCalendar = new GregorianCalendar();
                oCalendar.setTimeInMillis(oDate.getTime());

                Shift oShift = new Shift(oCalendar,oShiftType,iTeamCount);
                
                ResultSet oTeamResultSet = oShiftStatement.executeQuery();

                while (oTeamResultSet.next())
                {
                    Team oTeam = new Team();

                    oTeam.setShiftId(oTeamResultSet.getInt("nShiftId"));
                    oTeam.setDriver(Person.getPersonById(oTeamResultSet.getInt("nDriverId")));
                    oTeam.setMedi(Person.getPersonById(oTeamResultSet.getInt("nMediId")));

                    if (oTeamResultSet.getInt("nAdditionalId") != 0)
                    {
                        //FIXME: da war setDriver ?!??
                        oTeam.setAdditional(Person.getPersonById(oTeamResultSet.getInt("nAdditionalId")));
                    }
                    
                    int iTeamNumber = oTeamResultSet.getInt("nTeamNumber");

                    oShift.setTeam(iTeamNumber, oTeam);
                }

                coShifts.add(oShift);
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return coShifts;
    }

    /**
     * Bestätigt Schichtplan
     * @param iShiftplanId    Id des zu bestätigenden Schichtplans
     **/
    public void confirm(int iShiftplanId)
    {
        Connection oConnection = DBHelper.getConnection();
        PreparedStatement oStatement;

        try
        {
            oStatement = oConnection.prepareStatement(SQL_UPDATE_CONFIRMATION);
            oStatement.setInt(1, iShiftplanId);

            oStatement.execute();
            
            oStatement.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Aktualisiert Shiftplan
     * @param oShiftplan    zu aktualisiernder Schichtplan
     **/
    public void update(Shiftplan oShiftplan)
    {
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = null;

            List<Shift> coAll = new ArrayList<Shift>();

            coAll.addAll(oShiftplan.getNEFShifts());
            coAll.addAll(oShiftplan.getRTWShifts());
            coAll.addAll(oShiftplan.getSTBShifts());

            for (Shift oShift : coAll)
            {
                for (Team oTeam : oShift.getTeams())
                {
                    oStatement = oConnection.prepareStatement(SQL_UPDATE_SHIFTS);
                    oStatement.setInt(1, oTeam.getDriver().getPersonId());
                    oStatement.setInt(2, oTeam.getMedi().getPersonId());

                    if (oTeam.getAdditional() == null)
                    {
                        oStatement.setNull(3, java.sql.Types.INTEGER);
                    }
                    else
                    {
                        oStatement.setInt(3, oTeam.getAdditional().getPersonId());
                    }

                    oStatement.setInt(4, oTeam.getShiftId());

                    oStatement.execute();
                }
            }
            
            oStatement.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Aktualisiert Pfad zu den Plan-Dateien
     * @param iShiftplanId
     * @param coNames
     * @return hat das Update geklappt?
     **/
    public boolean updateFilepath(int iShiftplanId, Collection<String> coNames)
    {
        Connection oConnection = DBHelper.getConnection();
        PreparedStatement oStatement;
        String sFilePath = "";

        for (String sName : coNames)
        {
            if (!sFilePath.equalsIgnoreCase(""))
                sFilePath = sFilePath + ";" + sName;
            else
                sFilePath = sFilePath + sName;
        }

        try
        {
            oStatement = oConnection.prepareStatement(SQL_UPDATE_FILE_PATH);
            oStatement.setString(1, sFilePath);
            oStatement.setInt(2, iShiftplanId);

            oStatement.execute();
            oStatement.close();
            oConnection.close();
            
            return true;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Findet Daeipfade zu bei angegebenen Shiftplan
     * @param iShiftplanId
     * @return    Array mit Pfaden
     **/
    public String[] getFilepathByShiftplanId(int iShiftplanId)
    {
        Connection oConnection = DBHelper.getConnection();

        PreparedStatement oStatement;
        try
        {
            oStatement = oConnection.prepareStatement(SQL_SELECT_FILE_PATH);
            oStatement.setInt(1, iShiftplanId);

            ResultSet oResultSet = oStatement.executeQuery();
            oResultSet.next();

            String sFilepath = oResultSet.getString("cFilepath");
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();

            if (sFilepath != null) return sFilepath.split(";");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
