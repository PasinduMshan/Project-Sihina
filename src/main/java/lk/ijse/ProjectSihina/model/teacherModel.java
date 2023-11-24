package lk.ijse.ProjectSihina.model;

import javafx.scene.image.Image;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.TeacherDto;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class teacherModel {

    public static boolean addTeacher(TeacherDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Teacher VALUES (?,?,?,?,?,?,?)");
        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getAddress());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getContactNo());
        pstm.setString(6, dto.getSubjects());


        Blob blob = convertImageToBlob(dto.getImageTeacher());

        pstm.setBlob(7, blob);

        return pstm.executeUpdate() > 0;

    }

    public static Blob convertImageToBlob(Image image) {
        try {
            if (image == null) {
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Choose an appropriate image format based on your application (e.g., "png", "jpg")
            String format = "png";

            // Convert JavaFX Image to BufferedImage
            java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Write BufferedImage to output stream
            ImageIO.write(bufferedImage, format, outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            return new SerialBlob(imageBytes);
        } catch (IOException | SQLException e) {
            //Error converting Image to Blob
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteTeacher(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Teacher WHERE Teacher_id = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean updateTeacher(TeacherDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Teacher SET Name = ?, Address = ?, Email = ?, " +
                "Contact = ?, Subject = ? ,TeacherImage = ? WHERE Teacher_id = ?");


        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getContactNo());
        pstm.setString(5, dto.getSubjects());
        pstm.setBlob(6, convertImageToBlob(dto.getImageTeacher()));
        pstm.setString(7, dto.getId());

        return pstm.executeUpdate() > 0;
    }

   public static TeacherDto searchTeacher(String id) throws SQLException {
       Connection connection = DbConnection.getInstance().getConnection();
       PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Teacher WHERE Teacher_id = ?");
       pstm.setString(1,id);
       ResultSet resultSet = pstm.executeQuery();

       TeacherDto dto = null;

       if (resultSet.next()) {
           String T_id = resultSet.getString(1);
           String T_Name = resultSet.getString(2);
           String T_Address = resultSet.getString(3);
           String T_email = resultSet.getString(4);
           String T_Contact = resultSet.getString(5);
           String T_Subject = resultSet.getString(6);
           Image image = convertBlobToImage(resultSet.getBlob(7));

           dto = new TeacherDto(T_id,T_Name,T_Address,T_email,T_Subject,T_Contact,image);

       }
       return dto;
   }

   private static Image convertBlobToImage(Blob blob) {
       try {
            if (blob == null) {
                return null;
            }
           try (InputStream binaryStream = blob.getBinaryStream()){
               return new Image(binaryStream);
           }
       } catch (SQLException | IOException e) {
           throw new RuntimeException(e);
       }

   }

    public static List<TeacherDto> getAllTeachers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Teacher");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<TeacherDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new TeacherDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    convertBlobToImage(resultSet.getBlob(6))
            ));
        }
        return dtoList;
    }

    public static String generateTeacherId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Teacher_id FROM Teacher ORDER BY Teacher_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return splitTeacherId(resultSet.getString(1));
        }
        return "TID001";
    }

    private static String splitTeacherId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("TID0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "TID00"+id;
            }else {
                if (length < 3){
                    return "TID0"+id;
                }else {
                    return "TID"+id;
                }
            }
        }
        return "TID001";
    }
}
