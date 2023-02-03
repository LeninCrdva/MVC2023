package Controlador;

import Modelo.ModeloProducto;
import Modelo.Producto;
import Vista.VistaPrincipal;
import Vista.VistaProducto;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenin
 */
public class ControlProducto {

    private ModeloProducto modelo;
    private VistaProducto vista;

    public ControlProducto() {
    }

    public ControlProducto(ModeloProducto modelo, VistaProducto vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);
    }

    public void iniciaControl() {
        cargaProductos();
        vista.getBtnUpdate().addActionListener(l -> cargaProductos());//Agrega un listener para el boton actualizar
        vista.getBtnCreate().addActionListener(l -> abrirDialogo(1));
        vista.getBtnEdit().addActionListener(l -> abrirDialogo(2));
        vista.getButtonAgree().addActionListener(l -> crearEditarProducto());
        vista.getBtnEliminar().addActionListener(l -> eliminarProducto(vista.getTableProduct()));
        vista.getTxtBuscar().addActionListener(l -> buscarProducto());
        vista.getButtonCancel().addActionListener(l -> cancellOperation());
    }

    private void cargaProductos() {
        //Control para consultar a la BD/modelo y luego cargar en la vista
        List<Producto> listap = modelo.ListProductos();

        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTableProduct().getModel();
        mTabla.setNumRows(0); //Limpio la tabla

        listap.stream().forEach(pe -> {
            String[] filanueva = {String.valueOf(pe.getIdproducto()), pe.getNombre(), String.valueOf(pe.getPrecio()), String.valueOf(pe.getCantidad()), pe.getDescripcion()};
            mTabla.addRow(filanueva);
        });
    }

    private void abrirDialogo(int ce) {
        String title;
        boolean openwindow = false;
        if (ce == 1) {
            title = "Crear Producto";
            vista.getDlgCreate().setName("crear");
            openwindow = true;
        } else {
            title = "Editar Producto";
            vista.getDlgCreate().setName("editar");
            try {
                openwindow = uploadDates(vista.getTableProduct());
            } catch (ParseException ex) {
                Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (openwindow) {
            vista.getDlgCreate().setLocationRelativeTo(null);
            vista.getDlgCreate().setSize(530, 400);
            vista.getDlgCreate().setTitle(title);
            vista.getDlgCreate().setVisible(true);

            if (vista.getDlgCreate().getName().equals("crear")) {
                ModeloProducto producto = new ModeloProducto();
                String idproducto = producto.NoSerie();

                int increment_pro;

                if (idproducto == null) {
                    vista.getLblId().setText("000001");
                } else {
                    increment_pro = Integer.parseInt(idproducto);
                    increment_pro++;
                    vista.getLblId().setText("00000" + increment_pro);
                }
            }
        }
    }

    private void crearEditarProducto() {
        ModeloProducto prod = new ModeloProducto();
        String id_pro = prod.NoSerie();
        int increment_pro = 0;

        if (vista.getDlgCreate().getName().equals("crear")) {
            //INSERTAR
            String nombre = vista.getTxtName().getText();
            double precio = Double.parseDouble(vista.getTxtPrecio().getText());
            int cantidad = Integer.parseInt(vista.getTxtCant().getText());
            String descripcion = vista.getTxtDesc().getText();

            if (id_pro == null) {
                increment_pro = 1;
            } else {
                increment_pro = Integer.parseInt(id_pro);
                increment_pro++;
            }

            ModeloProducto producto = new ModeloProducto();
            producto.setIdproducto(increment_pro);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);
            producto.setDescripcion(descripcion);

            if (allowCreateEdit()) {
                if (producto.GrabaProductoDB() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA CREADO A LA PERSONA CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA CREADO A LA PERSONA");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ASEGÚRESE DE LLENAR TODOS LOS CAMPOS");
            }
        } else if (vista.getDlgCreate().getName().equals("editar")) {
            //EDITAR
            vista.getLblId().setText((String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 0))));
            int id = Integer.parseInt(vista.getLblId().getText());
            String nombre = vista.getTxtName().getText();
            double precio = Double.parseDouble(vista.getTxtPrecio().getText());
            int cantidad = Integer.parseInt(vista.getTxtCant().getText());
            String descripcion = vista.getTxtDesc().getText();

            ModeloProducto producto = new ModeloProducto();
            producto.setIdproducto(id);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);
            producto.setDescripcion(descripcion);

            if (allowCreateEdit()) {
                if (producto.EditProductoDB() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA EDITADO AL PRODUCTO CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO EDITAR AL PRODUCTO");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ASEGÚRESE QUE TODOS LOS CAMPOS ESTÉN LLENOS");
            }
        }
        closeAndClean();
        cargaProductos();
    }

    private void eliminarProducto(JTable table) {
        ModeloProducto persona = new ModeloProducto();
        if (table.getSelectedRowCount() == 1) {
            persona.setIdproducto(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
            if (persona.isUsed()) {
                if (persona.DeletePhisicProducto() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA ELIMNADO AL PRODUCTO CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ELIMINAR AL PRODUCTO");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR, NO SE PUEDE ELIMINAR\nPORQUE ESTE PRODUCTO YA HA SIDO RELACIONADO");
            }
        } else {
            JOptionPane.showMessageDialog(null, "NECESITA SELECCIONAR UNA FILA PRIMERO");
        }
        cargaProductos();
    }

    private void buscarProducto() {
        ModeloProducto producto = new ModeloProducto();
        producto.setIdproducto(Integer.parseInt(vista.getTxtBuscar().getText()));
        List<Producto> listap = producto.SearchListProductos();
        
        vista.getTxtBuscar().setText("");

        if (listap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "NO SE HA ENCONTRADO AL PRODUCTO CON DICHO ID");
        } else {
            DefaultTableModel mTabla;
            mTabla = (DefaultTableModel) vista.getTableProduct().getModel();
            mTabla.setNumRows(0); //Limpio la tabla
            
            listap.stream().forEach(pe -> {
                String[] filanueva = {String.valueOf(pe.getIdproducto()), pe.getNombre(), String.valueOf(pe.getPrecio()), String.valueOf(pe.getCantidad()), pe.getDescripcion()};
                mTabla.addRow(filanueva);
            });
        }
    }

    private boolean uploadDates(JTable table) throws ParseException {
        boolean a = false;
        if (table.getSelectedRowCount() == 1) {
            a = true;
            vista.getLblId().setText((String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 0))));
            vista.getTxtName().setText(String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 1)));
            vista.getTxtPrecio().setText(String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 2).toString()));
            vista.getTxtCant().setText(String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 3)));
            vista.getTxtDesc().setText(String.valueOf(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 4)));
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA");
        }
        return a;
    }

    public boolean allowCreateEdit() {
        boolean a = (!vista.getTxtName().getText().isEmpty() && !vista.getTxtPrecio().getText().isEmpty()
                && !vista.getTxtCant().getText().isEmpty() && !vista.getTxtDesc().getText().isEmpty());
        return a;
    }

    private void closeAndClean() {
        vista.getLblId().setText("");
        vista.getTxtName().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtCant().setText("");
        vista.getTxtDesc().setText("");
        vista.getDlgCreate().dispose();
    }

    private void cancellOperation() {
        closeAndClean();
    }
}
