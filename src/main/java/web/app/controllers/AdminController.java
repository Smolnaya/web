package web.app.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import web.app.api.request.UserRequest;
import web.app.dao.DbSqlite;
import web.app.dao.model.User;

@RestController
@RequestMapping(value = "/api/admin/")
public class AdminController {
    private final DbSqlite dbSqlite;

    public AdminController(DbSqlite dbSqlite) {
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
    public ResponseEntity<User> selectNextUser(@RequestBody UserRequest id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectNextUser(id.getId()), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Предыдущий пользователь")
    @RequestMapping(value = "select/previous/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> selectPreviousUser(@RequestBody UserRequest id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.selectPreviousUser(id.getId()), headers, HttpStatus.OK);
    }
}
