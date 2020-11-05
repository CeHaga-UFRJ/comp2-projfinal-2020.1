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
        } catch (IOException e){
            System.out.println("Erro ao salvar "+fileName);
            return false;
        }
        return true;
    }
    
}
