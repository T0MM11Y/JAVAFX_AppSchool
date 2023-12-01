package school_app;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class Connect {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root",
                    "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
  
}
