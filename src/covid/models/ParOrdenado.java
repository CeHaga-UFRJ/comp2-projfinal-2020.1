package covid.models;

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
