package persistencia.dao.model;

import java.util.Calendar;

public class TResultados {
	
	private Integer ID_RESULTADO;
	
	private Integer ID_CARRERA;
	
	private Integer ID_CORREDOR;
	
	private long TIEMPO;
	
	private Integer POS_CAT;
	
	private Integer POS_GEN;
	
	private String TIEMPO_FORMATEADO;
	
	private String RITMO;
	
	private String ID_EVENTO;
	
	public String getID_EVENTO() {
		return ID_EVENTO;
	}

	public void setID_EVENTO(String iD_EVENTO) {
		ID_EVENTO = iD_EVENTO;
	}

	public Integer getPOS_CAT() {
		return POS_CAT;
	}

	public void setPOS_CAT(Integer pOS_CAT) {
		POS_CAT = pOS_CAT;
	}

	public Integer getPOS_GEN() {
		return POS_GEN;
	}

	public void setPOS_GEN(Integer pOS_GEN) {
		POS_GEN = pOS_GEN;
	}

	public String getTIEMPO_FORMATEADO() {
		TIEMPO_FORMATEADO = getTiempoFormateado();
		return TIEMPO_FORMATEADO;
	}
	
	public String getTIEMPO_FORMATEADO(boolean sinformato) {
		return TIEMPO_FORMATEADO;
	}

	public void setTIEMPO_FORMATEADO(String tIEMPO_FORMATEADO) {
		TIEMPO_FORMATEADO = tIEMPO_FORMATEADO;
	}

	public String getRITMO() {
		return RITMO;
	}

	public void setRITMO(String rITMO) {
		RITMO = rITMO;
	}

	private TCorredores corredor;
	
	private TCarreras carrera;
	
	public String getTiempoFormateado() {
		
		if (carrera!=null){
			Calendar calendar = Calendar.getInstance();
	    	calendar.setTimeInMillis(TIEMPO - carrera.getINICIO());
	    	
	    	int millis = calendar.get(Calendar.MILLISECOND);
	    	int segundos = calendar.get(Calendar.SECOND);
	    	int minutos = calendar.get(Calendar.MINUTE);
	    	
	    	String sMillis = millis < 10 ? "0" + millis : millis + "";
	    	sMillis = millis < 100 ? "0" + sMillis : sMillis;
	    	String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
	    	String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";  
	    	
	    	return ("" + sMinutos + ":" + sSegundos);	
		}
		
		return "NO";
	}
	

	public TCarreras getCarrera() {
		return carrera;
	}

	public void setCarrera(TCarreras carrera) {
		this.carrera = carrera;
	}

	public TCorredores getCorredor() {
		return corredor;
	}

	public void setCorredor(TCorredores corredor) {
		this.corredor = corredor;
	}

	public Integer getID_RESULTADO() {
		return ID_RESULTADO;
	}

	public void setID_RESULTADO(Integer iD_RESULTADO) {
		ID_RESULTADO = iD_RESULTADO;
	}

	public Integer getID_CARRERA() {
		return ID_CARRERA;
	}

	public void setID_CARRERA(Integer iD_CARRERA) {
		ID_CARRERA = iD_CARRERA;
	}

	public Integer getID_CORREDOR() {
		return ID_CORREDOR;
	}

	public void setID_CORREDOR(Integer iD_CORREDOR) {
		ID_CORREDOR = iD_CORREDOR;
	}

	public long getTIEMPO() {
		return TIEMPO;
	}

	public void setTIEMPO(long tIEMPO) {
		TIEMPO = tIEMPO;
	}
	
	
	
}
