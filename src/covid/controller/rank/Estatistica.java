package covid.controller.rank;

import covid.comparators.CasosMaisRecentesComparator;
import covid.models.Medicao;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe que representa uma estatística que contém um nome,
 *  lista de medições, data da primeira medição, data a última medição
 *  
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public abstract class Estatistica {
    private String nome;
    private List<Medicao> observacoes;
    private LocalDate dataPrimeira;
    private LocalDate dataUltima;
    
    /**
     * Inclui medições na lista de observações
     * @param observacao medição a ser adicionada
     * @return booleano indicando se foi adicionado ou não
     */

    public boolean inclui(Medicao observacao) {
    	if(observacoes == null) {
    		observacoes = new ArrayList<>();
    	}
        boolean adicionado = observacoes.add(observacao);
        
        if(dataPrimeira == null || observacao.getMomento().isBefore(dataPrimeira)){
            dataPrimeira = observacao.getMomento();
        }
        if(dataUltima == null || observacao.getMomento().isAfter(dataUltima)){
            dataUltima = observacao.getMomento();
        }
        return adicionado;
    }
    
    /**
     * Retorna o valor da estatística com base nas datas das medições
     * @return valor da estatística solicitada
     */
    public abstract float valor();

    /**
     * 
     * @return data da primeira medição
     */
    public LocalDate dataInicio(){
        return dataPrimeira;
    }

    /**
     * 
     * @return data da última medição
     */
    public LocalDate dataFim(){
        return dataUltima;
    }

    /**
     * 
     * @return nome da estatística
     */
    public String getNome(){
        return nome;
    }

    /**
     * 
     * @return lista com medições
     */
    public List<Medicao> getObservacoes() {
        List<Medicao> list = new ArrayList<>(observacoes);
        list.sort(new CasosMaisRecentesComparator().reversed());
        return list;
    }
}
