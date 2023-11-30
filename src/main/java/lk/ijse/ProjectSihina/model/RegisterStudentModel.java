package lk.ijse.ProjectSihina.model;

import javafx.scene.control.Alert;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.GuardianDto;
import lk.ijse.ProjectSihina.dto.PaymentDto;
import lk.ijse.ProjectSihina.dto.StudentDto;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterStudentModel {
    public static boolean SaveStudentRegisterAndPayment(StudentDto studentDto, PaymentDto payDto, GuardianDto guardianDto) throws SQLException {
        Connection connection = null;
        try {
            connection= DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isSaved = StudentModel.saveStudent(studentDto);
            System.out.println(1);
            if (isSaved) {
                System.out.println(2);
                boolean isAddGuard = GuardianModel.AddGuard(guardianDto);
                if (isAddGuard) {
                    System.out.println(3);
                    boolean isAddPayment = PaymentModel.AddPayment(payDto);
                    if (isAddPayment) {
                        System.out.println(4);
                        boolean isSaveRegistration = saveDetailRegistration(studentDto, payDto);
                        if (isSaveRegistration) {
                            System.out.println(5);
                            connection.commit();

                            return true;
                        } else {
                            connection.rollback();
                        }
                    } else {
                        connection.rollback();
                    }
                } else {
                    connection.rollback();
                }
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } finally {
            if (connection != null) connection.setAutoCommit(true);
        }
        return false;
    }

    private static boolean saveDetailRegistration(StudentDto studentDto, PaymentDto payDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Registration VALUES (?,?,?,?,?,?,?)");
        pstm.setString(1, studentDto.getID());
        pstm.setString(2, payDto.getPayID());
        pstm.setString(3, studentDto.getName());


        PreparedStatement pstm1 = connection.prepareStatement("SELECT class_id FROM Class WHERE Name = ?");
        pstm1.setString(1, studentDto.getStu_Class());
        ResultSet resultSet = pstm1.executeQuery();
        String ClassId = null;
        if (resultSet.next()){
            ClassId = resultSet.getString(1);
        }

        pstm.setString(4, ClassId);
        pstm.setDouble(5, payDto.getAmount());
        pstm.setDate(6, Date.valueOf(payDto.getDate()));
        pstm.setTime(7, Time.valueOf(payDto.getTime()));

        return pstm.executeUpdate() > 0;
    }


    public static List<PaymentDto> getAllRegisterPayment(String type) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT P.Pay_id , P.Stu_id, S.Name, P.Stu_Class, P.date, P.Amount FROM Payment P JOIN Student S ON P.Stu_id = S.Stu_id WHERE P.Type = ? ORDER BY P.Pay_id DESC");
        pstm.setString(1, type);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<PaymentDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new PaymentDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5).toLocalDate(),
                    resultSet.getDouble(6)
            ));
        }
        return dtoList;
    }
}
