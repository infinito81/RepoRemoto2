package interfaz;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import dominio.Lecturas;

import java.awt.Font;

public class VentanaInsercionLectura extends JFrame{
	public VentanaInsercionLectura() {
		getContentPane().setLayout(null);
		setBounds(0, 0, 500, 500);
		
		tfHoras = new JTextField();
		tfHoras.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfHoras.setHorizontalAlignment(SwingConstants.CENTER);
		tfHoras.setText("00");
		tfHoras.setBounds(35, 41, 86, 42);
		getContentPane().add(tfHoras);
		tfHoras.setColumns(10);
		
		tfMinutos = new JTextField();
		tfMinutos.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfMinutos.setText("00");
		tfMinutos.setHorizontalAlignment(SwingConstants.CENTER);
		tfMinutos.setBounds(157, 41, 86, 42);
		getContentPane().add(tfMinutos);
		tfMinutos.setColumns(10);
		
		tfSegundos = new JTextField();
		tfSegundos.setHorizontalAlignment(SwingConstants.CENTER);
		tfSegundos.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfSegundos.setText("00");
		tfSegundos.setBounds(279, 41, 86, 42);
		getContentPane().add(tfSegundos);
		tfSegundos.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("HORAS");
		lblNewLabel.setBounds(56, 22, 46, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("MINUTOS");
		lblNewLabel_1.setBounds(176, 22, 56, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("SEGUNDOS");
		lblNewLabel_2.setBounds(296, 22, 69, 14);
		getContentPane().add(lblNewLabel_2);
		
		tfDorsal = new JTextField();
		tfDorsal.setFont(new Font("Tahoma", Font.PLAIN, 25));
		tfDorsal.setBounds(145, 134, 98, 51);
		getContentPane().add(tfDorsal);
		tfDorsal.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("DORSAL");
		lblNewLabel_3.setBounds(176, 114, 56, 14);
		getContentPane().add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("INSERTAR LECTURA");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tiempo = tfHoras.getText() + ":" + tfMinutos.getText() + ":" + tfSegundos.getText();
				String dorsal = tfDorsal.getText();
				
				//Instancia de Calendar con hora actual
				Calendar calendarLectura = Calendar.getInstance();
				//Le asigno el tiempo inicial, por tanto ahora tengo la fecha y hora exacta a la que comenzó la carrera
				calendarLectura.setTimeInMillis(tiempoInicial);
				//Le sumo horas, minutos y segundos de la lectura
				calendarLectura.add(Calendar.HOUR_OF_DAY, Integer.parseInt(tfHoras.getText()));
				calendarLectura.add(Calendar.MINUTE, Integer.parseInt(tfMinutos.getText()));
				calendarLectura.add(Calendar.SECOND, Integer.parseInt(tfSegundos.getText()));
				
				int resultado = Lecturas.guardaLectura(idEventoSeleccionado, carreraSeleccionada, "R", dorsal, "", tiempo, calendarLectura.getTimeInMillis());
				
				if (resultado > 0) {
					JOptionPane.showMessageDialog(null, "Lectura retrasada insertada CORRECTAMENTE");
				} else {
					
				}
			}
		});
		btnNewButton.setBounds(251, 250, 205, 50);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("SALIR");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarVentana();
			}
		});
		btnNewButton_1.setBounds(251, 318, 205, 98);
		getContentPane().add(btnNewButton_1);
	}
	
	private void cerrarVentana() {
		this.dispose();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long tiempoInicial;
	private int idEventoSeleccionado;
	private int carreraSeleccionada;
	private JTextField tfHoras;
	private JTextField tfMinutos;
	private JTextField tfSegundos;
	private JTextField tfDorsal;
	
	
	public void setMillisegundosInicioCarrera(long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
		
	}

	public void setIdEventoSeleccionado(int idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
		
	}

	public void setCarreraSeleccionada(int carreraSeleccionada) {
		this.carreraSeleccionada = carreraSeleccionada;
		
	}
}
