package persistencia.dao.model;

public class TLecturas {
	@Override
	public String toString() {
		return "TLecturas [idEvento=" + idEvento + ", idCarrera=" + idCarrera + ", tipoLectura=" + tipoLectura
				+ ", tagLeido=" + tagLeido + ", dorsalLeido=" + dorsalLeido + ", fechaInsert=" + fechaInsert
				+ ", tiempoFinal=" + tiempoFinal + "]";
	}

	private Integer idEvento;
	
	private Integer idCarrera;
	
	private String tipoLectura;
	
	private String tagLeido;
	
	private Integer dorsalLeido;
	
	private long fechaInsert;
	
	private String tiempoFinal;
	
	private boolean buena = false;

	public boolean isBuena() {
		return buena;
	}

	public void setBuena(boolean buena) {
		this.buena = buena;
	}

	public Integer getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Integer idEvento) {
		this.idEvento = idEvento;
	}

	public Integer getIdCarrera() {
		return idCarrera;
	}

	public void setIdCarrera(Integer idCarrera) {
		this.idCarrera = idCarrera;
	}

	public String getTipoLectura() {
		return tipoLectura;
	}

	public void setTipoLectura(String tipoLectura) {
		this.tipoLectura = tipoLectura;
	}

	public String getTagLeido() {
		return tagLeido;
	}

	public void setTagLeido(String tagLeido) {
		this.tagLeido = tagLeido;
	}

	public Integer getDorsalLeido() {
		return dorsalLeido;
	}

	public void setDorsalLeido(Integer dorsalLeido) {
		this.dorsalLeido = dorsalLeido;
	}

	public long getFechaInsert() {
		return fechaInsert;
	}

	public void setFechaInsert(long fechaInsert) {
		this.fechaInsert = fechaInsert;
	}

	public String getTiempoFinal() {
		return tiempoFinal;
	}

	public void setTiempoFinal(String tiempoFinal) {
		this.tiempoFinal = tiempoFinal;
	}
	
}
