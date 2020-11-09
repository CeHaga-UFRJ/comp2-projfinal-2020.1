package covid.controller.data;

import covid.comparators.ParOrdenadoComparator;
import covid.controller.files.CacheManager;
import covid.controller.files.RankingExport;
import covid.controller.rank.CrescimentoCasos;
import covid.controller.rank.Mortalidade;
import covid.controller.rank.TotalCasos;
import covid.enums.ExportType;
import covid.enums.RankType;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.Pais;
import covid.models.ParOrdenado;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    
    private HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> mapData;
    private HashMap<String, Pais> mapCountries;
    
    
    public String projectPath;
    
    private DataManager(){
    	mapData = new HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>();
    	setMapCountries(new HashMap<String, Pais>());
    }
    
    public static DataManager getDataManager() {
        if(dataManager == null) dataManager = new DataManager();
        return dataManager;
    }

    private List<ParOrdenado<String, Float>> rankingCasesByPeriod(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
    	
    	//EstatisticaData estatistica = getMedicaoList(status, dataInicio, dataFim);
    	HashMap<String, Medicao> mapInicial = mapData.get(status).get(dataInicio.toLocalDate());
    	HashMap<String, Medicao> mapFinal = mapData.get(status).get(dataFim.toLocalDate());
    	
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
    
    private List<ParOrdenado<String, Float>> rankingGrowthCasesByPeriod(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
        
        HashMap<String, Medicao> mapInicial = mapData.get(status).get(dataInicio.toLocalDate());
        HashMap<String, Medicao> mapFinal = mapData.get(status).get(dataFim.toLocalDate());
        
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
    
    private List<ParOrdenado<String, Float>> rankingGrowthCasesByPeriodSlug(StatusCaso status, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
        
        HashMap<String, Medicao> mapInicial = mapData.get(status).get(dataInicio.toLocalDate());
        HashMap<String, Medicao> mapFinal = mapData.get(status).get(dataFim.toLocalDate());
        
        for(Medicao medicoes : mapFinal.values()) {
            CrescimentoCasos crescimentoCasos = new CrescimentoCasos();
            crescimentoCasos.inclui(mapInicial.get(medicoes.getPais().getSlug()));
            crescimentoCasos.inclui(medicoes);
            ParOrdenado<String, Float> par = new ParOrdenado<>(medicoes.getPais().getSlug(),crescimentoCasos.valor());
            
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
    
    public List<ParOrdenado<String, Float>> calculateRanking(RankType rankType, ExportType exportType, LocalDateTime startDate, LocalDateTime endDate, float distance) {
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
        	list = rankingDeathRateByPeriod(startDate, endDate);
            break;
        case MAIOR_PROXIMIDADE_DO_EPICENTRO:
        	list = rankingProximityGrowthRateByPeriod(distance, startDate, endDate);
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
    
    private List<ParOrdenado<String, Float>> rankingProximityGrowthRateByPeriod(float distance, LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
        
    	String paisEpicentroString = rankingGrowthCasesByPeriodSlug(StatusCaso.CONFIRMADOS,  dataInicio, dataFim).get(0).getPais();
    	Pais paisEpicentro = getMapCountries().get(paisEpicentroString);
    	
    	for (Pais pais : getMapCountries().values()) {
    		if(pais.getSlug().equals(paisEpicentro.getSlug())) continue;
    		float distanciaEntrePaises = nearby(pais, paisEpicentro);
			if(distanciaEntrePaises <= distance ) {
				listRanking.add(new ParOrdenado<String, Float>(pais.getNome(), distanciaEntrePaises));
			}
		
		}
    	
        ParOrdenadoComparator<String, Float> comparator = new ParOrdenadoComparator<>();
        
        listRanking.sort(comparator);
        Collections.reverse(listRanking);
        
        return listRanking;
    }
    
    private float nearby(Pais ref, Pais pais){
    	double d = Math.sqrt(Math.pow((pais.getLatitude() - ref.getLatitude())*111.12, 2) + Math.pow((pais.getLongitude() - ref.getLongitude())*111.12, 2));
    	//System.out.println("Distancia entre " + ref.getNome() + " (" + ref.getLatitude() + ", " + ref.getLongitude() + ") " +" e " + pais.getNome() + " (" + pais.getLatitude() + ", " + pais.getLongitude() + ") "+ " = "  + d);
    	return (float) d;
    }
    
    
    private List<ParOrdenado<String, Float>> rankingDeathRateByPeriod(LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<ParOrdenado<String, Float>> listRanking = new ArrayList<>();
        
        HashMap<String, Medicao> mapInicialCasos = mapData.get(StatusCaso.CONFIRMADOS).get(dataInicio.toLocalDate());
        HashMap<String, Medicao> mapFinalCasos = mapData.get(StatusCaso.CONFIRMADOS).get(dataFim.toLocalDate());
        HashMap<String, Medicao> mapInicialMortos = mapData.get(StatusCaso.MORTOS).get(dataInicio.toLocalDate());
        HashMap<String, Medicao> mapFinalMortos = mapData.get(StatusCaso.MORTOS).get(dataFim.toLocalDate());
        
        
        
        for(Medicao medicoes : mapFinalCasos.values()) {
            Mortalidade mortalidade = new Mortalidade();
            mortalidade.inclui(mapInicialCasos.get(medicoes.getPais().getSlug()), mapInicialMortos.get(medicoes.getPais().getSlug()));
            mortalidade.inclui(medicoes, mapFinalMortos.get(medicoes.getPais().getSlug()));
            ParOrdenado<String, Float> par = new ParOrdenado<>(medicoes.getPais().getNome(), mortalidade.valor());
            
            listRanking.add(par);           
        } 
        
        ParOrdenadoComparator<String, Float> comparator = new ParOrdenadoComparator<>();
        
        listRanking.sort(comparator);
        
        return listRanking;
    }
    
       

    public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> getDataMap() {
    	try {
    		return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) mapData.clone();
    	}
    	catch (NullPointerException e) {
			return null;
		}
	}

	public void setDataMap(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
		try {
			this.mapData = (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) map.clone();
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
		return new EstatisticaData(getDataMap().get(status).get(startDate), getDataMap().get(status).get(endDate));
    }
	public HashMap<String, Pais> getMapCountries() {
		return mapCountries;
	}

	public void setMapCountries(HashMap<String, Pais> mapCountries) {
		this.mapCountries = mapCountries;
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

