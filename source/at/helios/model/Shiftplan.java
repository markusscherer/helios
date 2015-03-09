package at.helios.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.GregorianCalendar;

import at.helios.sheduling.Shifts;
import at.helios.sheduling.Teams;

/**
 * Stellt einen Schichtplan im Speicher dar
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   16.02.2009 Neuerstellung
 * </PRE>
 **/
public class Shiftplan
{
    private List<Shift>         _coNEFTeams = new ArrayList<Shift>();
    private List<Shift>         _coRTWTeams = new ArrayList<Shift>();
    private List<Shift>         _coSTBTeams = new ArrayList<Shift>();
    protected GregorianCalendar _oPeriodStart;
    protected GregorianCalendar _oPeriodEnd;

    /**
     * Gibt Schicht, die gewisse Bedingungen erfüllt zurück
     * @param oCalendar     Datum
     * @param oShiftType    Tagschicht/ Nachtschicht
     * @param oTeamType     NEF/ RTW/ Bereitschaft
     * @return    BESCHREIBUNG_EINFUEGEN
     **/
    public Shift getShiftByDate(GregorianCalendar oCalendar, Shifts oShiftType, Teams oTeamType)
    {
      //TODO ist vermutlich langsam 
        List<Shift> oTempList = null;

        if (oTeamType == Teams.nef)
        {
            oTempList = _coNEFTeams;
        }
        else if (oTeamType == Teams.rtw)
        {
            oTempList = _coRTWTeams;
        }
        else if (oTeamType == Teams.standby)
        {
            oTempList = _coSTBTeams;
        }
        else
        {
            return null;
        }

        for (Shift oShift : oTempList)
        {
            if (oShift.getGregorianCalendar().get(Calendar.MONTH) == oCalendar.get(Calendar.MONTH)
                && oShift.getGregorianCalendar().get(Calendar.DAY_OF_MONTH) == oCalendar.get(Calendar.DAY_OF_MONTH)
                && oShift.getGregorianCalendar().get(Calendar.YEAR) == oCalendar.get(Calendar.YEAR)
                && oShift.getShift() == oShiftType)
            {
                return oShift;
            }
        }

        return null;
    }

    /**
     * Getter für _oPeriodStart.
     * @return _oPeriodStart
     **/
    public GregorianCalendar getPeriodStart()
    {
        return _oPeriodStart;
    }

    /**
     * Setter für _oPeriodStart
     * @param oPeriodStart
     **/
    public void setPeriodStart(GregorianCalendar oPeriodStart)
    {
        _oPeriodStart = oPeriodStart;
    }

    /**
     * Getter für _oPeriodEnd.
     * @return _oPeriodEnd
     **/
    public GregorianCalendar getPeriodEnd()
    {
        return _oPeriodEnd;
    }

    /**
     * Setter für _oPeriodEnd
     * @param oPeriodEnd
     **/
    public void setPeriodEnd(GregorianCalendar oPeriodEnd)
    {
        _oPeriodEnd = oPeriodEnd;
    }

    /**
     * Getter für _coNEFTeams.
     * @return _coNEFTeams
     **/
    public List<Shift> getNEFShifts()
    {
        return _coNEFTeams;
    }

    /**
     * Setter für _coNEFTeams
     * @param coTeams
     **/
    public void setNEFTeams(List<Shift> coTeams)
    {
        _coNEFTeams = coTeams;
    }

    /**
     * Getter für _coRTWTeams.
     * @return _coRTWTeams
     **/
    public List<Shift> getRTWShifts()
    {
        return _coRTWTeams;
    }

    /**
     * Setter für _coRTWTeams
     * @param coTeams
     **/
    public void setRTWTeams(List<Shift> coTeams)
    {
        _coRTWTeams = coTeams;
    }

    /**
     * Getter für _coSTBTeams.
     * @return _coSTBTeams
     **/
    public List<Shift> getSTBShifts()
    {
        return _coSTBTeams;
    }

    /**
     * Setter für _coSTBTeams
     * @param coTeams
     **/
    public void setSTBTeams(List<Shift> coTeams)
    {
        _coSTBTeams = coTeams;
    }
    
    public String toString()
    {
        String sTemp = "\t\t";
       
        if(!_coNEFTeams.isEmpty())
        {
            sTemp += "NEF\t\t";
        }
        if(!_coRTWTeams.isEmpty())
        {
            sTemp += "RTW\t\t";
        }
        if(!_coSTBTeams.isEmpty())
        {
            sTemp += "STB\t\t";
        }
        
        sTemp +="\n";
        
        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
        
        //FIXME: nur für standard-fall
        for(int i = 0; i < _coNEFTeams.size(); i++)
        {
            sTemp += "d" + oFormat.format(_coNEFTeams.get(i).getGregorianCalendar().getTime()) + (_coNEFTeams.get(i).getShift() == Shifts.nightshift ? "n" : "d") + "\t"; 
            
            sTemp += _coNEFTeams.get(i).getTeam(0).getDriver().getEMTNumber() + " " + _coNEFTeams.get(i).getTeam(0).getMedi().getEMTNumber() + "\t"
                  +  _coRTWTeams.get(i).getTeam(0).getDriver().getEMTNumber() + " " + _coRTWTeams.get(i).getTeam(0).getMedi().getEMTNumber() + "\t"
                  +  _coRTWTeams.get(i).getTeam(1).getDriver().getEMTNumber() + " " + _coRTWTeams.get(i).getTeam(1).getMedi().getEMTNumber() + "\n";
        }
        
        return sTemp;
    }
}
