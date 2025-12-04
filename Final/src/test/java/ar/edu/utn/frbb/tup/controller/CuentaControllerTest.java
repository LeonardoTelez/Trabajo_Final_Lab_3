package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.CuentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Creación de cuenta exitosa
    @Test
    void crearCuentaSuccessTest()
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {

        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = getCuenta();

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuenta);

        Cuenta resultado = cuentaController.crearCuenta(cuentaDto);

        assertEquals(cuenta, resultado);
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    // Error al crear cuenta (cuenta ya existente)
    @Test
    void crearCuentaFailCuentaExistenteTest()
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {

        CuentaDto cuentaDto = getCuentaDto();

        doThrow(new CuentaAlreadyExistsException("La cuenta ya existe"))
                .when(cuentaService).darDeAltaCuenta(cuentaDto);

        assertThrows(CuentaAlreadyExistsException.class,
                () -> cuentaController.crearCuenta(cuentaDto));

        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    // Error al crear cuenta (tipo de cuenta no soportado)
    @Test
    void crearCuentaFailTipoNoSoportadoTest()
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {

        CuentaDto cuentaDto = getCuentaDto();

        doThrow(new TipoCuentaNoSoportadaException("Tipo no soportado"))
                .when(cuentaService).darDeAltaCuenta(cuentaDto);

        assertThrows(TipoCuentaNoSoportadaException.class,
                () -> cuentaController.crearCuenta(cuentaDto));

        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    // Métodos auxiliares

    private CuentaDto getCuentaDto() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTitularDni(12345678);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        return cuentaDto;
    }

    private Cuenta getCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(12345678);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(0.0);
        return cuenta;
    }
}
