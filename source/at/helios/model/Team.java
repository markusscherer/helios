package at.helios.model;

import at.helios.common.TrainingHelper;
import at.helios.sheduling.Teams;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus 16.02.2009 Neuerstellung
 * </PRE>
 **/
public class Team
{
    private Person _oDriver;
    private Person _oMedi;
    private Person _oAdditional;
    private Teams _oTeamType;
    private ShiftIdentifier _oShiftIdentifier;
    private int _iShiftId;
    
    public Team()
    {  
    };
    
    
    /**
     * TODO comment
     * @param oShiftIdentifier
     */
    public Team(ShiftIdentifier oShiftIdentifier, Teams oTeamType)
    {
        _oShiftIdentifier = oShiftIdentifier;
        _oTeamType = oTeamType;
    }
    /**
     * Getter für _iShiftId.
     * @return _iShiftId
     **/
    public int getShiftId()
    {
        return _iShiftId;
    }
    /**
     * Setter für _iShiftId
     * @param coShiftId
     **/
    public void setShiftId(int coShiftId)
    {
        _iShiftId = coShiftId;
    }
    /**
     * Getter für _oDriver.
     * @return _oDriver
     **/
    public Person getDriver()
    {
        return _oDriver;
    }
    /**
     * Setter für _oDriver
     * @param oDriver
     **/
    public void setDriver(Person oDriver)
    {
        if(_oDriver != oDriver)
        {
            if(_oDriver != null) _oDriver.removeShift(_oShiftIdentifier);
            _oDriver = oDriver;
            if(_oDriver != null) _oDriver.addShift(_oShiftIdentifier, TrainingHelper.getDriverTraining(_oTeamType));
        }
    }
    /**
     * Getter für _oMedi.
     * @return _oMedi
     **/
    public Person getMedi()
    {
        return _oMedi;
    }
    /**
     * Setter für _oMedi
     * @param oMedi
     **/
    public void setMedi(Person oMedi)
    {
        if(_oMedi != oMedi)
        {
            if(_oMedi != null) _oMedi.removeShift(_oShiftIdentifier);
            _oMedi = oMedi;
            if(_oMedi != null) _oMedi.addShift(_oShiftIdentifier, TrainingHelper.getMediTraining(_oTeamType));
        }
    }
    /**
     * Getter für _oAdditional.
     * @return _oAdditional
     **/
    public Person getAdditional()
    {
        return _oAdditional;
    }
    /**
     * Setter für _oAdditional
     * @param oAdditional
     **/
    public void setAdditional(Person oAdditional)
    {
        if(_oAdditional != oAdditional)
        {
            if(_oAdditional != null) _oAdditional.removeShift(_oShiftIdentifier);
            _oAdditional = oAdditional;
            if(_oAdditional != null) _oAdditional.addShift(_oShiftIdentifier, TrainingHelper.getAdditionalTraining());
        }
    }


    public void schedule(Person oDriver, Person oMedi, Person oAdditional)
    {
        setDriver(oDriver);
        setMedi(oMedi);
        setAdditional(oAdditional);
    }
}
