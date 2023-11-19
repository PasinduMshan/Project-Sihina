package lk.ijse.ProjectSihina.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectDto {
    String id;
    String subject;
    String AvailableClass;
    String teacherName;

    public SubjectDto(String subject) {
        this.subject = subject;
    }
}
