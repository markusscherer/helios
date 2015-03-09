package at.helios.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import at.helios.common.TrainingHelper;
import at.helios.sheduling.Prologable;
import at.helios.sheduling.Rule;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * Bildet Person im Speicher ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 *         mas   08.01.2010 getPersonByEMTNumber hinzugefügt
 * </PRE>
 **/
public class Person implements Prologable
{

    private int                          _iPersonId;
    private String                       _sSurname;
    private String                       _sForename;
    private String                       _sEMTNumber;
    private String                       _sPassword;
    private String                       _sEmail;
    private int                          _iMaxShiftCount    = 0;
    private int                          _iMinShiftInterval = 0;
    private HashMap<ShiftIdentifier, Training> _coShifts          = new HashMap<ShiftIdentifier, Training>();
    private Collection<Rule>             _coRules           = new ArrayList<Rule>();
    private HashMap<Training, Integer>   _coTrainings       = new HashMap<Training, Integer>();
    private Collection<Department>       _coDepartments     = new HashSet<Department>();
    private static Map<Integer, Person>  moPersonCache      = new HashMap<Integer, Person>();
    private static Date oLastAccess = new Date();
    
    /**
     * Gibt Liste mir Probehelfern zurück
     * @return    Liste mit Probhelfern
     **/
    public static List<Person> getAdditionals()
    {
        oLastAccess = new Date();
        List<Person> coPersons = new ArrayList<Person>();

        for (Person oPerson : moPersonCache.values())
        {
            if (oPerson.getTrainings().keySet().contains(TrainingHelper.getAdditionalTraining()))
            {
                coPersons.add(oPerson);
            }
        }
        return coPersons;
    }

    /* (non-Javadoc)
     * @see at.helios.sheduling.Prologable#getPrologStatement()
     */
    public String getPrologStatement()
    {
        oLastAccess = new Date();
        String sStatement = "";

        for (Training oTraining : _coTrainings.keySet())
        {
            if (oTraining.getCode().equals("NEFF"))
            {
                sStatement += "neff(p" + _iPersonId + ").\n";
            }
            else if (oTraining.getCode().equals("NEFS"))
            {
                sStatement += "nefs(p" + _iPersonId + ").\n";
            }
            else if (oTraining.getCode().equals("RTWF"))
            {
                sStatement += "rtwf(p" + _iPersonId + ").\n";
            }
            else if (oTraining.getCode().equals("RTWS"))
            {
                sStatement += "rtws(p" + _iPersonId + ").\n";
            }
            else if (oTraining.getCode().equals("STBF"))
            {
                sStatement += "stbf(p" + _iPersonId + ").\n";
            }
            else if (oTraining.getCode().equals("STBS"))
            {
                sStatement += "stbs(p" + _iPersonId + ").\n";
            }
        }

        for (Rule oRule : _coRules)
        {
            sStatement += oRule.getPrologStatement() + "\n";
        }

        return sStatement;
    }

    /**
     * Getter für _sForename
     * @return    _sForename
     **/
    public String getSurname()
    {
        oLastAccess = new Date();
        return _sSurname;
    }

    /**
     * Setter für _sSurename
     * @param sSurname
     **/
    public void setSurname(String sSurname)
    {
        oLastAccess = new Date();
        _sSurname = sSurname;
    }

    /**
     * Getter für _sForename
     * @return    _sForename
     **/
    public String getForename()
    {
        oLastAccess = new Date();
        return _sForename;
    }

    /**
     * Setter für _sForename
     * @param sForename
     **/
    public void setForename(String sForename)
    {
        oLastAccess = new Date();
        _sForename = sForename;
    }

    /**
     * Getter für _sEMTNumber
     * @return    _sEMTNumber
     **/
    public String getEMTNumber()
    {
        oLastAccess = new Date();
        return _sEMTNumber;
    }

    /**
     * Setter für _sEMTNumber
     * @param sEMTNumber
     **/
    public void setEMTNumber(String sEMTNumber)
    {
        oLastAccess = new Date();
        _sEMTNumber = sEMTNumber;
    }

    /**
     * Getter für _iPersonId
     * @return    _iPersonId
     **/
    public int getPersonId()
    {
        oLastAccess = new Date();
        return _iPersonId;
    }

    /**
     * Getter für _sPassword.
     * @return _sPassword
     **/
    public String getPassword()
    {
        oLastAccess = new Date();
        return _sPassword;
    }

    /**
     * Getter für _sEmail.
     * @return _sEmail
     **/
    public String getEmail()
    {
        oLastAccess = new Date();
        return _sEmail;
    }

    /**
     * Setter für _sEmail
     * @param sEmail
     **/
    public void setEmail(String sEmail)
    {
        oLastAccess = new Date();
        _sEmail = sEmail;
    }

    /**
     * Setter für _sPassword
     * @param sPassword
     **/
    public void setPassword(String sPassword)
    {
        oLastAccess = new Date();
        _sPassword = sPassword;
    }

    /**
     * Setter für _iPersonId
     * @param iPersonId
     **/
    public void setPersonId(int iPersonId)
    {
        oLastAccess = new Date();
        _iPersonId = iPersonId;
        moPersonCache.put(iPersonId, this);
    }

    /**
     * Getter für _iMaxShiftCount.
     * @return _iMaxShiftCount
     **/
    public int getMaxShiftCount()
    {
        oLastAccess = new Date();
        return _iMaxShiftCount;
    }

    /**
     * Setter für _iMaxShiftCount
     * @param iMaxShiftCount
     **/
    public void setMaxShiftCount(int iMaxShiftCount)
    {
        oLastAccess = new Date();
        _iMaxShiftCount = iMaxShiftCount;
    }

    /**
     * Getter für _iMinShiftInterval.
     * @return _iMinShiftInterval
     **/
    public int getMinShiftInterval()
    {
        oLastAccess = new Date();
        return _iMinShiftInterval;
    }

    /**
     * Setter für _iMinShiftInterval
     * @param iMinShiftInterval
     **/
    public void setMinShiftInterval(int iMinShiftInterval)
    {
        oLastAccess = new Date();
        _iMinShiftInterval = iMinShiftInterval;
    }

    /**
     * Fügt der Person eine Regel hinzu
     * @param oRule
     **/
    public void addRule(Rule oRule)
    {
        oLastAccess = new Date();
        oRule.setRuleOwner(this);
        _coRules.add(oRule);
    }

    /**
     * Entfernt Regel aus Person
     * @param oRule    BESCHREIBUNG_EINFUEGEN
     **/
    public void removeRule(Rule oRule)
    {
        if (_coRules.contains(oRule))
        {
            _coRules.remove(oRule);
        }
    }

    /**
     * Fügt der Person eine Schicht hinzu
     * @param oShiftIdentifier
     **/
    public void addShift(ShiftIdentifier oShiftIdentifier, Training oTraining)
    {        
        _coShifts.put(oShiftIdentifier, oTraining);
    }

    /**
     * Entfernt Schicht aus Person
     * @param oShiftIdentifier    zu entfernende Schicht
     **/
    public void removeShift(ShiftIdentifier oShiftIdentifier)
    {
        _coShifts.remove(oShiftIdentifier);
    }

    /**
     * Entfernt Schicht eines gewissen Datums aus Person
     * @param oDate    bestimmtes Datum
     **/
    public void removeShiftByDate(GregorianCalendar oDate)
    {
        oLastAccess = new Date();
        ShiftIdentifier oTempShift = null;
        
        for (ShiftIdentifier oShift : _coShifts.keySet())
        {

            if (oShift.getDate().get(Calendar.MONTH) == oDate.get(Calendar.MONTH)
                && oShift.getDate().get(Calendar.DAY_OF_MONTH) == oDate.get(Calendar.DAY_OF_MONTH)
                && oShift.getDate().get(Calendar.YEAR) == oDate.get(Calendar.YEAR))
            {
                oTempShift = oShift;
            }
        }
        
        if(oTempShift != null)
        {
            _coShifts.remove(oTempShift);
        }
    }

    /**
     * Getter für _coShifts.
     * @return _coShifts
     **/
    public HashMap<ShiftIdentifier, Training> getShifts()
    {
        oLastAccess = new Date();
        return _coShifts;
    }

    /**
     * Getter für _coRules.
     * @return _coRules
     **/
    public Collection<Rule> getRules()
    {
        oLastAccess = new Date();
        return _coRules;
    }

    /**
     * Setter für _coRules
     * @param coRules
     **/
    public void setRules(Collection<Rule> coRules)
    {
        oLastAccess = new Date();
        _coRules = coRules;
    }

    /**
     * Getter für _coTrainings.
     * @return _coTrainings
     **/
    public HashMap<Training, Integer> getTrainings()
    {
        oLastAccess = new Date();
        return _coTrainings;
    }

    /**
     * Fügt der Person ein Training hinzu
     * @param oTraining
     * @param iMaxShiftCount
     **/
    public void addTraining(Training oTraining, Integer iMaxShiftCount)
    {
        oLastAccess = new Date();
        _coTrainings.put(oTraining, iMaxShiftCount);
    }

	/**
     * Löscht ein Training der Person
     * @param oTraining
     */
    public void removeTraining(Training oTraining)
    {
        _coTrainings.remove(oTraining);
    }

    /**
     * Getter für _coDepartments.
     * @return _coDepartments
     **/
    public Collection<Department> getDepartments()
    {
        oLastAccess = new Date();
        return _coDepartments;
    }

    /**
     * Setter für _coDepartments
     * @param coDepartments
     **/
    public void setDepartments(Collection<Department> coDepartments)
    {
        oLastAccess = new Date();
        _coDepartments = coDepartments;
    }

    /**
     * Fügt der Person ein Department hinzu
     * @param oDepartment    
     **/
    public void addDepartment(Department oDepartment)
    {
        oLastAccess = new Date();
        _coDepartments.add(oDepartment);
    }

    /**
     * Gibt eine Person nach Id aus dem Cache aus
     * @param iPersonId
     * @return    Person aus Cache
     **/
    public static Person getPersonById(int iPersonId)
    {
        oLastAccess = new Date();
        return moPersonCache.get(iPersonId);
    }

    /**
     * Gibt alle Personen eines Departments aus Cache aus
     * @param oDepartment
     * @return    Personen eines Departments
     **/
    public static List<Person> getPersonsByDepartment(Department oDepartment)
    {
        oLastAccess = new Date();
        List<Person> coPersons = new ArrayList<Person>();

        for (Person oPerson : moPersonCache.values())
        {
            if (oPerson.getDepartments().contains(oDepartment))
            {
                coPersons.add(oPerson);
            }
        }

        return coPersons;
    }
    
    public static Person getPersonByEMTNumber(String sEMTNumber)
    {
        oLastAccess = new Date();

        for (Person oPerson : moPersonCache.values())
        {
            if (oPerson.getEMTNumber().equals(sEMTNumber))
            {
                return oPerson;
            }
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String sTemp = "[Person] " + _sForename + " " + _sSurname + " " + _sEMTNumber;

        for (Rule oRule : _coRules)
        {
            sTemp += "\n\t" + oRule;
        }

        for (Department oDepartment : _coDepartments)
        {
            sTemp += "\n\t" + oDepartment;
        }

        for (Training oTraining : _coTrainings.keySet())
        {
            sTemp += "\n\t" + oTraining + "(" + _coTrainings.get(oTraining) + ")";
        }

        return sTemp;
    }
    
    public float getShiftRatio(int iMonth, Training oTraining)
    {
        if(_coTrainings.get(oTraining)== 0) return 0;
        
        float fDenominator = _coTrainings.get(oTraining);    
        float fNumerator = 0;
        
        //TODO evtl. besser machen
        if(_coTrainings.get(oTraining) == -1)
        {
            for(ShiftIdentifier oShift : _coShifts.keySet())
            {
                if(oShift.getDate().get(Calendar.MONTH) == iMonth)
                {
                    fNumerator++;
                }
            }
            return fNumerator/(float)_iMaxShiftCount;
        }
        
        for(ShiftIdentifier oShift : _coShifts.keySet())
        {
            if(oShift.getDate().get(Calendar.MONTH) == iMonth)
            {
                if(_coShifts.get(oShift).equals(oTraining))
                {
                    fNumerator++;
                }
            }
        }
        return fNumerator/fDenominator;
    }

    /**
     * Löscht statischen Personen-Cache
     **/
    public static void clearCache()
    {
        moPersonCache.clear();        
    }
    
    /**
     * Gibt den letzten Zugriff auf eine Person zurück
     * @return letzten Zugriff auf eine Person   
     **/
    public static Date getLastAccess()
    {
        return oLastAccess;
    }
}