package lk.ijse.ProjectSihina.model;

import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ExamDto;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExamModel {

    public static boolean AddExam(ExamDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm1 = connection.prepareStatement("SELECT class_Id FROM Class WHERE Name = ?");
        pstm1.setString(1, dto.getClassName());
        ResultSet resultSet1 = pstm1.executeQuery();
        String Class_Id = null;

        if (resultSet1.next()){
            Class_Id = resultSet1.getString(1);
        }

        PreparedStatement pstm2 = connection.prepareStatement("SELECT Sub_id FROM Subject WHERE Sub_Name = ?");
        pstm2.setString(1, dto.getSubject());
        ResultSet resultSet2 = pstm2.executeQuery();
        String Sub_Id = null;
        
        if (resultSet2.next()) {
            Sub_Id = resultSet2.getString(1);
        }
        
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Exam VALUES (?,?,?,?,?,?,?)");
        pstm.setString(1, dto.getExamId());
        pstm.setDate(2, Date.valueOf(dto.getDate()));
        pstm.setTime(3, Time.valueOf(dto.getStartTime()));
        pstm.setTime(4, Time.valueOf(dto.getEndTime()));
        pstm.setString(5, dto.getDescription());
        pstm.setString(6, Class_Id);
        pstm.setString(7, Sub_Id);
        return pstm.executeUpdate() > 0;

    }

    public static boolean deleteExam(String examId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Exam WHERE Exam_id = ?");
        pstm.setString(1, examId);
        return pstm.executeUpdate() > 0;
    }

    public static ExamDto SearchExam(String examId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Exam");
        ResultSet resultSet = pstm.executeQuery();

        PreparedStatement pstm1 = connection.prepareStatement("SELECT Name FROM Class WHERE class_id = ?");
        PreparedStatement pstm2 = connection.prepareStatement("SELECT Sub_Name FROM Subject WHERE Sub_id = ?");

        ExamDto dto = null;

        if (resultSet.next()) {
            String Exam_id = resultSet.getString(1);
            LocalDate date = resultSet.getDate(2).toLocalDate();
            LocalTime StartTime = resultSet.getTime(3).toLocalTime();
            LocalTime EndTime = resultSet.getTime(4).toLocalTime();
            String description = resultSet.getString(5);
            String ClassName = null;
            String SubjectName = null;


            pstm1.setString(1, resultSet.getString(6));
            ResultSet resultSet1 = pstm1.executeQuery();
            if (resultSet1.next()) {
                ClassName = resultSet1.getString(1);
            }

            pstm2.setString(1, resultSet.getString(7));
            ResultSet resultSet2 = pstm2.executeQuery();
            if (resultSet2.next()) {
                SubjectName = resultSet2.getString(1);
            }

            dto = new ExamDto(Exam_id,ClassName,SubjectName,description,date,StartTime,EndTime);
        }
        return dto;
    }

    public static boolean updateExam(ExamDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm1 = connection.prepareStatement("SELECT class_Id FROM Class WHERE Name = ?");
        pstm1.setString(1, dto.getClassName());
        ResultSet resultSet1 = pstm1.executeQuery();
        String Class_Id = null;
        if (resultSet1.next()){
            Class_Id = resultSet1.getString(1);
        }

        PreparedStatement pstm2 = connection.prepareStatement("SELECT Sub_id FROM Subject WHERE Sub_Name = ?");
        pstm2.setString(1, dto.getSubject());
        ResultSet resultSet2 = pstm2.executeQuery();
        String Sub_Id = null;
        if (resultSet2.next()) {
            Sub_Id = resultSet2.getString(1);
        }

        PreparedStatement pstm = connection.prepareStatement("UPDATE Exam SET date = ? , Start_time = ? , " +
                "End_time = ? , Description = ? , class_id = ? , Sub_id = ? WHERE Exam_id = ?");
        pstm.setDate(1, Date.valueOf(dto.getDate()));
        pstm.setTime(2, Time.valueOf(dto.getStartTime()));
        pstm.setTime(3, Time.valueOf(dto.getEndTime()));
        pstm.setString(4, dto.getDescription());
        pstm.setString(5, Class_Id);
        pstm.setString(6, Sub_Id);
        pstm.setString(7, dto.getExamId());
        return pstm.executeUpdate() > 0;
    }

    public static List<ExamDto> getAllExam() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Exam");
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<ExamDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(new ExamDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5).toLocalDate(),
                    resultSet.getTime(6).toLocalTime(),
                    resultSet.getTime(7).toLocalTime()
            ));
        }
        return dtoList;
    }

    public static String generateExamId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Exam_id FROM Exam ORDER BY Exam_id DESC LIMIT 1");
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return splitExamId(resultSet.getString(1));
        }
        return "EX001";
    }

    private static String splitExamId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("EX0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "EX00"+id;
            }else {
                if (length < 3){
                    return "Ex0"+id;
                }else {
                    return "EX"+id;
                }
            }
        }
        return "EX001";
    }
}
