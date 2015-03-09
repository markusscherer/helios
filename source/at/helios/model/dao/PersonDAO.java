package at.helios.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import at.helios.common.DBHelper;
import at.helios.model.Department;
import at.helios.model.Person;
import at.helios.model.Training;
import at.helios.sheduling.Rule;

/**
 * DAO für Person
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.10.2008 Neuerstellung
 * </PRE>
 **/
public class PersonDAO
{

    private static final String SQL_INSERT_PERSON                 = "INSERT INTO tPerson(cForename, cSurname, cEMTNumber, cPassword, cEmail, nMaxShiftCount, nMinShiftInterval) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_PERSON_TRAINING        = "INSERT INTO tPersonTraining(nPersonId, nTrainingId, nMaxShiftCount) VALUES(?,?,?)";
    private static final String SQL_INSERT_PERSON_DEPARTMENT      = "INSERT INTO tPersonDepartment(nPersonId, nDepartmentId) VALUES(?,?)";
    private static final String SQL_SELECT_PK                     = "SELECT nPersonId FROM tPerson WHERE cForename = ? AND cSurname = ? AND cEMTNumber = ?";
    private static final String SQL_FIND_ALL                      = "SELECT nPersonId, cForename, cSurname, cEMTNumber, cPassword, cEmail, nMaxShiftCount, nMinShiftInterval FROM tPerson";
    private static final String SQL_FIND_PERSON_TRAINING          = "SELECT nTrainingId, nMaxShiftCount FROM tPersonTraining WHERE nPersonId = ?";
    private static final String SQL_FIND_DEPARTMENT               = "SELECT nDepartmentId FROM tPersonDepartment WHERE nPersonId = ?";
    private static final String SQL_UPDATE_PERSON                 = "UPDATE tPerson SET cForename = ?, cSurname = ?, cEMTNumber = ?, cPassword = ?, cEmail = ?, nMaxShiftCount = ?, nMinShiftInterval = ? WHERE nPersonId = ?";
    private static final String SQL_DELETE_PERSON                 = "DELETE FROM tPerson WHERE nPersonId = ?";
    private static final String SQL_FIND_RULE_ID                  = "SELECT nRuleId FROM tRule WHERE nRuleOwnerId = ?";
    private static final String SQL_UPDATE_TRAINING_MAXSHIFTCOUNT = "UPDATE tPersonTraining SET nMaxShiftCount = ? WHERE nPersonId = ?";
    private static final String SQL_DELETE_PERSON_TRAINING        = "DELETE FROM tPersonTraining WHERE nPersonId = ? AND nTrainingId = ?";
    
    /**
     * Fügt Person und dazugehörige Regeln in Datenbank ein
     * @param oPerson    einzufügende Person
     **/
    public void insert(Person oPerson)
    {
        Connection oConnection = DBHelper.getConnection();
        ResultSet oResultSet = null;
        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_PERSON);
            oStatement.setString(1, oPerson.getForename());
            oStatement.setString(2, oPerson.getSurname());
            oStatement.setString(3, oPerson.getEMTNumber());
            oStatement.setString(4, oPerson.getPassword());
            oStatement.setString(5, oPerson.getEmail());
            oStatement.setInt(6, oPerson.getMaxShiftCount());
            oStatement.setInt(7, oPerson.getMinShiftInterval());

            oStatement.execute();

            oStatement = oConnection.prepareStatement(SQL_SELECT_PK);
            oStatement.setString(1, oPerson.getForename());
            oStatement.setString(2, oPerson.getSurname());
            oStatement.setString(3, oPerson.getEMTNumber());

            oResultSet = oStatement.executeQuery();

            if (oResultSet.next())
            {
                oPerson.setPersonId(oResultSet.getInt(1));
                RuleDAO oRuleDAO = new RuleDAO();

                for (Rule oRule : oPerson.getRules())
                {
                    oRuleDAO.insert(oRule);
                }

                for (Training oTraining : oPerson.getTrainings().keySet())
                {
                    oStatement = oConnection.prepareStatement(SQL_INSERT_PERSON_TRAINING);
                    oStatement.setInt(1, oPerson.getPersonId());
                    oStatement.setInt(2, oTraining.getTrainingId());
                    oStatement.setInt(3, oPerson.getTrainings().get(oTraining));
                    oStatement.execute();
                }

                for (Department oDepartment : oPerson.getDepartments())
                {
                    oStatement = oConnection.prepareStatement(SQL_INSERT_PERSON_DEPARTMENT);
                    oStatement.setInt(1, oPerson.getPersonId());
                    oStatement.setInt(2, oDepartment.getDepartmentId());
                    oStatement.execute();
                }

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
     * Gibt alle Personen in der Datenbank zurück
     * @return    alle Personen
     **/
    public Collection<Person> findAll()
    {
        Collection<Person> coPersons = new Vector<Person>();

        Person oPerson = null;
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_FIND_ALL);
            ResultSet oResultSet = oStatement.executeQuery();

            while (oResultSet.next())
            {
                oPerson = new Person();
                oPerson.setPersonId(oResultSet.getInt("nPersonId"));
                oPerson.setForename(oResultSet.getString("cForename"));
                oPerson.setSurname(oResultSet.getString("cSurname"));
                oPerson.setEMTNumber(oResultSet.getString("cEMTNumber"));
                oPerson.setPassword(oResultSet.getString("cPassword"));
                oPerson.setEmail(oResultSet.getString("cEmail"));
                oPerson.setMaxShiftCount(oResultSet.getInt("nMaxShiftCount"));
                oPerson.setMinShiftInterval(oResultSet.getInt("nMinShiftInterval"));

                oStatement = oConnection.prepareStatement(SQL_FIND_PERSON_TRAINING);
                oStatement.setInt(1, oPerson.getPersonId());
                ResultSet oResultSet2 = oStatement.executeQuery();

                while (oResultSet2.next())
                {
                    oPerson.addTraining(Training.getTrainingById(oResultSet2.getInt("nTrainingId")),
                        oResultSet2.getInt("nMaxShiftCount"));
                }

                oStatement = oConnection.prepareStatement(SQL_FIND_DEPARTMENT);
                oStatement.setInt(1, oPerson.getPersonId());
                oResultSet2 = oStatement.executeQuery();

                while (oResultSet2.next())
                {
                    oPerson.addDepartment(Department.getDepartmentById(oResultSet2.getInt("nDepartmentId")));
                }

                coPersons.add(oPerson);
                oResultSet2.close();
            }
            
            oStatement.close();            
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        RuleDAO oRuleDAO = new RuleDAO();
        for (Person oRuleOwner : coPersons)
        {
            for (Rule oRule : oRuleDAO.findByRuleOwner(oRuleOwner))
            {
                oRuleOwner.addRule(oRule);
            }
        }

        return coPersons;
    }

    /**
     * Aktualisiert Person in der Datenbank
     * @param oPerson    zu aktualisierende Person
     **/
    public void update(Person oPerson)
    {
        if (oPerson.getPersonId() == 0)
        {
            return;
        }

        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_UPDATE_PERSON);

            oStatement.setString(1, oPerson.getForename());
            oStatement.setString(2, oPerson.getSurname());
            oStatement.setString(3, oPerson.getEMTNumber());
            oStatement.setString(4, oPerson.getPassword());
            oStatement.setString(5, oPerson.getEmail());
            oStatement.setInt(6, oPerson.getMaxShiftCount());
            oStatement.setInt(7, oPerson.getMinShiftInterval());
            oStatement.setInt(8, oPerson.getPersonId());

            oStatement.execute();

            oStatement = oConnection.prepareStatement(SQL_FIND_RULE_ID);

            oStatement.setInt(1, oPerson.getPersonId());

            ResultSet oResultSet = oStatement.executeQuery();

            Collection<Integer> coRuleId = new ArrayList<Integer>();

            while (oResultSet.next())
            {
                coRuleId.add(oResultSet.getInt("nRuleId"));
            }

            RuleDAO oRuleDAO = new RuleDAO();

            for (Rule oRule : oPerson.getRules())
            {
                //neue Regeln hinzufügen
                if (!coRuleId.contains(oRule.getRuleId()))
                {
                    if (oRule.getRuleId() == 0)
                    {
                        oRuleDAO.insert(oRule);
                    }
                    else
                    {
                        oRuleDAO.delete(oRule);
                    }
                }
            }
            
            //Regeln die nicht in der Person vorhanden sind löschen.
            boolean isToDelete = true;
            for (Integer iRuleId : coRuleId)
            {
                for (Rule oRule : oPerson.getRules())
                {
                    if (oRule.getRuleId() == iRuleId)
                    {
                        isToDelete = false;
                    }
                }
                if (isToDelete)
                {
                    oRuleDAO.delete(iRuleId);
                }
                isToDelete = true;
            }

            oStatement = oConnection.prepareStatement(SQL_FIND_PERSON_TRAINING);
            oStatement.setInt(1, oPerson.getPersonId());
            oResultSet = oStatement.executeQuery();
            
            Collection<Training> coTemp = new ArrayList<Training>();
            
            while (oResultSet.next())
            {
                //erstes Training der Person holen
                Training oCurrentTraining = Training.getTrainingById(oResultSet.getInt("nTrainingId"));
                coTemp.add(oCurrentTraining);
                
                //maximale mögliche Schichten
                int iCurrentCount = oResultSet.getInt("nMaxShiftCount");
                
                //Prüfen ob lokale TrainingMap, das Training aus der Datenbank hat
                if (oPerson.getTrainings().containsKey(oCurrentTraining))
                {
                    //Prüfen ob sich der Wert verändert hat
                    if (iCurrentCount == oPerson.getTrainings().get(oCurrentTraining))
                    {
                        continue;
                    }
                    //neuer Wert setzen
                    else
                    {
                        PreparedStatement oStatement2 = oConnection.prepareStatement(SQL_UPDATE_TRAINING_MAXSHIFTCOUNT);
                        oStatement2.setInt(1, oPerson.getTrainings().get(oCurrentTraining));
                        oStatement2.setInt(2, oPerson.getPersonId());
                        oStatement2.execute();
                        oStatement2.close();
                    }
                }
                //Training in der Datenbank wurde aus Map gelöscht --> Training aus DB löschen
                else
                {
                    PreparedStatement oStatement2 = oConnection.prepareStatement(SQL_DELETE_PERSON_TRAINING);
                    oStatement2.setInt(1, oPerson.getPersonId());
                    oStatement2.setInt(2, oCurrentTraining.getTrainingId());
                    oStatement2.execute();
                    oStatement2.close();
                }
            }
            
            //Alle neuen Trainings hinzufügen
            for(Training oTraining : oPerson.getTrainings().keySet())
            {
                if(!coTemp.contains(oTraining))
                {
                    PreparedStatement oStatement2 = oConnection.prepareStatement(SQL_INSERT_PERSON_TRAINING);
                    oStatement2.setInt(1, oPerson.getPersonId());
                    oStatement2.setInt(2, oTraining.getTrainingId());
                    oStatement2.setInt(3, oPerson.getTrainings().get(oTraining));
                    oStatement2.execute();
                    oStatement2.close();
                }
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
     * Löscht Person aus der Datenbank
     * @param oPerson    zu löschende Person
     **/
    public void delete(Person oPerson)
    {
        if (oPerson.getPersonId() == 0)
        {
            return;
        }

        Connection oConnection = DBHelper.getConnection();

        try
        {
            for(Training oTraining : oPerson.getTrainings().keySet())
            {
                PreparedStatement oStatement2 = oConnection.prepareStatement(SQL_DELETE_PERSON_TRAINING);
                oStatement2.setInt(1, oPerson.getPersonId());
                oStatement2.setInt(2, oTraining.getTrainingId());
                oStatement2.execute();
                oStatement2.close();
            }
            
            for(Rule oRule : oPerson.getRules())
            {
                RuleDAO oRuleDAO = new RuleDAO();
                oRuleDAO.delete(oRule);
            }
            
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_DELETE_PERSON);
            oStatement.setInt(1, oPerson.getPersonId());
            oStatement.execute();
            
            oStatement.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}