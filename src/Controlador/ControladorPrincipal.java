package Controlador;

import Modelo.ModeloPersona;
import Modelo.ModeloProducto;
import Vista.VistaPersonas;
import Vista.VistaPrincipal;
import Vista.VistaProducto;
import Vista.VistaPuntoDeVenta;

/**
 *
 * @author Lenin
 */
public class ControladorPrincipal {

    VistaPrincipal vistaPrincipal;
    
    public ControladorPrincipal(VistaPrincipal vista){
        this.vistaPrincipal = vista;
        vista.setVisible(true);
    }
    
    public void iniciaControl(){
        vistaPrincipal.getMnuCrudPersonas().addActionListener(l->crudPersonas());
        vistaPrincipal.getBtnPersonas().addActionListener(l->crudPersonas());
        vistaPrincipal.getBtnVentas().addActionListener(l->crudVentas());
        vistaPrincipal.getBtnProducto().addActionListener(l -> crudProductos());
    }
    
    private void crudPersonas(){
        ModeloPersona modelo = new ModeloPersona();
        VistaPersonas vista = new VistaPersonas();
        
        vistaPrincipal.getDptPrincipal().add(vista);
        
        ControlPersona control = new ControlPersona(modelo, vista);
        control.iniciaControl();
    }
    
    private void crudVentas(){
        VistaPuntoDeVenta vista = new VistaPuntoDeVenta();
        ControlVentas control = new ControlVentas(vista);
        vistaPrincipal.getDptPrincipal().add(vista);
    }
    
    private void crudProductos(){
        ModeloProducto producto = new ModeloProducto();
        VistaProducto vista = new VistaProducto();
        
        vistaPrincipal.getDptPrincipal().add(vista);
        
        ControlProducto control = new ControlProducto(producto, vista);
        control.iniciaControl();
    }
}
