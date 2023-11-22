package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MailModel {
    public static List<String> getAllEmailsByClass(String stuClass) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Email FROM Student WHERE Class = ?");
        pstm.setString(1, stuClass);
        ResultSet resultSet = pstm.executeQuery();
        ArrayList<String> EmailList = new ArrayList<>();
        if (resultSet.next()){
            EmailList.add(resultSet.getString(1));
        }
        return EmailList;
    }

    public static List<String> getAllEmailFromStudentOrTeacher(String teachOrStu) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Email FROM ?");
        pstm.setString(1, teachOrStu);
        ResultSet resultSet = pstm.executeQuery();
        ArrayList<String> EmailList = new ArrayList<>();
        if (resultSet.next()){
            EmailList.add(resultSet.getString(1));
        }
        return EmailList;
    }
}
