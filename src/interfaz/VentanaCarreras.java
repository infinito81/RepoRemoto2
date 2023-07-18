package interfaz;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.DatatypeConverter;

import persistencia.dao.model.TCorredores;
import util.Estatica;
import dominio.Carreras;
import dominio.Resultados;
import estaticos.LectorSerial;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class VentanaCarreras extends JFrame{

	private static final long serialVersionUID = 1L;
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	JLabel lLblCronometro [] = new JLabel[5];
	Thread hilo = null;
	long tiempoInicial []= new long [5];
	long tiempoFinal [] = new long [5];
	boolean hiloCorriendo = false;
	JLabel lLblLlegadosDorsal [] = new JLabel[5];
	JTextField lTxtDorsal [] = new JTextField[5];
	JLabel lLblCategorias [] = new JLabel[5];
	
	//lectores seriales
	Enumeration puertos_libres = null;
	CommPortIdentifier port = null;
	SerialPort puerto_ser = null;
	InputStream in = null;
	Thread timer;
	
	boolean hilosCorriendo [] = new boolean[5];
	
	
	int idEvento = 0;
	int pestanyaSeleccionada = 0;
	
	TCorredores ultimosCorredores [][] = new TCorredores[5][3]; //indice i: carrera ;;; indice j: corredor
	
	public VentanaCarreras() {
		getContentPane().setLayout(null);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (JOptionPane.showConfirmDialog(getContentPane(), "Asegúrese de parar el cronómetro de la carrera antes de cambiar a otra", "ALERTA", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
					Estatica.getInstance().setCarreraSeleccionada(tabbedPane.getSelectedIndex());
					Estatica.getInstance().setDescCarreraSeleccionada(lLblCategorias[tabbedPane.getSelectedIndex()].getText());
				} else {
					System.out.println("Cambio de pestaña cancelado");
					tabbedPane.setSelectedIndex(Estatica.getInstance().getCarreraSeleccionada());
				}
			}
		});
		
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 550, 450);
		getContentPane().add(tabbedPane);
        setBounds(250, 250, 560, 480);
		this.setResizable(false);
		this.setTitle("POPULARIA");
		
		JPanel paneles [] = new JPanel[5]; 
		JButton lBtnIniciar [] = new JButton[5];
		JButton lBtnParar [] = new JButton[5];
		JButton lBtnBorrarDorsal [] = new JButton[5];
		JLabel lLblDorsal [] =  new JLabel[5];
				
		for (int i=0; i<paneles.length; i++){
			paneles[i] = new JPanel();
			paneles[i].setLayout(null);
			lLblCategorias [i] = new JLabel(); 
			lBtnIniciar [i] = new JButton("INICIAR");
			lBtnParar [i] = new JButton("PARAR");
			lBtnBorrarDorsal [i] = new JButton("BORRAR DORSAL");
			lLblCronometro [i] = new JLabel();
			lLblDorsal [i] = new JLabel(); 
			lTxtDorsal [i] = new JTextField();
			lLblLlegadosDorsal [i] = new JLabel();
		}
		
		lLblCategorias[4].setText("BENJAMÍN");
		lLblCategorias[3].setText("ALEVÍN");
		lLblCategorias[2].setText("INFANTIL");
		lLblCategorias[1].setText("CADETE");
		lLblCategorias[0].setText("ABSOLUTA");
		
		Estatica.getInstance().setTiempoInicial(-1);
		Estatica.getInstance().setCarreraSeleccionada(0);
		Estatica.getInstance().setDescCarreraSeleccionada("ABSOLUTA");
		
		//Lector SERIAL
		prepararLectoresSerie();
		
		for (int i=0; i<paneles.length; i++){
			tabbedPane.add(paneles[i], lLblCategorias[i].getText());
			lLblCategorias[i].setFont(new Font("Calibri", Font.BOLD, 20));
			lLblCategorias[i].setBounds(220, 20, 100, 20);
			paneles[i].add(lLblCategorias[i]);
			
			lBtnIniciar[i].setBounds(40, 70, 120, 55);
			paneles[i].add(lBtnIniciar[i]);
			
			lBtnIniciar[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//CRONOMETRO
					preparaCronometro();
					hilo.start();	
					hiloCorriendo = true;
					lBtnParar[tabbedPane.getSelectedIndex()].setEnabled(true);
					lBtnIniciar[tabbedPane.getSelectedIndex()].setEnabled(false);
					
					//lectores
					hilosCorriendo[tabbedPane.getSelectedIndex()] = true;
					timer = new Thread(new ImplementoRunnable());
					timer.start();
				}
			});
			
			lBtnParar[i].setBounds(190, 70, 120, 55);
			paneles[i].add(lBtnParar[i]);
			lBtnParar[i].setEnabled(false);
			lBtnParar[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//preparaCronometro();
					
					//CRONOMETRO
					hilo.interrupt();					
					hiloCorriendo = false;
					lBtnParar[tabbedPane.getSelectedIndex()].setEnabled(false);
					lBtnIniciar[tabbedPane.getSelectedIndex()].setEnabled(true);
					
					
					//PARO LECTORES SERIE
					hilosCorriendo[tabbedPane.getSelectedIndex()] = false;					
				}
			});
			
			lLblCronometro[i].setHorizontalAlignment(SwingConstants.CENTER);
			lLblCronometro[i].setFont(new Font("LCD Display Grid", Font.BOLD, 20));
			lLblCronometro[i].setText("00:00:000");
			lLblCronometro[i].setBounds(330, 70, 150, 55);
			paneles[i].add(lLblCronometro[i]);

			lLblDorsal[i].setText("DORSAL: ");
			lLblDorsal[i].setFont(new Font("Calibri", Font.BOLD, 18));
			lLblDorsal[i].setBounds(60, 150, 120, 55);
			paneles[i].add(lLblDorsal[i]);

			lTxtDorsal[i].setBounds(190, 150, 200, 50);
			lTxtDorsal[i].setFont(new Font("Calibri", Font.BOLD, 18));
			lTxtDorsal[i].setHorizontalAlignment(SwingConstants.CENTER);			
			paneles[i].add(lTxtDorsal[i]);
			

			
			
			lLblLlegadosDorsal[i].setHorizontalAlignment(SwingConstants.CENTER);
			lLblLlegadosDorsal[i].setFont(new Font("Calibri", Font.BOLD, 18));
			lLblLlegadosDorsal[i].setText("");
			lLblLlegadosDorsal[i].setBounds(20, 175, 500, 200);
			paneles[i].add(lLblLlegadosDorsal[i]);

			lTxtDorsal[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER){
						String sDorsal = lTxtDorsal[tabbedPane.getSelectedIndex()].getText();
						lTxtDorsal[tabbedPane.getSelectedIndex()].setText("");
						System.out.println("Dorsal: " + sDorsal);
						if (sDorsal.length()==7){							
							System.out.println("Tiempo: " + lLblCronometro[tabbedPane.getSelectedIndex()].getText() + "\n");							
							
							TCorredores corredor = Resultados.guardarResultado(tabbedPane.getSelectedIndex() + 1, sDorsal.substring(3, 7), tiempoFinal[tabbedPane.getSelectedIndex()]);
							
							if (corredor == null){
								JOptionPane.showMessageDialog(getContentPane(), "Fallo al grabar tiempo del dorsal " + sDorsal); 
								//TODO en un ficherito podemos grabar todos los errores por si sirvieran para remendar las clasificaciones
							} else {
								corredor.setTIEMPO_TOTAL_FORMATEADO(formateaTiempo(tiempoFinal[tabbedPane.getSelectedIndex()]));	
								actualizaUltimosCorredores(corredor);
								actualizaEtiquetaUltimosDorsales();								
							}
														

						}
					}					
				}
			});		
			
			lTxtDorsal[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER){
						String sDorsal = lTxtDorsal[tabbedPane.getSelectedIndex()].getText();
						lTxtDorsal[tabbedPane.getSelectedIndex()].setText("");
						System.out.println("Dorsal: " + sDorsal);
						if (sDorsal.length()==7){							
							System.out.println("Tiempo: " + lLblCronometro[tabbedPane.getSelectedIndex()].getText() + "\n");							
							
							TCorredores corredor = Resultados.guardarResultado(tabbedPane.getSelectedIndex() + 1, sDorsal.substring(3, 7), tiempoFinal[tabbedPane.getSelectedIndex()]);
							
							if (corredor == null){
								JOptionPane.showMessageDialog(getContentPane(), "Fallo al grabar tiempo del dorsal " + sDorsal); 
								//TODO en un ficherito podemos grabar todos los errores por si sirvieran para remendar las clasificaciones
							} else {
								corredor.setTIEMPO_TOTAL_FORMATEADO(formateaTiempo(tiempoFinal[tabbedPane.getSelectedIndex()]));	
								actualizaUltimosCorredores(corredor);
								actualizaEtiquetaUltimosDorsales();								
							}
														

						}
					}					
				}
			});	
			
			lTxtDorsal[i].getDocument().addDocumentListener(new DocumentListener() {
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
					     if (lTxtDorsal[tabbedPane.getSelectedIndex()].getText().length()>0){
					    	 String sDorsal = lTxtDorsal[tabbedPane.getSelectedIndex()].getText();
					    	 //lTxtDorsal[tabbedPane.getSelectedIndex()].setText("");
					    	 System.out.println("Dorsal: " + sDorsal);				       
					     }
					  }
					});			
		}	
	}
	
	private void prepararLectoresSerie() {
		timer = new Thread(new ImplementoRunnable());
		puertos_libres = CommPortIdentifier.getPortIdentifiers();
		int aux = 0;
		
		while (puertos_libres.hasMoreElements()) {
			port = (CommPortIdentifier) puertos_libres.nextElement();
			HashSet<String> puertosLectores = new HashSet<String>();
			puertosLectores.add("COM4");
			if (puertosLectores.contains(port.getName())) {
				try {
					puerto_ser = (SerialPort) port.open("puerto serial", 500);
					int baudRate = 9600;
					puerto_ser.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					//puerto_ser.setDTR(true);
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

	private void actualizaUltimosCorredores (TCorredores ultimoCorredor) {
		TCorredores auxiliar = null;
		TCorredores auxiliar2 = null;
		
		if (ultimosCorredores[tabbedPane.getSelectedIndex()][0] == null){
			ultimosCorredores[tabbedPane.getSelectedIndex()][0] = ultimoCorredor;
			return;
		} 
		
		if (ultimosCorredores[tabbedPane.getSelectedIndex()][1] == null){
			auxiliar = ultimosCorredores[tabbedPane.getSelectedIndex()][0];
			ultimosCorredores[tabbedPane.getSelectedIndex()][0] = ultimoCorredor;
			ultimosCorredores[tabbedPane.getSelectedIndex()][1] = auxiliar;
			return;			
		}		
		
		auxiliar = ultimosCorredores[tabbedPane.getSelectedIndex()][0];
		ultimosCorredores[tabbedPane.getSelectedIndex()][0] = ultimoCorredor;
			
		auxiliar2 = ultimosCorredores[tabbedPane.getSelectedIndex()][1];
		ultimosCorredores[tabbedPane.getSelectedIndex()][1] = auxiliar;
		
		ultimosCorredores[tabbedPane.getSelectedIndex()][2] = auxiliar2;
			
	}
	
	
	private String actualizaEtiquetaUltimosDorsales() {
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("<html>");
		sb.append("<table border='1'>");
		//sb.append("<tr>");
		//sb.append("<td align='center' width='1400' colspan='3'>ÚLTIMOS CORREDORES EN META</td>");
		//sb.append("</tr>");
		//sb.append("<tr>");
		//sb.append("<td align='center' width='100'>DORSAL</td>");
		//sb.append("<td align='center' width='1200'>NOMBRE</td>");
		//sb.append("<td align='center' width='100'>TIEMPO</td>");
		//sb.append("</tr>");
		
		if (ultimosCorredores[tabbedPane.getSelectedIndex()][0] != null){
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][0].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1200'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][0].getNOMBRE() + " " + ultimosCorredores[tabbedPane.getSelectedIndex()][0].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][0].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			sb.append("</tr>");
		} 

		if (ultimosCorredores[tabbedPane.getSelectedIndex()][1] != null){
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][1].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1200'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][1].getNOMBRE() + " " + ultimosCorredores[tabbedPane.getSelectedIndex()][1].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][1].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			sb.append("</tr>");
		}

		if (ultimosCorredores[tabbedPane.getSelectedIndex()][2] != null){
			sb.append("<tr>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][2].getDORSAL() + "</td>");
			sb.append("<td align='center' width='1200'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][2].getNOMBRE() + " " + ultimosCorredores[tabbedPane.getSelectedIndex()][2].getAPELLIDOS() + "</td>");
			sb.append("<td align='center' width='100'>" + ultimosCorredores[tabbedPane.getSelectedIndex()][2].getTIEMPO_TOTAL_FORMATEADO() + "</td>");
			sb.append("</tr>");
		}
		
		sb.append("</table>");
		sb.append("</html>");

		lLblLlegadosDorsal[tabbedPane.getSelectedIndex()].setText(sb.toString());
		
		Estatica.getInstance().setlLblLlegadosDorsal(lLblLlegadosDorsal);
		
		
		return sb.toString();
	}

	private void preparaCronometro(){
		hilo = new Thread(){
	        public void run(){
	        	tiempoInicial[tabbedPane.getSelectedIndex()] = System.currentTimeMillis();
	        	System.out.println("Tiempo inicial en milis: " + tiempoInicial[tabbedPane.getSelectedIndex()]);
	        	
	        	Carreras.guardarTiempoInicial(idEvento, tabbedPane.getSelectedIndex()+1, tiempoInicial[tabbedPane.getSelectedIndex()]);
	        	Estatica.getInstance().setDescCarreraSeleccionada(lLblCategorias[tabbedPane.getSelectedIndex()].getText());
	        	Estatica.getInstance().setCarreraSeleccionada(tabbedPane.getSelectedIndex());
	        	Estatica.getInstance().setTiempoInicial(tiempoInicial[tabbedPane.getSelectedIndex()]);
	        	
	        	lTxtDorsal[tabbedPane.getSelectedIndex()].requestFocus();
	        		        		        	
	            try{
	            	
	                while(true){
	                	tiempoFinal[tabbedPane.getSelectedIndex()] = System.currentTimeMillis();
	                	lLblCronometro[tabbedPane.getSelectedIndex()].setText(formateaTiempo(tiempoFinal[tabbedPane.getSelectedIndex()]));
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
    	calendar.setTimeInMillis(tiempoFinalCorredor - tiempoInicial[tabbedPane.getSelectedIndex()]);
    	
    	int millis = calendar.get(Calendar.MILLISECOND);
    	int segundos = calendar.get(Calendar.SECOND);
    	int minutos = calendar.get(Calendar.MINUTE);
    	
    	String sMillis = millis < 10 ? "0" + millis : millis + "";
    	sMillis = millis < 100 ? "0" + sMillis : sMillis;
    	String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
    	String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";  
    	
    	return ("" + sMinutos + ":" + sSegundos + "." + sMillis);		
	}
	
	
	
	private class ImplementoRunnable implements Runnable{
		int aux;
		
		@Override
		public void run() {
			boolean mandadoIntro = false;
			StringBuffer sb = new StringBuffer();
			while (hilosCorriendo[tabbedPane.getSelectedIndex()]) {
				try {
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
						System.out.println(sb.toString());
						lTxtDorsal[tabbedPane.getSelectedIndex()].setText(sb.toString());
						sb = new StringBuffer();
						lTxtDorsal[tabbedPane.getSelectedIndex()].setText("");
					}
					

					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
