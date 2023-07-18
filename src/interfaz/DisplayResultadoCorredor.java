package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import dominio.Resultados;
import util.Constantes;
import util.Estatica;
import java.awt.Component;

public class DisplayResultadoCorredor extends JFrame {
	
	
	JLabel lblTitulo = null;
	JLabel lblDorsal = null;
	JLabel lblCorredor = null;
	JLabel lblPosicionesCategoria = null;
	JLabel lblTiempo = null;
	JLabel lblRitmo = null;
	
	public DisplayResultadoCorredor(GraphicsConfiguration gc){
		super(gc);
		init();
	}
	
	public void init () {
		String[] fontNames=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		System.out.println(Arrays.toString(fontNames));
		
		String nombreImagen = "/images/popularia.png";
		//Image imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
		
		//setContentPane(new JPanelFondo(imagen));
		
		getContentPane().setBackground(new Color(Constantes.CODIGO_COLOR_FONDO[0], Constantes.CODIGO_COLOR_FONDO[1], Constantes.CODIGO_COLOR_FONDO[2]));
		
		Color colorTextos = new Color (Constantes.CODIGO_COLOR_TEXTOS[0], Constantes.CODIGO_COLOR_TEXTOS[1], Constantes.CODIGO_COLOR_TEXTOS[2]);

		Icon logoClub = new ImageIcon(getClass().getResource("/images/popularia.png"));
		lblTitulo = new JLabel("  SENDA VIRIATO 2019 - SEGURILLA", logoClub, JLabel.LEFT);
		lblTitulo.setBounds(10, 11, 949, 120);
		lblTitulo.setForeground(colorTextos);
		Font fuenteCategoria = new Font("Roboto Slab", Font.BOLD, 55);
		getContentPane().setLayout(null);
		lblTitulo.setFont(fuenteCategoria);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblTitulo);
		
		lblDorsal = new JLabel(getHTMLDorsal());
		lblDorsal.setLocation(0, 120);
		lblDorsal.setSize(368, 76);
		lblDorsal.setFont(new Font("Lucida Sans", Font.PLAIN, 65));
		lblDorsal.setForeground(colorTextos);
		lblDorsal.setHorizontalTextPosition(JLabel.CENTER);
		lblDorsal.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(lblDorsal);
		
		lblCorredor = new JLabel(getHTMLCorredor());
		lblCorredor.setLocation(0, 196);
		lblCorredor.setSize(342, 89);
		lblCorredor.setFont(new Font("Roboto Slab", Font.PLAIN, 70));
		lblCorredor.setForeground(colorTextos);
		lblCorredor.setHorizontalAlignment(JLabel.CENTER);
		lblCorredor.setHorizontalTextPosition(JLabel.CENTER);
		getContentPane().add(lblCorredor);	
		
		lblPosicionesCategoria = new JLabel(getHTMLPosicionesYCategoria());
		lblPosicionesCategoria.setLocation(0, 285);
		lblPosicionesCategoria.setSize(244, 64);
		lblPosicionesCategoria.setFont(new Font("Roboto Slab", Font.PLAIN, 50));
		lblPosicionesCategoria.setForeground(colorTextos);
		lblPosicionesCategoria.setHorizontalAlignment(JLabel.CENTER);
		lblPosicionesCategoria.setHorizontalTextPosition(JLabel.CENTER);
		getContentPane().add(lblPosicionesCategoria);
		
		lblTiempo = new JLabel(getHTMLTiempo());
		lblTiempo.setLocation(0, 349);
		lblTiempo.setFont(new Font("LondonTwo", Font.TRUETYPE_FONT, 90));
		lblTiempo.setSize(440, 114);
		lblTiempo.setForeground(colorTextos);
		lblTiempo.setHorizontalAlignment(JLabel.CENTER);
		lblTiempo.setHorizontalTextPosition(JLabel.CENTER);
		getContentPane().add(lblTiempo);
		
		lblRitmo = new JLabel(getHTMLRitmo());
		lblRitmo.setLocation(0, 463);
		lblRitmo.setFont(new Font("Roboto Slab", Font.PLAIN, 70));
		lblRitmo.setSize(342, 89);
		lblRitmo.setForeground(colorTextos);
		lblRitmo.setHorizontalAlignment(JLabel.CENTER);
		lblRitmo.setHorizontalTextPosition(JLabel.CENTER);
		getContentPane().add(lblRitmo);
				
		//actualizaEtiquetaTitulo();
	}
	
	private String getHTMLTiempo() {
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='0'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='1200' height='70'>Tiempo: <b>" + Estatica.getInstance().getResultadoDorsal().getTIEMPO_FORMATEADO(true) + "</b></td>");
		sb.append("</tr>");	
		
		
		sb.append("</table>");
		sb.append("</html>");

		return sb.toString();
	}
	
	private String getHTMLRitmo() {
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='0'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='1200' height='70'>Ritmo: <b>" + Estatica.getInstance().getResultadoDorsal().getRITMO() + "</b></td>");
		sb.append("</tr>");	
		
		
		sb.append("</table>");
		sb.append("</html>");

		return sb.toString();
	}
	
	private String getHTMLPosicionesYCategoria() {
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='0'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='1'>" + "&nbsp;</td>");
		sb.append("<td align='center' width='600'>" + "<b>" + Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA()+"</b></td>");
		sb.append("<td align='center' width='600'>" + "POSICIÓN: <b>" + Estatica.getInstance().getResultadoDorsal().getPOS_CAT() + "</b></td>");
		sb.append("</tr>");	

		sb.append("</table>");
		sb.append("</html>");

		return sb.toString();
	}
	
	private String getHTMLCorredor(){
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='0'>");

		sb.append("<tr>");
		if (Estatica.getInstance().getResultadoDorsal().getCorredor().getSEXO().equals("MASC"))
			sb.append("<td align='center' width='1200' height='200'><b>" + Estatica.getInstance().getResultadoDorsal().getCorredor().getNOMBRE() + " " + Estatica.getInstance().getResultadoDorsal().getCorredor().getAPELLIDOS() + "</b></td>");
		else
			sb.append("<td align='center' width='1200' height='200'><b>" + Estatica.getInstance().getResultadoDorsal().getCorredor().getNOMBRE() + " " + Estatica.getInstance().getResultadoDorsal().getCorredor().getAPELLIDOS() + "</b></td>");
		
		sb.append("</tr>");	

		sb.append("</table>");
		sb.append("</html>");

		return sb.toString();
		
	}
	
	private String getHTMLDorsal(){
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='0'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='600'>Dorsal: <b>" + Estatica.getInstance().getResultadoDorsal().getCorredor().getDORSAL() + "</b></td>");
		sb.append("<td align='center' width='650'>Distancia: <b>" + Resultados.getDistancia(Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA()) + " KM</b></td>");
		sb.append("</tr>");	

		sb.append("</table>");
		sb.append("</html>");

		return sb.toString();
		
	}

	private void actualizaEtiquetaTitulo(){
		if (Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("CADETE")){
			lblTitulo.setText(" XVII CARRERA POPULAR SAN ROQUE");
		} else if (Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("INFANTIL")){
			lblTitulo.setText(" XVII CARRERA POPULAR SAN ROQUE");
		} else if (Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("ALEVIN")){
			lblTitulo.setText(" XVII CARRERA POPULAR SAN ROQUE");
		} else if (Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("BENJAMIN")){
			lblTitulo.setText(" XVII CARRERA POPULAR SAN ROQUE");
		} else if ( Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("VETERANO")
				|| Estatica.getInstance().getResultadoDorsal().getCorredor().getCATEGORIA().equals("SENIOR")){
			lblTitulo.setText(" XVII CARRERA POPULAR SAN ROQUE");
		}
	}
	
	private static final long serialVersionUID = 1L;
}
