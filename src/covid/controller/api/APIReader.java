package covid.controller.api;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.json.simple.*;
import org.json.simple.parser.*;

import covid.models.Medicao;
import covid.models.Pais;
import covid.enums.StatusCaso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static List<Medicao> getAllCountryCasesByPeriod(StatusCaso status, LocalDateTime dataInicial, LocalDateTime dataFinal){

        int qtdCasos = 0;

        String stringCaso = "";
        String strDataInicial;
        String strDataFinal;

        LocalDateTime dataMomento;
        
        ArrayList<Medicao> listMedicao = new ArrayList<Medicao>();

        if(status == StatusCaso.CONFIRMADOS) stringCaso = "confirmed";
        if(status == StatusCaso.RECUPERADOS) stringCaso = "recovered";
        if(status == StatusCaso.MORTOS) stringCaso = "deaths";
        
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
        	
        	if(paisName.charAt(0) != 'a') { 
        		System.out.println("Skippando.");
        		continue;
        	}

            // String personalizada que varia de acordo com qual pais se trata, qual eh a data final e inicial do periodo em questao
            // e que tipo de informacao queremos, casos confirmados, casos de pessoas recuperadas e casos de pessoas que morreram.
            String APIurl = "https://api.covid19api.com/country/" + paisName +
                            "/status/"+ stringCaso + "?from=" + strDataInicial +
                            "&to="+ strDataFinal;

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
                int tries = 0;
                while(statusCode != 200) {
                	resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());
                	statusCode = resposta.statusCode(); 
                	tries++;
                }
                try {
                    JSONArray respostaJson = (JSONArray) new JSONParser().parse(resposta.body());
                    if(!respostaJson.isEmpty()) {
                    	System.out.println("Resposta recebida(" + tries + " tentativas): " + ((JSONObject) respostaJson.get(0)).get("Country").toString());
                    }
                    else {
                    	System.out.println("Resposta vazia.");
                    	continue;
                    }
                    
                                        
                    // for each que percorre todas datas do periodo em questao 
                    for (Object x : respostaJson) {
                        // pega todas informacoes acerca do pais em questao
                        paisNome = ((JSONObject) x).get("Country").toString();
                        paisSlug = ((JSONObject) x).get("Country").toString().toLowerCase();
                        paisCodigo = ((JSONObject) x).get("CountryCode").toString();
                        paisLatitude = Float.parseFloat(((JSONObject) x).get("Lat").toString());
                        paisLongitude = Float.parseFloat(((JSONObject) x).get("Lon").toString());

                        Pais pais = new Pais(paisNome, paisCodigo, paisSlug, paisLatitude, paisLongitude);

                        Instant dataMomentoInstant = Instant.from(formatter.parse(((JSONObject) x).get("Date").toString()));
                        dataMomento = LocalDateTime.ofInstant(dataMomentoInstant, ZoneOffset.UTC);
                        
                        qtdCasos = Integer.parseInt(((JSONObject) x).get("Cases").toString());

                        Medicao medicao = new Medicao(pais, dataMomento, qtdCasos, status);

                        listMedicao.add(medicao);
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
        return listMedicao;
    }

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