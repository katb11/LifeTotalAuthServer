
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

/* this class authenticates that the user is a
   valid member and allowed to log in
 */
@WebServlet("/authenticate")
public class AuthenticationEndpoint extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get email/password from encrypted body
        String body = IOUtils.toString(request.getReader());
        byte[] bodyDecoded = Base64.getDecoder().decode(body);
        byte[] decrypted = CryptoUtil.decrypt(bodyDecoded);
        if (decrypted != null) {
            String decryptedBody = new String(decrypted);
            JSONObject obj = new JSONObject(decryptedBody);

            String email = obj.getString("email");
            String password = obj.getString("password");
            String hashedPassword = CryptoUtil.getHash(password);

            User user = null;
            try {
                // traditional login
                if (email != null && hashedPassword != null) {
                    user = UserDao.fetchUser(email, hashedPassword);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                if (user != null) {
                    HashMap<String, Object> claims = new HashMap<>();
                    claims.put("username", user.getEmail());
                    claims.put("accountID", user.getId());

                    // about 8 hours until expiration
                    response.getWriter().write(JWT.createJWT("Auth API", claims, 15000));
                }
                else {
                    response.getWriter().write("access denied");
                }
            } catch (Exception e) {
                response.getWriter().write("unexpected server error");
             }
        } else {
            response.getWriter().write("access denied");
        }
    }
}