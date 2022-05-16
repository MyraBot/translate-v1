document.addEventListener("DOMContentLoaded", function () {
    let url = new URL(window.location.href);
    let error = url.searchParams.get("error");

    if (error === "invalidLoginData") {
        document.querySelector(".error-invalidLoginData").classList.add("active");
    }

    console.log(error);
}, false);

document.addEventListener('keypress', e => {
    if (e.key === 'Enter') {
        onLogin()
    }
});


function onLogin() {
    let username = document.querySelector("#username").value;
    let password = document.querySelector("#password").value;

    // Save username and password as cookie
    document.cookie = "username=" + username;
    document.cookie = "password=" + password;

    window.location.href = "../languages"; // Redirect to languages tab
}
