package covid.controller.rank;

import covid.comparators.CasosMaisRecentesComparator;
import covid.models.Medicao;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public abstract class Estatistica {
    private String nome;
    private List<Medicao> observacoes;
    private LocalDate dataPrimeira;
    private LocalDate dataUltima;

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

    public abstract float valor();

    public LocalDate dataInicio(){
        return dataPrimeira;
    }

    public LocalDate dataFim(){
        return dataUltima;
    }

    public String getNome(){
        return nome;
    }

    public List<Medicao> getObservacoes() {
        List<Medicao> list = new ArrayList<>(observacoes);
        list.sort(new CasosMaisRecentesComparator().reversed());
        return list;
    }
}
