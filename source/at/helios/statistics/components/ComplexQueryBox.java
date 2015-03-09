package at.helios.statistics.components;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.impl.api.InputElement;

import at.helios.common.PersonComboBox;
import at.helios.common.ShiftplanComboxBox;
import at.helios.model.Person;
import at.helios.sheduling.Teams;

/**
 * Stellt die komplexe Abfragenbox dar
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   27.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ComplexQueryBox extends Groupbox
{
    private PersonComboBox     _oPersonCombobox      = new PersonComboBox();
    private Checkbox           _oShiftplanCheckbox   = new Checkbox();
    private Checkbox           _oTeamTypeCheckbox    = new Checkbox();
    private Checkbox           _oTrainingCheckbox    = new Checkbox();
    private Checkbox           _oPeriodCheckbox      = new Checkbox();
    private Checkbox           _oShiftTypeCheckbox   = new Checkbox();
    private Checkbox           _oDayCheckbox         = new Checkbox();

    private Checkbox           _oConfirmedCheckbox   = new Checkbox();
    private ShiftplanComboxBox _oShiftplanCombobox   = new ShiftplanComboxBox();
    private Checkbox[]         _aoTeamTypeCheckboxes = new Checkbox[3];
    private Checkbox[]         _aoTrainingCheckboxes = new Checkbox[3];
    private Datebox            _oFromDatebox         = new Datebox();
    private Datebox            _oToDatebox           = new Datebox();
    private Radiogroup         _oShiftTypeRadiogroup = new Radiogroup();
    private Checkbox[]         _aoDayCheckboxes      = new Checkbox[7];

    private Grid               _oGrid;

    /**
     * Standardkonstruktor, füllt die GUI-Komponente
     **/
    public ComplexQueryBox()
    {
        this.setMold("3d");
        this.setStyle("padding-top: 10px");
        this.setWidth("650px");

        _oShiftplanCheckbox.addForward("onCheck", this, "onUpdateRow");
        _oTeamTypeCheckbox.addForward("onCheck", this, "onUpdateRow");
        _oTrainingCheckbox.addForward("onCheck", this, "onUpdateRow");
        _oPeriodCheckbox.addForward("onCheck", this, "onUpdateRow");
        _oShiftTypeCheckbox.addForward("onCheck", this, "onUpdateRow");
        _oDayCheckbox.addForward("onCheck", this, "onUpdateRow");

        Caption oCaption = new Caption("Komplexe Abfrage");
        oCaption.setParent(this);

        _oGrid = new Grid();

        Rows oRows = new Rows();
        Hbox oHbox = new Hbox();

        Row oRow = new Row();
        (new Label()).setParent(oRow);
        (new Label("Person")).setParent(oRow);
        _oPersonCombobox.setPersonPanel(((StatisticsView) Sessions.getCurrent().getAttribute(
            "PersonPanelOwner")).getPersonPanel());
        _oPersonCombobox.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        (new Label()).setParent(oRow);
        (new Label("Bestätigung")).setParent(oRow);
        _oConfirmedCheckbox.setParent(oHbox);
        (new Label("unbestätigte Dienste ignorieren")).setParent(oHbox);
        oHbox.setParent(oRow);
        oRow.setParent(oRows);

        oHbox = new Hbox();

        oRow = new Row();
        _oShiftplanCheckbox.setParent(oRow);
        (new Label("Schichtplan")).setParent(oRow);
        _oShiftplanCombobox.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        _oTeamTypeCheckbox.setParent(oRow);
        (new Label("Teamart")).setParent(oRow);

        String[] asTeamTypes = { "NEF", "RTW", "Bereitschaft"};

        for (int i = 0; i < _aoTeamTypeCheckboxes.length; i++)
        {
            _aoTeamTypeCheckboxes[i] = new Checkbox();
            _aoTeamTypeCheckboxes[i].setParent(oHbox);
            (new Label(asTeamTypes[i])).setParent(oHbox);
        }

        oHbox.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        oHbox = new Hbox();
        _oTrainingCheckbox.setParent(oRow);
        (new Label("Dienstart")).setParent(oRow);

        String[] asTrainings = { "Fahrer", "Sanitäter", "Probehelfer"};

        for (int i = 0; i < _aoTrainingCheckboxes.length; i++)
        {
            _aoTrainingCheckboxes[i] = new Checkbox();
            _aoTrainingCheckboxes[i].setParent(oHbox);
            (new Label(asTrainings[i])).setParent(oHbox);
        }

        oHbox.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        _oPeriodCheckbox.setParent(oRow);
        (new Label("Zeitraum")).setParent(oRow);
        oHbox = new Hbox();
        (new Label("von")).setParent(oHbox);
        _oFromDatebox.setParent(oHbox);
        (new Label("bis")).setParent(oHbox);
        _oToDatebox.setParent(oHbox);
        oHbox.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        _oShiftTypeCheckbox.setParent(oRow);
        (new Label("Schichtart")).setParent(oRow);
        (new Radio("Nachtschicht")).setParent(_oShiftTypeRadiogroup);
        (new Radio("Tagschicht")).setParent(_oShiftTypeRadiogroup);
        _oShiftTypeRadiogroup.setParent(oRow);
        oRow.setParent(oRows);

        oRow = new Row();
        _oDayCheckbox.setParent(oRow);
        (new Label("Tag")).setParent(oRow);
        oHbox = new Hbox();

        String[] asDays = { "MO", "DI", "MI", "DO", "FR", "SA", "SO"};

        for (int i = 0; i < _aoDayCheckboxes.length; i++)
        {
            _aoDayCheckboxes[i] = new Checkbox();
            _aoDayCheckboxes[i].setParent(oHbox);
            (new Label(asDays[i])).setParent(oHbox);
        }

        oHbox.setParent(oRow);
        oRow.setParent(oRows);

        oRow.setParent(oRows);
        oRows.setParent(_oGrid);
        _oGrid.setParent(this);

        onUpdateRow();
    }

    /**
     * Deaktiviert/ Aktiviert die Inhalte einer Datei
     **/
    @SuppressWarnings("unchecked")
    public void onUpdateRow()
    {
        for (Component oComponent : (Collection<Component>) _oGrid.getChildren())
        {
            if (oComponent instanceof Rows)
            {
                for (Row oRow : (Collection<Row>) oComponent.getChildren())
                {
                    updateRow(oRow);
                }
            }
        }

    }

    private void updateRow(Row oRow)
    {
        if (!(oRow.getFirstChild() instanceof Checkbox))
        {
            return;
        }
        Checkbox oCheckbox = (Checkbox) oRow.getFirstChild();
        Component oComponent = oCheckbox.getNextSibling();

        while (oComponent != null)
        {
            setDisabled(oComponent, !oCheckbox.isChecked());

            if (oComponent instanceof Hbox || oComponent instanceof Radiogroup)
            {
                Component oComponent2 = oComponent.getFirstChild();

                while (oComponent2 != null)
                {
                    setDisabled(oComponent2, !oCheckbox.isChecked());
                    oComponent2 = oComponent2.getNextSibling();
                }
            }

            oComponent = oComponent.getNextSibling();
        }
    }

    private void setDisabled(Component oComponent, boolean bDisabled)
    {
        if (oComponent instanceof InputElement)
        {
            ((InputElement) oComponent).setDisabled(bDisabled);
        }
        else if (oComponent instanceof Checkbox)
        {
            ((Checkbox) oComponent).setDisabled(bDisabled);
        }
    }

    /**
     * Gibt ein SQL-Statement zurück, dass die Anzahl der Dienste, auf die die eingegebenen Vorgaben zutreffen, auswählt
     * @return    SQL-Statement
     **/
    public String getCountStatement()
    {
        String sStatement = "SELECT COUNT(*) 'nShiftCount' FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId";

        return sStatement + getConditions();
    }

    /**
     * Gibt ein SQL-Statement zurück, dass Informationen zu den Diensten, auf die die eingegebenen Vorgaben zutreffen, auswählt
     * @return    SQL-Statement
     **/
    public String getShiftStatement()
    {
        String sStatement = "SELECT a.bConfirmed, b.cTeamType, c.dShift, c.cShiftType,";
        sStatement += " (SELECT 'Fahrer' FROM tPerson d WHERE c.nDriverId = % AND d.nPersonId = %) 'Driver',";
        sStatement += " (SELECT 'Sanitäter' FROM tPerson d WHERE c.nMediId = % AND d.nPersonId = %) 'Medi',";
        sStatement += " (SELECT 'Probehelfer' FROM tPerson d WHERE c.nAdditionalId = % AND d.nPersonId = %) 'Additional'";
        sStatement += " FROM tShiftplan a, tShiftTree b, tShiftNode c WHERE a.nShiftplanId = b.nShiftplanId AND b.nShiftTreeId = c.nShiftTreeId";

        sStatement = sStatement.replaceAll("%", Integer.toString(((Person) _oPersonCombobox.getSelectedItem()
            .getValue()).getPersonId()));

        return sStatement + getConditions();
    }

    private String getConditions()
    {
        this.invalidate();
        String sStatement = "";

        if (_oShiftplanCheckbox.isChecked())
        {
            sStatement += " AND a.nShiftplanId = "
                + Integer.toString((Integer) _oShiftplanCombobox.getSelectedItem().getValue());
        }

        if (_oTeamTypeCheckbox.isChecked())
        {
            sStatement += " AND (";
            String sTemp = "";
            Teams[] aoTemp = Teams.values();

            for (int i = 0; i < _aoTeamTypeCheckboxes.length; i++)
            {
                if (_aoTeamTypeCheckboxes[i].isChecked())
                {
                    if (sTemp.length() != 0)
                    {
                        sTemp += " OR ";
                    }

                    sTemp += "b.cTeamType = '" + aoTemp[i].toString() + "'";
                }
            }
            sStatement += sTemp + ")";
        }

        if (_oTrainingCheckbox.isChecked())
        {
            sStatement += " AND (";

            String[] asTemp = { "c.nDriverId", "c.nMediId", "c.nAdditionalId"};
            String sTemp = "";

            for (int i = 0; i < _aoTrainingCheckboxes.length; i++)
            {
                if (_aoTrainingCheckboxes[i].isChecked())
                {
                    if (sTemp.length() != 0)
                    {
                        sTemp += " OR ";
                    }

                    sTemp += asTemp[i] + " = "
                        + (_oPersonCombobox.getSelectedPerson()).getPersonId();
                }
            }

            sStatement += sTemp + ")";
        }
        else
        {
            sStatement += " AND (c.nDriverId = % OR c.nMediId = % OR c.nAdditionalId = %)".replaceAll("%",
                Integer.toString(((Person) _oPersonCombobox.getSelectedItem().getValue()).getPersonId()));
        }

        if (_oPeriodCheckbox.isChecked())
        {
            SimpleDateFormat oFormat = new SimpleDateFormat("yyyy-MM-dd");

            sStatement += " AND c.dShift >= '%'".replaceAll("%", oFormat.format(_oFromDatebox.getValue()));
            sStatement += " AND c.dShift <= '%'".replaceAll("%", oFormat.format(_oToDatebox.getValue()));
        }

        if (_oShiftTypeCheckbox.isChecked())
        {
            if (_oShiftTypeRadiogroup.getSelectedItem().getLabel().equals("Nachtschicht"))
            {
                sStatement += " AND c.cShiftType = 'nightshift'";
            }
            else if (_oShiftTypeRadiogroup.getSelectedItem().getLabel().equals("Tagschicht"))
            {
                sStatement += " AND c.cShiftType = 'dayshift'";
            }
        }

        if (_oDayCheckbox.isChecked())
        {
            sStatement += " AND (";
            String sTemp = "";

            for (int i = 0; i < _aoDayCheckboxes.length; i++)
            {
                if (_aoDayCheckboxes[i].isChecked())
                {
                    if (sTemp.length() != 0)
                    {
                        sTemp += " OR ";
                    }

                    sTemp += "WEEKDAY(c.dShift) = " + i;
                }
            }
            sStatement += sTemp + ")";
        }

        if (_oConfirmedCheckbox.isChecked())
        {
            sStatement += " AND a.bConfirmed";
        }

        return sStatement;
    }

    /**
     * Gibt ausgewählte Person zurück
     * @return    ausgewählte Person
     **/
    public Person getSelectedPerson()
    {
        return _oPersonCombobox.getSelectedPerson();
    }
}
