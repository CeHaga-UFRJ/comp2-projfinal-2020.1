package covid.controller.data;

import covid.comparators.ParOrdenadoComparator;
import covid.controller.files.CacheManager;
import covid.controller.files.RankingExport;
import covid.controller.rank.CrescimentoCasos;
import covid.controller.rank.TotalCasos;
import covid.enums.ExportType;
import covid.enums.RankType;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.ParOrdenado;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 * 
 */


public class DataManager {
    private static DataManager dataManager;
    
    private HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map;
    
    public String projectPath;
    
    private DataManager(){

    }
    
    public static DataManager getDataManager() {
        if(dataManager == null) dataManager = new DataManager();
        return dataManager;
    }

    public List<ParOrdenado<String, Float>> rankingCasesByPeriod(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
    	
    	//EstatisticaData estatistica = getMedicaoList(status, dataInicio, dataFim);
    	HashMap<String, Medicao> mapInicial = map.get(status).get(dataInicio.toLocalDate());
    	HashMap<String, Medicao> mapFinal = map.get(status).get(dataFim.toLocalDate());
    	
    	for(Medicao medicoes : mapFinal.values()) {
    		TotalCasos totalCasos = new TotalCasos();
    		totalCasos.inclui(mapInicial.get(medicoes.getPais().getSlug()));
    		totalCasos.inclui(medicoes);
    		ParOrdenado<String, Float> par = new ParOrdenado<>(medicoes.getPais().getNome(),totalCasos.valor());
    		
    		listRanking.add(par);   		
    	} 
    	
    	ParOrdenadoComparator<String, Float> comparator = new ParOrdenadoComparator<>();
    	
    	listRanking.sort(comparator);
    	
    	return listRanking;
    }
    
    public List<ParOrdenado<String, Float>> rankingGrowthCasesByPeriod(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
        
        HashMap<String, Medicao> mapInicial = map.get(status).get(dataInicio.toLocalDate());
        HashMap<String, Medicao> mapFinal = map.get(status).get(dataFim.toLocalDate());
        
        for(Medicao medicoes : mapFinal.values()) {
            CrescimentoCasos crescimentoCasos = new CrescimentoCasos();
            crescimentoCasos.inclui(mapInicial.get(medicoes.getPais().getSlug()));
            crescimentoCasos.inclui(medicoes);
            ParOrdenado<String, Float> par = new ParOrdenado<>(medicoes.getPais().getNome(),crescimentoCasos.valor());
            
            listRanking.add(par);           
        } 
        
        ParOrdenadoComparator<String, Float> comparator = new ParOrdenadoComparator<>();
        
        listRanking.sort(comparator);
        
        return listRanking;
    }
    
    public JSONArray toJson(List<ParOrdenado<String, Float>> listRanking) {
        JSONArray list = new JSONArray();
        for (ParOrdenado<String, Float> par : listRanking) {
        	JSONObject obj = new JSONObject();
        	String pais = par.getPais();
        	Float casos = par.getCases();
        	obj.put("Pais", pais);
        	obj.put("Casos", casos);
            list.add(obj);
        }
        return list;
    }
    
    public List<ParOrdenado<String, Float>> calculateRanking(RankType rankType, ExportType exportType, LocalDateTime startDate, LocalDateTime endDate) {
    	List<ParOrdenado<String, Float>> list = null;
    	switch(rankType) {
        case MAIOR_NUMERO_CONFIRMADOS:
            list = rankingCasesByPeriod(StatusCaso.CONFIRMADOS, startDate, endDate);
            break;
        case MAIOR_NUMERO_MORTOS:
            list = rankingCasesByPeriod(StatusCaso.MORTOS, startDate, endDate);
            break;
        case MAIOR_NUMERO_RECUPERADOS:
            list = rankingCasesByPeriod(StatusCaso.RECUPERADOS, startDate, endDate);
            break;
        case MAIOR_CRESCIMENTO_CONFIRMADOS:
            list = rankingGrowthCasesByPeriod(StatusCaso.CONFIRMADOS, startDate, endDate);
            break;
        case MAIOR_CRESCIMENTO_MORTOS:
            list = rankingGrowthCasesByPeriod(StatusCaso.MORTOS, startDate, endDate);
            break;
        case MAIOR_CRESCIMENTO_RECUPERADOS:
            list = rankingGrowthCasesByPeriod(StatusCaso.RECUPERADOS, startDate, endDate);
            break;
        case MAIOR_TAXA_MORTALIDADE:
            break;
        default:
            list = null;
            break;
    }

    	String strDataInicial = startDate.toLocalDate().toString();
    	String strDataFinal = endDate.toLocalDate().toString();
    	
        RankingExport.export(list, exportType, rankType, strDataInicial, strDataFinal);
    	
    	return list;
    }
       

    public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> getMap() {
    	try {
    		return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) map.clone();
    	}
    	catch (NullPointerException e) {
			return null;
		}
	}

	public void setMap(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
		try {
			this.map = (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) map.clone();
    	}
    	catch (NullPointerException e) {
			
		}
	}
	public String getProjectPath() {
		return projectPath;
	}
	
	public void setProjectPath(String path) {
		this.projectPath = path;
	}
	
	  //remover depois, não tem mais sentido manter 
    public DataManager.EstatisticaData getMedicaoList(StatusCaso status, LocalDateTime startDate, LocalDateTime endDate){
		return new EstatisticaData(getMap().get(status).get(startDate), getMap().get(status).get(endDate));
    }
	public class EstatisticaData {
    	public HashMap<String, Medicao> mapInicialHashMap;
    	public HashMap<String, Medicao> mapFinalHashMap;
    	public EstatisticaData(HashMap<String, Medicao> mapInicialHashMap, HashMap<String, Medicao> mapFinalHashMap) {
    		this.mapInicialHashMap = mapInicialHashMap;
    		this.mapFinalHashMap = mapFinalHashMap;
    	}
    }    
}

