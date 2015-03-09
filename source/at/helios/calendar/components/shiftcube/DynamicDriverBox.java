/**
 * 
 */
package at.helios.calendar.components.shiftcube;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import at.helios.sheduling.ShiftCube;
import at.helios.sheduling.shiftcube.DriverIdentifier;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus.scherer 03.05.2010 Neuerstellung
 * </PRE>
 */
public class DynamicDriverBox extends Combobox
{
    private static final String IMAGE_VIOLATION_INTERVAL     = "/design/violation_interval.png";
    private static final String IMAGE_VIOLATION_COUNT        = "/design/violation_count.png";
    private static final String IMAGE_VIOLATION_BOTH         = "/design/violation_both.png";
    
    private ShiftCube _oShiftCube;
    private DriverIdentifier _oSelectedDriver;
    
    
    public DynamicDriverBox()
    {
        
    }
    
    public DynamicDriverBox(ShiftIdentifier oShiftIdentifier, ShiftCube oShiftCube)
    {
        _oShiftCube = oShiftCube;
    }

    public void onOpen()
    {    
        Comboitem oComboitem = new Comboitem();
        oComboitem.setLabel("aasdfasdfdsa");
        oComboitem = new Comboitem();
        oComboitem.setParent(this);
        oComboitem = new Comboitem();
        oComboitem.setLabel("aasdfasdfdsa");
        oComboitem.setParent(this);
        oComboitem = new Comboitem();
        oComboitem.setLabel("aasdfasdfdsa");
        oComboitem.setParent(this);
        
        oComboitem = new Comboitem();
        oComboitem.setLabel("ezruzrrt1");
        oComboitem.setImage(IMAGE_VIOLATION_COUNT);
        oComboitem.setParent(this);
               
        oComboitem.setTooltip("hallhoaldsaf");
        
        oComboitem = new Comboitem();
        oComboitem.setLabel("t2");
        oComboitem.setImage(IMAGE_VIOLATION_INTERVAL);
        oComboitem.setParent(this);
        
        oComboitem = new Comboitem();
        oComboitem.setLabel("ert3");
        oComboitem.setImage(IMAGE_VIOLATION_BOTH);
        oComboitem.setParent(this);
    }
    
    public void onSelect()
    {
        System.out.println("haddihaddi");
    }
}
