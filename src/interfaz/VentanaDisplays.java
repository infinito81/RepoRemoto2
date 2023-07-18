package interfaz;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TResultados;
import util.Estatica;
import dominio.Carreras;
import dominio.Resultados;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class VentanaDisplays extends JFrame {
	
	private static final long serialVersionUID = 1L;
	DisplayCarreras displayCarreras = null;
	DisplayPrevio displayPrevio = null;
	DisplayResultadoCorredor displayResultadoDorsal = null;
	DisplayClasificaciones displayClasificaciones = null;
	private JTextField txfDorsal;
	JComboBox <String> cmbCategorias;
	JComboBox <String> cmbSexos;
	int idEventoSeleccionado;

	public VentanaDisplays(int idEvento) {
		idEventoSeleccionado = idEvento;
		getContentPane().setLayout(null);
		setBounds(250, 250, 500, 500);
		this.setResizable(false);
		this.setTitle("POPULARIA");		
		
		JButton btnPrevio = new JButton("PREVIO");
		btnPrevio.setBounds(49, 53, 352, 64);
		getContentPane().add(btnPrevio);
		btnPrevio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gs = ge.getScreenDevices();
								
				int indexPantalla = gs.length > 1 ? 1 : 0;
				
				displayPrevio = new DisplayPrevio(gs[indexPantalla].getDefaultConfiguration());
				displayPrevio.setExtendedState(JFrame.MAXIMIZED_BOTH);
				displayPrevio.setUndecorated(true);
				displayPrevio.setVisible(true);
			}
		});
				
		
		JButton btnCarreras = new JButton("CARRERAS");
		btnCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gs = ge.getScreenDevices();
								
				int indexPantalla = gs.length > 1 ? 1 : 0;
				
				displayCarreras = new DisplayCarreras(gs[indexPantalla].getDefaultConfiguration(), idEventoSeleccionado);
				displayCarreras.setExtendedState(JFrame.MAXIMIZED_BOTH);
				displayCarreras.setUndecorated(true);
				displayCarreras.setVisible(true);
				//DisplayCarreras3 dp3 = new DisplayCarreras3(gs[indexPantalla].getDefaultConfiguration());
				
			}
		});
		btnCarreras.setBounds(49, 161, 352, 64);
		getContentPane().add(btnCarreras);
		
		JButton btnPostCarrera = new JButton("POST CARRERA");
		btnPostCarrera.setBounds(170, 269, 231, 64);
		btnPostCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gs = ge.getScreenDevices();
								
				int indexPantalla = gs.length > 1 ? 1 : 0;
				
				displayClasificaciones = new DisplayClasificaciones(gs[indexPantalla].getDefaultConfiguration(), idEventoSeleccionado);
				displayClasificaciones.setExtendedState(JFrame.MAXIMIZED_BOTH);
				displayClasificaciones.setUndecorated(true);
				displayClasificaciones.setVisible(true);
			}
		});
		getContentPane().add(btnPostCarrera);
		
		JButton btnCerrarPrevio = new JButton("");
		btnCerrarPrevio.setIcon(new ImageIcon(".\\\\res\\\\icons\\\\cerrar-icono.png"));
		btnCerrarPrevio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (displayPrevio!=null){
					displayPrevio.setVisible(false);
					displayPrevio.dispose();
				}
			}
		});
		btnCerrarPrevio.setBounds(411, 53, 64, 62);
		getContentPane().add(btnCerrarPrevio);
		
		JButton btnCerrarCarreras = new JButton("");
		btnCerrarCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (displayCarreras!=null){
					displayCarreras.setVisible(false);
					displayCarreras.dispose();
				}
			}
		});
		btnCerrarCarreras.setIcon(new ImageIcon(".\\\\res\\\\icons\\\\cerrar-icono.png"));
		btnCerrarCarreras.setBounds(411, 161, 64, 62);
		getContentPane().add(btnCerrarCarreras);
		
		JButton btnCerrarPostCarrera = new JButton("");
		btnCerrarPostCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (displayClasificaciones!=null){
					displayClasificaciones.setVisible(false);
					displayClasificaciones.dispose();
				}
			}
		});
		btnCerrarPostCarrera.setIcon(new ImageIcon(".\\\\res\\\\icons\\\\cerrar-icono.png"));
		btnCerrarPostCarrera.setBounds(411, 269, 64, 62);
		getContentPane().add(btnCerrarPostCarrera);
		
		
		JButton btnPruebaInternetExplorer = new JButton("PRUEBA INTERNET EXPLORER");
		btnPruebaInternetExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				}
			});
		
		JButton btnResultadoDorsal = new JButton("RESULTADO DORSAL");
		btnResultadoDorsal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean dorsalValido = false;
				if (!txfDorsal.getText().equals("")){
					try {
						int dorsal = Integer.parseInt(txfDorsal.getText());
							/*TResultados resultado = Resultados.getResultadoCompletoCorredor(idEvento,dorsal + "");
							
							if (resultado!=null){
								Estatica.getInstance().setResultadoDorsal(resultado);
								GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
								GraphicsDevice[] gs = ge.getScreenDevices();
												
								int indexPantalla = gs.length > 1 ? 1 : 0;
								
								displayResultadoDorsal = new DisplayResultadoCorredor(gs[indexPantalla].getDefaultConfiguration());
								displayResultadoDorsal.setExtendedState(JFrame.MAXIMIZED_BOTH);
								displayResultadoDorsal.setUndecorated(true);
								displayResultadoDorsal.setVisible(true);	
								
								dorsalValido = true;
							}*/
						ProcessBuilder pb = new ProcessBuilder();
						try {
							String nombrePaginaResultado = "C:\\Users\\ProBook\\git\\popularia_java_ee\\Popularia\\res\\resultados\\resultado_" + dorsal + "_" +  idEvento+ ".html";
							pb.command("cmd.exe", "/c","start chrome.exe --kiosk --incognito " + nombrePaginaResultado);
							Process process = pb.start();
							
							
							
							 BufferedReader reader =
					                  new BufferedReader(new InputStreamReader(process.getInputStream()));

				            String line;
				            while ((line = reader.readLine()) != null) {
				                System.out.println(line);
				            }
				            
				            int exitCode = process.waitFor();
				            System.out.println("\nExited with error code : " + exitCode);
				            
				            //Thread.sleep(5000);
				            reader.close();
				            //process.destroy();
					            
					           

				           

			 			} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						

					} catch (Exception e) {
						dorsalValido = false;
					}
				}
				
				//if (!dorsalValido) JOptionPane.showMessageDialog(getContentPane(), "Dorsal no válido", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnResultadoDorsal.setBounds(170, 375, 231, 64);
		getContentPane().add(btnResultadoDorsal);
		
		JButton btnCerrarResultadoDorsal = new JButton("");
		btnCerrarResultadoDorsal.setIcon(new ImageIcon(".\\res\\icons\\cerrar-icono.png"));
		btnCerrarResultadoDorsal.setBounds(411, 377, 64, 62);
		btnCerrarResultadoDorsal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	            try {
					ProcessBuilder pbMataChromes = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /IM chrome.exe");
					Process pMataChromes = pbMataChromes.start();
					
					String line;
					BufferedReader reader = new BufferedReader(new InputStreamReader(pMataChromes.getInputStream()));
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
					pMataChromes.destroy();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		getContentPane().add(btnCerrarResultadoDorsal);
		
		txfDorsal = new JTextField();
		txfDorsal.setBounds(49, 375, 111, 64);
		getContentPane().add(txfDorsal);
		txfDorsal.setColumns(10);
		
		cmbCategorias = new JComboBox<String>();
		cmbCategorias.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Estatica.getInstance().setDisplayCategoriaSeleccionada(cmbCategorias.getSelectedItem().toString().substring(0,1));
			}
		});
		
		cmbCategorias.setBounds(49, 269, 111, 20);
		rellenaCategorias();
		getContentPane().add(cmbCategorias);
		
		JToggleButton btnAutoRun = new JToggleButton("AutoRun");
		btnAutoRun.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Estatica.getInstance().setClasificacionesAutoRunActivado(btnAutoRun.isSelected());
				Estatica.getInstance().setDisplayCategoriaSeleccionada("");
			}
		});
		btnAutoRun.setBounds(49, 310, 111, 23);
		getContentPane().add(btnAutoRun);
		

		btnPruebaInternetExplorer.setBounds(77, 11, 324, 31);
		getContentPane().add(btnPruebaInternetExplorer);
	}

	private void rellenaCategorias() {
		HashMap<String, TCategorias> hmCategorias;
		try {
			String datosCarrera = Carreras.getDatosEvento(idEventoSeleccionado);
			hmCategorias = Carreras.procesaDatosEvento(datosCarrera, idEventoSeleccionado, 1);
		} catch (Exception e) {
			System.err.println("No se recuperan las categorías del servidor");
			hmCategorias = Carreras.recuperaCategoriasEventoBd(idEventoSeleccionado+"");
		}
		
		for (Map.Entry<String, TCategorias> entry : hmCategorias.entrySet()) {
			TCategorias categoria = entry.getValue();
			cmbCategorias.addItem(categoria.getIdCategoria() + " - " + categoria.getDescCategoria());
			//comboCategorias.add(categoria.getIdCategoria() + " - " + categoria.getDescCategoria());
		}
	}

}
