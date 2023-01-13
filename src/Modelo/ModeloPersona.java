package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloPersona extends Persona {

    public ModeloPersona() {
    }

    public ModeloPersona(String idpersona, String nombres, String apellidos, Date fechanac, String telefono, String sexo, double sueldo, int cupo, Byte foto) {
        super(idpersona, nombres, apellidos, fechanac, telefono, sexo, sueldo, cupo, foto);
    }

    public List<Persona> ListPersonas() {
        List<Persona> lista = new ArrayList<>();
        String sql = "SELECT idpersona, nombres, telefono, fechanacimiento, sexo, sueldo, cupo FROM \"Persona\"";
        ConectPG conpq = new ConectPG();
        ResultSet rs = conpq.Consulta(sql);
        try {
            while (rs.next()) {
                Persona per = new Persona();
                per.setIdpersona(rs.getString(1));
                per.setNombres(rs.getString(2));
                //per.setApellidos(rs.getString("apellidos"));
                per.setTelefono(rs.getString(3));
                per.setFechanac(rs.getDate(4));
                per.setSexo(rs.getString(5));
                per.setSueldo(rs.getDouble(6));
                per.setCupo(rs.getInt(7));
                lista.add(per);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Persona> SearchListPersonas() {
        List<Persona> lista = new ArrayList<>();
        String sql = "SELECT idpersona, nombres, telefono, fechanacimiento, sexo, sueldo, cupo FROM \"Persona\" WHERE idpersona like '%" + getIdpersona() +"%'";
        
        ConectPG conpq = new ConectPG();
        ResultSet rs = conpq.Consulta(sql);
        try {
            while (rs.next()) {
                Persona per = new Persona();
                per.setIdpersona(rs.getString(1));
                per.setNombres(rs.getString(2));
                //per.setApellidos(rs.getString("apellidos"));
                per.setTelefono(rs.getString(3));
                per.setFechanac(rs.getDate(4));
                per.setSexo(rs.getString(5));
                per.setSueldo(rs.getDouble(6));
                per.setCupo(rs.getInt(7));
                lista.add(per);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public SQLException GrabaPersonaDB() {
        String sql = "INSERT INTO \"Persona\" (idpersona, nombres, fechanacimiento, telefono, sexo, "
                + "sueldo, cupo, foto) VALUES ('" + getIdpersona() + "','" + getNombres() + "',"
                + "'" + getFechanac() + "','" + getTelefono() + "','" + getSexo() + "','" + getSueldo() + "','" + getCupo() + "',"
                + "'" + getFoto() + "')"; //REVISAR EL INSERT 

        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }

    public SQLException EditPersonaDB() {
        String sql = "UPDATE \"Persona\" SET nombres = '" + getNombres() +"',"
                + " fechanacimiento = '" + getFechanac() + "',telefono = '" + getTelefono() +"', sexo = '" + getSexo() + "'"
                + ",sueldo = '" + getSueldo() + "', cupo = '" + getCupo() + "', foto = '" + getFoto() +"' WHERE idpersona = '" + getIdpersona() + "'";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
    
    public SQLException DeleteLogicPerson(){
        String sql = "UPDATE \"Persona\" SET activo = false WHERE idpersona = '" + getIdpersona() + "'";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
    
    public SQLException DeletePhisicPerson(){
        String sql = "DELETE FROM \"Persona\" WHERE idpersona = '" + getIdpersona() + "'";
        
        ConectPG con = new ConectPG();
        SQLException ex = con.Accion(sql);
        return ex;
    }
}
