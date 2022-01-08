import axios from "axios";

axios.defaults.headers.common['Authorization'] = "Bearer " +  JSON.parse(window.localStorage.getItem("currentUser"))?.token;


export const ImagesApi = axios.create({
    baseURL: "http://localhost:5006/api/Image"
});

export const UsersApi = axios.create({
    baseURL: "https://localhost:5001/api/User"
})
