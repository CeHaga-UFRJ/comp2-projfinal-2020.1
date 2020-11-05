package covid.controller.data;

import covid.comparators.ParOrdenadoComparator;
import covid.controller.files.CacheManager;
import covid.controller.rank.TotalCasos;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.ParOrdenado;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class DataManager {
    private static DataManager dataManager;
    
    private DataManager(){

    }
    
    public static DataManager getDataManager() {
        if(dataManager == null) dataManager = new DataManager();
        return dataManager;
    }

    public List<ParOrdenado<String, Float>> rankingCasesByPeriod(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
    	
    	EstatisticaData estatistica = getMedicaoList(status, dataInicio, dataFim);
    	
    	for(Medicao medicoes : estatistica.mapFinalHashMap.values()) {
    		TotalCasos totalCasos = new TotalCasos();
    		totalCasos.inclui(estatistica.mapInicialHashMap.get(medicoes.getPais().getSlug()));
    		totalCasos.inclui(medicoes);
    		
    		ParOrdenado<String, Float> par = new ParOrdenado<>(medicoes.getPais().getSlug(),totalCasos.valor());
    		
    		listRanking.add(par);   		
    	} 
    	
    	ParOrdenadoComparator<String, Float> comparator = new ParOrdenadoComparator<>();
    	
    	listRanking.sort(comparator);
    	
    	return listRanking;
    }
    
    public DataManager.EstatisticaData getMedicaoList(StatusCaso status, LocalDateTime startDate, LocalDateTime endDate){
    	
    	CacheManager cm = new CacheManager();
		
		HashMap<String, Medicao> mapInicialHashMap = cm.readFile(status, startDate);
		HashMap<String, Medicao> mapFinalHashMap = cm.readFile(status, endDate);
		
		return new EstatisticaData(mapInicialHashMap, mapFinalHashMap);
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

