package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DashBoardScheduleTm {
    LocalTime StartTime;
    LocalTime EndTime;
    String Stu_Class;
    String Subject;
    String Type;
}
