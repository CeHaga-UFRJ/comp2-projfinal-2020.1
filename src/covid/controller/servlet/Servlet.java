package covid.controller.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rankType = request.getParameter("rankType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().println("brasil");		
		//test
	}

}
