package com.web.app.controllers;

import com.web.app.api.request.UserByIdRequest;
import com.web.app.dao.DbSqlite;
import com.web.app.dao.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/select/user")
public class UserController {
    private final DbSqlite dbSqlite;

    public UserController(DbSqlite dbSqlite) {
        this.dbSqlite = dbSqlite;
    }

    @ApiOperation(value = "Выборка пользователя по id")
    @RequestMapping(value = "by/id", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectUserById(@RequestBody UserByIdRequest id) {
        User user = dbSqlite.selectUserById(id.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
    @ApiOperation(value = "Добавить пользователя")
    @RequestMapping(value = "insert/db", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> insertUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.insertUser(user), headers, HttpStatus.OK);
    }
}
