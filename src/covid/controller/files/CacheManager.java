package covid.controller.files;

import covid.controller.data.DataManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrjr.br
 */
public class CacheManager {
//    public HashMap<String, Medicao> readFile(StatusCaso status, LocalDateTime date){
//        String fileName = status.toString() + "_" + date.toString() + ".ser";
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/cache/"+fileName))){
//            return (HashMap<String, Medicao>)ois.readObject();
//        } catch (IOException | ClassNotFoundException e){
//            return null;
//        }
//    }
//
//    public boolean writeFile(HashMap<String, Medicao> map){
//    	
//    	//gambiarra, melhorar isso depois
//    	StatusCaso status = map.get("austria").getStatus();
//    	LocalDate date = map.get("austria").getMomento();
//    	
//        String fileName = status.toString() + "_" + date.toString() + ".ser";
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/cache/"+fileName))){
//            oos.writeObject(map);
//        } catch (IOException e){
//            System.out.println("Erro ao salvar "+fileName);
//            return false;
//        }
//        return true;
//    }
    
    public void serializeData(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
    	String fileName = "SERIALIZED_DATA.ser";
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WebContent/WEB-INF/DATA/" + fileName))){
            oos.writeObject(map);
        } catch (IOException e){
            System.out.println("Erro ao salvar " + fileName);
        }
    }
    
    public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> deserializeData() {
    	String fileName = "SERIALIZED_DATA.ser";
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("WebContent/WEB-INF/DATA/" + fileName))){
	    	return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) ois.readObject();
	    } 
	    catch (IOException | ClassNotFoundException e){
	       	System.out.println("Exception when reading obj");
	    	e.printStackTrace();
	        return null;
	    }
	}
    
    
    
    
}
