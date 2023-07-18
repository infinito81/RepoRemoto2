package persistencia.dao.model;

import java.util.List;
import java.util.Map;

public class EstadoCarrera {

	private TEventos evento;
	
	private List <TCategorias> categorias;
	
	
	public EstadoCarrera(TEventos evento) {
		super();
		this.evento = evento;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("evento=" + evento + "\n");
		for (TCategorias categoria : categorias) {
			//if (categoria.getIdCategoria()<6) {
				sb.append(categoria.toString());
			//}
			
		}
		return sb.toString();
	}
	
	public String toString(String categoriaSeleccionada) {
		
		for (TCategorias categoria : categorias) {
			if (categoriaSeleccionada.equals(categoria.getIdCategoria() + "")) {
				return categoria.toString();
			}
		}
		return "EstadoCarrera [evento=" + evento + ", categorias=" + categorias + "]";
	}

	public TEventos getEvento() {
		return evento;
	}

	public void setEvento(TEventos evento) {
		this.evento = evento;
	}

	public List<TCategorias> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<TCategorias> categorias) {
		this.categorias = categorias;
	}

	public int anyadirCorredor(TCorredores ultimoCorredor) {
		
		for (TCategorias categoria : categorias) {
			if (ultimoCorredor.getIdCategoria() == categoria.getIdCategoria()) {
				return categoria.anyadirCorredor(ultimoCorredor);
			}
		}	
		return -1;
	}	
	

	
}
