package at.helios.dataview.jasper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Queue;

import org.zkoss.zk.ui.Sessions;

import at.helios.calendar.helper.DateHelper;
import at.helios.common.DBHelper;
import at.helios.model.Department;
import at.helios.model.Person;

/**
 * 
 * Diese Klasse liest die Daten aus der
 * Datenbank aus und füllt Löcher mit leeren
 * Einträgen.
 * 
 * @author
 * <PRE>
 *         ID     date         description
 *         mab    04.02.2009   Neuerstellung
 * </PRE>
 *
 */
public class JasperShiftPlanDAO
{

    /**
     * 
     * Füllt den Schichtplan
     * 
     * @param iPlanId
     * @throws SQLException
     *
     */
    public JasperShiftPlanDAO(int iPlanId) throws SQLException
    {
        fillShiftPlan(iPlanId);
    }

    private static Queue<Collection<ShiftplanBean>> _oFilledPlan;

    private static final String                     SQL_STATEMENT_M         = "select a.* from tShiftNode as a "
                                                                                + "inner join tShiftTree as b "
                                                                                + "on a.nShiftTreeId=b.nShiftTreeId "
                                                                                + "Where b.cTeamType=? and "
                                                                                + "b.nShiftplanId=? and "
                                                                                + "a.cShifttype=? and "
                                                                                + "a.nTeamNumber=? and "
                                                                                + "a.dShift>=? and "
                                                                                + "a.dShift<=?";

    private static final String                     SQL_STATEMENT_S         = "SELECT dPeriodStart,dPeriodEnd FROM tShiftplan as a "
                                                                                + "inner join tShiftTree as b "
                                                                                + "on a.nShiftplanId=b.nShiftplanId "
                                                                                + "where a.nShiftplanId=?";

    private static final String                     SQL_STATEMENT_PLANS     = "SELECT SP.cName, SP.nShiftplanId, SP.cFilePath FROM tShiftplan as SP "
                                                                                + "inner join tDepartment as DP "
                                                                                + "on SP.nDepartmentId=DP.nDepartmentId "
                                                                                + "where DP.nDepartmentId=? order by cName DESC";

    private static final String                     SQL_STATEMENT_PLAN_NAME = "SELECT cName FROM tShiftplan where nShiftplanId=?";

    /**
     * 
     * Gibt eine Collection mit allen gefüllten Teilplänen zurück
     * 
     * @return oFilledPlan
     * @throws SQLException
     *
     */
    public void fillShiftPlan(int iPlanId) throws SQLException
    {
        _oFilledPlan = new LinkedList<Collection<ShiftplanBean>>();

        _oFilledPlan.add(fillEmptyPos("rtw", 0, iPlanId, "dayshift", 0));
        _oFilledPlan.add(fillEmptyPos("rtw", 1, iPlanId, "dayshift", 0));
        _oFilledPlan.add(fillEmptyPos("rtw", 2, iPlanId, "dayshift", 0));
        _oFilledPlan.add(fillEmptyPos("rtw", 0, iPlanId, "nightshift", 0));
        _oFilledPlan.add(fillEmptyPos("rtw", 1, iPlanId, "nightshift", 0));

        _oFilledPlan.add(fillEmptyPos("rtw", 0, iPlanId, "dayshift", 1));
        _oFilledPlan.add(fillEmptyPos("rtw", 1, iPlanId, "dayshift", 1));
        _oFilledPlan.add(fillEmptyPos("rtw", 2, iPlanId, "dayshift", 1));
        _oFilledPlan.add(fillEmptyPos("rtw", 0, iPlanId, "nightshift", 1));
        _oFilledPlan.add(fillEmptyPos("rtw", 1, iPlanId, "nightshift", 1));

        _oFilledPlan.add(fillEmptyPos("nef", 0, iPlanId, "dayshift", 0));
        _oFilledPlan.add(fillEmptyPos("nef", 0, iPlanId, "nightshift", 0));
        _oFilledPlan.add(fillEmptyPos("nef", 0, iPlanId, "dayshift", 1));
        _oFilledPlan.add(fillEmptyPos("nef", 0, iPlanId, "nightshift", 1));
    }

    /**
     * 
     * Gibt eine Collection des gefüllten(ohne "Löcher") Teilplans zurück
     * 
     * @param sQual
     * @param iTeamNumber
     * @param iPlanId
     * @param oStatement
     * @param sShift
     * @return coPlan
     * @throws SQLException
     *
     */
    public static Collection<ShiftplanBean> fillEmptyPos(String sQual, int iTeamNumber, int iPlanId, String sShift, int iPeriod) throws SQLException
    {
        Date[] oaPeriod = getDaysOfMonth(iPlanId);
        String[] saPeriod = new String[2];

        GregorianCalendar oStart;
        GregorianCalendar oEnd = DateHelper.getCalendarFromDate(oaPeriod[1]);
        int iMonth = DateHelper.getMaxDaysOfMonth(oaPeriod[iPeriod]);

        //Perioden Start neu setzten
        if (iPeriod == 0)
        {
            oStart = DateHelper.getCalendarFromDate(oaPeriod[0]);
            oEnd.set(Calendar.DATE, iMonth);
            saPeriod[0] = DateHelper.getFormatedDate(oStart, "yyyy-MM-dd");
            saPeriod[1] = DateHelper.getFormatedDate(oEnd, "yyyy-MM-dd");
        }
        else
        {
            saPeriod[1] = DateHelper.getFormatedDate(oEnd, "yyyy-MM-dd");
            oEnd.set(Calendar.DATE, 1);
            saPeriod[0] = DateHelper.getFormatedDate(oEnd, "yyyy-MM-dd");
            oStart = (GregorianCalendar) oEnd.clone();
        }

        boolean bHasNext = false;

        Connection oCon = DBHelper.getConnection();
        //ResultSet oRs = getStatement(oCon, sShift, sQual, iPlanId, iTeamNumber, saPeriod);
        
        PreparedStatement oST = oCon.prepareStatement(SQL_STATEMENT_M);
        oST.setString(1, sQual);
        oST.setInt(2, iPlanId);
        oST.setString(3, sShift);
        oST.setInt(4, iTeamNumber);
        oST.setString(5, saPeriod[0]);
        oST.setString(6, saPeriod[1]);
        
        ResultSet oRs = oST.executeQuery();
        
        
        Collection<ShiftplanBean> coPlan = new ArrayList<ShiftplanBean>();

        //Pointer auf das erste Element setzten
        bHasNext = oRs.next();
        for (int i = 1; i <= iMonth; i++)
        {
            oStart.set(Calendar.DATE, i);

            if (bHasNext && DateHelper.getCalendarFromDate(oRs.getDate("dShift")).equals(oStart))
            {
                coPlan.add(new ShiftplanBean(oStart, getEMTNumberAsInt(oRs.getString("nDriverId")),
                    getEMTNumberAsInt(oRs.getString("nMediId")), getEMTNumberAsInt(oRs
                        .getString("nAdditionalId"))));
                bHasNext = oRs.next();
            }
            else
            {
                coPlan.add(new ShiftplanBean(oStart, 0, 0, 0));
            }
        }
        
        oST.close();
        oRs.close();
        oCon.close();

        return coPlan;
    }

    /**
     * 
     * Gibt ein SqlQuery zurück
     * 
     * @param sShift
     * @param sQual
     * @return sqlQuery
     * @throws SQLException 
     *
     */
   /* public static ResultSet getStatement(Connection oCon, String sShift, String sQual, int iPlanId,
        int iTeamNumber, String[] saPeriod) throws SQLException
    {
        PreparedStatement oST = oCon.prepareStatement(SQL_STATEMENT_M);
        oST.setString(1, sQual);
        oST.setInt(2, iPlanId);
        oST.setString(3, sShift);
        oST.setInt(4, iTeamNumber);
        oST.setString(5, saPeriod[0]);
        oST.setString(6, saPeriod[1]);
        
        ResultSet oRS = oST.executeQuery();
        oST.close();

        return oRS;
    }*/

    /**
     * 
     * Gibt den Periodenstart und Ende vom 
     * angeforderten Plan zurück
     * 
     * @param oSt
     * @param iPlanId
     * @return iaDate - Array[0]-PeriodenStart
     *                  Array[1]-PeriodenEnde
     * @throws SQLException
     *
     */
    public static Date[] getDaysOfMonth(int iPlanId) throws SQLException
    {
        Connection oCon = DBHelper.getConnection();
        PreparedStatement oPS = oCon.prepareStatement(SQL_STATEMENT_S);
        oPS.setInt(1, iPlanId);
        ResultSet oRs2 = oPS.executeQuery();

        oRs2.next();
        Date[] iaDate = { oRs2.getDate("dPeriodStart"), oRs2.getDate("dPeriodEnd")};
        oRs2.beforeFirst();
        
        oPS.close();
        oRs2.close();
        oCon.close();

        return iaDate;
    }

    /**
     * Bit alle Schichtpläne der Rettungsleitstelle Bludenz in einer
     * Collection zurück
     * 
     * @return Collection<String[]>
     * @throws SQLException    BESCHREIBUNG_EINFUEGEN
     *
     */
    public static Collection<String[]> getShiftPlans() throws SQLException
    {
        Connection oCon = DBHelper.getConnection();
        //String[] saContent = new String[2];

        PreparedStatement oSt = oCon.prepareStatement(SQL_STATEMENT_PLANS);
        oSt.setInt(1, ((Department) Sessions.getCurrent().getAttribute("department")).getDepartmentId());
        ResultSet oRS = oSt.executeQuery();

        Collection<String[]> coShifts = new ArrayList<String[]>();

        while (oRS.next())
        {
            String[] saContent = new String[3];
            saContent[0] = oRS.getString("cName");
            saContent[1] = oRS.getString("nShiftplanId");
            saContent[2] = oRS.getString("cFilePath");
            System.out.println(saContent);
            coShifts.add(saContent);
        }
        
        oSt.close();
        oRS.close();
        oCon.close();
        
        return coShifts;
    }

    /**
     * 
     * Gibt den Plannamen richtig formatiert zurück.
     * 
     * @param iPlanId
     * @return
     * @throws SQLException
     *
     */
    public static String getPlanName(int iPlanId) throws SQLException
    {
        Connection oCon = DBHelper.getConnection();
        PreparedStatement oSt = oCon.prepareStatement(SQL_STATEMENT_PLAN_NAME);

        oSt.setInt(1, iPlanId);
        ResultSet oRs = oSt.executeQuery();
        oRs.next();

        String sName = oRs.getString("cName");
        String[] sSubN = sName.split(", ");
        String sReturn;
        sReturn = (sSubN[1].split(" "))[1];

        if (sReturn.equals("Bludenz"))
            sReturn = "BZ";
        else
            sReturn = "KA";

        oSt.close();
        oRs.close();
        oCon.close();
        
        return "Dienstplan_" + sReturn + "_";
    }

    /**
     * 
     * Gibt den jeweils benötigten Subschichtplan für
     * einen Subreport in Form eines ShifplanBeans zurück
     * 
     * @param sShift - Schicht des Subreports
     * @param iPlanId - SchichtplanId
     * @param iPeriod - Periode (erster oder zweiter Monat der Periode)
     * 
     * @return Collection<ShiftplanBean>
     * 
     * @throws SQLException
     *
     */
    public static Collection<ShiftplanBean> getSubShiftPlan(String sShift, int iPlanId, int iPeriod)
        throws SQLException
    {
        //RTW
        if (sShift.equals("rtwn1"))
            return fillEmptyPos("rtw", 0, iPlanId, "nightshift", iPeriod - 1);

        else if (sShift.equals("rtwn2"))
            return fillEmptyPos("rtw", 1, iPlanId, "nightshift", iPeriod - 1);

        else if (sShift.equals("rtwd1"))
            return fillEmptyPos("rtw", 0, iPlanId, "dayshift", iPeriod - 1);

        else if (sShift.equals("rtwd2"))
            return fillEmptyPos("rtw", 1, iPlanId, "dayshift", iPeriod - 1);

        else if (sShift.equals("rtwd3"))
            return fillEmptyPos("rtw", 2, iPlanId, "dayshift", iPeriod - 1);

        //NEF
        else if (sShift.equals("nefn1"))
            return fillEmptyPos("nef", 0, iPlanId, "nightshift", iPeriod - 1);

        else if (sShift.equals("nefd1"))
            return fillEmptyPos("nef", 0, iPlanId, "dayshift", iPeriod - 1);

        //Datum
        else if (sShift.equals("Date"))
            return fillEmptyPos("rtw", 0, iPlanId, "nightshift", iPeriod - 1);

        else
            return null;
    }

    /**
     * 
     * Gibt die EMT Nummer als Integer zurück
     * 
     * @param sPersonId
     * @return sPersonId as Integer
     *
     */
    private static int getEMTNumberAsInt(String sPersonId)
    {
        if (sPersonId == null)
        {
            return 0;
        }
        else
            return Integer.valueOf(Person.getPersonById(Integer.valueOf(sPersonId)).getEMTNumber());
    }
}
