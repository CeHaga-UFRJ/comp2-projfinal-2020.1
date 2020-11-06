package covid.comparators;

import java.util.Comparator;

import covid.models.ParOrdenado;

public class ParOrdenadoComparator<T , E extends Number> implements Comparator<ParOrdenado<T,E>> {

	@Override
    public int compare(ParOrdenado<T, E>  o1, ParOrdenado<T, E> o2) {
        if ((float) o1.getCases() > (float) o2.getCases() ) return 1;
        if ((float) o1.getCases() < (float) o2.getCases() ) return -1;
        return 0;
    }
}
