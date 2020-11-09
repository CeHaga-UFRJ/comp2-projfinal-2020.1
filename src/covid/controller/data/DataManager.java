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
 * Classe para gestão de dados
 * 
 * @author Carlos Bravo, Matheus Oliveira Silva, Markson Arguello
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
    /**
     * Método usado para o singleton
     * @return Única instância da classe DataManager
     */
    public static DataManager getDataManager() {
        if(dataManager == null) dataManager = new DataManager();
        return dataManager;
    }
    
    /**
     * Faz o ranking de casos por períodos
     * 
     *<p>
     *Esse método é usado para todos os rankings que usam casos por período como: Total Casos, Total Mortos, Total Recuperados.
     *</p>
     *
     * @param status Tipo de ranking foi requisitado
     * @param dataInicio data de início solicitada
     * @param dataFim data final solicitada
     * @return lista de par ordenado com nome do país e número de casos
     */

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
    
    /**
     * Faz o ranking de casos por períodos
     * 
     *<p>
     *Esse método é usado para todos os rankings que usam crescimento de casos por período como: 
     *Crescimento Casos, Crescimento Mortos, Crescimento Recuperados.
     *</p>
     *
     * @param status Tipo de ranking foi requisitado
     * @param dataInicio data de início solicitada
     * @param dataFim data final solicitada
     * @return lista de par ordenado com nome do país e número de casos
     */
    
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
    
    /**
     * Faz o ranking de casos por períodos
     * 
     *<p>
     *Esse método é usado para o mesmo propósito do rankingGrowthCasesByPeriod porém no retorno da função ao invés do 
     *nome do país e usado o slug.
     *</p>
     *
     * @param status Tipo de ranking foi requisitado
     * @param dataInicio data de início solicitada
     * @param dataFim data final solicitada
     * @return lista de par ordenado com nome do país e número de casos
     */
    
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
    
    /**
     * Transforma uma lista de par ordenado em JSON
     * @param listRanking ranking 
     * @return ranking em JSON
     */
    
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
    
    /**
     * Método que escolhe qual método será chamado de acordo com o que foi pedido pelo usuário
     * @param rankType Tipo de ranking
     * @param exportType Tipo de arquivo que será exportado o rnaking
     * @param startDate Data inicial solicitada
     * @param endDate Data final solicitada
     * @param distance tamanho do raio do círculo caso o usuário queira as cidades mais perto da cidade com maior crescimento de casos
     * @return Lista de par ordenado que contém o ranking.
     */
    
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
    /**
     * Retorna o ranking com os países mais próximos do país com maior número de crescimento de casos no período selecionado
     * @param distance distancia do país com maior número de crescimento de casos no período selecionado
     * @param dataInicio Data inicial solicitada
     * @param dataFim Data final solicitada
     * @return ranking contendo nome do país e a distância
     */
    
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
    /**
     * Retorna a distância entre dois países
     * @param ref um país
     * @param pais outro país
     * @return distância entre os dois
     */
    
    private float nearby(Pais ref, Pais pais){
    	double d = Math.sqrt(Math.pow((pais.getLatitude() - ref.getLatitude())*111.12, 2) + Math.pow((pais.getLongitude() - ref.getLongitude())*111.12, 2));
    	//System.out.println("Distancia entre " + ref.getNome() + " (" + ref.getLatitude() + ", " + ref.getLongitude() + ") " +" e " + pais.getNome() + " (" + pais.getLatitude() + ", " + pais.getLongitude() + ") "+ " = "  + d);
    	return (float) d;
    }
    
    /**
     * Faz o ranking de mortalidade por período
     * 
     * @param dataInicio Data inicial solicitada
     * @param dataFim Data final solicitada
     * @return Lista de par ordenado contendo o ranking
     */
    
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
    
    /**
     * 
     * @return mapa contendo Tipo de caso, data, nome do pais e medicao.
     */

    public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> getDataMap() {
    	try {
    		return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) mapData.clone();
    	}
    	catch (NullPointerException e) {
			return null;
		}
	}
    /**
     * 
     * @param map mapa contendo tipo de caso que aponta para um mapa que contém a data que aponta para um país que contém uma medição
     */

	public void setDataMap(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
		try {
			this.mapData = (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) map.clone();
    	}
    	catch (NullPointerException e) {
			
		}
	}
	/**
	 *  
	 * @return caminho do projeto
	 */
	public String getProjectPath() {
		return projectPath;
	}
	/**
	 * 
	 * @param path caminho do projeto
	 */
	public void setProjectPath(String path) {
		this.projectPath = path;
	}
	
	/**
     * 
     * @return mapa de paises
     */
	public HashMap<String, Pais> getMapCountries() {
		return mapCountries;
	}
	/**
	 * 
	 * @param mapCountries mapa com nome do pais e objeto da classe Pais
	 */
	public void setMapCountries(HashMap<String, Pais> mapCountries) {
		this.mapCountries = mapCountries;
	}
    
}

