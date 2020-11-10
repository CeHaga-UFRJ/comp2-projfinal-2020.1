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
	 * Retorna o enum correspondente � string passada como par�metro.
	 * @param string Tipo de ranking em formato de String
	 * @return Enum correspondente � string passada como par�metro
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
