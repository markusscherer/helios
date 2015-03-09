package at.helios.common;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import at.helios.model.Department;
import at.helios.model.dao.DepartmentDAO;

/**
 * Combobox, in der alle Dienststellen zur Auswahl stehen
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   07.03.2009 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class DepartmentComboBox extends Combobox
{
    /**
     * Standard-Konstruktor, füllt GUI-Komponente
     **/
    public DepartmentComboBox()
    {
        this.setReadonly(true);
        DepartmentDAO oDepartmentDAO = new DepartmentDAO();
        
        for(Department oDepartment : oDepartmentDAO.findAll())
        {
            Comboitem oComboitem = new Comboitem();
            oComboitem.setLabel(oDepartment.getName());
            oComboitem.setValue(oDepartment);
            oComboitem.setParent(this);
        }
        
        if(this.getChildren().size() != 0)
        {
            this.setSelectedIndex(0);
        }
    }

}
