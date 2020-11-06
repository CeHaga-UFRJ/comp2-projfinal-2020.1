package covid.controller.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
<<<<<<< Updated upstream
=======
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

>>>>>>> Stashed changes
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< Updated upstream
=======
import covid.controller.data.DataManager;
import covid.controller.data.DataManager.EstatisticaData;
import covid.controller.files.CacheManager;
import covid.enums.StatusCaso;
import covid.launcher.ProgramLauncher;
import covid.models.Medicao;
>>>>>>> Stashed changes

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String rankType = request.getParameter("rankType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		response.setHeader("Access-Control-Allow-Origin", "*");
<<<<<<< Updated upstream
		response.getWriter().println("brasil");		
		//test
=======

//		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
//    	Instant dataInicialInstant = Instant.from(formatter.parse("2020-03-01T00:00:00Z"));
//        Instant dataFinalInstant = Instant.from(formatter.parse("2020-04-01T00:00:00Z"));
//        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
//        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
//        
//        StatusCaso statusCaso = StatusCaso.CONFIRMADOS;
//      
//        
//		EstatisticaData data = DataManager.getDataManager().getMedicaoListInServlet(statusCaso, dataInicial, dataFinal);
//		
//		response.getWriter().println(data.mapInicialHashMap.get("austria").getCasos());

		// System.out.println(getServletContext().getRealPath("/"));
		
		HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = deserializeData();
		System.out.println("WE DID IT BOIS" + map.containsKey(StatusCaso.MORTOS));
		if(DataManager.map == null)
			DataManager.map = map;
		
		response.getWriter().println("Hello World");
		response.getWriter().println("brasil");
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
	    
>>>>>>> Stashed changes
	}

}
