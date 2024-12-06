const env = ["dev-local", "dev-web"][1];

export let baseURL;

if (env === "dev-local") {
    baseURL = "http://" + location.hostname + ":5500";
} else if (env === "dev-web") {
    baseURL = location.href;
}
