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
import covid.controller.api.MedicaoLists;
import covid.controller.data.DataManager;
import covid.controller.files.CacheManager;
import covid.enums.RankType;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.Pais;
import covid.util.Tools;

/**
 * Classe para inicialização e configuração da API localmente 
 * @author 
 *
 */

public class ProgramLauncher {
    public static void main(String[] args) {
    	initializeServer();
    	//loadData();
    	menuInicial();
    }
    /**
     * Mostra um menu para facilitar as escolhas do usuário
     */
    private static void menuInicial(){
        System.out.println("API reconfigurada para esse computador. Deseja pegar dados mais recentes? (Pode demorar cerca de 5 minutos.)");
        System.out.println("1. Sim.");
        System.out.println("2. Não.");


        int escolha = 0;
        escolha = Tools.getInt();
        while(escolha < 1 || escolha > 2){
            System.out.println("Escolha invalida. Digite um numero de 1 a 2.");
            escolha = Tools.getInt();
        }

        switch(escolha) {
            case 1:
            	fetchData();
                break;
            case 2:
            	return;
        }
    }
    
    /**
     * Converte uma lista de medições em um Hashmap equivalente, com chaves de data e slug do país
     * @param lista de medições
     * @return hashmap equivalente à lista
     */
    
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
    
    /**
     * Faz novas requisições de dados ao servidor
     */
    
    public static void fetchData() {
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    	Instant dataInicialInstant = Instant.from(formatter.parse("2020-01-22T00:00:00Z"));
        Instant dataFinalInstant = Instant.from(formatter.parse("3020-03-08T00:00:00Z"));
        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
        
        HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = new HashMap<>();
        
        MedicaoLists lists = APIReader.getAllCountryCasesByPeriod(dataInicial, dataFinal);
        
    	map.put(StatusCaso.CONFIRMADOS, medicaoListToHashmap(lists.confirmedList));

    	map.put(StatusCaso.MORTOS, medicaoListToHashmap(lists.deathsList));

    	map.put(StatusCaso.RECUPERADOS, medicaoListToHashmap(lists.recoveredList));

    	CacheManager cm = new CacheManager();
    	System.out.println("Serializing Data");
    	cm.serializeData(map);
    	cm.serializeCountries(DataManager.getDataManager().getMapCountries());
    	
    }
    
    /**
     * Carrega os dados armazenados em cache para teste local
     */
    
    public static void loadData() {
    	CacheManager cm = new CacheManager();
    	HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map = cm.deserializeData();
    	HashMap<String, Pais> mapCountries = cm.deserializeCountries(); 
    	DataManager dm = DataManager.getDataManager();
    	if(dm.getDataMap() == null)
			dm.setDataMap(map);
    	dm.setMapCountries(mapCountries);
    }
    /**
     * Configura o servidor, salvando o caminho/path do projeto do computador do usuário
     */
    
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
       
}
