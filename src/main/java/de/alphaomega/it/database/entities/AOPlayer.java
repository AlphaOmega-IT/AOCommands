package de.alphaomega.it.database.entities;

import de.alphaomega.it.database.converter.UUIDConverter;
import de.alphaomega.it.database.entitylistener.AOPlayerListener;
import jakarta.persistence.*;
import lombok.*;
import org.bukkit.entity.Player;
import org.hibernate.annotations.NamedQuery;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AOPlayerListener.class)
@Table(name = "AOPLAYER")
@NamedQuery(
        name = "AOPlayer.findAll",
        query = "SELECT AOP FROM AOPlayer AOP"
)
@NamedQuery(
        name = "AOPlayer.findByUUID",
        query = "SELECT AOP FROM AOPlayer AOP WHERE AOP.uuid = :uuid"
)
@NamedQuery(
        name = "AOPlayer.findByName",
        query = "SELECT AOP FROM AOPlayer AOP WHERE AOP.dName = :dName"
)
public class AOPlayer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id", unique = true, nullable = false)
    private long _id;

    @Convert(converter = UUIDConverter.class)
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "dName")
    private String dName;

    @Column(name = "playtime")
    private long playtime = 0;

    @Column(name = "updatedAt")
    private LocalDate updatedAt = LocalDate.now();

    @Column(name = "createdAt")
    private LocalDate createdAt = LocalDate.now();

    public AOPlayer(final Player p) {
        this.uuid = p.getUniqueId();
        this.dName = p.getName();
        this.playtime = p.getPlayerTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AOPlayer aoPlayer)) return false;
        return _id == aoPlayer._id && Objects.equals(uuid, aoPlayer.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, uuid, dName, playtime, updatedAt, createdAt);
    }
}
