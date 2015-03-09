package at.helios.common;

/**
 * Diverse Methoden für Integer-Arrays
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   22.03.2010 Neuerstellung
 * </PRE>
 */
public class IntUtils
{
    public static int getMaximum(int[] aiArray)
    {
        int iTemp = aiArray[0];
        
        for(int i = 0; i < aiArray.length; i++)
        {
            if(aiArray[i] > iTemp)
            {
                iTemp = aiArray[i];
            }
        }
        System.out.println("IntUtils.getMaximum()  iTemp" + iTemp);
        return iTemp;
    }

}
