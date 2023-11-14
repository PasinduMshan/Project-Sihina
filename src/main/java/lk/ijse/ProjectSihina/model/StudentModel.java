package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.StudentDto;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentModel {
    public static boolean saveStudent(StudentDto dto, File imageFile) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Student VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
        pstm.setString(1, dto.getID());
        pstm.setString(2, dto.getBarcodeID());
        pstm.setString(3, dto.getName());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getAddress());
        pstm.setDate(6, Date.valueOf(dto.getDob()));
        pstm.setString(7, dto.getGender());
        pstm.setString(8, dto.getContact());
        pstm.setString(9, dto.getStu_Class());
        pstm.setString(10, dto.getSubject());

        byte[] imageBytes = convertFileToBytes(imageFile);

        pstm.setBytes(11, imageBytes);

        String user = "U001";

        pstm.setString(12, user);

        return pstm.executeUpdate() > 0;

    }

    private static byte[] convertFileToBytes(File imageFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(imageFile);;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0 ,bytesRead);
            }
            fileInputStream.close();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
