package covid.controller.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

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

/**
 * Classe para inicialização e configuração da API
 * 
 * @author André Gaeta
 *
 */

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Servlet() {
        super();
    }
    
	/**
	 * Função que é executada ao receber um request, interpretando os dados fornecidos e retornando com uma resposta baseada nos rankings do programa
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setHeader("Access-Control-Allow-Origin", "*");	
    	
		String rankTypeString = request.getParameter("rankType");
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		String exportTypeString = request.getParameter("exportType");
		String distanceString = request.getParameter("distance");
		
		if(distanceString == null || distanceString.isBlank()) distanceString = "";
		if(exportTypeString == null) exportTypeString = "none";
		if(rankTypeString == null || startDateString == null || endDateString == null) {
			response.getWriter().println("[]");
			return;
		}
		
		saveRequest(rankTypeString, startDateString, endDateString, exportTypeString, distanceString);
		
		if(distanceString.isBlank()) distanceString = "1";
		float distance = Float.parseFloat(distanceString);
		LocalDateTime startDate = LocalDate.parse(startDateString).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(endDateString).atStartOfDay();
		RankType rankType = RankType.stringToRankType(rankTypeString);
		ExportType exportType = ExportType.stringToExportType(exportTypeString);

		List<ParOrdenado<String, Float>> rankingList = DataManager.getDataManager().calculateRanking(rankType, exportType, startDate, endDate, distance);
		JSONArray jsonArray = DataManager.getDataManager().toJson(rankingList);
		response.getWriter().println(jsonArray.toJSONString());
	}
    
    /**
     * Escreve num arquivo um log de todas as requisições que são feitas
     * @param rankType tipo de ranking a ser calculado
     * @param startDate data de início
     * @param endDate data de fim
     * @param exportType tipo de exportação
     * @param distanceString distância máxima para cálculo do ranking MAIOR_PROXIMIDADE_DO_EPICENTRO
     */
    private void saveRequest(String rankType, String startDate, String endDate, String exportType, String distanceString) {
    	String projectPath = DataManager.getDataManager().getProjectPath();
    	
		String fileName = "REQUEST_LOG.txt";
    	
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(projectPath + "/WebContent/WEB-INF/DATA/" + fileName, true))){
            StringBuilder sb = new StringBuilder();
            sb.append(rankType + ",");
            sb.append(startDate + ",");
            sb.append(endDate + ",");
            sb.append(exportType);
            if(rankType.equals("MAIOR_PROXIMIDADE_DO_EPICENTRO"))
            	sb.append("," + distanceString);
            sb.append("\n");
            
            bw.append(sb.toString());
        }
        catch (IOException e){
            System.out.println("Não foi possível gerar o arquivo.");
        }
        
        
    }
    

}
