package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleTm {
    String Stu_Class;
    String Subject;
    String Teacher;
    String Day;
    LocalTime StartTime;
    LocalTime EndTime;
}
