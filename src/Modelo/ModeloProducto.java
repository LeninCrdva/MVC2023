/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenin
 */
public class ModeloProducto extends Producto{

    public ModeloProducto() {
    }

    public ModeloProducto(int idproducto, String nombre, double precio, int cantidad, byte foto, String descripcion) {
        super(idproducto, nombre, precio, cantidad, foto, descripcion);
    }
    
    public List<Producto> ListProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idproducto, nombre, precio, cantidad, descripcion FROM \"Producto\"";
        ConectPG conpq = new ConectPG();
        ResultSet rs = conpq.Consulta(sql);
        try {
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setIdproducto(rs.getInt(1));
                pro.setNombre(rs.getString(2));
                //per.setApellidos(rs.getString("apellidos"));
                pro.setPrecio(rs.getDouble(3));
                pro.setCantidad(rs.getInt(4));
                pro.setDescripcion(rs.getString(5));
                lista.add(pro);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Producto> SearchListProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idproducto, nombre, precio, cantidad, descripcion FROM \"Producto\" WHERE idproducto = " + getIdproducto();
        
        ConectPG conpq = new ConectPG();
        ResultSet rs = conpq.Consulta(sql);
        try {
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setIdproducto(rs.getInt(1));
                pro.setNombre(rs.getString(2));
                pro.setPrecio(rs.getDouble(3));
                pro.setCantidad(rs.getInt(4));
                pro.setDescripcion(rs.getString(5));
                lista.add(pro);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public SQLException GrabaProductoDB() {
        String sql = "INSERT INTO \"Producto\" (idproducto, nombre, precio, cantidad, descripcion)"
                + " VALUES (" + getIdproducto() + ",'" + getNombre() + "',"
                + "" + getPrecio() + "," + getCantidad() + ",'" + getDescripcion() + "')"; //REVISAR EL INSERT 

        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }

    public SQLException EditProductoDB() {
        String sql = "UPDATE \"Producto\" SET nombre = '" + getNombre() +"',"
                + " precio = " + getPrecio() + ",cantidad = " + getCantidad() +", descripcion = '" + getDescripcion() 
                +"' WHERE idproducto = " + getIdproducto() + "";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
    
    public SQLException DeleteLogicProducto(){
        String sql = "UPDATE \"Producto\" SET activo = false WHERE idproducto = " + getIdproducto() + "";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
    
    public SQLException DeletePhisicProducto(){
        String sql = "DELETE FROM \"Producto\" WHERE idproducto = " + getIdproducto() + "";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
    
    public boolean isUsed(){
        boolean rturn = false;
        String sql = "SELECT count(idproducto) FROM \"DetalleFactura\"";
        ConectPG conpq = new ConectPG();
        ResultSet rs = conpq.Consulta(sql);
        try {
            rturn = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rturn;
    }
    
    public String NoSerie(){
        String serie = "";
        String sql ="SELECT MAX(idproducto) FROM \"Producto\"";
        
        ConectPG con = new ConectPG();
        ResultSet rs = con.Consulta(sql);
        
        try{
            while(rs.next()){
                serie = rs.getString(1);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return serie;
    }
}
