package covid.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;


import covid.controller.api.APIReader;
import covid.controller.data.DataManager;
import covid.controller.data.DataManager.EstatisticaData;
import covid.controller.files.CacheManager;
import covid.enums.RankType;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.Pais;

public class ProgramLauncher {
    public static void main(String[] args) {
    	
    	
    	System.out.println("Hello World");
    	//fetchData();
    	loadData();
    	initializeServer();
    	
//    	LocalDateTime startDate = LocalDateTime.of(2020, 3, 1, 0, 0);
//    	LocalDateTime endDate = LocalDateTime.of(2020, 11, 1, 0, 0);
//    	
//    	System.out.println(DataManager.getDataManager().getMap().get(StatusCaso.CONFIRMADOS).containsKey(startDate.toLocalDate()));
//
//    	
//    	System.out.println(DataManager.getDataManager().calculateRanking(RankType.MAIOR_NUMERO_CONFIRMADOS, startDate, endDate).toJSONString());
//    	
    	
    	//fetchData();
    }
    
    
    public static HashMap<LocalDate, HashMap<String, Medicao>> medicaoListToHashmap(List<Medicao> list) {
    	HashMap<LocalDate, HashMap<String, Medicao>> dateMap = new HashMap<>();
    	
    	for (Medicao medicao : list) {
			LocalDate date = medicao.getMomento();
			String slug = medicao.getPais().getSlug();
			
			if(!dateMap.containsKey(date)) {
				HashMap<String, Medicao> map = new HashMap<>();
				dateMap.put(date, map);
			}
			dateMap.get(date).put(slug, medicao);
		}
    	
    	return dateMap;
    }
    
    public static void fetchData() {
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    	Instant dataInicialInstant = Instant.from(formatter.parse("2001-01-01T00:00:00Z"));
        Instant dataFinalInstant = Instant.from(formatter.parse("3001-03-08T00:00:00Z"));
        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
        
        HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = new HashMap<>();
        
        System.out.println("Fetching CONFIRMADOS");
    	List<Medicao> listConfirmados = APIReader.getAllCountryCasesByPeriod(StatusCaso.CONFIRMADOS, dataInicial, dataFinal);
    	map.put(StatusCaso.CONFIRMADOS, medicaoListToHashmap(listConfirmados));
    	System.out.println("Fetching MORTOS");
    	List<Medicao> listMortos = APIReader.getAllCountryCasesByPeriod(StatusCaso.MORTOS, dataInicial, dataFinal);
    	map.put(StatusCaso.MORTOS, medicaoListToHashmap(listMortos));
    	System.out.println("Fetching RECUPERADOS");
    	List<Medicao> listRecuperados = APIReader.getAllCountryCasesByPeriod(StatusCaso.RECUPERADOS, dataInicial, dataFinal);
    	map.put(StatusCaso.RECUPERADOS, medicaoListToHashmap(listRecuperados));

    	CacheManager cm = new CacheManager();
    	System.out.println("Serializing Data");
    	cm.serializeData(map);
    }
    
    public static void loadData() {
    	CacheManager cm = new CacheManager();
    	HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = cm.deserializeData();
    	DataManager dm = DataManager.getDataManager();
    	if(dm.getMap() == null)
			dm.setMap(map);
    }
    
    public static void initializeServer() {
    	String projectPath = new File("").getAbsolutePath();
    	System.out.println(projectPath);
    	
    	File file = new File(projectPath + "/WebContent/WEB-INF/DATA/PROJECT_PATH.txt");

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(projectPath);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Nao foi possivel gerar o arquivo.");
        }
    	
    }
    
    
    
    public static void teste() {
    	
    	System.out.println("Testando");
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    	Instant dataInicialInstant = Instant.from(formatter.parse("2020-03-01T00:00:00Z"));
        Instant dataFinalInstant = Instant.from(formatter.parse("2020-04-01T00:00:00Z"));
        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
        
        StatusCaso statusCaso = StatusCaso.CONFIRMADOS;


		EstatisticaData data = DataManager.getDataManager().getMedicaoList(statusCaso, dataInicial, dataFinal);
		
		System.out.println(data.mapInicialHashMap.get("austria").getCasos());
    }
    
    
    
    
}
