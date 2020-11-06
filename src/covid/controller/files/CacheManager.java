package covid.controller.files;

import covid.controller.data.DataManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Carlos Bravo - cehaga@dcc.ufrjr.br
 */
public class CacheManager {
<<<<<<< Updated upstream
    public Medicao readFile(StatusCaso status, LocalDateTime date){
        String fileName = status.toString()+"_"+date.toString()+".ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/cache/"+fileName))){
            return (Medicao)ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            return null;
        }
    }

    public boolean writeFile(Medicao medicao){
        String fileName = medicao.getStatus().toString()+"_"+medicao.getMomento().toString()+".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/cache/"+fileName))){
            oos.writeObject(medicao);
=======
	
	
//    public HashMap<String, Medicao> readFile(StatusCaso status, LocalDate date){
//        String fileName = status.toString() + "_" + date.toString() + ".ser";
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/cache/"+fileName))){
//            return (HashMap<String, Medicao>) ois.readObject();
//        } catch (IOException | ClassNotFoundException e){
//        	System.out.println("Exception when reading obj");
//        	e.printStackTrace();
//            return null;
//        }
//    }
// 
//    public boolean writeFile(HashMap<String, Medicao> map){
//    	
//    	StatusCaso status = map.get("austria").getStatus();
//    	LocalDate date = map.get("austria").getMomento();
//    	
//        String fileName = status.toString() + "_" + date.toString() + ".ser";
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/cache/"+fileName))){
//            oos.writeObject(map);
//        } catch (IOException e){
//            System.out.println("Erro ao salvar " + fileName);
//            return false;
//        }
//        return true;
//    }  
    
    public void serializeData(HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> map) {
    	String fileName = "SERIALIZED_DATA.ser";
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WebContent/WEB-INF/DATA/" + fileName))){
            oos.writeObject(map);
>>>>>>> Stashed changes
        } catch (IOException e){
            System.out.println("Erro ao salvar " + fileName);
        }
    }
    
}
