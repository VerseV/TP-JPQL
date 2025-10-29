package org.example;

import funciones.FuncionApp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // --- ESTA CLASE ES PARA CREAR Y GUARDAR DATOS INICIALES EN LA BASE DE DATOS ---
        // --- PARA EJECUTAR LAS CONSULTAS DEL TP, USA LA CLASE MainConsultasJPQL.java ---
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
            EntityManager em = emf.createEntityManager();

            // Inicia la transacción principal para guardar todos los datos
            em.getTransaction().begin();

            // --- Creación de Unidades de Medida ---
            UnidadMedida unidadMedidaKg = UnidadMedida.builder()
                    .denominacion("Kilogramo")
                    .build();
            UnidadMedida unidadMedidaPote = UnidadMedida.builder()
                    .denominacion("Pote")
                    .build();

            em.persist(unidadMedidaKg);
            em.persist(unidadMedidaPote);

            // --- Creación de Categorías ---
            Categoria categoriaFrutas = Categoria.builder()
                    .denominacion("Frutas")
                    .esInsumo(true)
                    .build();
            Categoria categoriaPostre = Categoria.builder()
                    .denominacion("Postre")
                    .esInsumo(false)
                    .build();

            em.persist(categoriaFrutas);
            em.persist(categoriaPostre);

            // --- Creación de Artículos de Insumo ---
            ArticuloInsumo articuloInsumoManzana = ArticuloInsumo.builder()
                    .denominacion("Manzana").codigo("INS-MANZ")
                    .precioCompra(1.5)
                    .precioVenta(5d)
                    .stockActual(100)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedidaKg)
                    .build();

            ArticuloInsumo articuloInsumoPera = ArticuloInsumo.builder()
                    .denominacion("Pera").codigo("INS-PERA")
                    .precioCompra(2.5)
                    .precioVenta(10d)
                    .stockActual(130)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedidaKg)
                    .build();

            em.persist(articuloInsumoManzana);
            em.persist(articuloInsumoPera);

            // --- Asignación de Artículos a Categoría ---
            categoriaFrutas.getArticulos().add(articuloInsumoManzana);
            categoriaFrutas.getArticulos().add(articuloInsumoPera);

            // --- Creación de Artículo Manufacturado (producto final) ---
            ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                    .denominacion("Ensalada de frutas")
                    .descripcion("Ensalada de manzanas y peras")
                    .precioVenta(150d)
                    .tiempoEstimadoMinutos(10)
                    .preparacion("Cortar las frutas en trozos pequeños y mezclar")
                    .unidadMedida(unidadMedidaPote)
                    .codigo("MAN-ENSA")
                    .build();

            // --- Creación de Detalles para el Artículo Manufacturado ---
            ArticuloManufacturadoDetalle detalleManzana = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumoManzana)
                    .build();

            ArticuloManufacturadoDetalle detallePera = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumoPera)
                    .build();

            // --- Asignación de Detalles al Artículo Manufacturado ---
            articuloManufacturado.getDetalles().add(detalleManzana);
            articuloManufacturado.getDetalles().add(detallePera);
            categoriaPostre.getArticulos().add(articuloManufacturado);

            em.persist(articuloManufacturado);

            // --- Creación y guardado de un Cliente ---
            Cliente cliente = Cliente.builder()
                    .cuit(FuncionApp.generateRandomCUIT())
                    .razonSocial("Juan Perez")
                    .build();
            em.persist(cliente);

            // --- Creación y guardado de una Factura ---
            FacturaDetalle detalle1 = new FacturaDetalle(3, articuloInsumoManzana);
            detalle1.calcularSubTotal();
            FacturaDetalle detalle2 = new FacturaDetalle(3, articuloInsumoPera);
            detalle2.calcularSubTotal();
            FacturaDetalle detalle3 = new FacturaDetalle(3, articuloManufacturado);
            detalle3.calcularSubTotal();

            Factura factura = Factura.builder()
                    .puntoVenta(2024)
                    .fechaAlta(new Date())
                    .fechaComprobante(FuncionApp.generateRandomDate())
                    .cliente(cliente)
                    .nroComprobante(FuncionApp.generateRandomNumber())
                    .build();
            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.addDetalleFactura(detalle3);
            factura.calcularTotal();

            em.persist(factura);

            // Confirma todos los cambios en la base de datos
            em.getTransaction().commit();


            // --- EJEMPLO DE CONSULTA PARA VERIFICAR DATOS GUARDADOS ---
            System.out.println("\n--- Verificando el artículo guardado ---");
            String jpql = "SELECT am FROM ArticuloManufacturado am LEFT JOIN FETCH am.detalles d WHERE am.id = :id";
            Query query = em.createQuery(jpql);
            // CORRECCIÓN: Usamos el ID del objeto que acabamos de crear para asegurar que lo encuentre
            query.setParameter("id", articuloManufacturado.getId());
            ArticuloManufacturado articuloManufacturadoCon = (ArticuloManufacturado) query.getSingleResult();

            // CORRECCIÓN: Imprimimos los datos del objeto recuperado de la base de datos (articuloManufacturadoCon)
            System.out.println("Artículo manufacturado: " + articuloManufacturadoCon.getDenominacion());
            System.out.println("Descripción: " + articuloManufacturadoCon.getDescripcion());
            System.out.println("Tiempo estimado: " + articuloManufacturadoCon.getTiempoEstimadoMinutos() + " minutos");
            System.out.println("Preparación: " + articuloManufacturadoCon.getPreparacion());

            System.out.println("Líneas de detalle:");
            for (ArticuloManufacturadoDetalle detalle : articuloManufacturadoCon.getDetalles()) {
                System.out.println("- " + detalle.getCantidad() + " unidades de " + detalle.getArticuloInsumo().getDenominacion());
            }

            // Cerrar el EntityManager y el EntityManagerFactory
            em.close();
            emf.close();

            System.out.println("\n--- Proceso de carga de datos finalizado correctamente. ---");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}