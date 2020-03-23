/*Добавить пользователя в базу*/
function insertUser() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/user/insert/db");
    xhr.setRequestHeader("Content-type", "application/json");
    var user_name = document.getElementById("input_user_name").value;
    var user_birthday = document.getElementById("input_user_birthday").value;
    var user_phone = document.getElementById("input_user_number").value;
    var user_mail = document.getElementById("input_user_mail").value;
    var user_vk = document.getElementById("input_user_vk").value;
    var user_about = document.getElementById("input_user_about").value;
    var user_group = document.getElementById("input_user_group").value;
    var user_hobby_name = document.getElementById("input_user_hobby_name").value;
    var user_hobby_content = document.getElementById("input_user_hobby_content").value;
    var params = {
        "nickname": user_name,
        "numberPhone": user_phone,
        "birthday": user_birthday,
        "elMail": user_mail,
        "vk": user_vk,
        "aboutInf": user_about,
        "studyGroup": user_group,
        "hobbyName": user_hobby_name,
        "hobbyContent": user_hobby_content
    };
    xhr.send(JSON.stringify(params));
    alert("Вы зарегистрированы!");
}

/*Найти пользователя в базе по ID*/
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/user/by/id");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.onload = (e) => {
        var userData = JSON.parse(e.target.response),
            userDataName = userData.nickname,
            userDataNumber = userData.numberPhone,
            userDataBirth = userData.birthday,
            userDataMail = userData.elMail,
            userDataVk = userData.vk,
            userDataAbout = userData.aboutInf,
            userDataGroup = userData.studyGroup,
            userDataHobbyName = userData.hobbyName,
            userDataGroupHobbyContent = userData.hobbyContent;
        document.getElementById("user_name").textContent = userDataName;
        document.getElementById("user_phone").textContent = userDataNumber;
        document.getElementById("user_birth").textContent = userDataBirth;
        document.getElementById("user_mail").textContent = userDataMail;
        document.getElementById("user_vk").textContent = userDataVk;
        document.getElementById("user_about").textContent = userDataAbout;
        document.getElementById("user_group").textContent = userDataGroup;
        document.getElementById("user_hobby_name").textContent = userDataHobbyName;
        document.getElementById("user_hobby_content").textContent = userDataGroupHobbyContent;
    };
    var user_id = document.getElementById("input_user_id").value;
    var params = {"id": user_id};
    xhr.send(JSON.stringify(params));
}

/*для serverData.html*/
function getUserLight() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/user/by/id");
    xhr.setRequestHeader("Content-type", "application/json");

    xhr.onload = (e) => {
        var userData = JSON.parse(e.target.response);
        var userDataName = userData.nickname;
        var userDataNumber = userData.numberPhone;
        var userDataBirth = userData.birthday;
        document.getElementById("user_name").textContent = userDataName;
        document.getElementById("user_phone").textContent = userDataNumber;
        document.getElementById("user_birth").textContent = userDataBirth;
        alert("userDataName: " + e.target.response.nickname
            + "\nuserDataNumber: " + e.target.response.numberPhone
            + "\nuserDataBirth: " + e.target.response.birthday
            + "\ne.target.response: " + e.target.response);
    };
    var user_id = document.getElementById("input_user_id").value;
    var params = {"id": user_id};
    xhr.send(JSON.stringify(params));
}
