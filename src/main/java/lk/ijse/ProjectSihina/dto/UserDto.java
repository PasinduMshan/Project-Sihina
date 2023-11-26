package lk.ijse.ProjectSihina.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String firstName;
    private String lastName;
    private String Email;
    private String NIC;
    private String userName;
    private String password;

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserDto(String userId, String firstName, String lastName, String email, String NIC) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        Email = email;
        this.NIC = NIC;
    }
}
