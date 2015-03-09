package at.helios.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Bildet Department im Speicher ab
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class Department
{
    private int    _iDepartmentId;
    private String _sName;
    private int    _iNEFCount;
    private int    _iRTWCount;
    
    private static Map<Integer, Department> moDepartmentCache = new HashMap<Integer, Department>();   
    
    /**
     * Konstruktor mit Felder, Department wird gleich gecached
     * @param iDepartmentId
     * @param iNEFCount
     * @param iRTWCount
     * @param sName
     **/
    public Department(int iDepartmentId, String sName, int iNEFCount, int iRTWCount)
    {
        _iDepartmentId = iDepartmentId;
        _sName = sName;
        _iNEFCount = iNEFCount;
        _iRTWCount = iRTWCount;
        
        moDepartmentCache.put(iDepartmentId, this);
    }
    /**
     * Getter für _iDepartmentId.
     * @return _iDepartmentId
     **/
    public int getDepartmentId()
    {
        return _iDepartmentId;
    }
    /**
     * Setter für _iDepartmentId
     * @param iDepartmentId
     **/
    public void setDepartmentId(int iDepartmentId)
    {
        _iDepartmentId = iDepartmentId;
        moDepartmentCache.put(_iDepartmentId, this);
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
     * Getter für _iNEFCount.
     * @return _iNEFCount
     **/
    public int getNEFCount()
    {
        return _iNEFCount;
    }
    /**
     * Setter für _iNEFCount
     * @param iNEFCount
     **/
    public void setNEFCount(int iNEFCount)
    {
        _iNEFCount = iNEFCount;
    }
    /**
     * Getter für _iRTWCount.
     * @return _iRTWCount
     **/
    public int getRTWCount()
    {
        return _iRTWCount;
    }
    /**
     * Setter für _iRTWCount
     * @param iRTWCount
     **/
    public void setRTWCount(int iRTWCount)
    {
        _iRTWCount = iRTWCount;
    }

    /**
     * Gibt Department aus Cache zurück
     * @param iDepartmentId
     * @return    Department
     **/
    public static Department getDepartmentById(int iDepartmentId)
    {
        return moDepartmentCache.get(iDepartmentId);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[Department] " + _iDepartmentId;
    }
    
    /**
     * Löscht den statischen Department-Cache
     **/
    public static void clearCache()
    {
        moDepartmentCache.clear();
    }
}