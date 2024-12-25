import React, { useEffect, useState, useContext } from "react";
import { getMessagesForRoom } from "../services/message";
import { downloadFile, getRoomById, uploadFile, isAdmin, leaveRoom } from "../services/chatRoom";
import { AuthContext } from "../contexts/AuthContext";
import { useParams, useNavigate } from "react-router-dom";
import "../styles/ChatRoomView.css";

const ChatRoomView = () => {
  const {
    authState: { userId, username },
  } = useContext(AuthContext);
  const { roomId } = useParams();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [socket, setSocket] = useState(null);
  const [roomName, setRoomName] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [isRoomAdmin, setIsRoomAdmin] = useState(false);

  useEffect(() => {
    getRoomById(roomId)
      .then((response) => setRoomName(response.data.roomName))
      .catch((error) => console.error("Error fetching room name:", error));

    getMessagesForRoom(roomId)
      .then((response) => setMessages(response.data))
      .catch((error) => console.error("Error fetching messages:", error));

    const ws = new WebSocket(`ws://localhost:8080/chatroom/${roomId}`);
    ws.onmessage = (event) => {
      const message = JSON.parse(event.data);
      setMessages((prev) => [...prev, message]);
    };
    setSocket(ws);

    isAdmin(roomId, userId)
      .then((response) => setIsRoomAdmin(response.data))
      .catch((error) => console.error("Error checking admin rights:", error));

    return () => ws.close();
  }, [roomId, userId]);

  const handleSendMessage = () => {
    if (!newMessage.trim()) return;

    const messageData = {
      roomId: parseInt(roomId, 10),
      userId: userId,
      username: username,
      content: newMessage.trim(),
      createdAt: new Date().toISOString(),
    };

    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify(messageData));
      setNewMessage("");
    } else {
      console.error("WebSocket is not connected.");
    }
  };

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const handleUploadFile = async () => {
    if (!selectedFile) return;

    try {
      const response = await uploadFile(roomId, selectedFile);

      const messageData = {
        roomId: parseInt(roomId, 10),
        userId: userId,
        username: username,
        content: response.data.url,
        createdAt: new Date().toISOString(),
      };

      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(messageData));
      }

      setSelectedFile(null);
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };

  const handleLeaveRoom = async () => {
    try {
      await leaveRoom(roomId, userId);
      alert("You have left the room!");
      navigate("/rooms");
    } catch (error) {
      console.error("Error leaving room:", error);
      alert("Failed to leave the room!");
    }
  };

  return (
    <div className="chat-room">
      <h2>{roomName ? roomName : "Chat Room"}</h2>

      <div className="room-actions">
        {isRoomAdmin && (
          <button onClick={() => navigate(`/manage-room/${roomId}`)}>Manage Room</button>
        )}
        {!isRoomAdmin && (
          <button onClick={handleLeaveRoom}>Out Room</button>
        )}
      </div>

      <div className="message-list">
        {messages.map((msg, index) => (
          <div key={index}>
            <p>
              <strong>{msg.username}:</strong>{" "}
              {msg.content.startsWith("http") ? (
                <>
                  <span>
                    {msg.content.split("/").pop().split("_").slice(1).join("_")}
                  </span>
                  <button
                    onClick={() =>
                      downloadFile(msg.content.split("/").pop())
                    }
                  >
                    Download
                  </button>
                </>
              ) : (
                msg.content
              )}{" "}
              ({new Date(msg.createdAt).toLocaleTimeString()}{" "}
              {new Date(msg.createdAt).toLocaleDateString()})
            </p>
          </div>
        ))}
      </div>

      <div className="send-message">
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Enter your message"
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>

      <div className="upload-file">
        <input type="file" onChange={handleFileChange} />
        <button onClick={handleUploadFile}>Upload File</button>
      </div>
    </div>
  );
};

export default ChatRoomView;
