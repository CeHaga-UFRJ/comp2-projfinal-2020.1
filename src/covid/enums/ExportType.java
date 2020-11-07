package covid.enums;
/**
 * Enumeracao dos tipos de exportacoes que vamos ter.
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 */
public enum ExportType {
	TSV,
	CSV,
	NONE;
	
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
				return null;
		}
	}
}
