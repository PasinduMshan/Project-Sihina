package lk.ijse.ProjectSihina.dto;

import javafx.scene.image.Image;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherDto {
    String Id;
    String Name;
    String Address;
    String Email;
    String Subjects;
    String ContactNo;
    Image imageTeacher;
}
