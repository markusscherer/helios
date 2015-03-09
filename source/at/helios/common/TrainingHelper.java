package at.helios.common;

import at.helios.model.Training;
import at.helios.sheduling.Teams;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   17.05.2010 Neuerstellung
 * </PRE>
 */
public class TrainingHelper
{
    public static Training getDriverTraining(Teams oTeamType)
    {
        switch (oTeamType)
        {
            case rtw:
                return Training.getTrainingById(1);
            case nef:
                return Training.getTrainingById(3);
            case standby:
                return Training.getTrainingById(5);
            default:
                throw new RuntimeException("No valid Teamtype specified.");
        }
    }

    public static Training getMediTraining(Teams oTeamType)
    {
        switch (oTeamType)
        {
            case rtw:
                return Training.getTrainingById(2);
            case nef:
                return Training.getTrainingById(4);
            case standby:
                return Training.getTrainingById(6);
            default:
                throw new RuntimeException("No valid Teamtype specified.");
        }
    }

    public static Training getAdditionalTraining()
    {
        return Training.getTrainingById(6);
    }
}
