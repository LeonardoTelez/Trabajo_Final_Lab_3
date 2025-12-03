# TRABAJO FINAL LABORATORIO III - TELEZ LEONARDO

# TABLA DE CONTENIDO
- [Introduccion](#Introducción)
- [Ejecucion](#Ejecución)
- [Endpoints](#Endpoints)
- [Instalacion](#Instalación)
- [Instalacion](#Instalación)

## INTRODUCCIÓN
Este proyecto consiste en un servicio REST desarrollado en Java con el framework Spring Boot. La aplicación simula el funcionamiento básico de un sistema bancario, permitiendo actualmente la creación y visualización de clientes, cuentas y el otorgamiento de préstamos. En futuras versiones se incorporarán nuevas funcionalidades como la modificación y eliminación de registros, entre otras mejoras.

## INSTALACIÓN
Para ejecutar este proyecto, es necesario tener instalado Java JDK y Maven. Una vez que se tengan estos requisitos, se puede ejecutar el siguiente comando en la terminal:
- mvn clean install

## EJECUCION
Para iniciar el proyecto, ejecutar el siguiente comando:
- mvn spring-boot:run

La aplicación estará disponible en:

  - 🌐 Servidor por defecto: http://localhost:8080

  - 🧪 Postman

Para ejecutar las pruebas unitarias:
- mvn clean test

## ENDPOINTS
## 1. obtener todos los clientes

  - **Endpoint:**   GET /api/clientes


  - ### 📤 Respuesta esperada

    Devuelve una lista con todos los clientes registrados.

## 2. crear cliente

  - **Endpoint:**   POST /api/clientes

  - ### 📥 Campos del JSON (ClienteDto + PersonaDto)

    | Campo | Tipo | Obligatorio | Descripción |
    |--------|------|-------------|-------------|
    | dni | int | ✅ | DNI del cliente |
    | nombre | String | ✅ | Nombre |
    | apellido | String | ✅ | Apellido |
    | fechaNacimiento | String | ✅ | Formato: YYYY-MM-DD |
    | tipoPersona | String | ✅ | "F" o "J" |
    | banco | String | ✅ | Nombre del banco |

  - ### ✅ Valores válidos para tipoPersona

    | Valor | Significado |
    |--------|-------------|
    | "F" | Persona Física |
    | "J" | Persona Jurídica |

  - ### ✅ Ejemplo

    ``` json
    {
      "dni": 12345678,
      "nombre": "Leonardo",
      "apellido": "Telez",
      "fechaNacimiento": "2000-05-10",
      "tipoPersona": "F",
      "banco": "UTN Bank"
    }
    ```

## 3. obtener cliente por dni

  - **Endpoint:**   GET /api/clientes/{dni}

  - ### 📤 Respuesta esperada

    Devuelve todos los datos del cliente registrado, incluyendo sus cuentas asociadas.

## 4. crear cuenta

  - **Endpoint:**   POST /api/cuentas

  - ### 📥 Campos del JSON (CuentaDto)

    | Campo | Tipo | Obligatorio | Descripción |
    |--------|------|-------------|-------------|
    | titularDni | int | ✅ | DNI del cliente titular |
    | tipoCuenta | String | ✅ | Tipo de cuenta |
    | moneda | String | ✅ | Tipo de moneda |

  - ### ✅ Valores válidos para tipoCuenta

    | Valor | Descripción |
    |--------|-------------|
    | "CAJA_AHORRO" | Caja de ahorro |
    | "CUENTA_CORRIENTE" | Cuenta corriente |

  - ### ✅ Valores válidos para moneda
  
    | Valor | Descripción |
    |--------|-------------|
    | "PESOS" | Pesos argentinos |
    | "DOLARES" | Dólares estadounidenses |

  - ### ✅ Ejemplo

    ``` json
    {
      "titularDni": 12345678,
      "tipoCuenta": "CAJA_AHORRO",
      "moneda": "PESOS"
    }
    ```

## 5. obtener cuenta del cliente por dni

  - **Endpoint:**   GET /api/cuentas/{dni}

  - ### 📤 Respuesta esperada

    Devuelve una lista con todas las cuentas del cliente registrado.

## 6. solicitar préstamo

  - **Endpoint:**   POST /api/prestamos

  - ### 📥 Campos del JSON

    |Campo           |Tipo     |Obligatorio|
    |--------------- |-------- |------------|
    |numeroCliente   |int      |✅|
    |plazoMeses      |int      |✅|
    |montoPrestamo   |double   |✅|
    |moneda          |String   |✅|

  - ### ✅ Ejemplo

    ``` json
    {
      "numeroCliente": 12345678,
      "plazoMeses": 12,
      "montoPrestamo": 500000,
      "moneda": "PESOS"
    }
    ```

## 7. obtener préstamos por dni

  - **Endpoint:**   GET /api/prestamos/{dni}

  - ### 📤 Respuesta esperada

    Devuelve una lista con todos los prestamos realizados por el cliente registrado.
