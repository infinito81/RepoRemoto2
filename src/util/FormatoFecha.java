package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.MaskFormatter;

/** Mascara para fecha/hora a nuestro gusto */
public class FormatoFecha extends MaskFormatter
{
	private static final long serialVersionUID = 1L;

	/** Se construye con el patrón deseado */
    public FormatoFecha() throws ParseException
    {
        // Las # son cifras y representa "dd/MM/yyyy"
        super ("##/##/####");
    }

    /** Una clase adecuada para convertir Date a String y viceversa de forma cómoda. Puedes ver cómo se hace el patrón "dd/MM/yy kk:mm:ss" en la API.
        El patrón que pongamos aquí debe cuadrar correctamente con la máscara que hemos puesto en el constructor */
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    
    private static SimpleDateFormat formatoDateToLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** Convierte el texto del editor en un Date */
    public Object stringToValue(String text) throws ParseException
    {
        return formato.parseObject(text);
    }

    /** Redibe un Date o null y debe convertirlo a texto que cumpla el patrón indicado anteriormente */
    public String valueToString(Object value) throws ParseException
    {
        if (value instanceof Date)
            return formato.format((Date)value);
        return formato.format(new Date());
    }
    
    public static long stringDateToLong (String sDate) {
    	try {
			Date date = formatoDateToLong.parse(sDate);
			return date.getTime();
		} catch (ParseException e) {
			
		}
    	return 0;
    }

	public static String calculaTiempoDescuento(String tiempoFinal, int retraso) {
		
		//System.out.println("tiempo final sin descuento " + tiempoFinal);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempoFinal.substring(0, 2))+1);
		calendar.set(Calendar.MINUTE, Integer.parseInt(tiempoFinal.substring(3, 5)));
		calendar.set(Calendar.SECOND, Integer.parseInt(tiempoFinal.substring(6, 8)));
		
		calendar.add(Calendar.SECOND, (-1) * retraso);
		
    	int horas = calendar.get(Calendar.HOUR_OF_DAY) - 1;
    	int segundos = calendar.get(Calendar.SECOND);
    	int minutos = calendar.get(Calendar.MINUTE);
    	
    	String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
    	String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";  
    	String sHoras = horas < 10 ? "0" + horas : horas + "";
    	
    	//System.out.println("tiempo final con descuento " + (sHoras + ":" + sMinutos + ":" + sSegundos));
    	
    	return (sHoras + ":" + sMinutos + ":" + sSegundos);	
	}
	
	public static String getHoraSistema() {
		Calendar calendar = Calendar.getInstance();
		
		return (calendar.get(Calendar.HOUR_OF_DAY)  + " : " + calendar.get(Calendar.MINUTE)  + " : " + calendar.get(Calendar.SECOND) + " . " + calendar.get(Calendar.MILLISECOND));
	}
    
    
}