package lk.ijse.ProjectSihina.model;

import com.github.sarxos.webcam.Webcam;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.GuardianDto;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuardianModel {

    public static String getGuardianId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Guard_id FROM Guardian ORDER BY Guard_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return splitGuardId(resultSet.getString(1));
        }
        return "G001";
    }

    private static String splitGuardId(String currentId) {
        if (currentId != null) {
            String[]  strings = currentId.split("G0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2) {
                return "G00"+id;
            } else {
                if (length < 3) {
                    return "G0"+id;
                } else {
                    return "G"+id;
                }
            }
        }
        return "G001";
    }

    public static boolean AddGuard(GuardianDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Guardian VALUES (?,?,?,?,?,?)");
        pstm.setString(1, dto.getGuardId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getContact());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getOccupation());
        pstm.setString(6, dto.getStuId());
        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteGuard(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Guardian WHERE Guard_id = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    public static GuardianDto SearchGuardianFromContact(String contact) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Guardian WHERE contactNo = ?");
        pstm.setString(1, contact);
        ResultSet resultSet = pstm.executeQuery();
        GuardianDto guardianDto = null;
        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String contactNO = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String occupation = resultSet.getString(5);
            String Stu_id = resultSet.getString(6);
            guardianDto = new GuardianDto(id, name, contactNO, Email, occupation, Stu_id);
        }
        return guardianDto;
    }

    public static GuardianDto SearchGuardianFromId(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Guardian WHERE Guard_id = ?");
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();
        GuardianDto guardianDto = null;
        if (resultSet.next()) {
            String ID = resultSet.getString(1);
            String name = resultSet.getString(2);
            String contactNO = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String occupation = resultSet.getString(5);
            String Stu_id = resultSet.getString(6);
            guardianDto = new GuardianDto(ID, name, contactNO, Email, occupation, Stu_id);
        }
        return guardianDto;
    }

    public static GuardianDto SearchGuardianFromStuId(String stuId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Guardian WHERE Stu_id = ?");
        pstm.setString(1, stuId);
        ResultSet resultSet = pstm.executeQuery();
        GuardianDto guardianDto = null;
        if (resultSet.next()) {
            String ID = resultSet.getString(1);
            String name = resultSet.getString(2);
            String contactNO = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String occupation = resultSet.getString(5);
            String Stu_id = resultSet.getString(6);
            guardianDto = new GuardianDto(ID, name, contactNO, Email, occupation, Stu_id);
        }
        return guardianDto;
    }

    public static boolean updateGuardian(GuardianDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Guardian SET Guard_Name = ?, contactNo = ?, Email = ?, occupation = ?, Stu_id = ? WHERE Guard_id = ?");
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getContact());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getOccupation());
        pstm.setString(5, dto.getStuId());
        pstm.setString(6, dto.getGuardId());
        return pstm.executeUpdate() > 0;
    }

    public static List<GuardianDto> getAllGuardian() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Guardian");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<GuardianDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new GuardianDto(
               resultSet.getString(1),
               resultSet.getString(2),
               resultSet.getString(3),
               resultSet.getString(4),
               resultSet.getString(5),
               resultSet.getString(6)
            ));
        }
        return dtoList;
    }
}
