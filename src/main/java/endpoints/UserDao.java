package endpoints;

import model.User;
import service.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class UserDao {

    static User fetchUser(String email, String password) {

        try (Connection connection = ConnectionFactory.getConnection()) {
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
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    static User createUser(String email, String password) {

        try (Connection connection = ConnectionFactory.getConnection()) {
            String query = "INSERT INTO [dbo].[User] VALUES ( ? , ? )";
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
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    static boolean checkUserExists(String email) {

        try (Connection connection = ConnectionFactory.getConnection()) {
            String query = "SELECT * FROM [dbo].[User] WHERE email= ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            return true; // just in case, don't allow a new user if we aren't sure
        }
    }
}
