package persistencia;

/* Clase compuesta por todos aquellos metodos que acceden a la BBDD */

import java.sql.*;


public class Agente{
        protected static Agente mInstancia = null;
        protected Connection conexion;

        // Constructor
        protected Agente() throws Exception{
             Class.forName("org.sqlite.JDBC");
             conexion = DriverManager.getConnection("jdbc:sqlite:popularia.sqlite");            
        }

        // Metodo get de Agente
        public static Agente getAgente() throws Exception{
                if (mInstancia == null){
                        mInstancia = new Agente();
                }
                return mInstancia;
        }


        // Ejecuta una sentencia de tipo READ
        public ResultSet select(String sentencia) throws Exception {
                Statement stmt = conexion.createStatement();
                ResultSet resultSet = stmt.executeQuery(sentencia);
                //stmt.close();
                return resultSet;                
        }


        // Ejecuta una sentencia de tipo INSERT
        public int insert(String sentencia) throws Exception {
                Statement stmt = conexion.createStatement();
                int resul = stmt.executeUpdate(sentencia);
                stmt.close();
                
                return resul;
        }


        // Ejecuta una sentencia de tipo DELETE
        public int delete(String sentencia) throws Exception {
                Statement stmt = conexion.createStatement();
                int elementosBorrados = stmt.executeUpdate(sentencia);
                stmt.close();
                return elementosBorrados;
        }


        // Ejecuta una sentencia de tipo UPDATE
        public void update(String sentencia) throws Exception {
                Statement stmt = conexion.createStatement();
                stmt.executeUpdate(sentencia);
                stmt.close();
        }

}// Fin de la clase Agente
