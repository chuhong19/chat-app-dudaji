import React, { useEffect, useState, useContext } from "react";
import { getMessagesForRoom } from "../services/message";
import { getRoomById } from "../services/chatRoom"; 
import { AuthContext } from "../contexts/AuthContext";
import { useParams } from "react-router-dom";

const ChatRoomView = () => {
  const {
    authState: { userId, username },
  } = useContext(AuthContext);
  const { roomId } = useParams(); 
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [socket, setSocket] = useState(null);
  const [roomName, setRoomName] = useState(""); 

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

    return () => ws.close();
  }, [roomId]);

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

  return (
    <div>
      <h2>{roomName ? roomName : "Chat Room"}</h2>
      <div>
        {messages.map((msg, index) => (
          <p key={index}>
            <strong>{msg.username}:</strong> {msg.content} (
            {new Date(msg.createdAt).toLocaleTimeString()}{" "}
            {new Date(msg.createdAt).toLocaleDateString()})
          </p>
        ))}
      </div>
      <div>
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Enter your message"
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );
};

export default ChatRoomView;
