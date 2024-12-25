import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";
import {
  getAllRequestJoin,
  acceptRequestJoinRoom,
  declineRequestJoinRoom,
  isAdmin,
  getAllUsersInRoom,
  removeUserFromRoom,
} from "../services/chatRoom";

const ManageRoom = () => {
  const { roomId } = useParams();
  const navigate = useNavigate();
  const {
    authState: { userId },
  } = useContext(AuthContext);

  const [isAdminRoom, setIsAdminRoom] = useState(false);
  const [requests, setRequests] = useState([]);
  const [users, setUsers] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const adminResponse = await isAdmin(roomId, userId);
        setIsAdminRoom(adminResponse.data);

        if (adminResponse.data) {
          const requestResponse = await getAllRequestJoin(roomId);
          setRequests(requestResponse.data);

          const usersResponse = await getAllUsersInRoom(roomId);
          setUsers(
            usersResponse.data.filter((user) => user.userId !== userId)
          );
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, [roomId, userId]);

  const handleAccept = (targetUserId) => {
    acceptRequestJoinRoom(roomId, targetUserId)
      .then(() => {
        alert("Request accepted!");
        setRequests((prev) => prev.filter((user) => user.userId !== targetUserId));
      })
      .catch((error) => {
        console.error("Error accepting request:", error);
        alert("Failed to accept request!");
      });
  };

  const handleDecline = (targetUserId) => {
    declineRequestJoinRoom(roomId, targetUserId)
      .then(() => {
        alert("Request declined!");
        setRequests((prev) => prev.filter((user) => user.userId !== targetUserId));
      })
      .catch((error) => {
        console.error("Error declining request:", error);
        alert("Failed to decline request!");
      });
  };

  const handleDeleteUser = (targetUserId) => {
    removeUserFromRoom(roomId, targetUserId)
      .then(() => {
        alert("User removed from room!");
        setUsers((prev) => prev.filter((user) => user.userId !== targetUserId));
      })
      .catch((error) => {
        console.error("Error removing user from room:", error);
        alert("Failed to remove user!");
      });
  };

  if (!isAdminRoom) {
    return (
      <div>
        <p>You do not have permission to manage this room.</p>
        <button onClick={() => navigate(`/chatroom/${roomId}`)}>Back to Chat Room</button>
      </div>
    );
  }

  return (
    <div>
      <h1>Manage Room: {roomId}</h1>

      <h2>Join Requests</h2>
      {requests.length === 0 ? (
        <p>No pending requests</p>
      ) : (
        <ul>
          {requests.map((user) => (
            <li key={user.userId}>
              {user.username}
              <button onClick={() => handleAccept(user.userId)}>Accept</button>
              <button onClick={() => handleDecline(user.userId)}>Decline</button>
            </li>
          ))}
        </ul>
      )}

      <h2>Users in Room</h2>
      {users.length === 0 ? (
        <p>No users in room</p>
      ) : (
        <ul>
          {users.map((user) => (
            <li key={user.userId}>
              {user.username}
              <button onClick={() => handleDeleteUser(user.userId)}>Delete</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default ManageRoom;
