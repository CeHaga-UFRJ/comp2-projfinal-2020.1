package covid.util;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 *
 * Classe para ajudar com funções auxiliares
 *
 */

public class Tools{

    /**
     *
     * Valida e retorna um float.
     *
     */

    public static float getFloat(){
        boolean sucesso = false;
        float f = 0;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                f = scanner.nextFloat();
                sucesso = true;
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. Float esperado.");
            }
        }
        return f;
    }

    /**
     *
     * Valida e retorna um int.
     *
     */
    public static int getInt(){
        boolean sucesso = false;
        int i = 0;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                i = scanner.nextInt();
                sucesso = true;
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. Int esperado.");
            }
        }
        return i;
    }
    /**
     *
     * Valida e retorna uma string.
     *
     */
    public static String getString(){
        boolean sucesso = false;
        String s = null;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                s = scanner.nextLine();
                sucesso = true;
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. String esperada.");
            }
        }
        return s;
    }

}