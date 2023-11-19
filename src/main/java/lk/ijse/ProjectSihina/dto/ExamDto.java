package lk.ijse.ProjectSihina.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExamDto {
    String ExamId;
    String ClassName;
    String Subject;
    String Description;
    LocalDate date;
    LocalTime StartTime;
    LocalTime EndTime;

}
