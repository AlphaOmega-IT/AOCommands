package de.alphaomega.it.api;

import de.alphaomega.it.AOCommand;
import de.alphaomega.it.database.SessionFactoryManager;
import de.alphaomega.it.database.daos.AOPlayerDao;
import de.alphaomega.it.database.entities.AOPlayer;
import de.alphaomega.it.invHandler.InvManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class AOCommandsAPI {

    private final JavaPlugin pl;
    private final InvManager invManager;
    private final AOCommand aoCommand;

    private final AOPlayers aoPlayers;
    private final AOPlayerDao aoPlayerDao;
    public static SessionFactory sF;

    public static final Map<UUID, AOPlayer> players = new LinkedHashMap<>();

    public AOCommandsAPI(final JavaPlugin pl) {
        setupDatabase(pl);

        this.pl = pl;
        this.invManager = new InvManager(pl);
        this.aoCommand = new AOCommand(pl);

        this.aoPlayerDao = new AOPlayerDao();
        this.aoPlayers = new AOPlayers(this);
    }

    private void setupDatabase(final JavaPlugin pl) {
        SessionFactoryManager sFManager = new SessionFactoryManager(pl);
        sF = sFManager
                .addAnnotatedClazz(AOPlayer.class)
                .build();
    }
}
