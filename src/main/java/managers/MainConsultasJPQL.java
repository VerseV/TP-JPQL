package managers;

import funciones.FuncionApp;
import org.example.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainConsultasJPQL {

    public static void main(String[] args) {
        //REPOSITORIO-> https://github.com/gerardomagni/jpqlquerys.git

        //buscarFacturas();
        //buscarFacturasActivas();
        //buscarFacturasXNroComprobante();
        //buscarFacturasXRangoFechas();
        //buscarFacturaXPtoVentaXNroComprobante();
        //buscarFacturasXCliente();
        //buscarFacturasXCuitCliente();
        //buscarFacturasXArticulo();
        //mostrarMaximoNroFactura();
        //buscarClientesXIds();
        //buscarClientesXRazonSocialParcial();
        //ejercicio7ArticulosPorFactura(1L);
        //ejercicio8ArticuloMasCaroFactura(1L);
        //ejercicio9ContarTotalFacturas();
        //ejercicio10FacturasMayorA(50000.0);
        //ejercicio11FacturasPorNombreArticulo("Coca Cola 2.25L");
        //ejercicio12ArticulosPorCodigoParcial("A01");
        //ejercicio13ArticulosPrecioMayorAlPromedio();
        //ejercicio14ClientesConFacturasEXISTS();

    }

    // EJERCICIO 1: Listar todos los clientes
    public static void ejercicio1ListarTodosLosClientes(){
        ClienteManager mCliente = new ClienteManager(true);
        try {
            System.out.println("=== EJERCICIO 1: Todos los Clientes ===");
            List<Cliente> clientes = mCliente.getAllClientes();
            for(Cliente cli : clientes){
                System.out.println("ID: " + cli.getId());
                System.out.println("CUIT: " + cli.getCuit());
                System.out.println("Razón Social: " + cli.getRazonSocial());
                System.out.println("-----------------");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mCliente.cerrarEntityManager();
        }
    }

    // EJERCICIO 2: Listar todas las facturas generadas en el último mes
    public static void ejercicio2FacturasUltimoMes(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            System.out.println("=== EJERCICIO 2: Facturas del Último Mes ===");
            List<Factura> facturas = mFactura.getFacturasUltimoMes();
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mFactura.cerrarEntityManager();
        }
    }

    // EJERCICIO 3: Obtener el cliente que ha generado más facturas
    public static void ejercicio3ClienteConMasFacturas(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            System.out.println("=== EJERCICIO 3: Cliente con Más Facturas ===");
            Cliente cliente = mFactura.getClienteConMasFacturas();
            System.out.println("ID: " + cliente.getId());
            System.out.println("CUIT: " + cliente.getCuit());
            System.out.println("Razón Social: " + cliente.getRazonSocial());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mFactura.cerrarEntityManager();
        }
    }

    // EJERCICIO 4: Listar los artículos más vendidos
    public static void ejercicio4ArticulosMasVendidos(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            System.out.println("=== EJERCICIO 4: Artículos Más Vendidos ===");
            List<Object[]> resultados = mFactura.getArticulosMasVendidos();
            for(Object[] resultado : resultados){
                Articulo articulo = (Articulo) resultado[0];
                Double cantidadVendida = (Double) resultado[1];
                System.out.println("Artículo: " + articulo.getDenominacion());
                System.out.println("Código: " + articulo.getCodigo());
                System.out.println("Cantidad Total Vendida: " + cantidadVendida);
                System.out.println("-----------------");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mFactura.cerrarEntityManager();
        }
    }

    // EJERCICIO 5: Obtener las facturas de un cliente emitidas en los últimos 3 meses
    public static void ejercicio5FacturasClienteUltimos3Meses(Long idCliente) {
        FacturaManager mFactura = new FacturaManager(true);
        try {
            System.out.println("=== EJERCICIO 5: Facturas del Cliente en los Últimos 3 Meses ===");
            List<Factura> facturas = mFactura.getFacturasClienteUltimos3Meses(idCliente);

            if (facturas.isEmpty()) {
                System.out.println("El cliente con ID " + idCliente + " no tiene facturas en los últimos 3 meses.");
            } else {
                for (Factura f : facturas) {
                    System.out.println("Factura N° " + f.getNroComprobante() +
                            " | Fecha: " + f.getFechaComprobante() +
                            " | Total: $" + f.getTotal());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mFactura.cerrarEntityManager();
        }
    }

    // EJERCICIO 6: Calcular el total facturado por un cliente
    public static void ejercicio6TotalFacturadoPorCliente(Long idCliente) {
        FacturaManager mFactura = new FacturaManager(true);
        try {
            System.out.println("=== EJERCICIO 6: Total Facturado por Cliente ===");
            Double total = mFactura.getTotalFacturadoPorCliente(idCliente);
            System.out.println("Cliente con ID " + idCliente + " ha facturado un total de: $" + total);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mFactura.cerrarEntityManager();
        }
    }



    public static void buscarFacturas(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturas();
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasActivas(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturasActivas();
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasXNroComprobante(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturasXNroComprobante(796910l);
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasXRangoFechas(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaInicio = FuncionApp.restarSeisMeses(fechaActual);
            List<Factura> facturas = mFactura.buscarFacturasXRangoFechas(fechaInicio, fechaActual);
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturaXPtoVentaXNroComprobante(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            Factura factura = mFactura.getFacturaXPtoVentaXNroComprobante(2024, 796910l);
            mostrarFactura(factura);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasXCliente(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturasXCliente(7l);
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasXCuitCliente(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturasXCuitCliente("27236068981");
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarFacturasXArticulo(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            List<Factura> facturas = mFactura.getFacturasXArticulo(6l);
            mostrarFacturas(facturas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void mostrarMaximoNroFactura(){
        FacturaManager mFactura = new FacturaManager(true);
        try {
            Long nroCompMax = mFactura.getMaxNroComprobanteFactura();
            System.out.println("N° " + nroCompMax);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mFactura.cerrarEntityManager();
        }
    }

    public static void buscarClientesXIds(){
        ClienteManager mCliente = new ClienteManager(true);
        try {
            List<Long> idsClientes = new ArrayList<>();
            idsClientes.add(1l);
            idsClientes.add(2l);
            List<Cliente> clientes = mCliente.getClientesXIds(idsClientes);
            for(Cliente cli : clientes){
                System.out.println("Id: " + cli.getId());
                System.out.println("CUIT: " + cli.getCuit());
                System.out.println("Razon Social: " + cli.getRazonSocial());
                System.out.println("-----------------");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mCliente.cerrarEntityManager();
        }
    }

    public static void buscarClientesXRazonSocialParcial(){
        ClienteManager mCliente = new ClienteManager(true);
        try {
            List<Long> idsClientes = new ArrayList<>();
            idsClientes.add(1l);
            idsClientes.add(2l);
            List<Cliente> clientes = mCliente.getClientesXRazonSocialParcialmente("Lui");
            for(Cliente cli : clientes){
                System.out.println("Id: " + cli.getId());
                System.out.println("CUIT: " + cli.getCuit());
                System.out.println("Razon Social: " + cli.getRazonSocial());
                System.out.println("-----------------");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            mCliente.cerrarEntityManager();
        }
    }



    public static void mostrarFactura(Factura factura){
        List<Factura> facturas = new ArrayList<>();
        facturas.add(factura);
        mostrarFacturas(facturas);
    }

    public static void mostrarFacturas(List<Factura> facturas) {
        for (Factura fact : facturas) {
            System.out.println("N° Comp: " + fact.getStrProVentaNroComprobante());
            System.out.println("Fecha: " + FuncionApp.formatLocalDateToString(fact.getFechaComprobante()));
            System.out.println("CUIT Cliente: " + FuncionApp.formatCuitConGuiones(fact.getCliente().getCuit()));
            System.out.println("Cliente: " + fact.getCliente().getRazonSocial() + " (" + fact.getCliente().getId() + ")");
            System.out.println("------Articulos------");
            for (FacturaDetalle detalle : fact.getDetallesFactura()) {
                System.out.println(detalle.getArticulo().getDenominacion() + ", " + detalle.getCantidad() + " unidades, $" + FuncionApp.getFormatMilDecimal(detalle.getSubTotal(), 2));
            }
            System.out.println("Total: $" + FuncionApp.getFormatMilDecimal(fact.getTotal(), 2));
            System.out.println("*************************");
        }
    }
        // EJERCICIO 7: Listar los Artículos vendidos en una factura
        public static void ejercicio7ArticulosPorFactura(Long idFactura) {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 7: Artículos de la Factura ID " + idFactura + " ===");
                List<org.example.Articulo> articulos = mFactura.getArticulosPorFactura(idFactura);

                if (articulos.isEmpty()) {
                    System.out.println("La factura no tiene artículos o no existe.");
                } else {
                    System.out.println("Artículos encontrados:");
                    for (org.example.Articulo art : articulos) {
                        // Asumo que Articulo tiene getDenominacion y getCodigo
                        System.out.println("- " + art.getDenominacion() + " (Código: " + art.getCodigo() + ")");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 8: Obtener el Artículo más caro vendido en una factura
        public static void ejercicio8ArticuloMasCaroFactura(Long idFactura) {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 8: Artículo más caro de la Factura ID " + idFactura + " ===");
                org.example.Articulo articulo = mFactura.getArticuloMasCaroFactura(idFactura);

                if (articulo != null) {
                    // Asumo que Articulo tiene getDenominacion y getPrecioVenta
                    System.out.println("Artículo: " + articulo.getDenominacion());
                    System.out.println("Precio: $" + articulo.getPrecioVenta());
                } else {
                    System.out.println("No se encontraron artículos para esa factura.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 9: Contar la cantidad total de facturas generadas en el sistema
        public static void ejercicio9ContarTotalFacturas() {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 9: Cantidad Total de Facturas ===");
                Long total = mFactura.countTotalFacturas();
                System.out.println("Total de facturas en el sistema: " + total);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 10: Listar las facturas cuyo total es mayor a un valor determinado
        public static void ejercicio10FacturasMayorA(Double monto) {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 10: Facturas con total mayor a $" + monto + " ===");
                List<Factura> facturas = mFactura.getFacturasMayorA(monto);

                if (facturas.isEmpty()) {
                    System.out.println("No se encontraron facturas con un total mayor a $" + monto);
                } else {
                    mostrarFacturas(facturas); // Reutilizamos el método que ya tienes
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 11: Consultar las facturas que contienen un Artículo específico por nombre
        public static void ejercicio11FacturasPorNombreArticulo(String nombre) {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 11: Facturas que contienen '" + nombre + "' ===");
                List<Factura> facturas = mFactura.getFacturasPorNombreArticulo(nombre);

                if (facturas.isEmpty()) {
                    System.out.println("No se encontraron facturas con ese artículo.");
                } else {
                    mostrarFacturas(facturas);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 12: Listar los Artículos filtrando por código parcial
        public static void ejercicio12ArticulosPorCodigoParcial(String codigo) {
            // Usamos FacturaManager ya que ahí pusimos el método
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 12: Artículos con código que contiene '" + codigo + "' ===");
                List<org.example.Articulo> articulos = mFactura.getArticulosPorCodigoParcial(codigo);

                if (articulos.isEmpty()) {
                    System.out.println("No se encontraron artículos.");
                } else {
                    System.out.println("Artículos encontrados:");
                    for (org.example.Articulo art : articulos) {
                        System.out.println("- " + art.getDenominacion() + " (Código: " + art.getCodigo() + ")");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 13: Listar Artículos cuyo precio sea mayor que el promedio
        public static void ejercicio13ArticulosPrecioMayorAlPromedio() {
            FacturaManager mFactura = new FacturaManager(true);
            try {
                System.out.println("=== EJERCICIO 13: Artículos con precio mayor al promedio ===");

                // Obtenemos el promedio para mostrarlo (opcional)
                Double promedio = mFactura.getPrecioPromedioArticulos();
                System.out.println(" (Promedio de precios: $" + String.format("%.2f", promedio) + ")");

                List<org.example.Articulo> articulos = mFactura.getArticulosPrecioMayorAlPromedio();

                if (articulos.isEmpty()) {
                    System.out.println("No se encontraron artículos por encima del promedio.");
                } else {
                    System.out.println("Artículos encontrados:");
                    for (org.example.Articulo art : articulos) {
                        System.out.println("- " + art.getDenominacion() + " (Precio: $" + art.getPrecioVenta() + ")");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mFactura.cerrarEntityManager();
            }
        }

        // EJERCICIO 14: Ejemplo de EXISTS - Clientes con al menos una factura
        public static void ejercicio14ClientesConFacturasEXISTS() {
            // Este ejercicio usa ClienteManager
            ClienteManager mCliente = new ClienteManager(true);
            try {
                System.out.println("=== EJERCICIO 14: Clientes con al menos una factura (usando EXISTS) ===");
                List<org.example.Cliente> clientes = mCliente.getClientesConFacturasUsandoExists();

                if (clientes.isEmpty()) {
                    System.out.println("No se encontraron clientes con facturas.");
                } else {
                    System.out.println("Clientes encontrados:");
                    for (org.example.Cliente cli : clientes) {
                        System.out.println("- ID: " + cli.getId() + ", Razón Social: " + cli.getRazonSocial());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mCliente.cerrarEntityManager();
            }
        }

    }

}
