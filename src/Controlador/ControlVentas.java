package Controlador;

import Vista.VistaPuntoDeVenta;

/**
 *
 * @author Lenin
 */
public class ControlVentas {

    private VistaPuntoDeVenta vista;
    
    public ControlVentas(){
    }
    
    public ControlVentas(VistaPuntoDeVenta vista){
        this.vista = vista;
        vista.setVisible(true);
    }
    
    
}
