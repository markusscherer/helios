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

import at.helios.calendar.helper.HolidayHelper;

/**
 * Box zum Eintragen von Urlauben
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   29.03.2009 Erstkommentierung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class HolidayBox extends Hbox
{
    Listbox _oListbox;
    Vbox    _oVbox;
    Datebox _oDatebox;
    HolidayHelper _oHolidayHelper;

    /**
     * Standard Konstruktor, füllt GUI-Komponente
     * @param oHolidayHelper
     **/
    public HolidayBox(HolidayHelper oHolidayHelper)
    {
        _oHolidayHelper = oHolidayHelper;
        
        _oListbox = new Listbox();
        _oListbox.setHeight("130px");
        _oListbox.setWidth("95px");
        _oListbox.setParent(this);
        
        (new Separator()).setParent(this);
        
        _oVbox = new Vbox();

        _oDatebox = new Datebox();
        _oDatebox.setParent(_oVbox);

        Button oAddButton = new Button();
        oAddButton.setLabel("Feiertag hinzufügen");
        oAddButton.addForward("onClick", this, "onAddHoliday");
        oAddButton.setParent(_oVbox);
        oAddButton.setWidth("150px");

        Button oDeleteButton = new Button();
        oDeleteButton.setLabel("Feiertag löschen");
        oDeleteButton.addForward("onClick", this, "onDeleteHoliday");
        oDeleteButton.setParent(_oVbox);
        oDeleteButton.setWidth("150px");
        
        _oVbox.setParent(this);

    }

    /**
     * Wird ausgeführt, wenn auf den "Feiertag hinzufügen"-Button klickt
     **/
    @SuppressWarnings("unchecked")
    public void onAddHoliday()
    {
        Date oDate = _oDatebox.getValue();

        if (oDate == null)
        {
            return;
        }

        SimpleDateFormat oFormater = new SimpleDateFormat("dd.MM.yyyy");

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
        oListitem.setParent(_oListbox);
        
        _oHolidayHelper.addHoliday(oDate);
    }

    /**
     * Wird ausgeführt, wenn auf den "Feiertag löschen"-Button klickt
     **/
    public void onDeleteHoliday()
    {
        if (_oListbox.getSelectedIndex() != -1)
        {
            int iDay = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(0, 2));
            int iMonth = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(3, 5));
            int iYear = Integer.parseInt(((Label) _oListbox.getSelectedItem().getFirstChild().getFirstChild()).getValue().substring(6, 10));
            
            GregorianCalendar oCalendar = new GregorianCalendar(iYear, iMonth-1, iDay);
            
            _oHolidayHelper.removeHoliday(oCalendar);
            _oListbox.removeItemAt(_oListbox.getSelectedIndex());
        }
    }
    
    /**
     * Getter für _oVbox
     * @return    _oVbox
     **/
    public Vbox getVbox()
    {
        return _oVbox;
    }
}
