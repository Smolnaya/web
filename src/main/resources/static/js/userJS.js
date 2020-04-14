window.onload = function selectUser() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/select/user");
    xhr.setRequestHeader("Content-type", "application/json");
    var name = decodeURI(location.search.substr(1).split('&')[0].split('=')[1]);
    var params = {"name": name};
    xhr.send(JSON.stringify(params));
    xhr.onload = (e) => {
        var user = JSON.parse(e.target.response);
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
};