import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

export const getAllRooms = () => {
  return axios.get(`${API_BASE_URL}/chatrooms`);
};

export const getRoomsForUser = (userId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/joined/${userId}`);
};

export const createRoom = (data) => {
  return axios.post(`${API_BASE_URL}/chatrooms`, data);
};

export const joinRoom = (roomId, userId) => {
  return axios.post(`${API_BASE_URL}/chatrooms/${roomId}/join/${userId}`);
};

export const getRoomById = (roomId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}`);
};