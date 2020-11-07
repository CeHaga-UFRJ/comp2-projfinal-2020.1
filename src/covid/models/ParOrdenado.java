package covid.models;
/**
 * Classe criada para ser um par ordenado de qualquer tipo.
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 *
 * @param <T>
 * @param <E>
 */
public class ParOrdenado<T , E extends Number>  {
	
	private T pais;
	private E cases;
	
	public ParOrdenado(T pais, E cases) {
		this.pais = pais;
		this.cases = cases;
	}
	
	public T getPais() {
		return pais;
	}
	
	public E getCases() {
		return cases;
	}
	
}
