package at.helios.dataview.components;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import at.helios.calendar.helper.DateHelper;
import at.helios.common.PersonPanel;
import at.helios.common.ShiftplanComboxBox;
import at.helios.model.Shift;
import at.helios.model.Shiftplan;
import at.helios.model.Team;
import at.helios.model.dao.ShiftplanDAO;
import at.helios.sheduling.Shifts;
import at.helios.sheduling.Teams;

/**
 * Tabpanel, die Plan-Bestätigung beinhaltet
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   27.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ConfirmTab extends Tabpanel
{
    PersonPanel _oPersonPanel;
    Shiftplan _oShiftplan;
    Groupbox _oSettingsGroupbox;
    ShiftplanComboxBox _oShiftplanComboxBox;
    
    /**
     * Standardkonstruktor, füllt die GUI-Komponente
     **/
    public ConfirmTab()
    {   
        _oSettingsGroupbox = new Groupbox();
        _oSettingsGroupbox.setMold("3d");
        _oSettingsGroupbox.setWidth("650px");

        Caption oCaption = new Caption("Einstellungen");
        oCaption.setParent(_oSettingsGroupbox);
        
        Button oButton = new Button("Plan anzeigen");
        oButton.addForward("onClick", this, "onGeneratePlan");
        oButton.setParent(_oSettingsGroupbox);
        
        oButton = new Button("Plan speichern");
        oButton.addForward("onClick", this, "onUpdatePlan");
        oButton.setParent(_oSettingsGroupbox);
        
        _oShiftplanComboxBox = new ShiftplanComboxBox();
        _oShiftplanComboxBox.setParent(_oSettingsGroupbox);
        
        _oSettingsGroupbox.setParent(this);
    }

    /**
     * Wird ausgeführt, wenn "Plan speichern" Button gedrückt wird
     **/
    public void onUpdatePlan()
    {
        ShiftplanDAO oShiftplanDAO = new ShiftplanDAO();
        oShiftplanDAO.update(_oShiftplan);
        
        try
        {
            int iResult = Messagebox.show("Der Plan wurde gespeichert. Wollen Sie ihn in dieser Form bestätigen?",
                "Plan gespeichert", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
            
            if(iResult == Messagebox.YES)
            {
                oShiftplanDAO.confirm((Integer)_oShiftplanComboxBox.getSelectedItem().getValue());
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Wird ausgeführt, wenn "Plan generieren" Button gedrückt wird
     **/
    public void onGeneratePlan()
    {
        ShiftplanDAO oShiftplanDAO = new ShiftplanDAO();
        
        _oShiftplan = oShiftplanDAO.findByShiftplanId((Integer)_oShiftplanComboxBox.getSelectedItem().getValue());

        _oPersonPanel = ((DataView) getParent().getParent().getParent().getParent()).getPersonPanel();

        GregorianCalendar oCalendarFrom = _oShiftplan.getPeriodStart();
        GregorianCalendar oCalendarTo = _oShiftplan.getPeriodEnd();

        int iMonthCount = 0;
        if (oCalendarFrom.get(Calendar.YEAR) == oCalendarTo.get(Calendar.YEAR))
        {
            iMonthCount = oCalendarTo.get(Calendar.MONTH) - oCalendarFrom.get(Calendar.MONTH) + 1;
        }
        else
        {
            iMonthCount = oCalendarFrom.get(Calendar.MONTH) - oCalendarTo.get(Calendar.MONTH) + 12
                * (oCalendarFrom.get(Calendar.YEAR) * oCalendarTo.get(Calendar.YEAR)) + 1;
        }

        Grid[] aoGrids = new Grid[iMonthCount];

        Tabbox oTabbox = new Tabbox();

        Rows oRows = new Rows();
        Row oRow = new Row();

        GregorianCalendar oTempCalendar = (GregorianCalendar) oCalendarFrom.clone();
        int iCurrentShift = 0;

        boolean bTemp = true;
        int iTempMonth = oTempCalendar.get(Calendar.MONTH);
        int iCurrentGrid = 0;

        aoGrids[iCurrentGrid] = new Grid();
        fillAuxHeaders(aoGrids[iCurrentGrid], _oShiftplan);

        Tabs oTabs = new Tabs();
        Tab oTab = new Tab();

        SimpleDateFormat oFormat = new SimpleDateFormat("MMMM");

        oTab.setLabel(oFormat.format(oTempCalendar.getTime()) + " " + oTempCalendar.get(Calendar.YEAR));

        Tabpanels oTabpanels = new Tabpanels();
        Tabpanel oTabpanel = new Tabpanel();

        oRows.setParent(aoGrids[iCurrentGrid]);
        aoGrids[iCurrentGrid].setParent(oTabpanel);
        oTab.setParent(oTabs);
        oTabpanel.setParent(oTabpanels);

        oTabs.setParent(oTabbox);
        oTabpanels.setParent(oTabbox);
        oTabbox.setParent(this);
        
        //TODO fälle prüfen wenn keine nefs oder rtw da sind
        int iNEFCount = _oShiftplan.getNEFShifts().get(0).getTeamCount();
        int iRTWCount = _oShiftplan.getRTWShifts().get(0).getTeamCount();
        int iSTBCount = 0;

        if (_oShiftplan.getSTBShifts().size() != 0)
        {
            iSTBCount = _oShiftplan.getSTBShifts().get(0).getTeamCount();
        }

        while (bTemp)
        {
            if (oTempCalendar.get(Calendar.MONTH) != iTempMonth)
            {
                iTempMonth = oTempCalendar.get(Calendar.MONTH);
                iCurrentGrid++;

                aoGrids[iCurrentGrid] = new Grid();
                fillAuxHeaders(aoGrids[iCurrentGrid], _oShiftplan);

                oTab = new Tab();
                oTab.setLabel(oFormat.format(oTempCalendar.getTime()) + " "
                    + oTempCalendar.get(Calendar.YEAR));

                oTabpanel = new Tabpanel();
                oRows = new Rows();

                oRows.setParent(aoGrids[iCurrentGrid]);
                aoGrids[iCurrentGrid].setParent(oTabpanel);
                oTab.setParent(oTabs);
                oTabpanel.setParent(oTabpanels);
            }
            bTemp = DateHelper.before(oTempCalendar, oCalendarTo);
            oRow = new Row();
            (new Label(oTempCalendar.get(GregorianCalendar.DAY_OF_MONTH) + ".")).setParent(oRow);

            fillWithTeamType(_oShiftplan, oRow, oTempCalendar, iNEFCount, Teams.nef);
            fillWithTeamType(_oShiftplan, oRow, oTempCalendar, iRTWCount, Teams.rtw);
            fillWithTeamType(_oShiftplan, oRow, oTempCalendar, iSTBCount, Teams.standby);
            
            oRow.setParent(oRows);
            long lTime = oTempCalendar.getTimeInMillis();
            lTime += 24 * 60 * 60 * 1000;
            oTempCalendar.setTimeInMillis(lTime);
            iCurrentShift++;
        }
    }

    private void fillWithTeamType(Shiftplan oShiftplan, Row oRow, GregorianCalendar oCalendar,
        int iTeamCount, Teams oTeamType)
    {
        Shift oShift = oShiftplan.getShiftByDate(oCalendar, Shifts.nightshift, oTeamType);
        
        if (oShift != null)
        {
            int iCurrentTeam = 0;
            for (Team oTeam : oShift.getTeams())
            {
                fillWithTeam(oTeam, oRow);
                oShift = oShiftplan.getShiftByDate(oCalendar, Shifts.dayshift, oTeamType);
                
                if (oShift != null)
                {                    
                    fillWithTeam(oShift.getTeam(iCurrentTeam), oRow);
                }
                else
                {
                    (new Label()).setParent(oRow);
                }
                iCurrentTeam++;
            }
        }
        else
        {
            for(int i = 0; i < iTeamCount; i++)
            {
                (new Label()).setParent(oRow);
                (new Label()).setParent(oRow);
            }
        }
    }

    private void fillWithTeam(Team oTeam, Row oRow)
    {
        if(oTeam == null)
        {
            (new Label()).setParent(this);
            return;
        }

        TeamBox oTeamBox = new TeamBox(oTeam, _oPersonPanel);
        oTeamBox.setParent(oRow);
    }

    private void fillAuxHeaders(Grid oGrid, Shiftplan oShiftplan)
    {
        Auxhead oAuxhead = new Auxhead();
        Auxheader oAuxheader = new Auxheader();

        int iNEFCount = oShiftplan.getNEFShifts().get(0).getTeamCount();
        int iRTWCount = oShiftplan.getRTWShifts().get(0).getTeamCount();
        int iSTBCount = 0;

        if (oShiftplan.getSTBShifts().size() != 0)
        {
            iSTBCount = oShiftplan.getSTBShifts().get(0).getTeamCount();
        }

        oAuxheader.setColspan(1);
        oAuxheader.setParent(oAuxhead);

        for (int i = 1; i <= iNEFCount; i++)
        {
            oAuxheader = new Auxheader();
            oAuxheader.setColspan(2);
            oAuxheader.setLabel(i + ". NEF-Team");
            oAuxheader.setParent(oAuxhead);
        }
        oAuxhead.setParent(oGrid);

        for (int i = 1; i <= iRTWCount; i++)
        {
            oAuxheader = new Auxheader();
            oAuxheader.setColspan(2);
            oAuxheader.setLabel(i + ". RTW-Team");
            oAuxheader.setParent(oAuxhead);
        }
        oAuxhead.setParent(oGrid);

        for (int i = 1; i <= iSTBCount; i++)
        {
            oAuxheader = new Auxheader();
            oAuxheader.setColspan(2);
            oAuxheader.setLabel(i + ". Bereitschaft-Team");
            oAuxheader.setParent(oAuxhead);
        }
        oAuxhead.setParent(oGrid);

        Columns oColumns = new Columns();
        Column oColumn = new Column();
        oColumn.setWidth("30px");
        oColumn.setParent(oColumns);

        for (int i = 0; i < (iNEFCount + iRTWCount + iSTBCount) * 2; i++)
        {
            oColumn = new Column();
            oColumn.setLabel("Fahrer   -   Sanitäter");
            oColumn.setWidth("150px");
            oColumn.setParent(oColumns);
        }

        oColumns.setParent(oGrid);
    }
}
