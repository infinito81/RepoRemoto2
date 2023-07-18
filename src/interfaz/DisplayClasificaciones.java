package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import dominio.Corredores;
import dominio.Eventos;
import dominio.Resultados;
import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TEventos;
import persistencia.dao.model.TResultados;
import util.Constantes;
import util.Estatica;

public class DisplayClasificaciones extends JFrame{
	
	private static final long serialVersionUID = 1L;
	JLabel lblCategoria = null;
	int idEventoSeleccionado;
	TEventos evento = null;
	JLabel lblClasificacion = new JLabel("");
	
	public DisplayClasificaciones (GraphicsConfiguration gc, int idEventoSeleccionado){
		super(gc);
		this.idEventoSeleccionado = idEventoSeleccionado;
		evento = Eventos.getEvento(idEventoSeleccionado); 
		init();
		
	}
	
	public void init () {
        JLabel label = null;
		try {
			label = new JLabel(new ImageIcon(ImageIO.read(new File(".\\res\\images\\fondo_btt_negro_med_2.jpg"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		this.setContentPane(label);
	    this.setLayout(new BorderLayout());
	     
	    JLabel lblCategoria = new JLabel("");
	        
        lblCategoria.setHorizontalAlignment(JLabel.CENTER);
        
		Icon logoClub = new ImageIcon(".\\res\\images\\popularia_negativo_peq.png");
		lblCategoria = new JLabel(evento.getNombreEvento(), logoClub, JLabel.LEFT);
		//lblCategoria.setForeground(colorTextos);
		Font fuenteCategoria = new Font("Roboto Slab", Font.BOLD, 54);
		lblCategoria.setFont(fuenteCategoria);
		lblCategoria.setForeground(Color.WHITE);
		lblCategoria.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblCategoria,BorderLayout.NORTH);
		
		getContentPane().setBackground(new Color(Constantes.CODIGO_COLOR_FONDO[0], Constantes.CODIGO_COLOR_FONDO[1], Constantes.CODIGO_COLOR_FONDO[2]));
		
		Color colorTextos = new Color (Constantes.CODIGO_COLOR_TEXTOS[0], Constantes.CODIGO_COLOR_TEXTOS[1], Constantes.CODIGO_COLOR_TEXTOS[2]);
		
		//Meter una etiqueta y una tabla HTML que rellene datos de las clasificaciones según la selección que está guardada en Estatica
		//Hacer el hilo que vaya cambiando la etiqueta
		lblClasificacion.setFont(new Font("Calibri", Font.BOLD, 50));
		lblClasificacion.setForeground(Color.WHITE);
		lblClasificacion.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblClasificacion,BorderLayout.CENTER);
		//rellenaEtiquetaClasificacion();
		
		
		if (Estatica.getInstance().getDisplayCategoriaSeleccionada()!= null && !Estatica.getInstance().getDisplayCategoriaSeleccionada().equals("")) {
			int categoriaSeleccionada = Integer.parseInt(Estatica.getInstance().getDisplayCategoriaSeleccionada());
			rellenaEtiquetaClasificacion(categoriaSeleccionada);
		} else {
			if (Estatica.getInstance().isClasificacionesAutoRunActivado()) {
				Thread hilo = new Thread(new HiloMuestraClasificaciones());
				hilo.start();
			}
		}
		
	}
	

	private void rellenaEtiquetaClasificacion(int categoriaSeleccionada) {
		//if (Estatica.getInstance().getDisplayCategoriaSeleccionada()!= null && !Estatica.getInstance().getDisplayCategoriaSeleccionada().equals("")) {
		//	int categoriaSeleccionada = Integer.parseInt(Estatica.getInstance().getDisplayCategoriaSeleccionada());
		
			TCategorias categoria = Corredores.getCategoriaBD(idEventoSeleccionado, categoriaSeleccionada);
			StringBuilder sb = new StringBuilder(128);
			
			sb.append("<html>");
			
			sb.append("<table border='1'>");
			sb.append("<caption align='center'>" + categoria.getDescCategoria() + "</caption>");
			sb.append("<tr>");
			sb.append("<td align='center' width='60'>" + "POS" + "</td>");
			sb.append("<td align='center' width='50'>" + "DORSAL" + "</td>");
			sb.append("<td align='center' width='800'>" + "NOMBRE Y APELLIDOS" + "</td>");
			sb.append("<td align='center' width='70'>" + "TIEMPO" + "</td>");
			sb.append("</tr>");		
			
			ArrayList<TResultados> lResultados = Resultados.getResultadosCarreras(idEventoSeleccionado);
			for( TResultados resultado : lResultados ){
				if (resultado.getCorredor().getIdCategoria() == categoriaSeleccionada) {

							sb.append("<tr>");
							sb.append("<td align='center' width='60'>" + resultado.getPOS_CAT() + "º</td>");
							sb.append("<td align='center' width='50'>" + resultado.getCorredor().getDORSAL() + "</td>");
							sb.append("<td align='center' width='800'>" + resultado.getCorredor().getNOMBRE() + " " + resultado.getCorredor().getAPELLIDOS() + "</td>");
							sb.append("<td align='center' width='70'>" + resultado.getTIEMPO_FORMATEADO(true) + "</td>");
							sb.append("</tr>");					

				}
			}
			sb.append("</table>");
			sb.append("</html>");

			lblClasificacion.setText(sb.toString());

			//return sb.toString();			
		//}
		
	}


	private class HiloMuestraClasificaciones implements Runnable{
		
		
		
		public HiloMuestraClasificaciones() {
			super();
		}


		@Override
		public void run() {
			System.out.println("ARRANCO SONDA MOSTRAR CLASIFICACIONES");
			int categoriaSeleccionada = 1;
			while (true) {
				try {
					long tiempoSonda = Constantes.TIEMPO_SONDA_CLASIFICACIONES;
					//tiempoSonda = 1000;
					
					
					
					
					if (Estatica.getInstance().getDisplayCategoriaSeleccionada()!= null && !Estatica.getInstance().getDisplayCategoriaSeleccionada().equals("")) {
						categoriaSeleccionada = Integer.parseInt(Estatica.getInstance().getDisplayCategoriaSeleccionada());
						rellenaEtiquetaClasificacion(categoriaSeleccionada);
					} else {
						rellenaEtiquetaClasificacion(categoriaSeleccionada);
						categoriaSeleccionada++;
						if (categoriaSeleccionada > 8) {
							categoriaSeleccionada = 1;
						}
					}
					System.out.println("dUERMO sonda clasificaciones " + tiempoSonda);
					Thread.sleep(tiempoSonda);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}
