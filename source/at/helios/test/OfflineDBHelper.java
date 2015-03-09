package at.helios.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Hilft, Verbindung mit der Datenbank aufzunehmen
 * @author
 * <PRE>
 *         ID     date       description
 *         mas    28.10.2008 Neuerstellung
 *         mas    10.02.2008 Properties hinzugefügt
 * </PRE>
 **/
public class OfflineDBHelper
{

    static Connection oConnection = null;

    /**
     * Initialisiert Verbindung mit MySQL Server
     */
    static private void init()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            oConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HELIOS",
                "root", "MEGApassWORT");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Konnte Treiberklasse nicht finden.");
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            System.out.println("Konnte Verbindung nicht herstellen.");
            e.printStackTrace();
        }

        try
        {
            Context initContext = new InitialContext();

            Context envContext = (Context) initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource) envContext.lookup("jdbc/heliosDB");

            oConnection = ds.getConnection();
        }
        catch (NamingException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Gibt Verbindung zurück
     * 
     * @return Verbindung
     */
    public static Connection getConnection()
    {
                if (oConnection == null)
                {
                    init();
                }

                return oConnection;
            
        /*Connection con = null;

        try
        {
            Context initContext = new InitialContext();

            Context envContext = (Context) initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource) envContext.lookup("jdbc/heliosDB");

            return ds.getConnection();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
            // Fehler loggen
            System.out.println("Fehler beim erstellen der Datenbankverbindung!");
        }
        catch (NamingException e)
        {
            // DataSource wurde nicht im JNDI gefunden
        }
        finally
        {
            if (con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                    System.out.println("Fehler beim schließen der Datenbankverbindung.");
                }
            }
        }

        return null;*/
    }
}