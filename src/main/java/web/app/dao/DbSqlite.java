package web.app.dao;

import web.app.dao.model.User;
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

    private final String selectFirst = "select * from USER LIMIT 1";
    private final String selectNext = "select * from USER where ID > %s limit 1";
    private final String selectPrevious = "select * from USER where ID < %s order by ID desc limit 1";

    /*
    Выбрать первого пользователя в БД
     */
    public User selectFirstUser() {
        User user = getUser(selectFirst, 0);
        return user;
    }

    /*
    Выбрать следующего пользователя в БД
     */
    public User selectNextUser(int id) {
        User user = getUser(selectNext, id);
        return user;
    }

    /*
    Выбрать предыдущего пользователя в БД
     */
    public User selectPreviousUser(int id) {
        User user = getUser(selectPrevious, id);
        return user;
    }

    public User getUser(String query, int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(query, id));
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
            user.setGender(resultSet.getString("gender"));
            user.setEducation(resultSet.getString("education"));
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
        String query = "insert into USER (name, phone_number, birthday, mail, vk, about, study_group, hobby_name, hobby_content, gender, education) " +
                "values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(String.format(query, user.getNickname(), user.getNumberPhone(), user.getTimeBirthday(),
                    user.getElMail(), user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                    user.getHobbyContent(), user.getGender(), user.getEducation()));
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить пользователя", ex);
            return false;
        }
    }
}