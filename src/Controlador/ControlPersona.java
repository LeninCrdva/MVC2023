/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.ModeloPersona;
import Modelo.Persona;
import Vista.VistaPersonas;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Lenin
 */
public class ControlPersona {

    private ModeloPersona modelo;
    private VistaPersonas vista;

    public ControlPersona() {
    }

    public ControlPersona(ModeloPersona modelo, VistaPersonas vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);
        //Aprovecho el constructor visible a mi vista
    }

    public void iniciaControl() {
        vista.getBtnUpdate().addActionListener(l -> cargaPersonas());//Agrega un listener para el boton actualizar
        vista.getBtnCreate().addActionListener(l -> abrirDialogo(1));
        vista.getBtnEdit().addActionListener(l -> abrirDialogo(2));
        vista.getButtonAgree().addActionListener(l -> crearEditarPersona());
        vista.getBtnEliminar().addActionListener(l -> eliminarPersona(vista.getTablePerson()));
        vista.getTxtBuscar().addActionListener(l -> buscarPersona());
    }

    private void cargaPersonas() {
        //Control para consultar a la BD/modelo y luego cargar en la vista
        List<Persona> listap = modelo.ListPersonas();

        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTablePerson().getModel();
        mTabla.setNumRows(0); //Limpio la tabla

        listap.stream().forEach(pe -> {
            String[] filanueva = {pe.getIdpersona(), pe.getNombres(), String.valueOf(pe.getFechanac()), pe.getTelefono(), pe.getSexo(), String.valueOf(pe.getSueldo()), String.valueOf(pe.getCupo())};
            mTabla.addRow(filanueva);
        });
    }

    private void abrirDialogo(int ce) {
        String title;
        boolean openwindow = false;
        if (ce == 1) {
            title = "Crear Persona";
            vista.getDlgCreate().setName("crear");
            openwindow = true;
        } else {
            title = "Editar Persona";
            vista.getDlgCreate().setName("editar");
            try {
                openwindow = uploadDates(vista.getTablePerson());
            } catch (ParseException ex) {
                Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(openwindow){
            vista.getDlgCreate().setLocationRelativeTo(null);
            vista.getDlgCreate().setSize(530, 400);
            vista.getDlgCreate().setTitle(title);
            vista.getDlgCreate().setVisible(true);
        }
    }

    private void crearEditarPersona() {
        if (vista.getDlgCreate().getName().equals("crear")) {
            //INSERTAR
            String cedula = vista.getTxtCed().getText();
            String nombre = vista.getTxtName().getText();
            Date fnac = vista.getDateNac().getDate();
            String telefono = vista.getTxtTelf().getText();
            String sex = vista.getTxtSex().getText();
            double sueldo = Double.parseDouble(vista.getTxtSalary().getText());
            int cupo = Integer.parseInt(vista.getTxtCup().getText());
            //byte foto = Byte.valueOf(vista.gettxtFoto().getText());

            ModeloPersona persona = new ModeloPersona();
            persona.setIdpersona(cedula);
            persona.setNombres(nombre);
            persona.setFechanac(fnac);
            persona.setTelefono(telefono);
            persona.setSexo(sex);
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);

            if (persona.GrabaPersonaDB() == null) {
                JOptionPane.showMessageDialog(null, "NO SE HA PODIDO CREAR LA PERSONA");
            } else {
                JOptionPane.showMessageDialog(null, "SE HA CREADO A LA PERSONA CON ÉXITO");
            }
        } else if (vista.getDlgCreate().getName().equals("editar")) {
            //EDITAR
            String cedula = vista.getTxtCed().getText();
            String nombre = vista.getTxtName().getText();
            Date fnac = vista.getDateNac().getDate();
            String telefono = vista.getTxtTelf().getText();
            String sex = vista.getTxtSex().getText();
            double sueldo = Double.parseDouble(vista.getTxtSalary().getText());
            int cupo = Integer.parseInt(vista.getTxtCup().getText());
            //byte foto = Byte.valueOf(vista.gettxtFoto().getText());

            ModeloPersona persona = new ModeloPersona();
            persona.setIdpersona(cedula);
            persona.setNombres(nombre);
            persona.setFechanac(fnac);
            persona.setTelefono(telefono);
            persona.setSexo(sex);
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);
            
            if (persona.EditPersonaDB()== null) {
                JOptionPane.showMessageDialog(null, "NO SE HA PODIDO EDITAR A LA PERSONA");
            } else {
                JOptionPane.showMessageDialog(null, "SE HA EDITADO A LA PERSONA CON ÉXITO");
            }
        }
    }
    
    private void eliminarPersona(JTable table){
        ModeloPersona persona = new ModeloPersona();
        if(table.getSelectedRowCount()== 1){
            persona.setIdpersona(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
        }else{
            JOptionPane.showMessageDialog(null, "NECESITA SELECCIONAR UNA FILA PRIMERO");
        }
        
        if(persona.DeletePhisicPerson() == null){
            JOptionPane.showMessageDialog(null, "SE HA ELIMNADO A LA PERSONA CON ÉXITO");
        }else{
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ELIMINAR A LA PERSONA");
        }
    }
    
    private void buscarPersona(){
        ModeloPersona persona = new ModeloPersona();
        persona.setIdpersona(String.valueOf(vista.getTxtBuscar().getText()));
        List<Persona> listap = persona.SearchListPersonas();

        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTablePerson().getModel();
        mTabla.setNumRows(0); //Limpio la tabla

        listap.stream().forEach(pe -> {
            String[] filanueva = {pe.getIdpersona(), pe.getNombres(), String.valueOf(pe.getFechanac()), pe.getTelefono(), pe.getSexo(), String.valueOf(pe.getSueldo()), String.valueOf(pe.getCupo())};
            mTabla.addRow(filanueva);
        });
    }
    
    private boolean uploadDates(JTable table) throws ParseException {
        boolean a = false;
        if (table.getSelectedRowCount() == 1) {
            a = true;
            vista.getTxtCed().setText((String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 0))));
            vista.getTxtName().setText(String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 1)));
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            vista.getDateNac().setDate(formato.parse(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 2).toString()));
            vista.getTxtTelf().setText(String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 3)));
            vista.getTxtSex().setText(String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 4)));
            vista.getTxtSalary().setText(String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 5)));
            vista.getTxtCup().setText(String.valueOf(vista.getTablePerson().getValueAt(vista.getTablePerson().getSelectedRow(), 6)));
        }else{
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA");
        }
        return a;
    }
}
