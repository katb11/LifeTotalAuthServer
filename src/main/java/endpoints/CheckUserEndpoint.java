package endpoints;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* this class authenticates that the user is a
   valid member and allowed to log in
 */
@WebServlet("/checkuser")
public class CheckUserEndpoint extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String stuff = IOUtils.toString(request.getReader());
        JSONObject obj = new JSONObject(stuff);

        String email = obj.getString("email");
        boolean exists = UserDao.checkUserExists(email);

        response.getWriter().write(String.valueOf(exists));
    }
}