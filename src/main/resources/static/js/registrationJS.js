/*Добавить пользователя в БД*/
function insertUser() {
    if (checkNonEmptyData() && checkInputNumber() && checkInputBirth() && checkInputMail() && checkInputVk()) {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "api/insert/user");
        xhr.setRequestHeader("Content-type", "application/json");
        let user_name = document.getElementById("input_user_name").value;
        let user_birthday = document.getElementById("input_user_birthday").value;
        let user_phone = document.getElementById("input_user_number").value;
        let user_mail = document.getElementById("input_user_mail").value;
        let user_vk = document.getElementById("input_user_vk").value;
        let user_about = document.getElementById("input_user_about").value;
        let user_group = document.getElementById("input_user_group").value;
        let user_hobby_name = document.getElementById("input_user_hobby_name").value;
        let user_hobby_content = document.getElementById("input_user_hobby_content").value;
        let selectedIndex = document.getElementById("input_user_gender").selectedIndex;
        let user_gender = document.getElementById("input_user_gender").options[selectedIndex].value;
        let user_education = getCheckedCheckBoxes();
        let user_password = document.getElementById("input_user_password").value;
        let user_role = "user";
        let params = {
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
    let checkboxes = document.getElementsByClassName('checkbox');
    let checkboxesChecked = "";
    for (let i = 0; i < checkboxes.length; i++) {
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
    let numberRegex = new RegExp("^[-+()0-9]*[^A-Za-zА-ЯА-я]$");
    let user_number = document.getElementById("input_user_number").value;
    if (numberRegex.test(user_number)) {
        return true;
    }
}

/*
Проверка корректной даты рождения
 */
function checkInputBirth() {
    let input_birth = document.getElementById("input_user_birthday").value.split('-');
    let input_date = new Date(input_birth[0], input_birth[1] - 1, input_birth[2]);
    let current_date = new Date();
    let min_date = new Date();
    min_date.setFullYear(min_date.getFullYear() - 150);
    if (input_date < current_date && input_date > min_date) {
        return true;
    }
}

/*
Проверка корректной почты
 */
function checkInputMail() {
    let mailRegex = new RegExp("^[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,4}$");
    let user_mail = document.getElementById("input_user_mail").value;
    if (mailRegex.test(user_mail)) {
        return true;
    }
}

/*
Проверка корректной ссылки на VK
 */
function checkInputVk() {
    let vkRegex = new RegExp("^https:\/\/vk\.com\/.*");
    let user_vk = document.getElementById("input_user_vk").value;
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