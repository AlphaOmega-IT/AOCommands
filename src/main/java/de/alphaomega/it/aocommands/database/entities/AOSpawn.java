package de.alphaomega.it.aocommands.database.entities;

import de.alphaomega.it.aocommands.database.converter.LocationConverter;
import de.alphaomega.it.aocommands.database.entitylistener.AOSpawnListener;
import jakarta.persistence.*;
import lombok.*;
import org.bukkit.Location;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AOSpawnListener.class)
@Table(name = "AOSPAWN")
@org.hibernate.annotations.NamedQuery(
        name = "AOSpawn.findAll",
        query = "SELECT AOS FROM AOSpawn AOS"
)
@org.hibernate.annotations.NamedQuery(
        name = "AOSpawn.findByServer",
        query = "SELECT AOS FROM AOSpawn AOS WHERE serverName = :serverName"
)
public class AOSpawn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id", unique = true, nullable = false)
    private long _id;

    @Column(name = "serverName", nullable = false, unique = true)
    private String serverName;

    @Convert(converter = LocationConverter.class)
    @Column(name = "location", nullable = false)
    private Location location;

    @Column(name = "updatedAt")
    private LocalDate updatedAt = LocalDate.now();

    @Column(name = "createdAt")
    private LocalDate createdAt = LocalDate.now();

    public AOSpawn(final Location loc, final String server) {
        this.location = loc;
        this.serverName = server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AOSpawn aoSpawn)) return false;
        return _id == aoSpawn._id && Objects.equals(serverName, aoSpawn.serverName) && Objects.equals(location, aoSpawn.location) && Objects.equals(updatedAt, aoSpawn.updatedAt) && Objects.equals(createdAt, aoSpawn.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, serverName, location, updatedAt, createdAt);
    }
}
