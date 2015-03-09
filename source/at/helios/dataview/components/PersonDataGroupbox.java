package at.helios.dataview.components;

import org.zkoss.zul.Caption;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import at.helios.dataview.PersonDataRules;
import at.helios.model.Person;

/**
 * 
 * Diese Klasse ist eine Groupbox, die
 * alle Daten über die Personen darstellt:
 *  - Vor- und Nachname
 *  - E-Mail
 *  - Minimaler Schichtintervall
 *  - Maximale Schichten
 *  - Dienstnummer
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   28.03.2009  Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class PersonDataGroupbox extends Groupbox implements PersonDataRules
{
    private Textbox _oMinShiftInterval = new Textbox();
    private Textbox _oMaxShiftCount    = new Textbox();
    private Person  _oPerson;

    /**
     * 
     * Ruft die Methode zum
     * initialisieren der GUI auf.
     *
     */
    public PersonDataGroupbox()
    {
        initGui();
    }

    /**
     * Initialisiert die Gui
     */
    public void initGui()
    {
        this.setMold("3d");
        this.appendChild(new Caption("Personendaten"));

        Grid oGrid = new Grid();

        Rows oRows = new Rows();

        Row oRow;

        oRow = new Row();

        oRow.appendChild(new Label("Vorname"));
        Textbox oTextbox = new Textbox();
        oTextbox.setWidth("220px");
        oTextbox.setId("P_forename");
        oTextbox.setReadonly(true);
        oTextbox.setParent(oRow);

        oRow.appendChild(new Label("Dienstnummer"));
        oTextbox = new Textbox();
        oTextbox.setWidth("220px");
        oTextbox.setId("P_emt");
        oTextbox.setReadonly(true);
        oTextbox.setParent(oRow);

        oRow.setParent(oRows);

        oRow = new Row();
        oRow.appendChild(new Label("Nachname"));
        oTextbox = new Textbox();
        oTextbox.setWidth("220px");
        oTextbox.setId("P_surname");
        oTextbox.setReadonly(true);
        oTextbox.setParent(oRow);

        oRow.appendChild(new Label("E-Mail"));
        oTextbox = new Textbox();
        oTextbox.setWidth("220px");
        oTextbox.setId("P_mail");
        oTextbox.setReadonly(true);
        oTextbox.setParent(oRow);

        oRow.setParent(oRows);

        oRow = new Row();

        oRow.appendChild(new Label("minimaler Schichtinterval"));
        _oMinShiftInterval = new Textbox();
        _oMinShiftInterval.setWidth("220px");
        _oMinShiftInterval.setId("P_minInterval");
        _oMinShiftInterval.setReadonly(false);
        _oMinShiftInterval.setParent(oRow);

        oRow.appendChild(new Label("maximale Schichtanzahl"));
        _oMaxShiftCount = new Textbox();
        _oMaxShiftCount.setWidth("220px");
        _oMaxShiftCount.setId("P_maxAnzahl");
        _oMaxShiftCount.setReadonly(false);
        _oMaxShiftCount.setParent(oRow);

        oRow.setParent(oRows);

        oRows.setParent(oGrid);

        oGrid.setParent(this);
    }

    /**
     * Setzt alle Daten von oPerson
     */
    public void setValues(Person oPerson)
    {
        _oPerson = oPerson;
        ((Textbox) this.getFellowIfAny("P_forename")).setValue(_oPerson.getForename());
        ((Textbox) this.getFellowIfAny("P_surname")).setValue(_oPerson.getSurname());
        ((Textbox) this.getFellowIfAny("P_emt")).setValue(_oPerson.getEMTNumber());
        ((Textbox) this.getFellowIfAny("P_mail")).setValue(_oPerson.getEmail());
        _oMinShiftInterval.setValue(String.valueOf(_oPerson.getMinShiftInterval()));
        _oMaxShiftCount.setValue(String.valueOf(_oPerson.getMaxShiftCount()));
    }

    /**
     * Speichert alle eingetragenen Daten in der Person
     */
    public void save()
    {
        _oPerson.setMinShiftInterval(Integer.valueOf(_oMinShiftInterval.getValue()));
        _oPerson.setMaxShiftCount(Integer.valueOf(_oMaxShiftCount.getValue()));
    }
}