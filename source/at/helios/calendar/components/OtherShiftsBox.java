package at.helios.calendar.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

import at.helios.common.PersonComboBox;
import at.helios.model.Person;

/**
 * Box zu eintragen von Schichten in anderer Dienststelle
 * @author
 * <PRE>
 *         ID    date       description
 *         mas  29.03.2009  Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class OtherShiftsBox extends Hbox
{
    
    private Listbox _oListbox;
    private Vbox _oVbox;
    private Datebox _oDatebox;
    private PersonComboBox _oPersonComboBox;

    /**
     * Standard Konstruktor, füllt GUI-Komponente auf
     **/
    public OtherShiftsBox()
    {
        _oListbox = new Listbox();
        _oListbox.setHeight("130px");
        _oListbox.setWidth("95px");
        _oListbox.setParent(this);

        _oVbox = new Vbox();

        (new Separator()).setParent(this);
        
        _oDatebox = new Datebox();
        _oDatebox.setParent(_oVbox);
        
        _oPersonComboBox = new PersonComboBox();
        _oPersonComboBox.setWidth("130px");
        _oPersonComboBox.setParent(_oVbox);
        
        Button oAddButton = new Button();
        oAddButton.setLabel("Schicht hinzufügen");
        oAddButton.addForward("onClick", this, "onAddOtherShift");
        oAddButton.setParent(_oVbox);
        oAddButton.setWidth("150px");

        Button oDeleteButton = new Button();
        oDeleteButton.setLabel("Schicht löschen");
        oDeleteButton.addForward("onClick", this, "onDeleteOtherShift");
        oDeleteButton.setParent(_oVbox);
        oDeleteButton.setWidth("150px");
        
        _oVbox.setParent(this);
    }
    
    /**
     * Wird ausgeführt, wenn "Andere Schicht hinzufügen"-Button geklick wird
     **/
    @SuppressWarnings("unchecked")
    public void onAddOtherShift()
    {
        Date oDate = _oDatebox.getValue();
        Person oPerson = _oPersonComboBox.getSelectedPerson();
        
        if (oDate == null)
        {
            return;
        }

        SimpleDateFormat oFormater = new SimpleDateFormat("dd.MM.yyyy - " + oPerson.getEMTNumber());

        for (Listitem oListitem : (List<Listitem>) _oListbox.getChildren())
        {
            if (((Label) oListitem.getFirstChild().getFirstChild()).getValue()
                .equals(oFormater.format(oDate)))
            {
                return;
            }
        }

        Listitem oListitem = new Listitem();
        Listcell oListcell = new Listcell();

        (new Label(oFormater.format(oDate))).setParent(oListcell);

        oListcell.setParent(oListitem);
        
        oListitem.setValue(oPerson);
        oListitem.setParent(_oListbox);
        
        GregorianCalendar oCalendar = new GregorianCalendar();
        oCalendar.setTime(oDate);
        
        //FIXME: machen!!
        //oPerson.addShift(new ShiftNode(oCalendar, Shifts.nightshift, 1, Teams.rtw), Training.getTrainingById(4));
    }

    /**
     * Wird ausgeführt, wenn "Andere Schicht löschen"-Button geklick wird
     **/
    public void onDeleteOtherShift()
    {
        if (_oListbox.getSelectedIndex() != -1)
        {
            int iDay = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(0, 2));
            int iMonth = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(3, 5));
            int iYear = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(6, 10));
            
            GregorianCalendar oCalendar = new GregorianCalendar(iYear, iMonth-1, iDay);
            
            ((Person)_oListbox.getSelectedItem().getValue()).removeShiftByDate(oCalendar);
            _oListbox.removeItemAt(_oListbox.getSelectedIndex());
        }
    }

}
