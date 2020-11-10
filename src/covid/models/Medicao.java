package covid.models;

import covid.enums.StatusCaso;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe que representa uma medicao que cont�m um pais, data, casos e status da medicao.
 * @author Markson Arguello - marksonva@dcc.ufrj.br
 */

public class Medicao implements Serializable {
    public static final long serialVersionUID = 1000L;

    private Pais pais;
    private LocalDateTime momento;
    private int casos;
    private StatusCaso status;
    
    /**
     * Construtor da classe Medi��o
     * @param pais Pa�s da medi��o
     * @param momento data da medi��o
     * @param casos casos totais da medi��o
     * @param status status da medi��o
     */

    public Medicao(Pais pais, LocalDateTime momento, int casos, StatusCaso status) {
        this.pais = pais;
        this.momento = momento;
        this.casos = casos;
        this.status = status;
    }
    
    /**
     * @return Pa�s
     */
    public Pais getPais() {
        return pais;
    }
    
    /**
     * 
     * @return data da medi��o
     */
    public LocalDate getMomento() {
        return momento.toLocalDate();
    }
    
    /**
     * 
     * @return Casos totais da medi��o
     */

    public int getCasos() {
        return casos;
    }

    /**
     * 
     * @return status da medi��o (confirmados, mortos ou recuperados)
     */
    public StatusCaso getStatus() {
        return status;
    }
}
