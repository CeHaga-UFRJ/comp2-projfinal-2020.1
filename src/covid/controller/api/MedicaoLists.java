package covid.controller.api;

import java.util.ArrayList;
import java.util.List;

import covid.models.Medicao;

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