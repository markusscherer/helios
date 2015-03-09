package at.helios.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import at.helios.model.Department;

/**
 * Combobox, in der Schichtpläne ausgewählt werden können
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   18.02.2009 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ShiftplanComboxBox extends Combobox
{
    private static String SQL_SELECT_ID_AND_NAME = "SELECT nShiftplanId, cName FROM tShiftplan WHERE nDepartmentId = ?";

    /**
     * Standard Konstruktor, füllt GUI-Komponente
     **/
    public ShiftplanComboxBox()
    {
        try
        {
            Connection oConnection = DBHelper.getConnection();
            PreparedStatement oStatement = oConnection.prepareStatement(SQL_SELECT_ID_AND_NAME);
            oStatement.setInt(1, ((Department) Sessions.getCurrent().getAttribute("department")).getDepartmentId());

            ResultSet oResultSet = oStatement.executeQuery();

            while (oResultSet.next())
            {
                Comboitem oComboitem = new Comboitem();

                oComboitem.setLabel(oResultSet.getString("cName"));
                oComboitem.setValue(oResultSet.getInt("nShiftplanId"));

                oComboitem.setParent(this);
            }
            
            oStatement.close();
            oResultSet.close();
            oConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        if(this.getChildren().size() != 0)
        {
            this.setSelectedIndex(0);
        }
        this.setReadonly(true);
    }
}
