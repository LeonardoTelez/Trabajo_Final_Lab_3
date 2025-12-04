package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.persistence.dao.ClienteDao;
import ar.edu.utn.frbb.tup.service.persistence.dao.CuentaDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private ClienteService clienteService;

    // Crear un cliente nuevo 

    @Test
    void crearCliente_ok() throws ClienteAlreadyExistsException {

        ClienteDto dto = new ClienteDto();
        dto.setDni(12345678);
        dto.setNombre("Leonardo");
        dto.setApellido("Telez");
        dto.setTipoPersona("F");
        dto.setBanco("UTN Bank");
        dto.setFechaNacimiento("1995-05-10");


        when(clienteDao.find(12345678, false)).thenReturn(null);

        Cliente clienteCreado = clienteService.darDeAltaCliente(dto);

        assertNotNull(clienteCreado);
        assertEquals(12345678, clienteCreado.getDni());

        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    @Test
    void crearCliente_dniDuplicado() {

        ClienteDto dto = new ClienteDto();
        dto.setDni(12345678);
        dto.setNombre("Leonardo");
        dto.setApellido("Telez");
        dto.setTipoPersona("F");
        dto.setBanco("UTN Bank");

        when(clienteDao.find(12345678, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () ->
                clienteService.darDeAltaCliente(dto)
        );

        verify(clienteDao, never()).save(any());
    }

    @Test
    void crearCliente_menorDeEdad() {

        ClienteDto dto = new ClienteDto();
        dto.setDni(99999999);
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setTipoPersona("F");
        dto.setBanco("UTN Bank");

        when(clienteDao.find(99999999, false)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                clienteService.darDeAltaCliente(dto)
        );

        verify(clienteDao, never()).save(any());
    }

    // Agregar cuenta a cliente

    @Test
    void agregarCuenta_ok() throws TipoCuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaNacimiento(LocalDate.of(1999, 5, 10));

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, 12345678);

        assertEquals(1, cliente.getCuentas().size());
        assertEquals(12345678, cuenta.getTitular());
    }

    @Test
    void agregarCuenta_duplicada_mismoTipoYMoneda() throws TipoCuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);

        Cuenta cuenta1 = new Cuenta();
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta1.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678, true)).thenReturn(cliente);
        clienteService.agregarCuenta(cuenta1, 12345678);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta2.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678, true)).thenReturn(cliente);

        assertThrows(TipoCuentaAlreadyExistsException.class, () ->
                clienteService.agregarCuenta(cuenta2, 12345678)
        );

        assertEquals(1, cliente.getCuentas().size());
    }

    @Test
    void agregarDosCuentas_distintoTipo_ok() throws TipoCuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);

        Cuenta cuenta1 = new Cuenta();
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta1.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta2.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678, true)).thenReturn(cliente);
        clienteService.agregarCuenta(cuenta1, 12345678);

        when(clienteDao.find(12345678, true)).thenReturn(cliente);
        clienteService.agregarCuenta(cuenta2, 12345678);

        assertEquals(2, cliente.getCuentas().size());
    }

    // Buscar clientes por DNI

    @Test
    void buscarClientePorDni_ok() {

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Leonardo");

        when(clienteDao.find(12345678, true)).thenReturn(cliente);

        Cliente result = clienteService.buscarClientePorDni(12345678);

        assertEquals("Leonardo", result.getNombre());
    }

    @Test
    void buscarClientePorDni_noExiste() {

        when(clienteDao.find(12345678, true)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                clienteService.buscarClientePorDni(12345678)
        );
    }

    // Obtener todos los clientes

    @Test
    void obtenerTodosClientes_ok() {

        Cliente c1 = new Cliente();
        Cliente c2 = new Cliente();

        when(clienteDao.findAll()).thenReturn(List.of(c1, c2));

        List<Cliente> clientes = clienteService.obtenerTodosClientes();

        assertEquals(2, clientes.size());
    }

    @Test
    void obtenerTodosClientes_vacio() {

        when(clienteDao.findAll()).thenReturn(new ArrayList<>());

        List<Cliente> clientes = clienteService.obtenerTodosClientes();

        assertNull(clientes);
    }
}
