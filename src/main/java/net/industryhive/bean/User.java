package net.industryhive.bean;

import java.util.Date;

public class User {
    private Integer id;

    private String account;

    private String password;

    private String username;

    private Integer authorityId;

    private Boolean bannedtopic;

    private Boolean bannedreply;

    private Boolean bannedmessage;

    private Boolean locked;

    private Boolean deleted;

    private String email;

    private Integer gender;

    private Integer topiccount;

    private Integer replycount;

    private Date registertime;

    private String introduction;

    private String comefrom;

    private String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public Boolean getBannedtopic() {
        return bannedtopic;
    }

    public void setBannedtopic(Boolean bannedtopic) {
        this.bannedtopic = bannedtopic;
    }

    public Boolean getBannedreply() {
        return bannedreply;
    }

    public void setBannedreply(Boolean bannedreply) {
        this.bannedreply = bannedreply;
    }

    public Boolean getBannedmessage() {
        return bannedmessage;
    }

    public void setBannedmessage(Boolean bannedmessage) {
        this.bannedmessage = bannedmessage;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getTopiccount() {
        return topiccount;
    }

    public void setTopiccount(Integer topiccount) {
        this.topiccount = topiccount;
    }

    public Integer getReplycount() {
        return replycount;
    }

    public void setReplycount(Integer replycount) {
        this.replycount = replycount;
    }

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getComefrom() {
        return comefrom;
    }

    public void setComefrom(String comefrom) {
        this.comefrom = comefrom == null ? null : comefrom.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }
}