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
    public void createPlayer(final Player player) {
        if (this.api.getPlayers().containsKey(player.getUniqueId())) {
            this.api.getPlayers().get(player.getUniqueId());
            return;
        }

        final AOPlayer aoPlayer = this.api.getAoPlayerDao().findByUUID(player.getUniqueId());

        CompletableFuture.supplyAsync(() -> aoPlayer == null ? dbUserCreate(new AOPlayer(player)) : aoPlayer).thenApplyAsync(aaoPlayer -> {
            updateDName(player, aoPlayer);
            return aaoPlayer;
        }).thenApply(aaoPlayer -> {
            api.getAoPlayers().updateUser(aaoPlayer);
            api.getPlayers().put(player.getUniqueId(), aaoPlayer);
            return aaoPlayer;
        });
    }

    public void updateDName(final Player player, final AOPlayer aoPlayer) {
        if (StringUtils.nullSafeEqual(player.getName(), aoPlayer.getDName())) return;

        aoPlayer.setDName(player.getName());

        CompletableFuture.supplyAsync(() -> {
            dbUserUpdate(aoPlayer);
            return aoPlayer;
        }).thenAccept(this::updateUser);
    }

    public Player getPlayer(final AOPlayer aoPlayer) {
        return Bukkit.getServer().getOfflinePlayer(aoPlayer.getUuid()).getPlayer();
    }

    public AOPlayer getAOPlayer(final Player player) {
        return this.api.getPlayers().get(player.getUniqueId());
    }

    public void updateUser(final AOPlayer aoPlayer) {
        this.api.getPlayers().computeIfPresent(aoPlayer.getUuid(), ((uuid, aoP) -> aoPlayer));
    }

    public void dbUserUpdate(final AOPlayer aoPlayer) {
        this.api.getAoPlayerDao().update(aoPlayer);
    }

    private AOPlayer dbUserCreate(final AOPlayer aoPlayer) {
        this.api.getAoPlayerDao().create(aoPlayer);
        return aoPlayer;
    }
}
