package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoConsultaDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoOutputDto;
import ar.edu.utn.frbb.tup.model.exception.PrestamoRechazadoException;
import ar.edu.utn.frbb.tup.service.PrestamoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrestamoControllerTest {

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private PrestamoController prestamoController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Solicitar prestamo (exitoso)
    @Test
    void solicitarPrestamoSuccessTest() throws PrestamoRechazadoException {
        PrestamoDto prestamoDto = getPrestamoDto();
        PrestamoOutputDto prestamoOutputDto = mock(PrestamoOutputDto.class);

        when(prestamoService.pedirPrestamo(prestamoDto)).thenReturn(prestamoOutputDto);

        PrestamoOutputDto resultado = prestamoController.solicitarPrestamo(prestamoDto);

        assertNotNull(resultado);
        assertEquals(prestamoOutputDto, resultado);
        verify(prestamoService, times(1)).pedirPrestamo(prestamoDto);
    }

    // Error al solicitar prestamo (dato nulo)
    @Test
    void solicitarPrestamoDtoNuloTest() throws PrestamoRechazadoException {

        assertThrows(IllegalArgumentException.class,
                () -> prestamoController.solicitarPrestamo(null));

        verify(prestamoService, times(0)).pedirPrestamo(any());
    }

    // Error al solicitar prestamo (rechazado)
    @Test
    void solicitarPrestamoRechazadoTest() throws PrestamoRechazadoException {
        PrestamoDto prestamoDto = getPrestamoDto();

        doThrow(new PrestamoRechazadoException("Prestamo rechazado"))
                .when(prestamoService).pedirPrestamo(prestamoDto);

        assertThrows(PrestamoRechazadoException.class,
                () -> prestamoController.solicitarPrestamo(prestamoDto));

        verify(prestamoService, times(1)).pedirPrestamo(prestamoDto);
    }

    // Consultar prestamos de un cliente por dni (exitoso) 
    @Test
    void retornarPrestamosClienteSuccessTest() {
        int dni = 12345678;
        PrestamoConsultaDto prestamoConsultaDto = mock(PrestamoConsultaDto.class);

        when(prestamoService.pedirConsultaPrestamos(dni)).thenReturn(prestamoConsultaDto);

        PrestamoConsultaDto resultado = prestamoController.retonarPrestamosCliente(dni);

        assertNotNull(resultado);
        assertEquals(prestamoConsultaDto, resultado);
        verify(prestamoService, times(1)).pedirConsultaPrestamos(dni);
    }

    // Datos auxiliares

    private PrestamoDto getPrestamoDto() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(12345678);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(50000);
        prestamoDto.setMoneda("PESOS");
        return prestamoDto;
    }
}
