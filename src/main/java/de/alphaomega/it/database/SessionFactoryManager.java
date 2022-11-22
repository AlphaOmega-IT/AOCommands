package de.alphaomega.it.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;

@Getter
public class SessionFactoryManager {

    private SessionFactory sF;
    private Configuration cfg;

    public SessionFactoryManager(final JavaPlugin plugin) {
        plugin.saveResource("database/connection.json", false);
        File f = new File(plugin.getDataFolder() + "/database/", "connection.json");

        try {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(new JsonReader(new FileReader(f)), JsonObject.class);

            Properties properties = new Properties();

            properties.setProperty("hibernate.dialect", json.get("hibernate.dialect").getAsString());
            properties.setProperty("connection.driver_class", json.get("connection.driver_class").getAsString());
            properties.setProperty("hibernate.connection.url", json.get("hibernate.connection.url").getAsString());
            properties.setProperty("hibernate.connection.username", json.get("hibernate.connection.username").getAsString());
            properties.setProperty("hibernate.connection.password", json.get("hibernate.connection.password").getAsString());
            properties.setProperty("hibernate.hbm2ddl.auto", json.get("hibernate.hbm2ddl.auto").getAsString());
            properties.setProperty("current_session_context_class", json.get("current_session_context_class").getAsString());

            this.cfg = new Configuration().addProperties(properties);
        } catch (final Exception e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "connection.json file does not exists in database folder or is incorrect.");
            plugin.onDisable();
        }
    }

    public SessionFactoryManager addAnnotatedClazz(final Class<?> clazz) {
        if (this.cfg == null) return this;
        this.cfg.addAnnotatedClass(clazz);
        return this;
    }

    public SessionFactory build() {
        if (this.cfg == null) return null;
        this.sF = this.cfg.buildSessionFactory();
        return this.sF;
    }

}
