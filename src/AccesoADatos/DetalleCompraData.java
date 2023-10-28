/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoADatos;

import Entidades.Producto;
import Entidades.Proveedor;
import Entidades.detalleCompra;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Entidades.Compras;
 
 
 
public class DetalleCompraData {
    
    
    
    
      private Connection connection = null;
    private ProductoData ProdData;
    private ComprasData ComData;

    public DetalleCompraData() { 
       connection = Conexion.getConexion();
        ProdData = new ProductoData();
      
    }
   
    public detalleCompra obtenerCantidadYPrecioCostoPorIdProducto(int idProducto) {
        detalleCompra detalleC = null;

        try {
            String sql = "SELECT cantidad, precioCosto FROM datellecompra WHERE idProducto = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, idProducto);

            ResultSet resultSet = st.executeQuery();

            if (resultSet.next()) {
                int cantidad = resultSet.getInt("cantidad");
                double precioCosto = resultSet.getDouble("precioCosto");

                detalleC = new detalleCompra(cantidad, precioCosto);
            }

           
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla");
          
        }

        return detalleC;
    }
}




 

