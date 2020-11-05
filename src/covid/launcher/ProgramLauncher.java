package covid.launcher;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import covid.controller.api.APIReader;
import covid.controller.files.CacheManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.Pais;

public class ProgramLauncher {
    public static void main(String[] args) {
    	
    	
    	System.out.println("Hello World");
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        Instant dataInicialInstant = Instant.from(formatter.parse("2020-03-01T00:00:00Z"));
        Instant dataFinalInstant = Instant.from(formatter.parse("2020-03-08T00:00:00Z"));
        LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
        LocalDateTime dataFinal = LocalDateTime.ofInstant(dataFinalInstant, ZoneOffset.UTC);
        
        List<Medicao> list = APIReader.getAllCountryCasesByPeriod(StatusCaso.CONFIRMADOS, dataInicial, dataFinal);
       	
        CacheManager cm = new CacheManager();
        for (Medicao medicao : list) {
        	System.out.println(medicao.getPais().getSlug() + ": " + medicao.getCasos());
        	cm.writeFile(medicao);
        }
        
    	
    	
//    	DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
//    	Instant dataInicialInstant = Instant.from(formatter.parse("2020-03-01T00:00:00Z"));
//    	LocalDateTime dataInicial = LocalDateTime.ofInstant(dataInicialInstant, ZoneOffset.UTC);
//    	Medicao medicao = new Medicao(new Pais("brazil", "codigo", "brazil", 0, 0), dataInicial, 15, StatusCaso.CONFIRMADOS);
//    	CacheManager cm = new CacheManager();
//    	cm.writeFile(medicao);
    	
    }
    
    
}
