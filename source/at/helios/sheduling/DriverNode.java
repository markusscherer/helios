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
//import at.helios.test.FuzzyQuickSort;
//
///**
// * Bildet Fahrer im Shifttree ab
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   29.10.2008 Neuerstellung
// * </PRE>
// **/
//public class DriverNode implements Comparable<DriverNode>
//{
//    private Person         _oDriver;
//    private List<MediNode> _coMediNodes   = new ArrayList<MediNode>();
//    private int            _iSelectedMedi = -1;
//    private Teams          _oTeamType;
//
//    /**
//     * Konstruktor mit Feldern
//     * @param oDriver
//     **/
//    public DriverNode(Person oDriver, Teams oTeamType)
//    {
//        _oDriver = oDriver;
//        _oTeamType = oTeamType;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Comparable#compareTo(java.lang.Object)
//     */
//    public int compareTo(DriverNode oOtherDriverNode)
//    {
//        /*if (_coMediNodes.size() - oOtherDriverNode.getMediCount() != 0)
//        {
//            return _coMediNodes.size() - oOtherDriverNode.getMediCount();
//        }
//        return _oDriver.getPersonId() - oOtherDriverNode.getDriver().getPersonId();*/
//        
//        float fQuotient1 = (float)_oDriver.getMaxShiftCount()/(float)_oDriver.getShifts().size();
//        float fQuotient2 = (float)oOtherDriverNode.getDriver().getMaxShiftCount()/(float)oOtherDriverNode.getDriver().getShifts().size();
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
//    /**
//     * Getter für _oDriver.
//     * @return _oDriver
//     **/
//    public Person getDriver()
//    {
//        return _oDriver;
//    }
//
//    /**
//     * Fügt einen MediNode an
//     * @param oMediNode   
//     **/
//    public void addMediNode(MediNode oMediNode)
//    {
//        _coMediNodes.add(oMediNode);
//    }
//
//    /**
//     * Validiert die bereits geleisteten Stunden (im Monat) gegen die Regeln der Person
//     * @return    Ist validierung geglückt!
//     **/
//    @SuppressWarnings("unchecked")
//    public boolean checkShiftCount()
//    {
//        Collection<Integer> ciMonths = new ArrayList<Integer>();
//
//        for (ShiftNode oShiftNode : _oDriver.getShifts().keySet())
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
//            for (ShiftNode oShiftNode : _oDriver.getShifts().keySet())
//            {
//                if (iCurrentMonth == oShiftNode.getDate().get(Calendar.MONTH))
//                {
//                    iShiftCount++;
//                }
//
//                if (iShiftCount > _oDriver.getMaxShiftCount())
//                {
//                    return false;
//                }
//            }
//
//            Map<Training, Integer> oTempMap = (Map<Training, Integer>)_oDriver.getTrainings().clone();
//
//            //Wenn der Wert für ein Training auf -1 gesetzt ist, wird die folgende Option nicht genutzt
//            if (_oDriver.getTrainings().values().contains(-1))
//            {
//                continue;
//            }
//
//            for (ShiftNode oShiftNode : _oDriver.getShifts().keySet())
//            {
//                if (oShiftNode.getDate().get(Calendar.MONTH) == iCurrentMonth)
//                {
//                    int iCount = oTempMap.get(_oDriver.getShifts().get(oShiftNode));
//                    iCount--;
//
//                    if (iCount < 0)
//                    {
//                        return false;
//                    }
//                    else
//                    {
//                        oTempMap.put(_oDriver.getShifts().get(oShiftNode), iCount);
//                    }
//                }
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * Überprüft Schicht-Interval
//     * @return    Schichtintervall unterschritten?
//     **/
//    public boolean checkShiftInterval()
//    {
//        ShiftNode[] aoNodes = (ShiftNode[])_oDriver.getShifts().keySet().toArray(
//            new ShiftNode[_oDriver.getShifts().keySet().size()]);
//        List<ShiftNode> coList = Arrays.asList(aoNodes);
//        Collections.sort(coList);
//
//                for (ShiftNode oNode : coList)
//                {
//                    int j = 0;
//                    for(int i = 0; i < oNode.getTeamCount(); i++)
//                    {
//                        if(oNode.getSelectedDriverIndex(i) >= 0)
//                        {
//                            if(oNode.getSelectedDriverNode(i) == this)
//                            {
//                                j++;
//                                if(j > 1) return false;
//                            }
//                        }
//                    }
//                }
//
//        for (int i = 0; i < _oDriver.getShifts().size() - 1; i++)
//        {
//            ShiftNode oShiftNode1 = coList.get(i);
//            ShiftNode oShiftNode2 = coList.get(i + 1);
//
//            long lDifference = oShiftNode2.getDate().getTimeInMillis() - oShiftNode1.getDate().getTimeInMillis();
//
//            lDifference = lDifference / (1000 * 24 * 60 * 60);
//
//            if (lDifference < _oDriver.getMinShiftInterval())
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }
//    
//    /**
//     * Überprüft Knoten auf Gültigkeit
//     * @return    Gibt zurück ob Knoten gültig ist.
//     **/
//    public boolean validate()
//    {
//        if (!checkShiftCount())
//        {
//            return false;
//        }
//
//        if (!checkShiftInterval())
//        {
//            return false;
//        }
//
//        if (_iSelectedMedi == -1)
//        {
//            for (MediNode oMediNode : _coMediNodes)
//            {
//                if (oMediNode.validate())
//                {
//                    return true;
//                }
//            }
//            return false;
//        }
//        else
//        {
//            return _coMediNodes.get(_iSelectedMedi).validate();
//        }
//    }
//
//    /**
//     * Gibt Anzahl der untergeordneten MediNodes zurück
//     * @return    Anzahl der untergeordneten MediNodes 
//     **/
//    public int getMediCount()
//    {
//        return _coMediNodes.size();
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#toString()
//     */
//    public String toString()
//    {
//        String sTemp = "   + " + "p" + _oDriver.getPersonId() + "\n   " + _iSelectedMedi;
//
//        int i = 0;
//        for (MediNode oMediNode : _coMediNodes)
//        {
//            if (i == _iSelectedMedi)
//            {
//                sTemp += "(" + oMediNode + ")  ";
//            }
//            else
//            {
//                sTemp += oMediNode + "  ";
//            }
//            i++;
//        }
//        return sTemp;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#equals(java.lang.Object)
//     */
//    public boolean equals(Object oOtherDriverNode)
//    {
//        return _oDriver.equals(((DriverNode)oOtherDriverNode).getDriver());
//    }
//
//    /**
//     * Überprüft ob der DriverNode einen gewissen MediNode beinhaltet
//     * @param oMedi
//     * @return    Ist der angegebene MediNode enthalten?
//     **/
//    public boolean contains(Person oMedi)
//    {
//        for (MediNode oMediNode : _coMediNodes)
//        {
//            if (oMediNode.getMedi().equals(oMedi))
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Setzt den ausgewählten Sanitäter
//     * @param iMediPointer  Index des MediNodes
//     * @param oShiftNode    zu leistende Schicht
//     **/
//    public void setSelectedMedi(int iMediPointer, ShiftNode oShiftNode)
//    {
//        _iSelectedMedi = iMediPointer;
//
//        Training oTraining = null;
//
//        if (_oTeamType == Teams.rtw)
//        {
//            oTraining = Training.getTrainingById(2);
//        }
//        if (_oTeamType == Teams.nef)
//        {
//            oTraining = Training.getTrainingById(4);
//        }
//        else if (_oTeamType == Teams.standby)
//        {
//            oTraining = Training.getTrainingById(6);
//        }
//
//        _coMediNodes.get(_iSelectedMedi).getMedi().addShift(oShiftNode, oTraining);
//    }
//
//    public void unsetSelectedMedi()
//    {
//        _iSelectedMedi = -1;
//    }
//
//    public void resetSelectedMedi(ShiftNode oShiftNode, int iTeamNumber)
//    {
//        _coMediNodes.get(_iSelectedMedi).getMedi().removeShift(oShiftNode, iTeamNumber);
//        //FIXME prüfen
//        //_iSelectedMedi = -1;
//    }
//
//    /**
//     * Teilt Sanitäter ein
//     * @return    Index des eingeteilten Sanitäters (-1 wenn fehlgeschlagen)
//     **/
//    public int schedule()
//    {
//        if (_iSelectedMedi == -1)
//        {
//            _iSelectedMedi = 0;
//        }
//
//        for (int i = _iSelectedMedi; i < _coMediNodes.size(); i++)
//        {
//            if (_coMediNodes.get(i).validate())
//            {
//                return i;
//            }
//        }
//
//        _iSelectedMedi = -1;
//
//        return -1;
//    }
//
//    /**
//     * Getter für _coMediNodes
//     * @return   _coMediNodes
//     **/
//    public List<MediNode> getMediNodes()
//    {
//        return _coMediNodes;
//    }
//
//    /**
//     * Getter für _iSelectedMedi.
//     * @return _iSelectedMedi
//     **/
//    public int getSelectedMediIndex()
//    {
//        return _iSelectedMedi;
//    }
//
//    /**
//     * Gibt gerade ausgewählten Sanitäter zurück
//     * @return    gerade ausgewählter Sanitäter
//     **/
//    public MediNode getSelectedMediNode()
//    {
//        return _coMediNodes.get(_iSelectedMedi);
//    }
//
//    /**
//     * Gibt nächsten möglichen Sanitäter zurück
//     * @return nächster möglicher Sanitäter
//     * @throws Exception    Wird geworfen, wenn keine möglichen Medis mehr vorhanden sind
//     **/
//    public int getNextMediIndex() throws NoPersonsLeftException
//    {
//        int iNextMedi = _iSelectedMedi;
//
//        if (iNextMedi < _coMediNodes.size() - 1)
//        {
//            iNextMedi++;
//        }
//        else
//        {
//            throw new NoPersonsLeftException("No Medis left");
//        }
//        if(iNextMedi == _coMediNodes.size()) System.out.println("gnaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        return iNextMedi;
//
//    }
//
//    /**
//     * Mischt die MediNodes
//     **/
//    public void shuffle()
//    {
//        FuzzyQuickSort<MediNode> oFuzzyQuickSort = new FuzzyQuickSort<MediNode>(_coMediNodes, 3);
//        if(_iSelectedMedi < 0)
//        {
//            oFuzzyQuickSort.sort();
//        }
//        else
//        {
//            oFuzzyQuickSort.sort(_iSelectedMedi, _coMediNodes.size()-1);
//        }
//    }
//}