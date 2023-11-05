package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpModel {

    public String generateNextUserId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return splitUserId(resultSet.getString(1));
        }
        return "U001";
    }

    private String splitUserId(String currentUserId) {
        if (currentUserId != null) {
            int id = Integer.parseInt(currentUserId.substring(1));
            id++;
            return "U" + String.format("%03d",id);
        } else {
            return "U001";
        }
    }

    public boolean userRegister(UserDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getUserId());
        pstm.setString(2, dto.getFirstName());
        pstm.setString(3, dto.getLastName());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getNIC());
        pstm.setString(6, dto.getUserName());
        pstm.setString(7, dto.getPassword());

        boolean isRegister = pstm.executeUpdate() > 0;

        return isRegister;
    }
}
