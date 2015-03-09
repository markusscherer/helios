package at.helios.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import at.helios.common.DBHelper;
import at.helios.model.Training;

/**
 * DAO für Training
 * @author
 * <PRE>
 *         ID    date       description
 *         mas 28.10.2008 Neuerstellung
 * </PRE>
 **/
public class TrainingDAO
{
    private static final String SQL_INSERT_TRAINING = "INSERT INTO tTraining(cName, cCode) VALUES (?, ?)";
    private static final String SQL_SELECT_PK       = "SELECT nTrainingId FROM tTraining WHERE cName = ? AND cCode = ?";
    private static final String SQL_FIND_ALL        = "SELECT nTrainingId, cName, cCode FROM tTraining";

    /**
     * Fügt eine Ausbiling in die Datenbank ein
     * @param oTraining   
     **/
    public void insert(Training oTraining)
    {
        Connection oConnection = DBHelper.getConnection();
        ResultSet oResultSet = null;
        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_TRAINING);
            oStatement.setString(1, oTraining.getName());
            oStatement.setString(2, oTraining.getCode());

            oStatement.execute();

            oStatement = oConnection.prepareStatement(SQL_SELECT_PK);
            oStatement.setString(1, oTraining.getName());
            oStatement.setString(2, oTraining.getCode());

            oResultSet = oStatement.executeQuery();

            if (oResultSet.next())
            {
                oTraining.setTrainingId(oResultSet.getInt(1));
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gibt alle Ausbildungen die sich in der Datenbank befinden in einer Collection zurück
     * @return    Collection mit allen Ausbildungen
     **/
    public Collection<Training> findAll()
    {
        Collection<Training> coTrainings = new Vector<Training>();
        Connection oConnection = DBHelper.getConnection();

        Training oTraining = null;
        
        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_FIND_ALL);
            ResultSet oResultSet = oStatement.executeQuery();
            
            while (oResultSet.next())
            {
                oTraining = new Training(oResultSet.getInt("nTrainingId"), oResultSet.getString("cCode"), oResultSet.getString("cName"));
                coTrainings.add(oTraining);
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return coTrainings;
    }
}
