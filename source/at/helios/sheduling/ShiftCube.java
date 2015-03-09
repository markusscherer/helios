package at.helios.sheduling;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.PrologException;
import alice.tuprolog.Theory;
import at.helios.calendar.helper.DateHelper;
import at.helios.calendar.helper.HolidayHelper;
import at.helios.common.TrainingHelper;
import at.helios.model.Department;
import at.helios.model.Person;
import at.helios.model.Shift;
import at.helios.model.Training;
import at.helios.sheduling.shiftcube.DriverIdentifier;
import at.helios.sheduling.shiftcube.MediIdentifier;
import at.helios.sheduling.shiftcube.ScheduledTeam;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * Alternativer Ansatz zur Einteilung 
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   19.04.2010 Neuerstellung
 * </PRE>
 */
public class ShiftCube
{
    private static final String                PROLOG_DATE_DAY_SHIFT    = "date_day_shift(d%D1%%D2%,%D3%,%S1%).";

    private boolean                            _abRawCube[][][];
    private List<DriverIdentifier>             _coDriverIdentifiers;
    private List<MediIdentifier>               _coMediIdentifiers;
    private List<ShiftIdentifier>              _coShiftIdentifiers;
    private Calendar                           _oPeriodEnd;
    private Calendar                           _oPeriodStart;
    private Department                         _oDepartment;
    private Teams                              _oTeamType;
    private int                                _iTeamCount;
    private HolidayHelper                      _oHolidayHelper;
    private Prolog                             _oPrologEngine;

    private ShiftIdentifier                    _oCurrentShiftIdentifier = null;

    private List<ScheduledTeam>                _coScheduledTeams;
    private List<Shift>                        _coShifts;

    private final Comparator<ShiftIdentifier>  _oShiftComparator        = new Comparator<ShiftIdentifier>()
    {
        @Override
        public int compare(ShiftIdentifier oShiftIdentifier1, ShiftIdentifier oShiftIdentifier2)
        {
            boolean bScheduled1 = oShiftIdentifier1.isScheduled();
            boolean bScheduled2 = oShiftIdentifier2.isScheduled();

            if (bScheduled1 && bScheduled2)
            {
                if (oShiftIdentifier1.getDate().after(oShiftIdentifier2.getDate()))
                {
                    return 1;
                }
                else if (oShiftIdentifier2.getDate().after(oShiftIdentifier1.getDate()))
                {
                    return -1;
                }
                else
                {
                    if (oShiftIdentifier1.getShift() == Shifts.nightshift && oShiftIdentifier2.getShift() == Shifts.dayshift)
                    {
                        return -1;
                    }
                    else if (oShiftIdentifier2.getShift() == Shifts.nightshift && oShiftIdentifier1.getShift() == Shifts.dayshift)
                    {
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                 }
            }
            else if (bScheduled1)
            {
                return -3;//Integer.MIN_VALUE;
            }
            else if (bScheduled2)
            {
                return 3;//Integer.MAX_VALUE;
            }
                int iMin1 = Integer.MAX_VALUE;
                int iMin2 = Integer.MAX_VALUE;
                for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
                {
                    for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
                    {
                        int iProduct1 = getProduct(oShiftIdentifier1, oDriverIdentifier, oMediIdentifier);
                        if (iProduct1 < iMin1 && iProduct1 != 0)
                        {
                            iMin1 = iProduct1;
                        }
                        int iProduct2 = getProduct(oShiftIdentifier2, oDriverIdentifier, oMediIdentifier);
                        if (iProduct2 < iMin2 && iProduct2 != 0)
                        {
                            iMin2 = iProduct2;
                        }
                    }
                }
                
                //TODO: aufpassen oO
                return iMin1 - iMin2;
            }
        };

    private final Comparator<DriverIdentifier> _oDriverComparator       = new Comparator<DriverIdentifier>()
    {
        @Override
        public int compare(DriverIdentifier oDriverIdentifier1, DriverIdentifier oDriverIdentifier2)
        {
            //            int iSum1 = getSum(_oCurrentShiftIdentifier, oDriverIdentifier1);
            //            int iSum2 = getSum(_oCurrentShiftIdentifier, oDriverIdentifier2);

            int iMin1 = Integer.MAX_VALUE;
            int iMin2 = Integer.MAX_VALUE;

            //if(oDriverIdentifier1.getDriver().getEMTNumber().equals("3900") || oDriverIdentifier2.getDriver().getEMTNumber().equals("3900")) System.err.println("ACHTUNG: ");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                int iProduct1 = getProduct(_oCurrentShiftIdentifier,oDriverIdentifier1,oMediIdentifier);
                if (iProduct1 < iMin1 && iProduct1 != 0)
                {
                    iMin1 = iProduct1;
                }
                //if(oDriverIdentifier1.getDriver().getEMTNumber().equals("3900") || oDriverIdentifier2.getDriver().getEMTNumber().equals("3900")) System.err.println("iProduct1:\t"+iProduct1+"\tiMin1:\t" + iMin1 + "\t" + oDriverIdentifier1.getDriver().getEMTNumber()+"\t"+"\t"+oMediIdentifier.getMedi().getEMTNumber());
                int iProduct2 = getProduct(_oCurrentShiftIdentifier,oDriverIdentifier2,oMediIdentifier);
                if (iProduct2 < iMin2 && iProduct2 != 0)
                {
                    iMin2 = iProduct2;
                }
                //if(oDriverIdentifier1.getDriver().getEMTNumber().equals("3900") || oDriverIdentifier2.getDriver().getEMTNumber().equals("3900")) System.err.println("iProduct2:\t"+iProduct2+"\tiMin2:\t" + iMin2 + "\t" + oDriverIdentifier2.getDriver().getEMTNumber()+"\t"+"\t"+oMediIdentifier.getMedi().getEMTNumber());
            }

            iMin1 = iMin1 == Integer.MAX_VALUE ? 0 : iMin1;
            iMin2 = iMin2 == Integer.MAX_VALUE ? 0 : iMin2;

            if (iMin1 == 0 ^ iMin2 == 0) //das ist ein XOR
            {
                if (iMin1 == 0)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
            
            if(iMin1 != iMin2)
            {
                return iMin1 - iMin2;
            }
            
            //TODO if löschen
//            
//            float fRatio1 = (float)oMediIdentifier1.getMedi().getShifts().size()/(float)oMediIdentifier1.getMedi().getMaxShiftCount();
//            float fRatio2 = (float)oMediIdentifier2.getMedi().getShifts().size()/(float)oMediIdentifier2.getMedi().getMaxShiftCount();
//            
//            if(fRatio1 > fRatio2) return 1;
//            else if(fRatio1 < fRatio2) return -1;
            
            return 0;
        }
    };

    private final Comparator<MediIdentifier>   _oMediComparator         = new Comparator<MediIdentifier>()
    {
        @Override
        public int compare(MediIdentifier oMediIdentifier1, MediIdentifier oMediIdentifier2)
        {
            DriverIdentifier oDriverIdentifier = _coDriverIdentifiers.get(0);
            int iProduct1 = getProduct(_oCurrentShiftIdentifier, oDriverIdentifier, oMediIdentifier1);
            int iProduct2 = getProduct(_oCurrentShiftIdentifier, oDriverIdentifier, oMediIdentifier2);

            if (iProduct1 == 0 ^ iProduct2 == 0) //das ist ein XOR
            {
                if (iProduct1 == 0)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
            if(iProduct1 != iProduct2)
            {
                return iProduct1 - iProduct2;
            }
                        
            float fRatio1 = oMediIdentifier1.getMedi().getShiftRatio(_oCurrentShiftIdentifier.getDate().get(Calendar.MONTH), TrainingHelper.getMediTraining(_oTeamType));//(float)oMediIdentifier1.getMedi().getShifts().size()/(float)oMediIdentifier1.getMedi().getMaxShiftCount();
            float fRatio2 = oMediIdentifier2.getMedi().getShiftRatio(_oCurrentShiftIdentifier.getDate().get(Calendar.MONTH), TrainingHelper.getMediTraining(_oTeamType));//(float)oMediIdentifier2.getMedi().getShifts().size()/(float)oMediIdentifier2.getMedi().getMaxShiftCount();
            
            if(fRatio1 > fRatio2) return 1;
            else if(fRatio1 < fRatio2) return -1;
                        
            return oMediIdentifier1.getMedi().getMaxShiftCount() - oMediIdentifier2.getMedi().getMaxShiftCount();
                
            //return getSum(_oCurrentShiftIdentifier, oMediIdentifier2) - getSum(_oCurrentShiftIdentifier, oMediIdentifier1);
            /*
            int iMin1 = Integer.MAX_VALUE;
            int iMin2 = Integer.MAX_VALUE;
            for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
            {
                for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
                {
                    int iProduct1 = getProduct(oShiftIdentifier,oDriverIdentifier,oMediIdentifier1);
                    if (iProduct1 < iMin1 && iProduct1 != 0)
                    {
                        iMin1 = iProduct1;
                    }
                    int iProduct2 = getProduct(oShiftIdentifier,oDriverIdentifier,oMediIdentifier2);
                    if (iProduct2 < iMin2 && iProduct2 != 0)
                    {
                        iMin2 = iProduct2;
                    }
                }
            }
            if(iMin1 == Integer.MAX_VALUE)
            {
                return Integer.MAX_VALUE;
            }
            return iMin1 - iMin2;*/
        }
    };

    private final Comparator<ScheduledTeam>    _oTeamComparator         = new Comparator<ScheduledTeam>()
                                                                        {
                                                                            @Override
                                                                            public int compare(
                                                                                ScheduledTeam oScheduledTeam1,
                                                                                ScheduledTeam oScheduledTeam2)
                                                                            {
                                                                                ShiftIdentifier oShiftIdentifier1 = oScheduledTeam1
                                                                                    .getShiftIdentifier();
                                                                                ShiftIdentifier oShiftIdentifier2 = oScheduledTeam2
                                                                                    .getShiftIdentifier();

                                                                                if (oShiftIdentifier1.getDate().after(
                                                                                    oShiftIdentifier2.getDate()))
                                                                                {
                                                                                    return 1;
                                                                                }
                                                                                else if (oShiftIdentifier2.getDate()
                                                                                    .after(oShiftIdentifier1.getDate()))
                                                                                {
                                                                                    return -1;
                                                                                }
                                                                                else
                                                                                {
                                                                                    if (oShiftIdentifier1.getShift() == Shifts.nightshift
                                                                                        && oShiftIdentifier2.getShift() == Shifts.dayshift)
                                                                                    {
                                                                                        return -1;
                                                                                    }
                                                                                    else if (oShiftIdentifier2
                                                                                        .getShift() == Shifts.nightshift
                                                                                        && oShiftIdentifier1.getShift() == Shifts.dayshift)
                                                                                    {
                                                                                        return 1;
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        return 0;
                                                                                    }
                                                                                }
                                                                            }
                                                                        };

    public ShiftCube(GregorianCalendar oPeriodStart, GregorianCalendar oPeriodEnd, Department oDepartment,
        Teams oTeamType, int iTeamCount, HolidayHelper oHolidayHelper, List<Shift> coShifts)
    {
        _oPeriodEnd = oPeriodEnd;
        _oPeriodStart = oPeriodStart;
        _oDepartment = oDepartment;
        _oTeamType = oTeamType;
        _iTeamCount = iTeamCount;
        _oHolidayHelper = oHolidayHelper;
        _oPrologEngine = new Prolog();
        _coDriverIdentifiers = new ArrayList<DriverIdentifier>();
        _coMediIdentifiers = new ArrayList<MediIdentifier>();
        _coShiftIdentifiers = new ArrayList<ShiftIdentifier>();
        _coScheduledTeams = new ArrayList<ScheduledTeam>();
        _coShifts = coShifts;
    }

    /**
     * Fügt Daten und Tage in die Prolog-Theorie eins
     * @throws InvalidTheoryException    Ungültige Theorie
     **/
    protected void addDates() throws InvalidTheoryException
    {
        String sDate = "";
        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");

        GregorianCalendar oTempCalendar = new GregorianCalendar();
        oTempCalendar = (GregorianCalendar)_oPeriodStart.clone();

        //Um Bugs bei Sommerzeit/Winterzeitumstellung zu verhindern
        _oPeriodEnd.set(Calendar.HOUR_OF_DAY, 12);
        oTempCalendar.set(Calendar.HOUR_OF_DAY, 12);

        boolean bTemp = true;

        int iNumShifts = 0;

        while (bTemp)
        {
            /* Um Bugs bei Sommerzeit/Winterzeitumstellung zu verhindern
             * z.B. bei bei Periode 01.10.2010-31.10.2010
             * anstatt bTemp = oTempCalendar.before(_oPeriodEnd);
             */
            //bTemp = oTempCalendar.before(_oPeriodEnd);
            //if(bTemp)
            //{
            //System.out.println(oFormat.format(oTempCalendar.getTime()) + "(" + oTempCalendar.getTimeInMillis() + ")  -  " + oFormat.format(_oPeriodEnd.getTime()) + "(" + _oPeriodEnd.getTimeInMillis() +")");
            bTemp = DateHelper.before(oTempCalendar, _oPeriodEnd);
            //bTemp = Math.abs(oTempCalendar.getTimeInMillis()-_oPeriodEnd.getTimeInMillis()) >= 1000*60*60;
            //}
            sDate = PROLOG_DATE_DAY_SHIFT.replaceAll("%D1%", oFormat.format(oTempCalendar.getTime()));

            int iDay = oTempCalendar.get(Calendar.DAY_OF_WEEK) - 2;
            if (iDay == -1)
            {
                iDay = 6;
            }
            sDate = sDate.replaceAll("%D3%", Days.values()[iDay].toString());

            if (iDay == 6 || _oHolidayHelper.isHoliday(oTempCalendar))
            {
                _oPrologEngine.addTheory(new Theory(sDate.replaceAll("%D2%", "n").replaceAll("%S1%",
                    Shifts.nightshift.toString())));

                GregorianCalendar oCalendar = (GregorianCalendar)oTempCalendar.clone();

                ShiftIdentifier oShiftIdentifier = new ShiftIdentifier(oCalendar, Shifts.nightshift, _iTeamCount,
                    _oTeamType, iNumShifts);
                _coShiftIdentifiers.add(oShiftIdentifier);
                _coShifts.add(new Shift(oCalendar, Shifts.nightshift, _iTeamCount));
                iNumShifts++;

                _oPrologEngine.addTheory(new Theory(sDate.replaceAll("%D2%", "d").replaceAll("%S1%",
                    Shifts.dayshift.toString())));
                oShiftIdentifier = new ShiftIdentifier(oCalendar, Shifts.dayshift, _iTeamCount, _oTeamType, iNumShifts);
                _coShiftIdentifiers.add(oShiftIdentifier);
                _coShifts.add(new Shift(oCalendar, Shifts.dayshift, _iTeamCount));
                iNumShifts++;
            }
            else if (iDay <= 5)
            {
                sDate = sDate.replaceAll("%D2%", "n");
                sDate = sDate.replaceAll("%S1%", Shifts.nightshift.toString());
                
                GregorianCalendar oCalendar = (GregorianCalendar)oTempCalendar.clone();

                _oPrologEngine.addTheory(new Theory(sDate));

                ShiftIdentifier oShiftIdentifier = new ShiftIdentifier(oCalendar,
                    Shifts.nightshift, _iTeamCount, _oTeamType, iNumShifts);
                _coShiftIdentifiers.add(oShiftIdentifier);
                _coShifts.add(new Shift(oCalendar, Shifts.nightshift, _iTeamCount));
                iNumShifts++;
            }

            long lTime = oTempCalendar.getTimeInMillis();
            lTime += 24 * 60 * 60 * 1000;
            oTempCalendar.setTimeInMillis(lTime);
        }
    }

    /**
     * Fügt Personen in die Prolog-Theorie ein
     * @throws InvalidTheoryException    Ungültige Theorie
     **/
    protected void addPersons() throws InvalidTheoryException
    {
        Training oDriverTraining = TrainingHelper.getDriverTraining(_oTeamType);
        Training oMediTraining = TrainingHelper.getMediTraining(_oTeamType);

        //System.out.println(Training.getTrainingById(iDriverTraining) + "  " + Training.getTrainingById(iMediTraining));
        System.out.println(oDriverTraining + "  " + oMediTraining);

        int iNumDrivers = 0;
        int iNumMedis = 0;

        for (Person oPerson : Person.getPersonsByDepartment(_oDepartment))
        {
            if (oPerson.getTrainings().containsKey(oDriverTraining)
                || oPerson.getTrainings().containsKey(oMediTraining))
            {
                //System.out.println(oPerson + "\n");
                if (oPerson.getTrainings().containsKey(oDriverTraining))
                {
                    _coDriverIdentifiers.add(new DriverIdentifier(oPerson, _oTeamType, iNumDrivers));
                    iNumDrivers++;
                }
                if (oPerson.getTrainings().containsKey(oMediTraining))
                {
                    _coMediIdentifiers.add(new MediIdentifier(oPerson, _oTeamType, iNumMedis));
                    iNumMedis++;
                }
                _oPrologEngine.addTheory(new Theory(oPerson.getPrologStatement()));
            }

        }
    }

    public void init()
    {
        try
        {
            _oPrologEngine.setTheory(new Theory(_oDepartment.getClass().getClassLoader().getResourceAsStream(
                "resources/scheduler_logic.pro")));
            addPersons();
            addDates();

            int iNumDates = _coShiftIdentifiers.size();
            int iNumDrivers = _coDriverIdentifiers.size();
            int iNumMedis = _coMediIdentifiers.size();

            _abRawCube = new boolean[iNumDates][iNumDrivers][iNumMedis];

            System.out.println(iNumDates + "  " + iNumDrivers + "  " + iNumMedis);

            //            initRawCube();

            //            printShiftDriverMatrix();
            //            printShiftMediMatrix();
            //            printDriverMediMatrix();

            //            schedule();

        }
        catch (InvalidTheoryException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (PrologException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @throws PrologException
     */
    private void initRawCube() throws PrologException
    {
        TeamLAO oTeamLAO = new TeamLAO(_oPrologEngine);
        for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            //            System.out.println(oShiftIdentifier.getShortName());
            for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            {
                //                System.out.print(oDriverIdentifier.getDriver().getEMTNumber() + "  ");
                for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
                {
                    boolean bTemp;
                    _abRawCube[oShiftIdentifier.getIndex()][oDriverIdentifier.getIndex()][oMediIdentifier.getIndex()] = bTemp = oTeamLAO
                        .isPossible(oShiftIdentifier, oDriverIdentifier, oMediIdentifier, _oTeamType);

                    //                    System.out.print(bTemp ? "O" : "X");
                }
                //                System.out.println();
            }
            //            System.out.println("\n");
        }
    }

    /**
     * TODO COMMENT
     */
    public void printDriverMediMatrix()
    {
        System.out.print("\t");
        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            System.out.print(oMediIdentifier.getMedi().getEMTNumber() + "\t");
        System.out.println();
        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            System.out.print(oDriverIdentifier.getDriver().getEMTNumber() + "\t");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                System.out.print(this.getSum(oDriverIdentifier, oMediIdentifier) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * TODO COMMENT
     */
    public void printSlice(ShiftIdentifier oShiftIdentifier)
    {        
        for(int i = 0; i < 4; i++)
        {
            System.out.print("      ");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
                System.out.print(oMediIdentifier.getMedi().getEMTNumber().substring(i,i+1) + " ");
            System.out.println();
        }        
        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            System.out.print(oDriverIdentifier.getDriver().getEMTNumber() + "  ");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                System.out.print(_abRawCube[oShiftIdentifier.getIndex()][oDriverIdentifier.getIndex()][oMediIdentifier.getIndex()] ? "X " : "O ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printDriverMediProductMatrix()
    {
        System.out.println(_oCurrentShiftIdentifier.getShortName());
        System.out.print("\t");
        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            System.out.print(oMediIdentifier.getMedi().getEMTNumber() + "\t");
        System.out.println();
        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            System.out.print(oDriverIdentifier.getDriver().getEMTNumber() + "\t");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                System.out.print(this.getProduct(_oCurrentShiftIdentifier, oDriverIdentifier, oMediIdentifier) + "\t");
//                System.out.print(getSum(_oCurrentShiftIdentifier, oDriverIdentifier)+"*");
//                System.out.print(getSum(_oCurrentShiftIdentifier, oMediIdentifier)+"*");
//                System.out.print(getSum(oDriverIdentifier, oMediIdentifier)+"\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * TODO COMMENT
     */
    public void printShiftMediMatrix()
    {
        ShiftIdentifier oTempShiftIdentifier = _oCurrentShiftIdentifier;
        System.out.print("\t\t");
        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            System.out.print(oMediIdentifier.getMedi().getEMTNumber() + "\t");
        System.out.println();
        for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            _oCurrentShiftIdentifier = oShiftIdentifier;
            System.out.print((oShiftIdentifier.isScheduled() ? "*" : "") + oShiftIdentifier.getShortName() + "\t");
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                System.out.print(this.getSum(oShiftIdentifier, oMediIdentifier) + "\t");
            }
            System.out.println();
        }
        System.out.println();
        _oCurrentShiftIdentifier = oTempShiftIdentifier;
    }

    /**
     * TODO COMMENT
     */
    public void printShiftDriverMatrix()
    {
        ShiftIdentifier oTempShiftIdentifier = _oCurrentShiftIdentifier;
        System.out.print("\t\t\t");
        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            System.out.print(oDriverIdentifier.getDriver().getEMTNumber() + "\t");
        System.out.println();
        for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            _oCurrentShiftIdentifier = oShiftIdentifier;
            System.out.print((oShiftIdentifier.isScheduled() ? "*" : "") + oShiftIdentifier.getShortName() + "\t");
            for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            {
                System.out.print(this.getSum(oShiftIdentifier, oDriverIdentifier) + "\t\t");
            }
            System.out.println();
        }
        System.out.println();
        _oCurrentShiftIdentifier = oTempShiftIdentifier;
    }

    public int getSum(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier)
    {
        if (!oDriverIdentifier.checkShiftCount(oShiftIdentifier)
            || !oDriverIdentifier.checkShiftInterval(oShiftIdentifier))
        {
            return 0;
        }

        int iSum = 0;
        int iShiftIndex = oShiftIdentifier.getIndex();
        int iDriverIndex = oDriverIdentifier.getIndex();

        for (int i = 0; i < _coMediIdentifiers.size(); i++)
        {
            if (_abRawCube[iShiftIndex][iDriverIndex][i])
            {
                iSum++;
            }
        }
        return iSum;
    }

    public int getSum(ShiftIdentifier oShiftIdentifier, MediIdentifier oMediIdentifier)
    {
        if (!oMediIdentifier.checkShiftCount(oShiftIdentifier) || !oMediIdentifier.checkShiftInterval(oShiftIdentifier))
        {
            return 0;
        }

        int iSum = 0;
        int iShiftIndex = oShiftIdentifier.getIndex();
        int iMediIndex = oMediIdentifier.getIndex();

        for (int i = 0; i < _coDriverIdentifiers.size(); i++)
        {
            if (_abRawCube[iShiftIndex][i][iMediIndex])
            {
                iSum++;
            }
        }
        return iSum;
    }

    public int getSum(DriverIdentifier oDriverIdentifier, MediIdentifier oMediIdentifier)
    {
        int iSum = 0;
        int iDriverIndex = oDriverIdentifier.getIndex();
        int iMediIndex = oMediIdentifier.getIndex();

        for (int i = 0; i < _coShiftIdentifiers.size(); i++)
        {
            if (_abRawCube[i][iDriverIndex][iMediIndex])
            {
                iSum++;
            }
        }
        
        if(iSum == 0) return 0;
        
        int iMonth = _oCurrentShiftIdentifier.getDate().get(Calendar.MONTH);
        float fRatio1 = oMediIdentifier.getMedi().getShiftRatio(iMonth, TrainingHelper.getMediTraining(_oTeamType));
        float fRatio2 = oDriverIdentifier.getDriver().getShiftRatio(iMonth, TrainingHelper.getDriverTraining(_oTeamType));
        float fRatio = fRatio1 + fRatio2+.1f;
        float fGrade = 6;
        
        return (int)Math.ceil(((float)iSum * Math.pow(fRatio, fGrade)));
    }

    private int getProduct(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier,
        MediIdentifier oMediIdentifier)
    {
        return getSum(oShiftIdentifier, oDriverIdentifier) * getSum(oShiftIdentifier, oMediIdentifier)
            * getSum(oDriverIdentifier, oMediIdentifier);
    }

    private void scheduleTeam(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier,
        MediIdentifier oMediIdentifier, int iTeamNumber)
    {
        _coScheduledTeams.add(new ScheduledTeam(oShiftIdentifier, oDriverIdentifier, oMediIdentifier, _oTeamType,
            iTeamNumber));
        _coShifts.get(oShiftIdentifier.getIndex()).getTeam(iTeamNumber).schedule(oDriverIdentifier.getDriver(),
            oMediIdentifier.getMedi(), null);
    }

    private void schedule()
    {
        //        printShiftDriverMatrix();
        //        System.out.println("*+*+*+*+*+*+*");
        //        printShiftMediMatrix();
        //        System.out.println("\n");
        //        sortCube();

        //        printShiftDriverMatrix();
        //        System.out.println("*+*+*+*+*+*+*");
        //        printShiftMediMatrix();
        //        System.out.println("\n");

//        Collections.sort(_coShiftIdentifiers, _oShiftComparator);
//        
//        for(ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
//        {
//            _oCurrentShiftIdentifier = oShiftIdentifier;
//            System.out.println("üü+++üü");
//            printDriverMediProductMatrix();
//        }

        for (int i = 0; i < _coShiftIdentifiers.size(); i++)
        {
            _oCurrentShiftIdentifier = _coShiftIdentifiers.get(i);

//            System.out.println(i + "####");

            for (int j = 0; j < _oCurrentShiftIdentifier.getTeamCount(); j++)
            {
                sortCube();
                //printShiftMediMatrix();
                printDriverMediProductMatrix();
                //printValidCombinations();
                
                if(_oCurrentShiftIdentifier.getShortName().equals("d22052010n")) printSlice(_oCurrentShiftIdentifier);
                
                if (!_oCurrentShiftIdentifier.isScheduled(j))
                {
                    for(DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
                    {
                        boolean bBreak = false;
                        for(MediIdentifier oMediIdentifier : _coMediIdentifiers)
                        {
                            if(_abRawCube[_oCurrentShiftIdentifier.getIndex()][oDriverIdentifier.getIndex()][oMediIdentifier.getIndex()])
                            {
                                scheduleTeam(_coShiftIdentifiers.get(i), oDriverIdentifier, oMediIdentifier, j);
                                bBreak = true;
                                break;
                            }
                        }
                        if(bBreak) break;
                    }
                }
            }

            Collections.sort(_coShiftIdentifiers, _oShiftComparator);
            
            
//            System.out.println("uiuiuiuiuiu");
//            System.out.print("\t\t");
//            for(ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers) System.out.print(oShiftIdentifier.getShortName()+"\t");
//            System.out.println();
//            
//            for(ShiftIdentifier oShiftIdentifier1 : _coShiftIdentifiers)
//            {
//                System.out.print(oShiftIdentifier1.getShortName() + "\t");
//                for(ShiftIdentifier oShiftIdentifier2 : _coShiftIdentifiers)
//                {
//                    System.out.print(_oShiftComparator.compare(oShiftIdentifier1, oShiftIdentifier2) + "\t\t");
//                }
//                System.out.println();
//            }
            //if(i==26)break;
        }

//        printValidCombinations();
        
        System.out.println("++++++");
                Collections.sort(_coShiftIdentifiers, _oShiftComparator);
//                System.out.print("\t\t");
//                for(ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers) System.out.print(oShiftIdentifier.getShortName()+"\t");
//                System.out.println();
//                
//                for(ShiftIdentifier oShiftIdentifier1 : _coShiftIdentifiers)
//                {
//                    System.out.print(oShiftIdentifier1.getShortName() + "\t");
//                    for(ShiftIdentifier oShiftIdentifier2 : _coShiftIdentifiers)
//                    {
//                        System.out.print(_oShiftComparator.compare(oShiftIdentifier1, oShiftIdentifier2) + "\t\t");
//                    }
//                    System.out.println();
//                }
//                
        Collections.sort(_coScheduledTeams, _oTeamComparator);
        for (int i = 0; i < _coScheduledTeams.size(); i++)
        {
            System.out.println(_coScheduledTeams.get(i).toVerboseString());
        }
        
        for(DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            System.out.println("+ " + oDriverIdentifier.getDriver().getEMTNumber() + "(" +oDriverIdentifier.getDriver().getShifts().size()+ "/" +oDriverIdentifier.getDriver().getMaxShiftCount() +")");
        }
        
        System.out.println();
        
        for(MediIdentifier oMediIdentifier : _coMediIdentifiers)
        {
            System.out.println("- " + oMediIdentifier.getMedi().getEMTNumber() + "(" +oMediIdentifier.getMedi().getShifts().size()+ "/" +oMediIdentifier.getMedi().getMaxShiftCount() +")");
        }
        /*for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            System.out.println(oShiftIdentifier.getShortName());
            for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            {
                for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
                {
                    int iProduct = getProduct(oShiftIdentifier,oDriverIdentifier,oMediIdentifier);
                    int iSum = getSum(oShiftIdentifier, oDriverIdentifier) + getSum(oShiftIdentifier, oMediIdentifier)
                        + getSum(oDriverIdentifier, oMediIdentifier);
                    if (iProduct != 0)
                    {
                        System.out.println(oShiftIdentifier.getShortName() + " - " + oDriverIdentifier.getDriver().getEMTNumber()
                            + " - " + oMediIdentifier.getMedi().getEMTNumber() + ": SD " + getSum(oShiftIdentifier, oDriverIdentifier)
                            + " SM " + getSum(oShiftIdentifier, oMediIdentifier) + " DM " + getSum(oDriverIdentifier, oMediIdentifier) + " S "
                            + iSum + " P " + iProduct);
                    }
                }
                System.out.println();
            }
            System.out.println("\n");
        }*/
    }

    /**
     * 
     */
    private void sortCube()
    {
        //Collections.sort(_coShiftIdentifiers, _oShiftComparator);
        Collections.sort(_coDriverIdentifiers, _oDriverComparator);
        Collections.sort(_coMediIdentifiers, _oMediComparator);
    }

    /**
     * 
     */
    private void printValidCombinations()
    {
        for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            System.out.println(oShiftIdentifier.getShortName());
            for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            {
                for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
                {
                    //    int iProduct = getProduct(oShiftIdentifier,oDriverIdentifier,oMediIdentifier);
                    //    int iSum = getSum(oShiftIdentifier, oDriverIdentifier) + getSum(oShiftIdentifier, oMediIdentifier)
                    //        + getSum(oDriverIdentifier, oMediIdentifier);
                    if (_abRawCube[oShiftIdentifier.getIndex()][oDriverIdentifier.getIndex()][oMediIdentifier
                        .getIndex()])//(iProduct != 0)
                    {
                        System.out.println(oShiftIdentifier.getShortName() + " - "
                            + oDriverIdentifier.getDriver().getEMTNumber() + " - "
                            + oMediIdentifier.getMedi().getEMTNumber());// + ": SD " + getSum(oShiftIdentifier, oDriverIdentifier)
                        //    + " SM " + getSum(oShiftIdentifier, oMediIdentifier) + " DM " + getSum(oDriverIdentifier, oMediIdentifier) + " S "
                        //    + iSum + " P " + iProduct);
                    }
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    public List<DriverIdentifier> getDriverIdentifiers(ShiftIdentifier oShiftIdentifier, MediIdentifier oMediIdentifier)
    {
        int iShiftIndex = oShiftIdentifier.getIndex();
        int iMediIndex = oMediIdentifier.getIndex();

        List<DriverIdentifier> coDriverIdentifiers = new ArrayList<DriverIdentifier>();

        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            if (_abRawCube[iShiftIndex][oDriverIdentifier.getIndex()][iMediIndex])
            {
                coDriverIdentifiers.add(oDriverIdentifier);
            }
        }

        return coDriverIdentifiers;
    }

    public List<MediIdentifier> getMediIdentifiers(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier)
    {
        int iShiftIndex = oShiftIdentifier.getIndex();
        int iDriverIndex = oDriverIdentifier.getIndex();

        List<MediIdentifier> coMediIdentifiers = new ArrayList<MediIdentifier>();

        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
        {
            if (_abRawCube[iShiftIndex][iDriverIndex][oMediIdentifier.getIndex()])
            {
                coMediIdentifiers.add(oMediIdentifier);
            }
        }

        return coMediIdentifiers;
    }

    public Teams getTeamType()
    {
        return _oTeamType;
    }

    public int getTeamCount()
    {
        return _iTeamCount;
    }

    public void generateShiftCube()
    {
        try
        {
            init();
            initRawCube();
        }
        catch (PrologException e)
        {
            e.printStackTrace();
        }
    }

    public void schedulePeriod()
    {
        schedule();
    }

    //FIXME so schlecht muss das nicht sein
    public ShiftIdentifier getShiftIdentifier(GregorianCalendar oCalendar, Shifts oShiftType)
    {
        for (ShiftIdentifier oShiftIdentifier : _coShiftIdentifiers)
        {
            if (oShiftType.equals(oShiftIdentifier.getShift()))
            {
                if (oShiftIdentifier.getDate().get(Calendar.MONTH) == oCalendar.get(Calendar.MONTH)
                    && oShiftIdentifier.getDate().get(Calendar.DAY_OF_MONTH) == oCalendar.get(Calendar.DAY_OF_MONTH)
                    && oShiftIdentifier.getDate().get(Calendar.YEAR) == oCalendar.get(Calendar.YEAR))
                {
                    return oShiftIdentifier;
                }
            }
        }
        return null;
    }

    public Shift getShift(ShiftIdentifier oShiftIdentifier)
    {
        return _coShifts.get(oShiftIdentifier.getIndex());
    }

    public List<DriverIdentifier> getDriverIdentifiers(ShiftIdentifier oShiftIdentifier)
    {
        List<DriverIdentifier> coDriverIdentifiers = new ArrayList<DriverIdentifier>();

        int iShiftIndex = oShiftIdentifier.getIndex();

        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
            {
                if (_abRawCube[iShiftIndex][oDriverIdentifier.getIndex()][oMediIdentifier.getIndex()])
                {
                    if (!coDriverIdentifiers.contains(oDriverIdentifier))
                    {
                        coDriverIdentifiers.add(oDriverIdentifier);
                    }
                    break;
                }
            }
        }

        return coDriverIdentifiers;
    }

    public List<MediIdentifier> getMediIdentifiers(ShiftIdentifier oShiftIdentifier)
    {
        List<MediIdentifier> coMediIdentifiers = new ArrayList<MediIdentifier>();

        int iShiftIndex = oShiftIdentifier.getIndex();

        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
        {
            for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
            {
                if (_abRawCube[iShiftIndex][oDriverIdentifier.getIndex()][oMediIdentifier.getIndex()])
                {
                    if (!coMediIdentifiers.contains(oMediIdentifier))
                    {
                        coMediIdentifiers.add(oMediIdentifier);
                    }
                    break;
                }
            }
        }

        return coMediIdentifiers;
    }

    //FIXME: :'-(, langsam, schlechtes Datenmodell
    public MediIdentifier getMediIdentifierByPerson(Person oPerson)
    {
        for (MediIdentifier oMediIdentifier : _coMediIdentifiers)
        {
            if (oMediIdentifier.getMedi() == oPerson) return oMediIdentifier;
        }
        return null;
    }

    //FIXME: :'-(, langsam, schlechtes Datenmodell
    public DriverIdentifier getDriverIdentifierByPerson(Person oPerson)
    {
        for (DriverIdentifier oDriverIdentifier : _coDriverIdentifiers)
        {
            if (oDriverIdentifier.getDriver() == oPerson) return oDriverIdentifier;
        }
        return null;
    }
}
