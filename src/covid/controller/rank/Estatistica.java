package covid.controller.rank;

import covid.comparators.CasosMaisRecentesComparator;
import covid.models.Medicao;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe que representa uma estat�stica que cont�m um nome,
 *  lista de medi��es, data da primeira medi��o, data a �ltima medi��o
 *  
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public abstract class Estatistica {
    private String nome;
    private List<Medicao> observacoes;
    private LocalDate dataPrimeira;
    private LocalDate dataUltima;
    
    /**
     * Inclui medi��es na lista de observa��es
     * @param observacao medi��o a ser adicionada
     * @return booleano indicando se foi adicionado ou n�o
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
     * Retorna o valor da estat�stica com base nas datas das medi��es
     * @return valor da estat�stica solicitada
     */
    public abstract float valor();

    /**
     * 
     * @return data da primeira medi��o
     */
    public LocalDate dataInicio(){
        return dataPrimeira;
    }

    /**
     * 
     * @return data da �ltima medi��o
     */
    public LocalDate dataFim(){
        return dataUltima;
    }

    /**
     * 
     * @return nome da estat�stica
     */
    public String getNome(){
        return nome;
    }

    /**
     * 
     * @return lista com medi��es
     */
    public List<Medicao> getObservacoes() {
        List<Medicao> list = new ArrayList<>(observacoes);
        list.sort(new CasosMaisRecentesComparator().reversed());
        return list;
    }
}
