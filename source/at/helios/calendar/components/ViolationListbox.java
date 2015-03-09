package at.helios.calendar.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import at.helios.calendar.components.shiftcube.ShiftBox;


/**
 * Listbox, die Regelverletzungen darstellt
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   15.01.2009 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ViolationListbox extends Listbox
{
    /**
     * Standard Konstruktor, füllt GUI-Komponente
     **/
    public ViolationListbox()
    {
        this.setId("ViolationListBox");
        this.setHeight("140px");
        this.setWidth("550px");
    }
    
    /**
     * Fügt Regelverletzung hinzu
     * @param oShiftBox   ShiftBox, bei der Fehler auftritt
     * @param sMessage    Fehlermeldung
     **/
    public void addViolation(ShiftBox oShiftBox, String sMessage)
    {
        Listitem oListitem = new Listitem();
        Listcell oListcell = new Listcell();
        SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
        
        oListitem.setAttribute("ShiftBoxReference", oShiftBox);
        (new Label(oFormat.format(oShiftBox.getShiftIdentifier().getDate().getTime()))).setParent(oListcell);
        oListcell.setParent(oListitem);
        
        oListcell = new Listcell();
        (new Label(sMessage)).setParent(oListcell);
        oListcell.setParent(oListitem);
        
        oListitem.setParent(this);
    }
    
    /**
     * Entfernt Regelverletzung
     * @param oShiftBox    ShiftBox, bei der Regelverletzung auftritt
     **/
    @SuppressWarnings("unchecked")
    public void removeViolations(ShiftBox oShiftBox)
    {
        List<Listitem> oList = new ArrayList<Listitem>();
        
        for(Listitem oListitem :(List<Listitem>) this.getChildren())
        {
            if(oListitem.getAttribute("ShiftBoxReference").equals(oShiftBox))
            {
                oList.add(oListitem);
            }    
        }
        
        for(Listitem oListitem : oList)
        {
            if(oListitem.getAttribute("ShiftBoxReference").equals(oShiftBox))
            {
                this.removeChild(oListitem);
            } 
        }
    }
}
