package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;

import java.sql.*;
import java.time.LocalDate;

public class DashBordModel {

    public static String getSumOfAmount(LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(Amount) FROM Payment WHERE date = ?");
        pstm.setDate(1, Date.valueOf(date));
        ResultSet resultSet = pstm.executeQuery();
        String count = null;
        if (resultSet.next()) {
            count = resultSet.getString(1);
        }
        return count;
    }

    public static String getStudentCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(Stu_id) FROM Student");
        ResultSet resultSet = pstm.executeQuery();
        String Count = null;
        if (resultSet.next()) {
            Count = resultSet.getString(1);
        }
        return Count;
    }

    public static String getTeacherCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(Teacher_id) FROM Teacher");
        ResultSet resultSet = pstm.executeQuery();
        String Count = null;
        if (resultSet.next()) {
            Count = resultSet.getString(1);
        }
        return Count;
    }

    public static String getSubjectCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(Sub_id) FROM Subject");
        ResultSet resultSet = pstm.executeQuery();
        String Count = null;
        if (resultSet.next()) {
            Count = resultSet.getString(1);
        }
        return Count;
    }
}
