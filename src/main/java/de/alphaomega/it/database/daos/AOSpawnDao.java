package de.alphaomega.it.database.daos;

import de.alphaomega.it.database.entities.AOSpawn;
import jakarta.persistence.NoResultException;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class AOSpawnDao extends BaseDao<AOSpawn> {

    @Override
    protected Class<AOSpawn> getClassType() {
        return AOSpawn.class;
    }

    public List<AOSpawn> findAll() {
        Query<AOSpawn> query = this.createNamedQuery("AOSpawn.findAll");
        try {
            return query.getResultList();
        } catch (final NoResultException exc) {
            return Collections.emptyList();
        }
    }

    public AOSpawn findByServer(final String server) {
        Query<AOSpawn> query = this.createNamedQuery("AOSpawn.findByServer");
        query.setParameter("serverName", server);
        return query.getSingleResultOrNull();
    }
}
