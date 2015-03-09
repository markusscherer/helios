//package at.helios.sheduling;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import alice.tuprolog.InvalidTheoryException;
//import alice.tuprolog.Prolog;
//import alice.tuprolog.Theory;
//import at.helios.calendar.helper.DateHelper;
//import at.helios.calendar.helper.HolidayHelper;
//import at.helios.model.Department;
//import at.helios.model.Person;
//import at.helios.model.Training;
//
///**
// * Bildet alle möglichen Schichten als Baum ab
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   29.10.2008 Neuerstellung
// *         mas   23.12.2009 Bug bei Sommerzeit/Winterzeitumstellung behoben
// *         mas   11.03.2010 generateShiftTree und schedulePeriod arbeiten nur noch bei iTeamcount > 0, generateShiftTree jetzt void 
// *         mas   12.03.2010 Leistungsoptimierung der Generierung durch Verkleinerung der Knowledgebase auf relevante Personen
// * </PRE>
// **/
//public class ShiftTree
//{
//
//    private Prolog              _oPrologEngine;
//    protected List<ShiftNode>   _coShiftNodes         = new ArrayList<ShiftNode>();
//    /**
//     * Vorlage für Datumsregel, zB date_day_shift(d15112008n,saturday,nightshift). <code>PROLOG_DATE_DAY_SHIFT</code>
//     **/
//    private static final String PROLOG_DATE_DAY_SHIFT = "date_day_shift(d%D1%%D2%,%D3%,%S1%).";
//    private Department          _oDepartment;
//    protected GregorianCalendar _oPeriodStart;
//    protected GregorianCalendar _oPeriodEnd;
//    private SchedulerStack      _oSchedulerStack      = new SchedulerStack();
//    private Teams               _oTeamType;
//    private int                 _iTeamCount;
//    private HolidayHelper       _oHolidayHelper;
//
//    /**
//     * Getter für _oTeamType.
//     * @return _oTeamType
//     **/
//    public Teams getTeamType()
//    {
//        return _oTeamType;
//    }
//
//    private void shuffle()
//    {
//        //TODO: eingeteilte ShiftNodes nicht mischen
//        System.out.println("ShiftTree.shuffle()-60");
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            oShiftNode.shuffle();
//        }
//    }
//
//    /**
//     * Konstruktor mit Feldern //TODO anpassen
//     * @param oPeriodEnd
//     * @param oPeriodStart
//     * @param oDepartment
//     **/
//    public ShiftTree(GregorianCalendar oPeriodStart, GregorianCalendar oPeriodEnd, Department oDepartment,
//        Teams oTeamType, int iTeamCount, HolidayHelper oHolidayHelper)
//    {
//        _oPeriodEnd = oPeriodEnd;
//        _oPeriodStart = oPeriodStart;
//        _oDepartment = oDepartment;
//        _oTeamType = oTeamType;
//        _iTeamCount = iTeamCount;
//        _oHolidayHelper = oHolidayHelper;
//    }
//
//    /**
//     * Initialisiert den Shifttree
//     * @throws InvalidTheoryException Ungültige Theorie
//     * @throws FileNotFoundException Prologdatei nicht gefunden
//     * @throws IOException    Lesefehler bei Prologdatei
//     **/
//    protected void init() throws InvalidTheoryException, FileNotFoundException, IOException
//    {
//        _oPrologEngine = new Prolog();
//        _oPrologEngine.setTheory(new Theory(this.getClass().getClassLoader().getResourceAsStream(
//            "resources/scheduler_logic.pro")));
//
//    }
//
//    /**
//     * Fügt Daten und Tage in die Prolog-Theorie eins
//     * @throws InvalidTheoryException    Ungültige Theorie
//     **/
//    protected void addDates() throws InvalidTheoryException
//    {
//        System.out.println("ShiftTree.addDates()");
//        String sDate = "";
//        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
//
//        GregorianCalendar oTempCalendar = new GregorianCalendar();
//        oTempCalendar = (GregorianCalendar) _oPeriodStart.clone();
//        
//        //Um Bugs bei Sommerzeit/Winterzeitumstellung zu verhindern
//        _oPeriodEnd.set(Calendar.HOUR_OF_DAY, 12);
//        oTempCalendar.set(Calendar.HOUR_OF_DAY, 12);
//
//        boolean bTemp = true;
//
//        while (bTemp)
//        {
//            /* Um Bugs bei Sommerzeit/Winterzeitumstellung zu verhindern
//             * z.B. bei bei Periode 01.10.2010-31.10.2010
//             * anstatt bTemp = oTempCalendar.before(_oPeriodEnd);
//             */
//            //bTemp = oTempCalendar.before(_oPeriodEnd);
//            //if(bTemp)
//            //{
//            //System.out.println(oFormat.format(oTempCalendar.getTime()) + "(" + oTempCalendar.getTimeInMillis() + ")  -  " + oFormat.format(_oPeriodEnd.getTime()) + "(" + _oPeriodEnd.getTimeInMillis() +")");
//            bTemp = DateHelper.before(oTempCalendar, _oPeriodEnd);
//            //bTemp = Math.abs(oTempCalendar.getTimeInMillis()-_oPeriodEnd.getTimeInMillis()) >= 1000*60*60;
//            //}
//            sDate = PROLOG_DATE_DAY_SHIFT.replaceAll("%D1%", oFormat.format(oTempCalendar.getTime()));
//            
//            System.out.println("gnaaz" + sDate);
//            int iDay = oTempCalendar.get(Calendar.DAY_OF_WEEK) - 2;
//            if (iDay == -1)
//            {
//                iDay = 6;
//            }
//            sDate = sDate.replaceAll("%D3%", Days.values()[iDay].toString());
//
//            if (iDay == 6 || _oHolidayHelper.isHoliday(oTempCalendar))
//            {
//                _oPrologEngine.addTheory(new Theory(sDate.replaceAll("%D2%", "n").replaceAll("%S1%",
//                    Shifts.nightshift.toString())));
//                _coShiftNodes.add(new ShiftNode((GregorianCalendar) oTempCalendar.clone(), Shifts.nightshift,
//                    _iTeamCount, _oTeamType));
//
//                _oPrologEngine.addTheory(new Theory(sDate.replaceAll("%D2%", "d").replaceAll("%S1%",
//                    Shifts.dayshift.toString())));
//                _coShiftNodes.add(new ShiftNode((GregorianCalendar) oTempCalendar.clone(), Shifts.dayshift,
//                    _iTeamCount, _oTeamType));
//            }
//            else if (iDay <= 5)
//            {
//                sDate = sDate.replaceAll("%D2%", "n");
//                sDate = sDate.replaceAll("%S1%", Shifts.nightshift.toString());
//
//                _oPrologEngine.addTheory(new Theory(sDate));
//                _coShiftNodes.add(new ShiftNode((GregorianCalendar) oTempCalendar.clone(), Shifts.nightshift,
//                    _iTeamCount, _oTeamType));
//            }
//
//            long lTime = oTempCalendar.getTimeInMillis();
//            lTime += 24 * 60 * 60 * 1000;
//            oTempCalendar.setTimeInMillis(lTime);
//        }
//        System.out.println("ShiftTree.addDates()-2");
//    }
//
//    /**
//     * Getter für _iTeamCount.
//     * @return _iTeamCount
//     **/
//    public int getTeamCount()
//    {
//        return _iTeamCount;
//    }
//
//    /**
//     * Fügt Personen in die Prolog-Theorie ein
//     * @throws InvalidTheoryException    Ungültige Theorie
//     **/
//    protected void addPersons() throws InvalidTheoryException
//    {
//        int iDriverTraining = 0;
//        int iMediTraining = 0;
//        if(_oTeamType == Teams.rtw)
//        {
//            iDriverTraining = 1;
//            iMediTraining = 2;
//        }
//        else if(_oTeamType == Teams.nef)
//        {
//            iDriverTraining = 3;
//            iMediTraining = 4;
//        }
//        else if(_oTeamType == Teams.standby)
//        {
//            iDriverTraining = 5;
//            iMediTraining = 6;
//        }
//        else
//        {
//                throw new RuntimeException("No valid Teamtype specified.");
//        }
//        System.out.println("ShiftTree.addPersons()-1");
//        for (Person oPerson : Person.getPersonsByDepartment(_oDepartment))
//        {
//            if(oPerson.getTrainings().containsKey(Training.getTrainingById(iDriverTraining)) || oPerson.getTrainings().containsKey(Training.getTrainingById(iMediTraining)))
//            {
//                _oPrologEngine.addTheory(new Theory(oPerson.getPrologStatement()));
//            }
//        }
//        System.out.println("ShiftTree.addPersons()-2");
//    }
//
//    /**
//     * Generiert alle möglichen Schicht/Team Konstellationen in Form eines Shifttrees
//     **/
//    public void generateShiftTree()
//    {
//        if(_iTeamCount < 1)
//        {
//            return;
//        }
//        try
//        {
//            init();
//            addPersons();
//            addDates();
//            for (ShiftNode oShiftNode : _coShiftNodes)
//            {
//                oShiftNode.addDrivers(_oPrologEngine, _oTeamType);
//            }
//
//            shuffle();
//            setIgnorables();
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return;
//        }
//        System.out.println("\t\t" + TeamLAO.getPrologTime());
//        return;
//    }
//
//    /**
//     * Teilt die gesamte Periode ein
//     **/
//    public void schedulePeriod()
//    {
//        if(_iTeamCount < 1)
//        {
//            return;
//        }
//        for (int iCurrentShiftNode = 0; iCurrentShiftNode < _coShiftNodes.size(); iCurrentShiftNode++)
//        {
//            ShiftNode oShiftNode = _coShiftNodes.get(iCurrentShiftNode);
//
//            for (int iCurrentTeam = 0; iCurrentTeam < _iTeamCount; iCurrentTeam++)
//            {
//                while (oShiftNode.scheduleNext(iCurrentTeam, _oSchedulerStack))
//                {
//                    if (this.validate())
//                    {
//                        this.shuffle();
//                        break;
//                    }
//                    else
//                    {
//                        _oSchedulerStack.pop();
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Überprüft Baum auf Gültigkeit
//     * @return    Gibt zurück ob der Baum gültig ist.
//     **/
//    public boolean validate()
//    {
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            if (!oShiftNode.validate())
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Setzt alle ShiftNodes, für die es nicht genug Schichten gibt auf Ignorable
//     **/
//    public void setIgnorables()
//    {
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            if (!oShiftNode.validate())
//            {
//                oShiftNode.setIgnorable(true);
//            }
//            else
//            {
//                oShiftNode.setIgnorable(false);
//            }
//        }
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#toString()
//     */
//    public String toString()
//    {
//        SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
//        String sTemp = "+ShiftTree[" + _oDepartment.getName() + ": "
//            + oFormat.format(_oPeriodStart.getTime()) + "-" + oFormat.format(_oPeriodEnd.getTime()) + "]";
//
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            sTemp += "\n" + oShiftNode;
//        }
//
//        return sTemp;
//    }
//
//    /**
//     * Checkt ob ein gewisses Team an einem gewissen Tag einteilbar ist
//     * @param oDate
//     * @param oDriver
//     * @param oMedi
//     * @return    Ist das Team an dem Tag einteilbar?
//     **/
//    public boolean contains(GregorianCalendar oDate, Person oDriver, Person oMedi)
//    {
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            if (oShiftNode.getDate().equals(oDate))
//            {
//                return oShiftNode.contains(oDriver, oMedi);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Getter für _coShiftNodes.
//     * @return _coShiftNodes
//     **/
//    public Collection<ShiftNode> getShiftNodes()
//    {
//        return _coShiftNodes;
//    }
//
//    /**
//     * Setter für _coShiftNodes
//     * @param coShiftNodes
//     **/
//    public void setShiftNodes(List<ShiftNode> coShiftNodes)
//    {
//        _coShiftNodes = coShiftNodes;
//    }
//
//    /**
//     * Getter für _oDepartment.
//     * @return _oDepartment
//     **/
//    public Department getDepartment()
//    {
//        return _oDepartment;
//    }
//
//    /**
//     * Setter für _oDepartment
//     * @param oDepartment
//     **/
//    public void setDepartment(Department oDepartment)
//    {
//        _oDepartment = oDepartment;
//    }
//
//    /**
//     * Getter für _oPeriodStart.
//     * @return _oPeriodStart
//     **/
//    public GregorianCalendar getPeriodStart()
//    {
//        return _oPeriodStart;
//    }
//
//    /**
//     * Setter für _oPeriodStart
//     * @param oPeriodStart
//     **/
//    public void setPeriodStart(GregorianCalendar oPeriodStart)
//    {
//        _oPeriodStart = oPeriodStart;
//    }
//
//    /**
//     * Getter für _oPeriodEnd.
//     * @return _oPeriodEnd
//     **/
//    public GregorianCalendar getPeriodEnd()
//    {
//        return _oPeriodEnd;
//    }
//
//    /**
//     * Setter für _oPeriodEnd
//     * @param oPeriodEnd
//     **/
//    public void setPeriodEnd(GregorianCalendar oPeriodEnd)
//    {
//        _oPeriodEnd = oPeriodEnd;
//    }
//
//    /**
//     * Gibt ShiftNode einer gewissen Schicht zurück
//     * @param oDate     Datum
//     * @param oShift    Tagschicht/ Nachtschicht
//     * @return    BESCHREIBUNG_EINFUEGEN
//     **/
//    public ShiftNode getShiftNode(GregorianCalendar oDate, Shifts oShift)
//    {
//        for (ShiftNode oShiftNode : _coShiftNodes)
//        {
//            if (oShift.equals(oShiftNode.getShift()))
//            {
//                if (oDate.equals(oShiftNode.getDate()))
//                {
//                    return oShiftNode;
//                }
//            }
//        }
//
//        return null;
//    }
//}