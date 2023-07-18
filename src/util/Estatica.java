package util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;

import persistencia.dao.model.TLecturas;
import persistencia.dao.model.TResultados;

public class Estatica {
		
	private int carreraSeleccionada = -1;
	private String descCarreraSeleccionada;
	private long tiempoInicial;
	
	private String displayCategoriaSeleccionada;
	private String displaySexoSeleccionado;
	private boolean clasificacionesAutoRunActivado;
	
	
	
	private HashMap<Integer, ArrayList<TLecturas>> lecturasTotalesDorsales = new HashMap<Integer, ArrayList<TLecturas>>();
	
	private ArrayList<String> retirados = null;
	
	
	public void anyadirRetirado(String dorsal) {
		if (retirados == null)
			retirados = new ArrayList<String>();
		
		if (!isRetirado(dorsal))
			retirados.add(dorsal);
		else 
			System.out.println("Ya estaba retirado");
	}
	
	

	public ArrayList<String> getRetirados() {
		if (retirados == null)
			retirados = new ArrayList<String>();
		return retirados;
	}
	
	public boolean isRetirado (String dorsal) {
		if (retirados == null)
			return false;
		
		return retirados.contains(dorsal);
	}


	public HashMap<Integer, ArrayList<TLecturas>> getLecturasTotalesDorsales() {
		return lecturasTotalesDorsales;
	}
	public void resetearLecturasTotales() {
		lecturasTotalesDorsales = new HashMap<Integer, ArrayList<TLecturas>>();
	}
	
	public void setLecturasTotalesDorsales(HashMap<Integer, ArrayList<TLecturas>> lecturasTotalesDorsales) {
		this.lecturasTotalesDorsales = lecturasTotalesDorsales;
	}

	public boolean isClasificacionesAutoRunActivado() {
		return clasificacionesAutoRunActivado;
	}

	public void setClasificacionesAutoRunActivado(
			boolean clasificacionesAutoRunActivado) {
		this.clasificacionesAutoRunActivado = clasificacionesAutoRunActivado;
	}

	public String getDisplaySexoSeleccionado() {
		return displaySexoSeleccionado;
	}

	public void setDisplaySexoSeleccionado(String displaySexoSeleccionado) {
		this.displaySexoSeleccionado = displaySexoSeleccionado;
	}

	public String getDisplayCategoriaSeleccionada() {
		return displayCategoriaSeleccionada;
	}

	public void setDisplayCategoriaSeleccionada(String displayCategoriaSeleccionada) {
		this.displayCategoriaSeleccionada = displayCategoriaSeleccionada;
	}

	private TResultados resultadoDorsal;	
	
		
	public TResultados getResultadoDorsal() {
		return resultadoDorsal;
	}

	public void setResultadoDorsal(TResultados resultadoDorsal) {
		this.resultadoDorsal = resultadoDorsal;
	}

	public JLabel[] getlLblLlegadosDorsal() {
		return lLblLlegadosDorsal;
	}

	public void setlLblLlegadosDorsal(JLabel[] lLblLlegadosDorsal) {
		this.lLblLlegadosDorsal = lLblLlegadosDorsal;
	}

	JLabel lLblLlegadosDorsal [] = new JLabel[5];

	public int getCarreraSeleccionada() {
		return carreraSeleccionada;
	}

	public void setCarreraSeleccionada(int carreraSeleccionada) {
		this.carreraSeleccionada = carreraSeleccionada;
	}

	public String getDescCarreraSeleccionada() {
		return descCarreraSeleccionada;
	}

	public void setDescCarreraSeleccionada(String descCarreraSeleccionada) {
		this.descCarreraSeleccionada = descCarreraSeleccionada;
	}

	public long getTiempoInicial() {
		return tiempoInicial;
	}

	public void setTiempoInicial(long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
	}

	private static final Estatica INSTANCE = new Estatica();

    private Estatica() {}

    public static Estatica getInstance() {
        return INSTANCE;
    }
}
