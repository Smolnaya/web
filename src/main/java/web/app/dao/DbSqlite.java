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
    private final String selectNickname = "select NICKNAME from USER where NICKNAME = '%s'";
    private final String selectUserData = "select * from USER where NICKNAME = '%s'";
    private final String insertMessage = "insert into MESSAGE (SENDER, RECIPIENT, MESSAGE, SENDING_TIME) VALUES ('%s', '%s', '%s', '%s')";
    private final String selectAllMessages = "select * from MESSAGE where (SENDER = '%s' or SENDER = '%s') and " +
            "(RECIPIENT = '%s' or RECIPIENT = '%s') order by SENDING_TIME";
    private final String selectChats = "select distinct SENDER, RECIPIENT from MESSAGE where SENDER = '%s' or RECIPIENT = '%s'";
    private final String countMessages = "select count(*) from MESSAGE  where (SENDER = '%s' or SENDER = '%s') and " +
            "(RECIPIENT = '%s' or RECIPIENT = '%s')";
    private final String insertUser = "insert into USER (name, phone_number, birthday, mail, vk, about, study_group, " +
            "hobby_name, hobby_content, gender, education, password, role, nickname) " +
            "values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";

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

    public Boolean isNicknameExistRequest(String nickname) {
        log.log(Level.INFO, "Запрос: " + String.format(selectNickname, nickname));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectNickname, nickname));
            return resultSet.next();
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос isNameExistRequest", ex);
            return false;
        }
    }

    public User selectUserData(String nickname) {
        log.log(Level.INFO, "Запрос: " + String.format(selectUserData, nickname));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(selectUserData, nickname));
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setNickname(resultSet.getString("nickname"));
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
            user.setNickname(resultSet.getString("nickname"));
            user.setRole(resultSet.getString("role"));
            user.setPassword(resultSet.getString("password"));
            return user;
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос: " + String.format(selectUserData, nickname), ex);
            return new User();
        }
    }

    public Boolean insertUser(User user) {
        log.log(Level.INFO, "Запрос: " + String.format(insertUser, user.getName(), user.getNumberPhone(), user.getTimeBirthday(),
                user.getElMail(), user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                user.getHobbyContent(), user.getGender(), user.getEducation(), user.getPassword(),
                user.getRole(), user.getNickname()));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            return stat.execute(String.format(insertUser, user.getName(), user.getNumberPhone(), user.getTimeBirthday(),
                    user.getElMail(), user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                    user.getHobbyContent(), user.getGender(), user.getEducation(), user.getPassword(),
                    user.getRole(), user.getNickname()));
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

    /*only admin*/
    private final String selectFirst = "select * from USER LIMIT 1";
    private final String selectNext = "select * from USER where ID > %s limit 1";
    private final String selectPrevious = "select * from USER where ID < %s order by ID desc limit 1";

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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery(String.format(query, id));
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setNickname(resultSet.getString("nickname"));
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
                user.setRole(resultSet.getString("role"));
                return user;
            }
            return new User();
        } catch (SQLException ex) {
            log.log(Level.WARNING, "Не удалось выполнить запрос", ex);
            return new User();
        }
    }
}