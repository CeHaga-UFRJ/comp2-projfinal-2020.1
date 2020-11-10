package covid.models;
/**
 * Classe criada para representar um par ordenado.
 * 
 * <p>
 * Classe feita para representar um par ordenado que cont�m o nome de um pa�s e um valor para o 
 * tipo de ranking que ser� utilizado.
 * </p>
 * 
 * 
 * @author  Carlos Bravo, Markson Arguello, Matheus Oliveira
 *
 * @param <T> Tipo que ser� o nome do pais
 * @param <E> Tipo que ser� n�mero de casos
 */
public class ParOrdenado<T , E extends Number>  {
	
	private T pais;
	private E cases;
	
	/**
	 * Construtor da classe par ordenado
	 * 
	 * @param pais Nome do pais
	 * @param cases N�mero de casos
	 */
	public ParOrdenado(T pais, E cases) {
		this.pais = pais;
		this.cases = cases;
	}
	/**
	 * 
	 * @return nome do pa�s
	 */
	public T getPais() {
		return pais;
	}
	/**
	 * 
	 * @return n�mero de casos
	 */
	public E getCases() {
		return cases;
	}
	
}
