package de.alphaomega.it.database.daos;

import de.alphaomega.it.api.AOCommandsAPI;
import org.hibernate.Session;
import org.hibernate.query.Query;

public abstract class BaseDao<T> {

    protected abstract Class<T> getClassType();

    public void create(final T data) {
        Session s = AOCommandsAPI.sF.openSession();
        try {
            s.clear();
            s.beginTransaction();
            s.persist(data);
            s.getTransaction().commit();
        } catch (final Exception e) {
            if (s.getTransaction() != null && s.getTransaction().isActive())
                s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void update(final T data) {
        Session s = AOCommandsAPI.sF.openSession();
        try {
            s.clear();
            s.beginTransaction();
            s.merge(data);
            s.flush();
            s.getTransaction().commit();
        } catch (final Exception e) {
            if (s.getTransaction() != null && s.getTransaction().isActive())
                s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(final T data) {
        Session s = AOCommandsAPI.sF.openSession();
        try {
            s.clear();
            s.beginTransaction();
            s.remove(data);
            s.flush();
            s.getTransaction().commit();
        } catch (final Exception e) {
            if (s.getTransaction() != null && s.getTransaction().isActive())
                s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public Query<T> createNamedQuery(final String queryName) {
        Session s = AOCommandsAPI.sF.openSession();
        return s.createNamedQuery(queryName, getClassType());
    }
}
