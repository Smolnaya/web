function checkData() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/is/my/user");
    xhr.setRequestHeader("Content-type", "application/json");

    var user_name = document.getElementById("input_user_name").value;
    var user_password = document.getElementById("input_user_password").value;
    var params = {
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