package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoConsultaDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoOutputDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.PrestamoRechazadoException;
import ar.edu.utn.frbb.tup.service.persistence.dao.PrestamoDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrestamoServiceTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private PrestamoDao prestamoDao;

    @InjectMocks
    private PrestamoService prestamoService;

    private PrestamoDto prestamoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123);
        prestamoDto.setMontoPrestamo(100000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("PESOS");
    }

    // Cálculo de intereses

    @Test
    void calculaInteresesTest() {
        double esperado = 12000 * ((double) 5 / 12);
        double resultado = prestamoService.calculaIntereses(12000, 5);

        assertEquals(esperado, resultado);
    }

    // Aprobacion de préstamo correctamente

    @Test
    void pedirPrestamoAprobadoTest() throws PrestamoRechazadoException {
        // Mockeamos servicios necesarios
        CuentaService cuentaServiceMock = Mockito.mock(CuentaService.class);
        ClienteService clienteServiceMock = Mockito.mock(ClienteService.class);
        prestamoService.cuentaService = cuentaServiceMock;
        prestamoService.clienteService = clienteServiceMock;

        // Creamos una cuenta de prueba
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        Mockito.when(clienteServiceMock.getCuentasCliente(Mockito.anyInt()))
            .thenReturn(cuentas);

        // Mock del método estático verificaScore (usar anyLong si el parámetro es long)
        try (MockedStatic<PrestamoService> prestamoServiceStaticMock = Mockito.mockStatic(PrestamoService.class)) {
            prestamoServiceStaticMock.when(() -> PrestamoService.verificaScore(Mockito.anyLong()))
                                    .thenReturn("OK");

            // PrestamoDto de prueba
            PrestamoDto prestamoDto = new PrestamoDto();
            prestamoDto.setNumeroCliente(12345678);
            prestamoDto.setPlazoMeses(12);
            prestamoDto.setMontoPrestamo(5000.0);
            prestamoDto.setMoneda("PESOS");

            PrestamoOutputDto resultado = prestamoService.pedirPrestamo(prestamoDto);

            assertEquals("APROBADO", resultado.getEstado());
            assertEquals("El monto del prestamo fue acreditado a su cuenta", resultado.getMensaje());
        }
    }

    // Rechazado por no tener cuenta permitida

    @Test
    void pedirPrestamoCuentaNoPermitidaTest() {

        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(123);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        when(clienteService.getCuentasCliente(123)).thenReturn(cuentas);

        assertThrows(PrestamoRechazadoException.class, () -> {
            prestamoService.pedirPrestamo(prestamoDto);
        });
    }

    // Rechazado por monto excesivo
    @Test
    void pedirPrestamoMontoExcesivoTest() {
        prestamoDto.setMontoPrestamo(6000000);

        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(123);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        when(clienteService.getCuentasCliente(123)).thenReturn(cuentas);

        assertThrows(PrestamoRechazadoException.class, () -> {
            prestamoService.pedirPrestamo(prestamoDto);
        });
    }

    // Rechazado por deber más de 3 préstamos
    @Test
    void pedirPrestamoDeudorTest() {

        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(123);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        List<Prestamo> prestamos = new ArrayList<>();
        prestamos.add(new Prestamo());
        prestamos.add(new Prestamo());
        prestamos.add(new Prestamo());
        prestamos.add(new Prestamo());

        when(clienteService.getCuentasCliente(123)).thenReturn(cuentas);
        when(prestamoDao.getPrestamosByCliente(123)).thenReturn(prestamos);

        assertThrows(PrestamoRechazadoException.class, () -> {
            prestamoService.pedirPrestamo(prestamoDto);
        });
    }

    // Error al consulta préstamos
    @Test
    void pedirConsultaPrestamosFalloTest() {

        when(prestamoDao.getPrestamosByCliente(anyInt())).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.pedirConsultaPrestamos(123);
        });
    }

    // Exito al consultar préstamos
    @Test
    void pedirConsultaPrestamosExitoTest() {

        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(123);
        prestamo.setMontoPrestamo(1000.0);
        prestamo.setInteresTotal(500.0);
        prestamo.setPlazoMeses(12);

        List<Prestamo> prestamos = new ArrayList<>();
        prestamos.add(prestamo);

        when(prestamoDao.getPrestamosByCliente(123)).thenReturn(prestamos);

        PrestamoConsultaDto resultado = prestamoService.pedirConsultaPrestamos(123);

        assertEquals(123, resultado.getNumeroCliente());
        assertEquals(1, resultado.getPrestamos().size());
        assertEquals(1000.0, resultado.getPrestamos().get(0).getMonto());
        assertEquals(500.0, resultado.getPrestamos().get(0).getIntereses());
        assertEquals(12, resultado.getPrestamos().get(0).getPlazoMeses());
    }

}
