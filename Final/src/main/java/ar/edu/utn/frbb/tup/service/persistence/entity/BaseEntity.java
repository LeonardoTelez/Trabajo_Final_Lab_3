package ar.edu.utn.frbb.tup.service.persistence.entity;

public class BaseEntity {
    private final int Id;

    public BaseEntity(int id) {
        Id = id;
    }

    public BaseEntity() {
        Id = 0;
    }

    public int getId() {
        return Id;
    }
}