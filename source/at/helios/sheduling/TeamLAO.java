package at.helios.sheduling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import alice.tuprolog.Prolog;
import alice.tuprolog.PrologException;
import alice.tuprolog.SolveInfo;
import at.helios.model.Person;
import at.helios.sheduling.shiftcube.DriverIdentifier;
import at.helios.sheduling.shiftcube.MediIdentifier;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * LAO für Teams
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   10.11.2008 Erstkommentierung
 * </PRE>
 **/
public class TeamLAO
{
    private static long         _lPrologTime                = 0;
    private long                lTemp                      = 0;
    private Prolog              _oPrologEngine;
    private static final String PROLOG_GET_NEFTEAM_BY_DATE = "neff_nefs_date(NEFF,NEFS,d%D1%%D2%).";
    private static final String PROLOG_GET_RTWTEAM_BY_DATE = "rtwf_rtws_date(RTWF,RTWS,d%D1%%D2%).";
    private static final String PROLOG_GET_STBTEAM_BY_DATE = "stbf_stbs_date(STBF,STBS,d%D1%%D2%).";

    /**
     * Konstruktor mit Feldern
     * @param oPrologEngine
     **/
    public TeamLAO(Prolog oPrologEngine)
    {
        _oPrologEngine = oPrologEngine;
    }

    void startTimer()
    {
        lTemp = System.currentTimeMillis();
    }

    void stopTimer()
    {
        _lPrologTime += System.currentTimeMillis() - lTemp;
    }

//    /**
//     * Gibt alle möglichen NEF-Teams zu einem bestimmten Datum aus
//     * @param oDate
//     * @param oShift
//     * @return alle möglichen Teams
//     * @throws PrologException
//     **/
//    public List<DriverNode> getNEFTeamsByDate(GregorianCalendar oDate, Shifts oShift) throws PrologException
//    {
//        List<DriverNode> coDriverNodes = new ArrayList<DriverNode>();
//        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
//        String sStatement = PROLOG_GET_NEFTEAM_BY_DATE.replaceAll("%D1%", oFormat.format(oDate.getTime()));
//
//        if (oShift == Shifts.nightshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "n");
//        }
//        else if (oShift == Shifts.dayshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "d");
//        }
//
//        startTimer();
//        SolveInfo oInfo = _oPrologEngine.solve(sStatement);
//        stopTimer();
//
//        if (oInfo.isSuccess())
//        {
//            String sDriverId = oInfo.getVarValue("NEFF").toString().substring(1);
//            String sMediId = oInfo.getVarValue("NEFS").toString().substring(1);
//
//            DriverNode oDriverNode = new DriverNode(Person.getPersonById(Integer.parseInt(sDriverId)),
//                Teams.nef);
//            oDriverNode.addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//            coDriverNodes.add(oDriverNode);
//
//            while (_oPrologEngine.hasOpenAlternatives())
//            {
//                startTimer();
//                oInfo = _oPrologEngine.solveNext();
//                stopTimer();
//                if (oInfo.isSuccess())
//                {
//                    sDriverId = oInfo.getVarValue("NEFF").toString().toString().substring(1);
//                    sMediId = oInfo.getVarValue("NEFS").toString().substring(1);
//                    Person oDriver = Person.getPersonById(Integer.parseInt(sDriverId));
//                    boolean bNewDriver = true;
//
//                    for (DriverNode oDriverNodeTemp : coDriverNodes)
//                    {
//                        if (oDriverNodeTemp.getDriver().equals(oDriver))
//                        {
//                            oDriverNodeTemp.addMediNode(new MediNode(Person.getPersonById(Integer
//                                .parseInt(sMediId))));
//                            bNewDriver = false;
//                            break;
//                        }
//                    }
//
//                    if (bNewDriver)
//                    {
//                        oDriverNode = new DriverNode(oDriver, Teams.nef);
//                        oDriverNode
//                            .addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//                        coDriverNodes.add(oDriverNode);
//                    }
//
//                }
//            }
//        }
//
//        return coDriverNodes;
//    }

//    /**
//     * Gibt alle möglichen RTW-Teams zu einem bestimmten Datum aus
//     * @param oDate
//     * @param oShift
//     * @return alle möglichen Teams
//     * @throws PrologException
//     **/
//    public List<DriverNode> getRTWTeamsByDate(GregorianCalendar oDate, Shifts oShift) throws PrologException
//    {
//        List<DriverNode> coDriverNodes = new ArrayList<DriverNode>();
//        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
//        String sStatement = PROLOG_GET_RTWTEAM_BY_DATE.replaceAll("%D1%", oFormat.format(oDate.getTime()));
//
//        if (oShift == Shifts.nightshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "n");
//        }
//        else if (oShift == Shifts.dayshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "d");
//        }
//
//        startTimer();
//        SolveInfo oInfo = _oPrologEngine.solve(sStatement);
//        stopTimer();
//
//        if (oInfo.isSuccess())
//        {
//            String sDriverId = oInfo.getVarValue("RTWF").toString().substring(1);
//            String sMediId = oInfo.getVarValue("RTWS").toString().substring(1);
//
//            DriverNode oDriverNode = new DriverNode(Person.getPersonById(Integer.parseInt(sDriverId)),
//                Teams.rtw);
//            oDriverNode.addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//            coDriverNodes.add(oDriverNode);
//
//            while (_oPrologEngine.hasOpenAlternatives())
//            {
//                startTimer();
//                oInfo = _oPrologEngine.solveNext();
//                stopTimer();
//                if (oInfo.isSuccess())
//                {
//                    sDriverId = oInfo.getVarValue("RTWF").toString().toString().substring(1);
//                    sMediId = oInfo.getVarValue("RTWS").toString().substring(1);
//                    Person oDriver = Person.getPersonById(Integer.parseInt(sDriverId));
//                    boolean bNewDriver = true;
//
//                    for (DriverNode oDriverNodeTemp : coDriverNodes)
//                    {
//                        if (oDriverNodeTemp.getDriver().equals(oDriver))
//                        {
//                            oDriverNodeTemp.addMediNode(new MediNode(Person.getPersonById(Integer
//                                .parseInt(sMediId))));
//                            bNewDriver = false;
//                            break;
//                        }
//                    }
//
//                    if (bNewDriver)
//                    {
//                        oDriverNode = new DriverNode(oDriver, Teams.rtw);
//                        oDriverNode
//                            .addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//                        coDriverNodes.add(oDriverNode);
//                    }
//
//                }
//            }
//        }
//
//        return coDriverNodes;
//    }

//    /**
//     * Gibt alle möglichen Bereitschaft-Teams zu einem bestimmten Datum aus
//     * @param oDate
//     * @param oShift
//     * @return alle möglichen Teams
//     * @throws PrologException
//     **/
//    public List<DriverNode> getSTBTeamsByDate(GregorianCalendar oDate, Shifts oShift) throws PrologException
//    {
//        List<DriverNode> coDriverNodes = new ArrayList<DriverNode>();
//        SimpleDateFormat oFormat = new SimpleDateFormat("ddMMyyyy");
//        String sStatement = PROLOG_GET_STBTEAM_BY_DATE.replaceAll("%D1%", oFormat.format(oDate.getTime()));
//
//        if (oShift == Shifts.nightshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "n");
//        }
//        else if (oShift == Shifts.dayshift)
//        {
//            sStatement = sStatement.replaceAll("%D2%", "d");
//        }
//
//        startTimer();
//        SolveInfo oInfo = _oPrologEngine.solve(sStatement);
//        stopTimer();
//
//        if (oInfo.isSuccess())
//        {
//            String sDriverId = oInfo.getVarValue("STBF").toString().substring(1);
//            String sMediId = oInfo.getVarValue("STBS").toString().substring(1);
//
//            DriverNode oDriverNode = new DriverNode(Person.getPersonById(Integer.parseInt(sDriverId)),
//                Teams.standby);
//            oDriverNode.addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//            coDriverNodes.add(oDriverNode);
//
//            while (_oPrologEngine.hasOpenAlternatives())
//            {
//                startTimer();
//                oInfo = _oPrologEngine.solveNext();
//                stopTimer();
//                if (oInfo.isSuccess())
//                {
//                    sDriverId = oInfo.getVarValue("STBF").toString().toString().substring(1);
//                    sMediId = oInfo.getVarValue("STBS").toString().substring(1);
//                    Person oDriver = Person.getPersonById(Integer.parseInt(sDriverId));
//                    boolean bNewDriver = true;
//
//                    for (DriverNode oDriverNodeTemp : coDriverNodes)
//                    {
//                        if (oDriverNodeTemp.getDriver().equals(oDriver))
//                        {
//                            oDriverNodeTemp.addMediNode(new MediNode(Person.getPersonById(Integer
//                                .parseInt(sMediId))));
//                            bNewDriver = false;
//                            break;
//                        }
//                    }
//
//                    if (bNewDriver)
//                    {
//                        oDriverNode = new DriverNode(oDriver, Teams.standby);
//                        oDriverNode
//                            .addMediNode(new MediNode(Person.getPersonById(Integer.parseInt(sMediId))));
//                        coDriverNodes.add(oDriverNode);
//                    }
//
//                }
//            }
//        }
//
//        return coDriverNodes;
//    }
    
    public boolean isPossible(ShiftIdentifier oShiftIdentifier, DriverIdentifier oDriverIdentifier, MediIdentifier oMediIdentifier, Teams oTeamType) throws PrologException
    {
        String sStatement = "";
        //TODO: blalbla, andere methoden refactoren
        switch (oTeamType)
        {
            case nef:
                sStatement = "neff_nefs_date(p%P1%,p%P2%,%D1%).";
                break;
            case rtw:
                sStatement = "rtwf_rtws_date(p%P1%,p%P2%,%D1%).";
                break;
            case standby:
                sStatement = "stbf_stbs_date(p%P1%,p%P2%,%D1%).";
                break;
            default:
                throw new RuntimeException("No valid Teamtype specified.");
        }
        
        sStatement = sStatement.replaceAll("%P1%", oDriverIdentifier.getDriver().getPersonId()+"");
        sStatement = sStatement.replaceAll("%P2%", oMediIdentifier.getMedi().getPersonId()+"");
        sStatement = sStatement.replaceAll("%D1%", oShiftIdentifier.getShortName());
        
   
        
        return _oPrologEngine.solve(sStatement).isSuccess();

    }
    
//    public ArrayList<DriverNode> getDrivers(Teams oTeams) throws PrologException
//    {
//        ArrayList<DriverNode> coDriverNodes = new ArrayList<DriverNode>();
//        String sStatement;
//
//        if(oTeams == Teams.nef)
//        {
//            sStatement = "neff(X).";
//        }
//        else if(oTeams == Teams.rtw)
//        {
//            sStatement = "rtwf(X).";
//        }
//        else
//        {
//            sStatement = "stbf(X).";
//        }
//        
//        SolveInfo oInfo = _oPrologEngine.solve(sStatement);
//        
//        if (oInfo.isSuccess())
//        {
//            String sDriverId = oInfo.getVarValue("X").toString().substring(1);
//            DriverNode oDriverNode = new DriverNode(Person.getPersonById(Integer.parseInt(sDriverId)),oTeams);
//            coDriverNodes.add(oDriverNode);
//            while (_oPrologEngine.hasOpenAlternatives())
//            {
//                startTimer();
//                oInfo = _oPrologEngine.solveNext();
//                stopTimer();
//                if (oInfo.isSuccess())
//                {
//                    sDriverId = oInfo.getVarValue("X").toString().toString().substring(1);
//                    oDriverNode = new DriverNode(Person.getPersonById(Integer.parseInt(sDriverId)),oTeams);
//                    coDriverNodes.add(oDriverNode);
//                }
//            }
//        }
//        return coDriverNodes;
//    }
//    
//    public ArrayList<MediNode> getMedis(Teams oTeams) throws PrologException
//    {
//        ArrayList<MediNode> coMediNodes = new ArrayList<MediNode>();
//        String sStatement;
//
//        if(oTeams == Teams.nef)
//        {
//            sStatement = "nefs(X).";
//        }
//        else if(oTeams == Teams.rtw)
//        {
//            sStatement = "rtws(X).";
//        }
//        else
//        {
//            sStatement = "stbs(X).";
//        }
//        
//        SolveInfo oInfo = _oPrologEngine.solve(sStatement);
//        
//        if (oInfo.isSuccess())
//        {
//            String sDriverId = oInfo.getVarValue("X").toString().substring(1);
//            MediNode oMediNode = new MediNode(Person.getPersonById(Integer.parseInt(sDriverId)));
//            coMediNodes.add(oMediNode);
//            while (_oPrologEngine.hasOpenAlternatives())
//            {
//                startTimer();
//                oInfo = _oPrologEngine.solveNext();
//                stopTimer();
//                if (oInfo.isSuccess())
//                {
//                    sDriverId = oInfo.getVarValue("X").toString().toString().substring(1);
//                    oMediNode = new MediNode(Person.getPersonById(Integer.parseInt(sDriverId)));
//                    coMediNodes.add(oMediNode);
//                }
//            }
//        }
//        return coMediNodes;
//    }

    public static long getPrologTime()
    {
        return _lPrologTime;
    }
}