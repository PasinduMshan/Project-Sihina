package lk.ijse.ProjectSihina.dto.Tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentTm {
    String Pay_id;
    String Name;
    String Stu_Class;
    String Stu_Subject;
    String Pay_Month;
    Double Amount;

    public PaymentTm(String pay_id, String stu_Subject,  Double amount) {
        Pay_id = pay_id;
        Stu_Subject = stu_Subject;
        Amount = amount;
    }
}
