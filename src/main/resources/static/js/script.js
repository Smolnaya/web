/*Добавить пользователя в базу*/
function insertUser() {
    if (checkNonEmptyData() && checkInputNumber() && checkInputBirth() && checkInputMail() && checkInputVk()) {
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
        document.getElementById("forma").submit();
    } else {
        if (!checkNonEmptyData()) {
            alert("Не все поля заполнены :(")
        }
        if (!checkInputNumber()) {
            alert("Неккоректный номер :( \nВведите номер без букв")
        }
        if (!checkInputBirth()) {
            alert("Неккоректная дата рождения :(")
        }
        if (!checkInputMail()) {
            alert("Неккоректная почта :( \nВведите почту в формате: somebody@domen.ru")
        }
        if (!checkInputVk()) {
            alert("Неккоректная ссылка VK :( \nВведите ссылку в формате: https://vk.com/somebody")
        }
    }
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

function checkInputNumber() {
    var numberRegex = new RegExp("^[-+()0-9]*[^A-Za-zА-ЯА-я]$");
    var user_number = document.getElementById("input_user_number").value;
    if (numberRegex.test(user_number)) {
        return true;
    }
}

function checkInputBirth() {
    var input_birth = document.getElementById("input_user_birthday").value.split('-');
    var input_date = new Date(input_birth[0], input_birth[1] - 1, input_birth[2]);
    var current_date = new Date();
    var min_date = new Date();
    min_date.setFullYear(min_date.getFullYear() - 150);
    if (input_date < current_date && input_date > min_date) {
        return true;
    }
}

function checkInputMail() {
    var mailRegex = new RegExp("^[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,4}$");
    var user_mail = document.getElementById("input_user_mail").value;
    if (mailRegex.test(user_mail)) {
        return true;
    }
}

function checkInputVk() {
    var vkRegex = new RegExp("^https:\/\/vk\.com\/.*");
    var user_vk = document.getElementById("input_user_vk").value;
    if (vkRegex.test(user_vk)) {
        return true;
    }
}

function checkNonEmptyData() {
    if (document.getElementById("input_user_name").value.trim() != "" ||
        document.getElementById("input_user_about").value.trim() != "" ||
        document.getElementById("input_user_group").value.trim() != "" ||
        document.getElementById("input_user_hobby_name").value.trim() != "" ||
        document.getElementById("input_user_hobby_content").value.trim() != "") {
        return true;
    }
}
