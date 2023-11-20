package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterTm {
    String PayId;
    String StuId;
    String StuName;
    String StuClass;
    LocalDate date;
    double Amount;

}
