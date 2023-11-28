package lk.ijse.ProjectSihina.model;

import javafx.scene.image.Image;
import lk.ijse.ProjectSihina.User.UserConnection;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.StudentDto;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentModel {
    public static boolean saveStudent(StudentDto dto, File imageFile) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Student VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        pstm.setString(1, dto.getID());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getAddress());
        pstm.setDate(5, Date.valueOf(dto.getDob()));
        pstm.setString(6, dto.getGender());
        pstm.setString(7, dto.getContact());
        pstm.setString(8, dto.getStu_Class());
        pstm.setString(9, dto.getSubject());

        Blob imageBlob = convertFileToBytes(imageFile);

        pstm.setBlob(10, imageBlob);

        String userName = UserConnection.getInstance().getUserName();
        String Password = UserConnection.getInstance().getPassword();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM user WHERE User_Name = ? AND Password = ?");
        preparedStatement.setString(1,userName);
        preparedStatement.setString(2, Password);
        ResultSet resultSet = preparedStatement.executeQuery();
        String user = null;
        if (resultSet.next()) {
             user = resultSet.getString(1);
        }

        pstm.setString(11, user);
        return pstm.executeUpdate() > 0;

    }

    private static Blob convertFileToBytes(File imageFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0 ,bytesRead);
            }
            fileInputStream.close();

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new javax.sql.rowset.serial.SerialBlob(byteArray);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Image convertBlobToImage(Blob blob) {

        try {
            if (blob == null) {
                return null;
            }
            try (InputStream binaryStream = blob.getBinaryStream()) {
                return new Image(binaryStream);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteStudent(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Student WHERE Stu_id = ?");
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateStudent(StudentDto dto, File imageFile) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Student SET Name = ?, " +
                "Email = ?, Address = ?, D_O_B = ?, Gender = ?, Contact = ?, Class = ?, " +
                "subjects = ?, image = ? WHERE Stu_id = ?");
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getAddress());
        pstm.setDate(5, Date.valueOf(dto.getDob()));
        pstm.setString(6, dto.getGender());
        pstm.setString(7, dto.getContact());
        pstm.setString(8, dto.getStu_Class());
        pstm.setString(9, dto.getSubject());

        Blob imageBlob = convertFileToBytes(imageFile);

        pstm.setBlob(10, imageBlob);
        pstm.setString(11, dto.getID());

        return pstm.executeUpdate() > 0;
    }

    public static StudentDto searchStudent(String Id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Student WHERE Stu_id = ?");
        pstm.setString(1, Id);

        ResultSet resultSet = pstm.executeQuery();

        StudentDto dto = null;

        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(3);
            String email = resultSet.getString(4);
            String address = resultSet.getString(5);
            LocalDate dob = resultSet.getDate(6).toLocalDate();
            String gender = resultSet.getString(7);
            String contact = resultSet.getString(8);
            String stu_class = resultSet.getString(9);
            String subject = resultSet.getString(10);
            Image image = convertBlobToImage(resultSet.getBlob(11));

            dto = new StudentDto(id,name,address,gender,email,dob,contact,stu_class,subject,image);
        }

        return dto;
    }

    public static List<StudentDto> getAllStudent() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Stu_id,Name,Class,Email,Contact FROM Student");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<StudentDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(
                    new StudentDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
            );
        }
        return dtoList;
    }

    public static String generateStudentId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Stu_id FROM Student ORDER BY Stu_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return splitStuId(resultSet.getString(1));
        }
        return "ID001";
    }

    private static String splitStuId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("ID0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "ID00"+id;
            }else {
                if (length < 3){
                    return "ID0"+id;
                }else {
                    return "ID"+id;
                }
            }
        }
        return "ID001";
    }
}
