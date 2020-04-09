package web.app.api.request;

import java.util.ArrayList;
import java.util.List;
/*
Список всех ID в БД
 */
public class UsersIdRequest {
    private List<Integer> listId;

    public UsersIdRequest() {
        this.listId = new ArrayList<>();
    }

    public List<Integer> getListId() {
        return listId;
    }

    public void setListId(List<Integer> listId) {
        this.listId = listId;
    }
}