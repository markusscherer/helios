/**
 * 
 */
package at.helios.sheduling.shiftcube;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.helios.common.TrainingHelper;
import at.helios.model.Person;
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
public class DriverIdentifier
{
    private Person _oDriver;
    private Teams _oTeamType;
    private int _iIndex;

    public DriverIdentifier(Person oPerson, Teams oTeamType, int iIndex)
    {
        _oDriver = oPerson;
        _oTeamType = oTeamType;
        _iIndex = iIndex;
    }
    
    
    //TODO comment
    public int getIndex()
    {
        return _iIndex;
    }

    //TODO comment
    public boolean checkShiftCount(ShiftIdentifier oNewShiftIdentifier)
    {
        Collection<Integer> ciMonths = new ArrayList<Integer>();

        for (ShiftIdentifier oShiftIdentifier : _oDriver.getShifts().keySet())
        {
            if (!ciMonths.contains(oShiftIdentifier.getDate().get(Calendar.MONTH)))
            {
                ciMonths.add(oShiftIdentifier.getDate().get(Calendar.MONTH));
            }
        }

        for (Integer iCurrentMonth : ciMonths)
        {
            int iShiftCount = 0;
            for (ShiftIdentifier oShiftIdentifier : _oDriver.getShifts().keySet())
            {
                if (iCurrentMonth == oShiftIdentifier.getDate().get(Calendar.MONTH))
                {
                    iShiftCount++;
                }

                //größer-gleich wichtig
                if (iShiftCount >= _oDriver.getMaxShiftCount())
                {
                    return false;
                }
            }

            Map<Training, Integer> oTempMap = new HashMap<Training, Integer>();
            
            for(Training oTraining : _oDriver.getTrainings().keySet())
            {
                oTempMap.put(oTraining, 0);
            }
            
            Training oNewTraining = TrainingHelper.getDriverTraining(_oTeamType);
            
            oTempMap.put(oNewTraining, 1);

            //Wenn der Wert für ein Training auf -1 gesetzt ist, wird die folgende Option nicht genutzt
            //TODO: beser machen
            //TODO: besser gemacht! -> in DriverNode ändern
            //if (_oDriver.getTrainings().values().contains(-1))
            //{
            //    continue;
            //}

            for (ShiftIdentifier oShiftIdentifier : _oDriver.getShifts().keySet())
            {
                if (oShiftIdentifier.getDate().get(Calendar.MONTH) == iCurrentMonth)
                {
                    Training oTraining = _oDriver.getShifts().get(oShiftIdentifier);
                    int iCount1 = oTempMap.get(oTraining)+1;
                    int iCount2 = _oDriver.getTrainings().get(oTraining);
                    
                    //hier kein kleiner-gleich, weil wir oNewShiftIdentifier
                    if (iCount1 >  iCount2 && iCount2 != -1)
                    {
                        return false;
                    }
                    else
                    {
                        oTempMap.put(_oDriver.getShifts().get(oShiftIdentifier), iCount1);
                    }
                }
            }
        }
        return true;
    }
    
    //TODO comment
    public boolean checkShiftInterval(ShiftIdentifier oNewShiftIdentifier)
    {
        for(ShiftIdentifier oShiftIdentifier : _oDriver.getShifts().keySet())
        {
            long lDifference = Math.abs(oNewShiftIdentifier.getDate().getTimeInMillis() - oShiftIdentifier.getDate().getTimeInMillis());
            lDifference /= 60*60*24*1000;
            if (lDifference < _oDriver.getMinShiftInterval())
            {
                return false;
            }
        }
        return true;
    }
    
    //TODO comment
    public Person getDriver()
    {
        return _oDriver;
    }

}
