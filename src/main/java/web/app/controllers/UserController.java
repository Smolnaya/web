package web.app.controllers;

import web.app.api.request.UserByIdRequest;
import web.app.dao.DbSqlite;
import web.app.dao.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Boolean.class),
            @ApiResponse(code = 400, message = "Ошибка входных данных", response = List.class)})
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
}