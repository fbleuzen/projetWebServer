package server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import server.User;
import server.UserController;

/**
 * Servlet implementation class ConnectServlet
 */
@WebServlet("/connect")
public class ConnectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ConnectServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Do nothing
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String payloadRequest = getBody(request);
        JSONObject obj = new JSONObject(payloadRequest);
		User user = new User(obj.getString("name"), obj.getString("interests"), obj.getString("latitude"), obj.getString("longitude"));
		UserController ctrl = UserController.getInstance();
		ctrl.addUser(user);
		response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Allow-Methods"));
        response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Allow-Headers"));
	}

    public static String getBody(HttpServletRequest request) throws IOException {

        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}
