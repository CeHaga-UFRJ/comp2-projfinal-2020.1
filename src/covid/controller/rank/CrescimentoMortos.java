package covid.controller.rank;

import covid.models.Medicao;

import java.util.List;

/**
 * Classe que representa a estatística para o crescimento de mortos.
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class CrescimentoMortos extends Estatistica{
    @Override
    public float valor() {
        List<Medicao> list = getObservacoes();
        Medicao ult = list.get(list.size()-1);
        Medicao prim = list.get(0);
        return (ult.getCasos() - prim.getCasos()) / prim.getCasos();
    }
}
