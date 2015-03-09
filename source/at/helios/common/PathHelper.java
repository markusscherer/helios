package at.helios.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathHelper
{
    private static String _sPath = null;

    private PathHelper()
    {
    }

    /**
     * 
     * Returns the Root Path of the Project with slash at the end
     * 
     * for instance: /opt/apache-tomcat/webapps/helios/
     * 
     * 
     * @return    Project Root Path
     *
     */
    public static String getRootPath()
    {
        if (_sPath == null)
        {
            String sub_string = "file:";

            _sPath = "" + PathHelper.class.getResource("/at/helios/common/PathHelper.class");

            _sPath = _sPath.substring(sub_string.length());
            _sPath = _sPath.split("WEB-INF")[0];
            
            //Wird benötigt um z.B. Sonderzeichen wie Abstände (%20)
            //richtig anzuzeigen
            try
            {
                _sPath = URLDecoder.decode(_sPath,"UTF8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }

        return _sPath;
    }
}