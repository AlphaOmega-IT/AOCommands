package de.alphaomega.it.aocommands.database.entitylistener;

import com.mysql.cj.log.Log;
import com.mysql.cj.log.LogFactory;
import jakarta.persistence.*;

public abstract class BaseEntityListener<T> {

    protected Log log = LogFactory.getLogger(this.getClassType().getName(), this.getClassType().getName());

    public abstract Class<T> getClassType();

    @PrePersist
    protected void beforeAnyInsert(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] Entity is added " + entity.toString());
    }

    @PreUpdate
    protected void beforeAnyUpdate(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] Entity is updated " + entity.toString());
    }

    @PreRemove
    protected void beforeAnyDelete(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] Entity is deleted " + entity.toString());
    }

    @PostPersist
    protected void afterAnyInsert(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] add of Entity " + entity.toString() + " finished.");
    }

    @PostUpdate
    protected void afterAnyUpdate(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] update of Entity " + entity.toString() + " finished.");
    }

    @PostRemove
    protected void afterAnyDelete(final T entity) {
        log.logInfo("[" + getClassType().getName() + "] delete of Entity " + entity.toString() + " finished.");
    }
}
