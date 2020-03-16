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
            user.setName(resultSet.getString("name"));
            user.setNumberPhone(resultSet.getString("phone_number"));
            user.setBirthday(resultSet.getDate("birthday"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }

    public Boolean insertUser(User user) {
        String query = "insert into USER (name, phone_number, birthday) values ('" + user.getName() + "','" + user.getNumberPhone() + "','" + user.getTimeBirthday() + "');";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(query);
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить пользователя", ex);
            return null;
        }
    }
}