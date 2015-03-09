//package at.helios.sheduling;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import alice.tuprolog.Prolog;
//import alice.tuprolog.PrologException;
//import at.helios.common.IntUtils;
//import at.helios.model.Person;
//import at.helios.model.Training;
//import at.helios.test.FuzzyQuickSort;
//
///**
// * Bildet eine Schicht im Shifttree ab
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   29.10.2008 Neuerstellung
// * </PRE>
// **/
//public class ShiftNode implements Comparable<ShiftNode>
//{
//
//    private GregorianCalendar _oDate;
//    private Shifts            _oShift;
//    private List<DriverNode>  _coDriverNodes = new ArrayList<DriverNode>();
//    private Person            _oAdditional;
//    private int[]             _aiSelectedTeams;
//    private boolean           _bIgnore       = false;
//    private Teams             _oTeamType;
//
//    /**
//     * Konstruktor mit Feldern
//     * @param oDate
//     * @param oShift
//     * @param iRequiredTeams
//     * @param oTeamType
//     **/
//    public ShiftNode(GregorianCalendar oDate, Shifts oShift, int iRequiredTeams, Teams oTeamType)
//    {
//        _oDate = oDate;
//        _oShift = oShift;
//        _aiSelectedTeams = new int[iRequiredTeams];
//        Arrays.fill(_aiSelectedTeams, -1);
//        _oTeamType = oTeamType;
//    }
//
//    /**
//     * Getter für _oAdditional.
//     * @return _oAdditional
//     **/
//    public Person getAdditional()
//    {
//        return _oAdditional;
//    }
//
//    /**
//     * Setter für _oAdditional
//     * @param oAdditional
//     **/
//    public void setAdditional(Person oAdditional)
//    {
//        _oAdditional = oAdditional;
//    }
//
//    /**
//     * Holt alle möglichen Teams und fügt sie im ShiftNode ein
//     * @param oPrologEngine
//     * @throws PrologException
//     **/
//    public void addDrivers(Prolog oPrologEngine, Teams oTeamType) throws PrologException
//    {
//        TeamLAO oTeamLAO = new TeamLAO(oPrologEngine);
//
//        if (oTeamType == Teams.nef)
//        {
//            _coDriverNodes = oTeamLAO.getNEFTeamsByDate(_oDate, _oShift);
//        }
//        else if (oTeamType == Teams.rtw)
//        {
//            _coDriverNodes = oTeamLAO.getRTWTeamsByDate(_oDate, _oShift);
//        }
//        else if (oTeamType == Teams.standby)
//        {
//            _coDriverNodes = oTeamLAO.getSTBTeamsByDate(_oDate, _oShift);
//        }
//
//    }
//
//    /**
//     * Setzt ausgewähltes Team
//     * @param iTeamNumber     Nummer des Teams
//     * @param iDriverPointer  Index des Fahrers
//     * @param iMediPointer    Index des Sanitäters
//     **/
//    public void setSelectedTeam(int iTeamNumber, int iDriverPointer, int iMediPointer)
//    {
//        _coDriverNodes.get(iDriverPointer).setSelectedMedi(iMediPointer, this);
//
//        Training oTraining = new Training(0, "", "");
//
//        if (_oTeamType == Teams.rtw)
//        {
//            oTraining = Training.getTrainingById(1);
//        }
//        if (_oTeamType == Teams.nef)
//        {
//            oTraining = Training.getTrainingById(3);
//        }
//        else if (_oTeamType == Teams.standby)
//        {
//            oTraining = Training.getTrainingById(5);
//        }
//
//        _coDriverNodes.get(iDriverPointer).getDriver().addShift(this, oTraining);
//        _aiSelectedTeams[iTeamNumber] = iDriverPointer;
//    }
//    
//    //TODO revise
//    public void setSelectedTeam(int iTeamNumber)
//    {
//        _aiSelectedTeams[iTeamNumber] = 1000;
//    }
//
//    /**
//     * Macht Auswahl eines Teams rückgängig
//     * @param iTeamNumber    Nummer des Teams
//     **/
//    public void resetSelectedTeam(int iTeamNumber)
//    {
//        if (_aiSelectedTeams[iTeamNumber] != -1)
//        {
//            _coDriverNodes.get(_aiSelectedTeams[iTeamNumber]).resetSelectedMedi(this, iTeamNumber);
//            _coDriverNodes.get(_aiSelectedTeams[iTeamNumber]).getDriver().removeShift(this, iTeamNumber);
//            //FIXME prüfen
//            //_aiSelectedTeams[iTeamNumber] = -1;
//        }
//    }
//
//    /**
//     * Überprüft diesen Knoten auf Gültigkeit
//     * @return    Gibt zurück ob dieser Knoten gültig ist.
//     **/
//    public boolean validate()
//    {
//        if (_bIgnore)
//        {
//            return true;
//        }
//
//        for (int i = 0; i < _aiSelectedTeams.length; i++)
//        {
//            if (_aiSelectedTeams[i] != -1)
//            {
//                if (!getSelectedDriverNode(i).validate())
//                {
////                    System.out.println("notvalid-false");
//                    return false;
//                }
//            }
//        }
//
//        int iValidTeams = 0;
//
//        for (DriverNode oDriverNode : _coDriverNodes)
//        {
//            if (oDriverNode.validate())
//            {
//                iValidTeams++;
//
//                if (iValidTeams >= _aiSelectedTeams.length)
//                {
////                    System.out.println("enough-valid-true");
//                    return true;
//                }
//            }
//        }
//        
////        System.out.println("not-enough-valid-false");
//
//        return false;
//    }
//
//    /**
//     * Teilt nächstes Mögliches Team ein
//     * @param iTeamNumber
//     * @param oSchedulerStack
//     * @return    Einteilung geglückt?
//     **/
//    public boolean scheduleNext(int iTeamNumber, SchedulerStack oSchedulerStack)
//    {
//        if (_bIgnore)
//        {
//            return false;
//        }
//
//        try
//        {
//            //FIXME: (a) Das (also die Kommentierung) ist offenbar leeeeeeeeebens wichtig -.-
//            //if(_aiSelectedTeams[iTeamNumber] < 0) _aiSelectedTeams[iTeamNumber] = 0;
//            int iNextMedi = getSelectedDriverNode(iTeamNumber).getNextMediIndex();
//            oSchedulerStack.push(new SchedulerStackItem(iTeamNumber, _aiSelectedTeams[iTeamNumber],
//                iNextMedi, this));
//        }
//        catch (Exception e)
//        {
//            if (_aiSelectedTeams[iTeamNumber] < _coDriverNodes.size() - 1)
//            {
//                int iNextDriver;
//                try
//                {
//                    iNextDriver = getNextDriverIndex(iTeamNumber); //_aiSelectedTeams[iTeamNumber] + 1;
//                }
//                catch (NoPersonsLeftException e1)
//                {
////                    _aiSelectedTeams[iTeamNumber] = -1;
////                    _bIgnore = true;
//                    //TODO: Evtl. Pop
//                    System.out.println("exit-1");
//                    return false;
//                }
//                oSchedulerStack.push(new SchedulerStackItem(iTeamNumber, iNextDriver, 0, this));
//            }
//            else
//            {
//                //TODO: Evtl. Pop
//                getSelectedDriverNode(iTeamNumber).unsetSelectedMedi();
//                System.out.println(getShortName() + "-f-" + _oTeamType.name() + "---" + _aiSelectedTeams[iTeamNumber]);
//                _aiSelectedTeams[iTeamNumber] = -1;
//                _bIgnore = true;
//                System.out.println("exit-2");
//                return false;
//            }
//        }
//        return true;
//    }
//    
//    private int getNextDriverIndex(int iTeamNumber) throws NoPersonsLeftException
//    {
//        //FIXME das geht vermutlich schlauer (hängt vrmtl. irgendwie mit (a) zusammen)
//        int iTemp = _aiSelectedTeams[iTeamNumber] + 1;
//        
//        for(int i : _aiSelectedTeams)
//        {
//            if(iTemp == i) iTemp++;
//        }
//        
//        if(iTemp >= _coDriverNodes.size()) throw new NoPersonsLeftException("No Drivers left.");
//        
//        return iTemp;
//    }
//
//    /**
//     * Getter für _oDate.
//     * @return _oDate
//     **/
//    public GregorianCalendar getDate()
//    {
//        return _oDate;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#toString()
//     */
//    public String toString()
//    {
//        SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
//        String sTemp = "  + " + oFormat.format(_oDate.getTime()) + " - " + _oShift.toString() + " " + _oTeamType.name() 
//            + (_bIgnore ? " (ignored)" : "");// + _aiSelectedTeams[0] + " " + _aiSelectedTeams[1];
//
//        if(!_bIgnore)
//        {
//            for(int i = 0; i < _aiSelectedTeams.length; i++)
//            {
//                if(getSelectedDriverIndex(i) >= 0)
//                {
//                    sTemp += "\n"+i + "  " + getSelectedDriverNode(i).getDriver().getEMTNumber() + " ";
//                    
//                    if(getSelectedDriverNode(i).getSelectedMediIndex() >= 0)
//                    {
//                         sTemp += getSelectedDriverNode(i).getSelectedMediNode().getMedi().getEMTNumber();
//                    }
//                    else
//                    {
//                        sTemp += "noMedi";
//                    }
//                }
//                else
//                {
//                    sTemp += "noDriver";
//                }
//            }
//        }
//        
//        /*int i = 0;
//        boolean bIsSet = false;
//        for (DriverNode oDriverNode : _coDriverNodes)
//        {
//            for (int j = 0; j < _aiSelectedTeams.length; j++)
//            {
//                if (_aiSelectedTeams[j] == i)
//                {
//                    bIsSet = true;
//                    break;
//                }
//            }
//
//            if (bIsSet)
//            {
//                sTemp += "\n[" + oDriverNode + "]";
//            }
//            else
//            {
//                sTemp += "\n" + oDriverNode;
//            }
//            bIsSet = false;
//            i++;
//        }*/
//        return sTemp;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Comparable#compareTo(java.lang.Object)
//     */
//    public int compareTo(ShiftNode oOtherShiftNode)
//    {
//        if (_oDate.compareTo(oOtherShiftNode.getDate()) != 0)
//        {
//            return _oDate.compareTo(oOtherShiftNode.getDate());
//        }
//
//        return _oShift.ordinal() - oOtherShiftNode.getShift().ordinal();
//    }
//
//    /**
//     * Getter für _oShift.
//     * @return _oShift
//     **/
//    public Shifts getShift()
//    {
//        return _oShift;
//    }
//
//    /**
//     * Checkt ob der ShiftNode Team enthält
//     * @param oDriver
//     * @param oMedi
//     * @return    Enthält der ShiftNode das angegebene Team?
//     **/
//    public boolean contains(Person oDriver, Person oMedi)
//    {
//        for (DriverNode oDriverNode : _coDriverNodes)
//        {
//            if (oDriverNode.getDriver().equals(oDriver))
//            {
//                return oDriverNode.contains(oMedi);
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * Fügt Fahrer zu ShiftNode hinzu
//     * @param oDriverNode
//     **/
//    public void addDriver(DriverNode oDriverNode)
//    {
//        _coDriverNodes.add(oDriverNode);
//    }
//
//    /**
//     * Setter für _bIgnore
//     * @param bIgnore
//     **/
//    public void setIgnorable(boolean bIgnore)
//    {
//        _bIgnore = bIgnore;
//    }
//
//    /**
//     * Getter für _coDriverNodes.
//     * @return _coDriverNodes
//     **/
//    public List<DriverNode> getDriverNodes()
//    {
//        return _coDriverNodes;
//    }
//
//    /**
//     * Gibt Index des ausgewählten DriverNodes zurück
//     * @param iTeamNumber
//     * @return    Index des ausgewählten Fahrers
//     **/
//    public int getSelectedDriverIndex(int iTeamNumber)
//    {
//        return _aiSelectedTeams[iTeamNumber];
//    }
//
//    /**
//     * Gibt ausgewählten DriverNode zurück
//     * @param iTeamNumber
//     * @return    ausgewählter DriverNode
//     **/
//    public DriverNode getSelectedDriverNode(int iTeamNumber)
//    {
//        return _coDriverNodes.get(_aiSelectedTeams[iTeamNumber]);
//    }
//
//    /**
//     * Getter für _bIgnore.
//     * @return _bIgnore
//     **/
//    public boolean isIgnore()
//    {
//        return _bIgnore;
//    }
//
//    
//    /**
//     * Gibt kurzen Namen des ShiftNodes zurück
//     * @return    kurzer Name des ShiftNode
//     **/
//    public String getShortName()
//    {
//        String sTemp = "d";
//
//        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
//
//        sTemp += oFormat.format(this.getDate().getTime());
//
//        if (_oShift == Shifts.nightshift)
//        {
//            sTemp += "n";
//        }
//        else
//        {
//            sTemp += "d";
//        }
//        return sTemp;
//    }
//
//    /**
//     * Gibt Anzahl der einzuteilenden Teams zurück
//     * @return    Anzahl der einzuteilenden Teams
//     **/
//    public int getTeamCount()
//    {
//        return _aiSelectedTeams.length;
//    }
//
//    /**
//     * Mischt die darunterliegenden Ebenen
//     **/
//    public void shuffle()
//    {
//        FuzzyQuickSort<DriverNode> oFuzzyQuickSort = new FuzzyQuickSort<DriverNode>(_coDriverNodes, 4);
//        System.out.println("ShiftNode.shuffle()");
//        if(IntUtils.getMaximum(_aiSelectedTeams) > -1)
//        {
//            System.out.println("IntUtils.getMaximum(_aiSelectedTeams) > 0");
//            System.out.println("ShiftNode.shuffle()-461");
//            int iFrom = IntUtils.getMaximum(_aiSelectedTeams)+1;
//            int iTo = _coDriverNodes.size()-1;
//            if(iFrom > iTo)
//            {
//                iFrom = iTo;
//            }
//            oFuzzyQuickSort.sort(iFrom, iTo);
//            for (int i = IntUtils.getMaximum(_aiSelectedTeams)+1; i < _coDriverNodes.size(); i++)
//            {
//                _coDriverNodes.get(i).shuffle();
//            }
//        }
//        else
//        {
//            System.out.println("ShiftNode.shuffle()-470");
//            oFuzzyQuickSort.sort();
//            for (int i = 0; i < _coDriverNodes.size(); i++)
//            {
//                _coDriverNodes.get(i).shuffle();
//            }
//        }
//    }
//
//}