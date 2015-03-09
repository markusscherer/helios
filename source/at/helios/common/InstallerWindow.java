/**
 * 
 */
package at.helios.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   16.09.2009 Neuerstellung
 * </PRE>
 */
@SuppressWarnings("serial")
public class InstallerWindow extends Window
{
    private static final String IMAGE_STATUS_OK    = "/design/status_ok.png";
    private static final String IMAGE_STATUS_NOTOK = "/design/status_notok.png";

    public InstallerWindow()
    {
    }

    public void onCheckDatabasename()
    {
        checkDatabaseName();
    }

    private boolean checkDatabaseName()
    {
        String sName = "databasename";

        Textbox oTextbox = (Textbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oTextbox.getValue().isEmpty())
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Der Datenbankname kann nicht leer sein.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onCheckHost()
    {
        checkHost();
    }

    private boolean checkHost()
    {
        String sName = "host";

        Textbox oTextbox = (Textbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oTextbox.getValue().isEmpty())
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Der Hostname kann nicht leer sein.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onCheckPort()
    {
        checkPort();
    }

    private boolean checkPort()
    {
        String sName = "port";

        Intbox oIntbox = (Intbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oIntbox.getValue() < 0 || oIntbox.getValue() > 65535)
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel
                .setValue("Der Port muss im bereich zwischen 0 und 65535 liegen. Der empfohlene Port für MySQL ist 3306.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onCheckUsername()
    {
        checkCheckUsername();
    }

    private boolean checkCheckUsername()
    {
        String sName = "username";

        Textbox oTextbox = (Textbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oTextbox.getValue().isEmpty())
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Der Benutzername kann nicht leer sein.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onCheckPassword()
    {
        checkCheckPassword();
    }

    private boolean checkCheckPassword()
    {
        String sName = "password";

        Textbox oTextbox = (Textbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oTextbox.getValue().isEmpty())
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Bitte geben Sie ein Passwort ein.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onCreate()
    {
        //Status checken
    }

    public void onCheckConfirmPassword()
    {
        checkCheckConfirmPassword();
    }

    private boolean checkCheckConfirmPassword()
    {
        String sName = "confirmpassword";

        Textbox oTextbox = (Textbox)getFellow(sName);
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");

        if (oTextbox.getValue().isEmpty())
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Bitte geben Sie ein Passwort ein.");
            return false;
        }
        else if (!oTextbox.getValue().equals(((Textbox)getFellow("password")).getValue()))
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Die angegebenen Passwörter stimmen nicht überein.");
            return false;
        }
        else
        {
            oImage.setSrc(IMAGE_STATUS_OK);
            oLabel.setValue("");
            return true;
        }
    }

    public void onDatabaseFinish()
    {
        if (checkDatabaseName() && checkHost() && checkPort() && checkCheckUsername() && checkCheckPassword()
            && checkCheckConfirmPassword())
        {
            try
            {
                Messagebox.show("Datenbank wurde eingerichtet.", "Mitteilung", Messagebox.OK, Messagebox.INFORMATION);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                Messagebox.show("Bitte überprüfen Sie die eingegebenen Daten.", "Fehler", Messagebox.OK,
                    Messagebox.ERROR);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void onCheckConnector()
    {
        String sName = "connector";
        Image oImage = (Image)getFellow(sName + "img");
        Label oLabel = (Label)getFellow(sName + "err");
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            oImage.setSrc(IMAGE_STATUS_NOTOK);
            oLabel.setValue("Die Treiberklasse wurde nicht gefunden.");
            return;
        }
        
        try
        {
            ((Context)(new InitialContext()).lookup("java:/comp/env")).lookup("jdbc/heliosDB");
        }
        catch (NamingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
