function checkData() {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "api/is/my/user");
    xhr.setRequestHeader("Content-type", "application/json");

    let user_name = document.getElementById("input_user_name").value;
    let user_password = document.getElementById("input_user_password").value;
    let params = {
        "name": user_name,
        "password": user_password
    };
    xhr.send(JSON.stringify(params));

    xhr.onload = (e) => {
        if (xhr.status === 200) {
            document.location.href = "user.html?name=" + user_name;
        } else if (xhr.status === 400) {
            alert("Неверный пароль");
        } else if (xhr.status === 404) {
            alert("Пользователь не найден");
        }
    }
}