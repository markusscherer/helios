package at.helios.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 
 * Diese Klasse enthält eine Methode die eine Testverbindung
 * zur Datenbank aufbaut
 * 
 * @author
 * <PRE>
 *         ID    date       description
 *         martin 03.05.2009 Neuerstellung
 * </PRE>
 *
 */
public class ConnectionTester
{
    /**
     * 
     * Versucht eine Verbindung mit der Datenbank aufzubauen.
     *
     */
    public static void establishConnection()
    {
        Connection oConnection = DBHelper.getConnection();
        
        PreparedStatement oStatement;
        try
        {
            oStatement = oConnection.prepareStatement("select * from SELECT * FROM tDay");
            ResultSet oResultSet = oStatement.executeQuery();
            oResultSet.next();
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
            
        }
        catch (SQLException e)
        {
            if (oConnection != null)
            {
                try
                {
                    oConnection.close();
                }
                catch (SQLException e1)
                {
                    System.out.println("Versuche eine Verbindung aufzubauen ... (2)");

                }
            }
            System.out.println("Versuche eine Verbindung aufzubauen ...");
        }
    }
}
