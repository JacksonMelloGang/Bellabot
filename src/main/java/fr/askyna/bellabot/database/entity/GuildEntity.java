package fr.askyna.bellabot.database.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Date;


@Entity
@Table(name="guild")
public class GuildEntity {

    @Id
    @Column(name = "guild_id")
    Long guildId;

    String guildName;

    Long ownerId;

    OffsetDateTime creationDate;

    @Column(nullable = true)
    String logModChannelId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;


    public GuildEntity(){

    }

    public GuildEntity(Long guildId, String guildName, Long ownerId, OffsetDateTime creationDate) {
        this.guildId = guildId;
        this.guildName = guildName;
        this.ownerId = ownerId;
        this.creationDate = creationDate;
    }

    public GuildEntity(Long guildId, String guildName, Long ownerId, OffsetDateTime creationDate, String logModChannelId) {
        this.guildId = guildId;
        this.guildName = guildName;
        this.ownerId = ownerId;
        this.creationDate = creationDate;
        this.logModChannelId = logModChannelId;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getLogModChannelId() {
        return logModChannelId;
    }

    public void setLogModChannelId(String logModChannelId) {
        this.logModChannelId = logModChannelId;
    }
}
