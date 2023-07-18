package persistencia.dao.model;


public class TCorredores {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DORSAL == null) ? 0 : DORSAL.hashCode());
		result = prime * result + ((ID_EVENTO == null) ? 0 : ID_EVENTO.hashCode());
		
		return result;
	}

	@Override
	public String toString() {
		//if (DORSAL != null && DORSAL.)
		return "\t" + DORSAL + "\t" + NOMBRE + " " + APELLIDOS + ", " + TIEMPO_TOTAL_FORMATEADO + "";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TCorredores other = (TCorredores) obj;
		if (DORSAL == null) {
			if (other.DORSAL != null)
				return false;
		} else if (!DORSAL.equals(other.DORSAL))
			return false;
		if (ID_EVENTO == null) {
			if (other.ID_EVENTO != null)
				return false;
		} else if (!ID_EVENTO.equals(other.ID_EVENTO))
			return false;

		return true;
	}

	private Integer ID_EVENTO;
	
	private Integer ID;
	
	private String NOMBRE;
	
	private String APELLIDOS;
	
	private String DNI;
	
	private String EMAIL;
	
	private String SEXO;
	
	/*
	 * YYYY-MM-DD
	 */
	private String FECHA_NAC;
	
	private String CATEGORIA;
	
	private String POBLACION;
	
	private String CLUB;
	
	private String DORSAL;
	
	private String TIEMPO_TOTAL_FORMATEADO;
	
	private String ultimoPaso;
	
	private TCategorias tCategoria;
	
	private String vuelta;
	
	private int posicionProvisional;
	



	public int getPosicionProvisional() {
		return posicionProvisional;
	}

	public void setPosicionProvisional(int posicionProvisional) {
		this.posicionProvisional = posicionProvisional;
	}

	public String getVuelta() {
		return vuelta;
	}

	public void setVuelta(String vuelta) {
		this.vuelta = vuelta;
	}

	public TCategorias gettCategoria() {
		return tCategoria;
	}

	public void settCategoria(TCategorias tCategoria) {
		this.tCategoria = tCategoria;
	}

	private int idCategoria;

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	public String getUltimoPaso() {
		return ultimoPaso;
	}

	public void setUltimoPaso(String ultimoPaso) {
		this.ultimoPaso = ultimoPaso;
	}

	public String getTIEMPO_TOTAL_FORMATEADO() {
		return TIEMPO_TOTAL_FORMATEADO;
	}

	public void setTIEMPO_TOTAL_FORMATEADO(String tIEMPO_TOTAL_FORMATEADO) {
		TIEMPO_TOTAL_FORMATEADO = tIEMPO_TOTAL_FORMATEADO;
	}
	
	public Integer getID_EVENTO() {
		return ID_EVENTO;
	}

	public void setID_EVENTO(Integer iD_EVENTO) {
		ID_EVENTO = iD_EVENTO;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	public String getAPELLIDOS() {
		return APELLIDOS;
	}

	public void setAPELLIDOS(String aPELLIDOS) {
		APELLIDOS = aPELLIDOS;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getSEXO() {
		return SEXO;
	}

	public void setSEXO(String sEXO) {
		SEXO = sEXO;
	}

	public String getFECHA_NAC() {
		return FECHA_NAC;
	}

	public void setFECHA_NAC(String fECHA_NAC) {
		FECHA_NAC = fECHA_NAC;
	}

	public String getCATEGORIA() {
		return CATEGORIA;
	}

	public void setCATEGORIA(String cATEGORIA) {
		CATEGORIA = cATEGORIA;
	}

	public String getPOBLACION() {
		return POBLACION;
	}

	public void setPOBLACION(String pOBLACION) {
		POBLACION = pOBLACION;
	}

	public String getCLUB() {
		return CLUB;
	}

	public void setCLUB(String cLUB) {
		CLUB = cLUB;
	}

	public String getDORSAL() {
		return DORSAL;
	}

	public void setDORSAL(String dORSAL) {
		DORSAL = dORSAL;
	}

	public boolean isCorredorLocal() {
		if (POBLACION!=null && (POBLACION.trim().toUpperCase().equals("NAVALCÁN") || POBLACION.trim().toUpperCase().equals("NAVALCAN")))
			return true;
		
		return false;
	}
	
	
}
