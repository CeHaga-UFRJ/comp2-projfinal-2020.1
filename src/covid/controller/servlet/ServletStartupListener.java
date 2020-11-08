package covid.controller.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import covid.controller.data.DataManager;
import covid.enums.StatusCaso;
import covid.models.Medicao;

@WebListener
public class ServletStartupListener implements javax.servlet.ServletContextListener{
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		deserializeProjectPath(context);
		DataManager.getDataManager().setMap(deserializeData(context));
	}
	
	public String deserializeProjectPath(ServletContextEvent context) {
		InputStream inputStream = context.getServletContext().getResourceAsStream("/WEB-INF/DATA/PROJECT_PATH.txt");
		
		Scanner scanner = new Scanner(inputStream);
		String path = scanner.nextLine();
		scanner.close();
		if(path.isBlank()) {
			System.out.println("Configurações de exportação incorretas. Inicie a main do programa para configurar a API.");
			return null;
		}
		
		DataManager.getDataManager().setProjectPath(path);
		return path;
	}
	
	public HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>> deserializeData(ServletContextEvent context) {
		InputStream inputStream = context.getServletContext().getResourceAsStream("/WEB-INF/DATA/SERIALIZED_DATA.ser");
	    try (ObjectInputStream ois = new ObjectInputStream(inputStream)){
	    	return (HashMap<StatusCaso, HashMap<LocalDate, HashMap<String, Medicao>>>) ois.readObject();
	    } 
	    catch (IOException | ClassNotFoundException e){
	       	System.out.println("Exception when reading obj");
	    	e.printStackTrace();
	        return null;
	    }
	}
}
