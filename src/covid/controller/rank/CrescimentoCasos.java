package covid.controller.rank;

import covid.models.Medicao;

import java.util.List;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class CrescimentoCasos extends Estatistica{
    @Override
    public float valor() {
        List<Medicao> list = getObservacoes();
        Medicao ult = list.get(list.size()-1);
        Medicao prim = list.get(0);
        
        float ultCasos = ult.getCasos();
        float primCasos = prim.getCasos();
        if(primCasos == 0) return 0f;
        return (ultCasos - primCasos) / primCasos;
    }
}
