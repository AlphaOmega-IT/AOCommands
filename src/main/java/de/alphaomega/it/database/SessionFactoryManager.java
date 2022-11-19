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

    public SessionFactoryManager(final JavaPlugin plugin) {
        plugin.saveResource("database/hibernate.cfg.xml", false);
        File f = new File(plugin.getDataFolder() + "/database/", "hibernate.cfg.xml");

        try {
            this.cfg = new Configuration().configure(f);
        } catch (final Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Hibernate.cfg.xml file does not exists in database folder.");
            plugin.onDisable();
        }
    }

    public SessionFactoryManager addAnnotatedClazz(final Class<?> clazz) {
        this.cfg.addAnnotatedClass(clazz);
        return this;
    }

    public SessionFactory build() {
        this.sF = this.cfg.buildSessionFactory();
        return this.sF;
    }

}
