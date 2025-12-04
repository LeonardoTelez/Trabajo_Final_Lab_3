package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Crear cliente 
    @Test
    public void testCrearClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = getCliente();

        doNothing().when(clienteValidator).validate(clienteDto);
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        Cliente resultado = clienteController.crearCliente(clienteDto);

        assertNotNull(resultado);
        assertEquals(cliente.getDni(), resultado.getDni());

        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    // Crear cliente ya existente
    @Test
    public void testCrearClienteFailClienteExistente() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();

        doNothing().when(clienteValidator).validate(clienteDto);
        doThrow(new ClienteAlreadyExistsException("Ya existe el cliente"))
                .when(clienteService).darDeAltaCliente(clienteDto);

        assertThrows(ClienteAlreadyExistsException.class,
                () -> clienteController.crearCliente(clienteDto));

        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    // Obtener cliente por dni
    @Test
    public void testObtenerClienteSuccess() {
        int dni = 12345678;
        Cliente cliente = getCliente();

        when(clienteService.buscarClientePorDni(dni)).thenReturn(cliente);

        Cliente resultado = clienteController.obtenerClienteCompleto(dni);

        assertNotNull(resultado);
        assertEquals(cliente.getDni(), resultado.getDni());

        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    // Error al obtener cliente por dni (no existe)
    @Test
    public void testObtenerClienteFail() {
        int dni = 12345678;

        doThrow(new IllegalArgumentException("El cliente buscado no existe"))
                .when(clienteService).buscarClientePorDni(dni);

        assertThrows(IllegalArgumentException.class,
                () -> clienteController.obtenerClienteCompleto(dni));

        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    // Obtener todos los clientes
    @Test
    public void testObtenerTodosClientesSuccess() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(getCliente());
        clientes.add(getCliente());

        when(clienteService.obtenerTodosClientes()).thenReturn(clientes);

        List<Cliente> resultado = clienteController.obtenerTodosClientes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(clienteService, times(1)).obtenerTodosClientes();
    }

    // Metodos auxiliares
    private Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        cliente.setTipoPersona(TipoPersona.fromString("F"));
        cliente.setBanco("Banco Prueba");
        return cliente;
    }

    private ClienteDto getClienteDto() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Prueba");
        return clienteDto;
    }
}
