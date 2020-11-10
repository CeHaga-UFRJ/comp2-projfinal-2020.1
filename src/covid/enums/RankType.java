package covid.enums;

/**
 * Enum para representar qual ranking o usuário escolher.
 * @author Markson de Viana Arguello
 *
 */
public enum RankType {
	MAIOR_NUMERO_CONFIRMADOS,
	MAIOR_NUMERO_MORTOS,
	MAIOR_NUMERO_RECUPERADOS,
	MAIOR_CRESCIMENTO_CONFIRMADOS,
	MAIOR_CRESCIMENTO_MORTOS,
	MAIOR_CRESCIMENTO_RECUPERADOS,
	MAIOR_TAXA_MORTALIDADE,
	MAIOR_PROXIMIDADE_DO_EPICENTRO;
	
	
	/**
	 * Retorna o enum correspondente à string passada como parâmetro.
	 * @param string Tipo de ranking em formato de String
	 * @return Enum correspondente à string passada como parâmetro
	 */
	
	public static RankType stringToRankType(String string) {
		String stringFormatted = string.trim().toUpperCase();
		switch(stringFormatted) {
			case("MAIOR_NUMERO_CONFIRMADOS"):
				return MAIOR_NUMERO_CONFIRMADOS;
			case("MAIOR_NUMERO_MORTOS"):
				return MAIOR_NUMERO_MORTOS;
			case("MAIOR_NUMERO_RECUPERADOS"):
				return MAIOR_NUMERO_RECUPERADOS;
			case("MAIOR_CRESCIMENTO_CONFIRMADOS"):
				return MAIOR_CRESCIMENTO_CONFIRMADOS;
			case("MAIOR_CRESCIMENTO_MORTOS"):
				return MAIOR_CRESCIMENTO_MORTOS;
			case("MAIOR_CRESCIMENTO_RECUPERADOS"):
				return MAIOR_CRESCIMENTO_RECUPERADOS;
			case("MAIOR_TAXA_MORTALIDADE"):
				return MAIOR_TAXA_MORTALIDADE;
			case("MAIOR_PROXIMIDADE_DO_EPICENTRO"):
				return MAIOR_PROXIMIDADE_DO_EPICENTRO;
			default:
				return null;
		}
	}
	
	
	
	
}
