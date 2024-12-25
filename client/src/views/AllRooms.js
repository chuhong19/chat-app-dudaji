import React, { useEffect, useState, useContext } from "react";
import { getAllRooms, getRoomsForUser, createRoom, joinRoom, getPendingRoomsForUser } from "../services/chatRoom";
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

const AllRooms = () => {
  const {
    authState: { userId },
  } = useContext(AuthContext);
  const [allRooms, setAllRooms] = useState([]);
  const [myRooms, setMyRooms] = useState([]);
  const [pendingRooms, setPendingRooms] = useState([]);
  const [newRoomName, setNewRoomName] = useState("");
  const [newRoomDescription, setNewRoomDescription] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    getAllRooms().then((response) => setAllRooms(response.data));
    getRoomsForUser(userId).then((response) => setMyRooms(response.data));
    getPendingRoomsForUser(userId).then((response) => {
      setPendingRooms(response.data.map((room) => room.id));
    });
  }, [userId]);

  useEffect(() => {
    const interval = setInterval(() => {
      getRoomsForUser(userId).then((response) => setMyRooms(response.data));
      getPendingRoomsForUser(userId).then((response) => {
        setPendingRooms(response.data.map((room) => room.id));
      });
    }, 5000);
    return () => clearInterval(interval);
  }, [userId]);

  const handleCreateRoom = () => {
    if (!newRoomName || !newRoomDescription) return;
    createRoom({ name: newRoomName, description: newRoomDescription })
      .then((response) => {
        alert("Room created successfully!");
        setAllRooms((prev) => [...prev, response.data]);
        navigate(`/chatroom/${response.data.id}`);
      })
      .catch((error) => alert("Error creating room: " + error.message));
  };

  const handleRequestJoinRoom = (roomId) => {
    joinRoom(roomId, userId)
      .then(() => {
        setPendingRooms((prev) => [...prev, roomId]);
        alert("Your request to join the room has been sent. Please wait for admin approval.");
      })
      .catch((error) => {
        console.error("Error requesting to join room:", error);
        alert("Failed to request join room!");
      });
  };

  return (
    <div>
      <h3>Pending Rooms</h3>
      <ul>
        {pendingRooms.length > 0 ? (
          allRooms
            .filter((room) => pendingRooms.includes(room.id))
            .map((room) => (
              <li key={room.id}>
                {room.roomName} - <span>Pending Approval</span>
              </li>
            ))
        ) : (
          <p>No pending rooms</p>
        )}
      </ul>

      <h3>Available Rooms</h3>
      <ul>
        {allRooms
          .filter(
            (room) =>
              !myRooms.some((r) => r.id === room.id) && !pendingRooms.includes(room.id)
          )
          .map((room) => (
            <li key={room.id} style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              {room.roomName}
              <button onClick={() => handleRequestJoinRoom(room.id)}>Join</button>
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
          placeholder="New Room Name..."
          value={newRoomName}
          onChange={(e) => setNewRoomName(e.target.value)}
        />
        <input
          type="text"
          placeholder="New Room Description..."
          value={newRoomDescription}
          onChange={(e) => setNewRoomDescription(e.target.value)}
        />
        <button onClick={handleCreateRoom}>Create Room</button>
      </div>
    </div>
  );
};

export default AllRooms;
