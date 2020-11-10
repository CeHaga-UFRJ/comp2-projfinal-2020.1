package covid.enums;
/**
 * Enumeracao dos tipos de exportacoes que vamos ter.
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 */
public enum ExportType {
	TSV,
	CSV,
	NONE;
	
	/**
	 * Retorna o enum correspondente à string passada como parâmetro.
	 * @param string Tipo de ranking em formato de String
	 * @return Enum correspondente à string passada como parâmetro
	 */
	public static ExportType stringToExportType(String string) {
		String stringFormatted = string.trim().toUpperCase();
		switch(stringFormatted) {
			case("TSV"):
				return TSV;
			case("CSV"):
				return CSV;
			case("NONE"):
				return NONE;
			default:
				return NONE;
		}
	}
}
