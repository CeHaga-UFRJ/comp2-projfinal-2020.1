package covid.controller.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import covid.controller.data.DataManager;
import covid.controller.files.CacheManager;
import covid.enums.RankType;
import covid.enums.StatusCaso;
import covid.launcher.ProgramLauncher;
import covid.models.Medicao;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rankTypeString = request.getParameter("rankType");
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		response.setHeader("Access-Control-Allow-Origin", "*");	
		if(rankTypeString == null || startDateString == null || endDateString == null) {
			response.getWriter().println("[]");
			return;
		}
		
		System.out.println(rankTypeString);
		System.out.println(startDateString);
		System.out.println(endDateString);
		
		DataManager dm = DataManager.getDataManager();
		
		LocalDateTime startDate = LocalDate.parse(startDateString).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(endDateString).atStartOfDay();
		RankType rankType = RankType.stringToRankType(rankTypeString);

		HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = deserializeData();
		if(dm.getMap() == null)
			dm.setMap(map);
		
		JSONArray jsonArray = DataManager.getDataManager().calculateRanking(rankType, startDate, endDate);
		response.getWriter().println(jsonArray.toJSONString());
	}
    
	
	public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> deserializeData() {
		InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/DATA/SERIALIZED_DATA.ser");
	    try (ObjectInputStream ois = new ObjectInputStream(inputStream)){
	    	return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) ois.readObject();
	    } 
	    catch (IOException | ClassNotFoundException e){
	       	System.out.println("Exception when reading obj");
	    	e.printStackTrace();
	        return null;
	    }
	}

}
