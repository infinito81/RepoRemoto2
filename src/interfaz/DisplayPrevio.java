package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import util.Constantes;

public class DisplayPrevio extends JFrame {
	
	
	JLabel lblCarreras = null;
	JLabel lblTitulo = null;
	
	public DisplayPrevio(GraphicsConfiguration gc){
		super(gc);
		init();
	}
	
	public void init () {
		
		getContentPane().setBackground(new Color(Constantes.CODIGO_COLOR_FONDO[0], Constantes.CODIGO_COLOR_FONDO[1], Constantes.CODIGO_COLOR_FONDO[2]));
		
		Color colorTextos = new Color (Constantes.CODIGO_COLOR_TEXTOS[0], Constantes.CODIGO_COLOR_TEXTOS[1], Constantes.CODIGO_COLOR_TEXTOS[2]);
		
		/*
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet s = null;
		

		try {
			s = loadStyleSheet(new FileInputStream("./res/resultados/assets/css/style.css"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		kit.setStyleSheet(s);
		*/
		
		Icon logoClub = new ImageIcon("C:/eclipse/workspace/Popularia/icons/logo_club.jpg");
		lblTitulo = new JLabel("  XVII CARRERA POPULAR SAN ROQUE", logoClub, JLabel.LEFT);
		lblTitulo.setForeground(colorTextos);
		Font fuenteCategoria = new Font("Roboto Slab", Font.BOLD, 55);
		lblTitulo.setFont(fuenteCategoria);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblTitulo, BorderLayout.NORTH);
				
		lblCarreras = new JLabel("");
		lblCarreras.setForeground(colorTextos);
		lblCarreras.setFont(new Font("Roboto Slab", Font.PLAIN, 60));
		lblCarreras.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblCarreras, BorderLayout.CENTER);
	
		actualizaEtiquetaCarreras();
	}


	
	private void actualizaEtiquetaCarreras () {
		StringBuilder sb = new StringBuilder(128);
		
		
		sb.append("<html>");
		//sb.append("<link href=\"../../res/assets/css/animate.css\" rel=\"stylesheet\">    <link href=\"../../res/assets/css/plugins.css\" rel=\"stylesheet\">    <link href=\"../../res/assets/css/style.css\" rel=\"stylesheet\"> <link href=\"../../res/assets/font-awesome-4.1.0/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">    <link href=\"../../assets/css/pe-icons.css\" rel=\"stylesheet\">");
		sb.append("<table border='1'>");
		sb.append("<tr>");
		sb.append("<th align='center'  width='1000'>CATEGORÍA</td>");
		sb.append("<th align='center' width='300'>HORA</td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>CADETE</td>");
		sb.append("<td align='center' width='300'>19:30</td>");
		sb.append("</tr>");			

		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>INFANTIL</td>");
		sb.append("<td align='center' width='300'>19:45</td>");
		sb.append("</tr>");			
		
		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>ALEVÍN</td>");
		sb.append("<td align='center' width='300'>20:00</td>");
		sb.append("</tr>");			
		
		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>BENJAMÍN</td>");
		sb.append("<td align='center' width='300'>20:10</td>");
		sb.append("</tr>");			
		
		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>PREBENJAMÍN</td>");
		sb.append("<td align='center' width='300'>20:20</td>");
		sb.append("</tr>");		
		
		sb.append("<tr>");
		sb.append("<td align='center' width='1000'>ABSOLUTA</td>");
		sb.append("<td align='center' width='300'>20:30</td>");
		sb.append("</tr>");			
		
		sb.append("</table>");
		sb.append("</html>");	
		
		System.out.println(sb.toString());
		
		lblCarreras.setText(sb.toString());
	}
	
   public static StyleSheet loadStyleSheet(InputStream is) throws IOException
   {
      StyleSheet s = new StyleSheet();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      s.loadRules(br, null);
      br.close();
   
      return s;
   }
	
	private static final long serialVersionUID = 1L;
}
