package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ScheduleDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.TeacherDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleModel {

    public static boolean AddSchedule(ScheduleDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Schedule VALUES (?,?,?,?,?,?)");
        pstm.setString(1, getClassId(dto.getStu_Class()));
        pstm.setString(2, getSubjectId(dto.getSubject()));
        pstm.setString(3, getTeacherId(dto.getTeacher()));
        pstm.setString(4, dto.getDay());
        pstm.setString(5, String.valueOf(dto.getStartTime()));
        pstm.setString(6, String.valueOf(dto.getEndTime()));
        return pstm.executeUpdate() > 0;
    }

    private static String getTeacherId(String teacher) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Teacher_id FROM Teacher WHERE Name = ?");
        pstm.setString(1, teacher);
        ResultSet resultSet = pstm.executeQuery();
        String Teacher_id = null;
        if (resultSet.next()) {
            Teacher_id = resultSet.getString(1);
        }
        return Teacher_id;
    }

    private static String getClassId(String stuClass) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT class_id FROM Class WHERE Name = ?");
        pstm.setString(1, stuClass);
        ResultSet resultSet = pstm.executeQuery();
        String Class_Id = null;
        if (resultSet.next()) {
            Class_Id = resultSet.getString(1);
        }
        return Class_Id;
    }

    private static String getSubjectId(String subject) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Sub_id FROM Subject WHERE Sub_Name = ?");
        pstm.setString(1, subject);
        ResultSet resultSet = pstm.executeQuery();
        String Sub_Id = null;
        if (resultSet.next()) {
            Sub_Id = resultSet.getString(1);
        }
        return Sub_Id;
    }


    public static boolean DeleteShedule(ScheduleDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Schedule WHERE class_id = ? AND Sub_id = ? AND Teacher_id = ? AND Class_day = ? AND Start_Time = ? AND End_Time = ?");
        pstm.setString(1, getClassId(dto.getStu_Class()));
        pstm.setString(2, getSubjectId(dto.getSubject()));
        pstm.setString(3, getTeacherId(dto.getTeacher()));
        pstm.setString(4, dto.getDay());
        pstm.setString(5, String.valueOf(dto.getStartTime()));
        pstm.setString(6, String.valueOf(dto.getEndTime()));
        return pstm.executeUpdate() > 0;
    }

    public static List<ScheduleDto> getAllSchedule() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Schedule ORDER BY Class_Day ");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ScheduleDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new ScheduleDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getTime(5).toLocalTime(),
                    resultSet.getTime(6).toLocalTime()
            ));
        }
        return dtoList;
    }
    public static List<SubjectDto> getAllSubject() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Sub_Name FROM Subject");
        ResultSet resultSet = pstm.executeQuery();
        ArrayList<SubjectDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new SubjectDto(
                    resultSet.getString(1)
            ));
        }
        return dtoList;
    }

    public static List<TeacherDto> getAllTeacher() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Name FROM Teacher");
        ResultSet resultSet = pstm.executeQuery();
        ArrayList<TeacherDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new TeacherDto(
                    resultSet.getString(1)
            ));
        }
        return dtoList;
    }
}
