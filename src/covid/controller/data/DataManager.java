package covid.controller.data;

import covid.controller.api.APIReader;
import covid.controller.files.CacheManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;

<<<<<<< Updated upstream
=======
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
>>>>>>> Stashed changes
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class DataManager {
    private static DataManager dataManager;
    
    public static HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map;

    private DataManager(){

    }
    
    public static DataManager getDataManager() {
        if(dataManager == null) dataManager = new DataManager();
        return dataManager;
    }

<<<<<<< Updated upstream
    public List<Medicao> getMedicaoList(StatusCaso status, LocalDateTime startDate, LocalDateTime endDate){
        CacheManager cacheManager = new CacheManager();
        LocalDateTime missingStartDate = null;
        List<Medicao> list = new ArrayList<>();
        for(LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            Medicao medicao = cacheManager.readFile(status, date);
            if(medicao == null){
                if(missingStartDate == null){
                    missingStartDate = date;
                }
            }else{
                if(missingStartDate != null){
                    List<Medicao> missingMedicao = APIReader.getAllCountryCasesByPeriod(status, missingStartDate, date.minusDays(1));
                    list.addAll(missingMedicao);
                    missingStartDate = null;
                }
                list.add(medicao);
            }
        }
        return list;
    }
=======
//    public List<Medicao> getMedicaoList(StatusCaso status, LocalDateTime startDate, LocalDateTime endDate){
//        CacheManager cacheManager = new CacheManager();
//        LocalDateTime missingStartDate = null;
//        List<Medicao> list = new ArrayList<>();
//        for(LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
//            Medicao medicao = cacheManager.readFile(status, date);
//            if(medicao == null){
//                if(missingStartDate == null){
//                    missingStartDate = date;
//                }
//            }else{
//                if(missingStartDate != null){
//                    List<Medicao> missingMedicao = APIReader.getAllCountryCasesByPeriod(status, missingStartDate, date.minusDays(1));
//                    list.addAll(missingMedicao);
//                    missingStartDate = null;
//                }
//                list.add(medicao);
//            }
//        }
//        return list;
//    }
    
    
    
    //remover depois, não tem mais sentido manter 
    public DataManager.EstatisticaData getMedicaoList(StatusCaso status, LocalDateTime startDate, LocalDateTime endDate){
		return new EstatisticaData(map.get(status).get(startDate), map.get(status).get(endDate));
    }
    
    public class EstatisticaData {
    	public HashMap<String, Medicao> mapInicialHashMap;
    	public HashMap<String, Medicao> mapFinalHashMap;
    	public EstatisticaData(HashMap<String, Medicao> mapInicialHashMap, HashMap<String, Medicao> mapFinalHashMap) {
    		this.mapInicialHashMap = mapInicialHashMap;
    		this.mapFinalHashMap = mapFinalHashMap;
    	}
    }
    

    
>>>>>>> Stashed changes
}
