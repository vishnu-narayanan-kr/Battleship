import { baseURL } from "../../config.js";

// JavaScript source code
window.showSettings = () => {
    document.getElementById("settingsPage").style.display = "inline-block";
}

window.closePopUpsMenus = () => {
    document.getElementById("settingsPage").style.display = "none";
}

window.onPlayOnline = () => {
    // authentication will be done at a later phase

    const username = document.getElementById("username").value;
    console.log({ username });

    window.location.replace(baseURL.replace("Landing", "Placement") + "?username=" + username);
}