package at.helios.dataview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.zkoss.zul.Label;

import at.helios.dataview.components.RuleListGroupbox;
import at.helios.model.Person;
import at.helios.sheduling.DateRule;
import at.helios.sheduling.DayRule;
import at.helios.sheduling.FriendRule;
import at.helios.sheduling.Rule;
import at.helios.sheduling.Shifts;

/**
 * 
 * Stellt wichige Methoden zur Verwaltung
 * (prüfen einfügen, löschen, aufrufen und speichern)
 * von Regeln in der DataViewRules zur Verfügung.
 * 
 * @author
 * <PRE>
 *         ID      date          description
 *         mab     28.03.2009    Erstkommentierung
 * </PRE>
 *
 */
public class RuleboxHelper
{
    private RuleListGroupbox       _oRuleBox;

    private Collection<Rule> _coRules        = new ArrayList<Rule>();
    private Collection<Rule> _coDeletedRules = new ArrayList<Rule>();
    private Label _oInfoLabel;

    /**
     * 
     * Konstruktor - Setzt das InfoLabel
     * über das Informationen an den
     * User weitergegeben werden kann.
     * 
     * @param oInfoLabel
     *
     */
    public RuleboxHelper(Label oInfoLabel)
    {
        _oInfoLabel = oInfoLabel;
    }
    
    /**
     * 
     * Gibt alle Regeln (Regeln die bereits in 
     * der Datenbank gespeichert sind und neue Regeln)
     * der jeweiligen Person zurück
     * 
     * @return _coRules - Regeln der Person
     *
     */
    public Collection<Rule> getRulesOfPerson()
    {
        return _coRules;
    }
    
    /**
     * Gibt die gelöschten Rules zurück
     * @return _coDeletedRules
     */
    public Collection<Rule> getDeletedRules()
    {
        return _coDeletedRules;
    }

    /**
     * Nimmt eine neue Rule auf
     * 
     * @param oRule 
     *
     */
    public void addRule(Rule oRule)
    {
        _coRules.add(oRule);
        _oRuleBox.addRuleToListBox(oRule);
        _oInfoLabel.setValue("Eintrag erfolgreich hinzugefügt.");
    }
    
    /**
     * 
     * Nimmte eine gelöschte Regel wieder
     * in die _coRules auf.
     * 
     * @param oRule - wiederherzustellende Regel
     *
     */
    public void restoreRule(Rule oRule)
    {
        addRule(oRule);
        _coDeletedRules.remove(oRule);
    }

    /**
     * 
     * Wird beim initialisieren der Rules einer 
     * neuen Person aufgerufen
     * 
     * @param oRule
     *
     */
    public void fillList(Rule oRule)
    {
        _coRules.add(oRule);
    }

    /**
     * 
     * Löschte die übergebene Regel,
     * - ist oRule eine Regel die bereits in der Datenbank
     *   vorhanden ist, wird sie in eine Collection mit
     *   gelöschten Regelen aufgenommen
     * - anderenfalls wird sie sofort gelöscht.
     * 
     * @param oRule - zu löschende Regel
     *
     */
    public void removeRule(Rule oRule)
    {
        if (oRule.getRuleId() != 0)
            _coDeletedRules.add(oRule);

        _coRules.remove(oRule);
        _oInfoLabel.setValue("Eintrag gelöscht.");
    }

    /** 
     * Löscht alle Regeln der Person
     * (Regeln + gelöschte Regeln) aus dem Speicher
     */
    public void flushRuleList()
    {
        _coRules.clear();
        _coDeletedRules.clear();
    }

    /**
     * 
     * Prüft ob die Regel bereits vorhanden ist
     * 
     * @param oFriend
     * @param oOwner
     * @return true --> Eintrag nicht vorhanden
     *         false --> Eintrag vorhanden - Regel wird nicht hinzugefügt
     *
     */
    public boolean checkRule(Person oFriend, Person oOwner)
    {
        Collection<Rule> coRules;
        for (int i = 0; i <= 1; i++)
        {
            coRules = setRulePool(i);

            for (Rule oRule : coRules)
            {
                if (oRule instanceof FriendRule)
                {
                    if (oRule.getRuleOwner().equals(oOwner)
                        && ((FriendRule) oRule).getFriend().equals(oFriend) || oFriend.equals(oOwner))
                    {
                        if (i == 1)
                            this.restoreRule(oRule);
                        else
                            _oInfoLabel.setValue("Eintrag bereits vorhanden.");
                        
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 
     * Prüft ob die Regel bereits vorhanden ist
     * 
     * @param oDate
     * @param oShift
     * @param oOwner
     * @return true --> Eintrag nicht vorhanden
     *         false --> Eintrag vorhanden - Regel wird nicht hinzugefügt
     *         
     */
    public boolean checkRule(GregorianCalendar oDate, Shifts oShift, Person oOwner)
    {
        Collection<Rule> coRules;
        for (int i = 0; i <= 1; i++)
        {
            coRules = setRulePool(i);

            for (Rule oRule : coRules)
            {
                if (oRule instanceof DateRule)
                {
                    if (oRule.getRuleOwner().equals(oOwner) && ((DateRule) oRule).getDate().equals(oDate)
                        && ((DateRule) oRule).getShift().equals(oShift))
                    {
                        if (i == 1)
                            this.restoreRule(oRule);
                        else
                            _oInfoLabel.setValue("Eintrag bereits vorhanden.");
                        
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 
     * Prüft ob die Regel bereits vorhanden ist
     * 
     * @param oDate
     * @param oShift
     * @param oOwner
     * @return true --> Eintrag nicht vorhanden
     *         false --> Eintrag vorhanden - Regel wird nicht hinzugefügt
     *         
     */
    public boolean checkRule(int iDay, Shifts oShift, Person oOwner)
    {
        Collection<Rule> coRules;
        for (int i = 0; i <= 1; i++)
        {
            coRules = setRulePool(i);

            for (Rule oRule : coRules)
            {
                if (oRule instanceof DayRule)
                {
                    if (oRule.getRuleOwner().equals(oOwner) && ((DayRule) oRule).getDayId() == iDay
                        && ((DayRule) oRule).getShift().equals(oShift))
                    {
                        if (i == 1)
                            this.restoreRule(oRule);
                        else
                            _oInfoLabel.setValue("Eintrag bereits vorhanden.");
                        
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 
     * iCheckCode = 0 --> alle definierten Regeln der Person werden zurückgegeben
     * iCheckCode != 0 --> alle gespeicherten, gelöschten Regeln werden zurückgegeben.
     * 
     * @param iCheckCode
     * @return Collection<Rule> Collection mit Regeln 
     *
     */
    private Collection<Rule> setRulePool(int iCheckCode)
    {
        if (iCheckCode == 0)
            return _coRules;
        else
            return _coDeletedRules;
    }

    /**
     * 
     * Setzt die RuleListBox
     * 
     * @param oRuleBox
     *
     */
    public void setRuleListBox(RuleListGroupbox oRuleBox)
    {
        _oRuleBox = oRuleBox;
    }
    
    /**
     * 
     * Schreibt in sInfo enthaltene Informationen
     * auf die Dataview
     * 
     * @param sInfo - zu schreibende Informationen
     *
     */
    public void setInfo(String sInfo)
    {
        _oInfoLabel.setValue(sInfo);
    }
}
