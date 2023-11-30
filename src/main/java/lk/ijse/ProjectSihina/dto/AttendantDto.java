package lk.ijse.ProjectSihina.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendantDto {
    String Att_id;
    String StudentId;
    String StudentName;
    String ClassName;
    String Month;
    String Subject;
    LocalDate Date;
    LocalTime Time;
    String Type;

    public AttendantDto(String att_id, String studentName, String className, String month, LocalDate date,  String type) {
        Att_id = att_id;
        StudentName = studentName;
        ClassName = className;
        Month = month;
        Date = date;
        Type = type;
    }
}
