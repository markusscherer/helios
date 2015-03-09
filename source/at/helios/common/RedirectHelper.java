package at.helios.common;

import java.io.IOException;
import java.util.Properties;

public class RedirectHelper
{
    private static Properties   oProperties;
    private static final String PROPERTY_PREFIX     = "at.helios.redirect.prefix";
    private static final String PROPERTY_OVERVIEW   = "at.helios.redirect.overview";
    private static final String PROPERTY_DATA       = "at.helios.redirect.data";
    private static final String PROPERTY_STATISTICS = "at.helios.redirect.statistics";
    private static final String PROPERTY_SCHEDULING = "at.helios.redirect.scheduling";
    private static final String PROPERTY_LOGIN      = "at.helios.redirect.login";

    static
    {
        oProperties = new Properties();
        try
        {
            oProperties
                .load(RedirectHelper.class.getClassLoader().getResourceAsStream("resources/redirect.properties"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String getOverviewLink()
    {
        return oProperties.getProperty(PROPERTY_PREFIX, "") + oProperties.getProperty(PROPERTY_OVERVIEW);
    }

    public static String getDataLink()
    {
        return oProperties.getProperty(PROPERTY_PREFIX, "") + oProperties.getProperty(PROPERTY_DATA);
    }

    public static String getStatisticsLink()
    {
        return oProperties.getProperty(PROPERTY_PREFIX, "") + oProperties.getProperty(PROPERTY_STATISTICS);
    }

    public static String getSchedulingLink()
    {
        return oProperties.getProperty(PROPERTY_PREFIX, "") + oProperties.getProperty(PROPERTY_SCHEDULING);
    }
    
    public static String getLoginLink()
    {
        return oProperties.getProperty(PROPERTY_PREFIX, "") + oProperties.getProperty(PROPERTY_LOGIN);
    }
}
