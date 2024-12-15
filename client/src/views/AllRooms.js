import React, { useEffect, useState, useContext } from "react";
import { getAllRooms, getRoomsForUser, createRoom, joinRoom } from "../services/chatRoom"; 
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

const AllRooms = () => {
  const {
    authState: { userId }, 
  } = useContext(AuthContext);
  const [allRooms, setAllRooms] = useState([]); 
  const [myRooms, setMyRooms] = useState([]); 
  const [newRoomName, setNewRoomName] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    getAllRooms().then((response) => setAllRooms(response.data)); 
    getRoomsForUser(userId).then((response) => setMyRooms(response.data)); 
  }, [userId]);

  const handleCreateRoom = () => {
    if (!newRoomName) return;

    createRoom({ name: newRoomName, description: "New Room" })
      .then((response) => {
        alert("Room created successfully!");
        setAllRooms((prev) => [...prev, response.data]); 
      })
      .catch((error) => alert("Error creating room: " + error.message));
  };

  const handleJoinRoom = (roomId) => {
    joinRoom(roomId, userId)
      .then(() => {
        setMyRooms((prev) => [...prev, allRooms.find((room) => room.id === roomId)]);
        navigate(`/chatroom/${roomId}`);
      })
      .catch((error) => {
        console.error("Error joining room:", error);
        alert("Failed to join room!");
      });
  };

  return (
    <div>
      <h2>All Rooms</h2>
      <h3>Available Rooms</h3>
      <ul>
        {allRooms.map((room) => (
          <li key={room.id}>
            {room.roomName}
            {!myRooms.some((r) => r.id === room.id) && (
              <button onClick={() => handleJoinRoom(room.id)}>Join</button>
            )}
          </li>
        ))}
      </ul>

      <h3>Your Rooms</h3>
      <ul>
        {myRooms.map((room) => (
          <li key={room.id} onClick={() => navigate(`/chatroom/${room.id}`)}>
            {room.roomName}
          </li>
        ))}
      </ul>

      <div>
        <input
          type="text"
          placeholder="New Room Name"
          value={newRoomName}
          onChange={(e) => setNewRoomName(e.target.value)}
        />
        <button onClick={handleCreateRoom}>Create Room</button>
      </div>
    </div>
  );
};

export default AllRooms;
