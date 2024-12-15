import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

export const getMessagesForRoom = (roomId) => {
  return axios.get(`${API_BASE_URL}/messages/${roomId}`);
};

export const sendMessage = (data) => {
  return axios.post(`${API_BASE_URL}/messages`, data);
};
