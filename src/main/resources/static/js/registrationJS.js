/*Добавить пользователя в БД*/
function insertUser() {
    if (checkNonEmptyData() && checkInputNumber() && checkInputBirth() && checkInputMail() && checkInputVk()) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/insert/user");
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
        var selectedIndex = document.getElementById("input_user_gender").selectedIndex;
        var user_gender = document.getElementById("input_user_gender").options[selectedIndex].value;
        var user_education = getCheckedCheckBoxes();
        var user_password = document.getElementById("input_user_password").value;
        var user_role = "user";
        var params = {
            "nickname": user_name,
            "numberPhone": user_phone,
            "birthday": user_birthday,
            "elMail": user_mail,
            "vk": user_vk,
            "aboutInf": user_about,
            "studyGroup": user_group,
            "hobbyName": user_hobby_name,
            "hobbyContent": user_hobby_content,
            "gender": user_gender,
            "education": user_education,
            "password": user_password,
            "role": user_role
        };
        xhr.send(JSON.stringify(params));
        xhr.onload = (e) => {
            if (xhr.status === 200) {
                alert("Вы зарегистрированы!");
                document.location.href = "user.html?name=" + user_name;
            } else if (xhr.status === 400) {
                alert("Не верные данные" + e.target.response);
            } else {
                alert("Повторите попытку");
            }
        }
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

/*
Получить выбранные checkBoxes
 */
function getCheckedCheckBoxes() {
    var checkboxes = document.getElementsByClassName('checkbox');
    var checkboxesChecked = "";
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            if (checkboxesChecked !== "") {
                checkboxesChecked += ", ";
            }
            checkboxesChecked += checkboxes[i].value;
        }
    }
    return checkboxesChecked;
}

/*
Проверка корректного номера
 */
function checkInputNumber() {
    var numberRegex = new RegExp("^[-+()0-9]*[^A-Za-zА-ЯА-я]$");
    var user_number = document.getElementById("input_user_number").value;
    if (numberRegex.test(user_number)) {
        return true;
    }
}

/*
Проверка корректной даты рождения
 */
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

/*
Проверка корректной почты
 */
function checkInputMail() {
    var mailRegex = new RegExp("^[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,4}$");
    var user_mail = document.getElementById("input_user_mail").value;
    if (mailRegex.test(user_mail)) {
        return true;
    }
}

/*
Проверка корректной ссылки на VK
 */
function checkInputVk() {
    var vkRegex = new RegExp("^https:\/\/vk\.com\/.*");
    var user_vk = document.getElementById("input_user_vk").value;
    if (vkRegex.test(user_vk)) {
        return true;
    }
}


/*
Проверка полей на заполненность
 */
function checkNonEmptyData() {
    if (document.getElementById("input_user_name").value.trim() !== "" &&
        document.getElementById("input_user_about").value.trim() !== "" &&
        document.getElementById("input_user_group").value.trim() !== "" &&
        document.getElementById("input_user_hobby_name").value.trim() !== "" &&
        document.querySelectorAll("input[type=checkbox]:checked").length !== 0 &&
        document.getElementById("input_user_password").value.trim() !== "" &&
        document.getElementById("input_user_hobby_content").value.trim() !== "") {
        return true;
    }
}