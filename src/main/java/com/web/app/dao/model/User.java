package com.web.app.dao.model;

import java.util.Date;

public class User {
    private Integer id;
    private String nickname;
    private String numberPhone;
    private Date birthday;
    private String elMail;
    private String vk;
    private String aboutInf;
    private String studyGroup;
    private String hobbyName;
    private String hobbyContent;

    public String getElMail() { return elMail; }

    public void setElMail(String elMail) { this.elMail = elMail; }

    public String getVk() { return vk; }

    public void setVk(String vk) { this.vk = vk; }

    public String getAboutInf() { return aboutInf; }

    public void setAboutInf(String aboutInf) { this.aboutInf = aboutInf; }

    public String getStudyGroup() { return studyGroup; }

    public void setStudyGroup(String studyGroup) { this.studyGroup = studyGroup; }

    public String getHobbyName() { return hobbyName; }

    public void setHobbyName(String hobbyName) { this.hobbyName = hobbyName; }

    public String getHobbyContent() { return hobbyContent; }

    public void setHobbyContent(String hobbyContent) { this.hobbyContent = hobbyContent; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

//    public Long getTimeBirthday() {
//        return birthday.getTime();
//    }
}