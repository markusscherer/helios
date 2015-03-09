package at.helios.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.zkoss.zk.ui.Sessions;

import at.helios.common.DBHelper;
import at.helios.model.Department;

/**
 * Überprüft Sessions und Login-Daten
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   07.03.2009 Neuerstellung
 * </PRE>
 **/
public class AuthenticationManager
{
    private static final String SQL_SELECT_VALID_USER = "SELECT * FROM tDepartment WHERE nDepartmentId=? AND cUsername=? AND cPassword=?";

    /**
     * Überprüft, ob ein User mit dem angegeben Password sich bei der angegebenen Dienststelle anmelden darf
     * @param oDepartment
     * @param sUser
     * @param sPassword
     * @return ist der angegebene ein valider User?
     **/
    public static boolean isValidUser(Department oDepartment, String sUser, String sPassword)
    {
        Connection oConnection = DBHelper.getConnection();

        PreparedStatement oStatement;

        try
        {
            oStatement = oConnection.prepareStatement(SQL_SELECT_VALID_USER);
            oStatement.setInt(1, oDepartment.getDepartmentId());
            oStatement.setString(2, sUser);
            oStatement.setString(3, sPassword);

            ResultSet oResultSet = oStatement.executeQuery();

            boolean bIsValidUser = oResultSet.next();
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
            
            return bIsValidUser;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Überprüft ob der Benutzer der aktuellen Session angemeldet ist
     * @return ist der angegebene Session ein valide?   
     **/
    public static boolean isValidSession()
    {        
        Boolean bValid = (Boolean) Sessions.getCurrent().getAttribute("authenticated");
        
        if(bValid == null)
        {
            return false;
        }

        return bValid;
    }
}
