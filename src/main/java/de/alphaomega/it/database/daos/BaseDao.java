package de.alphaomega.it.database.daos;

import de.alphaomega.it.AOCommands;
import org.hibernate.Session;
import org.hibernate.query.Query;

public abstract class BaseDao<T> {

    protected abstract Class<T> getClassType();

    public void create(final T data) {
        Session session = AOCommands.getInstance().getSF().openSession();;
        try {
            session.clear();
            session.beginTransaction();
            session.persist(data);
            session.getTransaction().commit();
        } catch (final Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void update(final T data) {
        Session session = AOCommands.getInstance().getSF().openSession();;
        try {
            session.clear();
            session.beginTransaction();
            session.merge(data);
            session.flush();
            session.getTransaction().commit();
        } catch (final Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(final T data) {
        Session session = AOCommands.getInstance().getSF().openSession();;
        try {
            session.clear();
            session.beginTransaction();
            session.remove(data);
            session.flush();
            session.getTransaction().commit();
        } catch (final Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public Query<T> createNamedQuery(final String queryName) {
        Session session = AOCommands.getInstance().getSF().openSession();;
        return session.createNamedQuery(queryName, getClassType());
    }
}
