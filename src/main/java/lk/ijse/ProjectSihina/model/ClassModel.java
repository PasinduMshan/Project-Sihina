package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.TeacherDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassModel {



    public static boolean savaClass(ClassDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Class VALUES (?,?)");
        pstm.setString(1, dto.getClassID());
        pstm.setString(2, dto.getClassName());

        return pstm.executeUpdate() > 0;
    }

    public static boolean isDeleteClass(String classId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Class WHERE class_id = ?");
        pstm.setString(1, classId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean isUpdate(ClassDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement("UPDATE Class SET Name = ? WHERE class_id = ?");
        pstm.setString(1, dto.getClassName());
        pstm.setString(2, dto.getClassID());

        return pstm.executeUpdate() > 0;
    }

    public static ClassDto searchClass(String classId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Class WHERE class_id = ?");
        pstm.setString(1, classId);

        ResultSet resultSet = pstm.executeQuery();

        ClassDto dto = null;

        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);

            dto = new ClassDto(id, name);
        }
        return dto;
    }


    public static List<ClassDto> getAllClass() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT c.class_id, c.Name, COUNT(s.Stu_id) AS " +
                "student_count FROM Class c LEFT JOIN Registration r ON c.class_id = r.class_id LEFT JOIN Student s ON " +
                "r.Stu_id = s.Stu_id GROUP BY c.class_id, c.Name ORDER BY c.class_id;");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<ClassDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
           dtoList.add(
                   new ClassDto(
                           resultSet.getString(1),
                           resultSet.getString(2),
                           resultSet.getString(3)
                   )
           );
        }
        return dtoList;
    }

    public static String generateClassId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT class_id FROM Class ORDER BY class_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return splitClassId(resultSet.getString(1));
        }
        return "C001";
    }

    private static String splitClassId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("C0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "C00"+id;
            }else {
                if (length < 3){
                    return "C0"+id;
                }else {
                    return "C"+id;
                }
            }
        }
        return "C001";
    }

}
