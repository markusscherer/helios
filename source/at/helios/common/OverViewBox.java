package at.helios.common;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;

/**
 * Auswahl-Box für verschiedene Ansichten
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   08.03.2009 Neuerstellung
 * </PRE>
 **/
@SuppressWarnings("serial")
public class OverViewBox extends Hbox
{
    private static final String IMAGE_DATA_NORMAL       = "/design/data-normal.png";
    private static final String IMAGE_DATA_HOVER        = "/design/data-hover.png";
    private static final String IMAGE_SCHEDULING_NORMAL = "/design/scheduling-normal.png";
    private static final String IMAGE_SCHEDULING_HOVER  = "/design/scheduling-hover.png";
    private static final String IMAGE_STATISTICS_NORMAL = "/design/statistics-normal.png";
    private static final String IMAGE_STATISTICS_HOVER  = "/design/statistics-hover.png";
    
    //private static final String LINK_DATA = "/data";
    //private static final String LINK_SCHEDULING = "/scheduling";
    //private static final String LINK_STATISTICS = "/statistics";
    
    private Image _oDataImage;
    private Image _oSchedulingImage;
    private Image _oStatisticsImage;
    
    /**
     * Standard-Konstruktor, füllt GUI-Komponente
     **/
    public OverViewBox()
    {
        _oDataImage = new Image(IMAGE_DATA_NORMAL);
        _oDataImage.setHover(IMAGE_DATA_HOVER);
        _oDataImage.setHspace("4px");
        _oDataImage.addForward("onClick", this, "onClickData");
        
        _oSchedulingImage = new Image(IMAGE_SCHEDULING_NORMAL);
        _oSchedulingImage.setHover(IMAGE_SCHEDULING_HOVER);
        _oSchedulingImage.setHspace("4px");
        _oSchedulingImage.addForward("onClick", this, "onClickScheduling");
        
        _oStatisticsImage = new Image(IMAGE_STATISTICS_NORMAL);
        _oStatisticsImage.setHover(IMAGE_STATISTICS_HOVER);
        _oStatisticsImage.setHspace("4px");
        _oStatisticsImage.addForward("onClick", this, "onClickStatistics");
        
        _oDataImage.setParent(this);
        _oSchedulingImage.setParent(this);
        _oStatisticsImage.setParent(this);
    }
    
    /**
     * Wird ausgeführt, wenn "Daten"-Button geklickt wird
     **/
    public void onClickData()
    {
        Executions.sendRedirect(RedirectHelper.getDataLink());
    }
    
    /**
     * Wird ausgeführt, wenn "Einteilung"-Button geklickt wird
     **/
    public void onClickScheduling()
    {
        Executions.sendRedirect(RedirectHelper.getSchedulingLink());
    }
    
    /**
     * Wird ausgeführt, wenn "Statistik"-Button geklickt wird
     **/
    public void onClickStatistics()
    {
        Executions.sendRedirect(RedirectHelper.getStatisticsLink());
    }
}
