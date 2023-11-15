package lk.ijse.ProjectSihina.dto;

import javafx.scene.image.Image;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDto {
    String ID;
    String BarcodeID;
    String Name;
    String Address;
    String Gender;
    String Email;
    LocalDate dob;
    String contact;
    String Stu_Class;
    String Subject;
    Image StudentImage;

    public StudentDto(String ID, String barcodeID, String name, String stu_Class, String email,String contact) {
        this.ID = ID;
        BarcodeID = barcodeID;
        Name = name;
        Email = email;
        Stu_Class = stu_Class;
        this.contact = contact;
    }

}
