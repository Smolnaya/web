package web.app.controllers;

import web.app.api.request.UserByIdRequest;
import web.app.api.request.UsersIdRequest;
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

@RestController
@RequestMapping(value = "/api/select/user")
public class UserController {
    private final DbSqlite dbSqlite;

    public UserController(DbSqlite dbSqlite) {
        this.dbSqlite = dbSqlite;
    }

    @ApiOperation(value = "Получить все ID в базе данных")
    @RequestMapping(value = "get/id", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersIdRequest> getUsersId() {
        UsersIdRequest listId = dbSqlite.getUsersId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(listId, headers, HttpStatus.OK);
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = User.class),
            @ApiResponse(code = 400, message = "Ошибка входных данных")})
    @RequestMapping(value = "insert/db", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CheckDataService checkDataService = new CheckDataService();
        if (checkDataService.checkInputData(user)) {
            return new ResponseEntity<>(dbSqlite.insertUser(user), headers, HttpStatus.OK);
        } else return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }
}