package interfaz;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;


public class JPanelFondo extends JPanel {
	private Image imagen;
	 
    
 
    public JPanelFondo(Image imagen) {
		super();
		this.imagen = imagen;
	}



	@Override
    public void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(),
                        this);
 
        setOpaque(false);
        super.paint(g);
    }
}
