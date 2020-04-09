package web.app.services;

import jdk.nashorn.internal.runtime.regexp.RegExp;
import web.app.dao.model.User;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDataService {
    private Logger log = Logger.getLogger(getClass().getName());
    public CheckDataService() {
    }

    public boolean checkInputData(User user) {
        if (checkInputNumber(user.getNumberPhone()) && checkInputBirth(user.getBirthday())
            && checkInputMail(user.getElMail()) && checkInputVk(user.getVk())
            && checkNonEmptyData(user.getNickname(), user.getAboutInf(), user.getStudyGroup(), user.getHobbyName(), user.getHobbyContent())) {
            return true;
        } else return false;
    }

    public boolean checkInputNumber(String numberPhone) {
        Pattern p = Pattern.compile("^[-+()0-9]*[^A-Za-zА-ЯА-я]$");
        Matcher m = p.matcher(numberPhone);
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputNumber: false");
        }
        return m.matches();
    }

    public boolean checkInputBirth(Date date) {
        Date current_date = new Date();
        Date min_date = new Date();
        min_date.setYear(min_date.getYear() - 150);
        if (date.before(current_date) && date.after(min_date)) return true;
        else {
            log.log(Level.WARNING, "checkInputBirth: false");
            return false;
        }
    }

    public boolean checkInputMail (String email) {
        Pattern p = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,4}$");
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputMail: false");
        }
        return m.matches();
    }

    public boolean checkInputVk(String vk) {
        Pattern p = Pattern.compile("^https://vk.com/.*");
        Matcher m = p.matcher(vk);
        if (!m.matches()) {
            log.log(Level.WARNING, "checkInputVk: false");
        }
        return m.matches();
    }

    public boolean checkNonEmptyData(String name, String about, String group, String hobby_name, String hobby_content) {
        if (name.trim() != "" && about.trim() != "" && group.trim() != "" && hobby_name.trim() != ""
                && hobby_content.trim() != "") return true;
        else {
            log.log(Level.WARNING, "checkNonEmptyData: false");
            return false;
        }
    }
}
