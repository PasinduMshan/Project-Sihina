package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.PaymentDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentModel {
    public static List<PaymentDto> searchStuPays(String id, String Month) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Pay_id, Subject , Pay_Month FROM Payment WHERE Stu_id = ? AND Pay_Month = ?");
        pstm.setString(1, id);
        pstm.setString(2, Month);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<PaymentDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
           dtoList.add(new PaymentDto(
                   resultSet.getString(1),
                   resultSet.getString(2),
                   resultSet.getDouble(3)
           ));
        }
        return dtoList;
    }

    public static StudentDto searchStu(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Name , subjects  FROM Student WHERE " +
                "Stu_id = ?");
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        StudentDto dto = null;

        if (resultSet.next()) {
            String Stu_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String subject = resultSet.getString(3);

            dto = new StudentDto(Stu_id,name,subject);
        }
        return dto;
    }

    public static String generateNextPayId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Pay_id FROM Payment ORDER BY Pay_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return splitPayId(resultSet.getString(1));
        }
        return "P0001";
    }

    private static String splitPayId(String CurrentPayId) {
        if (CurrentPayId != null) {
            int id = Integer.parseInt(CurrentPayId.substring(1));
            id++;
            return "P" + String.format("%04d",id);
        } else {
            return "P0001";
        }
    }

    public static boolean AddPayment(PaymentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Payment VALUES (?,?,?,?,?,?,?,?,?)");
        pstm.setString(1, dto.getPayID());
        pstm.setString(2, dto.getStuID());
        pstm.setString(3, dto.getType());
        pstm.setString(4, dto.getStuClass());
        pstm.setString(5, dto.getSubject());
        pstm.setString(6, dto.getPayMonth());
        pstm.setDate(7, Date.valueOf(dto.getDate()));
        pstm.setTime(8, Time.valueOf(dto.getTime()));
        pstm.setDouble(9, dto.getAmount());

        return pstm.executeUpdate() > 0;
    }

    public static boolean DeletePayment(String payId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Payment WHERE Pay_id = ?");
        pstm.setString(1, payId);
        return pstm.executeUpdate() > 0;
    }

    public static PaymentDto SearchPaymontId(String payId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT P.Pay_id,P.Stu_id,S.Barcode_id,S.Name,P.Type," +
                "P.Stu_Class,P.Pay_Month,P.Subject,P.Amount,P.date,P.time FROM Payment P JOIN Student S ON P.Stu_id" +
                " = S.Stu_id WHERE Pay_id = ?");
        pstm.setString(1, payId);
        ResultSet resultSet = pstm.executeQuery();

        PaymentDto dto = null;

        if (resultSet.next()) {
            String PayID = resultSet.getString(1);
            String StuId = resultSet.getString(2);
            String BarId = resultSet.getString(3);
            String name = resultSet.getString(4);
            String type = resultSet.getString(5);
            String StuClass = resultSet.getString(6);
            String PayMonth = resultSet.getString(7);
            String Subject = resultSet.getString(8);
            double Amount = resultSet.getDouble(9);
            LocalDate date = resultSet.getDate(10).toLocalDate();
            LocalTime time = resultSet.getTime(11).toLocalTime();

            dto = new PaymentDto(PayID,StuId,BarId,name,type,StuClass,PayMonth,Subject,Amount,date,time);
        }
        return dto;
    }

    public static boolean updatePayment(PaymentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Payment SET Stu_id = ?, Type = ?, Stu_Class = ?, Subject = ?, " +
                "Pay_Month = ?, date = ?, time = ? WHERE Pay_id = ?");
        pstm.setString(1, dto.getStuID());
        pstm.setString(2, dto.getType());
        pstm.setString(3, dto.getStuClass());
        pstm.setString(4, dto.getSubject());
        pstm.setString(5, dto.getPayMonth());
        pstm.setDate(6, Date.valueOf(dto.getDate()));
        pstm.setTime(7, Time.valueOf(dto.getTime()));
        pstm.setDouble(8, dto.getAmount());
        pstm.setString(9, dto.getPayID());

        return pstm.executeUpdate() > 0;
    }

    public static List<PaymentDto> getAllPayment() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT P.Pay_id , S.Name , P.Stu_Class , P.Subject , " +
                "P.Pay_Month , P.Amount FROM Payment P JOIN Student S ON P.Stu_id = S.Stu_id ORDER BY Pay_id DESC ");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<PaymentDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new PaymentDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getDouble(6)
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

    public static String getAllAttendant(String stu_id, String subject, String month, String stu_class) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(Att_id) FROM Attendance WHERE Stu_id = ? AND Subject = ? AND Month = ? AND Stu_Class = ?");
        ResultSet resultSet = pstm.executeQuery();
        String Count = null;
        if (resultSet.next()) {
             Count = resultSet.getString(1);
        }
        return Count;
    }
}
