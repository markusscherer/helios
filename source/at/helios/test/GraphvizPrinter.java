///**
// * 
// */
//package at.helios.test;
//
//import java.util.GregorianCalendar;
//
//import at.helios.calendar.helper.HolidayHelper;
//import at.helios.model.Department;
//import at.helios.model.Person;
//import at.helios.model.Training;
//import at.helios.model.dao.DepartmentDAO;
//import at.helios.model.dao.PersonDAO;
//import at.helios.model.dao.TrainingDAO;
//import at.helios.sheduling.ShiftNode;
//import at.helios.sheduling.ShiftTree;
//import at.helios.sheduling.Teams;
//
///**
// * TODO BESCHREIBUNG_HIER_EINFUEGEN
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   24.03.2010 Neuerstellung
// * </PRE>
// */
//public class GraphvizPrinter
//{
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args)
//    {
//        DepartmentDAO oDepartmentDAO = new DepartmentDAO();
//        oDepartmentDAO.findAll();
//        
//        TrainingDAO oTrainingDAO = new TrainingDAO();
//        oTrainingDAO.findAll();
////        
//        PersonDAO oPersonDAO = new PersonDAO();
//        oPersonDAO.findAll();
//        
//        Department oDepartment = Department.getDepartmentById(1);
//        
//        GregorianCalendar oPeriodStart = new GregorianCalendar(2011,0,1);
//        GregorianCalendar oPeriodEnd = new GregorianCalendar(2011,0,31);
//        HolidayHelper oHolidayHelper = new HolidayHelper();
//        ShiftTree oShiftTree = new ShiftTree(oPeriodStart, oPeriodEnd, oDepartment, Teams.nef, 1, oHolidayHelper);
//        
//        oShiftTree.generateShiftTree();
//        
//        oShiftTree.schedulePeriod();
//        
//        for(ShiftNode oShiftNode : oShiftTree.getShiftNodes())
//        {
//            for(int i = 0; i < oShiftNode.getTeamCount(); i++)
//            {
//                System.out.println(i + "--" + oShiftNode.getShortName()+"  "+oShiftNode.getSelectedDriverIndex(i) + "/" + oShiftNode.getDriverNodes().size() + "  " + (oShiftNode.getSelectedDriverIndex(i) == -1 ? "##" : oShiftNode.getSelectedDriverNode(i).getSelectedMediIndex() + "/" + oShiftNode.getSelectedDriverNode(i).getMediCount()));
//            }
//        }
//        
//        GraphvizCreator.printGraphviz(oShiftTree);
//        
////        for(Person oPerson : Person.getPersonsByDepartment(Department.getDepartmentById(2)))
////        {
////            //System.out.println(oPerson.getEMTNumber() + "  " + oPerson.getForename() + "  " + oPerson.getSurname());
////            if(oPerson.getTrainings().keySet().contains(Training.getTrainingById(3)) || oPerson.getTrainings().keySet().contains(Training.getTrainingById(4)))
////            {
////                System.out.println(oPerson.getEMTNumber() + "  " + oPerson.getForename() + "  " + oPerson.getSurname() + "\t" + oPerson.getShifts().size() + " " + oPerson.getMaxShiftCount() + " " +((float)oPerson.getShifts().size()/(float)oPerson.getMaxShiftCount()) + "\t\t" + oPerson.getTrainings().keySet());
////            }
////        }
//    }
//
//}
