/**
 * 
 */
package at.helios.sheduling.shiftcube;


import java.util.Collection;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;

import at.helios.calendar.components.shiftcube.BusyGenerationWindow;
import at.helios.sheduling.ShiftCube;
/**
 * Thread, der die Generierung des Schichtplans ausführt
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   23.12.2009 Neuerstellung
 * </PRE>
 */
public class ScheduleGenerationThread extends Thread
{
    private Collection<ShiftCube> _coShiftCubes;
    private Desktop _oDesktop;
    private BusyGenerationWindow _oBusyWindow;

    public ScheduleGenerationThread(Collection<ShiftCube> oShiftCube, BusyGenerationWindow oBusyWindow, Desktop oDesktop)
    {
        _coShiftCubes = oShiftCube;
        _oBusyWindow = oBusyWindow;
        _oDesktop = oDesktop;
    }
    
    public void run()
    {
        if (!_oDesktop.isServerPushEnabled()) 
            _oDesktop.enableServerPush(true);
        
        int i = 0;
        for(ShiftCube oShiftCube : _coShiftCubes)
        {
            i++;
            String sMessage = "Plan " + i + "  (" + oShiftCube.getTeamType().name() + "/ " + oShiftCube.getTeamCount() + " Teams) wird generiert.";
            try
            {
                Executions.activate(_oDesktop);
                _oBusyWindow.setMessage(sMessage);
            }
            catch (DesktopUnavailableException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally
            {
                Executions.deactivate(_oDesktop);
            }
            
            oShiftCube.generateShiftCube();
        }
        
        try
        {
            Executions.activate(_oDesktop);
            _oBusyWindow.onFinished();
        }
        catch (DesktopUnavailableException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            Executions.deactivate(_oDesktop);
        }
    }
}
