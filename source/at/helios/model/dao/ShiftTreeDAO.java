//package at.helios.model.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import at.helios.common.DBHelper;
//import at.helios.sheduling.ShiftNode;
//import at.helios.sheduling.ShiftTree;
//
///**
// * DAO für ShiftTree
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   30.03.2009 Erstkommentierung
// * </PRE>
// **/
//public class ShiftTreeDAO
//{
//    private static final String SQL_INSERT_SHIFTTREE = "INSERT INTO tShiftTree(nShiftplanId, dPeriodStart, dPeriodEnd, nTeamCount, cTeamType) VALUES  (?,?,?,?,?);";
//    private static final String SQL_SELECT_PK = "SELECT nShiftTreeId FROM tShiftTree WHERE nShiftplanId = ? AND dPeriodStart = ? AND dPeriodEnd = ? AND nTeamCount = ? AND cTeamType = ?";
//    
//    /**
//     * Speichert mehrere ShiftTree
//     * @param oShiftTree    BESCHREIBUNG_EINFUEGEN
//     **/
//    public void insert(ShiftTree oShiftTree, int iShiftplanId)
//    {
//        Connection oConnection = DBHelper.getConnection();
//        ResultSet oResultSet = null;
//        try
//        {
//            PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_SHIFTTREE);
//            
//            oStatement.setInt(1, iShiftplanId);
//            oStatement.setDate(2, new java.sql.Date((oShiftTree.getPeriodStart().getTime().getTime())));
//            oStatement.setDate(3, new java.sql.Date((oShiftTree.getPeriodEnd().getTime().getTime())));
//            oStatement.setInt(4, oShiftTree.getTeamCount());
//            oStatement.setString(5, oShiftTree.getTeamType().toString());
//            
//            oStatement.execute();
//
//            oStatement = oConnection.prepareStatement(SQL_SELECT_PK);
//            oStatement.setInt(1, iShiftplanId);
//            oStatement.setDate(2, new java.sql.Date((oShiftTree.getPeriodStart().getTime().getTime())));
//            oStatement.setDate(3, new java.sql.Date((oShiftTree.getPeriodEnd().getTime().getTime())));
//            oStatement.setInt(4, oShiftTree.getTeamCount());
//            oStatement.setString(5, oShiftTree.getTeamType().toString());
//            
//            oResultSet = oStatement.executeQuery();
//            
//            if (oResultSet.next())
//            {
//               int iShiftTreeId = oResultSet.getInt("nShiftTreeId");
//               ShiftNodeDAO oShiftNodeDAO = new ShiftNodeDAO();
//               
//               for(ShiftNode oShiftNode: oShiftTree.getShiftNodes())
//               {
//                   oShiftNodeDAO.insert(oShiftNode, iShiftTreeId);
//               }
//            }
//            
//            oStatement.close();
//            oResultSet.close();
//            oConnection.close();
//        }
//        catch (SQLException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//}
