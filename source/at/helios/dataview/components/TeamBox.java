package at.helios.dataview.components;

import org.zkoss.zul.Hbox;

import at.helios.common.PersonBox;
import at.helios.common.PersonPanel;
import at.helios.common.SmallPersonBox;
import at.helios.model.Team;

/**
 * Box zur Auswahl von Teams
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   26.02.2009 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class TeamBox extends Hbox
{
    PersonBox _oDriverBox = null;
    PersonBox _oMediBox = null;
    PersonBox _oAdditionalBox = null;
    Team _oTeam;
    
    
    /**
     * Konstruktor mit Feldern
     * @param oTeam
     * @param oPersonPanel
     **/
    public TeamBox(Team oTeam, PersonPanel oPersonPanel)
    {
        _oTeam = oTeam;
        
        _oDriverBox = new SmallPersonBox();
        _oDriverBox.setSelectedByValue(oTeam.getDriver());
        _oDriverBox.setPersonPanel(oPersonPanel);
        _oDriverBox.addForward("onChange", this, "onDriverChange");
        _oDriverBox.setParent(this);

        _oMediBox = new SmallPersonBox();
        _oMediBox.setSelectedByValue(oTeam.getMedi());
        _oMediBox.setPersonPanel(oPersonPanel);
        _oMediBox.addForward("onChange", this, "onMediChange");
        _oMediBox.setParent(this);
        

        _oAdditionalBox = new SmallPersonBox();
        _oAdditionalBox.setPersonPanel(oPersonPanel);
        _oAdditionalBox.addForward("onChange", this, "onAdditionalChange");
        _oAdditionalBox.setParent(this);

        if (oTeam.getAdditional() != null)
        {
            _oAdditionalBox.setSelectedByValue(oTeam.getAdditional());
        }
    }
    
    /**
     * wird ausgeführt, wenn Fahrer geändert wird
     **/
    public void onDriverChange()
    {
        _oTeam.setDriver(_oDriverBox.getSelectedPerson());
    }
    
    /**
     * wird ausgeführt, wenn Sanitäter geändert wird
     **/
    public void onMediChange()
    {
        _oTeam.setMedi(_oMediBox.getSelectedPerson());
    }
    
    /**
     * wird ausgeführt, wenn Probehelfer geändert wird
     **/
    public void onAdditionalChange()
    {
        _oTeam.setAdditional(_oAdditionalBox.getSelectedPerson());
    }
}
