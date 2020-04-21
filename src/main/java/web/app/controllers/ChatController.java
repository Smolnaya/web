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
import web.app.api.request.MessageRequest;
import web.app.dao.DbSqlite;
import web.app.dao.model.Message;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/chat/")
public class ChatController {
    private final DbSqlite dbSqlite;
    public ChatController(DbSqlite dbSqlite) {
        this.dbSqlite = dbSqlite;
    }

    @ApiOperation(value = "Добавить сообщение")
    @RequestMapping(value = "insert/message", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> insertMessage(@RequestBody Message message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(dbSqlite.insertMessage(message), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить все сообщения от")
    @RequestMapping(value = "select/all/messages", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> selectAllMessages(@RequestBody MessageRequest messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Message> messageList = dbSqlite.selectAllMessages(messageRequest.getSender(), messageRequest.getRecipient());
        if (!messageList.isEmpty()) return new ResponseEntity<>(messageList, headers, HttpStatus.OK);
        else return new ResponseEntity<>(new ArrayList<>(), headers, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Найти чаты")
    @RequestMapping(value = "select/chats", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> selectChats(@RequestBody MessageRequest messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> chatList = dbSqlite.selectChats(messageRequest.getSender());
        if (!chatList.isEmpty()) return new ResponseEntity<>(chatList, headers, HttpStatus.OK);
        else return new ResponseEntity<>(new ArrayList<>(), headers, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Есть ли новые сообщения")
    @RequestMapping(value = "check/new/messages", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkNewMessages(@RequestBody MessageRequest messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (dbSqlite.isNewMessage(messageRequest.getSender(), messageRequest.getRecipient(), messageRequest.getMessagesAmount()))
            return new ResponseEntity<>(true, headers, HttpStatus.OK);
        else return new ResponseEntity<>(false, headers, HttpStatus.NO_CONTENT);
    }
}
