window.onload = function selectUser() {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/user");
    xhr.setRequestHeader("Content-type", "application/json");
    let user = decodeURI(location.search.substr(1).split('&')[0].split('=')[1]);
    let params = {"name": user};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        let user = JSON.parse(e.target.response);
        document.getElementById("user_id").textContent = user.id;
        document.getElementById("user_name").textContent = user.nickname;
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
    };
    setInterval(getChats, 5000, user);
};

function getChats(user) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/chats");
    xhr.setRequestHeader("Content-type", "application/json");
    let params = {"sender": user};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        let chats = [];
        chats = JSON.parse(e.target.response);
        for (let i = 0; i < chats.length; i++) {
            if (checkUserName(chats[i]).length === 0 ) {
                createChat(chats[i]);
            }
        }
    }
}

function findUser() {
    let recipient = document.getElementById("input_recipient").value;
    let errors = checkUserName(recipient);
    if (errors.length === 0) {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "api/find/user/name");
        xhr.setRequestHeader("Content-type", "application/json");
        let params = {"name": recipient};
        xhr.send(JSON.stringify(params));
        xhr.onload = (e) => {
            if (xhr.status === 200) {
                document.getElementById("input_recipient").value = "";
                createChat(recipient);
            } else if (xhr.status === 404) {
                alert("Пользователь не найден");
            }
        };
    } else alert(errors);
}

function createChat(recipient) {
    let tab = document.getElementById("tab");
    let buttons = document.getElementById("buttons");
    let tablink = document.createElement("button");
    tablink.setAttribute("class", "tablinks");
    tablink.setAttribute("onclick", "openChat(event, '" + recipient + "')");
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
    inputButton.setAttribute("onclick", "send('" + recipient + "')");
    tabcontent.append(inputButton);

}

function openChat(evt, recipient) {
    let tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for ( let i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (let i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(recipient).style.display = "block";
    evt.currentTarget.className += " active";

    let username = document.getElementById("user_name").textContent;
    setInterval(getMessages, 2000, recipient, username);
}

function update(recipient, user, messagesAmount) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/check/new/messages");
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

function getMessages(recipient, user) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/all/messages");
    xhr.setRequestHeader("Content-type", "application/json");
    let params = {
        "sender": user,
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
                if (messages[i].sender === user) {
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

function send(recipient) {
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
        xhr.open("POST", "api/insert/message");
        xhr.setRequestHeader("Content-type", "application/json");
        let params = {
            "sender": document.getElementById("user_name").textContent,
            "recipient": recipient,
            "message": message,
            "sendingTime": new Date()
        };
        xhr.send(JSON.stringify(params));
    }
}

function checkUserName(recipient) {
    let errors = [];
    if (document.getElementById(recipient) !== null) {
        errors.push("У вас уже есть чат с этим пользователем");
    }
    if (document.getElementById("user_name").textContent === recipient) {
        errors.push("Нельзя переписываться с самим собой :(")
    }
    return errors;
}