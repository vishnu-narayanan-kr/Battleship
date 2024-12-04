// JavaScript source code
function showSettings() {
    document.getElementById("settingsPage").style.display = "inline-block";
}

function closePopUpsMenus() {
    document.getElementById("settingsPage").style.display = "none";
}

const onPlayOnline = () => {
    // authentication will be done at a later phase

    const username = document.getElementById("username").value;
    console.log({ username });

    window.location.replace("http://" + location.hostname + ":5500/UI/Placement/index.html?username=" + username);
}