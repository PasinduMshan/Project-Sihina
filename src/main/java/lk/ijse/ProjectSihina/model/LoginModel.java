package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    public boolean checkCredentials(String userName , String password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE Password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, password);

        ResultSet resultSet = ptsm.executeQuery();

        String U_Name = null;
        String PassWord = null;

        if (resultSet.next()) {
            U_Name = resultSet.getString(6);
            PassWord = resultSet.getString(7);
        }

        if (userName.equals(U_Name) && password.equals(PassWord)) {
                return true;
        }
        return false;
    }
}
