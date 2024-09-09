package fr.askyna.bellabot.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

@jakarta.persistence.Entity(name = "user")
public class UserEntity {

    @Id
    @Column(name="userId")
    Long userId;

    @Column(nullable = true)
    String email;

    @Column(nullable = true)
    String password;

    String username;

    String avatar;

    String creationDate;

    public UserEntity(){

    }

    public UserEntity(Long userId, String username, String avatar, String creationDate) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.creationDate = creationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}
