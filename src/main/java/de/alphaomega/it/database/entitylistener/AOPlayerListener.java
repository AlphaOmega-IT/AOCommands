package de.alphaomega.it.database.entitylistener;

import de.alphaomega.it.database.entities.AOPlayer;

import java.time.LocalDate;

public class AOPlayerListener extends BaseEntityListener<AOPlayer> {

    @Override
    public Class<AOPlayer> getClassType() {
        return AOPlayer.class;
    }

    @Override
    protected void afterAnyUpdate(final AOPlayer entity) {
        entity.setUpdatedAt(LocalDate.now());

        super.afterAnyUpdate(entity);
    }
}
