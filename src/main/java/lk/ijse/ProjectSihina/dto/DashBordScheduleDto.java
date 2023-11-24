package lk.ijse.ProjectSihina.dto;

import lombok.*;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DashBordScheduleDto {
    LocalTime StartTime;
    LocalTime EndTime;
    String Stu_Class;
    String Subject;
    String Type;
}
