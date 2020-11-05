package covid.comparators;

import covid.models.Medicao;

import java.util.Comparator;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrj.br
 */
public class CasosMaisRecentesComparator implements Comparator<Medicao> {
    @Override
    public int compare(Medicao o1, Medicao o2) {
        if(o1.getMomento().isBefore(o2.getMomento())) return 1;
        if(o1.getMomento().isAfter(o2.getMomento())) return -1;
        return 0;
    }
}
