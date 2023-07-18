package estaticos;


import java.util.Calendar;

import javax.swing.JLabel;
import javax.xml.ws.handler.Handler;

public class Cronometro implements Runnable
{
    // Atributos privados de la clase
    //private TextView etiq;                  // Etiqueta para mostrar la información
	private JLabel etiq;
    private String nombrecronometro;        // Nombre del cronómetro
    private int segundos, minutos, horas;   // Segundos, minutos y horas que lleva activo el cronómetro
    private Handler escribirenUI;           // Necesario para modificar la UI
    private Boolean pausado;                // Para pausar el cronómetro
    private String salida;                  // Salida formateada de los datos del cronómetro

    private long tiempoInicial;             //Milisegundos absolutos del tiempo inicial
    private long tiempoFinal;               //Milisegundos absolutos del tiempo final
    
    

    /**
     * Constructor de la clase
     * @param nombre Nombre del cronómetro
     * @param etiqueta Etiqueta para mostrar información
     */
    public Cronometro(String nombre, JLabel etiqueta, long tiempoInicial)
    {
        etiq = etiqueta;
        salida = "";
        segundos = 0;
        minutos = 0;
        horas = 0;
        nombrecronometro = nombre;
        //escribirenUI = new Handler();
        pausado = Boolean.FALSE;
        if (tiempoInicial < 0)
            this.tiempoInicial = System.currentTimeMillis();
        else
            this.tiempoInicial = tiempoInicial;
    }

    @Override
    /**
     * Acción del cronómetro, contar tiempo en segundo plano
     */
    public void run()
    {
        try
        {
            while(Boolean.TRUE)
            {
                Thread.sleep(56);
                    salida = "";
                    if( !pausado )
                    {
                    tiempoFinal = System.currentTimeMillis();
                    salida = formateaTiempo(tiempoFinal - tiempoInicial);
                    // Modifico la UI
                    try
                    {
                                etiq.setText(salida);

                    }
                    catch (Exception e)
                    {
                        //Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + " al escribir en la UI: " + e.toString());
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            //Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + ": " + e.toString());
        }
    }


    private String formateaTiempo (long tiempoExacto){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tiempoExacto);

        int millis = calendar.get(Calendar.MILLISECOND);
        int segundos = calendar.get(Calendar.SECOND);
        int minutos = calendar.get(Calendar.MINUTE);
        int horas = calendar.get(Calendar.HOUR_OF_DAY)-1;

        String sMillis = millis < 10 ? "0" + millis : millis + "";
        sMillis = millis < 100 ? "0" + sMillis : sMillis;
        String sSegundos = segundos < 10 ? "0" + segundos : segundos + "";
        String sMinutos = minutos < 10 ? "0" + minutos : minutos + "";
        String sHoras = horas < 10 ? "0" + horas : horas + "";

        return (sHoras + ":" + sMinutos + ":" + sSegundos + "." + sMillis);
        //return (tiempoFinalCorredor + "");
    }
    /**
     * Reinicia el cronómetro
     */
    public void reiniciar()
    {
        segundos = 0;
        minutos = 0;
        horas = 0;
        pausado = Boolean.FALSE;
    }

    /**
     * Pausa/Continua el cronómetro
     */
    public void pause()
    {
        pausado = !pausado;
    }
    
    public long getTiempoFinal() {
    	return tiempoFinal;
    }

}