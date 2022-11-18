package de.alphaomega.it.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.logging.Level;

@Getter
public class SessionFactoryManager {

    private SessionFactory sF;
    private Configuration cfg;

    public SessionFactoryManager(final JavaPlugin pl) {
        pl.saveResource("database/hibernate.cfg.xml", false);
        File f = new File(pl.getDataFolder() + "/database/", "hibernate.cfg.xml");

        try {
            cfg = new Configuration().configure(f);
        } catch (final Exception e) {
            pl.getLogger().log(Level.SEVERE, "Hibernate.cfg.xml file does not exists in database folder.");
            pl.onDisable();
        }
    }

    public SessionFactoryManager addAnnotatedClazz(final Class<?> clazz) {
        cfg.addAnnotatedClass(clazz);
        return this;
    }

    public SessionFactory build() {
        sF = cfg.buildSessionFactory();
        return sF;
    }

}
