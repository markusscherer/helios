//package at.helios.test;
//
//import java.io.File;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//import at.helios.model.Department;
//import at.helios.model.Person;
//import at.helios.model.Training;
//import at.helios.sheduling.DriverNode;
//import at.helios.sheduling.MediNode;
//import at.helios.sheduling.ShiftNode;
//import at.helios.sheduling.ShiftTree;
//
///**
// * TODO BESCHREIBUNG_HIER_EINFUEGEN
// * @author
// * <PRE>
// *         ID    date       description
// *         markus 18.01.2009 Neuerstellung
// * </PRE>
// **/
//public class GraphvizCreator
//{
//    private static int iNum = 0;
//    private static int iColorIndex = 0;
//    private static Map<String, String> moColorMap = new HashMap<String, String>();
//    private static String[] asColors = {"antiquewhite", "aquamarine", "blue", "brown", "chartreuse", "coral", 
//                "cadetblue", "darkgoldenrod", "darkorchid", "darkseagreen", "firebrick", "palegreen"};
//
//    private static String getColor(String sKey)
//    {
//        /*if(!moColorMap.keySet().contains(sKey))
//        {
//            moColorMap.put(sKey, asColors[iColorIndex]);
//            iColorIndex++;
//        }*/
//        return "palegreen";//moColorMap.get(sKey);
//    }
//    /**
//     * TODO    KOMMENTAR_EINFUEGEN
//     * @param args    BESCHREIBUNG_EINFUEGEN
//     **/
//    public static void printGraphviz(ShiftTree oShiftTree)
//    {       
//        try
//        {
//            PrintWriter oWriter2 = new PrintWriter(new File("C:/temp/tree/list"+iNum+".html"));
//            oWriter2.println("<html><table><tr><td width=\"650px\">");
//            for(Person oPerson : Person.getPersonsByDepartment(oShiftTree.getDepartment()))
//            {
//                //for NEF
//                if(oPerson.getTrainings().containsKey(Training.getTrainingById(3)) || oPerson.getTrainings().containsKey(Training.getTrainingById(4)))
//                {
//                    oWriter2.println("<b>" + oPerson.getEMTNumber() + "  " + oPerson.getForename() + "  " + oPerson.getSurname() + "</b> max: "+ oPerson.getMaxShiftCount() + "/ min: " + oPerson.getMinShiftInterval() +"<br/>");
//                    oWriter2.println(oPerson.getTrainings()+"<br/>");
//                    for(ShiftNode oNode : oPerson.getShifts().keySet())
//                    {
//                        oWriter2.println("+++" + oNode.getShortName() + "<br/>");
//                    }
//                    oWriter2.println("<br/>");
//                }
//            }
//            oWriter2.println("</td><td>"+/*<img width=\"50%\" height=\"50%\" src=\"tree" + iNum + ".gv.png\"/>*/"</td></tr></table></html>");
//            
//            oWriter2.close();
//            
//            oWriter2 = new PrintWriter(new File("C:/temp/tree/tree"+iNum+".html"));
//            
//            oWriter2.println("<html>");
//            
//            for(ShiftNode oNode : oShiftTree.getShiftNodes())
//            {
//                oWriter2.println("<table><tr><td><h3>"+oNode.getShortName()+"</h3></td></tr><tr>");
//                for(DriverNode oDriverNode : oNode.getDriverNodes())
//                {
//                    if(oDriverNode.getDriver().getShifts().get(oNode) == Training.getTrainingById(3))
//                    {
//                        oWriter2.println("<td><b>"+oDriverNode.getDriver().getEMTNumber()+"</b><br/>");
//                    }
//                    else
//                    {
//                        oWriter2.println("<td>"+oDriverNode.getDriver().getEMTNumber()+"<br/>");
//                    }
//                    for(MediNode oMediNode : oDriverNode.getMediNodes())
//                    {
//                        if(oMediNode.getMedi().getShifts().get(oNode) == Training.getTrainingById(4) && oDriverNode.getDriver().getShifts().get(oNode) == Training.getTrainingById(3))
//                        {
//                            oWriter2.println("<b>---"+oMediNode.getMedi().getEMTNumber()+"</b><br/>");
//                        }
//                        else
//                        {
//                            oWriter2.println("---"+oMediNode.getMedi().getEMTNumber()+"<br/>");
//                        }
//                    }
//                    oWriter2.println("</td>");
//                }
//                oWriter2.println("</tr><table>");
//            }
//            oWriter2.println("/<html>");
//            oWriter2.close();
//            PrintWriter oWriter = new PrintWriter(new File("C:/temp/tree/tree"+iNum+".gv"));
//
//            oWriter.println("digraph shifttree {");
//            oWriter.println("\tl1 [label=\"ShiftTree\",shape=circle,size=\"3,6\"];");
//
//            int i = 0;
//            int j = 0;
//            int k = 0;
//            
//            for (ShiftNode oShiftNode : oShiftTree.getShiftNodes())
//            {
//                oWriter.println("\t" + "l1" + " -> " + "s" + i + ";");
//                oWriter.println("\t" + "s" + i + " [label=\"" + oShiftNode.getShortName()
//                    + "\",shape=circle,size=\"3,3\",color=lightblue2, style=filled];");
//
//                for (DriverNode oDriverNode : oShiftNode.getDriverNodes())
//                {
//                    oWriter.println("\t" + "s" + i + " -> " + "d" + j + ";");
//                    
//                    if(oDriverNode.getDriver().getShifts().get(oShiftNode) != Training.getTrainingById(3))
//                    {
//                        oWriter.println("\t" + "d" + j + " [label=\"" + oDriverNode.getDriver().getEMTNumber()
//                        + "\",shape=circle,size=\"3,3\",color="+getColor(oDriverNode.getDriver().getEMTNumber())+", style=filled];");
//                    }
//                    else
//                    {
//                        oWriter.println("\t" + "d" + j + " [label=\"" + oDriverNode.getDriver().getEMTNumber()
//                            + "\",shape=circle,size=\"3,3\",fillcolor="+getColor(oDriverNode.getDriver().getEMTNumber())+", style=\"filled,solid\", penwidth=8];");
//                    }
//                    for (MediNode oMediNode : oDriverNode.getMediNodes())
//                    {
//                        oWriter.println("\t" + "d" + j + " -> " + "m" + k + ";");
//                        
//                        if(oMediNode.getMedi().getShifts().get(oShiftNode) == Training.getTrainingById(4) && oDriverNode.getDriver().getShifts().get(oShiftNode) == Training.getTrainingById(3))
//                        {
//                            oWriter.println("\t" + "m" + k + " [label=\"" + oMediNode.getMedi().getEMTNumber()
//                                + "\",shape=circle,size=\"3,3\",fillcolor="+getColor(oMediNode.getMedi().getEMTNumber())+", style=\"filled,solid\", penwidth=8];");
//                        }
//                        else
//                        {
//                            oWriter.println("\t" + "m" + k + " [label=\"" + oMediNode.getMedi().getEMTNumber()
//                                + "\",shape=circle,size=\"3,3\",color="+getColor(oMediNode.getMedi().getEMTNumber())+", style=filled];");
//                        }
//                        k++;
//                    }
//                    j++;
//                }
//                i++;
//            }
//
//            oWriter.println("}");
//
//            oWriter.close();
//        }
//        catch (Exception e)
//        {
//
//            System.out.println("EXCEPTION!!!");
//        }
//        
//        iNum++;
//
//    }
//
//}