package interfaz;

import java.awt.Toolkit;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Dimension;



public class Popularia {
    boolean packFrame = false;

    
    /**
     * Construct and show the application
     */
    public Popularia() {
    	
   	
    	VentanaInicio frame = new VentanaInicio();
        
        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        

        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);

        frame.setVisible(true);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Application entry point.
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                /*try {
                    //UIManager.setLookAndFeel(UIManager.
                    //        getSystemLookAndFeelClassName());
                    
                    UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }*/
            	try {
            	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            	        //if ("Windows".equals(info.getName())) {
            	        if (info.getName().startsWith("Nimbus")) {	
            	            UIManager.setLookAndFeel(info.getClassName());
            	            break;
            	        }
            	    }
            	} catch (Exception e) {
            	    // If Nimbus is not available, you can set the GUI to another look and feel.
            	}

                new Popularia();
            }
        });
    }

    private void jbInit() throws Exception {
    }
}