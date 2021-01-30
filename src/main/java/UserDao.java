import model.User;
import service.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public static User fetchUser(String email, String password) throws SQLException, ClassNotFoundException {
        Connection connection = ConnectionFactory.getConnection();

        try {
            String query = "SELECT * FROM [dbo].[User] WHERE email= ? AND password= ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setId(rs.getInt("id"));
                connection.close();

                return user;
            }
        } catch(Exception e) {
            return null;
        } finally {
            connection.close();
        }
        return null;
    }
}