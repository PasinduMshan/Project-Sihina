package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExamTm {
    String ExamId;
    String Description;
    String className;
    String Subject;
    String Date;
}
