package at.helios.dataview.jasper;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * 
 * Diese klasse stellt eine virtuelle Datenbank
 * für den JasperReport dar, und gibt Daten in 
 * einer Form zurück, dass sie direkt in
 * das PDF-File eingetragen werden können.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   28.03.2009  Erstkommentierung
 *         mas   22.01.2010  Führende Nullen bei EMT-Nummern hinzugefügt
 *         mas   12.03.2010  Führende Nullen bei Datumswerten entfernt -.-
 * </PRE>
 *
 */
public class DataSource implements JRDataSource
{
    private Collection<ShiftplanBean> _coPlan;
    private Iterator<ShiftplanBean>   _oIterator;
    private ShiftplanBean             _oData;

    /**
     * 
     * Setzt Information über die Periode
     * von welcher Daten bereitgestellt werden sollen.
     * 
     * @param sShiftType - Schicht des Subreports (z.B. rtw1)
     * @param sPlanId - SchictplanId
     * @param sPeriod - Periode
     * 
     * @throws SQLException
     *
     */
    public DataSource(String sShiftType, String sPlanId, String sPeriod) throws SQLException
    {
        int iPlanId = Integer.valueOf(sPlanId);
        int iPeriod = Integer.valueOf(sPeriod);

        _coPlan = JasperShiftPlanDAO.getSubShiftPlan(sShiftType, iPlanId, iPeriod);

        _oIterator = _coPlan.iterator();
    }

    /**
     * Setzt den Pointer auf den nächsten Datensatz
     * falls einer vorhanden ist.
     * 
     * @return true --> auf nächsten Datensatz gesetzt
     *         false --> kein weiterer Datensatz vorhanden.
     */
    public boolean next() throws JRException
    {
        if (_oIterator.hasNext())
        {
            _oData = _oIterator.next();
            return true;
        }
        else
            return false;
    }

    /**
     * Gibt den Wert von oJrField zurück
     */
    public Object getFieldValue(JRField oJrField) throws JRException
    {
        String sFieldName = oJrField.getName();
        if (sFieldName != null && sFieldName.length() > 0)
        {
            return this.getField(sFieldName);
        }
        return null;
    }

    /**
     * Gibt den Wert von sField zurück
     * 
     * @param sField
     * @return Wert von sField
     *
     */
    private Object getField(String sField)
    {
        if (sField.equalsIgnoreCase("Medi"))
            return checkValue(_oData.getMedi());
        else if (sField.equalsIgnoreCase("Driver"))
            return checkValue(_oData.getDriver());
        else if (sField.equalsIgnoreCase("Additional"))
            return checkValue(_oData.getAdditional());
        else if (sField.equalsIgnoreCase("Date"))
            return _oData.getDay()+"";

        return null;
    }

    /**
     * 
     * Prüft den Wert des Feldes.
     * Ist dieser 0 --> keine Informationen vorhanden
     * wird ein leerer String zurückgegeben.
     * 
     * @param iValue
     * @return - neuer Wert des Feldes.
     *
     */
    private String checkValue(int iValue)
    {
        if (iValue == 0)
            return "";
        else
        {
            DecimalFormat oFormat = new DecimalFormat("0000");
            return oFormat.format(iValue);
        }
    }
}
