package imagenes;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class GestionaImagen extends javax.swing.JPanel {
	
	String file;
	
	public GestionaImagen(String file) {
		//this.setSize(300, 400); //se selecciona el tamaño del panel
		this.file = file;
	}

//Se crea un método cuyo parámetro debe ser un objeto Graphics

	public void paint(Graphics grafico) {
		Dimension height = getSize();

		//Se selecciona la imagen que tenemos en el paquete de la //ruta del programa

		ImageIcon Img = new ImageIcon(getClass().getResource("/images/" + file)); 

		//se dibuja la imagen que tenemos en el paquete Images //dentro de un panel

		grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);

		setOpaque(false);
		super.paintComponent(grafico);
	}
}