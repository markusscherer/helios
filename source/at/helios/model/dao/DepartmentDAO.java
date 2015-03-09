package at.helios.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import at.helios.common.ConnectionTester;
import at.helios.common.DBHelper;
import at.helios.model.Department;

/**
 * DAO für Department
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class DepartmentDAO
{

    private static final String SQL_INSERT_DEPARTMENT = "INSERT INTO tDepartment(cName, nRTWCount, nNEFCount) VALUES  (?,?,?);";
    private static final String SQL_SELECT_PK         = "SELECT nDepartmentId FROM tDepartment WHERE cName = ?";
    private static final String SQL_FIND_ALL          = "SELECT nDepartmentId, cName, nNEFCount, nRTWCount FROM tDepartment";

    /**
     * Fügt Department in Datenbank ein
     * @param oDepartment
     **/
    public void insert(Department oDepartment)
    {
        Connection oConnection = DBHelper.getConnection();
        ResultSet oResultSet = null;
        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_DEPARTMENT);
            oStatement.setString(1, oDepartment.getName());
            oStatement.setInt(2, oDepartment.getRTWCount());
            oStatement.setInt(3, oDepartment.getNEFCount());

            oStatement.execute();

            oStatement = oConnection.prepareStatement(SQL_SELECT_PK);
            oStatement.setString(1, oDepartment.getName());

            oResultSet = oStatement.executeQuery();

            if (oResultSet.next())
            {
                oDepartment.setDepartmentId(oResultSet.getInt(1));
            }
            
            oStatement.close();            
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gibt Collection mit allen Departments zurück
     * @return    Collection mit allen Departments 
     **/
    public Collection<Department> findAll()
    {
        ConnectionTester.establishConnection();
        
        Collection<Department> coDepartments = new Vector<Department>();
        Connection oConnection = DBHelper.getConnection();

        Department oDepartment = null;

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_FIND_ALL);
            ResultSet oResultSet = oStatement.executeQuery();

            while (oResultSet.next())
            {
                oDepartment = new Department(oResultSet.getInt("nDepartmentId"), oResultSet
                    .getString("cName"), oResultSet.getInt("nNEFCount"), oResultSet.getInt("nRTWCount"));
                coDepartments.add(oDepartment);
            }
            
            oStatement.close();            
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            System.out.println("Kontte Verbindung nicht herstellen.");
        }
        finally
        {
            if (oConnection != null)
            {
                try
                {
                    oConnection.close();
                }
                catch (SQLException e)
                {
                    
                }
            }
        }

        return coDepartments;
    }
}