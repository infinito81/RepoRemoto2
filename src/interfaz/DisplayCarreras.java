package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon; 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dominio.Eventos;
import persistencia.dao.model.TEventos;
import util.Constantes;
import util.Estatica;

public class DisplayCarreras extends JFrame {
	
	Thread hilo = null;
	JLabel lblCronometro = new JLabel("No iniciada");
	JLabel lblLlegados = new JLabel("");
	JLabel lblCategoria = null;
	int idEventoSeleccionado;
	TEventos evento = null;
	
	Estatica estatica;
	
	public DisplayCarreras(GraphicsConfiguration gc, int idEventoSeleccionado){
		super(gc);
		this.idEventoSeleccionado = idEventoSeleccionado;
		init();
	}
	
	public void init () {
		
		evento = Eventos.getEvento(idEventoSeleccionado); 
		
		try {
            FondoDisplayCarrera fondo = new FondoDisplayCarrera(ImageIO.read(new File(".\\res\\images\\fondo_btt_negro_med_2.jpg")));
            JPanel panel = (JPanel) this.getContentPane();
            panel.setBorder(fondo);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }		
		
		
		
		Color colorTextos = new Color (Constantes.CODIGO_COLOR_TEXTOS[0], Constantes.CODIGO_COLOR_TEXTOS[1], Constantes.CODIGO_COLOR_TEXTOS[2]);
		
		estatica = Estatica.getInstance();
		Icon logoClub = new ImageIcon(".\\res\\images\\popularia_contrario.png");
		lblCategoria = new JLabel(evento.getNombreEvento(), logoClub, JLabel.LEFT);
		lblCategoria.setForeground(colorTextos);
		Font fuenteCategoria = new Font("Calibri", Font.BOLD, 45);
		lblCategoria.setFont(fuenteCategoria);
		lblCategoria.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblCategoria, BorderLayout.NORTH);
				
		lblCronometro.setForeground(colorTextos);
		lblCronometro.setFont(new Font("Calibri", Font.BOLD, 350));
		lblCronometro.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblCronometro, BorderLayout.CENTER);
		

		lblLlegados.setFont(new Font("Calibri", Font.BOLD, 50));
		lblLlegados.setForeground(colorTextos);
		lblLlegados.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblLlegados, BorderLayout.SOUTH);
		
		preparaCronometro();
		//if (estatica.getCarreraSeleccionada()>=0){
			hilo.start();	
		//}
	
	}

	
	private void preparaCronometro(){
		hilo = new Thread(){
	        public void run(){
	        	try{
	            	
	                while(true){
	                	actualizaEtiquetaCategoria();
	                	long tiempoFinal = System.currentTimeMillis();
	                	lblCronometro.setText(formateaTiempo(estatica.getTiempoInicial(), tiempoFinal));
	                	if (estatica.getlLblLlegadosDorsal()!=null && estatica.getlLblLlegadosDorsal()[estatica.getCarreraSeleccionada()]!=null)
	                		lblLlegados.setText(estatica.getlLblLlegadosDorsal()[estatica.getCarreraSeleccionada()].getText());
	                	Thread.sleep(56);
	                }
	            } catch (InterruptedException ie){
	                
	            }
	        }
		};
	}
	
	
	private String formateaTiempo (long tiempoInicial, long tiempoFinalCorredor){
    	if (tiempoInicial == -1)
    		return "00:00.000";
    	
		Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(tiempoFinalCorredor - tiempoInicial);
    	
    	int horas = calendar.get(Calendar.HOUR_OF_DAY)-1;
    	int millis = calendar.get(Calendar.MILLISECOND);
    	int segundos = calendar.get(Calendar.SECOND);
    	int minutos = calendar.get(Calendar.MINUTE);
    	
    	String sMillis = millis < 10 ? "0" + millis : millis + "";
    	sMillis = millis < 100 ? "0" + sMillis : sMillis;
    	String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
    	String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";  
    	String sHoras = horas < 10 ? "0" + horas : horas + "";
    	
    	return (sHoras + ":" + sMinutos + ":" + sSegundos);		
	}
	
	private void actualizaEtiquetaCategoria () {
		
		
		
		StringBuilder sb = new StringBuilder(128);
		
		
		
		sb.append("<html>");
		sb.append("<table border='0'>");
		sb.append("<tr>");
		sb.append("<td align='center'>&nbsp;&nbsp;&nbsp;" + evento.getNombreEvento() + "</td>");
		sb.append("</tr>");
		
		if (estatica.getCarreraSeleccionada()>-1){
			/*sb.append("<tr>");
			sb.append("<td align='center'>Carrera: ABSOLUTA</td>");
			sb.append("</tr>");*/			
		}
		
		sb.append("</table>");
		sb.append("</html>");		
		
		lblCategoria.setText(sb.toString());
	}
	
	private static final long serialVersionUID = 1L;
}
