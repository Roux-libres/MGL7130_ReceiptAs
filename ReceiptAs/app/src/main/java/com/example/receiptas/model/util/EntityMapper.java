package com.example.receiptas.model.util;

public interface EntityMapper<Entity, DomainClass> {
    public DomainClass mapFromEntity(Entity entity);
    public Entity mapToEntity(DomainClass domainObject);
}
