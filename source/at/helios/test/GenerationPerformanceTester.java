///**
// * 
// */
//package at.helios.test;
//
//import java.text.SimpleDateFormat;
//import java.util.GregorianCalendar;
//
//import at.helios.calendar.helper.HolidayHelper;
//import at.helios.model.Department;
//import at.helios.model.Person;
//import at.helios.model.dao.PersonDAO;
//import at.helios.model.dao.TrainingDAO;
//import at.helios.sheduling.ShiftTree;
//import at.helios.sheduling.Teams;
//
///**
// * TODO BESCHREIBUNG_HIER_EINFUEGEN
// * @author
// * <PRE>
// *         ID    date       description
// *         markus.scherer 12.03.2010 Neuerstellung
// * </PRE>
// */
//public class GenerationPerformanceTester
//{
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args)
//    {
//        TrainingDAO oTrainingDAO = new TrainingDAO();
//        oTrainingDAO.findAll();
//        
//        PersonDAO oPersonDAO = new PersonDAO();
//        oPersonDAO.findAll();
//        
//        Department oDepartment = Department.getDepartmentById(1);
//        Person.getPersonsByDepartment(oDepartment);
//        GregorianCalendar oPeriodStart = new GregorianCalendar(2010,3,1);
//        GregorianCalendar oPeriodEnd = new GregorianCalendar(2010,3,30);
//        HolidayHelper oHolidayHelper = new HolidayHelper();
////        ShiftTree oShiftTree = new ShiftTree(oPeriodStart, oPeriodEnd, oDepartment, Teams.nef, 1, oHolidayHelper);
//        SimpleDateFormat oFormat = new SimpleDateFormat("dd.MMMM.yyyy");
//        System.out.println(oFormat.format(oPeriodEnd.getTime()));
//        
//        long time = System.currentTimeMillis();
//        
//        oShiftTree.generateShiftTree();
////        for(Person oPerson: Person.getPersonsByDepartment(oDepartment))
////        {
////            if(oPerson.getTrainings().containsKey(Training.getTrainingById(1)) || oPerson.getTrainings().containsKey(Training.getTrainingById(2)))
////            {
////                System.out.println("t");
////            }
////        }
//        
//        System.out.println(System.currentTimeMillis()-time);
//
//
//    }
//
//}
