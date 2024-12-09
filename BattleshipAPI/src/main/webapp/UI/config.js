const env = ["dev-local", "dev-web"][0];

export let baseURL;
export let baseURLApi;

if (env === "dev-local") {
    baseURL = "http://" + location.hostname + ":5500";
    baseURLApi = "http://" + location.hostname + ":8080";
} else if (env === "dev-web") {
    baseURL = location.href;
    baseURLApi = "http://" + location.hostname + ":8080"; // should be ngrok link
}


