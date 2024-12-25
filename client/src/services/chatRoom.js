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
  return axios.post(`${API_BASE_URL}/chatrooms/${roomId}/request/${userId}`);
};

export const getRoomById = (roomId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}`);
};

export const uploadFile = (roomId, file) => {
  const formData = new FormData();
  formData.append("file", file);

  return axios.post(`${API_BASE_URL}/files/upload-to-room/${roomId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const downloadFile = async (fileName) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/files/download/${fileName}`);
    const presignedUrl = response.data;

    const link = document.createElement("a");
    link.href = presignedUrl;
    link.download = fileName; 
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error("Error downloading file:", error);
    throw error;
  }
};

export const isAdmin = async (roomId, userId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}/isAdmin/${userId}`);
}

export const getPendingRoomsForUser = (userId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/pending/${userId}`);
};

export const getAllRequestJoin = (roomId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}/getAllRequestJoin`);
};

export const acceptRequestJoinRoom = (roomId, userId) => {
  return axios.post(`${API_BASE_URL}/chatrooms/${roomId}/acceptRequest/${userId}`);
};

export const declineRequestJoinRoom = (roomId, userId) => {
  return axios.post(`${API_BASE_URL}/chatrooms/${roomId}/declineRequest/${userId}`);
};

export const getAllUsersInRoom = (roomId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}/getAllUser`);
};

export const removeUserFromRoom = (roomId, userId) => {
  return axios.post(`${API_BASE_URL}/chatrooms/${roomId}/remove/${userId}`);
};

export const leaveRoom = (roomId, userId) => {
  return axios.get(`${API_BASE_URL}/chatrooms/${roomId}/leave/${userId}`);
};
