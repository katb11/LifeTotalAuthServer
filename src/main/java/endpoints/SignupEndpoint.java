package endpoints;

import model.User;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import service.CryptoUtil;
import service.JWT;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/* this class creates a new user
 */
@WebServlet("/signup")
public class SignupEndpoint extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get email/password from encrypted body
        String requestData = IOUtils.toString(request.getReader());

        JSONObject obj = new JSONObject(requestData);

        String email = obj.getString("email");
        String password = obj.getString("password");

        if (email == null || password == null) {
            response.getWriter().write("false");
        } else {
            String hashedPassword = CryptoUtil.getHash(password);

            try {

                if (UserDao.checkUserExists(email)) {
                    response.getWriter().write("false");
                } else {
                    UserDao.createUser(email, hashedPassword);
                    User newUser = UserDao.fetchUser(email, hashedPassword);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    if (newUser != null) {
                        HashMap<String, Object> claims = new HashMap<>();
                        claims.put("username", newUser.getEmail());
                        claims.put("accountID", newUser.getId());

                        // about 8 hours until expiration
                        response.getWriter().write(JWT.createJWT("Auth API", claims, 15000));
                    } else {
                        response.getWriter().write("false");
                    }
                }
            } catch (Exception e) {
                response.getWriter().write("unexpected server error");
            }
        }
    }
}