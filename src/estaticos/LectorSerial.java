package estaticos;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JTextField;
import javax.xml.bind.DatatypeConverter;



  public class LectorSerial  {
	  
	/*
	 * public static void main(String[] args) { new LectorSerial(); }
	 */ 
	  
	  
  
      //inicializamos y decalramos variables
    CommPortIdentifier portId;
    Enumeration puertos;
    //SerialPort serialport;
    static InputStream entrada = null;
    JTextField campoDorsal;
    Thread t;
    Thread t2;
    
    
    
    
    //creamos un constructor para realizar la conexion del puerto
    public LectorSerial(JTextField campoDorsal) {
    	
        super();
        this.campoDorsal = campoDorsal;
        
        puertos = CommPortIdentifier.getPortIdentifiers();
        
        t = new Thread(new LeerSerial());
        t2 = new Thread(new LeerSerial());
        
        while (puertos.hasMoreElements()) { //para recorrer el numero de los puertos, y especificar con cual quiero trabajar 
            //hasmorelements mientras tenga mas eleementos
            portId = (CommPortIdentifier) puertos.nextElement(); //next elemento recorre uno por uno
            System.out.println(portId.getName()); //puertos disponbibles
            if (portId.getName().equalsIgnoreCase("COM10")) {
            	SerialPort puerto_ser = null;
                try {
                	
                	puerto_ser = (SerialPort) portId.open("LecturaSerial", 500);
                    int baudRate = 9600; // 9600bps
                    //configuracion de arduino
                     puerto_ser.setSerialPortParams(
                             baudRate,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
                     
                     System.out.println("Puerto abierto");
                     puerto_ser.setDTR(true);	
                	
                //serialport= (SerialPort)portId.open("LecturaSerial", 500);//tiempo en ms
                    entrada = puerto_ser.getInputStream();//esta variable del tipo InputStream obtiene el dato serial
                t.start(); // inciamos el hilo para realizar nuestra accion de imprimir el dato serial
              
            } catch (IOException e) {
            	System.out.println("Excepción abriendo puerto " + portId.getName());
            } catch (Exception e) {
            	System.out.println("Excepción abriendo puerto " + portId.getName());
            } 
           } 
       }
  }
    //con este metodo del tipo thread relaizamos 
 


	public static class LeerSerial implements Runnable {
       int aux;
       public void run () {
           while(true){
              try {
            	  //System.out.println("leyendooooo");
                //aux = entrada.read(); // aqui estamos obteniendo nuestro dato serial
                int aux = entrada.read();
                Thread.sleep(10);
                if (aux>-1) {
                	byte[] array = {(byte)aux};
                    //System.out.println(String.format("%02X ", aux));//imprimimos el dato serial
                	System.out.print(DatatypeConverter.printHexBinary(array) + " ");
                }               
            } catch (Exception e) {
            } } }
  }

}
