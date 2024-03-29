document.addEventListener("DOMContentLoaded", function (event) {
    const original = document.querySelector(".string"); // Get original text
    const translation = document.querySelector(".translation"); // Translation input
    const key = document.querySelector(".string-key").innerHTML; // Get key of translation

    // Adjust input size box
    let height = original.innerHTML.length * 3;
    console.log(height);
    if (height < 50) height = 50;
    console.log(height);
    console.log("-----");
    translation.style.height = height + "px";

    translation.focus();
    translation.select();

    const isCommand = window.location.pathname.split("/")[2] === "commands"
    const isCommandName = key.split(".")[key.split(".").length - 1] === "name"
    // If user edits a command translation
    if (isCommand && isCommandName) {
        translation.addEventListener('keypress', e => {
            if (e.keyCode === 32) e.preventDefault();  // Prevent spaces in command names
        });
    }

    document.querySelector('.back').addEventListener("click", e => {
        const translation = document.querySelector(".translation"); // Translation input
        const key = document.querySelector(".string-key").innerHTML; // Get key of translation
        const platform = window.location.pathname.split("/")[2];
        const language = window.location.pathname.split("/")[3];
        const url = window.location.origin + "/api/edit";

        const json = {
            code: "7YHTQNzmyC3fj8IcwJwj",
            platform: platform,
            language: language,
            key: key,
            value: translation.value,
        };

        fetch(url, {
            method: "POST",
            body: JSON.stringify(json),
        }).then((response) => response.json())
            .then((data) => {
                document.location = `/translate/${platform}/${language}`
            });
    })

    document.querySelector('.next').addEventListener("click", e => nextTranslation())

    const map = {}; // Map holding state which keys are down
    onkeydown = onkeyup = function (e) {
        map[e.code] = e.type === 'keydown';

        if (map["ControlLeft"] && map["Space"]) {
            nextTranslation();
        }
    }

});

function nextTranslation() {
    const translation = document.querySelector(".translation"); // Translation input
    const key = document.querySelector(".string-key").innerHTML; // Get key of translation
    const platform = window.location.pathname.split("/")[2];
    const language = window.location.pathname.split("/")[3];
    const url = window.location.origin + "/api/edit";

    const json = {
        code: "7YHTQNzmyC3fj8IcwJwj",
        platform: platform,
        language: language,
        key: key,
        value: translation.value,
    };

    fetch(url, {
        method: "POST",
        body: JSON.stringify(json),
    }).then((response) => response.json())
        .then((data) => {
            if (data.next === "null") document.location = `/translate/${platform}/${language}`
            else document.location = `/translate/${platform}/${language}/editor?key=${data.next}`
        });
}