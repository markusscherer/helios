package at.helios.dataview;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Vbox;

import at.helios.common.PersonPanel;
import at.helios.dataview.components.PersonDataGroupbox;
import at.helios.dataview.components.RuleListGroupbox;
import at.helios.dataview.components.RulesGroupbox;
import at.helios.dataview.components.TrainingsGroupbox;
import at.helios.model.Person;
import at.helios.model.dao.PersonDAO;

/**
 * 
 * Diese Klasse stellt einen RegelTab dar.
 * 
 * @author
 * <PRE>
 *         ID    date         description
 *         mab   28.03.2009   Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class RuleTab extends Tabpanel implements PersonDataRules
{
    private TrainingsGroupbox  _oTrainigsGB;
    private RulesGroupbox      _oRulesGB;
    private PersonDataGroupbox _oPersonDataGB;
    private RuleListGroupbox   _oRuleListGB;
    private PersonPanel  _oPersonPanel;
    private Person       _oPerson;
    private Label                _oInfoLabel = new Label();

   /**
    * 
    * Setzt das PersonPanel und inizialisiert die GUI
    * 
    * @param oPersonPanel
    *
    */
    public RuleTab(PersonPanel oPersonPanel)
    {
        _oPersonPanel = oPersonPanel;
        initGui();
    }

    /**
     * Initialisiert die Gui
     *  - PersonPanel
     *  - PersonDatagroupbox
     *  - TrainingsGroupbox
     *  - RuleListBox
     *  - RulesGroupbox
     */
    public void initGui()
    {
        Vbox oVbox = new Vbox();
        oVbox.setStyle("min-width: 800px");
        _oPersonDataGB = new PersonDataGroupbox();
        _oPersonDataGB.setParent(oVbox);

        _oTrainigsGB = new TrainingsGroupbox();
        _oTrainigsGB.setStyle("padding-top: 10px");
        _oTrainigsGB.setParent(oVbox);

        RuleboxHelper oRuleboxHelper = new RuleboxHelper(_oInfoLabel);

        _oRuleListGB = new RuleListGroupbox(oRuleboxHelper);
        _oRuleListGB.setStyle("padding-top: 10px");

        _oRulesGB = new RulesGroupbox(oRuleboxHelper, _oPersonPanel, oRuleboxHelper);
        _oRulesGB.setStyle("padding-top: 10px");
        _oRulesGB.setParent(oVbox);

        _oRuleListGB.setParent(oVbox);

        Hbox oHbox = new Hbox();
        oHbox.setWidth("800px");
        _oInfoLabel.setValue("");
        _oInfoLabel.setParent(oHbox);

        Div oDiv = new Div();
        oDiv.setStyle("text-align: right");

        //Speichern Button

        Button oBSave = new Button("Speichern");
        oBSave.addForward("onClick", this, "onSave");
        oBSave.setParent(oDiv);
        oDiv.setParent(oHbox);
        oHbox.setParent(oVbox);

        oVbox.setParent(this);
    }

    /**
     * 
     * Wird aufgerufen, wenn auf den 
     * Speichern Button geklickt wird
     *
     */
    public void onSave()
    {
        save();
    }

   
    /**
     * Speichert die alle Daten der Person.
     */
    public void save()
    {
        _oPersonDataGB.save();
        _oTrainigsGB.save();
        _oRulesGB.save();

        PersonDAO oPersonDAO = new PersonDAO();
        oPersonDAO.update(_oPerson);

        //Neue Daten übernehmen
        _oRuleListGB.rebuildList();
        _oInfoLabel.setValue("Daten gespeichert");
    }

    /**
     * Setzt die Werte der Person in der DataView - Rules
     */
    public void setValues(Person oPerson)
    {
        _oTrainigsGB.setValues(oPerson);
        _oRulesGB.setValues(oPerson);
        _oPersonDataGB.setValues(oPerson);
        _oRuleListGB.setValues(oPerson);
        _oPerson = oPerson;
    }
}
