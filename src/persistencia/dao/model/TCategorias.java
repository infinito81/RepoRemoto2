package persistencia.dao.model;

import java.util.ArrayList;

import dominio.Corredores;

public class TCategorias implements Comparable{
	private int idEvento;
	private int idCategoria;
	private String descCategoria;
	private int numPasos;
	private int distancia;
	private int retraso;
	
	private int idCarrera;
	
	private ArrayList<ArrayList<TCorredores>> corredoresTodos = new ArrayList<ArrayList<TCorredores>>();
	

	
	public int getRetraso() {
		return retraso;
	}
	public void setRetraso(int retraso) {
		this.retraso = retraso;
	}
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public int getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getDescCategoria() {
		return descCategoria;
	}
	public void setDescCategoria(String descCategoria) {
		this.descCategoria = descCategoria;
	}
	public int getNumPasos() {
		return numPasos;
	}
	public void setNumPasos(int numPasos) {
		this.numPasos = numPasos;
		
		for (int i=0; i<numPasos;i++) {
			corredoresTodos.add(new ArrayList<TCorredores>());
		}
	}


	public int anyadirCorredor(TCorredores ultimoCorredor) {
		String vuelta = ultimoCorredor.getVuelta();
		int paso = -1;
		if (vuelta != null && vuelta.equals("META")){
			paso = numPasos - 1;
		} else if (vuelta != null) {
			paso = Integer.parseInt(vuelta.substring(1)) - 1;
			
		}
		for (int i=0; i<numPasos; i++) {
			
			if (paso == i && corredoresTodos.get(i).contains(ultimoCorredor)) {
				
			} else if (paso==i){
				corredoresTodos.get(i).add(ultimoCorredor);
				return corredoresTodos.get(i).size();
			}
		}
		
		return -1;
		
	}


	@Override
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("\nCATEGORIA: " + descCategoria);
		returnString.append("\n");
		for (int i=0; i<numPasos; i++) {
			//returnString.append("PASO " + (i+1));
			if (numPasos - i == 1)
				returnString.append("META");
			else
				returnString.append("PASO " + (i+1));
			returnString.append("\n");
			int indexCorredor = 1;
			returnString.append("HAN PASADO: " + corredoresTodos.get(i).size() + "\n");
			for (TCorredores corredor : corredoresTodos.get(i)) {
				returnString.append((indexCorredor++) + ": " + corredor.toString() + "\n" );
			}
			//returnString.append(corredoresTodos.get(i));
			returnString.append("\n-------------------------\n");
		}
		
		return returnString.toString();
	}
	@Override
	public int compareTo(Object arg0) {
		TCategorias categoria = (TCategorias) arg0;
		if (categoria.idCategoria > this.getIdCategoria()) {
			return -1;
		} else if (categoria.idCategoria < this.getIdCategoria()) {
			return 1;
		}
		return 0;
	}
	/**
	 * @return the idCarrera
	 */
	public int getIdCarrera() {
		return idCarrera;
	}
	/**
	 * @param idCarrera the idCarrera to set
	 */
	public void setIdCarrera(int idCarrera) {
		this.idCarrera = idCarrera;
	}
	
	
}
