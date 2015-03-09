package at.helios.dataview.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Spinner;

import at.helios.dataview.PersonDataRules;
import at.helios.model.Person;
import at.helios.model.Training;

/**
 * 
 * Diese Klasse stelle eine Oberläche bereit,
 * mit der es möglich ist alle Qualifikationen
 * einer Person anzuzeigen und zu ändern. Zusätzlich
 * ist es möglich die Anzahl der gewünschten Dienste
 * pro Qualifikation festzulegen.
 * 
 * @author
 * <PRE>
 *         ID    date        description
 *         mab   28.03.2009  Erstkommentierung
 * </PRE>
 *
 */
@SuppressWarnings("serial")
public class TrainingsGroupbox extends Groupbox implements PersonDataRules
{
    private HashMap<Training, Integer> _moTrainings;
    private Map<String, Checkbox>      _coCheckbox = new HashMap<String, Checkbox>();
    private Map<String, Spinner>       _coSpinner  = new HashMap<String, Spinner>();
    private Person                     _oPerson;

    /**
     * 
     * Ruft die Methode auf um die GUI zu initialisieren.
     *
     */
    public TrainingsGroupbox()
    {
        initGui();
    }

    /**
     * Initialisiert die GUI
     */
    public void initGui()
    {
        new Caption("Ausbildung").setParent(this);
        this.setMold("3d");

        Listbox oListBox = new Listbox();
        oListBox.setStyle("width: 500px; margin: 0px");

        Listitem oListitem;
        Listcell oListcell;

        //TrainingDAO oTrainingDAO = new TrainingDAO();
        Collection<Training> coTrainings = Training.getTrainings();

        for (Training oTraining : coTrainings)
        {
            if (oTraining.getCode() != null && !oTraining.getCode().equalsIgnoreCase(""))
            {
                oListitem = new Listitem();

                oListcell = new Listcell();
                Checkbox oCheckbox = new Checkbox();
                oCheckbox.setLabel(oTraining.getName());
                //oCheckbox.setId(oTraining.getCode());
                oCheckbox.setAttribute("TrainId", oTraining.getTrainingId());
                oCheckbox.setAttribute("code", oTraining.getCode());
                oCheckbox.setParent(oListcell);
                oCheckbox.addForward("onCheck", this, "onChecking");

                _coCheckbox.put(oTraining.getCode(), oCheckbox);
                oListcell.setParent(oListitem);

                oListcell = new Listcell();
                Spinner oSpinner = new Spinner();
                //oSpinner.setId("s_" + oTraining.getCode());
                oSpinner.setParent(oListcell);
                oSpinner.setVisible(false);
                _coSpinner.put("s_" + oTraining.getCode(), oSpinner);
                oListcell.setParent(oListitem);

                oListitem.setParent(oListBox);
            }
            else
            {
                System.out.println("Error: Training --> null");
                oListitem = new Listitem();

                oListcell = new Listcell();
                oListcell.appendChild(new Label("Error: Training = null"));
                oListcell.setParent(oListitem);

                oListcell = new Listcell();
                oListcell.setParent(oListitem);

                oListitem.setParent(oListBox);
            }
        }
        oListBox.setParent(this);
    }

    /**
     * Setzt die Werte der übergebenen Person
     * 
     * @param oPerson  - ausgewählte Person
     *
     */
    public void setValues(Person oPerson)
    {
        unCheckAll();
        _oPerson = oPerson;

        _moTrainings = _oPerson.getTrainings();

        for (Training oTraining : _moTrainings.keySet())
        {
            _coCheckbox.get(oTraining.getCode()).setChecked(true);
            //((Checkbox) this.getFellowIfAny(oTraining.getCode())).setChecked(true);
            _coSpinner.get("s_" + oTraining.getCode()).setValue(_moTrainings.get(oTraining));
            _coSpinner.get("s_" + oTraining.getCode()).setVisible(true);
            //((Spinner) this.getFellowIfAny("s_" + oTraining.getCode())).setValue(_moTrainings.get(oTraining));
        }
    }

    /**
     *
     * Wählt alle Checkboxen ab
     *
     */
    private void unCheckAll()
    {
        for (Checkbox oCheckbox : _coCheckbox.values())
        {
            oCheckbox.setChecked(false);
        }
        for (Spinner oSpinner : _coSpinner.values())
        {
            oSpinner.setValue(-1);
            oSpinner.setVisible(false);
        }
    }

    /**
     * 
     * Wird aufgerufen, wenn eine Qualifikation
     * hinzugefügt oder weggenommen wird. Dabei
     * wird der dazugehörige Spinner ein oder ausgeblendet.
     *
     */
    public void onChecking()
    {
        for (Checkbox oCheckbox : _coCheckbox.values())
        {
            if (oCheckbox.isChecked())
            {
                (_coSpinner.get("s_" + oCheckbox.getAttribute("code"))).setVisible(true);
            }
            else
            {
                (_coSpinner.get("s_" + oCheckbox.getAttribute("code"))).setVisible(false);
                (_coSpinner.get("s_" + oCheckbox.getAttribute("code"))).setValue(-1);
            }
        }
    }

    /**
     * Speichert alle gesetzten Qualifikationen.
     */
    public void save()
    {
        Training oTmpTraining;
        int iTrainingCount;

        for (Checkbox oCheckbox : _coCheckbox.values())
        {
            oTmpTraining = Training.getTrainingById((Integer) oCheckbox.getAttribute("TrainId"));

            if (oCheckbox.isChecked())
            {
                iTrainingCount = (_coSpinner.get("s_" + oCheckbox.getAttribute("code"))).getValue();

                //Training hinzufügen oder updaten
                _oPerson.addTraining(oTmpTraining, iTrainingCount);
            }
            else
            {
                //not selected Trainings werden, falls vorhanden gelöscht
                if (_oPerson.getTrainings().containsKey(oTmpTraining))
                {
                    _oPerson.removeTraining(oTmpTraining);
                }
            }
        }
    }
}
