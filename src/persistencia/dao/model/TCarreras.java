package persistencia.dao.model;

public class TCarreras {
	
	private Integer ID_EVENTO;
	
	public Integer getID_EVENTO() {
		return ID_EVENTO;
	}

	public void setID_EVENTO(Integer iD_EVENTO) {
		ID_EVENTO = iD_EVENTO;
	}

	private Integer ID_CARRERA;
	
	private String HORA;
	
	private String CATEGORIA;
	
	private long INICIO;

	public Integer getID_CARRERA() {
		return ID_CARRERA;
	}

	public void setID_CARRERA(Integer iD_CARRERA) {
		ID_CARRERA = iD_CARRERA;		
	}

	public String getHORA() {
		return HORA;
	}

	public void setHORA(String hORA) {
		HORA = hORA;
	}

	public String getCATEGORIA() {
		return CATEGORIA;
	}

	public void setCATEGORIA(String cATEGORIA) {
		CATEGORIA = cATEGORIA;
	}

	public long getINICIO() {
		return INICIO;
	}

	public void setINICIO(long iNICIO) {
		INICIO = iNICIO;		
	}
	
	
	
}
