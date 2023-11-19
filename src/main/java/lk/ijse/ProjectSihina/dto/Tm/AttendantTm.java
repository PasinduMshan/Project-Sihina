package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendantTm {
    String AttendantId;
    String StuName;
    String StuClass;
    LocalDate date;
    String month;
    String AttendanceType;

}
