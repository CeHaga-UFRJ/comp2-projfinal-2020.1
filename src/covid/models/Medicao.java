package covid.models;

import covid.enums.StatusCaso;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Markson Arguello - marksonva@dcc.ufrj.br
 */
public class Medicao implements Serializable {
    public static final long serialVersionUID = 1000L;

    private Pais pais;
    private LocalDateTime momento;
    private int casos;
    private StatusCaso status;

    public Medicao(Pais pais, LocalDateTime momento, int casos, StatusCaso status) {
        this.pais = pais;
        this.momento = momento;
        this.casos = casos;
        this.status = status;
    }

    public Pais getPais() {
        return pais;
    }

    public LocalDate getMomento() {
        return momento.toLocalDate();
    }

    public int getCasos() {
        return casos;
    }

    public StatusCaso getStatus() {
        return status;
    }
}
