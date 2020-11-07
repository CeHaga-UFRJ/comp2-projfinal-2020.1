package covid.controller.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import covid.enums.ExportType;
import covid.enums.RankType;
import covid.models.ParOrdenado;

/**
 * Classe destinada a exportar os rankings consultados pelo usuario para arquivos .tsv ou .csv 
 * 
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 *
 */

public class RankingExport {

	/**
	 * Funcao que recebe a lista dos rankings e a transcreve no arquivo .tsv ou .csv, baseado na escolha do usuario
	 * criando um arquivo do tipo  rankType_dataInicial_to_dataFinal.exportType
	 * exemplo : MAIOR_NUMERO_CONFIRMADOS_2020-03-01_to_2020-04-02.tsv
	 * 
	 * @param list
	 * @param exportType
	 * @param rankType
	 */
	
    public static void export(List<ParOrdenado<String, Float>> list, ExportType exportType, RankType rankType, String startDate, String endDate) {
    	
    	String linha = "";
        String top = "pais,quantidade_casos\n";
        StringBuilder sb = new StringBuilder(top);
        String tipoArquivo = "";
        
        switch(exportType) {
        case TSV:
        	tipoArquivo = ".tsv";
        	break;
        case CSV:
        	tipoArquivo = ".csv";
        case NONE:
        	tipoArquivo = "none";
		default:
			break;
        }
        
        String fileName = "";
        if(tipoArquivo.equals(".tsv")) {
        	for(ParOrdenado<String,Float> medicao : list) {
            	linha = medicao.getPais() + "\t" + Float.toString(medicao.getCases()) + "\n";
            	sb.append(linha);
        	}
        	
        	fileName = rankType.toString() + "_" + startDate + "_to_" + endDate + tipoArquivo;
        }
        	
        if(tipoArquivo.equals(".csv")) {
            for(ParOrdenado<String,Float> medicao : list) {
            	linha = medicao.getPais() + "," + Float.toString(medicao.getCases()) + "\n";
            	sb.append(linha);       	
            }
            
            fileName = rankType.toString() + "_" + startDate + "_to_" + endDate + tipoArquivo;
        }
        
        File file = new File("./src/ExportedRankingFiles/" + fileName);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(sb.toString());
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Nao foi possivel gerar o arquivo.");
        }
    }
}
