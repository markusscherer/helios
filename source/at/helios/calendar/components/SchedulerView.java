//package at.helios.calendar.components;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Session;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.SuspendNotAllowedException;
//import org.zkoss.zkex.zul.Borderlayout;
//import org.zkoss.zkex.zul.Center;
//import org.zkoss.zkex.zul.North;
//import org.zkoss.zul.Auxhead;
//import org.zkoss.zul.Auxheader;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Column;
//import org.zkoss.zul.Columns;
//import org.zkoss.zul.Datebox;
//import org.zkoss.zul.Grid;
//import org.zkoss.zul.Hbox;
//import org.zkoss.zul.Intbox;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Messagebox;
//import org.zkoss.zul.Row;
//import org.zkoss.zul.Rows;
//import org.zkoss.zul.Tab;
//import org.zkoss.zul.Tabbox;
//import org.zkoss.zul.Tabpanel;
//import org.zkoss.zul.Tabpanels;
//import org.zkoss.zul.Tabs;
//
//import at.helios.calendar.helper.DateHelper;
//import at.helios.calendar.helper.HolidayHelper;
//import at.helios.common.RedirectHelper;
//import at.helios.model.Department;
//import at.helios.model.dao.ShiftplanDAO;
//import at.helios.sheduling.ScheduleGenerationThread;
//import at.helios.sheduling.SchedulingThread;
//import at.helios.sheduling.ShiftNode;
//import at.helios.sheduling.ShiftTree;
//import at.helios.sheduling.Shifts;
//import at.helios.sheduling.Teams;
//
///**
// * Ansicht für Einteilung
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   26.12.2008 Neuerstellung
// * </PRE>
// **/
//@SuppressWarnings("serial")
//public class SchedulerView extends Borderlayout
//{
//    private ShiftTree            _oNEFShiftTree;
//    private ShiftTree            _oRTWShiftTree;
//    private ShiftTree            _oSTBShiftTree;
//    private North                _oNorth;
//    private Center               _oSouth;
//    private Datebox              _oFrom;
//    private Datebox              _oTo;
//    private Intbox               _oNEFCount;
//    private Intbox               _oRTWCount;
//    private Intbox               _oSTBCount;
//    private Department           _oDepartment;
//    private HolidayHelper        _oHolidayHelper = new HolidayHelper();
//    private HolidayBox           _oHolidayBox;
//    private OtherShiftsBox       _oOtherShiftsBox;
//    private Collection<ShiftBox> _coShiftBoxes   = new ArrayList<ShiftBox>();
//    private Button               _oScheduleButton;
//    private Button               _oGenerateButton;
//    
//    private ScheduleGenerationThread    _oSchedulingThread;
//
//    /**
//     * Standard Konstruktor, füllt GUI-Komponente
//     **/
//    public SchedulerView()
//    {
//        Session oSession = Sessions.getCurrent();
//        oSession.setAttribute("SchedulerView", this);
//        
//        _oDepartment = (Department) Sessions.getCurrent().getAttribute("department");
//
//        Hbox oHbox = new Hbox();
//
//        _oNorth = new North();
//        _oNorth.setSize("170px");
//        _oNorth.setTitle("Einstellungen");
//        _oNorth.setCollapsible(true);
//
//        _oSouth = new Center();
//        _oSouth.setTitle("Plan");
//        _oSouth.setFlex(true);
//        //this.setHeight("1000px");
//
//        Listbox oListbox = new Listbox();
//        oListbox.setWidth("270px");
//        Listitem oListItem = new Listitem();
//        Listcell oListcell = new Listcell();
//
//        Label oLabel = new Label("Von: ");
//
//        oListcell.setStyle("padding:3px");
//        oLabel.setParent(oListcell);
//        oListcell.setParent(oListItem);
//        oListcell = new Listcell();
//        oListcell.setParent(oListItem);
//        _oFrom = new Datebox();
//        _oFrom.setParent(oListcell);
//
//        oListcell.setParent(oListItem);
//        oListItem.setParent(oListbox);
//        oListcell = new Listcell();
//        oListItem = new Listitem();
//
//        oLabel = new Label("Bis: ");
//
//        oListcell.setStyle("padding:3px");
//        oLabel.setParent(oListcell);
//        oListcell.setParent(oListItem);
//        oListcell = new Listcell();
//        oListcell.setParent(oListItem);
//        _oTo = new Datebox();
//        _oTo.setParent(oListcell);
//
//        oListcell.setParent(oListItem);
//        oListItem.setParent(oListbox);
//        oListcell = new Listcell();
//        oListItem = new Listitem();
//
//        oLabel = new Label("NEF-Teams: ");
//
//        oListcell.setStyle("padding:3px");
//        oLabel.setParent(oListcell);
//        oListcell.setParent(oListItem);
//        oListcell = new Listcell();
//        oListcell.setParent(oListItem);
//        _oNEFCount = new Intbox(_oDepartment.getNEFCount());
//        _oNEFCount.setParent(oListcell);
//
//        oListcell.setParent(oListItem);
//        oListItem.setParent(oListbox);
//        oListcell = new Listcell();
//        oListItem = new Listitem();
//
//        oLabel = new Label("RTW-Teams: ");
//
//        oListcell.setStyle("padding:3px");
//        oLabel.setParent(oListcell);
//        oListcell.setParent(oListItem);
//        oListcell = new Listcell();
//        oListcell.setParent(oListItem);
//        _oRTWCount = new Intbox(_oDepartment.getRTWCount());
//        _oRTWCount.setParent(oListcell);
//
//        oListcell.setParent(oListItem);
//        oListItem.setParent(oListbox);
//        oListcell = new Listcell();
//        oListItem = new Listitem();
//
//        oLabel = new Label("Bereitschaftsteams: ");
//
//        oListcell.setStyle("padding:3px");
//        oLabel.setParent(oListcell);
//        oListcell.setParent(oListItem);
//        oListcell = new Listcell();
//        oListcell.setParent(oListItem);
//        _oSTBCount = new Intbox(0);
//        _oSTBCount.setParent(oListcell);
//        oListcell.setParent(oListItem);
//
//        oListItem.setParent(oListbox);
//
//        oListItem = new Listitem();
//        oListcell = new Listcell();
//        oListcell.setStyle("padding:3px");
//        _oGenerateButton = new Button();
//        _oGenerateButton.setLabel("Plan generieren");
//        _oGenerateButton.addForward("onClick", this, "onGeneratePlan");
//        _oGenerateButton.setParent(oListcell);
//        oListcell.setParent(oListItem);
//
//        oListcell = new Listcell();
//        oListcell.setStyle("padding:3px");
//        _oScheduleButton = new Button();
//        _oScheduleButton.setLabel("Plan einteilen");
//        _oScheduleButton.addForward("onClick", this, "onSchedulePlan");
//        _oScheduleButton.setParent(oListcell);
//        _oScheduleButton.setDisabled(true);
//        oListcell.setParent(oListItem);
//
//        oListItem.setParent(oListbox);
//
//        oListbox.setParent(oHbox);
//        ViolationListbox oViolationListbox = new ViolationListbox();
//        oViolationListbox.setParent(oHbox);
//
//        Tabbox oTabbox = new Tabbox();
//        oTabbox.setOrient("vertical");
//        oTabbox.setHeight("140px");
//        oTabbox.setWidth("360px");
//
//        Tabs oTabs = new Tabs();
//
//        Tab oTab = new Tab("Feiert.");
//        oTab.setParent(oTabs);
//
//        oTab = new Tab("Schichten");
//        oTab.setParent(oTabs);
//
//        oTabs.setParent(oTabbox);
//        
//        Tabpanels oTabpanels = new Tabpanels();
//        Tabpanel oTabpanel = new Tabpanel();
//
//        _oHolidayBox = new HolidayBox(_oHolidayHelper);
//        _oHolidayBox.setParent(oTabpanel);
//        oTabpanel.setParent(oTabpanels);
//
//        oTabpanel = new Tabpanel();
//        _oOtherShiftsBox = new OtherShiftsBox();
//        _oOtherShiftsBox.setParent(oTabpanel);
//        oTabpanel.setParent(oTabpanels);
//
//        oTabpanels.setParent(oTabbox);
//        oTabbox.setParent(oHbox);
//        oHbox.setParent(_oNorth);
//        _oNorth.setParent(this);
//        _oSouth.setParent(this);
//        
//        Executions.getCurrent().getDesktop().enableServerPush(true);
//
//    }
//
//    /**
//     * Wird ausgeführt, wenn "Plan generieren"-Button gedrückt wird
//     **/
//    public void onGeneratePlan()
//    {
//        if (_oFrom.getValue() == null || _oTo.getValue() == null)
//        {
//            try
//            {
//                Messagebox.show("Geben Sie ein Von-Datum und ein Bis-Datum ein.", "Warnung", Messagebox.OK,
//                    Messagebox.EXCLAMATION);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//
//            return;
//        }
//
//        GregorianCalendar oCalendarFrom = new GregorianCalendar();
//        oCalendarFrom.setTime(_oFrom.getValue());
//
//        GregorianCalendar oCalendarTo = new GregorianCalendar();
//        oCalendarTo.setTime(_oTo.getValue());
//
//        if (oCalendarFrom.after(oCalendarTo) || oCalendarFrom.equals(oCalendarTo))
//        {
//            try
//            {
//                Messagebox
//                    .show(
//                        "Von-Datum liegt zeitlich hinter oder auf dem Bis-Datum, wählen Sie ein späteres Bis-Datum.",
//                        "Warnung", Messagebox.OK, Messagebox.EXCLAMATION);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        if (_oNEFCount.getValue() < 1 && _oRTWCount.getValue() < 1 && _oSTBCount.getValue() < 1)
//        {
//            try
//            {
//                Messagebox
//                    .show(
//                        "Laut Ihrer momentanen Auswahl, wird kein Team eingeteilt. Setzen Sie die Anzahl mindestens einer Team-Art auf mindestens 1.",
//                        "Warnung", Messagebox.OK, Messagebox.EXCLAMATION);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            return;
//        }
//        
//        Collection<ShiftTree> coShiftTrees = new ArrayList<ShiftTree>();
//
//        BusyGenerationWindow oWindow = (BusyGenerationWindow) Executions.createComponents("/busy_generation_window.zul", null, null);
//        oWindow.setClosable(false);
//        oWindow.setVisible(true);        
//        
//        _oNEFShiftTree = new ShiftTree(oCalendarFrom, oCalendarTo, (Department)Sessions.getCurrent().getAttribute("department"),
//            Teams.nef, _oNEFCount.getValue(), _oHolidayHelper);   
//        _oRTWShiftTree = new ShiftTree(oCalendarFrom, oCalendarTo, (Department)Sessions.getCurrent().getAttribute("department"),
//            Teams.rtw, _oRTWCount.getValue(), _oHolidayHelper);
//        _oSTBShiftTree = new ShiftTree(oCalendarFrom, oCalendarTo, (Department)Sessions.getCurrent().getAttribute("department"),
//            Teams.standby, _oSTBCount.getValue(), _oHolidayHelper);
//        
//        coShiftTrees.add(_oNEFShiftTree);
//        coShiftTrees.add(_oRTWShiftTree);
//        coShiftTrees.add(_oSTBShiftTree);
//        _oSchedulingThread = new ScheduleGenerationThread(coShiftTrees, oWindow, Executions.getCurrent().getDesktop());
//        _oSchedulingThread.start();
//        
//        try
//        {
//            oWindow.doModal();
//        }
//        catch (SuspendNotAllowedException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        catch (InterruptedException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        _oGenerateButton.setDisabled(true);
//    }
//    
//    private void generateCalendar()
//    {
//        GregorianCalendar oCalendarFrom = new GregorianCalendar();
//        oCalendarFrom.setTime(_oFrom.getValue());
//
//        GregorianCalendar oCalendarTo = new GregorianCalendar();
//        oCalendarTo.setTime(_oTo.getValue());
//        
//        int iMonthCount = 0;
//     
//        if (oCalendarFrom.get(Calendar.YEAR) == oCalendarTo.get(Calendar.YEAR))
//        {
//            iMonthCount = oCalendarTo.get(Calendar.MONTH) - oCalendarFrom.get(Calendar.MONTH) + 1;
//        }
//        else
//        {
//            iMonthCount = (oCalendarFrom.get(Calendar.MONTH) + oCalendarTo.get(Calendar.MONTH) + 2)%12 + 12
//                * (oCalendarTo.get(Calendar.YEAR) - oCalendarFrom.get(Calendar.YEAR));
//        }
//        
//        System.out.println("++++++++++++"+iMonthCount +"++++++++++++++");
//
//        Grid[] aoGrids = new Grid[iMonthCount];
//
//        Tabbox oTabbox = new Tabbox();
//
//        Rows oRows = new Rows();
//        Row oRow = new Row();
//
//        GregorianCalendar oTempCalendar = (GregorianCalendar) oCalendarFrom.clone();
//
//        //Um Bugs bei Sommerzeit/Winterzeitumstellung zu verhindern
//        oCalendarTo.set(Calendar.HOUR_OF_DAY, 12);
//        oTempCalendar.set(Calendar.HOUR_OF_DAY, 12);
//        
//        boolean bTemp = true;
//        int iTempMonth = oTempCalendar.get(Calendar.MONTH);
//        int iCurrentGrid = 0;
//
//        aoGrids[iCurrentGrid] = new Grid();
//        fillAuxHeaders(aoGrids[iCurrentGrid]);
//
//        Tabs oTabs = new Tabs();
//        Tab oTab = new Tab();
//
//        SimpleDateFormat oFormat = new SimpleDateFormat("MMMM");
//
//        oTab.setLabel(oFormat.format(oTempCalendar.getTime()) + " " + oTempCalendar.get(Calendar.YEAR));
//
//        Tabpanels oTabpanels = new Tabpanels();
//        Tabpanel oTabpanel = new Tabpanel();
//
//        oRows.setParent(aoGrids[iCurrentGrid]);
//        aoGrids[iCurrentGrid].setHeight("560px");
//        aoGrids[iCurrentGrid].setParent(oTabpanel);
//        oTab.setParent(oTabs);
//        oTabpanel.setParent(oTabpanels);
//        SimpleDateFormat oFormat1 = new SimpleDateFormat("HH:mm:ss  -  dd.MM.yyyy");
//        while (bTemp)
//        {
//            System.out.println("++++   " + oFormat1.format(oTempCalendar.getTime()) + " before " + oFormat1.format(oCalendarTo.getTime()) + "  " + bTemp);
//            if (oTempCalendar.get(Calendar.MONTH) != iTempMonth)
//            {
//                iTempMonth = oTempCalendar.get(Calendar.MONTH);
//                iCurrentGrid++;
//
//                aoGrids[iCurrentGrid] = new Grid();
//                fillAuxHeaders(aoGrids[iCurrentGrid]);
//
//                oTab = new Tab();
//                oTab.setLabel(oFormat.format(oTempCalendar.getTime()) + " "
//                    + oTempCalendar.get(Calendar.YEAR));
//
//                oTabpanel = new Tabpanel();
//                oRows = new Rows();
//
//                oRows.setParent(aoGrids[iCurrentGrid]);
//                aoGrids[iCurrentGrid].setHeight("560px");
//                aoGrids[iCurrentGrid].setParent(oTabpanel);
//                oTab.setParent(oTabs);
//                oTabpanel.setParent(oTabpanels);
//            }
////            bTemp = oTempCalendar.before(oCalendarTo);
////            if(!bTemp)
////            {
//             bTemp = DateHelper.before(oTempCalendar, oCalendarTo);
//            
//            
//          //  bTemp = Math.abs(oTempCalendar.getTimeInMillis()-oCalendarTo.getTimeInMillis()) >= 1000*60*60;
////                bTemp = Math.abs(oTempCalendar.get(Calendar.HOUR_OF_DAY)-oCalendarTo.get(Calendar.HOUR_OF_DAY)) <= 1;
//            //    System.out.println(""+(oTempCalendar.get(Calendar.HOUR_OF_DAY)-oCalendarTo.get(Calendar.HOUR_OF_DAY)));
////            }
//           
//            oRow = new Row();
//            (new Label(oTempCalendar.get(GregorianCalendar.DAY_OF_MONTH) + ".")).setParent(oRow);
//
//            fillRow(oRow, oTempCalendar, _oNEFShiftTree, _oNEFCount.getValue());
//            fillRow(oRow, oTempCalendar, _oRTWShiftTree, _oRTWCount.getValue());
//            fillRow(oRow, oTempCalendar, _oSTBShiftTree, _oSTBCount.getValue());
//
//            oRow.setParent(oRows);
//
//            long lTime = oTempCalendar.getTimeInMillis();
//            lTime += 24 * 60 * 60 * 1000;
//            oTempCalendar.setTimeInMillis(lTime);
//        }
//
//        oTabs.setParent(oTabbox);
//        oTabpanels.setParent(oTabbox);
//
//        Button oButton = new Button();
//        oButton.setLabel("Plan speichern");
//        oButton.addForward("onClick", this, "onSavePlan");
//        oButton.setWidth("150px");
//        oButton.setParent(_oHolidayBox.getVbox());
//
//        _oScheduleButton.setDisabled(false);
//
//        oTabbox.setParent(_oSouth);
//    }
//    
//    public void onFinishedGeneration()
//    {
//        this.removeChild(_oSouth);
//        _oSouth = new Center();
//        _oSouth.setTitle("Plan");
//        _oSouth.setFlex(true);
//        _oSouth.setParent(this);
//        
//        generateCalendar();
//    }
//    
//    public void onFinishedScheduling()
//    {
//        updateShiftBoxes();
//    }
//
//    /**
//     * Wird ausgeführt, wenn "Plan einteilen"-Button gerückt wird
//     **/
//    public void onSchedulePlan()
//    {
//        Collection<ShiftTree> coShiftTrees = new ArrayList<ShiftTree>();
//
//        BusySchedulingWindow oWindow = (BusySchedulingWindow) Executions.createComponents("/busy_scheduling_window.zul", null, null);
//        oWindow.setClosable(false);
//        oWindow.setVisible(true);
//        
//        coShiftTrees.add(_oNEFShiftTree);
//        coShiftTrees.add(_oRTWShiftTree);
//        coShiftTrees.add(_oSTBShiftTree);
//        
//        SchedulingThread oSchedulingThread = new SchedulingThread(coShiftTrees, oWindow, Executions.getCurrent().getDesktop());
//        oSchedulingThread.start();       
//        
//        try
//        {
//            oWindow.doModal();
//        }
//        catch (SuspendNotAllowedException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        catch (InterruptedException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//    }
//
//    private void updateShiftBoxes()
//    {
//        for (ShiftBox oShiftBox : _coShiftBoxes)
//        {
//            oShiftBox.update();
//        }
//    }
//
//    /**
//     * Wird ausgeführt, wenn "Plan speichern"-Button gerückt wird
//     **/
//    public void onSavePlan()
//    {
//        ShiftplanDAO oShiftplanDAO = new ShiftplanDAO();
//
//        List<ShiftTree> coShiftTrees = new ArrayList<ShiftTree>();
//
//        coShiftTrees.add(_oNEFShiftTree);
//        coShiftTrees.add(_oRTWShiftTree);
//        coShiftTrees.add(_oSTBShiftTree);
//
//        oShiftplanDAO.insert(coShiftTrees);
//
//        try
//        {
//            int iResult = Messagebox.show("Der Plan wurde gespeichert. Wollen Sie zur Übersicht zurückkehren?",
//                "Plan gespeichert", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
//            
//            if(iResult == Messagebox.YES)
//            {
//                Executions.sendRedirect(RedirectHelper.getOverviewLink());
//            }
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        return;
//    }
//
//    private void fillRow(Row oRow, GregorianCalendar oCalendarTemp, ShiftTree oShiftTree, int iCount)
//    {
//        for (int i = 0; i < iCount; i++)
//        {
//            ShiftNode oShiftNode = oShiftTree.getShiftNode(oCalendarTemp, Shifts.nightshift);
//
//            if (oShiftNode != null)
//            {
//                ShiftBox oShiftBox = new ShiftBox(oShiftNode, i);
//                oShiftBox.setParent(oRow);
//                _coShiftBoxes.add(oShiftBox);
//            }
//            else
//            {
//                Label oLabel = new Label();
//                oLabel.setParent(oRow);
//            }
//
//            oShiftNode = oShiftTree.getShiftNode(oCalendarTemp, Shifts.dayshift);
//
//            if (oShiftNode != null)
//            {
//                ShiftBox oShiftBox = new ShiftBox(oShiftNode, i);
//                oShiftBox.setParent(oRow);
//                _coShiftBoxes.add(oShiftBox);
//            }
//            else
//            {
//                Label oLabel = new Label();
//                oLabel.setParent(oRow);
//            }
//        }
//    }
//
//    private void fillAuxHeaders(Grid oGrid)
//    {
//        Auxhead oAuxhead = new Auxhead();
//        Auxheader oAuxheader = new Auxheader();
//
//        oAuxheader.setColspan(1);
//        oAuxheader.setParent(oAuxhead);
//
//        for (int i = 1; i <= _oNEFCount.getValue(); i++)
//        {
//            oAuxheader = new Auxheader();
//            oAuxheader.setColspan(2);
//            oAuxheader.setLabel(i + ". NEF-Team");
//            oAuxheader.setParent(oAuxhead);
//        }
//        oAuxhead.setParent(oGrid);
//
//        for (int i = 1; i <= _oRTWCount.getValue(); i++)
//        {
//            oAuxheader = new Auxheader();
//            oAuxheader.setColspan(2);
//            oAuxheader.setLabel(i + ". RTW-Team");
//            oAuxheader.setParent(oAuxhead);
//        }
//        oAuxhead.setParent(oGrid);
//
//        for (int i = 1; i <= _oSTBCount.getValue(); i++)
//        {
//            oAuxheader = new Auxheader();
//            oAuxheader.setColspan(2);
//            oAuxheader.setLabel(i + ". Bereitschaft-Team");
//            oAuxheader.setParent(oAuxhead);
//        }
//        oAuxhead.setParent(oGrid);
//
//        Columns oColumns = new Columns();
//        Column oColumn = new Column();
//        oColumn.setWidth("30px");
//        oColumn.setParent(oColumns);
//
//        for (int i = 0; i < (_oRTWCount.getValue() + _oNEFCount.getValue() + _oSTBCount.getValue()) * 2; i++)
//        {
//            oColumn = new Column();
//            oColumn.setLabel("Fahrer   -   Sanitäter");
//            oColumn.setWidth("150px");
//            oColumn.setParent(oColumns);
//        }
//
//        oColumns.setParent(oGrid);
//    }
//}
