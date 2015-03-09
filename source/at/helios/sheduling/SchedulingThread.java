///**
// * 
// */
//package at.helios.sheduling;
//
//import java.util.Collection;
//
//import org.zkoss.zk.ui.Desktop;
//import org.zkoss.zk.ui.DesktopUnavailableException;
//import org.zkoss.zk.ui.Executions;
//
//import at.helios.calendar.components.BusySchedulingWindow;
//
///**
// * Thread, der den Einteilungsvorgang ausführt
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   05.01.2010 Neuerstellung
// * </PRE>
// */
//public class SchedulingThread extends Thread
//{
//    private Collection<ShiftTree> _coShiftTrees;
//    private Desktop _oDesktop;
//    private BusySchedulingWindow _oBusyWindow;
//
//    public SchedulingThread(Collection<ShiftTree> oShiftTree, BusySchedulingWindow oBusyWindow, Desktop oDesktop)
//    {
//        _coShiftTrees = oShiftTree;
//        _oBusyWindow = oBusyWindow;
//        _oDesktop = oDesktop;
//    }
//    
//    public void run()
//    {
//        if (!_oDesktop.isServerPushEnabled()) 
//            _oDesktop.enableServerPush(true);
//        
//        int i = 0;
//        for(ShiftTree oShiftTree : _coShiftTrees)
//        {
//            i++;
//            String sMessage = "Plan " + i + "  (" + oShiftTree.getTeamType().name() + "/ " + oShiftTree.getTeamCount() + " Teams) wird eingeteilt.";
//            try
//            {
//                Executions.activate(_oDesktop);
//                _oBusyWindow.setMessage(sMessage);
//            }
//            catch (DesktopUnavailableException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            catch (InterruptedException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            finally
//            {
//                Executions.deactivate(_oDesktop);
//            }
//            
//            oShiftTree.schedulePeriod();
//        }
//        
//        try
//        {
//            Executions.activate(_oDesktop);
//            _oBusyWindow.onFinished();
//        }
//        catch (DesktopUnavailableException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (InterruptedException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        finally
//        {
//            Executions.deactivate(_oDesktop);
//        }
//    }
//}
