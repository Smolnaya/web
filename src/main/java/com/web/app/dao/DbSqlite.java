package com.web.app.dao;

import com.web.app.api.request.UsersIdRequest;
import com.web.app.dao.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    /*
    Получить список всех ID в БД
    Возвращает UsersIdRequest
     */
    public UsersIdRequest getUsersId() {
        String query = "select ID from USER";
        System.out.println(query);
        UsersIdRequest idList = new UsersIdRequest();
        List<Integer> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(query);
            while (resultSet.next()) {
                list.add(resultSet.getInt("ID"));
            }
            idList.setListId(list);
            return idList;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new UsersIdRequest();
        }
    }

    /*
    Выбрать пользователя по ID
    Принимает параметр ID
    Возвращает User
     */
    public User selectUserById(int id) {
        String query = "select * from USER where id = " + id;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(query);
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setNickname(resultSet.getString("name"));
            user.setNumberPhone(resultSet.getString("phone_number"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setElMail(resultSet.getString("mail"));
            user.setVk(resultSet.getString("vk"));
            user.setAboutInf(resultSet.getString("about"));
            user.setStudyGroup(resultSet.getString("study_group"));
            user.setHobbyName(resultSet.getString("hobby_name"));
            user.setHobbyContent(resultSet.getString("hobby_content"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }

    /*
    Добавить пользователя в БД
    Принимает User
     */
    public Boolean insertUser(User user) {
        StringBuilder query = new StringBuilder("insert into USER (name" +
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
                + "','" + user.getTimeBirthday()
                + "','" + user.getElMail()
                + "','" + user.getVk()
                + "','" + user.getAboutInf()
                + "','" + user.getStudyGroup()
                + "','" + user.getHobbyName()
                + "','" + user.getHobbyContent()
                + "');");
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(query.toString());
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить пользователя", ex);
            return null;
        }
    }
}