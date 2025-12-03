package ar.edu.utn.frbb.tup.config;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.service.persistence.dao.ClienteDao;
import ar.edu.utn.frbb.tup.service.persistence.dao.CuentaDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public DataLoader(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    @Override
    public void run(String... args) throws Exception {

        // === CLIENTE 1 ===
        Cliente c1 = new Cliente();
        c1.setDni(12345678);
        c1.setNombre("Leonardo");
        c1.setApellido("Telez");
        c1.setBanco("UTN Bank");
        c1.setFechaAlta(java.time.LocalDate.now());
        c1.setTipoPersona(ar.edu.utn.frbb.tup.model.TipoPersona.PERSONA_FISICA);

        clienteDao.save(c1);

        // CUENTA DEL CLIENTE 1
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta1.setMoneda(TipoMoneda.PESOS);
        cuenta1.setTitular(c1.getDni());
        cuentaDao.save(cuenta1);

        // Agregamos la cuenta al cliente (en memoria)
        c1.addCuenta(cuenta1);
        clienteDao.save(c1);


        // === CLIENTE 2 ===
        Cliente c2 = new Cliente();
        c2.setDni(87654321);
        c2.setNombre("Gian");
        c2.setApellido("Luca");
        c2.setBanco("UTN Bank");
        c2.setFechaAlta(java.time.LocalDate.now());
        c2.setTipoPersona(ar.edu.utn.frbb.tup.model.TipoPersona.PERSONA_JURIDICA);

        clienteDao.save(c2);

        // CUENTA DEL CLIENTE 2
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta2.setMoneda(TipoMoneda.DOLARES);
        cuenta2.setTitular(c2.getDni());
        cuentaDao.save(cuenta2);

        c2.addCuenta(cuenta2);
        clienteDao.save(c2);

        System.out.println("=== DataLoader: Datos iniciales cargados ===");
    }
}
