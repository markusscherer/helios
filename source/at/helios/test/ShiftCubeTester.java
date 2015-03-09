package at.helios.test;

import java.io.IOException;
import java.util.GregorianCalendar;

import alice.tuprolog.PrologException;
import at.helios.calendar.helper.DateHelper;
import at.helios.calendar.helper.HolidayHelper;
import at.helios.model.Department;
import at.helios.model.Shift;
import at.helios.model.Shiftplan;
import at.helios.model.Team;
import at.helios.model.dao.DepartmentDAO;
import at.helios.model.dao.PersonDAO;
import at.helios.model.dao.TrainingDAO;
import at.helios.sheduling.ShiftCube;
import at.helios.sheduling.Shifts;
import at.helios.sheduling.Teams;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

public class ShiftCubeTester
{

    /**
     * @param args
     * @throws IOException 
     * @throws PrologException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, PrologException, InterruptedException
    {
        DepartmentDAO oDepartmentDAO = new DepartmentDAO();
        oDepartmentDAO.findAll();
        
        TrainingDAO oTrainingDAO = new TrainingDAO();
        oTrainingDAO.findAll();
        
        PersonDAO oPersonDAO = new PersonDAO();
        oPersonDAO.findAll();
        
        Department oDepartment = Department.getDepartmentById(1);
        
        GregorianCalendar oPeriodStart = new GregorianCalendar(2010,4,1);
        GregorianCalendar oPeriodEnd = new GregorianCalendar(2010,4,31);
        GregorianCalendar oTempCalendar = (GregorianCalendar)oPeriodStart.clone();
        HolidayHelper oHolidayHelper = new HolidayHelper();
        int iCount = 1;        
        Teams oTeamType = Teams.nef;
        
        Shiftplan oShiftplan = new Shiftplan();
        ShiftCube oShiftCube = new ShiftCube(oPeriodStart, oPeriodEnd, oDepartment, oTeamType, iCount, oHolidayHelper, oShiftplan.getNEFShifts());
        oShiftCube.generateShiftCube();
        
        boolean bTemp = true;
        
        while (bTemp)
        {
            bTemp = DateHelper.before(oTempCalendar, oPeriodEnd);
            
            for (int i = 0; i < iCount; i++)
            {
                ShiftIdentifier oShiftIdentifier = oShiftCube.getShiftIdentifier(oTempCalendar, Shifts.nightshift);
                if (oShiftIdentifier != null)
                {
                    Shift oShift = oShiftCube.getShift(oShiftIdentifier);
                    Team oTeam = new Team(oShiftIdentifier, oShiftCube.getTeamType());
                    oShift.setTeam(i, oTeam);
                }
                oShiftIdentifier = oShiftCube.getShiftIdentifier(oTempCalendar, Shifts.dayshift);
                if (oShiftIdentifier != null)
                {
                    Shift oShift = oShiftCube.getShift(oShiftIdentifier);
                    Team oTeam = new Team(oShiftIdentifier, oShiftCube.getTeamType());
                    oShift.setTeam(i, oTeam);
                }
            }
  
            long lTime = oTempCalendar.getTimeInMillis();
            lTime += 24 * 60 * 60 * 1000;
            oTempCalendar.setTimeInMillis(lTime);
        }
        oShiftCube.schedulePeriod();
    }
}
