package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.Prestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuotaServiceTest {

    private Prestamo prestamo;

    @BeforeEach
    void setUp() {
        prestamo = new Prestamo();
        prestamo.setMontoPrestamo(12000.0);
        prestamo.setInteresTotal(6000.0);
        prestamo.setPlazoMeses(12);
    }

    // Verificar que se generen cuotas correctamente
    @Test
    void generarCuotasTest() {
        assertDoesNotThrow(() -> CuotaService.generarCuotas(prestamo));

        List<Cuota> cuotas = prestamo.getPlanPagos();

        assertFalse(cuotas.isEmpty(), "El plan de pagos no debería estar vacío");
        assertTrue(cuotas.size() >= 1, "Debe generarse al menos una cuota");
        assertTrue(cuotas.size() <= prestamo.getPlazoMeses(),
                "No se pueden generar más cuotas que el plazo del préstamo");
    }

    // Verificar que los números de cuota se generen correctamente
    @Test
    void generarCuotasNumeracionCorrectaTest() {
        CuotaService.generarCuotas(prestamo);
        List<Cuota> cuotas = prestamo.getPlanPagos();

        for (int i = 0; i < cuotas.size(); i++) {
            assertEquals(i + 1, cuotas.get(i).getNroCuota(),
                    "La numeración de las cuotas debe ser consecutiva");
        }
    }

    // Verificar que el número random esté dentro del rango correcto
    @Test
    void generarRandomCantCuotasTest() {
        int random = assertDoesNotThrow(() ->
                CuotaService.generarRandomCantCuotas(prestamo.getPlazoMeses())
        );

        assertTrue(random >= 1, "El número random debe ser mayor o igual a 1");
        assertTrue(random <= prestamo.getPlazoMeses(),
                "El número random no puede superar el plazo del préstamo");
    }

    // Verificar que el cálculo del monto de cuota sea correcto
    @Test
    void calcularMontoCuotaTest() {
        double esperado = (prestamo.getMontoPrestamo() + prestamo.getInteresTotal())
                / prestamo.getPlazoMeses();

        double resultado = assertDoesNotThrow(() ->
                CuotaService.calcularMontoCuota(prestamo)
        );

        assertEquals(esperado, resultado);
    }

    // Verificar que todas las cuotas tengan el mismo monto correcto
    @Test
    void todasLasCuotasTienenMontoCorrecto() {
        double montoEsperado = CuotaService.calcularMontoCuota(prestamo);

        CuotaService.generarCuotas(prestamo);
        List<Cuota> cuotas = prestamo.getPlanPagos();

        for (Cuota cuota : cuotas) {
            assertEquals(montoEsperado, cuota.getMonto());
        }
    }
}
