package at.helios.email;

import java.util.Properties;

import javax.mail.PasswordAuthentication;

/**
 * In dieser Klasse findet die Authentication mit dem Server statt
 * @author
 * <PRE>
 *         ID     date           description
 *         tom    27.03.2009     Erstkommentierung
 * </PRE>
 **/
public class Authenticator extends javax.mail.Authenticator
{
    private Properties _oProperties;

    /**
     * Konstruktor, setzt die E-Mail Properties,
     * die zur Authentication benötig werden.
     * 
     * @param oProperties
     **/
    public Authenticator(Properties oProperties)
    {
        _oProperties = oProperties;
    }

    /* (non-Javadoc)
     * @see javax.mail.Authenticator#getPasswordAuthentication()
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(_oProperties.getProperty("mail.smtp.user"), _oProperties
            .getProperty("mail.smtp.password"));
    }
}
