package web.app.controllers;

import web.app.api.request.UserByIdRequest;
import web.app.api.request.UserByNameRequest;
import web.app.api.request.UserByPasswordRequest;
import web.app.dao.DbSqlite;
import web.app.dao.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.app.services.CheckDataService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class UserController {
    private final DbSqlite dbSqlite;

    public UserController(DbSqlite dbSqlite) {
        this.dbSqlite = dbSqlite;
    }

    @ApiOperation(value = "Первый пользователь")
    @RequestMapping(value = "select/first/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectFirstUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectFirstUser(), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Следующий пользователь")
    @RequestMapping(value = "select/next/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectNextUser(@RequestBody UserByIdRequest id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectNextUser(id.getId()), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Предыдущий пользователь")
    @RequestMapping(value = "select/previous/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectPreviousUser(@RequestBody UserByIdRequest id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectPreviousUser(id.getId()), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Добавить пользователя")
    @RequestMapping(value = "insert/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> checkUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CheckDataService checkDataService = new CheckDataService();
        List<String> errors = new ArrayList<>(checkDataService.checkInputData(user));
        if (errors.isEmpty()) {
            return new ResponseEntity<>(dbSqlite.insertUser(user), headers, HttpStatus.OK);
        } else return new ResponseEntity<>(errors, headers, HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Есть ли данное имя пользователя в бд")
    @RequestMapping(value = "is/name/exist", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> nameExist(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CheckDataService checkDataService = new CheckDataService();
        String error = checkDataService.checkNameExisting(name);
        String noError = "Такого имени еще нет";
        if (error.isEmpty()) {
            return new ResponseEntity<>(noError, headers, HttpStatus.OK);
        } else return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Зарегистрирован ли пользователь")
    @RequestMapping(value = "is/my/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> isMyUser(@RequestBody UserByPasswordRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CheckDataService checkDataService = new CheckDataService();
        String error = checkDataService.checkNameExisting(user.getName());
        if (!error.isEmpty()) {
            if (dbSqlite.isMyUser(user.getName(), user.getPassword())) {
                return new ResponseEntity<>("Пользователь найден", headers, HttpStatus.OK);
            } else return new ResponseEntity<>("Неверный пароль", headers, HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Пользователь не найден", headers, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Пользователь")
    @RequestMapping(value = "select/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectUser(@RequestBody UserByNameRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectUser(user.getName()), headers, HttpStatus.OK);
    }
}