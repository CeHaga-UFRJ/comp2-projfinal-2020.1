package covid.controller.api;

import java.util.ArrayList;
import java.util.List;

import covid.models.Medicao;


/**
 * Classe destinada a representar um conjunto de medições
 * @author Matheus Oliveira Silva - matheusflups8@gmail.com
 */

public class MedicaoLists {
	public List<Medicao> confirmedList;
	public List<Medicao> deathsList;
	public List<Medicao> recoveredList;

	public MedicaoLists() {
		this.confirmedList = new ArrayList<>();
		this.deathsList = new ArrayList<>();
		this.recoveredList = new ArrayList<>();
	}
}