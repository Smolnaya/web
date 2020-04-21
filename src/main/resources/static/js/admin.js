function init() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/admin/select/first/user");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.onload = (e) => {
        var user = JSON.parse(e.target.response);
        document.getElementById("user_id").textContent = user.id;
        document.getElementById("user_name").textContent = user.name;
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
        document.getElementById("user_role").textContent = user.role;
        document.getElementById("user_nickname").textContent = user.nickname;
    };
    xhr.send();
}

/*
Следующий пользователь
 */
function getNextUser() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/admin/select/next/user");
    xhr.setRequestHeader("Content-type", "application/json");
    var user_id = document.getElementById("user_id").textContent;
    var params = {"id": user_id};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        var user = JSON.parse(e.target.response);
        if (user.id != null) {
            document.getElementById("user_id").textContent = user.id;
            document.getElementById("user_name").textContent = user.name;
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
            document.getElementById("user_role").textContent = user.role;
            document.getElementById("user_nickname").textContent = user.nickname;
        } else {
            alert("Конец списка пользователей");
        }
    };
}

/*
Предыдущий пользователь
 */
function getPreviousUser() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/admin/select/previous/user");
    xhr.setRequestHeader("Content-type", "application/json");
    var user_id = document.getElementById("user_id").textContent;
    var params = {"id": user_id};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        var user = JSON.parse(e.target.response);
        if (user.id != null) {
            document.getElementById("user_id").textContent = user.id;
            document.getElementById("user_name").textContent = user.name;
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
            document.getElementById("user_role").textContent = user.role;
            document.getElementById("user_nickname").textContent = user.nickname;
        } else {
            alert("Начало списка пользователей");
        }
    };
}