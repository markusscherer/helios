package at.helios.sheduling.shiftcube;

import at.helios.common.TrainingHelper;
import at.helios.model.Training;
import at.helios.sheduling.Teams;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus.scherer 27.04.2010 Neuerstellung
 * </PRE>
 */
public class ScheduledTeam
{
    ShiftIdentifier _oShiftIdentifier;
    DriverIdentifier _oDriverIdentifier;
    MediIdentifier _oMediIdentifier;
    int _iTeamNumber;
    
    public ScheduledTeam(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier, MediIdentifier oMediIdentifier, Teams oTeamType, int iTeamNumber)
    {
        _oShiftIdentifier = oShiftIdentifier;
        _oDriverIdentifier = oDriverIdentifier;
        _oMediIdentifier = oMediIdentifier;
        _iTeamNumber = iTeamNumber;
        
        Training oDriverTraining = TrainingHelper.getDriverTraining(oTeamType);
        Training oMediTraining = TrainingHelper.getMediTraining(oTeamType);
        

        
//        _oDriverIdentifier.getDriver().addShift(_oShiftIdentifier, oDriverTraining);
//        _oMediIdentifier.getMedi().addShift(_oShiftIdentifier, oMediTraining);
        _oShiftIdentifier.setSelectedTeam(_iTeamNumber);
        
    }
    
//    public void revert()
//    {
//        _oDriverIdentifier.getDriver().removeShift(_oShiftIdentifier, _iTeamNumber);
//    }
    
    public String toString()
    {
        return _oShiftIdentifier.getShortName() + " " + _oDriverIdentifier.getDriver().getEMTNumber() + " " + _oMediIdentifier.getMedi().getEMTNumber();
    }
    
    public ShiftIdentifier getShiftIdentifier()
    {
        return _oShiftIdentifier;
    }
    
    public String toVerboseString()
    {
        return _oShiftIdentifier.getShortName() + " " + _oDriverIdentifier.getDriver().getEMTNumber() + "(" +_oDriverIdentifier.getDriver().getShifts().size()+ "/" +_oDriverIdentifier.getDriver().getMaxShiftCount() +") " + _oMediIdentifier.getMedi().getEMTNumber()+ "(" +_oMediIdentifier.getMedi().getShifts().size()+ "/" +_oMediIdentifier.getMedi().getMaxShiftCount() +") ";
    }
}
