package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import dominio.Carreras;
import dominio.Corredores;
import dominio.Eventos;
import dominio.Lecturas;
import dominio.Resultados;
import dominio.ResultadosAsincrona;
import estaticos.Cronometro;
import estaticos.Dorsales;
import estaticos.Log;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import persistencia.dao.model.EstadoCarrera;
import persistencia.dao.model.TCarreras;
import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TCorredores;
import persistencia.dao.model.TEventos;
import persistencia.dao.model.TLecturas;
import util.Constantes;
import util.EnviosHTTP;
import util.Estatica;
import util.FormatoFecha;

public class VentanaCarrera extends JFrame{

	private static final long serialVersionUID = 1L;
	private static Logger log = Log.getLogger(VentanaCarrera.class);
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	JLabel lLblCronometro = new JLabel();
	Thread hilo = null;
	long tiempoInicial;
	long tiempoFinal;
	boolean hiloCorriendo = false;
	boolean hilosCorriendo = false;
	JLabel lLblLlegadosDorsal = new JLabel();
	JTextField lTxtDorsal = new JTextField();
	JLabel lLblCategorias = new JLabel();
	
	//lectores seriales
	Enumeration puertos_libres = null;
	CommPortIdentifier port = null;
	SerialPort puerto_ser = null;
	InputStream in1 = null;
	InputStream in2 = null;
	InputStream in3 = null;
	InputStream in4 = null;
	
	OutputStream out1 = null; 
	OutputStream out2 = null; 
	OutputStream out3 = null;
	OutputStream out4 = null;
	String mensajeALectores = null; //Si es distinto de NULL es que hay que mandar mensaje a lectores.
	
	
	String puerto1 = "";
	String puerto2 = "";
	String puerto3 = "";
	String puerto4 = "";
	Thread timer;
	Thread timer2;
	Thread timer3;
	Thread timer4;
	
	Thread hiloSondaPeticionLectura;
	
	/*Thread timerWrite;
	Thread timerWrite2;
	Thread timerWrite3;
	Thread timerWrite4;*/
	
	//boolean hiloCorriendo;

	JButton lBtnIniciar= new JButton();
	JButton lBtnParar = new JButton();
	
	int idEventoSeleccionado;
	int idCarreraSeleccionada;
	
	Cronometro cronometro = null;
	
	TEventos evento;
	
	TCorredores ultimosCorredores [] = new TCorredores[3]; //indice i: carrera ;;; indice j: corredor
	private JPanel paneles_1;
	HashMap <String, TCategorias> hmCategorias = null;
	EstadoCarrera estadoCarrera;
	JTextArea textArea;
	JTextArea textAreaDerecho;
	JCheckBox interruptorLectores;
	JComboBox<String> comboCategorias;
	DisplayCarreras displayCarreras = null;
	
	JTextField txfDorsal = new JTextField();
	
	public VentanaCarrera(int idEventoSeleccionado, int idCarreraSeleccionada) {
		System.out.println("Gestión Evento: " + idEventoSeleccionado);
		this.idEventoSeleccionado = idEventoSeleccionado;
		this.idCarreraSeleccionada = idCarreraSeleccionada;
		
		evento = Eventos.getEvento(idEventoSeleccionado); 
		
		getContentPane().setLayout(null);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				/*if (JOptionPane.showConfirmDialog(getContentPane(), "Asegúrese de parar el cronómetro de la carrera antes de cambiar a otra", "ALERTA", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
					Estatica.getInstance().setCarreraSeleccionada(tabbedPane.getSelectedIndex());
					Estatica.getInstance().setDescCarreraSeleccionada(lLblCategorias.getText());
				} else {
					System.out.println("Cambio de pestaña cancelado");
					tabbedPane.setSelectedIndex(Estatica.getInstance().getCarreraSeleccionada());
				}*/
			}
		});
		
		Dorsales.cargaDorsales();
		
		
		try {
			String datosCarrera = Carreras.getDatosEvento(idEventoSeleccionado);
			hmCategorias = Carreras.procesaDatosEvento(datosCarrera, idEventoSeleccionado, idCarreraSeleccionada);
		} catch (Exception e) {
			System.err.println("No se recuperan las categorías del servidor");
			hmCategorias = Carreras.recuperaCategoriasEventoBd(idEventoSeleccionado+"");
		}		
				
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 1340, 550);
		getContentPane().add(tabbedPane);
        setBounds(0, 0, 1366, 581);
		this.setResizable(false);
		this.setTitle("POPULARIA");
		
		JLabel lLblDorsal =  new JLabel();
				

		paneles_1 = new JPanel();
		paneles_1.setLayout(null);
		lLblCategorias = new JLabel(); 
		lLblCategorias.setHorizontalAlignment(SwingConstants.CENTER);
		lBtnIniciar= new JButton("INICIAR");
		lBtnParar = new JButton("PARAR");
		lLblCronometro = new JLabel();
		lLblDorsal = new JLabel(); 
		lTxtDorsal = new JTextField();
		
		
		Estatica.getInstance().setTiempoInicial(-1);
		Estatica.getInstance().setCarreraSeleccionada(idCarreraSeleccionada);
		Estatica.getInstance().setDescCarreraSeleccionada(evento.getNombreEvento());
		
		lLblCategorias.setText(evento.getNombreEvento());
		
		//Lector SERIAL
		//prepararLectoresSerie();
		
		//Cronometro
		boolean cronometroArrancado = arrancaCronometroSiProcede(idCarreraSeleccionada);
		
		if (cronometroArrancado) {
			//arrancarLectoresUHF();
			
			recuperaSituacionCarrera();

			
		}
	

		
		//for (int i=0; i<paneles.length; i++){
		tabbedPane.add(paneles_1, lLblCategorias.getText());
		lLblCategorias.setFont(new Font("Calibri", Font.BOLD, 20));
		lLblCategorias.setBounds(10, 11, 525, 20);
		paneles_1.add(lLblCategorias);
		
		lBtnIniciar.setBounds(36, 47, 120, 55);
		paneles_1.add(lBtnIniciar);
		
		lBtnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//CRONOMETRO
				arrancaCronometroNuevo();
				
				lBtnParar.setEnabled(true);
				lBtnIniciar.setEnabled(false);
				
				arrancarLectoresUHF();
				
				recuperaSituacionCarrera();
				

			}
		
		
		});
			
		lBtnParar.setBounds(190, 47, 120, 55);
		paneles_1.add(lBtnParar);
		lBtnParar.setEnabled(false);
		lBtnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//preparaCronometro();
				//CRONOMETRO
				hilo.interrupt();					
				hiloCorriendo = false;
				lBtnParar.setEnabled(false);
				lBtnIniciar.setEnabled(true);
				
				
				//PARO LECTORES SERIE
				hilosCorriendo = false;					
			}
		});
		
		lLblCronometro.setHorizontalAlignment(SwingConstants.CENTER);
		lLblCronometro.setFont(new Font("LCD Display Grid", Font.BOLD, 20));
		lLblCronometro.setText("00:00:000");
		lLblCronometro.setBounds(337, 42, 171, 55);
		paneles_1.add(lLblCronometro);

		lLblDorsal.setText("DORSAL: ");
		lLblDorsal.setFont(new Font("Calibri", Font.BOLD, 18));
		lLblDorsal.setBounds(36, 119, 120, 55);
		paneles_1.add(lLblDorsal);

		lTxtDorsal.setBounds(152, 122, 120, 41);
		lTxtDorsal.setFont(new Font("Calibri", Font.BOLD, 18));
		lTxtDorsal.setHorizontalAlignment(SwingConstants.CENTER);			
		paneles_1.add(lTxtDorsal);
		
		textArea = new JTextArea();
		textArea.setBounds(36, 235, 472, 187);
		textArea.setBackground(Color.black);
		textArea.setForeground(Color.white);

		paneles_1.add(textArea);
		
		JButton btnLecturaDestiempo = new JButton("Lectura destiempo");
		btnLecturaDestiempo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaInsercionLectura ventanaInsercionLectura = new VentanaInsercionLectura();
				ventanaInsercionLectura.setMillisegundosInicioCarrera(tiempoInicial);
				ventanaInsercionLectura.setIdEventoSeleccionado(idEventoSeleccionado);
				ventanaInsercionLectura.setCarreraSeleccionada(1);
				ventanaInsercionLectura.setVisible(true);
			}
		});
		btnLecturaDestiempo.setBounds(64, 185, 166, 39);
		paneles_1.add(btnLecturaDestiempo);
		
		JButton btnEstadoActual = new JButton("Estado Actual");
		btnEstadoActual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//estadoCarrera = null;
				recuperaSituacionCarrera();
				System.out.println(estadoCarrera.toString());
				textAreaDerecho.setText(estadoCarrera.toString() + "\n" + "RETIRADOS: " + Estatica.getInstance().getRetirados().toString() + textAreaDerecho.getText());
				//textAreaDerecho.setText(textAreaDerecho.getText() + Estatica.getInstance().getRetirados().toString());
				
			}
		});
		btnEstadoActual.setBounds(305, 185, 166, 39);
		paneles_1.add(btnEstadoActual);
		
		interruptorLectores = new JCheckBox("LECTORES ON");
		interruptorLectores.setFont(new Font("Tahoma", Font.BOLD, 14));
		interruptorLectores.setBounds(278, 134, 134, 23);
		interruptorLectores.setSelected(true);
		paneles_1.add(interruptorLectores);
		
		JButton btnPonteALeer = new JButton("");
		btnPonteALeer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mensajeALectores = Constantes.MENSAJE_LECTORES_UFH_CMD_PONTE_A_LEER;
				System.out.println("Voy a mandar mensaje: " + mensajeALectores);
			}
		});
		btnPonteALeer.setIcon(new ImageIcon(VentanaCarrera.class.getResource("/images/rfid.png")));
		btnPonteALeer.setBounds(455, 93, 30, 30);
		paneles_1.add(btnPonteALeer);
		
		JButton btnLectoresConSonido = new JButton("");
		btnLectoresConSonido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mensajeALectores = Constantes.MENSAJE_LECTORES_UFH_CMD_TODAS_ANTENAS_ENABLE_SONIDO;
				System.out.println("Voy a mandar Mensaje: " + mensajeALectores);
				
			}
		});
		btnLectoresConSonido.setIcon(new ImageIcon(VentanaCarrera.class.getResource("/images/enable_sound.png")));
		btnLectoresConSonido.setBounds(455, 125, 30, 30);
		paneles_1.add(btnLectoresConSonido);
		
		JButton btnLectoresSinSonido = new JButton("");
		btnLectoresSinSonido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mensajeALectores = Constantes.MENSAJE_LECTORES_UFH_CMD_TODAS_ANTENAS_DISABLE_SONIDO;
				System.out.println("Voy a mandar Mensaje: " + mensajeALectores);
				
			}
		});
		btnLectoresSinSonido.setIcon(new ImageIcon(VentanaCarrera.class.getResource("/images/disable_sound.png")));
		btnLectoresSinSonido.setBounds(455, 157, 30, 30);
		paneles_1.add(btnLectoresSinSonido);
		
		JButton btnDescargarJugadores = new JButton("DESCARGA PARTICIPANTES");
		btnDescargarJugadores.setBounds(22, 430, 176, 60);
		paneles_1.add(btnDescargarJugadores);
		
		JButton btnEnviarLecturas = new JButton("ENVIAR LECTURAS");
		btnEnviarLecturas.setBounds(206, 433, 143, 55);
		paneles_1.add(btnEnviarLecturas);
		
		JButton btnGenerarResultados = new JButton("GENERAR RESULTADOS");
		btnGenerarResultados.setBounds(359, 433, 174, 55);
		paneles_1.add(btnGenerarResultados);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(562, 81, 520, 400);
		paneles_1.add(scrollPane);
		
		textAreaDerecho = new JTextArea();
		textAreaDerecho.setForeground(Color.WHITE);
		textAreaDerecho.setBackground(Color.BLACK);
		scrollPane.setViewportView(textAreaDerecho);
		

		
		
		btnGenerarResultados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int borrar = JOptionPane.showConfirmDialog(getContentPane(),"¿borro los anteriores?");
				
				if (borrar == JOptionPane.OK_OPTION) {
					generarResultados(true);
				} else {
					generarResultados(false);
				}
				
				System.out.println("Mando los retirados");
				for (int i=0; Estatica.getInstance().getRetirados()!= null && i<Estatica.getInstance().getRetirados().size(); i++) {
					String dorsalRetirado = Estatica.getInstance().getRetirados().get(i);
					try {
						System.out.println("Mando retirado " + dorsalRetirado);
						String respuesta = EnviosHTTP.sendGet("http://popularia.es/android/get_RETIRA_CORREDOR_evento.php?evento=" + idEventoSeleccionado + "&dorsal=" + dorsalRetirado);
						System.out.println(respuesta);
					} catch (Exception e) {
						
					}					
				}
			}
		});
		btnEnviarLecturas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int borrar = JOptionPane.showConfirmDialog(getContentPane(),"¿borro los anteriores?");
				
				if (borrar == JOptionPane.OK_OPTION) {
					enviarLecturas(true);
				} else {
					enviarLecturas(false);
				}
				
				
			}

		});
		btnDescargarJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int borrar = JOptionPane.showConfirmDialog(getContentPane(),"¿borro los anteriores?");
				System.out.println(borrar + "");
				if (borrar == JOptionPane.OK_OPTION) {
					Corredores.borrarCorredoresEvento(idEventoSeleccionado);
					String cadenaDevuelta = Corredores.getCorredoresEvento(idEventoSeleccionado);
					Corredores.guardaCorredores(cadenaDevuelta, idEventoSeleccionado);
				} else if (borrar == JOptionPane.NO_OPTION){
					String cadenaDevuelta = Corredores.getCorredoresEvento(idEventoSeleccionado);
					Corredores.guardaCorredores(cadenaDevuelta, idEventoSeleccionado);
				}
				
			}
		});

					
		lTxtDorsal.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					String sDorsal = lTxtDorsal.getText();
					if (sDorsal.startsWith(Constantes.LECTURA_CORREDOR_RETIRADO)) {
						sDorsal = sDorsal.substring(3).trim();
						Estatica.getInstance().anyadirRetirado(sDorsal);
						System.out.println("CORREDOR RETIRADO " + sDorsal);
						registraLecturaYActualizaEstructuras(sDorsal, "RET", null);
						
					} else {
						registraLecturaYActualizaEstructuras(sDorsal, "D", null);
					}
					lTxtDorsal.setText("");
					
					//System.out.println("Lectura manual: " + sDorsal);
					
					

					//System.out.println("Guardado dorsal manual: " + sDorsal);
				}					
			}
		});	
		
		lTxtDorsal.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				    warn();
				  }
				  public void removeUpdate(DocumentEvent e) {
				    warn();
				  }
				  public void insertUpdate(DocumentEvent e) {
				    warn();
				  }

				  public void warn() {
				     //if (lTxtDorsal.getText().length()>0){
				    	 //String sDorsal = lTxtDorsal.getText();
				    	 //lTxtDorsal.setText("");
				    	// System.out.println("Dorsaaal: " + sDorsal);				       
				     //}
				  }
				});		
		
		comboCategorias = new JComboBox<String>();
		comboCategorias.setFont(new Font("Rockwell", Font.BOLD, 18));
		comboCategorias.setBounds(715, 29, 263, 30);	
		paneles_1.add(comboCategorias);
		rellenaComboCategorias();
		comboCategorias.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				String categoriaSeleccionada = ((String)comboCategorias.getSelectedItem()).substring(0, ((String)comboCategorias.getSelectedItem()).indexOf('-')).trim();
				recuperaSituacionCarrera();
				textAreaDerecho.setText(estadoCarrera.toString(categoriaSeleccionada));
				
			}
		});
		
		
		JButton btnCarreras = new JButton("CARRERAS");
		btnCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gs = ge.getScreenDevices();
								
				int indexPantalla = gs.length > 1 ? 1 : 0;
				
				displayCarreras = new DisplayCarreras(gs[indexPantalla].getDefaultConfiguration(), idEventoSeleccionado);
				//displayCarreras = new DisplayCarrera();
				displayCarreras.setExtendedState(JFrame.MAXIMIZED_BOTH);
				displayCarreras.setUndecorated(true);
				displayCarreras.setVisible(true);
				//DisplayCarreras3 dp3 = new DisplayCarreras3(gs[indexPantalla].getDefaultConfiguration());
				
			}
		});
		
		btnCarreras.setBounds(1100, 161, 150, 64);
		
		paneles_1.add(btnCarreras);
		
		JButton btnCerrarCarreras = new JButton("");
		btnCerrarCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (displayCarreras!=null){
					displayCarreras.setVisible(false);
					displayCarreras.dispose();
				}
			}
		});
		btnCerrarCarreras.setIcon(new ImageIcon(".\\res\\icons\\cerrar-icono.png"));
		btnCerrarCarreras.setBounds(1260, 161, 64, 62);
		paneles_1.add(btnCerrarCarreras);
		
		
		JButton btnResultadoDorsal = new JButton("RESULTADO DORSAL");
		btnResultadoDorsal.setFont(new Font("Tahoma", Font.PLAIN, 7));
		btnResultadoDorsal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean dorsalValido = false;
				if (!txfDorsal.getText().equals("")){
					try {
						int dorsalResultado = Integer.parseInt(txfDorsal.getText());
						ProcessBuilder pb = new ProcessBuilder();
						try {
							String nombrePaginaResultado = ".\\res\\resultados\\resultado_" + dorsalResultado + "_" +  idEventoSeleccionado+ ".html";
							//pb.command("cmd.exe", "/c","start chrome.exe --kiosk --incognito " + nombrePaginaResultado);
							pb.command("cmd.exe", "/c","start firefox.exe -url " + nombrePaginaResultado);
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
								
								e.printStackTrace();
							}
						

					} catch (Exception e) {
						dorsalValido = false;
					}
				}
				
				//if (!dorsalValido) JOptionPane.showMessageDialog(getContentPane(), "Dorsal no válido", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnResultadoDorsal.setBounds(1160, 300, 95, 64);
		paneles_1.add(btnResultadoDorsal);
		
		JButton btnCerrarResultadoDorsal = new JButton("");
		btnCerrarResultadoDorsal.setIcon(new ImageIcon(".\\res\\icons\\cerrar-icono.png"));
		btnCerrarResultadoDorsal.setBounds(1260, 300, 64, 62);
		btnCerrarResultadoDorsal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	            try {
					ProcessBuilder pbMataChromes = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /IM firefox.exe");
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
		paneles_1.add(btnCerrarResultadoDorsal);
		
		txfDorsal = new JTextField();
		txfDorsal.setBounds(1100, 300, 50, 64);
		paneles_1.add(txfDorsal);
		txfDorsal.setColumns(10);
	}

	private void rellenaComboCategorias() {
		//ArrayList<TCategorias> listaCategorias = (ArrayList<TCategorias>)hmCategorias.values();
		List<TCategorias> listaCategorias = new ArrayList<TCategorias>(hmCategorias.values());
		Collections.sort(listaCategorias);
		for (TCategorias categoria: listaCategorias) {
			comboCategorias.addItem(categoria.getIdCategoria() + " - " + categoria.getDescCategoria());
		}
		/*for (Map.Entry<String, TCategorias> entry : hmCategorias.entrySet()) {
			TCategorias categoria = entry.getValue();
			comboCategorias.addItem(categoria.getIdCategoria() + " - " + categoria.getDescCategoria());
			//comboCategorias.add(categoria.getIdCategoria() + " - " + categoria.getDescCategoria());
		}*/
	}

	protected void generarResultados(boolean borrarAnteriores) {
		if (borrarAnteriores) {
			try {
				System.out.println("Peticion borrar resultados y pasos anteriores del servidor antes de mandar de nuevo todos");
				String respuesta = EnviosHTTP.sendGet("http://popularia.es/android/get_BORRA_pasos_y_resultados_evento.php?evento=" + idEventoSeleccionado);
				System.out.println(respuesta);
			} catch (Exception e) {
				
			}
		}	
		

		String lineaLecturas = Lecturas.getLecturasServidor(idEventoSeleccionado, idCarreraSeleccionada);					
		ArrayList<TLecturas> lecturas = Lecturas.procesaLecturas(lineaLecturas, idEventoSeleccionado);
		
		Resultados.generaResultados(lecturas, hmCategorias);
		System.out.println("");
		
		//ArrayList<TCategorias> categorias = (ArrayList<TCategorias>)hmCategorias.values();
		//Map<String, TCategorias> datos = new HashMap<String, TCategorias>();
		//borro resultados entre medias
		Resultados.borraResultadosTODOS(idEventoSeleccionado + "");
		
		for (Map.Entry<String, TCategorias> entry : hmCategorias.entrySet()) {
			TCategorias categoria = entry.getValue();
			
			if (idEventoSeleccionado == 175) {
				if (categoria.getIdCategoria() == 1 || categoria.getIdCategoria() == 2) {
					String lineasClasificaciones = Carreras.getClasificaciones(idEventoSeleccionado, categoria.getIdCategoria());
					Carreras.generaHTMLResultadosIndividuales(evento, lineasClasificaciones, categoria);					
				}						
			} else {
				String lineasClasificaciones = Carreras.getClasificaciones(idEventoSeleccionado, categoria.getIdCategoria());

				Carreras.generaHTMLResultadosIndividuales(evento, lineasClasificaciones, categoria);
			}


		    
		}		
		
	}

	private void recuperaSituacionCarrera() {
		estadoCarrera = new EstadoCarrera(evento);
		
		List<TCategorias> lCategorias = new ArrayList<TCategorias>(hmCategorias.values());
		estadoCarrera.setCategorias(lCategorias);
		
		Estatica.getInstance().resetearLecturasTotales();
		
		ArrayList<TLecturas> lecturas = Lecturas.recuperarLecturas(idEventoSeleccionado, idCarreraSeleccionada);
		for (TLecturas lectura: lecturas) {
			Lecturas.anyadirHashMapDinamico(lectura);
			TCorredores ultimoCorredor = Resultados.getIdPorDorsal(idEventoSeleccionado, lectura.getDorsalLeido()+"");
			
			if (ultimoCorredor != null) {
				String vuelta = Corredores.getUltimoPaso(ultimoCorredor);
				
				if (vuelta.equals("REPE")) {
				
				} else if(vuelta.equals("RET.")) {
					Estatica.getInstance().anyadirRetirado(ultimoCorredor.getDORSAL());
					
				} else {
					ultimoCorredor.setTIEMPO_TOTAL_FORMATEADO(lectura.getTiempoFinal());
					ultimoCorredor.setVuelta(Corredores.getUltimoPaso(ultimoCorredor));
					
					estadoCarrera.anyadirCorredor(ultimoCorredor);					
				}
			
			} else {
				System.out.println("CORREDOR CON ESE DORSAL NO ENCONTRADO " + lectura.getDorsalLeido());
			}
			
			
		}
		System.out.println(estadoCarrera);	
		System.out.println("RETIRADOS " + Estatica.getInstance().getRetirados().toString());
		
		log.info(estadoCarrera);
	}

	protected void arrancarLectoresUHF() {
		//lectores
		hilosCorriendo = true;

		timer = new Thread(new HiloLector(1, in1, puerto1));
		timer.start();
		
		/*timerWrite = new Thread(new HiloLectorWrite(out1));
		timerWrite.start();*/

		timer2 = new Thread(new HiloLector(2, in2, puerto2));
		timer2.start();
		
		/*timerWrite2 = new Thread(new HiloLectorWrite(out2));
		timerWrite2.start();*/

		timer3 = new Thread(new HiloLector(3, in3, puerto3));
		timer3.start();
		
		/*timerWrite3 = new Thread(new HiloLectorWrite(out3));
		timerWrite3.start();*/
		
		
		timer4 = new Thread(new HiloLector(4, in4, puerto4));
		timer4.start();
		
		/*timerWrite4 = new Thread(new HiloLectorWrite(out4));
		timerWrite4.start();*/
		
		
		//hiloSondaPeticionLectura = new Thread( new HiloSondaPeticionLectura());	
		//hiloSondaPeticionLectura.start();
		
	}

	private void enviarLecturas(boolean borrarAnteriores) {
		
		if (borrarAnteriores) {
			try {
				System.out.println("Peticion borrar lecturas evento del servidor antes de mandar de nuevo todas");
				String respuesta = EnviosHTTP.sendGet("http://popularia.es/android/get_BORRA_lecturas_evento.php?evento=" + idEventoSeleccionado);
				System.out.println(respuesta);
			} catch (Exception e) {
				
			}
		}
		ArrayList<TLecturas> lecturas = Lecturas.recuperarLecturas(idEventoSeleccionado, idCarreraSeleccionada);
		int i = 0;
		for (TLecturas lectura : lecturas) {
			if (lectura.getTipoLectura().equals("RET")) {
				
			} else {
				ResultadosAsincrona lecturaEnvio = new ResultadosAsincrona(-1, lectura.getIdEvento(), lectura.getIdCarrera(), lectura.getTipoLectura(),
						lectura.getTagLeido(), lectura.getDorsalLeido() + "", lectura.getTiempoFinal(), lectura.getFechaInsert());
				
				lecturaEnvio.start();
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					
				}
				i++;				
			}

		}
		System.out.println("Mandadas " + i + " lecturas");
	}
	
	private void arrancaCronometroNuevo() {
		
        if (hilo != null)
            hilo.interrupt();
		
		cronometro = new Cronometro("Popularia", lLblCronometro, -1);
		hilo = new Thread(cronometro);
		hilo.start();	
		hiloCorriendo = true;
		
		tiempoInicial = System.currentTimeMillis();
    	System.out.println("Tiempo inicial en milis: " + tiempoInicial);		        	
    	Carreras.guardarTiempoInicial(idEventoSeleccionado, idCarreraSeleccionada, tiempoInicial);
        ResultadosAsincrona resultadosAsincrona = new ResultadosAsincrona(idEventoSeleccionado,idCarreraSeleccionada);
        resultadosAsincrona.enviarInicioCarrera(tiempoInicial);	

		Estatica.getInstance().setDescCarreraSeleccionada(lLblCategorias.getText());
		Estatica.getInstance().setCarreraSeleccionada(idCarreraSeleccionada);
		Estatica.getInstance().setTiempoInicial(tiempoInicial);		
		
	}
	
	private void prepararLectoresSerie() {
		
		  /*timer = new Thread(new HiloLector1(1, in1, out1, puerto1)); 
		  timer2 = new Thread(new HiloLector1(2, in2, out2, puerto2)); 
		  timer3 = new Thread(new HiloLector1(3, in3, out3, puerto3));
		 */
		/*
		 * timer = new Thread(new HiloLector1(1, in1, puerto1)); timer2 = new Thread(new
		 * HiloLector1(2, in2, puerto2)); timer3 = new Thread(new HiloLector1(3, in3,
		 * puerto3));
		 */	
		
		puertos_libres = CommPortIdentifier.getPortIdentifiers();
		
		
		while (puertos_libres.hasMoreElements()) {
			port = (CommPortIdentifier) puertos_libres.nextElement();
			HashSet<String> puertosLectores = new HashSet<String>();
			
			for (int i=1; i<35;i++) {
				puertosLectores.add("COM" + i);
			}
			if (puertosLectores.contains(port.getName())) {
				try {
					puerto_ser = (SerialPort) port.open("puerto serial", 500);
					int baudRate = 9600;
					puerto_ser.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

					if (in1 == null) {
						in1 = puerto_ser.getInputStream();
						out1 = puerto_ser.getOutputStream();
						puerto1 = port.getName();
					}
					else if (in2 == null) {
						in2 = puerto_ser.getInputStream();
						out2 = puerto_ser.getOutputStream();
						puerto2= port.getName();
					}
					else if (in3 == null) {
						in3 = puerto_ser.getInputStream();
						out3 = puerto_ser.getOutputStream();
						puerto3 = port.getName();
					}
					
					else if (in4 == null) {
						in4 = puerto_ser.getInputStream();
						out4 = puerto_ser.getOutputStream();
						puerto4 = port.getName();
					}
					
				} catch (PortInUseException e) {
					e.printStackTrace();
				} catch (UnsupportedCommOperationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void actualizaUltimosCorredores (TCorredores ultimoCorredor) {
		
		if (ultimoCorredor.getPosicionProvisional() > 0) {
			TCorredores auxiliar = null;
			TCorredores auxiliar2 = null;
			
			if (ultimosCorredores[0] != null && ultimosCorredores[0].getDORSAL().equals(ultimoCorredor.getDORSAL()))
					return;
			if (ultimosCorredores[1] != null && ultimosCorredores[1].getDORSAL().equals(ultimoCorredor.getDORSAL()))
				return;
			if (ultimosCorredores[2] != null && ultimosCorredores[2].getDORSAL().equals(ultimoCorredor.getDORSAL()))
				return;
				
			if (ultimosCorredores[0] == null){
				ultimosCorredores[0] = ultimoCorredor;
				return;
			} 
			
			if (ultimosCorredores[1] == null){
				auxiliar = ultimosCorredores[0];
				ultimosCorredores[0] = ultimoCorredor;
				ultimosCorredores[1] = auxiliar;
				return;			
			}		
			
			auxiliar = ultimosCorredores[0];
			ultimosCorredores[0] = ultimoCorredor;
				
			auxiliar2 = ultimosCorredores[1];
			ultimosCorredores[1] = auxiliar;
			
			ultimosCorredores[2] = auxiliar2;			
		}

			
	}
	
	
	private String actualizaEtiquetaUltimosDorsales() {
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='1' width='95%' align='center'>");
		
		sb.append("<tr>");
		sb.append("<td align='center' width='100'>P.</td>");
		//sb.append("<td align='center' width='100'>D.</td>");
		sb.append("<td align='center' width='1300'>CORREDOR</td>");
		sb.append("<td align='center' width='100'>CAT.</td>");
		sb.append("<td align='center' width='100'>VUELTA</td>");
		sb.append("<td align='center' width='100'>TIEMPO</td>");
		
		sb.append("</tr>");
		
		if (ultimosCorredores[0] != null){
			
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[0].getPosicionProvisional() + "º</td>");
			//sb.append("<td align='center' width='100'>" + ultimosCorredores[0].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1300'>" + ultimosCorredores[0].getDORSAL() + " -> " + ultimosCorredores[0].getNOMBRE() + " " + ultimosCorredores[0].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[0].getCATEGORIA() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[0].getVuelta() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[0].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			
			sb.append("</tr>");
		} 

		if (ultimosCorredores[1] != null){
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[1].getPosicionProvisional() + "º</td>");
			//sb.append("<td align='center' width='100'>" + ultimosCorredores[1].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1300'>" + ultimosCorredores[1].getDORSAL() + " -> " + ultimosCorredores[1].getNOMBRE() + " " + ultimosCorredores[1].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[1].getCATEGORIA() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[1].getVuelta() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[1].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			sb.append("</tr>");
		}

		if (ultimosCorredores[2] != null){
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[2].getPosicionProvisional() + "º</td>");
			//sb.append("<td align='center' width='100'>" + ultimosCorredores[2].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1300'>" + ultimosCorredores[2].getDORSAL() + " -> " + ultimosCorredores[2].getNOMBRE() + " " + ultimosCorredores[2].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[2].getCATEGORIA() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[2].getVuelta() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[2].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			sb.append("</tr>");
		}
		
		sb.append("</table>");
		sb.append("</html>");

		lLblLlegadosDorsal.setText(sb.toString());
		
		JLabel arrayJLabel [] = {lLblLlegadosDorsal};
		Estatica.getInstance().setlLblLlegadosDorsal(arrayJLabel);
		
		
		return sb.toString();
	}

	private boolean arrancaCronometroSiProcede(int idCarreraSeleccionada) {
		TCarreras carrera = Carreras.getCarreraPorId(idEventoSeleccionado, idCarreraSeleccionada);
		
		if (carrera.getINICIO()>0) {
			System.out.println("Tiene un inicio, arrancamos ahí");
			//preparaCronometro(carrera.getINICIO());
			cronometro = new Cronometro("Popularia", lLblCronometro, carrera.getINICIO());
			hilo = new Thread(cronometro);
			hilo.start();	
			hiloCorriendo = true;
			//lBtnParar.setEnabled(true);
			//lBtnIniciar.setEnabled(false);
			tiempoInicial = carrera.getINICIO();
			Estatica.getInstance().setTiempoInicial(tiempoInicial);
			
			return true;
		} else {
			System.out.println("No tiene inicio, nos quedamos esperando");
			return false;
		}
	}
	
	private void preparaCronometro(long timeInicio){
		hilo = new Thread(){
	        public void run(){
	        	
	        	if (timeInicio > 0) {
	        		tiempoInicial = timeInicio;
	        	} else {
	        		tiempoInicial = System.currentTimeMillis();
		        	System.out.println("Tiempo inicial en milis: " + tiempoInicial);		        	
		        	Carreras.guardarTiempoInicial(idEventoSeleccionado, tabbedPane.getSelectedIndex()+1, tiempoInicial);
		            ResultadosAsincrona resultadosAsincrona = new ResultadosAsincrona(idEventoSeleccionado, (tabbedPane.getSelectedIndex()+1));
		            resultadosAsincrona.enviarInicioCarrera(tiempoInicial);
		            
		            
	        	}

	        	Estatica.getInstance().setDescCarreraSeleccionada(lLblCategorias.getText());
	        	Estatica.getInstance().setCarreraSeleccionada(tabbedPane.getSelectedIndex());
	        	Estatica.getInstance().setTiempoInicial(tiempoInicial);
	        	
	        	lTxtDorsal.requestFocus();
	        		        		        	
	            try{
	            	
	                while(true){
	                	tiempoFinal = System.currentTimeMillis();
	                	lLblCronometro.setText(formateaTiempo(tiempoFinal));
	                	Thread.sleep(56);
	                }
	            } catch (InterruptedException ie){
	            	Estatica.getInstance().setTiempoInicial(-1);
	            }
	        }
		};
	}
	
	private String formateaTiempo (long tiempoFinalCorredor){
		
		
    	
		Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(tiempoFinalCorredor - tiempoInicial);
    	
    	long timeInMillis = tiempoFinalCorredor - tiempoInicial;
    	long horas = timeInMillis / 3600000;
    	timeInMillis = timeInMillis - (horas * 3600000);
    	long minutos = timeInMillis / (60 * 1000);
    	timeInMillis = timeInMillis - (minutos * 60000);
    	long segundos = timeInMillis / 1000;
    	long millis = timeInMillis - (segundos * 1000);
    	
    	//System.out.println("tiempo nuevo " + horas + ":" + minutos + ":" + segundos);
    	
    	
		/*
		 * int horas = calendar.get(Calendar.HOUR_OF_DAY) - 1; int millis =
		 * calendar.get(Calendar.MILLISECOND); int segundos =
		 * calendar.get(Calendar.SECOND); int minutos = calendar.get(Calendar.MINUTE);
		 */
    	
    	String sMillis = millis < 10 ? "0" + millis : millis + "";
    	sMillis = millis < 100 ? "0" + sMillis : sMillis;
    	String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
    	String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";  
    	String sHoras = horas < 10 ? "0" + horas : horas + "";
    	
    	return (sHoras + ":" + sMinutos + ":" + sSegundos);		
	}

	
	/*private class HiloSondaPeticionLectura implements Runnable{
		
		
		
		public HiloSondaPeticionLectura() {
			super();
		}

		
		@Override
		public void run() {
			System.out.println("ARRANCO SONDA PETICION LECTURA");
			while (hilosCorriendo) {
				try {
					long tiempoSonda = Constantes.TIEMPO_SONDA_PETICION_LECTURA +  + (new Random().nextInt(30000));
					System.out.println("dUERMO sonda " + tiempoSonda);
					Thread.sleep(tiempoSonda);
					mensajeALectores = Constantes.MENSAJE_LECTORES_UFH_CMD_PONTE_A_LEER;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}*/

	/*private class HiloLectorWrite implements Runnable{
		OutputStream output = null;
		
		
		public HiloLectorWrite(OutputStream output) {
			super();
			this.output = output;
		}


		@Override
		public void run() {
			
			while (hilosCorriendo) {
				try {
					if (output != null) {
						
							
						if (mensajeALectores != null && !mensajeALectores.equals("")) {
							System.out.println("Mando "+ mensajeALectores + " en hilo ");
							
							for (int i=0; i<mensajeALectores.length(); i=i+2) {
								byte [] byteAMandar = DatatypeConverter.parseHexBinary(mensajeALectores.substring(i, i+2));
								output.write(byteAMandar);
							}
							Thread.sleep(400);
							mensajeALectores = "";
						}
						Thread.sleep(100);
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}*/
		
	private class HiloLector implements Runnable{
		int numeroLector;
		InputStream input = null;
		
		String puerto = null;
		
		public HiloLector (int numeroLector, InputStream input, String puerto ) {
			this.numeroLector = numeroLector;
			this.input = input;
			this.puerto = puerto;
		}
		
		

		@Override
		public void run() {
			boolean mandadoIntro = false;
			StringBuffer sb = new StringBuffer();
			while (hilosCorriendo) {
				try {
					if (input != null) {
						int aux = input.read();
						Thread.sleep(10);				
				
						if (aux > -1) {
							byte[]array = {(byte)aux};
							sb.append(DatatypeConverter.printHexBinary(array));
							//System.out.print(DatatypeConverter.printHexBinary(array));
							mandadoIntro = false;
						} else if (!mandadoIntro){
							//System.out.println("");
							sb.append("");
							mandadoIntro = true;
							//System.out.println("lector 1: " + sb.toString());
							
							String tag = sb.toString();
							if (sb.length()>=38) {
								tag = tag.substring(14, 38);
							}							
							if (interruptorLectores!= null && interruptorLectores.isSelected() && (cronometro.getTiempoFinal() - tiempoInicial) > Constantes.TIEMPO_INICIAL_NO_LECTURAS) {
								String dorsal = Dorsales.hashUhfTagDorsales.get(tag);
								
								if (dorsal != null && Estatica.getInstance().isRetirado(dorsal)) {
									System.out.println("Lectura de corredor RETIRADO " + dorsal);
									log.error("Lectura de corredor RETIRADO " + dorsal);
								} else {
									if (tag.equals(Constantes.MENSAJE_LECTORES_UHF_RESPUESTA_CMD_PONTE_A_LEER) || tag.equals(Constantes.MENSAJE_LECTORES_UHF_RESPUESTA_CMD_CONFIGURACION))
										System.out.println("Descarto lectura porque es una respuesta a un mensaje de configuracion");
									else
										registraLecturaYActualizaEstructuras(dorsal, "T", tag);									
								}
								//Lecturas.guardaLectura(idEventoSeleccionado, 1, "T", dorsal, tag, formateaTiempo(cronometro.getTiempoFinal()), 0);

							} else if (interruptorLectores!= null && !interruptorLectores.isSelected()) {
								String dorsal = Dorsales.hashUhfTagDorsales.get(tag);
								System.out.println("Lectura UHF descartada porque el INTERRUPTOR está OFF " + dorsal);
								log.error("Lectura UHF descartada porque el INTERRUPTOR está OFF " + dorsal);
								//TLecturas lectura = Lecturas.anyadirHashMapDinamico(idEventoSeleccionado, tabbedPane.getSelectedIndex() + 1, "T", dorsal, tag, formateaTiempo(cronometro.getTiempoFinal()), 0);
							} else {
								String dorsal = Dorsales.hashUhfTagDorsales.get(tag);
								log.error("lectura UHF descartada porque es muy pronto. dorsal " + dorsal + ". tiempo: " + formateaTiempo(cronometro.getTiempoFinal()));
								log.error("tiempo inicial: "  + tiempoInicial + ". tiempo final: " + tiempoFinal);
								System.out.println("lectura UHF descartada porque es muy pronto. dorsal " + dorsal + ". tiempo: " + formateaTiempo(cronometro.getTiempoFinal()));
							}
							//int numeroLector = 1;
							//log.debug(FormatoFecha.getHoraSistema() + ": Lector " + numeroLector + " - " + puerto +   ": " + tag);
							System.out.println(FormatoFecha.getHoraSistema() + ": Lector " + numeroLector + " - " + puerto +   ": " + tag);
								
							sb = new StringBuffer();
						}	
						
					} else {
						Thread.sleep(10);
					}

					

					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void registraLecturaYActualizaEstructuras(String sDorsal, String tipo, String tag) {
		Lecturas.guardaLectura(idEventoSeleccionado, idCarreraSeleccionada, tipo, sDorsal, tag, formateaTiempo(cronometro.getTiempoFinal()), 0);
		
		if (sDorsal != null) {
			TLecturas lectura = Lecturas.anyadirHashMapDinamico(idEventoSeleccionado, idCarreraSeleccionada, tipo, sDorsal, null, formateaTiempo(cronometro.getTiempoFinal()), 0);
			
			
			TCorredores ultimoCorredor = Resultados.getIdPorDorsal(idEventoSeleccionado, sDorsal);
			
			
			if (ultimoCorredor != null) {
				String vuelta = Corredores.getUltimoPaso(ultimoCorredor);
				
				if (vuelta.equals("REPE") || vuelta.equals("RET.")) {
					
				} else {
					ultimoCorredor.setTIEMPO_TOTAL_FORMATEADO(formateaTiempo(cronometro.getTiempoFinal()));
					ultimoCorredor.setVuelta(Corredores.getUltimoPaso(ultimoCorredor));
					
					int posicion = estadoCarrera.anyadirCorredor(ultimoCorredor);
					if (posicion > 0) ultimoCorredor.setPosicionProvisional(posicion);
					//System.out.println(estadoCarrera);
					log.info(estadoCarrera);
																		
					actualizaUltimosCorredores(ultimoCorredor);
					actualizaEtiquetaUltimosDorsales();
					actualizaTextArea(ultimoCorredor);

				}

			} else {
				//System.out.println("CORREDOR CON ESE DORSAL NO ENCONTRADO " + sDorsal);
			}	
			if(tipo.equals("T")) {
				System.out.println("Leido y guardado. Tipo: UHF. Dorsal: " + sDorsal);
			} else {
				System.out.println("Leido y guardado. Tipo: Teclado. Dorsal: " + sDorsal);
			}			
		}
		
	}

	private void actualizaTextArea(TCorredores ultimoCorredor) {
		textArea.setText(ultimoCorredor.getPosicionProvisional() + "º"+ " - "  + ultimoCorredor.toString() + "\n" + textArea.getText());
		//textAreaDerecho.setText(ultimoCorredor.getPosicionProvisional() + "º"+ " - "  + ultimoCorredor.toString() + "\n" + textAreaDerecho.getText());
		
	}
}
