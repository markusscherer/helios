package at.helios.statistics.components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.Tabpanel;

import at.helios.common.DBHelper;
import at.helios.model.Person;

/**
 * Tabpanel, das einfache Abfragen beinhaltet
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   30.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class SimpleStatisticsTab extends Tabpanel
{
    private static final String SQL_SELECT_WEEKDAY_DISTRIBUTION           = "SELECT"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 0) 'nMondayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 1) 'nTuesdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 2) 'nWednesdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 3) 'nThursdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 4) 'nFridayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 5) 'nSaturdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 6) 'nSundayCount'";
    private static final String SQL_SELECT_SHIFTS                         = "SELECT a.bConfirmed, b.cTeamType, c.dShift, c.cShiftType, "
                                                                              + "(SELECT 'Fahrer' FROM tPerson d WHERE c.nDriverId = ? AND d.nPersonId = ?) 'Driver', "
                                                                              + "(SELECT 'Sanitäter' FROM tPerson d WHERE c.nMediId = ? AND d.nPersonId  AND d.nPersonId = ?) 'Medi', "
                                                                              + "(SELECT 'Probehelfer' FROM tPerson d WHERE c.nAdditionalId = ? AND d.nPersonId=?) 'Additional' "
                                                                              + "FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId = ?) ORDER BY c.dShift DESC";
    private static final String SQL_SELECT_CONFIRMED_WEEKDAY_DISTRIBUTION = "SELECT"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 0  AND a.bConfirmed) 'nMondayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 1  AND a.bConfirmed) 'nTuesdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 2  AND a.bConfirmed) 'nWednesdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 3  AND a.bConfirmed) 'nThursdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 4  AND a.bConfirmed) 'nFridayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 5  AND a.bConfirmed) 'nSaturdayCount',"
                                                                              + "(SELECT COUNT(*) FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId =?) AND WEEKDAY(c.dShift) = 6  AND a.bConfirmed) 'nSundayCount'";
    private static final String SQL_SELECT_CONFIRMED_SHIFTS               = "SELECT a.bConfirmed, b.cTeamType, c.dShift, c.cShiftType, "
                                                                              + "(SELECT 'Fahrer' FROM tPerson d WHERE c.nDriverId = ? AND d.nPersonId = ?) 'Driver', "
                                                                              + "(SELECT 'Sanitäter' FROM tPerson d WHERE c.nMediId = ? AND d.nPersonId  AND d.nPersonId = ?) 'Medi', "
                                                                              + "(SELECT 'Probehelfer' FROM tPerson d WHERE c.nAdditionalId = ? AND d.nPersonId=?) 'Additional' "
                                                                              + "FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId AND (c.nDriverId = ? OR c.nMediId = ? OR c.nAdditionalId = ?)  AND a.bConfirmed ORDER BY c.dShift DESC";
    private Groupbox            _oSettingsGroupbox;
    private Groupbox            _oEvaluationGroupbox;
    private Checkbox            _oConfirmedCheckbox;
    private Person              _oPerson;

    /**
     * Standard Konstruktor, füllt GUI-Komponente
     **/
    public SimpleStatisticsTab()
    {
        StatisticsView oFutureRoot = (StatisticsView) Sessions.getCurrent().getAttribute(
            "PersonPanelOwner");
        _oPerson = oFutureRoot.getSelectedPerson();

        if (_oPerson == null)
        {
            (new Label("Bitte wählen Sie links eine Person aus.")).setParent(this);
            return;
        }

        createNewSettingsBox();
    }

    /**
     * Wird ausgeführt, wenn "Wochentagsverteilung"-Button geklickt wird
     **/
    public void onGenerateWeekDayDistribution()
    {
        createNewEvaluationGroupbox();

        Person oPerson = ((StatisticsView) getFellow("StatisticsView")).getSelectedPerson();

        if (oPerson == null)
        {
            return;
        }
        
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = null;

            if (_oConfirmedCheckbox.isChecked())
            {
                oStatement = oConnection.prepareStatement(SQL_SELECT_CONFIRMED_WEEKDAY_DISTRIBUTION);
            }
            else
            {
                oStatement = oConnection.prepareStatement(SQL_SELECT_WEEKDAY_DISTRIBUTION);
            }

            for (int i = 1; i <= 21; i++)
            {
                oStatement.setInt(i, oPerson.getPersonId());
            }

            ResultSet oResultSet = oStatement.executeQuery();

            if (oResultSet.next())
            {
                Chart oChart = new Chart();

                oChart.setWidth("300px");
                oChart.setHeight("200px");
                oChart.setType("bar");

                SimpleCategoryModel oModel = new SimpleCategoryModel();

                oModel.setValue("MO", "", oResultSet.getInt("nMondayCount"));
                oModel.setValue("DI", "", oResultSet.getInt("nTuesdayCount"));
                oModel.setValue("MI", "", oResultSet.getInt("nWednesdayCount"));
                oModel.setValue("DO", "", oResultSet.getInt("nThursdayCount"));
                oModel.setValue("FR", "", oResultSet.getInt("nFridayCount"));
                oModel.setValue("SA", "", oResultSet.getInt("nSaturdayCount"));
                oModel.setValue("SO", "", oResultSet.getInt("nSundayCount"));

                oChart.setModel(oModel);
                oChart.setBgColor("#FFFFFF");
                oChart.setParent(_oEvaluationGroupbox);
                _oEvaluationGroupbox.setVisible(true);
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();

        }
        catch (SQLException e)
        {
            (new Label("Fehler in Datenbank aufgetreten.")).setParent(_oEvaluationGroupbox);
            _oEvaluationGroupbox.setVisible(true);
            e.printStackTrace();
        }
    }

    private void createNewEvaluationGroupbox()
    {
        if (_oEvaluationGroupbox != null)
        {
            this.removeChild(_oEvaluationGroupbox);
        }
        _oEvaluationGroupbox = new Groupbox();
        _oEvaluationGroupbox.setMold("3d");
        _oEvaluationGroupbox.setWidth("650px");
        this.setStyle("padding-top: 10px");
        _oEvaluationGroupbox.setParent(this);

        Caption oCaption = new Caption("Auswertung");
        oCaption.setParent(_oEvaluationGroupbox);
    }

    private void createNewSettingsBox()
    {
        this.removeChild(this.getFirstChild());

        if (_oPerson == null)
        {
            return;
        }

        if (_oSettingsGroupbox != null)
        {
            this.removeChild(_oSettingsGroupbox);
        }
        if (_oEvaluationGroupbox != null)
        {
            this.removeChild(_oEvaluationGroupbox);
        }

        _oSettingsGroupbox = new Groupbox();
        _oSettingsGroupbox.setMold("3d");
        _oSettingsGroupbox.setWidth("650px");

        Caption oCaption = new Caption("Einstellungen");
        oCaption.setParent(_oSettingsGroupbox);
        _oSettingsGroupbox.setParent(this);

        _oConfirmedCheckbox = new Checkbox();

        Grid oGrid = new Grid();
        Rows oRows = new Rows();
        Row oRow = new Row();
        Hbox oHbox = new Hbox();

        String sTemp = _oPerson.getForename() + " " + _oPerson.getSurname() + " [" + _oPerson.getEMTNumber()
            + "]";

        (new Label(sTemp)).setParent(oRow);
        oRow.setParent(oRows);

        _oConfirmedCheckbox.setParent(oHbox);
        (new Label("unbestätigte Dienste ignorieren")).setParent(oHbox);
        oHbox.setParent(oRow);

        oRow = new Row();
        oHbox = new Hbox();

        Button oButton = new Button("Wochentagsverteilung");
        oButton.addForward("onClick", this, "onGenerateWeekDayDistribution");
        oButton.setParent(oHbox);

        oButton = new Button("Letzte Dienste");
        oButton.addForward("onClick", this, "onGenerateLastShifts");
        oButton.setParent(oHbox);

        oHbox.setParent(oRow);
        oRow.setParent(oRows);
        oRows.setParent(oGrid);
        oGrid.setParent(_oSettingsGroupbox);
    }

    /**
     * Wird ausgeführt, wenn "Letzte Dienste"-Button geklickt wird
     **/
    public void onGenerateLastShifts()
    {
        createNewEvaluationGroupbox();

        Person oPerson = ((StatisticsView) getFellow("StatisticsView")).getSelectedPerson();

        if (oPerson == null)
        {
            return;
        }
        
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = null;

            if (_oConfirmedCheckbox.isChecked())
            {
                oStatement = oConnection.prepareStatement(SQL_SELECT_CONFIRMED_SHIFTS);
            }
            else
            {
                oStatement = oConnection.prepareStatement(SQL_SELECT_SHIFTS);
            }

            for (int i = 1; i <= 9; i++)
            {
                oStatement.setInt(i, oPerson.getPersonId());
            }

            ResultSet oResultSet = oStatement.executeQuery();

            if (!oResultSet.first())
            {
                (new Label("Keine Dienste gefunden")).setParent(_oEvaluationGroupbox);
                return;
            }
            else
            {
                oResultSet.relative(-1);
            }

            Listbox oListbox = new Listbox();

            SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");

            while (oResultSet.next())
            {
                Listitem oListitem = new Listitem();
                Listcell oListcell = new Listcell();

                (new Label(oFormat.format(new Date(oResultSet.getDate("dShift").getTime()))))
                    .setParent(oListcell);
                oListcell.setParent(oListitem);

                oListcell = new Listcell();

                String sTemp = oResultSet.getString("cShiftType");

                if (sTemp.equals("dayshift"))
                {
                    sTemp = "Tagschicht";
                }
                else if (sTemp.equals("nightshift"))
                {
                    sTemp = "Nachtschicht";
                }

                (new Label(sTemp)).setParent(oListcell);

                oListcell = new Listcell();

                sTemp = oResultSet.getString("cTeamType");
                if (sTemp.equals("nef") || sTemp.equals("rtw"))
                {
                    sTemp = sTemp.toUpperCase();
                }

                (new Label(sTemp)).setParent(oListcell);
                oListcell.setParent(oListitem);

                oListcell = new Listcell();

                if (oResultSet.getString("Driver") != null)
                {
                    (new Label("Fahrer")).setParent(oListcell);
                }
                else if (oResultSet.getString("Medi") != null)
                {
                    (new Label("Sanitäter")).setParent(oListcell);
                }
                else if (oResultSet.getString("Additional") != null)
                {
                    (new Label("Probehelfer")).setParent(oListcell);
                }

                oListcell.setParent(oListitem);

                oListcell = new Listcell();

                boolean bTemp = oResultSet.getBoolean("bConfirmed");

                if (bTemp)
                {
                    sTemp = "bestätigt";
                }
                else
                {
                    sTemp = "nicht bestätigt";
                }

                (new Label(sTemp)).setParent(oListcell);
                oListcell.setParent(oListitem);

                oListitem.setParent(oListbox);
            }

            oListbox.setParent(_oEvaluationGroupbox);
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            (new Label("Fehler in Datenbank aufgetreten.")).setParent(_oEvaluationGroupbox);
            _oEvaluationGroupbox.setVisible(true);
            e.printStackTrace();
        }

    }

    /**
     * Wird ausgeführt, wenn Person ausgewählt wird
     **/
    public void onPersonSelected()
    {
        StatisticsView oFutureRoot = (StatisticsView) Sessions.getCurrent().getAttribute(
            "PersonPanelOwner");
        _oPerson = oFutureRoot.getSelectedPerson();

        createNewSettingsBox();
    }
}
