/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoADatos;

import Entidades.Compras;
import Entidades.Producto;
import Entidades.Proveedor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ComprasData {
    private Connection con=null;
    private ProveedorData provData;
    private DetalleCompraData detdData;

    public ComprasData() {
        
        con= Conexion.getConexion();
        provData=new ProveedorData();
        detdData=new DetalleCompraData();
        
    }
    public void GuardarCompra(Compras compras){

  Connection connection = Conexion.getConexion();

        if (connection == null) {
            // Manejar la falta de conexión a la base de datos
            return;
        }

        try {
            String sql = "INSERT INTO compras (idCompras, idProveedor, fecha, estadoCompra)"
                    + " VALUES (?, ?, ?, ?)";

            // Crear una sentencia preparada para ejecutar la consulta
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, compras.getIdCompras());
            ps.setInt(2, compras.getProveedor().getIdProveedor());
            ps.setDate(3, compras.getFecha());
            ps.setBoolean(4, compras.isEstadoCompra());

            // Ejecutar la consulta para insertar la compra
            int exito = ps.executeUpdate();
if (exito > 0) {
                JOptionPane.showMessageDialog(null, "Compra agregada con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar la compra.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla compras. " + ex.getMessage());
        }
    }  
    
   public List<Compras> obtenerComprasPorFecha(Date fecha) {
    List<Compras> comprasPorFecha = new ArrayList<>();
    Connection con = Conexion.getConexion();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        String sql = "SELECT c.idCompras, c.fecha, pdt.nombreProducto, prv.razonSocial " +
                     "FROM compras c " +
                     "JOIN datelleCompra dc ON c.idCompras = dc.idCompras " +
                     "JOIN producto pdt ON dc.idProducto = pdt.idProducto " +
                     "JOIN proveedor prv ON c.idProveedor = prv.idProveedor " +
                     "WHERE c.fecha = ?";
        ps = con.prepareStatement(sql);
        ps.setDate(1, new java.sql.Date(fecha.getTime())); 

        rs = ps.executeQuery();

        while (rs.next()) {
            int compraId = rs.getInt("idCompras");
            Date compraFecha = rs.getDate("fecha");
            String nombreProducto = rs.getString("nombreProducto");
            String razonSocialProveedor = rs.getString("razonSocial");
            Producto producto = new Producto(-1, nombreProducto, null, -1, -1, true);

            Proveedor proveedor = new Proveedor();
            proveedor.setRazonSocial(razonSocialProveedor);

            Compras compra = new Compras(compraId, compraFecha, producto, proveedor);
            comprasPorFecha.add(compra);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al obtener compras por fecha: " + ex.getMessage());
    } 
    return comprasPorFecha;
}

 

    public Compras ObtenerCompraPorProveedor( String RazonSocial){
    Compras compraAProveedor= new Compras() ;
    Connection connection = Conexion.getConexion();
        
    try{
     String sql= "SELECT MAX(idCompras),fecha FROM compras JOIN proveedor ON \"+\n" +
            "compras.idProveedor=proveedor.idProveedor WHERE proveedor.razonSocial= ?";
       
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1, RazonSocial);
            ResultSet rs =ps.executeQuery();
            Proveedor proveedor=new Proveedor();
                  
            while (rs.next()) { 
               
                int idComp=rs.getInt("idCompras");
                Date fec=rs.getDate("fecha");
                  
             compraAProveedor  =new Compras(idComp, fec);
                
         //    compraAProveedor.add();
                       
            }
          
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar Lista de compras a proveedores") ;
        }
   
    return compraAProveedor;
    } 
    
   public int obtenerIdCompraPorIdProducto(int idProducto) {
    int idCompra = -1; 

    try {
        
        String sql = "SELECT dc.idCompras FROM datellecompra dc WHERE dc.idProducto = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, idProducto);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()) {
            idCompra = resultSet.getInt("idCompras");
        }

       
    } catch (SQLException e) {
       
       JOptionPane.showMessageDialog(null, "Error al acceder a la tabla");
    }

    return idCompra;
}

public Compras obtenerUltimaCompraPorProveedor(int idProveedor) {
    Compras ultimaCompra = null;
    Connection connection = Conexion.getConexion();

    if (connection == null) {
        return null;
    }

    try {
        String sql = "SELECT c.idCompras, c.fecha " +
                     "FROM compras c " +
                     "WHERE c.idProveedor = ? AND c.idCompras = (SELECT MAX(idCompras) FROM compras WHERE idProveedor = ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idProveedor);
        ps.setInt(2, idProveedor);

        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            int compraId = rs.getInt("idCompras");
            Date compraFecha = rs.getDate("fecha");
            

           
            ultimaCompra = new Compras(compraId, compraFecha);
           
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al obtener la última compra por proveedor: " + ex.getMessage());
    }

    return ultimaCompra;
}
     
     
    
    
    
    }
    










    
 
            

