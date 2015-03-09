package at.helios.statistics.components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Chart;
import org.zkoss.zul.ChartModel;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tabpanel;

import at.helios.common.DBHelper;
import at.helios.model.Person;

/**
 * Tabpanel, die die komplexen Abfragen beinhaltet
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   27.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ComplexStatisticsTab extends Tabpanel
{
    private Groupbox              _oSettingsGroupbox;
    private Groupbox              _oEvaluationGroupbox;
    private ComplexQueryBox       _oQueryBox;
    private List<ComplexQueryBox> _coQueryboxes;
    private Checkbox              _oConnectCheckbox;
    private Spinner               _oConnectSpinner;
    private Combobox              _oDiagramCombobox;

    /**
     * Standardkonstruktor, füllt die GUI-Komponente
     **/
    public ComplexStatisticsTab()
    {
        _oSettingsGroupbox = new Groupbox();
        _oSettingsGroupbox.setMold("3d");
        _oSettingsGroupbox.setWidth("650px");

        _oConnectCheckbox = new Checkbox();

        _oConnectCheckbox.addForward("onCheck", this, "onCheck");

        _oConnectSpinner = new Spinner();
        _oConnectSpinner.setValue(2);
        _oConnectSpinner.setConstraint("min 2 max 10");
        _oConnectSpinner.setDisabled(true);

        _oConnectSpinner.addForward("onChanging", this, "onUpdateQueryBoxes");

        _oDiagramCombobox = new Combobox();
        _oDiagramCombobox.setReadonly(true);
        _oDiagramCombobox.setDisabled(true);

        Comboitem oComboitem = new Comboitem("Tortendiagramm");
        oComboitem.setValue("pie");
        oComboitem.setParent(_oDiagramCombobox);

        oComboitem = new Comboitem("Balkendiagramm");
        oComboitem.setValue("bar");
        oComboitem.setParent(_oDiagramCombobox);

        _oDiagramCombobox.setSelectedIndex(0);

        Caption oCaption = new Caption("Einstellungen");
        oCaption.setParent(_oSettingsGroupbox);
        _oSettingsGroupbox.setParent(this);

        Grid oGrid = new Grid();
        Rows oRows = new Rows();
        Row oRow = new Row();

        _oConnectCheckbox.setParent(oRow);
        (new Label("Verknüpfen")).setParent(oRow);
        _oConnectSpinner.setParent(oRow);
        _oDiagramCombobox.setParent(oRow);

        oRow.setParent(oRows);
        oRow = new Row();

        (new Label()).setParent(oRow);
        (new Label()).setParent(oRow);
        (new Label()).setParent(oRow);
        
        Button oButton = new Button("abfragen");
        oButton.addForward("onClick", this, "onEvaluate");
        oButton.setParent(oRow);

        oRow.setParent(oRows);

        oRows.setParent(oGrid);
        oGrid.setParent(_oSettingsGroupbox);

        _coQueryboxes = new ArrayList<ComplexQueryBox>();

        _oQueryBox = new ComplexQueryBox();
        _oQueryBox.setParent(this);
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
        _oEvaluationGroupbox.setParent(this);

        Caption oCaption = new Caption("Auswertung");
        oCaption.setParent(_oEvaluationGroupbox);
    }

    /**
     * Wird ausgeführt wenn Auswerten-Button gedrückt wurde
     **/
    public void onEvaluate()
    {
        createNewEvaluationGroupbox();

        if (_oConnectCheckbox.isChecked())
        {
            fillChart();
        }
        else
        {
            fillShifts();
        }
    }

    /**
     * Wird ausgeführt, wenn die Anzahl der Boxen verändert wird
     * @param oEvent    Eingabe-Ereignis
     **/
    public void onUpdateQueryBoxes(Event oEvent)
    {
        oEvent = ((ForwardEvent) oEvent).getOrigin();
        InputEvent oInputEvent;

        if (oEvent instanceof InputEvent)
        {
            oInputEvent = (InputEvent) oEvent;
        }
        else
        {
            return;
        }
        
        if(oInputEvent.getValue().length() == 0)
        {
            return;
        }

        generateQueryBoxes(Integer.parseInt(oInputEvent.getValue()));
    }

    /**
     * Wird ausgführt, wenn "Verknüpfen"-Checkbox angehakt wird
     **/
    public void onCheck()
    {
        if (_oConnectCheckbox.isChecked())
        {
            _oConnectSpinner.setDisabled(false);
            _oDiagramCombobox.setDisabled(false);
            generateQueryBoxes(_oConnectSpinner.getValue());
        }
        else
        {
            for (ComplexQueryBox oQueryBox : _coQueryboxes)
            {
                this.removeChild(oQueryBox);
            }
            if (_oEvaluationGroupbox != null)
            {
                this.removeChild(_oEvaluationGroupbox);
            }
            
            _coQueryboxes = new ArrayList<ComplexQueryBox>();

            _oConnectSpinner.setDisabled(true);
            _oDiagramCombobox.setDisabled(true);
            _oQueryBox = new ComplexQueryBox();
            _oQueryBox.setParent(this);
        }
    }

    private void fillShifts()
    {
        Connection oConnection = DBHelper.getConnection();

        try
        {
            PreparedStatement oStatement = oConnection.prepareStatement(_oQueryBox.getShiftStatement());

            ResultSet oResultSet = oStatement.executeQuery();

            Listbox oListbox = new Listbox();

            SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
            
            if(!oResultSet.first())
            {
                (new Label("Keine Dienste gefunden")).setParent(_oEvaluationGroupbox);
                
                oStatement.close();
                oResultSet.close();
                oConnection.close();
                
                return;
            }
            else
            {
                oResultSet.relative(-1);
            }
            
            int iCount = 0;
            
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
                iCount++;
            }
            
            oListbox.setParent(_oEvaluationGroupbox);
            (new Label(iCount + " Dienste gefunden.")).setParent(_oEvaluationGroupbox);
            
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

    private void fillChart()
    {
        Person oPerson = ((StatisticsView) getFellow("StatisticsView")).getSelectedPerson();

        if (oPerson == null)
        {
            return;
        }

        try
        {
            Connection oConnection = DBHelper.getConnection();
            PreparedStatement oStatement = null;
            ResultSet oResultSet = null;

            List<Integer> ciValues = new ArrayList<Integer>();

            for (ComplexQueryBox oQueryBox : _coQueryboxes)
            {
                oStatement = oConnection.prepareStatement(oQueryBox.getCountStatement());
                oResultSet = oStatement.executeQuery();

                if (oResultSet.next())
                {
                    ciValues.add(oResultSet.getInt("nShiftCount"));
                }
            }

            Chart oChart = new Chart();

            oChart.setWidth("300px");
            oChart.setHeight("200px");
            oChart.setType((String) _oDiagramCombobox.getSelectedItem().getValue());

            oChart.setModel(getModel(ciValues));
            oChart.setParent(_oEvaluationGroupbox);
            
            if (oResultSet != null)
            {
                oStatement.close();
                oResultSet.close();
            }
            oConnection.close();
        }
        catch (SQLException e)
        {
            (new Label("Fehler in Datenbank aufgetreten.")).setParent(_oEvaluationGroupbox);
            _oEvaluationGroupbox.setVisible(true);
            e.printStackTrace();
        }

    }

    private void generateQueryBoxes(int iCount)
    {
        if (_oQueryBox != null)
        {
            this.removeChild(_oQueryBox);
            _oQueryBox = null;
        }

        if (_oEvaluationGroupbox != null)
        {
            this.removeChild(_oEvaluationGroupbox);
        }

        for (ComplexQueryBox oQueryBox : _coQueryboxes)
        {
            this.removeChild(oQueryBox);
        }

        _coQueryboxes = new ArrayList<ComplexQueryBox>();

        for (int i = 0; i < iCount; i++)
        {
            _coQueryboxes.add(new ComplexQueryBox());
            _coQueryboxes.get(i).setParent(this);
        }
    }
    
    private ChartModel getModel(List<Integer> ciValues)
    {
        ChartModel oModel = null;

        if (_oDiagramCombobox.getSelectedItem().getValue().equals("bar"))
        {
            oModel = new SimpleCategoryModel();

            for (int i = 0; i < ciValues.size(); i++)
            {
                String sTemp = _coQueryboxes.get(i).getSelectedPerson().getEMTNumber() + " [" + (i+1) + "]";
                ((SimpleCategoryModel) oModel).setValue(sTemp, "", ciValues.get(i));
            }
        }
        else if (_oDiagramCombobox.getSelectedItem().getValue().equals("pie"))
        {
            oModel = new SimplePieModel();

            for (int i = 0; i < ciValues.size(); i++)
            {
                String sTemp = _coQueryboxes.get(i).getSelectedPerson().getEMTNumber() + " [" + (i+1) + "]";
                ((SimplePieModel) oModel).setValue(sTemp, ciValues.get(i));
            }
        }

        return oModel;
    }

}
