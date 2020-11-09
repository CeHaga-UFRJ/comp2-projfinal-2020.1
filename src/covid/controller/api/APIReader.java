package covid.controller.api;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient.*;
import java.sql.Struct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.json.simple.*;
import org.json.simple.parser.*;

import covid.models.Medicao;
import covid.models.Pais;
import covid.controller.data.DataManager;
import covid.enums.StatusCaso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe destinada a ler a API e fazer as manipulacoes necessarias.
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 */
public class APIReader {

    /**
     * Metodo que le a API e conta quantos casos, seja de confirmados, de pessoas recuperadas ou de mortos no intervalo
     * dos dias dataInicial e dataFinal.
     * 
     * @param status 
     * @param dataInicial 
     * @param dataFinal
     * @return listMedicao
     */
    public static MedicaoLists getAllCountryCasesByPeriod(LocalDateTime dataInicial, LocalDateTime dataFinal){

        String strDataInicial;
        String strDataFinal;

        LocalDateTime dataMomento;
        
        MedicaoLists medicaoLists = new MedicaoLists();
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        strDataInicial = dataInicial.toString();
        strDataFinal = dataFinal.toString();

        ArrayList<String> listaDePaises = listaPaises();
        

        String paisNome = "";
        String paisCodigo = "";
        String paisSlug = "";
        Float paisLatitude = (float) 0;
        Float paisLongitude = (float) 0;

        for(String paisName: listaDePaises){
            // String personalizada que varia de acordo com qual pais se trata, qual eh a data final e inicial do periodo em questao
            // e que tipo de informacao queremos, casos confirmados, casos de pessoas recuperadas e casos de pessoas que morreram.
            String APIurl = "https://api.covid19api.com/country/" + paisName +
                            "?from=" + strDataInicial +
                            "&to="+ strDataFinal;
            if(paisName.equals("united-states")) {continue;}
//            		System.err.println("USA");
//            		LocalDateTime newDataFinal = LocalDateTime.parse(strDataInicial).plusDays(1);
//            		APIurl = "https://api.covid19api.com/country/" + paisName +
//                            "?from=" + strDataInicial +
//                            "&to="+ newDataFinal.toLocalDate().toString();
//            		System.out.println(APIurl);
//        	}

            
            // abre a comunicacao com a api
            HttpClient cliente = HttpClient.newBuilder()
                    .version(Version.HTTP_2)
                    .followRedirects(Redirect.ALWAYS)
                    .build();

            HttpRequest requisicao = HttpRequest.newBuilder()
                        .uri(URI.create(APIurl))
                        .build();


            try {
            	int statusCode = -1;
                HttpResponse<String> resposta = null;
                while(statusCode != 200) {
                	resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());
                	statusCode = resposta.statusCode(); 
                }
                try {
                    JSONArray respostaJson = (JSONArray) new JSONParser().parse(resposta.body());
                    if(!respostaJson.isEmpty()) {
                    	System.out.println("Resposta recebida: " + ((JSONObject) respostaJson.get(0)).get("Country").toString());
                    }
                    else {
                    	System.out.println("Resposta vazia.");
                    	continue;
                    }
                    
                    
                    if((((JSONObject)respostaJson.get(1)).get("Province").toString() != null ||
                    		!(((JSONObject)respostaJson.get(1)).get("Province").toString().isBlank()
                    		))){ continue;}
                    
                    JSONObject firstElement = ((JSONObject)respostaJson.get(0));
                    
                    paisNome = firstElement.get("Country").toString();
                    paisSlug = firstElement.get("Country").toString().toLowerCase();
                    paisCodigo = firstElement.get("CountryCode").toString();
                    paisLatitude = Float.parseFloat(firstElement.get("Lat").toString());
                    paisLongitude = Float.parseFloat(firstElement.get("Lon").toString());
                    Pais pais = new Pais(paisNome, paisCodigo, paisSlug, paisLatitude, paisLongitude);
                    DataManager.getDataManager().getMapCountries().put(paisSlug, pais);  
                    
                    	// for each que percorre todas datas do periodo em questao 
                        for (Object x : respostaJson) {
                            // pega todas informacoes acerca do pais em questao
                            
                            

                            Instant dataMomentoInstant = Instant.from(formatter.parse(((JSONObject) x).get("Date").toString()));
                            dataMomento = LocalDateTime.ofInstant(dataMomentoInstant, ZoneOffset.UTC);
                            
                            int qtdConfirmed = Integer.parseInt(((JSONObject) x).get("Confirmed").toString());
                            int qtdDeaths = Integer.parseInt(((JSONObject) x).get("Deaths").toString());
                            int qtdRecovered = Integer.parseInt(((JSONObject) x).get("Recovered").toString());

                            Medicao medicaoConfirmed = new Medicao(pais, dataMomento, qtdConfirmed, StatusCaso.CONFIRMADOS);
                            Medicao medicaoDeaths = new Medicao(pais, dataMomento, qtdConfirmed, StatusCaso.MORTOS);
                            Medicao medicaoRecovered = new Medicao(pais, dataMomento, qtdConfirmed, StatusCaso.RECUPERADOS);
                            
                            
                            medicaoLists.confirmedList.add(medicaoConfirmed);
                            medicaoLists.deathsList.add(medicaoDeaths);
                            medicaoLists.recoveredList.add(medicaoRecovered);
                        }
                    
                }catch (ParseException e) {
                    System.err.println("Resposta invalida");
                    e.printStackTrace();
                }
            }catch (IOException e){
                System.err.println("Problema com a conexao");
                e.printStackTrace();
            }catch (InterruptedException e) {
                System.err.println("Requisicao interrompida");
                e.printStackTrace();
            }
            
        }
        return medicaoLists;
    }
  
    /**
     * Pega o nome dos países presentes na API
     * @return lista com nome dos países
     */
    
    public static ArrayList<String> listaPaises(){
        String APIurl = "https://api.covid19api.com/countries";
        
        // abre a comunicacao com a api
        HttpClient cliente = HttpClient.newBuilder()
                            .version(Version.HTTP_2)
                            .followRedirects(Redirect.ALWAYS)
                            .build();

        HttpRequest requisicao = HttpRequest.newBuilder()
                                .uri(URI.create(APIurl))
                                .build();

        ArrayList<String> listaDePaises = new ArrayList<String>();

        try {
            HttpResponse<String> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());
            try {
                JSONArray respostaJson = (JSONArray) new JSONParser().parse(resposta.body());
                
                for (Object pais : respostaJson) listaDePaises.add((String)((JSONObject) pais).get("Slug").toString()); 
                
            }catch (ParseException e) {
                System.err.println("Resposta invalida");
                e.printStackTrace();
            }
        }catch (IOException e) {
            System.err.println("Problema com a conexao");
            e.printStackTrace();
        }catch (InterruptedException e) {
            System.err.println("Requisicao interrompida");
            e.printStackTrace();
        }

        return listaDePaises;
    }
}

