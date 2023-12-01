package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgetPasswordModel {

    public static String getEmail(String nic) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Email FROM user WHERE NIC = ?");
        pstm.setString(1,nic);
        ResultSet resultSet = pstm.executeQuery();
        String Email = null;
        if (resultSet.next()) {
            Email = resultSet.getString(1);
        }
        return Email;
    }

    public static boolean changeCredentials(String userName, String password, String NIC) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE user SET user_id = ?, Password = ? WHERE NIC = ?");
        pstm.setString(1, userName);
        pstm.setString(2, password);
        pstm.setString(3, NIC);
        return pstm.executeUpdate() > 0;
    }
}
