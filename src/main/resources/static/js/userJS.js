window.onload = function selectUserData() {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/user/select/user/data");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
    let nickname = "";
    xhr.onload = (e) => {
        let user = JSON.parse(e.target.response);
        document.getElementById("user_name").textContent = user.name;
        document.getElementById("user_nickname").textContent = user.nickname;
        document.getElementById("user_phone").textContent = user.numberPhone;
        document.getElementById("user_birth").textContent = user.birthday;
        document.getElementById("user_mail").textContent = user.elMail;
        document.getElementById("user_vk").href = user.vk;
        document.getElementById("user_about").textContent = user.aboutInf;
        document.getElementById("user_group").textContent = user.studyGroup;
        document.getElementById("user_hobby_name").textContent = user.hobbyName;
        document.getElementById("user_hobby_content").textContent = user.hobbyContent;
        document.getElementById("user_gender").textContent = user.gender;
        document.getElementById("user_education").textContent = user.education;
        setInterval(getChats, 5000, user.nickname);
    };

};

function getChats(nickname) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/chat/select/chats");
    xhr.setRequestHeader("Content-type", "application/json");
    let params = {"sender": nickname};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        let chats = [];
        chats = JSON.parse(e.target.response);
        for (let i = 0; i < chats.length; i++) {
            if (checkUserName(chats[i], nickname).length === 0) {
                createChat(chats[i], nickname);
            }
        }
    }
}

function findUser() {
    let recipient = document.getElementById("input_recipient").value;
    let nickname = document.getElementById("user_nickname").textContent;
    let errors = checkUserName(recipient, nickname);
    if (errors.length === 0) {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "api/user/find/user/name");
        xhr.setRequestHeader("Content-type", "application/json");
        let params = {"nickname": recipient};
        xhr.send(JSON.stringify(params));
        xhr.onload = (e) => {
            if (xhr.status === 200) {
                document.getElementById("input_recipient").value = "";
                createChat(recipient, nickname);
            } else if (xhr.status === 404) {
                alert("Пользователь не найден");
            }
        };
    } else alert(errors);
}

function createChat(recipient, nickname) {
    let tab = document.getElementById("tab");
    let buttons = document.getElementById("buttons");
    let tablink = document.createElement("button");
    tablink.setAttribute("class", "tablinks");
    tablink.setAttribute("onclick", "openChat(event, '" + recipient + "','" + nickname +"')");
    tablink.innerHTML = recipient;
    buttons.append(tablink);

    let tabcontent = document.createElement("div");
    tabcontent.setAttribute("class", "tabcontent");
    tabcontent.setAttribute("id", recipient);
    tabcontent.innerHTML = "<h3>" + recipient + "</h3>";
    tab.append(tabcontent);

    let dialog = document.createElement("div");
    dialog.setAttribute("class", "dialog");
    dialog.setAttribute("id", "dialog_" + recipient);
    dialog.style.overflowY = "scroll";
    tabcontent.append(dialog);


    let inputText = document.createElement("input");
    inputText.setAttribute("class", "sending");
    inputText.setAttribute("type", "text");
    inputText.setAttribute("id", "send_message_" + recipient);
    tabcontent.append(inputText);

    let inputButton = document.createElement("input");
    inputButton.setAttribute("class", "sending");
    inputButton.setAttribute("type", "button");
    inputButton.setAttribute("value", "Отправить");
    inputButton.setAttribute("onclick", "send('" + recipient + "','" + nickname +"')");
    tabcontent.append(inputButton);
}

function openChat(evt, recipient, nickname) {
    let tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (let i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (let i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(recipient).style.display = "block";
    evt.currentTarget.className += " active";

    setInterval(getMessages, 2000, recipient, nickname);
}

function update(recipient, user, messagesAmount) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/chat/check/new/messages");
    xhr.setRequestHeader("Content-type", "application/json");
    let params = {
        "sender": user,
        "recipient": recipient,
        "messagesAmount": messagesAmount
    };
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        if (xhr.status === 200) {
            getMessages(recipient, user);
        }
    }
}

function getMessages(recipient, nickname) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/chat/select/all/messages");
    xhr.setRequestHeader("Content-type", "application/json");
    let params = {
        "sender": nickname,
        "recipient": recipient
    };
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        if (xhr.status === 200) {
            let messages = [];
            messages = JSON.parse(e.target.response);
            let dialog = document.getElementById("dialog_" + recipient);
            dialog.innerHTML = "";
            for (let i = 0; i < messages.length; i++) {
                let p = document.createElement("p");
                if (messages[i].sender === nickname) {
                    p.setAttribute("class", "sending_message");
                    p.setAttribute("align", "right");
                    p.innerHTML = messages[i].message;
                    dialog.append(p);
                } else if (messages[i].sender === recipient) {
                    p.setAttribute("class", "incoming_message");
                    p.setAttribute("align", "left");
                    p.innerHTML = messages[i].message;
                    dialog.append(p);
                }
            }
            dialog.scrollTop = dialog.scrollHeight;
        }
    }
}

function send(recipient, nickname) {
    let message = document.getElementById("send_message_" + recipient).value;
    if (message !== "") {
        let dialog = document.getElementById("dialog_" + recipient);
        let p = document.createElement("p");
        p.setAttribute("class", "sending_message");
        p.setAttribute("align", "right");
        p.innerHTML = message;
        document.getElementById("send_message_" + recipient).value = "";
        dialog.append(p);
        dialog.scrollTop = dialog.scrollHeight;

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "api/chat/insert/message");
        xhr.setRequestHeader("Content-type", "application/json");
        let params = {
            "sender": nickname,
            "recipient": recipient,
            "message": message,
            "sendingTime": new Date()
        };
        xhr.send(JSON.stringify(params));
    }
}

function checkUserName(recipient, nickname) {
    let errors = [];
    if (document.getElementById(recipient) !== null) {
        errors.push("У вас уже есть чат с этим пользователем");
    }
    if (nickname === recipient) {
        errors.push("Нельзя переписываться с самим собой :(")
    }
    return errors;
}