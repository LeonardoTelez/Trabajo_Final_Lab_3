package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaValidatorTest {

    // Cuenta valida (PESOS + CAJA_AHORRO)
    @Test
    void validateCuentaValidaTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");

        assertDoesNotThrow(() -> CuentaValidator.validate(cuentaDto));
    }

    // Cuenta valida (DOLARES + CAJA_AHORRO)
    @Test
    void validateCuentaDolaresCajaAhorroValidaTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("DOLARES");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");

        assertDoesNotThrow(() -> CuentaValidator.validate(cuentaDto));
    }

    // Cuenta valida (PESOS + CUENTA_CORRIENTE)
    @Test
    void validateCuentaPesosCuentaCorrienteValidaTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");

        assertDoesNotThrow(() -> CuentaValidator.validate(cuentaDto));
    }

    // Moneda inválida
    @Test
    void validateTipoMonedaInvalidaTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("EUROS");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CuentaValidator.validate(cuentaDto));

        assertEquals("La moneda: EUROS no es soportada", exception.getMessage());
    }

    // Tipo de cuenta inválido
    @Test
    void validateTipoCuentaInvalidaTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("PLAZO_FIJO");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CuentaValidator.validate(cuentaDto));

        assertEquals("La cuenta del tipo PLAZO_FIJO no es correcta", exception.getMessage());
    }

    // Cuenta invalida (falla primero por la moneda)
    @Test
    void validateTipoMonedaYTipoCuentaInvalidosTest() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("BITCOIN");
        cuentaDto.setTipoCuenta("INVERSION");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CuentaValidator.validate(cuentaDto));

        assertEquals("La moneda: BITCOIN no es soportada", exception.getMessage());
    }
}
