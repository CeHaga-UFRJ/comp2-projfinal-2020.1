package covid.models;
/**
 * Classe criada para representar um par ordenado.
 * 
 * <p>
 * Classe feita para representar um par ordenado que contém o nome de um país e um valor para o 
 * tipo de ranking que será utilizado.
 * </p>
 * 
 * 
 * @author  Carlos Bravo, Markson Arguello, Matheus Oliveira
 *
 * @param <T> Tipo que será o nome do pais
 * @param <E> Tipo que será número de casos
 */
public class ParOrdenado<T , E extends Number>  {
	
	private T pais;
	private E cases;
	
	/**
	 * Construtor da classe par ordenado
	 * 
	 * @param pais Nome do pais
	 * @param cases Número de casos
	 */
	public ParOrdenado(T pais, E cases) {
		this.pais = pais;
		this.cases = cases;
	}
	/**
	 * 
	 * @return nome do país
	 */
	public T getPais() {
		return pais;
	}
	/**
	 * 
	 * @return número de casos
	 */
	public E getCases() {
		return cases;
	}
	
}
