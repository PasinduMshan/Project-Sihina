package lk.ijse.ProjectSihina.User;

public class UserConnection {
        private static UserConnection userConnection;
        private String userName;
        private String Password;

        private UserConnection() {

        }

        public static UserConnection getInstance() {
            return (userConnection == null) ? userConnection = new UserConnection() : userConnection;
        }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
