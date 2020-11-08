package covid.controller.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import covid.controller.data.DataManager;
import covid.enums.ExportType;
import covid.enums.RankType;
import covid.models.ParOrdenado;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setHeader("Access-Control-Allow-Origin", "*");	
    	
		String rankTypeString = request.getParameter("rankType");
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		String exportTypeString = request.getParameter("exportType");
		
		if(exportTypeString == null) exportTypeString = "none";
		if(rankTypeString == null || startDateString == null || endDateString == null) {
			response.getWriter().println("[]");
			return;
		}

		LocalDateTime startDate = LocalDate.parse(startDateString).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(endDateString).atStartOfDay();
		RankType rankType = RankType.stringToRankType(rankTypeString);
		ExportType exportType = ExportType.stringToExportType(exportTypeString);

		List<ParOrdenado<String, Float>> rankingList = DataManager.getDataManager().calculateRanking(rankType, exportType, startDate, endDate);
		JSONArray jsonArray = DataManager.getDataManager().toJson(rankingList);
		response.getWriter().println(jsonArray.toJSONString());
	}
    

}
