package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SignUpModel {

    public static boolean updateCredentials(String newUserName, String newPassword, String NIC) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE user SET User_Name = ?, Password = ? WHERE NIC = ?");
        pstm.setString(1, newUserName);
        pstm.setString(2, newPassword);
        pstm.setString(3, NIC);
        return pstm.executeUpdate() > 0;
    }

    public static List<UserDto> getAllUsers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM user");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<UserDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new UserDto(
               resultSet.getString(1),
               resultSet.getString(2),
               resultSet.getString(3),
               resultSet.getString(4),
               resultSet.getString(5),
               resultSet.getString(6),
               resultSet.getString(7)
            ));
        }
        return dtoList;
    }

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

    private static String splitUserId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("U0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "U00"+id;
            }else {
                if (length < 3){
                    return "U0"+id;
                }else {
                    return "U"+id;
                }
            }
        }
        return "U001";
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

    public static boolean deleteUser(String userId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM user WHERE user_id = ?");
        pstm.setString(1, userId);
        return pstm.executeUpdate() > 0;
    }

    public static UserDto searchUser(String nic) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM user WHERE NIC = ?");
        pstm.setString(1, nic);
        ResultSet resultSet = pstm.executeQuery();

        UserDto userDto = null;

        if (resultSet.next()) {
            String userId = resultSet.getString(1);
            String FirstName = resultSet.getString(2);
            String LastName = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String NIC = resultSet.getString(5);
            String userName = resultSet.getString(6);
            String password = resultSet.getString(7);

            userDto = new UserDto(userId, FirstName, LastName, Email, NIC, userName, password);
        }
        return userDto;
    }

    public static boolean updateUser(UserDto userDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE user SET First_Name = ?, Last_Name = ?, Email = ?, NIC = ? WHERE user_id = ?");
        pstm.setString(1, userDto.getFirstName());
        pstm.setString(2, userDto.getLastName());
        pstm.setString(3, userDto.getEmail());
        pstm.setString(4, userDto.getNIC());
        pstm.setString(5, userDto.getUserId());
        return pstm.executeUpdate() > 0;
    }

    public static boolean checkCredentials(String userNameNow, String passwordNow, String NIC) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT User_Name , Password FROM user WHERE NIC = ?");
        pstm.setString(1, NIC);
        ResultSet resultSet = pstm.executeQuery();
        String U_Name = null;
        String Password = null;
        boolean flag = false;

        if (resultSet.next()) {
            U_Name = resultSet.getString(1);
            Password = resultSet.getString(2);
        }
        if (U_Name.equals(userNameNow) && Password.equals(passwordNow)) {
            flag = true;
        }
        return flag;
    }
}
