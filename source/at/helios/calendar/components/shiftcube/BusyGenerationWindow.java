/**
 * 
 */
package at.helios.calendar.components.shiftcube;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 * Fenster, das angezeigt wird, während die Generierung der Pläne läuft
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   28.12.2009 Neuerstellung
 * </PRE>
 */
@SuppressWarnings("serial")
public class BusyGenerationWindow extends Window
{
    Label _oLabel;
    Vbox _oVbox;
    Hbox _oHbox;
    Image _oImage;
    
    public void onCreate()
    {
        _oLabel = new Label();
        _oVbox = new Vbox();
        _oHbox = new Hbox();
        _oImage = new Image("/zkau/web/zk/img/progress2.gif");
        
        this.setTitle("Verarbeitung läuft...");
        _oImage.setParent(_oHbox);
        _oLabel.setParent(_oHbox);
        _oHbox.setParent(_oVbox);
        _oVbox.setParent(this);
        
        this.setPosition("center");
        this.setWidth("400px");
    }

    public void setMessage(String sMessage)
    {
        _oLabel.setValue(sMessage);        
    }

    public void onFinished()
    {
        _oImage.setVisible(false);
        _oLabel.setValue("Generierung fertig gestellt!");
        Button oButton = new Button("Plan anzeigen");
        oButton.addForward("onClick", this, "onClick");
        oButton.setParent(this);
        this.setTitle("Verarbeitung fertig gestellt!");
        this.setPosition("center");
    }
    
    public void onClick()
    {
        SchedulerView oSchedulerView = (SchedulerView) Sessions.getCurrent().getAttribute("SchedulerView");
        oSchedulerView.onFinishedGeneration();
        this.detach();
    }
}
