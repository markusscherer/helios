//package at.helios.calendar.components;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Hbox;
//import org.zkoss.zul.Image;
//
//import at.helios.model.Person;
//import at.helios.sheduling.DriverNode;
//import at.helios.sheduling.MediNode;
//import at.helios.sheduling.ShiftNode;
//
///**
// * Box für ein Team
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   27.12.2008 Neuerstellung
// * </PRE>
// **/
//@SuppressWarnings("serial")
//public class ShiftBox extends Hbox
//{
//    Combobox                    _oDriverBox;
//    Combobox                    _oMediBox;
//    Combobox                    _oAdditionalBox;
//    Image                       _oStatusImage;
//    boolean                     _bIsInit            = false;
//
//    ShiftNode                   _oShiftNode;
//
//    int                         _iTeamNumber;
//    Image                       _oAddRemoveImage;
//
//    Hbox                        _oHbox1             = new Hbox();
//    Hbox                        _oHbox2             = new Hbox();
//    Hbox                        _oHbox3             = new Hbox();
//
//    private static final String IMAGE_STATUS_OK     = "/design/status_ok.png";
//    private static final String IMAGE_STATUS_NOTOK  = "/design/status_notok.png";
//    private static final String IMAGE_REMOVE_NORMAL = "/design/remove_normal.png";
//    private static final String IMAGE_REMOVE_HOVER  = "/design/remove_hover.png";
//    private static final String IMAGE_ADD_NORMAL    = "/design/add_normal.png";
//    private static final String IMAGE_ADD_HOVER     = "/design/add_hover.png";
//
//    /**
//     * Konstruktor mit Parametern
//     * @param oShiftNode
//     * @param iTeamNumber
//     **/
//    public ShiftBox(ShiftNode oShiftNode, int iTeamNumber)
//    {
//        this.setAlign("center");
//        _oShiftNode = oShiftNode;
//        _iTeamNumber = iTeamNumber;
//
//        _oStatusImage = new Image(IMAGE_STATUS_NOTOK);
//        _oHbox1.setAlign("center");
//        _oStatusImage.setParent(_oHbox1);
//        _oStatusImage.setHspace("2px");
//
//        _oHbox1.setParent(this);
//        _oHbox2.setParent(this);
//        _oHbox3.setParent(this);
//
//        List<DriverNode> coTemp = _oShiftNode.getDriverNodes();
//
//        if (coTemp.isEmpty())
//        {
//            return;
//        }
//
//        _oDriverBox = new Combobox();
//
////        Dieser Code könnte eine Sortierung der Personen verwirklichen, verursacht aber in onMediChange Fehler (verlässt sich auf Index)
////        Collections.sort(coTemp, new Comparator<DriverNode>()
////        {
////            public int compare(DriverNode oNode1, DriverNode oNode2)
////            {
////                return oNode1.getDriver().getEMTNumber().compareTo(oNode2.getDriver().getEMTNumber());
////            }
////        });
//
//        for (DriverNode oDriverNode : coTemp)
//        {
//            Comboitem oComboitem = new Comboitem();
//            oComboitem.setLabel(oDriverNode.getDriver().getEMTNumber());
//            oComboitem.setValue(oDriverNode);
//            oComboitem.setParent(_oDriverBox);
//        }
//
//        _oDriverBox.addForward("onChange", this, "onDriverChange");
//        _oDriverBox.setParent(_oHbox1);
//        _oDriverBox.setWidth("70px");
//
//        if (_oShiftNode.getSelectedDriverIndex(iTeamNumber) != -1)
//        {
//            _oDriverBox.setSelectedIndex(_oShiftNode.getSelectedDriverIndex(iTeamNumber));
//        }
//
//        fillMediBox();
//
//    }
//
//    /**
//     * Wird ausgeführt, wenn "+"-Icon gedrückt wird
//     **/
//    public void onAddAdditional()
//    {
//        updateStatus();
//        _oHbox3.removeChild(_oAddRemoveImage);
//
//        List<Person> coPersons = Person.getAdditionals();
//
//        _oAdditionalBox = new Combobox();
//
//        Collections.sort(coPersons, new Comparator<Person>()
//        {
//            public int compare(Person oPerson1, Person oPerson2)
//            {
//                return oPerson1.getEMTNumber().compareTo(oPerson2.getEMTNumber());
//            }
//        });
//
//        for (Person oPerson : coPersons)
//        {
//            Comboitem oComboitem = new Comboitem();
//            oComboitem.setLabel(oPerson.getEMTNumber());
//            oComboitem.setValue(oPerson);
//            oComboitem.setParent(_oAdditionalBox);
//        }
//
//        _oAdditionalBox.setWidth("70px");
//        _oAdditionalBox.setParent(_oHbox3);
//        _oAdditionalBox.addForward("onChange", this, "onAdditionalChange");
//
//        _oAddRemoveImage = new Image(IMAGE_REMOVE_NORMAL);
//        _oAddRemoveImage.setHover(IMAGE_REMOVE_HOVER);
//        _oAddRemoveImage.addForward("onClick", this, "onRemoveAdditional");
//        _oAddRemoveImage.setParent(_oHbox3);
//        _oAddRemoveImage.setHspace("2px");
//    }
//    
//
//    /**
//     * Wird ausgeführt, wenn Probehelfer-Combobox geändert wird
//     **/
//    public void onAdditionalChange()
//    {
//        _oShiftNode.setAdditional((Person) _oAdditionalBox.getSelectedItem().getValue());
//    }
//
//    /**
//     * Wird ausgeführt, wenn "-"-Icon gedrückt wird
//     **/
//    public void onRemoveAdditional()
//    {
//        _oHbox3.removeChild(_oAdditionalBox);
//        _oAdditionalBox = null;
//
//        _oHbox3.removeChild(_oAddRemoveImage);
//        _oAddRemoveImage = new Image(IMAGE_ADD_NORMAL);
//        _oAddRemoveImage.setHover(IMAGE_ADD_HOVER);
//        _oAddRemoveImage.addForward("onClick", this, "onAddAdditional");
//        _oAddRemoveImage.setParent(_oHbox3);
//        _oAddRemoveImage.setHspace("2px");
//    }
//
//    /**
//     * Wird ausgeführt, wenn Fahrer-Combobox geändert wird
//     **/
//    public void onDriverChange()
//    {
//        fillMediBox();
//        
//        
//        if(_oShiftNode.getSelectedDriverIndex(_iTeamNumber) > -1)
//        {
//            //TODO: kontrollieren
//            if(_oDriverBox.getSelectedItem() == null)
//            {
//                _oShiftNode.resetSelectedTeam(_iTeamNumber);
//            }
//            else if(((DriverNode)_oDriverBox.getSelectedItem().getValue()).getSelectedMediIndex() > -1)
//            {
//                _oShiftNode.resetSelectedTeam(_iTeamNumber);
//                _oShiftNode.setSelectedTeam(_iTeamNumber, _oDriverBox.getSelectedIndex(), _oMediBox
//                    .getSelectedIndex());
//                _oShiftNode.setIgnorable(true);
//            }
//        }
//        
//        updateStatus();
//    }
//
//    private void fillMediBox()
//    {
//        if (_oMediBox != null)
//        {
//            _oHbox2.removeChild(_oMediBox);
//        }
//
//        _oMediBox = new Combobox();
//
//        DriverNode oDriverNode = null;
//
//        try
//        {
//            oDriverNode = (DriverNode) _oDriverBox.getSelectedItem().getValue();
//        }
//        catch (NullPointerException e)
//        {
//            //TODO: evtl. besser machen, dieser Fall behandelt ShiftNodes ohne Kinder
//            return;
//        }
//
//        List<MediNode> coTemp = oDriverNode.getMediNodes();
//
////        Dieser Code könnte eine Sortierung der Personen verwirklichen, verursacht aber in onMediChange Fehler (verlässt sich auf Index)
////        Collections.sort(coTemp, new Comparator<MediNode>()
////        {
////            public int compare(MediNode oNode1, MediNode oNode2)
////            {
////                return oNode1.getMedi().getEMTNumber().compareTo(oNode2.getMedi().getEMTNumber());
////            }
////        });
//
//        for (MediNode oMediNode : coTemp)
//        {
//            Comboitem oComboitem = new Comboitem();
//            oComboitem.setLabel(oMediNode.getMedi().getEMTNumber());
//            oComboitem.setValue(oMediNode);
//            oComboitem.setParent(_oMediBox);
//        }
//
//        _oMediBox.addForward("onChange", this, "onMediChange");
//        _oMediBox.setWidth("70px");
//        _oMediBox.setParent(_oHbox2);
//
//        if (oDriverNode.getSelectedMediIndex() > -1)
//        {
//            _oMediBox.setSelectedIndex(oDriverNode.getSelectedMediIndex());
//        }
//    }
//
//    /**
//     * Wird ausgeführt, wenn Driver-Combobox geändert wird
//     **/
//    public void onMediChange()
//    {
//        _oShiftNode.resetSelectedTeam(_iTeamNumber);
//        
//        if(_oDriverBox.getSelectedIndex() > -1 && _oMediBox.getSelectedIndex() > -1)
//        {
//            _oShiftNode.setSelectedTeam(_iTeamNumber, _oDriverBox.getSelectedIndex(), _oMediBox
//                .getSelectedIndex());
//            _oShiftNode.setIgnorable(true);
//        }
//        updateStatus();
//    }
//
//    private void updateStatus()
//    {
//        DriverNode oDriverNode;
//        MediNode oMediNode;
//
//        try
//        {
//            oDriverNode = (DriverNode) _oDriverBox.getSelectedItem().getValue();
//            
//            if(_oMediBox.getSelectedItem() == null) return;
//            
//            oMediNode = (MediNode) _oMediBox.getSelectedItem().getValue();
//            
//        }
//        catch (NullPointerException e)
//        {
//            System.out.println("ShiftBox.updateStatus()");
//            e.printStackTrace();
//            //TODO: evtl. besser machen, dieser Fall behandelt ShiftNodes ohne Kinder
//            //und nicht ausgewählte medinodes
//            return;
//        }
//
//        if (oDriverNode.validate() && oMediNode.validate())
//        {
//            _oStatusImage.setSrc(IMAGE_STATUS_OK);
//
//            if (_bIsInit)
//            {
//                ViolationListbox oViolationListbox = (ViolationListbox) getFellow("ViolationListBox");
//                oViolationListbox.removeViolations(this);
//            }
//        }
//        else
//        {
//
//            ViolationListbox oViolationListbox = (ViolationListbox) getFellow("ViolationListBox");
//            oViolationListbox.removeViolations(this);
//            _bIsInit = true;
//
//            _oStatusImage.setSrc(IMAGE_STATUS_NOTOK);
//            _oStatusImage.setHspace("2px");
//
//            //TODO Strings in konstanten auslagern
//            if (!oDriverNode.checkShiftCount())
//            {
//                oViolationListbox.addViolation(this, "Fahrer (" + oDriverNode.getDriver().getEMTNumber()
//                    + ") hat die maximale Schichtanzahl (" + oDriverNode.getDriver().getMaxShiftCount()
//                    + ") überschritten\n");
//            }
//
//            if (!oDriverNode.checkShiftInterval())
//            {
//                oViolationListbox.addViolation(this, "Fahrer (" + oDriverNode.getDriver().getEMTNumber()
//                    + ") hat das minimale Schichtinterval (" + oDriverNode.getDriver().getMinShiftInterval()
//                    + ") unterschritten\n");
//            }
//
//            if (!oMediNode.checkShiftCount())
//            {
//                oViolationListbox.addViolation(this, "Sanitäter (" + oMediNode.getMedi().getEMTNumber()
//                    + ") hat die maximale Schichtanzahl (" + oMediNode.getMedi().getMaxShiftCount()
//                    + ") überschritten\n");
//            }
//
//            if (!oMediNode.checkShiftInterval())
//            {
//                oViolationListbox.addViolation(this, "Sanitäter (" + oMediNode.getMedi().getEMTNumber()
//                    + ") hat das minimale Schichtinterval (" + oMediNode.getMedi().getMinShiftInterval()
//                    + ") unterschritten\n");
//            }
//
//        }
//
//        if (_oAddRemoveImage == null)
//        {
//            _oAddRemoveImage = new Image(IMAGE_ADD_NORMAL);
//            _oAddRemoveImage.setHover(IMAGE_ADD_HOVER);
//            _oAddRemoveImage.addForward("onClick", this, "onAddAdditional");
//            _oAddRemoveImage.setHspace("2px");
//            _oAddRemoveImage.setParent(_oHbox3);
//        }
//    }
//
//    /**
//     * Aktualisiert Inhalt der ShiftBox
//     **/
//    public void update()
//    {
//        if (_oShiftNode.getSelectedDriverIndex(_iTeamNumber) != -1
//            && _oShiftNode.getSelectedDriverNode(_iTeamNumber).getSelectedMediIndex() != -1)
//        {
//            _oDriverBox.setSelectedIndex(_oShiftNode.getSelectedDriverIndex(_iTeamNumber));
//
//            fillMediBox();
//            _oMediBox
//                .setSelectedIndex(_oShiftNode.getSelectedDriverNode(_iTeamNumber).getSelectedMediIndex());
//
//            _oShiftNode.resetSelectedTeam(_iTeamNumber);
//            _oShiftNode.setSelectedTeam(_iTeamNumber, _oDriverBox.getSelectedIndex(), _oMediBox
//                .getSelectedIndex());
//        }
//
//        updateStatus();
//    }
//
//    /**
//     * Getter für _oShiftNode.
//     * @return _oShiftNode
//     **/
//    public ShiftNode getShiftNode()
//    {
//        return _oShiftNode;
//    }
//
//}
