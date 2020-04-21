package web.app.services;

import org.springframework.stereotype.Service;
import web.app.dao.DbSqlite;
import web.app.dao.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckDataService {
    private Logger log = Logger.getLogger(getClass().getName());
    private DbSqlite dbSqlite = new DbSqlite();

    public List<String> checkInputData(User user) {
        List<String> errors = new ArrayList<>();
        if (!checkNonEmptyData(user.getName(), user.getNumberPhone(), user.getBirthday(), user.getElMail(),
                user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                user.getHobbyContent(), user.getEducation(), user.getPassword(),
                user.getNickname()).isEmpty()) {
            errors.addAll(checkNonEmptyData(user.getName(), user.getNumberPhone(), user.getBirthday(), user.getElMail(),
                    user.getVk(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(),
                    user.getHobbyContent(), user.getEducation(), user.getPassword(),
                    user.getNickname()));
        } else {
            if (!checkInputNumber(user.getNumberPhone()).isEmpty()) errors.add(checkInputNumber(user.getNumberPhone()));
            if (!checkInputBirth(user.getBirthday()).isEmpty()) errors.add(checkInputBirth(user.getBirthday()));
            if (!checkInputMail(user.getElMail()).isEmpty()) errors.add(checkInputMail(user.getElMail()));
            if (!checkInputVk(user.getVk()).isEmpty()) errors.add(checkInputVk(user.getVk()));
            if (!checkNicknameExisting(user.getNickname()).isEmpty()) errors.add(checkNicknameExisting(user.getName()));
        }
        return errors;
    }

    public String checkNicknameExisting(String nickname) {
        String error = new String();
        if (dbSqlite.isNicknameExistRequest(nickname)) {
            error = "Данный никнейм уже занят";
        }
        return error;
    }

    public String checkInputNumber(String numberPhone) {
        Pattern p = Pattern.compile("^[-+()0-9]*[^A-Za-zА-ЯА-я]$");
        Matcher m = p.matcher(numberPhone);
        String error = new String();
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputNumber: false");
            error = "Номер телефона заполнен неккоректно";
        }
        return error;
    }

    public String checkInputBirth(Date date) {
        String error = new String();
        Date current_date = new Date();
        Date min_date = new Date();
        min_date.setYear(min_date.getYear() - 150);
        if (!date.before(current_date) && !date.after(min_date)) {
            log.log(Level.WARNING, "checkInputBirth: false");
            error = "Дата заполнена неккоректно";
        }
        return error;
    }

    public String checkInputMail(String email) {
        String error = new String();
        Pattern p = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,4}$");
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputMail: false");
            error = "Почта заполнена неккоректно";
        }
        return error;
    }

    public String checkInputVk(String vk) {
        String error = new String();
        Pattern p = Pattern.compile("^https://vk\\.com/.*");
        Matcher m = p.matcher(vk);
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputVk: false");
            error = "Ссылка vk заполнена неккоректно";
        }
        return error;
    }

    public List<String> checkNonEmptyData(String name, String phoneNumber, Date birthday, String mail,
                                          String vk, String about, String group, String hobby_name,
                                          String hobby_content, String education, String password,
                                          String nickname) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) errors.add("Имя не указано");
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) errors.add("Номер телефона не указан");
        if (birthday.toString() == null || birthday.toString().isEmpty()) errors.add("Дата рождения не указана");
        if (mail == null || mail.trim().isEmpty()) errors.add("Почта не указана");
        if (vk == null || vk.trim().isEmpty()) errors.add("Ссылка на vk не указана");
        if (about == null || about.trim().isEmpty()) errors.add("Информация о себе не заполнена");
        if (group == null || group.trim().isEmpty()) errors.add("Номер группы не указан");
        if (hobby_name == null || hobby_name.trim().isEmpty()) errors.add("Название хобби не указано");
        if (hobby_content == null || hobby_content.trim().isEmpty()) errors.add("Информация о хобби не заполнена");
        if (education == null || education.trim().isEmpty()) errors.add("Информация об образовании не заполнена");
        if (password == null || password.trim().isEmpty()) errors.add("Пароль не заполнен");
        if (nickname == null || nickname.trim().isEmpty()) errors.add("Никнейм не указан");
        return errors;
    }
}