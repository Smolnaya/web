package web.app.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.app.api.request.MessageRequest;
import web.app.api.request.UserRequest;
import web.app.dao.DbSqlite;
import web.app.dao.model.Message;
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
@RequestMapping(value = "/api/user/")
public class UserController {
    private final DbSqlite dbSqlite;
    private final CheckDataService checkDataService;
    private final PasswordEncoder passwordEncoder;

    public UserController(DbSqlite dbSqlite, CheckDataService checkDataService, PasswordEncoder passwordEncoder) {
        this.dbSqlite = dbSqlite;
        this.passwordEncoder = passwordEncoder;
        this.checkDataService = checkDataService;
    }

    @ApiOperation(value = "Добавить пользователя")
    @RequestMapping(value = "insert/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> checkUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String passwordEncode = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncode);
        user.setNickname(user.getNickname().toLowerCase());
        List<String> errors = new ArrayList<>(checkDataService.checkInputData(user));
        if (errors.isEmpty()) {
            return new ResponseEntity<>(dbSqlite.insertUser(user), headers, HttpStatus.OK);
        } else return new ResponseEntity<>(errors, headers, HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Данные пользователя")
    @RequestMapping(value = "select/user/data", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectUser() {
        String nickname = SecurityContextHolder.getContext().getAuthentication().getName();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectUserData(nickname), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Найти имя пользователя")
    @RequestMapping(value = "find/user/name", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> selectUserName(@RequestBody UserRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Boolean resp = dbSqlite.isNicknameExistRequest(user.getNickname());
        if (resp) return new ResponseEntity<>(resp, headers, HttpStatus.OK);
        else return new ResponseEntity<>(resp, headers, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Никнейм не занят?")
    @RequestMapping(value = "is/nickname/exist", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> nicknameExist(@RequestBody UserRequest nickname) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String error = checkDataService.checkNicknameExisting(nickname.getNickname());
        if (error.isEmpty()) {
            return new ResponseEntity<>(true, headers, HttpStatus.OK);
        } else return new ResponseEntity<>(false, headers, HttpStatus.BAD_REQUEST);
    }
}