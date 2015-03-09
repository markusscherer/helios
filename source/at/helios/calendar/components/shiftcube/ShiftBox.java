package at.helios.calendar.components.shiftcube;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;

import at.helios.model.Person;
import at.helios.model.Team;
import at.helios.sheduling.ShiftCube;
import at.helios.sheduling.shiftcube.DriverIdentifier;
import at.helios.sheduling.shiftcube.MediIdentifier;
import at.helios.sheduling.shiftcube.ShiftIdentifier;

/**
 * Box für ein Team
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   27.12.2008 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class ShiftBox extends Hbox
{
    Combobox                    _oDriverBox;
    Combobox                    _oMediBox;
    Combobox                    _oAdditionalBox;
    Image                       _oStatusImage;
    boolean                     _bIsInit            = false;
    
    Team                        _oTeam;

    ShiftIdentifier             _oShiftIdentifier;
    ShiftCube                   _oShiftCube;

    int                         _iTeamNumber;
    Image                       _oAddRemoveImage;

    Hbox                        _oHbox1             = new Hbox();
    Hbox                        _oHbox2             = new Hbox();
    Hbox                        _oHbox3             = new Hbox();

    private static final String IMAGE_STATUS_OK          = "/design/status_ok.png";
    private static final String IMAGE_STATUS_NOTOK       = "/design/status_notok.png";
    private static final String IMAGE_REMOVE_NORMAL      = "/design/remove_normal.png";
    private static final String IMAGE_REMOVE_HOVER       = "/design/remove_hover.png";
    private static final String IMAGE_ADD_NORMAL         = "/design/add_normal.png";
    private static final String IMAGE_ADD_HOVER          = "/design/add_hover.png";
    private static final String IMAGE_VIOLATION_COUNT    = "/design/violation_count.png";
    private static final String IMAGE_VIOLATION_INTERVAL = "/design/violation_interval.png";
    private static final String IMAGE_VIOLATION_BOTH     = "/design/violation_both.png";
    
    private final Comparator<DriverIdentifier> oDriverComparator = new Comparator<DriverIdentifier>()
    {      
        @Override
        public int compare(DriverIdentifier oDriverIdentifier1, DriverIdentifier oDriverIdentifier2)
        {
            int iValue1 = 0;
            int iValue2 = 0;
            
            boolean bIntervalViolation1 = !oDriverIdentifier1.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation1 = !oDriverIdentifier1.checkShiftCount(_oShiftIdentifier);
              
            boolean bIntervalViolation2 = !oDriverIdentifier2.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation2 = !oDriverIdentifier2.checkShiftCount(_oShiftIdentifier);

            if(!bIntervalViolation1 && !bCountViolation1)
            {
                iValue1 = 0;
            }
            else if(bIntervalViolation1 && bCountViolation1)
            {
                iValue1 = 3;
            }
            else if(bIntervalViolation1)
            {
                iValue1 = 2;
            }
            else if(bCountViolation1)
            {
                iValue1 = 1;
            }
            
            if(!bIntervalViolation2 && !bCountViolation2)
            {
                iValue2 = 0;
            }
            else if(bIntervalViolation2 && bCountViolation2)
            {
                iValue2 = 3;
            }
            else if(bIntervalViolation2)
            {
                iValue2 = 2;
            }
            else if(bCountViolation2)
            {
                iValue2 = 1;
            }
            
            if(iValue1 == iValue2)
            {
                return oDriverIdentifier1.getDriver().getEMTNumber().compareTo(oDriverIdentifier2.getDriver().getEMTNumber());
            }
            return iValue1-iValue2;
        }
    };

    private final Comparator<MediIdentifier> oMediComparator = new Comparator<MediIdentifier>()
    {      
        @Override
        public int compare(MediIdentifier oMediIdentifier1, MediIdentifier oMediIdentifier2)
        {
            int iValue1 = 0;
            int iValue2 = 0;
            
            boolean bIntervalViolation1 = !oMediIdentifier1.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation1 = !oMediIdentifier1.checkShiftCount(_oShiftIdentifier);
              
            boolean bIntervalViolation2 = !oMediIdentifier2.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation2 = !oMediIdentifier2.checkShiftCount(_oShiftIdentifier);

            if(!bIntervalViolation1 && !bCountViolation1)
            {
                iValue1 = 0;
            }
            else if(bIntervalViolation1 && bCountViolation1)
            {
                iValue1 = 3;
            }
            else if(bIntervalViolation1)
            {
                iValue1 = 2;
            }
            else if(bCountViolation1)
            {
                iValue1 = 1;
            }
            
            if(!bIntervalViolation2 && !bCountViolation2)
            {
                iValue2 = 0;
            }
            else if(bIntervalViolation2 && bCountViolation2)
            {
                iValue2 = 3;
            }
            else if(bIntervalViolation2)
            {
                iValue2 = 2;
            }
            else if(bCountViolation2)
            {
                iValue2 = 1;
            }
            
            if(iValue1 == iValue2)
            {
                return oMediIdentifier1.getMedi().getEMTNumber().compareTo(oMediIdentifier2.getMedi().getEMTNumber());
            }
            return iValue1-iValue2;
        }
    };

 
    /**
     * Konstruktor mit Parametern
     * @param oShiftIdentifier
     * @param oTeam
     **/
    public ShiftBox(ShiftIdentifier oShiftIdentifier, Team oTeam, ShiftCube oShiftCube, int iTeamNumber)
    {
        _oShiftIdentifier = oShiftIdentifier;
        _oTeam = oTeam;
        _oShiftCube = oShiftCube;
        _iTeamNumber = iTeamNumber;
        
        this.setAlign("center");

        //FIXME das ist nur zu motivation
        _oStatusImage = new Image(IMAGE_STATUS_OK);
        _oHbox1.setAlign("center");
        _oStatusImage.setParent(_oHbox1);
        _oStatusImage.setHspace("2px");

        _oHbox1.setParent(this);
        _oHbox2.setParent(this);
        _oHbox3.setParent(this);
        
        generateNewDriverBox();
        generateNewMediBox();
        
        fillDriverBox();
        fillMediBox();
    }
    
    private void generateNewDriverBox()
    {
        if(_oDriverBox != null) _oHbox1.removeChild(_oDriverBox);
        _oDriverBox = new Combobox();
        _oDriverBox.addForward("onChange", this, "onDriverChange");
        _oDriverBox.setParent(_oHbox1);
        _oDriverBox.setWidth("70px");
    }
    
    private void generateNewMediBox()
    {
        if(_oMediBox != null) _oHbox2.removeChild(_oMediBox);
        _oMediBox = new Combobox();
        _oMediBox.addForward("onChange", this, "onMediChange");
        _oMediBox.setWidth("70px");
        _oMediBox.setParent(_oHbox2);
    }
    
    private void fillDriverBox()
    {
        generateNewDriverBox();
        
        Person oDriver = _oTeam.getDriver();
        Person oMedi = _oTeam.getMedi();
        
        MediIdentifier oMediIdentifier = null;
        
        if(oMedi != null) oMediIdentifier = _oShiftCube.getMediIdentifierByPerson(oMedi);

        List<DriverIdentifier> coDriverIdentifiers;
        
        if(oMediIdentifier != null)
        {
            coDriverIdentifiers = _oShiftCube.getDriverIdentifiers(_oShiftIdentifier, oMediIdentifier);
        }
        else
        {
            coDriverIdentifiers = _oShiftCube.getDriverIdentifiers(_oShiftIdentifier);
            System.out.println("MediIdentifier null");
        }
        Collections.sort(coDriverIdentifiers, oDriverComparator);
        for(DriverIdentifier oDriverIdentifier : coDriverIdentifiers)
        {
            Comboitem oComboitem = new Comboitem();
            oComboitem.setValue(oDriverIdentifier);
            oComboitem.setLabel(oDriverIdentifier.getDriver().getEMTNumber());
            oComboitem.setParent(_oDriverBox);
            
            boolean bIntervalViolation = !oDriverIdentifier.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation = !oDriverIdentifier.checkShiftCount(_oShiftIdentifier);
            
            if(bIntervalViolation && bCountViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_BOTH);
            }
            else if(bIntervalViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_INTERVAL);
            }
            else if(bCountViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_COUNT);
            }
            
            if(oDriverIdentifier.getDriver() == oDriver)
            {
                _oDriverBox.setSelectedItem(oComboitem);
            }
        }
    }
    
    private void fillMediBox()
    {
        generateNewMediBox();
        
        Person oDriver = _oTeam.getDriver();
        Person oMedi = _oTeam.getMedi();
        
        DriverIdentifier oDriverIdentifier = null;
        
        if(oDriver != null) oDriverIdentifier = _oShiftCube.getDriverIdentifierByPerson(oDriver);

        List<MediIdentifier> coMediIdentifiers;
        
        if(oDriverIdentifier != null)
        {         
            coMediIdentifiers = _oShiftCube.getMediIdentifiers(_oShiftIdentifier, oDriverIdentifier);
        }
        else
        {
            System.out.println("DriverIdentifier null");
            coMediIdentifiers = _oShiftCube.getMediIdentifiers(_oShiftIdentifier);
        }    
            
        Collections.sort(coMediIdentifiers, oMediComparator);
            
        for(MediIdentifier oMediIdentifier : coMediIdentifiers)
        {
            Comboitem oComboitem = new Comboitem();
            oComboitem.setValue(oMediIdentifier);
            oComboitem.setLabel(oMediIdentifier.getMedi().getEMTNumber());
            oComboitem.setParent(_oMediBox);
            
            boolean bIntervalViolation = !oMediIdentifier.checkShiftInterval(_oShiftIdentifier);
            boolean bCountViolation = !oMediIdentifier.checkShiftCount(_oShiftIdentifier);
            
            if(bIntervalViolation && bCountViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_BOTH);
            }
            else if(bIntervalViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_INTERVAL);
            }
            else if(bCountViolation)
            {
                oComboitem.setImage(IMAGE_VIOLATION_COUNT);
            }
            if(oMediIdentifier.getMedi() == oMedi)
            {
                _oMediBox.setSelectedItem(oComboitem);
            }
        }
    }

    /**
     * Wird ausgeführt, wenn "+"-Icon gedrückt wird
     **/
    public void onAddAdditional()
    {
        _oHbox3.removeChild(_oAddRemoveImage);

        List<Person> coPersons = Person.getAdditionals();

        _oAdditionalBox = new Combobox();

        Collections.sort(coPersons, new Comparator<Person>()
        {
            public int compare(Person oPerson1, Person oPerson2)
            {
                return oPerson1.getEMTNumber().compareTo(oPerson2.getEMTNumber());
            }
        });

        for (Person oPerson : coPersons)
        {
            Comboitem oComboitem = new Comboitem();
            oComboitem.setLabel(oPerson.getEMTNumber());
            oComboitem.setValue(oPerson);
            oComboitem.setParent(_oAdditionalBox);
        }

        _oAdditionalBox.setWidth("70px");
        _oAdditionalBox.setParent(_oHbox3);
        _oAdditionalBox.addForward("onChange", this, "onAdditionalChange");

        _oAddRemoveImage = new Image(IMAGE_REMOVE_NORMAL);
        _oAddRemoveImage.setHover(IMAGE_REMOVE_HOVER);
        _oAddRemoveImage.addForward("onClick", this, "onRemoveAdditional");
        _oAddRemoveImage.setParent(_oHbox3);
        _oAddRemoveImage.setHspace("2px");
    }
    

    /**
     * Wird ausgeführt, wenn Probehelfer-Combobox geändert wird
     **/
    public void onAdditionalChange()
    {
        _oTeam.setAdditional((Person) _oAdditionalBox.getSelectedItem().getValue());
    }

    /**
     * Wird ausgeführt, wenn "-"-Icon gedrückt wird
     **/
    public void onRemoveAdditional()
    {
        _oHbox3.removeChild(_oAdditionalBox);
        _oAdditionalBox = null;
        _oTeam.setAdditional(null);

        _oHbox3.removeChild(_oAddRemoveImage);
        _oAddRemoveImage = new Image(IMAGE_ADD_NORMAL);
        _oAddRemoveImage.setHover(IMAGE_ADD_HOVER);
        _oAddRemoveImage.addForward("onClick", this, "onAddAdditional");
        _oAddRemoveImage.setParent(_oHbox3);
        _oAddRemoveImage.setHspace("2px");
    }

    /**
     * Wird ausgeführt, wenn Fahrer-Combobox geändert wird
     **/
    public void onDriverChange()
    {
        Person oPerson = null;
        
        if(_oDriverBox.getSelectedItem() != null)
        {
            oPerson = ((DriverIdentifier)_oDriverBox.getSelectedItem().getValue()).getDriver();
        }
        _oTeam.setDriver(oPerson);
        
        //FIXME Shift aus Person löschen
        if(oPerson == null)
        {
            _oShiftIdentifier.resetSelectedTeam(_iTeamNumber);
        }
        else if(!_oShiftIdentifier.isScheduled(_iTeamNumber) && _oMediBox.getSelectedItem() != null)
        {
            _oShiftIdentifier.setSelectedTeam(_iTeamNumber);
        }
        fillMediBox();
        
        System.out.println("uiuiuiui driver: " + oPerson);
    }

    /**
     * Wird ausgeführt, wenn Driver-Combobox geändert wird
     **/
    public void onMediChange()
    {
        Person oPerson = null;
        
        if(_oMediBox.getSelectedItem() != null)
        {
            oPerson = ((MediIdentifier)_oMediBox.getSelectedItem().getValue()).getMedi();
        }
        _oTeam.setMedi(oPerson);
        
        System.out.println("uiuiuiui medi " + oPerson);
        
        //FIXME Shift aus Person löschen
        if(oPerson == null)
        {
            _oShiftIdentifier.resetSelectedTeam(_iTeamNumber);
        }
        else if(!_oShiftIdentifier.isScheduled(_iTeamNumber) && _oDriverBox.getSelectedItem() != null)
        {
            _oShiftIdentifier.setSelectedTeam(_iTeamNumber);
        }
        fillDriverBox();
    }

    /**
     * Getter für _oShiftIdentifier.
     * @return _oShiftIdentifier
     **/
    public ShiftIdentifier getShiftIdentifier()
    {
        return _oShiftIdentifier;
    }

    public void update()
    {
//        if(_oTeam.getDriver() != null)
//        {
            fillDriverBox();
//        }
//        else System.err.println("Driver Null");
//        if(_oTeam.getMedi() != null)
//        {
            fillMediBox();
//        }
//        else System.err.println("Medi Null");
    }

}
