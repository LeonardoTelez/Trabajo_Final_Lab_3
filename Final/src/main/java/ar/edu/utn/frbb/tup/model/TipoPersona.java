package ar.edu.utn.frbb.tup.model;

public enum TipoPersona {

    PERSONA_FISICA("F"),
    PERSONA_JURIDICA("J");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoPersona fromString(String text) {
    text = text.trim().toUpperCase();

    if (text.equals("F") || text.equals("FISICA") || text.equals("PERSONA_FISICA"))
        return PERSONA_FISICA;

    if (text.equals("J") || text.equals("JURIDICA") || text.equals("PERSONA_JURIDICA"))
        return PERSONA_JURIDICA;

    throw new IllegalArgumentException("TipoPersona inválido: " + text);
}
}
