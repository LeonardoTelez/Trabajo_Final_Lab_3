package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
    private String nombre;
    private String apellido;
    private int dni;
    private LocalDate fechaNacimiento;

    public Persona() {}
    public Persona(int dni, String apellido, String nombre, String fechaNacimiento) {
    this.dni = dni;
    this.apellido = apellido;
    this.nombre = nombre;
    if (fechaNacimiento != null) {
        this.fechaNacimiento = LocalDate.parse(fechaNacimiento);
    }
}


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
    if (fechaNacimiento == null) {
        return 0; // o el valor que quieras por defecto
    }
    LocalDate currentDate = LocalDate.now();
    return Period.between(fechaNacimiento, currentDate).getYears();
}

}

