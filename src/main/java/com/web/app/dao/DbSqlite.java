package com.web.app.dao;

import com.web.app.dao.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DbSqlite implements InitializingBean {
    private Logger log = Logger.getLogger(getClass().getName());

    private String dbPath = "webapp.db";

    @Override
    public void afterPropertiesSet() throws Exception {
        initDb();
    }

    public void initDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            }
        } catch (ClassNotFoundException | SQLException ex) {
            log.log(Level.WARNING, "База не подключена", ex);
        }
    }

    public Boolean execute(String query) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(query);
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запуск", ex);
            return false;
        }
    }

    public User selectUserById(int id) {
        String query = "select * from USER where id = " + id;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(query);
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setNickname(resultSet.getString("nickname"));
            user.setNumberPhone(resultSet.getString("numberPhone"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setElMail(resultSet.getString("elMail"));
            user.setVk(resultSet.getString("vk"));
            user.setAboutInf(resultSet.getString("aboutInf"));
            user.setStudyGroup(resultSet.getString("studyGroup"));
            user.setHobbyName(resultSet.getString("hobbyName"));
            user.setHobbyContent(resultSet.getString("hobbyContent"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }

    public Boolean insertUser(User user) {
        String query = "insert into USER (name" +
                ", phone_number" +
                ", birthday" +
                ", mail" +
                ", vk" +
                ", about" +
                ", study_group" +
                ", hobby_name" +
                ", hobby_content" +
                ") values " +
                "('" + user.getNickname()
                + "','" + user.getNumberPhone()
                + "','" + user.getBirthday()
//                + "','" + user.getTimeBirthday()
                + "','" + user.getElMail()
                + "','" + user.getVk()
                + "','" + user.getAboutInf()
                + "','" + user.getStudyGroup()
                + "','" + user.getHobbyName()
                + "','" + user.getHobbyContent()
                + "');";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(query);
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить пользователя", ex);
            return null;
        }
    }
}