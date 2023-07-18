package interfaz;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import dominio.Carreras;
import dominio.Eventos;
import estaticos.Log;
import persistencia.dao.model.TCarreras;
import persistencia.dao.model.TEventos;

import javax.swing.JComboBox;




public class VentanaInicio extends JFrame {
	
	private static Logger log = Log.getLogger(VentanaInicio.class);
	private int eventoSeleccionado;
	JComboBox<String> comboEventos;
	JComboBox<String> comboCarreras;
	
	private static final long serialVersionUID = 1L;

	public VentanaInicio() {
		getContentPane().setLayout(null);
		
		JButton btnIncripciones = new JButton("INSCRIPCIONES");
		btnIncripciones.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnIncripciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaInscripciones ventanaInscripciones = new VentanaInscripciones();
				ventanaInscripciones.setVisible(true);
			}
		});
		btnIncripciones.setBounds(50, 64, 185, 67);
		getContentPane().add(btnIncripciones);
		
		JButton btnCarreras = new JButton("CARRERAS");
		btnCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemSeleccionado = (String)comboEventos.getSelectedItem();
				int idEventoSeleccionado = Integer.parseInt(itemSeleccionado.substring(0, itemSeleccionado.indexOf('-')));
				
				String carreraSeleccionada = (String)comboCarreras.getSelectedItem();
				int idCarreraSeleccionada = Integer.parseInt(carreraSeleccionada.substring(0, carreraSeleccionada.indexOf('-')));
				VentanaCarrera ventanaCarrera = new VentanaCarrera(idEventoSeleccionado, idCarreraSeleccionada);
				//ventanaCarrera.setB
				ventanaCarrera.setVisible(true);
				
				
			}
		});
		btnCarreras.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnCarreras.setBounds(283, 64, 185, 67);
		getContentPane().add(btnCarreras);
		
		JButton btnDisplays = new JButton("DISPLAYS");
		btnDisplays.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnDisplays.setBounds(50, 195, 185, 67);
		btnDisplays.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemSeleccionado = (String)comboEventos.getSelectedItem();
				int idEventoSeleccionado = Integer.parseInt(itemSeleccionado.substring(0, itemSeleccionado.indexOf('-')));
				VentanaDisplays ventanaDisplays = new VentanaDisplays(idEventoSeleccionado);
				ventanaDisplays.setVisible(true);
				
			}
		});		
		getContentPane().add(btnDisplays);
		
		JButton btnClasificaciones = new JButton("CLASIFICACIONES");
		btnClasificaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemSeleccionado = (String)comboEventos.getSelectedItem();
				int idEventoSeleccionado = Integer.parseInt(itemSeleccionado.substring(0, itemSeleccionado.indexOf('-')));
				VentanaClasificaciones ventanaClasificaciones = new VentanaClasificaciones(idEventoSeleccionado);
				ventanaClasificaciones.setVisible(true);
			}
		});
		btnClasificaciones.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnClasificaciones.setBounds(283, 195, 185, 67);
		getContentPane().add(btnClasificaciones);
		
		JButton btnAdministracion = new JButton("ADMINISTRACION");
		btnAdministracion.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnAdministracion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//String itemSeleccionado = (String)comboEventos.getSelectedItem();
				//int idEventoSeleccionado = Integer.parseInt(itemSeleccionado.substring(0, itemSeleccionado.indexOf('-')));
				VentanaAdministracion ventanaAdministracion = new VentanaAdministracion(0);
				ventanaAdministracion.setVisible(true);
			}
		});
		btnAdministracion.setBounds(50, 309, 185, 67);
		getContentPane().add(btnAdministracion);
		
		comboEventos = new JComboBox<String>();
		comboEventos.setBounds(38, 11, 430, 20);
		getContentPane().add(comboEventos);
		rellenarComboEventos(comboEventos);
		
		JButton btnSalir = new JButton("SALIR POPULARIA");
		btnSalir.setFont(new Font("Calibri", Font.PLAIN, 15));
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(getContentPane(), "DESEA SALIR SEGURO???") == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		btnSalir.setBounds(283, 309, 185, 67);
		getContentPane().add(btnSalir);
		
		comboCarreras = new JComboBox<String>();
		comboCarreras.setBounds(317, 142, 115, 20);
		rellenarComboCarreras(comboCarreras);
		getContentPane().add(comboCarreras);
		
	
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(250, 250, 500, 450);
		this.setResizable(false);
		this.setTitle("POPULARIA");
		
		log.debug("Ventana principal inicializada");
		
	}

	private void rellenarComboCarreras(JComboBox comboCarreras) {
		String stringCarreras = Eventos.dameCarrerasServidor(eventoSeleccionado);
		System.out.println(stringCarreras);
		
		StringTokenizer stCarreras = new StringTokenizer(stringCarreras, ";");
		
		while (stCarreras.hasMoreTokens()) {
			TCarreras carrera = new TCarreras();
			
			String idEvento = stCarreras.nextToken();
			String descEvento = stCarreras.nextToken();
			String fechaCarrera = stCarreras.nextToken();
			String idCarrera = stCarreras.nextToken();
			String horaCarrera = stCarreras.nextToken();
			String descCarrera = stCarreras.nextToken();
			String marcaInicio = stCarreras.nextToken();
			
            carrera.setID_EVENTO(Integer.parseInt(idEvento));
            carrera.setID_CARRERA(Integer.parseInt(idCarrera));
            carrera.setHORA(horaCarrera);
            carrera.setCATEGORIA(descCarrera);
            if (marcaInicio!=null && !marcaInicio.equalsIgnoreCase("no"))
            	carrera.setINICIO(Long.parseLong(marcaInicio));
            
			comboCarreras.addItem(idCarrera + "-" + descCarrera);
			
			Carreras.guardarCarrera(carrera);
		}
		
		System.out.println("Fin rellena combo carreras.");
	}

	private void rellenarComboEventos(JComboBox comboEventos) {
		ArrayList<TEventos> lEventos = Eventos.getTodosEventos();
		
		for (TEventos tEventos : lEventos) {
			comboEventos.addItem(tEventos.getIdEvento() + "-" + tEventos.getFechaEvento() + " - " + tEventos.getNombreEvento());
		}
		
	}
	
	/*private void rellenaComboCarreras(JComboBox comboCarreras) {
		String 
	}*/
}
