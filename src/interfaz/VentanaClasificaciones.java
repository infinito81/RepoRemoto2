package interfaz;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TResultados;
import util.Constantes;
import util.EnviosHTTP;
import util.Estatica;
import dominio.Carreras;
import dominio.Resultados;

public class VentanaClasificaciones extends JFrame {

	private static final long serialVersionUID = 1L;
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	JLabel lLblCronometro [] = null;
	JPanel paneles [] = null;
	JLabel lLblCategorias [] = null;
	JButton btnUpload = new JButton("UPLOAD");
	
	JLabel lLblResultadosMasculinos [] = new JLabel [8]; 
	JLabel lLblResultadosFemeninos [] = new JLabel [8];
	JLabel lLblResultados [] = null; 
	
	ArrayList<TResultados> lResultados = null;
	int idEvento;
		
	public VentanaClasificaciones(int idEvento) {
		this.idEvento = idEvento;
		getContentPane().setLayout(null);
		
		HashMap<String, TCategorias> categorias = Carreras.recuperaCategoriasEventoBd(idEvento+"");
		paneles = new JPanel[categorias.size()];
		lLblCronometro = new JLabel[categorias.size()];
		lLblCategorias = new JLabel[categorias.size()];
		lLblResultados = new JLabel[categorias.size()];
		
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 1180, 650);
		getContentPane().add(tabbedPane);
		
        setBounds(10, 10, 1200, 730);
		this.setResizable(false);
		this.setTitle("POPULARIA");
				
		for (int i=0; i<paneles.length; i++){
			paneles[i] = new JPanel();
			paneles[i].setLayout(null);			
			lLblCategorias [i] = new JLabel(); 
			lLblResultados [i] = new JLabel();
			lLblCategorias[i].setText(categorias.get((i+1) + "").getDescCategoria());
		}
		
		
		lResultados = Resultados.getResultadosCarreras(idEvento);
		/*Resultados.actualizaPosicionesCorredoresCarrera(lResultados, Constantes.ID_CARRERA_ABSOLUTA);
		Resultados.actualizaPosicionesCorredoresCarrera(lResultados, Constantes.ID_CARRERA_ALEVIN);
		Resultados.actualizaPosicionesCorredoresCarrera(lResultados, Constantes.ID_CARRERA_BENJAMIN);
		Resultados.actualizaPosicionesCorredoresCarrera(lResultados, Constantes.ID_CARRERA_CADETE);
		Resultados.actualizaPosicionesCorredoresCarrera(lResultados, Constantes.ID_CARRERA_INFANTIL);
		Resultados.actualizaRitmos(lResultados);
		*/
		for (int i=0; i<paneles.length; i++){
			tabbedPane.add(paneles[i], lLblCategorias[i].getText());
			lLblCategorias[i].setFont(new Font("Calibri", Font.BOLD, 20));
			lLblCategorias[i].setHorizontalAlignment(SwingConstants.CENTER);
			lLblCategorias[i].setBounds(550, 20, 100, 20);
			paneles[i].add(lLblCategorias[i]);
			
			lLblResultados[i].setHorizontalAlignment(SwingConstants.CENTER);
			lLblResultados[i].setFont(new Font("Calibri", Font.BOLD, 14));
			lLblResultados[i].setText("");
			lLblResultados[i].setBounds(20, 50, 550, 560);
			lLblResultados[i].setVerticalTextPosition(SwingConstants.TOP);
			paneles[i].add(lLblResultados[i]);
			
			/*lLblResultadosFemeninos[i].setHorizontalAlignment(SwingConstants.CENTER);
			lLblResultadosFemeninos[i].setFont(new Font("Calibri", Font.BOLD, 14));
			lLblResultadosFemeninos[i].setText("");
			lLblResultadosFemeninos[i].setBounds(590, 50, 550, 560);
			lLblResultadosFemeninos[i].setVerticalTextPosition(SwingConstants.TOP);
			paneles[i].add(lLblResultadosFemeninos[i]);*/
			
			actualizaEtiquetasResultados(i);
		}
		

		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uploadResultados();
			}
		});
		btnUpload.setBounds(73, 660, 183, 40);
		getContentPane().add(btnUpload);		
	}
	
	protected void uploadResultados() {
		for (TResultados resultado : lResultados){
			
			StringBuffer stb = new StringBuffer();
			stb.append("http://atletismonavalcan.hol.es/resultados_get.php?identificador=");
			stb.append(resultado.getID_RESULTADO());
			stb.append("&carrera=");
			stb.append(resultado.getID_CARRERA());
			stb.append("&dorsal=");
			stb.append(resultado.getCorredor().getDORSAL());
			stb.append("&tiempo=");
			stb.append(resultado.getTiempoFormateado());
			
			System.out.println(stb.toString());
			try {
				String respuesta = EnviosHTTP.sendGet(stb.toString());
				if (respuesta.contains("CORRECTO")) respuesta = "CORRECTO";
				System.out.println(resultado.getID_RESULTADO() + ": " + respuesta);
			} catch (Exception e) {
				System.out.println("Excepcion al actualizar resultado " + resultado.getID_RESULTADO());
			}
		}		
	}

	private String actualizaEtiquetasResultados (int pestanya) {
		
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='1'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='60'>" + "POS" + "</td>");
		sb.append("<td align='center' width='50'>" + "DORSAL" + "</td>");
		sb.append("<td align='center' width='370'>" + "NOMBRE Y APELLIDOS" + "</td>");
		sb.append("<td align='center' width='70'>" + "TIEMPO" + "</td>");
		sb.append("</tr>");		
		
		
		for( TResultados resultado : lResultados ){
			if (resultado.getCorredor().getIdCategoria() == (pestanya + 1)) {

						sb.append("<tr>");
						sb.append("<td align='center' width='60'>" + resultado.getPOS_CAT() + "</td>");
						sb.append("<td align='center' width='50'>" + resultado.getCorredor().getDORSAL() + "</td>");
						sb.append("<td align='center' width='370'>" + resultado.getCorredor().getNOMBRE() + " " + resultado.getCorredor().getAPELLIDOS() + "</td>");
						sb.append("<td align='center' width='70'>" + resultado.getTIEMPO_FORMATEADO(true) + "</td>");
						sb.append("</tr>");					

			}
		}
		sb.append("</table>");
		sb.append("</html>");

		lLblResultados[pestanya].setText(sb.toString());


		/*
		sb = new StringBuilder(128);
		posicion = 0;
		
		sb.append("<html>");
		sb.append("<table border='1'>");

		sb.append("<tr>");
		sb.append("<td align='center' width='60'>" + "POS" + "</td>");
		sb.append("<td align='center' width='50'>" + "DORSAL" + "</td>");
		sb.append("<td align='center' width='370'>" + "NOMBRE Y APELLIDOS" + "</td>");
		sb.append("<td align='center' width='70'>" + "TIEMPO" + "</td>");
		sb.append("</tr>");		
		
		
		for( TResultados resultado : lResultados ){
			if (posicion<=18){
				if ( (pestanya == Constantes.PESTANYA_CLASIF_BENJAMIN && resultado.getID_CARRERA() == Constantes.ID_CARRERA_BENJAMIN) 
						|| (pestanya == Constantes.PESTANYA_CLASIF_ALEVIN && resultado.getID_CARRERA() == Constantes.ID_CARRERA_ALEVIN)
							|| (pestanya == Constantes.PESTANYA_CLASIF_INFANTIL && resultado.getID_CARRERA() == Constantes.ID_CARRERA_INFANTIL)
								|| (pestanya == Constantes.PESTANYA_CLASIF_CADETE && resultado.getID_CARRERA() == Constantes.ID_CARRERA_CADETE)	
									|| (pestanya == Constantes.PESTANYA_CLASIF_ABSOLUTA && resultado.getID_CARRERA() == Constantes.ID_CARRERA_ABSOLUTA)
									|| (pestanya == Constantes.PESTANYA_CLASIF_SENIOR && resultado.getID_CARRERA() == Constantes.ID_CARRERA_ABSOLUTA && resultado.getCorredor().getCATEGORIA().equals("SENIOR"))
									|| (pestanya == Constantes.PESTANYA_CLASIF_VETERANO && resultado.getID_CARRERA() == Constantes.ID_CARRERA_ABSOLUTA && resultado.getCorredor().getCATEGORIA().equals("VETERANO"))
									|| (pestanya == Constantes.PESTANYA_CLASIF_LOCAL && resultado.getID_CARRERA() == Constantes.ID_CARRERA_ABSOLUTA && resultado.getCorredor().isCorredorLocal())
						){
					if (resultado.getCorredor().getSEXO().equals("FEME")){
						sb.append("<tr>");
						sb.append("<td align='center' width='60'>" + (++posicion) + "</td>");
						sb.append("<td align='center' width='50'>" + resultado.getCorredor().getDORSAL() + "</td>");
						sb.append("<td align='center' width='370'>" + resultado.getCorredor().getNOMBRE() + " " + resultado.getCorredor().getAPELLIDOS() + "</td>");
						sb.append("<td align='center' width='70'>" + resultado.getTiempoFormateado() + "</td>");
						sb.append("</tr>");					
					}
				}
			}
			
			//Para todas, aunque no pintemos
			
		}
		sb.append("</table>");
		sb.append("</html>");
		
		lLblResultadosFemeninos[pestanya].setText(sb.toString());
		*/
		return sb.toString();
	}
}

