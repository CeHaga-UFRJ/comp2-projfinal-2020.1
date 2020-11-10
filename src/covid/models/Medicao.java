package covid.models;

import covid.enums.StatusCaso;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe que representa uma medicao que contém um pais, data, casos e status da medicao.
 * @author Markson Arguello - marksonva@dcc.ufrj.br
 */

public class Medicao implements Serializable {
    public static final long serialVersionUID = 1000L;

    private Pais pais;
    private LocalDateTime momento;
    private int casos;
    private StatusCaso status;
    
    /**
     * Construtor da classe Medição
     * @param pais País da medição
     * @param momento data da medição
     * @param casos casos totais da medição
     * @param status status da medição
     */

    public Medicao(Pais pais, LocalDateTime momento, int casos, StatusCaso status) {
        this.pais = pais;
        this.momento = momento;
        this.casos = casos;
        this.status = status;
    }
    
    /**
     * @return País
     */
    public Pais getPais() {
        return pais;
    }
    
    /**
     * 
     * @return data da medição
     */
    public LocalDate getMomento() {
        return momento.toLocalDate();
    }
    
    /**
     * 
     * @return Casos totais da medição
     */

    public int getCasos() {
        return casos;
    }

    /**
     * 
     * @return status da medição (confirmados, mortos ou recuperados)
     */
    public StatusCaso getStatus() {
        return status;
    }
}
