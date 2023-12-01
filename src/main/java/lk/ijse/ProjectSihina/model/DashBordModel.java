package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.DashBordScheduleDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashBordModel {

    public static String getSumOfAmount(LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT SUM(Amount) FROM Payment WHERE date = ?");
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

    public static List<DashBordScheduleDto> getTodaySchedule(LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm1 = connection.prepareStatement("SELECT DAYNAME(NOW()) AS current_day");
        ResultSet resultSet1 = pstm1.executeQuery();
        String day  = null;
        if (resultSet1.next()) {
            day  = resultSet1.getString(1);
        }

        PreparedStatement pstm = connection.prepareStatement("SELECT S.Start_Time , S.End_Time , C.Name , Su.Sub_Name " +
                "FROM Schedule S JOIN Class C ON S.class_id = C.class_id JOIN Subject Su ON Su.Sub_id = S.Sub_id WHERE S.Class_day = ?");
        pstm.setString(1, day);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<DashBordScheduleDto> dtoList = new ArrayList<>();

        String type = "Today Class";

        while (resultSet.next()) {
            dtoList.add(new DashBordScheduleDto(
                    resultSet.getTime(1).toLocalTime(),
                    resultSet.getTime(2).toLocalTime(),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    type
            ));
        }
        return dtoList;
    }

    public static List<DashBordScheduleDto> getTodayExams(LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT E.Start_time, E.End_time, C.Name, Su.Sub_Name " +
                "FROM Exam E JOIN Class C ON E.class_id = C.class_id JOIN Subject Su ON E.Sub_id = Su.Sub_id WHERE E.date = ?");
        pstm.setString(1, String.valueOf(date));
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<DashBordScheduleDto> dtoList = new ArrayList<>();

        String type = "Exam";

        while (resultSet.next()) {
            dtoList.add(new DashBordScheduleDto(
                    resultSet.getTime(1).toLocalTime(),
                    resultSet.getTime(2).toLocalTime(),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    type
            ));
        }
        return dtoList;
    }
}
