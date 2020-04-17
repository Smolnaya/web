package web.app.dao;

import web.app.dao.model.Message;
import web.app.dao.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DbSqlite implements InitializingBean {
    private Logger log = Logger.getLogger(getClass().getName());
    private String dbPath = "webapp.db";
    private final String selectFirst = "select * from USER LIMIT 1";
    private final String selectNext = "select * from USER where ID > %s limit 1";
    private final String selectPrevious = "select * from USER where ID < %s order by ID desc limit 1";
    private final String selectName = "select NAME from USER where NAME = '%s'";
    private final String selectUser = "select * from USER where NAME = '%s'";
    private final String selectPassword = "select PASSWORD from USER where NAME = '%s'";
    private final String insertMessage = "insert into MESSAGE (SENDER, RECIPIENT, MESSAGE, SENDING_TIME) VALUES ('%s', '%s', '%s', '%s')";
    private final String selectAllMessages = "select * from MESSAGE where (SENDER = '%s' or SENDER = '%s') and " +
            "(RECIPIENT = '%s' or RECIPIENT = '%s') order by SENDING_TIME";
    private final String selectChats = "select distinct SENDER, RECIPIENT from MESSAGE where SENDER = '%s' or RECIPIENT = '%s'";
    private final String countMessages = "select count(*) from MESSAGE  where (SENDER = '%s' or SENDER = '%s') and " +
            "(RECIPIENT = '%s' or RECIPIENT = '%s')";

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

    public Boolean isNameExistRequest(String name) {
        log.log(Level.INFO, "Запрос: " + String.format(selectName, name));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectName, name));
            return resultSet.next();
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос isNameExistRequest", ex);
            return false;
        }
    }

    public Boolean isMyUser(String name, String password) {
        log.log(Level.INFO, "Запрос: " + String.format(selectPassword, name));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectPassword, name));
            return resultSet.getString("password").equals(password);
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос isNameExistRequest", ex);
            return false;
        }
    }

    public User selectUser(String name) {
        log.log(Level.INFO, "Запрос: " + String.format(selectUser, name));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectUser, name));
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
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }

    public User selectFirstUser() {
        User user = getUser(selectFirst, 0);
        return user;
    }

    public User selectNextUser(int id) {
        User user = getUser(selectNext, id);
        return user;
    }

    public User selectPreviousUser(int id) {
        User user = getUser(selectPrevious, id);
        return user;
    }

    public User getUser(String query, int id) {
        log.log(Level.INFO, "Запрос: " + String.format(query, id));
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
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }

    public Boolean insertUser(User user) {
        String query = "insert into USER (name, phone_number, birthday, mail, vk, about, study_group, hobby_name, hobby_content, gender, education, password, role) " +
                "values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(String.format(query, user.getNickname(), user.getNumberPhone(), user.getTimeBirthday(),
                    user.getElMail(), user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                    user.getHobbyContent(), user.getGender(), user.getEducation(), user.getPassword(), user.getRole()));
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить пользователя", ex);
            return false;
        }
    }

    public Boolean insertMessage(Message message) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(String.format(insertMessage, message.getSender(), message.getRecipient(),
                    message.getMessage(), message.getTimeSending()));
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось добавить сообщение", ex);
            return false;
        }
    }

    public List<Message> selectAllMessages(String sender, String recipient) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectAllMessages, sender, recipient, sender, recipient));
            List<Message> messageList = new ArrayList<>();
            if (resultSet.next()) {
                do {
                    Message message = new Message();
                    message.setSender(resultSet.getString("sender"));
                    message.setRecipient(resultSet.getString("recipient"));
                    message.setMessage(resultSet.getString("message"));
                    message.setSendingTime(resultSet.getDate("sending_time"));
                    messageList.add(message);
                }
                while (resultSet.next());
                return messageList;
            } else return new ArrayList<>();
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось получить сообщения", ex);
            return new ArrayList<>();
        }
    }

    public List<String> selectChats(String sender) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectChats, sender, sender));
            HashSet<String> hashSet = new HashSet<>();
            if (resultSet.next()) {
                do {
                    hashSet.add(resultSet.getString("sender"));
                    hashSet.add(resultSet.getString("recipient"));
                }
                while (resultSet.next());
                return new ArrayList<>(hashSet);
            } else return new ArrayList<>();
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось получить сообщения", ex);
            return new ArrayList<>();
        }
    }

    public Boolean isNewMessage(String sender, String recipient, int messagesAmount) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(countMessages, sender, recipient, sender, recipient));
            return resultSet.getInt("count(*)") > messagesAmount;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось получить сообщения", ex);
            return false;
        }
    }
}