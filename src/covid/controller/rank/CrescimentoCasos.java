package covid.controller.rank;

import covid.models.Medicao;

import java.util.List;

/**
 * Classe que representa a estatística para o crescimento de casos.
 * @author Carlos Bravo, Markson Arguello
 */
public class CrescimentoCasos extends Estatistica{
    @Override
    public float valor() {
        List<Medicao> list = getObservacoes();
        Medicao ult = list.get(list.size()-1);
        Medicao prim = list.get(0);
        
        int ultCasos = ult.getCasos();
        int primCasos = prim.getCasos();
        if(primCasos == 0) primCasos = 1;
        return (ultCasos - primCasos) / (float)primCasos;
    }
}
