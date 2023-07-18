package estaticos;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author enigma7
 *
 
 */
public class Log extends Logger 
{
	// La primera vez que se invoque a la clase, se configura el Logger
	static
	{
		System.out.println ("Popularia Log: Configuramos el fichero de log : /config/log4j.properties" );
		PropertyConfigurator.configure("config/log4j.properties");
	}
	
	/**
	 * Constructor
	 *
	 */
	public Log (){
		super (Log.class.toString());
	};
	
	public static Logger getLogger (Class clase)
	{
		return Logger.getLogger(clase);
	}
}

