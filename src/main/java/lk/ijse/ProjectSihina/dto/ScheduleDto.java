package lk.ijse.ProjectSihina.dto;

import lombok.*;

import java.time.LocalTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleDto {
    String Stu_Class;
    String Subject;
    String Teacher;
    String Day;
    LocalTime StartTime;
    LocalTime EndTime;
}


