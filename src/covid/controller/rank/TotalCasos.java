package covid.controller.rank;

import covid.models.Medicao;

import java.util.List;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class TotalCasos extends Estatistica{
    @Override
    public float valor(){
        List<Medicao> list = getObservacoes();
        return list.get(list.size()-1).getCasos() - list.get(0).getCasos();
    }
}
