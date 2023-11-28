package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectTm {
    String Sub_Id;
    String Subject;
    String TeacherName;
    String AvailableClass;
    double MonthlyAmount;
}
