package lk.ijse.ProjectSihina.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDto {
    String PayID;
    String StuID;
    String StuName;
    String type;
    String StuClass;
    String PayMonth;
    String Subject;
    double Amount;
    LocalDate date;
    LocalTime time;

    public PaymentDto(String payID, String subject, Double amount) {
        this.PayID = payID;
        this.Subject = subject;
        this.Amount = amount;
    }

    public PaymentDto(String payID, String stuName, String stuClass, String subject, String payMonth, double amount) {
        PayID = payID;
        StuName = stuName;
        StuClass = stuClass;
        Subject = subject;
        PayMonth = payMonth;
        Amount = amount;
    }

    public PaymentDto(String payID, String stuID, String stuName, String stuClass, LocalDate date, double amount) {
        PayID = payID;
        StuID = stuID;
        StuName = stuName;
        StuClass = stuClass;
        Amount = amount;
        this.date = date;
    }
}
