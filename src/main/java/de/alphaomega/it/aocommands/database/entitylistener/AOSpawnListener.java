package de.alphaomega.it.aocommands.database.entitylistener;

import de.alphaomega.it.aocommands.database.entities.AOSpawn;

import java.time.LocalDate;

public class AOSpawnListener extends BaseEntityListener<AOSpawn> {

    @Override
    public Class<AOSpawn> getClassType() {
        return AOSpawn.class;
    }

    @Override
    protected void afterAnyUpdate(final AOSpawn entity) {
        entity.setUpdatedAt(LocalDate.now());

        super.afterAnyUpdate(entity);
    }
}
