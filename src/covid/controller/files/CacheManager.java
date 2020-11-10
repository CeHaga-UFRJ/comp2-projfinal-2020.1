package covid.controller.files;

import covid.controller.data.DataManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;
import covid.models.Pais;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrjr.br
 */
public class CacheManager {
	
	/**
	 * Serializa o Hashmap com todos os dados do programa
	 * @param map Hashmap a ser serializado
	 */
    
    public void serializeData(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
    	String fileName = "SERIALIZED_DATA.ser";
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WebContent/WEB-INF/DATA/" + fileName))){
            oos.writeObject(map);
        } catch (IOException e){
            System.out.println("Erro ao salvar " + fileName);
        }
    }
    
    /**
	 * Serializa o Hashmap com todos os dados dos países
	 * @param map Hashmap a ser serializado
	 */
    public void serializeCountries(HashMap<String, Pais> map) {
    	String fileName = "SERIALIZED_COUNTRIES.ser";
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WebContent/WEB-INF/DATA/" + fileName))){
            oos.writeObject(map);
        } catch (IOException e){
            System.out.println("Erro ao salvar " + fileName);
        }
    }
    /**
     * Deserializa o Hashmap com todos os dados do programa
     * @param map Hashmap a ser deserializado
     */
        
    
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
    
    /**
	 * Deserializa o Hashmap com todos os dados dos países
	 * @param map Hashmap a ser deserializado
	 */
    
    public HashMap<String, Pais> deserializeCountries() {
    	String fileName = "SERIALIZED_COUNTRIES.ser";
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("WebContent/WEB-INF/DATA/" + fileName))){
	    	return (HashMap<String, Pais>) ois.readObject();
	    } 
	    catch (IOException | ClassNotFoundException e){
	       	System.out.println("Exception when reading obj");
	    	e.printStackTrace();
	        return null;
	    }
	}
    
    
    
}
