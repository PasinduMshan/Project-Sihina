package lk.ijse.ProjectSihina.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassDto {
    String ClassID;
    String ClassName;
    String Stu_Count;

    public ClassDto(String classID, String className) {
        ClassID = classID;
        ClassName = className;
    }
}
