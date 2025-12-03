package ar.edu.utn.frbb.tup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
public class MenuController {

    @GetMapping("/menu")
    public Map<String, Object> mostrarMenu() {
        return Map.of(
                "titulo", "Menú principal del sistema bancario",
                "opciones", List.of(
                        Map.of(
                                "descripcion", "Crear cliente",
                                "endpoint", "POST /Cliente"
                        ),
                        Map.of(
                                "descripcion", "Obtener todos los clientes",
                                "endpoint", "GET /Cliente"
                        ),
                        Map.of(
                                "descripcion", "Buscar las cuentas del cliente por DNI",
                                "endpoint", "GET /Cliente/{dni}"
                        ),
                        Map.of(
                                "descripcion", "Crear cuenta",
                                "endpoint", "POST /Cuenta"
                        ),
                        Map.of(
                                "descripcion", "Mostrar cuentas de cliente",
                                "endpoint", "GET /Cliente/{dni}/cuentas"
                        ),
                        Map.of(
                                "descripcion", "Solicitar préstamo",
                                "endpoint", "POST /Prestamo"
                        ),
                        Map.of(
                                "descripcion", "Ver préstamos de un cliente",
                                "endpoint", "GET /Prestamo/{dni}"
                        )
                )
        );
    }
}
