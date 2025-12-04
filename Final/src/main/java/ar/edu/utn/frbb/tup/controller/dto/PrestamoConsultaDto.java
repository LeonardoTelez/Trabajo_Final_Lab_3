package ar.edu.utn.frbb.tup.controller.dto;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.model.PrestamoConsulta;

public class PrestamoConsultaDto {
    private int numeroCliente;
    private List<PrestamoConsulta> prestamos;

    public PrestamoConsultaDto(int dni) {
        this.numeroCliente = dni;
        this.prestamos = new ArrayList<PrestamoConsulta>();
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(int numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public List<PrestamoConsulta> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<PrestamoConsulta> prestamos) {
        this.prestamos = prestamos;
    }

    public void addPrestamos(PrestamoConsulta prestamo) {
        this.prestamos.add(prestamo);
    }
}
