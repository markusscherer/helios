package at.helios.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Bildet Ausbildung im Speicher ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class Training
{
    private int _iTrainingId;
    private String _sName;
    private String _sCode;
    
    private static Map<Integer, Training> moTrainingCache = new HashMap<Integer, Training>();

    /**
     * Konstruktor der Felder benutzt, Training wird gleich gecached
     * @param iTrainingId
     * @param sCode
     * @param sName
     **/
    public Training(int iTrainingId, String sCode, String sName)
    {
        _iTrainingId = iTrainingId;
        _sCode = sCode;
        _sName = sName;
        
        moTrainingCache.put(iTrainingId, this);
    }

    /**
     * Getter für _iTrainingId.
     * @return _iTrainingId
     **/
    public int getTrainingId()
    {
        return _iTrainingId;
    }
    
    /**
     * Setter für _iTrainingId
     * @param iTrainingId
     **/
    public void setTrainingId(int iTrainingId)
    {
        _iTrainingId = iTrainingId;
        moTrainingCache.put(iTrainingId, this);
    }
    
    /**
     * Getter für _sName.
     * @return _sName
     **/
    public String getName()
    {
        return _sName;
    }
    /**
     * Setter für _sName
     * @param sName
     **/
    public void setName(String sName)
    {
        _sName = sName;
    }
    /**
     * Getter für _sCode.
     * @return _sCode
     **/
    public String getCode()
    {
        return _sCode;
    }
    /**
     * Setter für _sCode
     * @param sCode
     **/
    public void setCode(String sCode)
    {
        _sCode = sCode;
    }
    
    /**
     * Gibt Training aus Cache zurück
     * @param iTrainingId
     * @return    Training
     **/
    public static Training getTrainingById(int iTrainingId)
    {     
        return moTrainingCache.get(iTrainingId);
    }
    
    /**
     * Gibt alle Ausbildungen zurück
     * @return    alle Ausbildungen
     **/
    public static Collection<Training>getTrainings()
    {
        return moTrainingCache.values();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[Training] " + _sCode;
    }

    /**
     * Löscht Ausbildungs-Cache
     **/
    public static void clearCache()
    {
        moTrainingCache.clear();        
    }
}
