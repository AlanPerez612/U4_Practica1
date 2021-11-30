/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Trix
 * AQUÍ SE INSERTAN LOS MÉTODOS: CRUD.
 */
public class BaseDatos {
    Connection conexion;
    Statement transaccion;
    ResultSet cursor;
    
    
    public BaseDatos(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/practica1?zeroDateTimeBehavior=CONVERT_TO_NULL","root","");
            transaccion = conexion.createStatement();
            
        }catch(SQLException ex){
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean insertar(Producto producto){
        String SQL_Insert ="INSERT INTO PRODUCTOS VALUES(NULL,'%DESC%','%PREC%','%EXIS%')";
        
        // Utilizar conversiones tipo: Float - String & Integer - String
        // String p=Float.toString(producto.precio) ;
        // String e = Integer.toString(producto.existencia);
        
        SQL_Insert = SQL_Insert.replaceAll("%DESC%", producto.descripcion);
        SQL_Insert = SQL_Insert.replaceAll("%PREC%", Float.toString(producto.precio));
        SQL_Insert = SQL_Insert.replaceAll("%EXIS%", Integer.toString(producto.existencia));
        
        try {
            transaccion.execute(SQL_Insert);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public ArrayList<String[]> consultarDatos(){
        ArrayList<String[]> resultado = new ArrayList<String[]>();
        try {
            cursor=transaccion.executeQuery("SELECT * FROM PRODUCTOS");
            if(cursor.next()){
                do{
                    String[] renglon= { cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4) };
                    
                    resultado.add(renglon);
                }while(cursor.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }
    public Producto consultarID(String id){
        Producto productoResultado = new Producto();
        
        try {
            cursor = transaccion.executeQuery("SELECT * FROM PRODUCTOS WHERE IDPRODUCTOS="+id);
            if(cursor.next()){
                productoResultado.descripcion = cursor.getString(2);
                productoResultado.precio = cursor.getShort(3);
                productoResultado.existencia = cursor.getShort(4);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productoResultado;
    }
    
    public boolean eliminar(String idBorrar){
        try {
            transaccion.execute("DELETE FROM PRODUCTOS WHERE IDPRODUCTOS="+idBorrar);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean update(Producto producto){
        String update ="UPDATE PRODUCTOS SET DESCRIPCION='%DESC%',PRECIO='%PREC%',EXISTENCIA='%EXIS%' WHERE IDPRODUCTOS="+producto.id;
        
        update = update.replaceAll("%DESC%", producto.descripcion);
        update = update.replaceAll("%PREC%", Float.toString(producto.precio));
        update = update.replaceAll("%EXIS%", Integer.toString(producto.existencia));
        
        try {
            transaccion.executeUpdate(update);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    
}
