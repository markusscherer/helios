//package at.helios.sheduling;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import at.helios.model.Person;
//import at.helios.model.Training;
//
///**
// * Bildet Sanitäter im Shifttree ab
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   29.10.2008 Neuerstellung
// * </PRE>
// **/
//public class MediNode implements Comparable<MediNode>
//{
//
//    private Person _oMedi;
//
//    /**
//     * Konstruktor mit Feldern
//     * @param oMedi
//     **/
//    public MediNode(Person oMedi)
//    {
//        _oMedi = oMedi;
//    }
//
//    /**
//     * Überprüft ob dieser Knoten gültig ist
//     * @return    Gibt zurück ob der Knoten gültig ist.
//     **/
//    public boolean validate()
//    {
//        if (!checkShiftCount())
//        {
//            return false;
//        }
//        
//        if(!checkShiftInterval())
//        {
//            return false;
//        }
//        
//    	return true;
//    }
//
//    /**
//     * Überprüft Schichtinterval
//     * @return    Wurde das Schichtinterval unterschritten?
//     **/
//    public boolean checkShiftInterval()
//    {
//        ShiftNode[] aoNodes = (ShiftNode[]) _oMedi.getShifts().keySet().toArray(new ShiftNode[_oMedi.getShifts().keySet().size()]);
//        List<ShiftNode> coList = Arrays.asList(aoNodes);
//        Collections.sort(coList);
//        
//        for (ShiftNode oNode : coList)
//        {
//            int j = 0;
//            for(int i = 0; i < oNode.getTeamCount(); i++)
//            {
//                if(oNode.getSelectedDriverIndex(i) > -1)
//                {
//                    if(oNode.getSelectedDriverNode(i).getSelectedMediNode().getMedi() == this.getMedi())
//                    {
//                        j++;
//                        if(j > 1) return false;
//                    }
//                }
//            }
//        }
//    	
//    	for(int i = 0; i < coList.size() -1; i++)
//    	{
//    		ShiftNode oShiftNode1 = coList.get(i);
//    		ShiftNode oShiftNode2 = coList.get(i+1);
//    		
//    		long lDifference = oShiftNode2.getDate().getTimeInMillis() - oShiftNode1.getDate().getTimeInMillis();
//			lDifference = lDifference / (1000 * 24 * 60 * 60);
//			
//			if(lDifference < _oMedi.getMinShiftInterval())
//			{
//				return false;
//			}		
//    	}
//    	return true;
//    }
//
//    /**
//     * Überprüft die Schichtanzahl in einem Monat
//     * @return    Wurde die maximale Schichtanzahl überschritten?
//     **/
//    @SuppressWarnings("unchecked")
//    public boolean checkShiftCount()
//    {
//        Collection<Integer> ciMonths = new ArrayList<Integer>();
//
//        for (ShiftNode oShiftNode : _oMedi.getShifts().keySet())
//        {
//            if (!ciMonths.contains(oShiftNode.getDate().get(Calendar.MONTH)))
//            {
//                ciMonths.add(oShiftNode.getDate().get(Calendar.MONTH));
//            }
//        }
//
//        for (Integer iCurrentMonth : ciMonths)
//        {
//            int iShiftCount = 0;
//            for (ShiftNode oShiftNode : _oMedi.getShifts().keySet())
//            {
//                if (iCurrentMonth == oShiftNode.getDate().get(Calendar.MONTH))
//                {
//                    iShiftCount++;
//                }
//
//                if (iShiftCount > _oMedi.getMaxShiftCount())
//                {
//                    return false;
//                }
//            }
//
//            Map<Training, Integer> oTempMap = (Map<Training, Integer>) _oMedi.getTrainings().clone();
//
//            //Wenn der Wert für ein Training auf -1 gesetzt ist, wird die folgende Option nicht genutzt
//            if (_oMedi.getTrainings().values().contains(-1))
//            {
//                continue;
//            }
//
//            for (ShiftNode oShiftNode : _oMedi.getShifts().keySet())
//            {
//                if (oShiftNode.getDate().get(Calendar.MONTH) == iCurrentMonth)
//                {
//                    int iCount = oTempMap.get(_oMedi.getShifts().get(oShiftNode));
//                    iCount--;
//
//                    if (iCount < 0)
//                    {
//                        return false;
//                    }
//                    else
//                    {
//                        oTempMap.put(_oMedi.getShifts().get(oShiftNode), iCount);
//                    }
//                }
//            }
//        }
//
//        return true;
//    }
//    
//    /**
//     * Getter für _oMedi.
//     * @return _oMedi
//     **/
//    public Person getMedi()
//    {
//        return _oMedi;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Comparable#compareTo(java.lang.Object)
//     */
//    public int compareTo(MediNode oOtherMediNode)
//    {
//        /*
//        if(_oMedi.getMaxShiftCount() - oOtherMediNode.getMedi().getMaxShiftCount() != 0)
//        {
//            return _oMedi.getMaxShiftCount() - oOtherMediNode.getMedi().getMaxShiftCount();
//        }
//        return _oMedi.getPersonId() - oOtherMediNode.getMedi().getPersonId();*/
//        
//        float fQuotient1 = (float)_oMedi.getMaxShiftCount()/(float)_oMedi.getShifts().size();
//        float fQuotient2 = (float)oOtherMediNode.getMedi().getMaxShiftCount()/(float)oOtherMediNode.getMedi().getShifts().size();
//        
//        if(fQuotient1 > fQuotient2)
//        {
//            return 1;
//        }
//        else if(fQuotient1 < fQuotient2)
//        {
//            return -1;
//        }        
//        return 0;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#toString()
//     */
//    public String toString()
//    {
//        return "     + p" + _oMedi.getPersonId();
//    }
//}