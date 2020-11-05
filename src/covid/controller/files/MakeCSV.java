package covid.controller.files;

import java.io.*;

/**
 * Classe destinada a todas as funções que vão criar arquivos .csv para usarmos nas análises estatísticas dos rankings.
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 */
public class MakeCSV {

    /**
     * Classe que cria um casesInPeriod.csv que nele vai conter o número de casos no periodo entre dataFinal-dataInicial
     * de cada país que temos no nosso banco de dados da API.
     * @param dataInicial
     * @param dataFinal
     */
    public static void makeCasesCSV(String dataInicial, String dataFinal) {
        // cabeçalho
        String top = "pais,qtd_casos_confirmados,qtd_recuperados,qtd_mortes";

        // cria uma lista com nome de todos os paises que temos informações quanto ao número de casos na API.
//        ArrayList<String> listaPaises = APIReader.listaPaises();

        StringBuilder sb = new StringBuilder(top);
        
//        String linha = "";
        // no for each a seguir usaremos a funcao
        /*for (String pais : listaPaises){
            linha = pais + "\t" + 
                    Integer.toString(APIReader.getCasosByPeriod(1, pais, dataInicial, dataFinal)) + "\t" + // casos Confirmados
                    Integer.toString(APIReader.getCasosByPeriod(2, pais, dataInicial, dataFinal)) + "\t" + // pessoas que se Recuperaram
                    Integer.toString(APIReader.getCasosByPeriod(3, pais, dataInicial, dataFinal)); // número de mortes
            sb.append(linha);
        }*/
        
        File file = new File("./src/dataFiles/casesInPeriod.csv");

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(sb.toString());
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Não foi possível gerar o arquivo.");
        }
    }

    /**
     * Classe que cria um casesGrowthRateInPeriod.csv que nele vai conter a taxa de crescimento 
     * de cada país que temos no nosso banco de dados da API.
     * @param dataInicial
     * @param dataFinal
     */
    public static void makeCasesGrowthRateCSV(String dataInicial, String dataFinal) {
        // cabeçalho
        String top = "pais,growthRate_casos_confirmados,growthRate_recuperados,growthRate_mortes";

        // cria uma lista com nome de todos os paises que temos informações quanto ao número de casos na API.
//        ArrayList<String> listaPaises = APIReader.listaPaises();

        StringBuilder sb = new StringBuilder(top);
        
//        String linha = "";
        // no for each a seguir usaremos a funcao
        /*for (String pais : listaPaises){
            linha = pais + "\t" + 
                    Integer.toString(APIReader.getGrowthRateByPeriod(1, pais, dataInicial, dataFinal)) + "\t" + // casos Confirmados
                    Integer.toString(APIReader.getGrowthRateByPeriod(2, pais, dataInicial, dataFinal)) + "\t" + // pessoas que se Recuperaram
                    Integer.toString(APIReader.getGrowthRateByPeriod(3, pais, dataInicial, dataFinal)); // número de mortes
            sb.append(linha);
        }*/
        
        File file = new File("./src/dataFiles/casesGrowthRateInPeriod.csv");

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(sb.toString());
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Não foi possível gerar o arquivo.");
        }
    }
    


}
