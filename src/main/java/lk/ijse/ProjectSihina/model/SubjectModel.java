package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.TeacherDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectModel {

    public static boolean saveSubject(SubjectDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Subject VALUES (?,?,?,?,?)");
        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getSubject());
        pstm.setString(3, dto.getAvailableClass());
        pstm.setString(4, dto.getTeacherName());
        pstm.setString(5, String.valueOf(dto.getMonthlyAmount()));
        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteSubject(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Subject WHERE Sub_id = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean updateSubject(SubjectDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Subject SET Sub_Name = ?, AvailableClass = ?," +
                " teacherName = ?, MonthlyAmount = ? WHERE Sub_id = ?");
        pstm.setString(1, dto.getSubject());
        pstm.setString(2, dto.getAvailableClass());
        pstm.setString(3, dto.getTeacherName());
        pstm.setString(4, String.valueOf(dto.getMonthlyAmount()));
        pstm.setString(5, dto.getId());
        return pstm.executeUpdate() > 0;
    }

    public static SubjectDto searchSubject(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Subject WHERE Sub_id = ?");
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        SubjectDto dto = null;

        if (resultSet.next()) {
            String S_id = resultSet.getString(1);
            String S_Name = resultSet.getString(2);
            String AvailableClass = resultSet.getString(3);
            String Teacher = resultSet.getString(4);
            double Amount = Double.parseDouble(resultSet.getString(5));

            dto = new SubjectDto(S_id, S_Name, AvailableClass, Teacher, Amount);
        }
        return dto;
    }

    public static List<SubjectDto> getAllDetails() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Subject");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<SubjectDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new SubjectDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            ));
        }
        return dtoList;
    }

    public static String generateSubId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Sub_id FROM Subject ORDER BY Sub_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return splitSubId(resultSet.getString(1));
        }
        return "SUB001";
    }

    private static String splitSubId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("SUB0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "SUB00"+id;
            }else {
                if (length < 3){
                    return "SUB0"+id;
                }else {
                    return "SUB"+id;
                }
            }
        }
        return "SUB001";
    }

    public static List<TeacherDto> getAllTeacher() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Name FROM Teacher");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<TeacherDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(new TeacherDto(
                    resultSet.getString(1)
            ));
        }
        return dtoList;
    }

    public static double getAmountINSubject(String subject) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT MonthlyAmount FROM Subject WHERE Sub_Name = ?");
        pstm.setString(1, subject);
        ResultSet resultSet = pstm.executeQuery();
        double amount = 0;
        if (resultSet.next()) {
            amount = resultSet.getDouble(1);
        }
        return amount;
    }
}
