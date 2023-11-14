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
}
