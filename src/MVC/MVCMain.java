package MVC;

import Controlador.ControladorPrincipal;
import Vista.VistaPrincipal;
/**
 *
 * @author Lenin
 */
public class MVCMain {
    public static void main(String[] args) {
        VistaPrincipal vista = new VistaPrincipal();
        ControladorPrincipal control = new ControladorPrincipal(vista);
        control.iniciaControl();
    }
}
