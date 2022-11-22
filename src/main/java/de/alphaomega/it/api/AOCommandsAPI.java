package de.alphaomega.it.api;

import de.alphaomega.it.AOCommand;
import de.alphaomega.it.database.SessionFactoryManager;
import de.alphaomega.it.database.daos.AOPlayerDao;
import de.alphaomega.it.database.daos.AOSpawnDao;
import de.alphaomega.it.database.entities.AOPlayer;
import de.alphaomega.it.database.entities.AOSpawn;
import de.alphaomega.it.invhandler.InvManager;
import de.alphaomega.it.maven.LibraryLoader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class AOCommandsAPI {

    public final Map<UUID, AOPlayer> players = new LinkedHashMap<>();
    private final JavaPlugin pl;
    private final InvManager invManager;
    private final AOCommand aoCommand;
    private final AOPlayers aoPlayers;
    private final AOPlayerDao aoPlayerDao;
    private final AOSpawnDao aoSpawnDao;
    private final SessionFactory sF;

    public AOCommandsAPI(final JavaPlugin pl) {
        this.aoCommand = new AOCommand(pl);

        loadDependencies();

        this.pl = pl;
        this.sF = setupDatabase(pl);
        this.invManager = new InvManager(pl);

        this.aoPlayerDao = new AOPlayerDao();
        this.aoSpawnDao = new AOSpawnDao();
        this.aoPlayers = new AOPlayers(this);
    }

    private SessionFactory setupDatabase(final JavaPlugin pl) {
        SessionFactoryManager sFManager = new SessionFactoryManager(pl);
        return sFManager
                .addAnnotatedClazz(AOPlayer.class)
                .addAnnotatedClazz(AOSpawn.class)
                .build();
    }

    @SneakyThrows
    private void loadDependencies() {
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("jakarta.persistence", "jakarta.persistence-api", "3.1.0"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("jakarta.transaction", "jakarta.transaction-api", "2.0.1"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("jakarta.activation", "jakarta.activation-api", "2.1.0"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("jakarta.inject", "jakarta.inject-api", "2.0.1"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("jakarta.xml.bind", "jakarta.xml.bind-api", "4.0.0"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("com.fasterxml", "classmate", "1.5.1"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("net.bytebuddy", "byte-buddy", "1.12.19"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.antlr", "antlr4-runtime", "4.11.1"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.glassfish.jaxb", "jaxb-runtime", "4.0.1"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.hibernate.common", "hibernate-commons-annotations", "6.0.5.Final"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.jboss", "jandex", "2.4.3.Final"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.jboss.logging", "jboss-logging", "3.5.0.Final"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("org.hibernate.orm", "hibernate-core", "6.1.5.Final"));
        this.aoCommand.getLoader().load(new LibraryLoader.Dependency("mysql", "mysql-connector-java", "8.0.30"));
    }
}
