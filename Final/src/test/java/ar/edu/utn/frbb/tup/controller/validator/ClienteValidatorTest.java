package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteValidatorTest {

    private ClienteValidator clienteValidator;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        clienteValidator = new ClienteValidator();
        clienteDto = new ClienteDto();
    }

    // Tipo de persona y fecha válidos
    @Test
    void validateClienteValidoTest() {
        clienteDto.setTipoPersona("F");
        clienteDto.setFechaNacimiento("1990-01-01");

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    // Tipo de persona inválido
    @Test
    void validateTipoPersonaInvalidoTest() {
        clienteDto.setTipoPersona("X");
        clienteDto.setFechaNacimiento("1990-01-01");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clienteValidator.validate(clienteDto));

        assertEquals("El tipo de persona no es correcto", exception.getMessage());
    }

    // Fecha con formato inválido
    @Test
    void validateFechaNacimientoInvalidaTest() {
        clienteDto.setTipoPersona("F");
        clienteDto.setFechaNacimiento("01-01-1990");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clienteValidator.validate(clienteDto));

        assertEquals("Error en el formato de fecha", exception.getMessage());
    }

    // Fecha inválida
    @Test
    void validateFechaIncorrectaTest() {
        clienteDto.setTipoPersona("J");
        clienteDto.setFechaNacimiento("2024-15-99");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clienteValidator.validate(clienteDto));

        assertEquals("Error en el formato de fecha", exception.getMessage());
    }

    // Error por campos inválidos
    @Test
    void validateTipoPersonaYFechaInvalidosTest() {
        clienteDto.setTipoPersona("Z");
        clienteDto.setFechaNacimiento("15/01/2020");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clienteValidator.validate(clienteDto));

        assertEquals("El tipo de persona no es correcto", exception.getMessage());
    }
}
