package at.helios.model;

import java.util.GregorianCalendar;

import at.helios.sheduling.Shifts;

/**
 * Bildet Schicht im Speicher ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   30.03.2009 Erstkommentierung
 * </PRE>
 **/
public class Shift
{
    private GregorianCalendar _oGregorianCalendar;
    private Shifts _oShift;
    private Team[] _aoTeams;// = new ArrayList<Team>();
    
    public Shift(GregorianCalendar oDate, Shifts oShift, int iTeamCount)
    {
        _oGregorianCalendar = oDate;
        _oShift = oShift;
        _aoTeams = new Team[iTeamCount];
    }
    
    
    /**
     * Getter für _aoTeams.
     * @return _aoTeams
     **/
    public Team[] getTeams()
    {
        return _aoTeams;
    }

    /**
     * Fügt Team zur Schicht hinzu
     * @param oTeam    
     **/
//    public void addTeam(Team oTeam)
//    {
//        _coTeams.add(oTeam);
//    }
    
    /**
     * Gibt Team mit angegebenen Index zurück
     * @param iTeamNumber Index
     * @return    Team
     **/
    public Team getTeam(int iTeamNumber)
    {
        if(_aoTeams.length <= iTeamNumber)
        {
            return null;
        }
        return _aoTeams[iTeamNumber];
    }
    
    /**
     * Getter für _iTeamCount.
     * @return _iTeamCount
     **/
    public int getTeamCount()
    {
        return _aoTeams.length;
    }


    /**
     * Setter für _iTeamCount
     * @param iTeamCount
     **/
//    public void setTeamCount(int iTeamCount)
//    {
//        _iTeamCount = iTeamCount;
//    }


    /**
     * Getter für _oGregorianCalendar.
     * @return _oGregorianCalendar
     **/
    public GregorianCalendar getGregorianCalendar()
    {
        return _oGregorianCalendar;
    }
    /**
     * Setter für _oGregorianCalendar
     * @param coGregorianCalendar
     **/
    private void setGregorianCalendar(GregorianCalendar coGregorianCalendar)
    {
        _oGregorianCalendar = coGregorianCalendar;
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
     * @param coShift
     **/
    public void setShift(Shifts coShift)
    {
        _oShift = coShift;
    }


    public void setTeam(int iTeamNumber, Team oTeam)
    {
        if(iTeamNumber >= _aoTeams.length)
        {
            throw new RuntimeException("Too high TeamNumber "+iTeamNumber+". Length " + _aoTeams.length + ".");
        }
        _aoTeams[iTeamNumber] = oTeam;
    }
    
    
}