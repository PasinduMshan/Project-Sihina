package lk.ijse.ProjectSihina.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SIHINA",
                "root",
                "Ijse@1234"
        );
    }
    private static DbConnection getInstance() throws SQLException {
        return (null == dbConnection) ? dbConnection = new DbConnection() :dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
