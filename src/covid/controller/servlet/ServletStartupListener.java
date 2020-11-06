package covid.controller.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import covid.enums.StatusCaso;
import covid.launcher.ProgramLauncher;
import covid.models.Medicao;

public class ServletStartupListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Context Initialized");
		//ProgramLauncher.Teste();
		
		
		
		
	}
}
