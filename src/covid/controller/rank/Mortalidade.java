package covid.controller.rank;

import java.util.ArrayList;
import java.util.List;

import covid.comparators.CasosMaisRecentesComparator;
import covid.models.Medicao;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class Mortalidade extends Estatistica{
	
	private List<Medicao> observacoesMortes;
    @Override
    public float valor() {
    	List<Medicao> listCasos = getObservacoes();
    	List<Medicao> listMortes = getObservacoesMortes();
        int casos = listCasos.get(listCasos.size()-1).getCasos() - listCasos.get(0).getCasos();
        int mortes = listMortes.get(listMortes.size()-1).getCasos() - listMortes.get(0).getCasos();
        
        if(casos == 0) casos = 1;
        return mortes/(float)casos;
    }
    
    public boolean inclui(Medicao observacaoCasos, Medicao observacaoMortes) {
    	boolean adicionadoCaso = super.inclui(observacaoCasos);
    	if(observacoesMortes == null) {
    		observacoesMortes = new ArrayList<>();
    	}
        boolean adicionadoMortes = observacoesMortes.add(observacaoMortes);

        return adicionadoCaso && adicionadoMortes;
    }
    
    public List<Medicao> getObservacoesMortes() {
        List<Medicao> list = new ArrayList<>(observacoesMortes);
        list.sort(new CasosMaisRecentesComparator().reversed());
        return list;
    }
}
