package de.alphaomega.it.aocommands.database.daos;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.database.entities.AOPlayer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class AOPlayerDao extends BaseDao<AOPlayer> {

    @Override
    protected Class<AOPlayer> getClassType() {
        return AOPlayer.class;
    }

    public List<AOPlayer> findAll() {
        Query<AOPlayer> query = this.createNamedQuery("AOPlayer.findAll");
        return query.getResultList();
    }

    public AOPlayer findByUUID(final UUID uuid) {
        Query<AOPlayer> query = this.createNamedQuery("AOPlayer.findByUUID");
        query.setParameter("uuid", uuid);
        return query.getSingleResultOrNull();
    }

    public AOPlayer findByName(final String name) {
        Query<AOPlayer> query = this.createNamedQuery("AOPlayer.findByName");
        query.setParameter("dName", name);
        return query.getSingleResultOrNull();
    }

    public void updateByUser(final AOPlayer aoP) {
        Session s = AOCommands.getInstance().getSF().openSession();
        try {
            s.clear();
            s.beginTransaction();
            CriteriaBuilder cBuilder = s.getCriteriaBuilder();
            CriteriaUpdate<AOPlayer> cUpdate = cBuilder.createCriteriaUpdate(AOPlayer.class);
            Root<AOPlayer> aoPRoot = cUpdate.getRoot();

            cUpdate.set("dName", aoP.getDName());
            cUpdate.set("updatedAt", aoP.getUpdatedAt());
            cUpdate.set("playtime", aoP.getPlaytime());
            cUpdate.where(cBuilder.equal(aoPRoot.get("_id"), aoP.get_id()));
            s.createMutationQuery(cUpdate).executeUpdate();

            s.flush();
            s.getTransaction().commit();
        } catch (final Exception e) {
            if (s.getTransaction() != null && s.getTransaction().isActive())
                s.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
    }
}
