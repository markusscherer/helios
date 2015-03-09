/**
 * 
 */
package at.helios.sheduling.shiftcube;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import at.helios.sheduling.Shifts;
import at.helios.sheduling.Teams;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus.scherer 27.04.2010 Neuerstellung
 * </PRE>
 */
public class ShiftIdentifier
{
    private int _iIndex;
    private GregorianCalendar _oDate;
    private Shifts            _oShift;
    private boolean[]         _abSelectedTeams;
    private Teams             _oTeamType;
    
    public ShiftIdentifier(GregorianCalendar oDate, Shifts oShift,
        int iTeamCount, Teams oTeamType, int iIndex)
    {
        _oDate = oDate;
        _oShift = oShift;
        _oTeamType = oTeamType;
        _abSelectedTeams = new boolean[iTeamCount];
        Arrays.fill(_abSelectedTeams, false);
        _iIndex = iIndex;
    }
    
    /**
     * Gibt kurzen Namen des ShiftIdentifiers zurück
     * @return    kurzer Name des ShiftNode
     **/
    public String getShortName()
    {
        String sTemp = "d";

        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");

        sTemp += oFormat.format(_oDate.getTime());

        if (_oShift == Shifts.nightshift)
        {
            sTemp += "n";
        }
        else
        {
            sTemp += "d";
        }
        return sTemp;
    }
    
    //TODO comment
    public int getIndex()
    {
        return _iIndex;
    }

    //TODO comment
    public Calendar getDate()
    {
        return _oDate;
    }
    
    public void setSelectedTeam(int iTeamNumber)
    {
        _abSelectedTeams[iTeamNumber] = true;
    }
    
    public void resetSelectedTeam(int iTeamNumber)
    {
        _abSelectedTeams[iTeamNumber] = false;
    }
    
    public boolean isScheduled()
    {
        for(boolean bTemp : _abSelectedTeams)
        {
            if(!bTemp)
            {
                return false;
            }
        }
        return true;
    }
    
   
    //TODO comment
    public Shifts getShift()
    {
        return _oShift;
    }
    
    //TODO comment
    public Teams getTeamType()
    {
        return _oTeamType;
    }

    public int getTeamCount()
    {
        return _abSelectedTeams.length;
    }

    public boolean isScheduled(int iTeamNumber)
    {
        return _abSelectedTeams[iTeamNumber];
    }
}
