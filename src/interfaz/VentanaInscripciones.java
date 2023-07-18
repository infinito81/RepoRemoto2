package interfaz;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import persistencia.dao.model.TCorredores;
import util.EnviosHTTP;
import dominio.Corredores;

public class VentanaInscripciones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txfNombre;
	private JTextField txfApellidos;
	private JTextField txfDni;
	private JTextField txfEmail;
	private JTextField txfPoblacion;
	private JTextField txfClub;
	JRadioButton rdbtnMasculino = new JRadioButton("Masculino");
	JRadioButton rdbtnFemenino = new JRadioButton("Femenino");
	//JDateChooser dateChooserFechaNacim = new JDateChooser();
	
	private ArrayList<TCorredores> todosCorredores;
	//private JTextField txfFechaNac;
	JTextField txfFechaNac = null;
	
	public VentanaInscripciones() {
		getContentPane().setLayout(null);
        setBounds(250, 250, 527, 450);
		this.setResizable(false);
		this.setTitle("POPULARIA");
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(27, 31, 60, 14);
		getContentPane().add(lblNombre);
		
		txfNombre = new JTextField();
		txfNombre.setBounds(107, 23, 220, 30);
		getContentPane().add(txfNombre);
		txfNombre.setColumns(10);
		
		JLabel lblApellidos = new JLabel("Apellidos");
		lblApellidos.setBounds(27, 70, 60, 14);
		getContentPane().add(lblApellidos);
		
		txfApellidos = new JTextField();
		txfApellidos.setBounds(107, 62, 220, 30);
		getContentPane().add(txfApellidos);
		txfApellidos.setColumns(10);
		

		txfFechaNac = new JTextField();
		txfFechaNac.setColumns(10);
		txfFechaNac.setBounds(124, 103, 203, 30);
		getContentPane().add(txfFechaNac);
		
		JLabel lblFechaNac = new JLabel("Fecha Nacimiento");
		lblFechaNac.setBounds(27, 110, 105, 14);
		getContentPane().add(lblFechaNac);
		
		JLabel lblDNI = new JLabel("DNI (+ 16 a\u00F1os)");
		lblDNI.setBounds(27, 157, 105, 14);
		getContentPane().add(lblDNI);
		
		txfDni = new JTextField();
		txfDni.setColumns(10);
		txfDni.setBounds(124, 149, 203, 30);
		getContentPane().add(txfDni);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(27, 198, 105, 14);
		getContentPane().add(lblEmail);
		
		txfEmail = new JTextField();
		txfEmail.setColumns(10);
		txfEmail.setBounds(124, 190, 203, 30);
		getContentPane().add(txfEmail);
		
		JLabel lblSexo = new JLabel("Sexo");
		lblSexo.setBounds(27, 231, 105, 14);
		getContentPane().add(lblSexo);
		
		rdbtnMasculino.setBounds(86, 227, 97, 23);
		getContentPane().add(rdbtnMasculino);
		
		rdbtnFemenino.setBounds(201, 227, 109, 23);
		getContentPane().add(rdbtnFemenino);
		
		ButtonGroup buttonGroupSexo = new ButtonGroup();
		buttonGroupSexo.add(rdbtnMasculino);
		buttonGroupSexo.add(rdbtnFemenino);
		
		JLabel lblPoblacion = new JLabel("Poblaci\u00F3n");
		lblPoblacion.setBounds(27, 264, 105, 14);
		getContentPane().add(lblPoblacion);
		
		txfPoblacion = new JTextField();
		txfPoblacion.setColumns(10);
		txfPoblacion.setBounds(124, 256, 203, 30);
		getContentPane().add(txfPoblacion);
		
		JLabel lblClub = new JLabel("Club");
		lblClub.setBounds(27, 305, 105, 14);
		getContentPane().add(lblClub);
		
		txfClub = new JTextField();
		txfClub.setColumns(10);
		txfClub.setBounds(124, 297, 203, 30);
		getContentPane().add(txfClub);
		
		JButton btnInscribir = new JButton("INSCRIBIR");
		btnInscribir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comprobacionesInscripcion()){
					TCorredores corredor = recogeDatosIncripcion();
					if (guardarCorredorYAsignaDorsal(corredor, true)){
						ponerCamposEnBlanco();
					}				
				}
			}
		});
		btnInscribir.setBounds(124, 338, 203, 58);
		getContentPane().add(btnInscribir);
		
		JButton btnUpload = new JButton("UPLOAD");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				todosCorredores = Corredores.getTodosCorredores();
				actualizaCorredoresServidor();
			}
		});
		btnUpload.setBounds(372, 66, 119, 58);
		getContentPane().add(btnUpload);
		
		JButton btnDownload = new JButton("DOWNLOAD");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				todosCorredores = Corredores.getTodosCorredoresArchivo();
				Collections.sort(todosCorredores, new OrdenarCorredoresPorID());
				if (todosCorredores!=null){
					for (TCorredores corredor : todosCorredores){
						guardarCorredorYAsignaDorsal(corredor, false);	
					}
					JOptionPane.showMessageDialog(getContentPane(), "Finalizada la descargar de la tabla de corredores del servidor");
				}
				
			}
		});
		btnDownload.setBounds(372, 174, 119, 58);
		getContentPane().add(btnDownload);
		
	}	
	
	protected void ponerCamposEnBlanco() {
		txfNombre.setText("");
		txfApellidos.setText("");
		txfFechaNac.setText("");
		txfEmail.setText("");
		txfClub.setText("");
		txfPoblacion.setText("");
		txfDni.setText("");
		rdbtnMasculino.setSelected(false);
		rdbtnFemenino.setSelected(false);
	}

	protected void actualizaCorredoresServidor() {
		
		for (TCorredores corredor : todosCorredores){
			
			String nombre = util.FormatoCadenas.formateaParaPHP(corredor.getNOMBRE().trim().toUpperCase());
			String apellidos = util.FormatoCadenas.formateaParaPHP(corredor.getAPELLIDOS().trim().toUpperCase()); 
			String email = util.FormatoCadenas.formateaParaPHP(corredor.getEMAIL().trim().toUpperCase());					
			String poblacion = util.FormatoCadenas.formateaParaPHP(corredor.getPOBLACION().trim().toUpperCase());
			String club = util.FormatoCadenas.formateaParaPHP(corredor.getCLUB().trim().toUpperCase());
			
			StringBuffer stb = new StringBuffer();
			stb.append("http://atletismonavalcan.hol.es/registro_get.php?id=");
			stb.append(corredor.getID());
			stb.append("&nombre=");
			stb.append(nombre);
			stb.append("&apellidos=");
			stb.append(apellidos);
			stb.append("&dni=");
			stb.append(corredor.getDNI().trim().toUpperCase());
			stb.append("&email=");
			stb.append(email);
			stb.append("&autoc2=");
			stb.append(poblacion);
			stb.append("&autoc1=");
			stb.append(club);
			stb.append("&gender=");
			stb.append(corredor.getSEXO().equals("MASC") ? "male" : "female");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaNac = null;
			String sFechaNac = corredor.getFECHA_NAC();
			try {
				fechaNac = sdf.parse(corredor.getFECHA_NAC());
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				sFechaNac = sdf.format(fechaNac);
				stb.append("&fechanac=");
				stb.append(sFechaNac);				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			stb.append("&dorsal=");
			stb.append(corredor.getDORSAL());
			System.out.println(stb.toString());
			try {
				String respuesta = EnviosHTTP.sendGet(stb.toString());
				if (respuesta.contains("CORRECTO")) respuesta = "CORRECTO";
				if (respuesta.contains("FALLO")) respuesta = "FALLO";
				System.out.println(corredor.getID() + ": " + respuesta);
			} catch (Exception e) {

			}
		}
		
	}
	
	private boolean comprobacionesInscripcion(){
		if (txfNombre.getText().trim().equals("")){
			JOptionPane.showMessageDialog(getContentPane(), "NOMBRE OBLIGATORIO", "Faltan datos", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (txfApellidos.getText().trim().equals("")){
			JOptionPane.showMessageDialog(getContentPane(), "APELLIDOS OBLIGATORIO", "Faltan datos", JOptionPane.ERROR_MESSAGE);
			return false;			
		} else if (txfFechaNac.getText().trim().equals("")){
			JOptionPane.showMessageDialog(getContentPane(), "APELLIDOS OBLIGATORIO", "Faltan datos", JOptionPane.ERROR_MESSAGE);
			return false;	
		}
		
		
		return true;
	}
	
	
	private TCorredores recogeDatosIncripcion (){
		
		TCorredores corredor = new TCorredores();
		
		corredor.setNOMBRE(txfNombre.getText().toUpperCase());
		corredor.setAPELLIDOS(txfApellidos.getText().toUpperCase());
		corredor.setDNI(txfDni.getText().toUpperCase());
		corredor.setEMAIL(txfEmail.getText().toUpperCase());
		
		if (rdbtnMasculino.isSelected())
			corredor.setSEXO("MASC");
		else 
			corredor.setSEXO("FEME");
		
		SimpleDateFormat formatea = new SimpleDateFormat("dd-MM-yyyy");
		Date dFechaNac = null;
		try {
			dFechaNac = formatea.parse(txfFechaNac.getText().trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		formatea = new SimpleDateFormat("yyyy-MM-dd");
		corredor.setFECHA_NAC(formatea.format(dFechaNac));
		corredor.setCATEGORIA(getCategoria(dFechaNac));

		corredor.setDORSAL("-1");
		corredor.setPOBLACION(txfPoblacion.getText().toUpperCase());
		corredor.setCLUB(txfClub.getText().toUpperCase());
		
		return corredor;		
	}

	private boolean guardarCorredorYAsignaDorsal (TCorredores corredor, boolean mostrarAviso){
						
		int resultado = Corredores.guardarCorredorNuevo(corredor);
		
		if (resultado < 0){
			JOptionPane.showMessageDialog(getContentPane(), "Error al guardar"); 
			return false;
		} else {
			String sUltimoDorsal = Corredores.getUltimoDorsal();
			int ultimoDorsal = Integer.parseInt(sUltimoDorsal);
			
			if (ultimoDorsal >= 601){
				sUltimoDorsal = "" + (ultimoDorsal+1);
				
			} else {
				sUltimoDorsal = "601";
			}
			int resultDorsal = Corredores.setDorsal(resultado , sUltimoDorsal);
			
			if (resultDorsal > 0){
				if (mostrarAviso)
					JOptionPane.showMessageDialog(getContentPane(), "Corredor guardado correctamente. Dorsal: " + sUltimoDorsal + ". Categoria: " + corredor.getCATEGORIA() + "-" + corredor.getSEXO());
					return true;		
			} else {
				JOptionPane.showMessageDialog(getContentPane(), "Error al actualizar dorsal del id " + resultado);
				return false;
			}
			
		}		
	}


	private String getCategoria(Date date) {
		Calendar comparar = Calendar.getInstance();
		comparar.set(Calendar.YEAR, 1976);
		comparar.set(Calendar.MONTH, Calendar.JULY);
		comparar.set(Calendar.DAY_OF_MONTH, 30);
		
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "VETERANO";
		
		comparar.set(Calendar.YEAR, 2000);
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "SENIOR";
		
		comparar.set(Calendar.YEAR, 2003);
		comparar.set(Calendar.MONTH, Calendar.JANUARY);
		comparar.set(Calendar.DAY_OF_MONTH, 1);		
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "CADETE";
		
		comparar.set(Calendar.YEAR, 2005);
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "INFANTIL";

		comparar.set(Calendar.YEAR, 2007);
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "ALEVIN";
		
		comparar.set(Calendar.YEAR, 2009);
		if (date.before(new Date(comparar.getTimeInMillis())))
			return "BENJAMIN";
		
		return "PREBENJAMIN";
	}
}

class OrdenarCorredoresPorID implements Comparator<TCorredores>{

	@Override
	public int compare(TCorredores arg0, TCorredores arg1) {
		return arg0.getID().compareTo(arg1.getID());
	}	
}