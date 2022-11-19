package de.alphaomega.it.api;

import com.mysql.cj.util.StringUtils;
import de.alphaomega.it.database.entities.AOPlayer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@Getter
public class AOPlayers {

    private final AOCommandsAPI api;

    public AOPlayers(final AOCommandsAPI api) {
        this.api = api;
    }

    @SneakyThrows
    public void createPlayer(final Player p) {
        if (api.getPlayers().containsKey(p.getUniqueId())) {
            api.getPlayers().get(p.getUniqueId());
            return;
        }

        AOPlayer aoP = api.getAoPlayerDao().findByUUID(p.getUniqueId());

        CompletableFuture.supplyAsync(() -> aoP == null ? dbUserCreate(new AOPlayer(p)) : aoP).thenApplyAsync(aoPlayer -> {
            updateDName(p, aoPlayer);
            return aoPlayer;
        }).thenApply(aoPlayer -> {
            api.getAoPlayers().updateUser(aoPlayer);
            api.getPlayers().put(p.getUniqueId(), aoPlayer);
            return aoPlayer;
        });
    }

    public void updateDName(final Player p, final AOPlayer aoP) {
        if (StringUtils.nullSafeEqual(p.getName(), aoP.getDName())) return;

        aoP.setDName(p.getName());

        CompletableFuture.supplyAsync(() -> {
            dbUserUpdate(aoP);
            return aoP;
        }).thenAccept(this::updateUser);
    }

    public Player getPlayer(final AOPlayer aoP) {
        return Bukkit.getServer().getOfflinePlayer(aoP.getUuid()).getPlayer();
    }

    public AOPlayer getAOPlayer(final Player p) {
        return api.getPlayers().get(p.getUniqueId());
    }

    public void updateUser(final AOPlayer aoP) {
        api.getPlayers().computeIfPresent(aoP.getUuid(), ((uuid, aoPlayer) -> aoP));
    }

    public void dbUserUpdate(final AOPlayer aoP) {
        api.getAoPlayerDao().update(aoP);
    }

    private AOPlayer dbUserCreate(final AOPlayer aoP) {
        api.getAoPlayerDao().create(aoP);
        return aoP;
    }
}
