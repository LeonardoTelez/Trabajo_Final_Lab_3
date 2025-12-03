package ar.edu.utn.frbb.tup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
public class MenuController {

    @GetMapping("/api/menu")
    public Map<String, Object> mostrarMenu() {
        return Map.of(
                "titulo", "Menú principal del sistema bancario",
                "opciones", List.of(
                        Map.of(
                                "opcion", 1,
                                "descripcion", "Obtener todos los clientes",
                                "endpoint", "GET /api/clientes"
                        ),
                        Map.of(
                                "opcion", 2,
                                "descripcion", "Crear cliente",
                                "help", "El cliente debe ser mayor de edad (18 años mínimo)",
                                "endpoint", "POST /api/clientes"
                                
                        ),
                        Map.of(
                                "opcion", 3,
                                "descripcion", "Buscar las cuentas del cliente por DNI",
                                "endpoint", "GET /api/clientes/{dni}"
                        ),
                        Map.of(
                                "opcion", 4,
                                "descripcion", "Crear cuenta",
                                "help", "Solo se permiten cuentas en PESOS o DOLARES",
                                "endpoint", "POST /api/cuentas"
                                
                        ),
                        Map.of(
                                "opcion", 5,
                                "descripcion", "Solicitar préstamo",
                                "help", "La cuenta destino del préstamo debe existir y pertenecer al cliente solicitante",
                                "endpoint", "POST /api/prestamos"
                        ),
                        Map.of(
                                "opcion", 6,
                                "descripcion", "Ver préstamos de un cliente",
                                "endpoint", "GET /api/prestamos/{dni}"
                        )
                )
        );
    }
}
