package managers;

import org.example.Factura;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaManager {
    EntityManagerFactory emf = null;
    EntityManager em = null;

    public FacturaManager(boolean anularShowSQL) {
        Map<String, Object> properties = new HashMap<>();
        if(anularShowSQL){
            // Desactivar el show_sql (si está activado en el persistence.xml o configuración por defecto)
            properties.put("hibernate.show_sql", "false");
        }else{
            properties.put("hibernate.show_sql", "true");
        }
        emf = Persistence.createEntityManagerFactory("example-unit", properties);
        em = emf.createEntityManager();

    }

    // EJERCICIO 2: Listar todas las facturas generadas en el último mes
    public List<Factura> getFacturasUltimoMes(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaHaceUnMes = fechaActual.minusMonths(1);

        String jpql = "SELECT f FROM Factura f " +
                "WHERE f.fechaComprobante >= :fechaInicio " +
                "ORDER BY f.fechaComprobante DESC";
        Query query = em.createQuery(jpql);
        query.setParameter("fechaInicio", fechaHaceUnMes);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturas(){
        String jpql = "FROM Factura";
        Query query = em.createQuery(jpql);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasActivas(){
        //si quiero buscar distintos de NULL uso -> IS NOT NULL
        String jpql = "FROM Factura WHERE fechaBaja IS NULL ORDER BY fechaComprobante DESC";
        Query query = em.createQuery(jpql);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXNroComprobante(Long nroComprobante){
        String jpql = "FROM Factura WHERE nroComprobante = :nroComprobante";
        Query query = em.createQuery(jpql);
        query.setParameter("nroComprobante", nroComprobante);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> buscarFacturasXRangoFechas(LocalDate fechaInicio, LocalDate fechaFin){
        String jpql = "FROM Factura WHERE fechaComprobante >= :fechaInicio AND fechaComprobante <= :fechaFin";
        Query query = em.createQuery(jpql);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public Factura getFacturaXPtoVentaXNroComprobante(Integer puntoVenta, Long nroComprobante){
        String jpql = "FROM Factura WHERE puntoVenta = :puntoVenta AND nroComprobante = :nroComprobante";
        Query query = em.createQuery(jpql);
        query.setMaxResults(1);
        query.setParameter("puntoVenta", puntoVenta);
        query.setParameter("nroComprobante", nroComprobante);

        Factura factura = (Factura) query.getSingleResult();
        return factura;
    }

    public List<Factura> getFacturasXCliente(Long idCliente){
        String jpql = "FROM Factura WHERE cliente.id = :idCliente";
        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXCuitCliente(String cuitCliente){
        String jpql = "FROM Factura WHERE cliente.cuit = :cuitCliente";
        Query query = em.createQuery(jpql);
        query.setParameter("cuitCliente", cuitCliente);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXArticulo(Long idArticulo){ //INNER JOIN, LEFT JOIN, LEFT OUTER JOIN, etc
        StringBuilder jpql = new StringBuilder("SELECT fact FROM Factura AS fact LEFT OUTER JOIN fact.detallesFactura AS detalle");
        jpql.append(" WHERE detalle.id = :idArticulo");
        Query query = em.createQuery(jpql.toString());
        query.setParameter("idArticulo", idArticulo);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public Long getMaxNroComprobanteFactura(){ //MAX, MIN, COUNT, AVG, SUM
        StringBuilder jpql = new StringBuilder("SELECT MAX(nroComprobante) FROM Factura WHERE fechaBaja IS NULL");
        Query query = em.createQuery(jpql.toString());

        Long maxNroFactura = (Long) query.getSingleResult();
        return maxNroFactura;
    }

    // EJERCICIO 3: Obtener el cliente que ha generado más facturas
    public org.example.Cliente getClienteConMasFacturas(){
        Query query = em.createQuery("SELECT f.cliente FROM Factura f WHERE f.fechaBaja IS NULL " +
                "GROUP BY f.cliente " +
                "ORDER BY COUNT(f) DESC");
        query.setMaxResults(1);

        org.example.Cliente cliente = (org.example.Cliente) query.getSingleResult();
        return cliente;
    }

    // EJERCICIO 4: Listar los artículos más vendidos
    public List<Object[]> getArticulosMasVendidos(){
        Query query = em.createQuery("SELECT fd.articulo, SUM(fd.cantidad) as cantidadVendida FROM FacturaDetalle fd " +
                "GROUP BY fd.articulo " +
                "ORDER BY cantidadVendida DESC");

        List<Object[]> resultados = query.getResultList();
        return resultados;
    }

    //EJERCICIO 5: Obtener las facturas de un cliente emitidas en los ultimos 3 meses
    public List<Factura> getFacturasClienteUltimos3Meses(Long idCliente){
        LocalDate fecha3Meses= LocalDate.now().minusMonths(3);
        String jpql = "SELECT f FROM factura WHERE f.cliente.id = :idCliente AND f.fechaComprobante >= :fecha3Meses";

        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);
        query.setParameter("fecha3Meses", fecha3Meses);

        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    //EJERCICIO 6: Calcular el total facturado por un cliente
    public Double getTotalFacturadoPorCliente(Long idCliente){
        String jpql = "SELECT SUM(f.total) FROM Factura f WHERE f.cliente.id = :idCliente AND f.fechaBaja IS NULL";

        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);

        Double total= (Double) query.getSingleResult();
        return (total != null) ? total : 0.0;
    }

    public void cerrarEntityManager(){
        em.close();
        emf.close();

        // EJERCICIO 7: Listar los Artículos vendidos en una factura
        public List<org.example.Articulo> getArticulosPorFactura(Long facturaId) {
            String jpql = "SELECT fd.articulo FROM FacturaDetalle fd WHERE fd.factura.id = :facturaId";
            Query query = em.createQuery(jpql);
            query.setParameter("facturaId", facturaId);
            return query.getResultList();
        }

        // EJERCICIO 8: Obtener el Artículo más caro vendido en una factura
        public org.example.Articulo getArticuloMasCaroFactura(Long facturaId) {
            String jpql = "SELECT fd.articulo FROM FacturaDetalle fd " +
                    "WHERE fd.factura.id = :facturaId " +
                    "ORDER BY fd.articulo.precioVenta DESC"; // Ordenamos por precio descendente

            Query query = em.createQuery(jpql);
            query.setParameter("facturaId", facturaId);
            query.setMaxResults(1); // Tomamos solo el primer resultado

            try {
                // Usamos getSingleResult, pero puede lanzar NoResultException
                return (org.example.Articulo) query.getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                return null; // Retorna null si la factura no tiene artículos
            }
        }

        // EJERCICIO 9: Contar la cantidad total de facturas generadas en el sistema
        public Long countTotalFacturas() {
            String jpql = "SELECT COUNT(f) FROM Factura f";
            Query query = em.createQuery(jpql);
            return (Long) query.getSingleResult();
        }

        // EJERCICIO 10: Listar las facturas cuyo total es mayor a un valor determinado
        public List<Factura> getFacturasMayorA(Double montoMinimo) {
            String jpql = "SELECT f FROM Factura f WHERE f.total > :montoMinimo";
            Query query = em.createQuery(jpql);
            query.setParameter("montoMinimo", montoMinimo);
            return query.getResultList();
        }

        // EJERCICIO 11: Consultar las facturas que contienen un Artículo específico por nombre
        public List<Factura> getFacturasPorNombreArticulo(String nombreArticulo) {
            // Usamos DISTINCT para no repetir facturas si el artículo aparece varias veces
            String jpql = "SELECT DISTINCT f FROM Factura f " +
                    "JOIN f.detallesFactura fd " +
                    "JOIN fd.articulo a " +
                    "WHERE a.denominacion = :nombreArticulo";

            Query query = em.createQuery(jpql);
            query.setParameter("nombreArticulo", nombreArticulo);
            return query.getResultList();
        }

        // EJERCICIO 12: Listar los Artículos filtrando por código parcial
        public List<org.example.Articulo> getArticulosPorCodigoParcial(String codigoParcial) {
            String jpql = "SELECT a FROM Articulo a WHERE a.codigo LIKE :codigoParcial";
            Query query = em.createQuery(jpql);

            // Añadimos los '%' para que LIKE busque "contiene"
            query.setParameter("codigoParcial", "%" + codigoParcial + "%");
            return query.getResultList();
        }

        // EJERCICIO 13: Listar Artículos cuyo precio sea mayor que el promedio
        public List<org.example.Articulo> getArticulosPrecioMayorAlPromedio() {
            // La subconsulta (SELECT AVG...) se ejecuta primero
            String jpql = "SELECT a FROM Articulo a WHERE a.precioVenta > " +
                    "(SELECT AVG(a2.precioVenta) FROM Articulo a2)";
            Query query = em.createQuery(jpql);
            return query.getResultList();
        }

        // Método auxiliar para mostrar el promedio en la consola (opcional pero útil)
        public Double getPrecioPromedioArticulos() {
            String jpql = "SELECT AVG(a.precioVenta) FROM Articulo a";
            Query query = em.createQuery(jpql);
            return (Double) query.getSingleResult();
        }



    }
}
