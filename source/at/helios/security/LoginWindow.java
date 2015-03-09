package at.helios.security;

import java.util.Date;
import java.util.Locale;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import at.helios.common.CacheThread;
import at.helios.common.DepartmentComboBox;
import at.helios.common.RedirectHelper;
import at.helios.model.Department;
import at.helios.model.dao.PersonDAO;
import at.helios.model.dao.TrainingDAO;

/**
 * Login-Fenster
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   07.03.2009 Neuerstellung
 *         mas   12.01.2010 kleineren Bug bei falschen Login-Daten behoben
 * </PRE>
 **/
@SuppressWarnings("serial")
public class LoginWindow extends Window
{
    DepartmentComboBox _oDepartmentComboBox = new DepartmentComboBox();
    Textbox            _oLoginBox           = new Textbox();
    Textbox            _oPasswordBox        = new Textbox();
    Label              _oLabel              = new Label("Falsche Logindaten");

    /**
     * Standard Konstruktor, füllt GUI-Komponente aus
     **/
    public LoginWindow()
    {
        //TODO: Das könnte wo anders hin
        Locale.setDefault(Locale.GERMAN);
        
        Grid oGrid = new Grid();
        Rows oRows = new Rows();
        Row oRow = new Row();

        _oPasswordBox.setType("password");
        _oPasswordBox.setWidth("191px");
        _oPasswordBox.addForward("onOK", this, "onLogin");
        
        _oLoginBox.setWidth("191px");

        (new Label("Dienststelle")).setParent(oRow);
        _oDepartmentComboBox.setParent(oRow);
        oRow.setParent(oRows);
        oRow = new Row();
        (new Label("Benutzername")).setParent(oRow);
        _oLoginBox.setParent(oRow);
        oRow.setParent(oRows);
        oRow = new Row();
        (new Label("Passwort")).setParent(oRow);
        _oPasswordBox.setParent(oRow);
        oRow.setParent(oRows);
        oRow = new Row();
        
        (new Label()).setParent(oRow);
        Button oButton = new Button("login");
        oButton.addForward("onClick", this, "onLogin");
        oButton.setParent(oRow);
        
        oRow.setParent(oRows);
        
        oRows.setParent(oGrid);

        oGrid.setParent(this);
        _oLabel.setParent(this);
        _oLabel.setVisible(false);
    }

    /**
     * Wird ausgeführt, wenn der  "login"-Button geklickt wird, oder im Passwort-Feld Enter gedrückt wird
     **/
    public void onLogin()
    {
        System.out.println(new Date() + ": Login on System.");
        if (AuthenticationManager.isValidUser((Department) _oDepartmentComboBox.getSelectedItem().getValue(),
            _oLoginBox.getValue(), _oPasswordBox.getValue()))
        {
            Sessions.getCurrent().setAttribute("authenticated", true);
            Sessions.getCurrent().setAttribute("department", _oDepartmentComboBox.getSelectedItem().getValue());
            
            TrainingDAO oTrainingDAO = new TrainingDAO();
            oTrainingDAO.findAll();
            
            PersonDAO oPersonDAO = new PersonDAO();
            oPersonDAO.findAll();
            
            CacheThread oCacheThread = new CacheThread();
            oCacheThread.start();

            Executions.sendRedirect(RedirectHelper.getOverviewLink());
        }
        else
        {
            _oLabel.setVisible(true);
        }
    }
}
