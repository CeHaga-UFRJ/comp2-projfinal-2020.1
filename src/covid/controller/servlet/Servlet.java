package covid.controller.servlet;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import covid.controller.files.CacheManager;


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
		CacheManager cm = new CacheManager();
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    	Instant dataInicialInstant = Instant.from(formatter.parse(startDate));
        Instant dataFinalInstant = Instant.from(formatter.parse(endDate));
        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
        
        //if(rankType.equals("CONFIRMADOS") )
		

		//cm.readFile(rankType, dataInicial);
	}

}
