package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import dominio.Carreras;
import dominio.Corredores;
import dominio.Eventos;
import dominio.Resultados;
import dominio.ResultadosAsincrona;
import estaticos.Dorsales;
import estaticos.Log;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import persistencia.dao.model.TCarreras;
import persistencia.dao.model.TCorredores;
import persistencia.dao.model.TEventos;
import persistencia.dao.model.TResultados;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;



public class VentanaAdministracion extends JFrame {

	//lectores seriales
	Enumeration puertos_libres = null;
	CommPortIdentifier port = null;
	SerialPort puerto_ser = null;
	
	private JPanel contentPane;
	private static Logger log = Log.getLogger(VentanaAdministracion.class);
	JComboBox<String> comboBox;
	private JButton btnNewButton;
	InputStream in = null;
	int dorsal = 1;
	Thread timer1;
	JSpinner spinner;
	String ultimoTagInsertado = "NINGUNO";
	
	ArrayList<String> tagsYaGuardados = new ArrayList<String>();
	/**
	 * Create the frame.
	 */
	public VentanaAdministracion(int idEvento) {
		
		//Relleno datos
		onPostExecute(dameCarrerasServidor());
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(66, 11, 313, 20);
		contentPane.add(comboBox);
		rellenarComboEventos(comboBox);
		
		
		
		
		JButton btnDescargarCorredores = new JButton("Descargar corredores");
		btnDescargarCorredores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eventoSeleccionado = (String)comboBox.getSelectedItem();
				int evento = Integer.parseInt(eventoSeleccionado.substring(0, eventoSeleccionado.indexOf("-")));
				String cadenaDevuelta = Corredores.getCorredoresEvento(evento);
				Corredores.guardaCorredores(cadenaDevuelta, evento);
			}
		});
		btnDescargarCorredores.setBounds(128, 59, 176, 42);
		contentPane.add(btnDescargarCorredores);
		
		JButton button = new JButton("Descargar resultados");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String eventoSeleccionado = (String)comboBox.getSelectedItem();
				int evento = Integer.parseInt(eventoSeleccionado.substring(0, eventoSeleccionado.indexOf("-")));
				for (int i = 1; i < 9; i++) {
					String cadenaDevuelta = getResultadosEvento(evento, i);
					System.out.println(cadenaDevuelta);
					guardarResultadosCategoria(cadenaDevuelta);
					
				}
			}
		});
		button.setBounds(128, 122, 176, 42);
		contentPane.add(button);
		
		btnNewButton = new JButton("Gestión Dorsales");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prepararLectoresSerie();
				//hilosCorriendo = true;
				timer1 = new Thread(new HiloLector1());
				timer1.start();
				
				System.out.println("Listo para leer");
				
			}
		});
		btnNewButton.setBounds(128, 185, 176, 42);
		contentPane.add(btnNewButton);
		
		spinner = new JSpinner();
		spinner.setBounds(329, 185, 82, 42);
		contentPane.add(spinner);
		spinner.setValue(1);
	}
	
	private void prepararLectoresSerie() {
		timer1 = new Thread(new HiloLector1());
		//timer2 = new Thread(new HiloLector2());
		puertos_libres = CommPortIdentifier.getPortIdentifiers();
		int aux = 0;
		
		while (puertos_libres.hasMoreElements()) {
			port = (CommPortIdentifier) puertos_libres.nextElement();
			HashSet<String> puertosLectores = new HashSet<String>();
			
			for (int i=1; i<15;i++) {
				puertosLectores.add("COM" + i);
			}
			if (puertosLectores.contains(port.getName())) {
				try {
					puerto_ser = (SerialPort) port.open("puerto serial", 500);
					int baudRate = 9600;
					puerto_ser.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					//puerto_ser.setDTR(true);
					if (in == null)
						in = puerto_ser.getInputStream();
				
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
	
	public void guardarResultadosCategoria (String s) {
        StringTokenizer st = new StringTokenizer(s, ";");
        int numResultados = Integer.parseInt(st.nextToken());
        
        for (int j=0; j<numResultados; j++){
        	 
        	
            String idCarrera = st.nextToken();
            String idCorredor = st.nextToken();
            String tiempo = st.nextToken();
            String ritmo = st.nextToken();
           
            TResultados resultado = new TResultados();
            resultado.setID_CARRERA(Integer.parseInt(idCarrera));
            resultado.setID_CORREDOR(Integer.parseInt(idCorredor));
            resultado.setPOS_CAT(j+1);
            resultado.setTIEMPO_FORMATEADO(tiempo);
            resultado.setRITMO(ritmo);
            
            Resultados.guardaResultado(resultado);
        }		
	}
	
	private void rellenarComboEventos(JComboBox comboEventos) {
		ArrayList<TEventos> lEventos = Eventos.getTodosEventos();
		
		for (TEventos tEventos : lEventos) {
			comboEventos.addItem(tEventos.getIdEvento() + "-" + tEventos.getFechaEvento() + " - " + tEventos.getNombreEvento());
		}
		
	}
	
	public String getResultadosEvento(int evento, int categoria) {
		
        String datos = "evento=" + evento + "&categoria=" + categoria;
     
        String [] strings = new String[]{"http://popularia.es/android/get_resultados_evento.php",datos};
        HttpURLConnection con = null;
		
        String data = strings[1];

        StringBuilder output = new StringBuilder();
        StringBuilder responseOutput= new StringBuilder();
        try {
        	URL url = new URL(strings[0]);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("USER-AGENT", "Mozilla/5.0");
			con.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

			con.setDoOutput(true);
			DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
			dStream.writeBytes(data);
			dStream.flush();
			dStream.close();
			int responseCode = con.getResponseCode();
			output = new StringBuilder("Request URL " + url);
			output.append(System.getProperty("line.separator") + "Request Parameters " + data);
			output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
			output.append(System.getProperty("line.separator") + "Type " + "POST");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			String line = "";
			responseOutput = new StringBuilder();
			System.out.println("output===============" + br);
			while ((line = br.readLine()) != null) {
				responseOutput.append(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        log.debug("ENVIOS_SALIDA" + output.toString());
        return responseOutput.toString();
	}

	

	public String dameCarrerasServidor() {
        String datos = "password=rocioysergio";
        String [] strings = new String[]{"http://popularia.es/android/get_carreras.php",datos};
        HttpURLConnection con = null;
		
        String data = strings[1];

        StringBuilder output = new StringBuilder();
        StringBuilder responseOutput= new StringBuilder();
        try {
        	URL url = new URL(strings[0]);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("USER-AGENT", "Mozilla/5.0");
			con.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

			con.setDoOutput(true);
			DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
			dStream.writeBytes(data);
			dStream.flush();
			dStream.close();
			int responseCode = con.getResponseCode();
			output = new StringBuilder("Request URL " + url);
			output.append(System.getProperty("line.separator") + "Request Parameters " + data);
			output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
			output.append(System.getProperty("line.separator") + "Type " + "POST");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			String line = "";
			responseOutput = new StringBuilder();
			System.out.println("output===============" + br);
			while ((line = br.readLine()) != null) {
				responseOutput.append(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        log.debug("ENVIOS_SALIDA" + output.toString());
        return responseOutput.toString();
	}

	 protected void onPostExecute(String s) {
         StringTokenizer st = new StringTokenizer(s, ";");
         int i=0;
         TEventos evento = new TEventos();
         TCarreras carrera = new TCarreras();
         
         HashSet<Integer> eventosCreados = new HashSet<Integer>();
         while (st.hasMoreTokens()){
             int idEvento = Integer.parseInt(st.nextToken());
             String nombre = st.nextToken();
             String fecha = st.nextToken();
             //st.nextToken();
             
             evento = new TEventos();
             
             evento.setIdEvento(idEvento);
             evento.setFechaEvento(fecha);
             evento.setNombreEvento(nombre);
             
             carrera = new TCarreras();
             

             Eventos.guardarEvento(evento);

             int resultado = 0;
             if (!eventosCreados.contains(new Integer(idEvento) ) ){
                 //resultado = dbManager.darAltaEventoYaExisteServidor(idEvento, nombre, fecha);
                 log.debug("alta evento " + idEvento + ". Resultado: " + resultado);
                 eventosCreados.add(new Integer(idEvento));
                 i++;
             }
             
             
             int idCarrera = Integer.parseInt(st.nextToken());
             String hora = st.nextToken();
             String descCarrera = st.nextToken();
             String marcaInicio = st.nextToken();
             
             carrera.setID_EVENTO(idEvento);
             carrera.setID_CARRERA(idCarrera);
             carrera.setHORA(hora);
             carrera.setCATEGORIA(descCarrera);

             //resultado = dbManager.darAltaCarrera(idEvento, idCarrera, descCarrera, hora);
             i++;

             if (marcaInicio!=null && !marcaInicio.toUpperCase().equals("NO")){
                 //Eliminamos inicio carrera
                 //dbManager.eliminarInicioCarrera(idEvento+"", idCarrera+"");
                 //Agregamos marca inicio del servidor

                 Date dateInicio = new Date();
                 dateInicio.setTime(Long.parseLong(marcaInicio));
                 carrera.setINICIO(Long.parseLong(marcaInicio));
                 
                 //Carreras.guardarCarrera(carrera);
                 
                 //dbManager.insertarInicios(idEvento+"", idCarrera+"", dateInicio.getTime());
             }
             //carrera.setINICIO(null);
             
             Carreras.guardarCarrera(carrera);

             int numPasos = Integer.parseInt(st.nextToken());

             for (int j=0; j<numPasos; j++){
                 String idPaso = st.nextToken();
                 String idCategoria = st.nextToken(); //por ahora no hacemos nada con la categoría, solo queremos que salte al siguiente token
                 String distancia = st.nextToken();
                 String tipoTramo = st.nextToken();
                 int isMeta = Integer.parseInt(st.nextToken());
                 if (isMeta == 1){
                     idPaso = "META";
                 }

                 //dbManager.insertarPasoIntermedio(idEvento+"", idCarrera+"", idPaso, distancia, tipoTramo);
             }
             

         }

     }
	 
	 private class HiloLector1 implements Runnable{
			
			
			@Override
			public void run() {
				boolean mandadoIntro = false;
				StringBuffer sb = new StringBuffer();
				while (true) {
					try {
						if (in != null) {
							int aux = in.read();
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
								System.out.println("lector 1: " + sb.toString());
								
								if (sb != null && !sb.toString().equals("")) {
									if (sb.toString().length()>38 && sb.toString().length()<60 && !sb.toString().substring(14, 38).startsWith("3035") && !tagsYaGuardados.contains(sb.toString().substring(14, 38))) {
										dorsal = (int)spinner.getValue();
										int resultado = Dorsales.guardaDorsal(dorsal + "", sb.toString().trim());
										ultimoTagInsertado = sb.toString().trim().substring(14, 38);
										tagsYaGuardados.add(sb.toString().trim().substring(14, 38));
										
										if (resultado > 0) 
											spinner.setValue(dorsal+1);
									} else {
										System.out.println("TAG repetido. No inserto");
									}
									
									//spinner.setValue(dorsal);
									Thread.sleep(500);
								}
								
								
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
 }
