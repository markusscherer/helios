package at.helios.common;

import java.util.Date;

import at.helios.model.Department;
import at.helios.model.Person;
import at.helios.model.Training;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus 07.03.2009 Neuerstellung
 * </PRE>
 **/
public class CacheThread extends Thread
{
    private boolean _bRun = true;
    public void run()
    {
        while(_bRun)
        {
            //Momentane Cache-Zeit: 40 min, evtl. in Property auslagern
            if((new Date()).getTime() - Person.getLastAccess().getTime() > 60 * 1000 * 40)
            {
                System.out.println("Cache wurde gelöscht");
                Person.clearCache();
                Department.clearCache();
                Training.clearCache();
                //TODO evtl. weitere Kontrollieren
                _bRun = false;
            }
            
            try
            {
                sleep(60 * 1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}