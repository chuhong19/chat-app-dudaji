import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './components/layout/Landing';
import Auth from './views/Auth';
import About from './views/About';
import AuthContextProvider from './contexts/AuthContext';
import Dashboard from './views/Dashboard';
import ProtectedRoute from './components/routing/ProtectedRoute';
import PostsContextProvider from './contexts/PostsContext';
import Home from './views/Home';
import Stalk from './views/Stalk';
import Profile from './views/Profile';
import Community from './views/Community';
import TictacToe from './views/Tictactoe';
import ChatRoomView from './views/ChatRoomView';
import AllRooms from './views/AllRooms';
import ManageRoom from './views/ManageRoom';

function App() {
  return (
    <Router>
      <AuthContextProvider>
        <PostsContextProvider>
          <Routes>
            <Route path='/' element={<Landing />} />
            <Route path='/about' element={<About />} />
            <Route path='/login' element={<Auth authRoute='login' />} />
            <Route path='/register' element={<Auth authRoute='register' />} />
            <Route element={<ProtectedRoute />}>
              <Route path="/dashboard" Component={Dashboard} />
              <Route path="/home" Component={Home} />
              <Route path="/stalk/:stalkUserId" Component={Stalk} />
              <Route path="/community" Component={Community} />
              <Route path="/profile" Component={Profile} />
              <Route path="/tictactoe" Component={TictacToe} />
              <Route path="/rooms" Component={AllRooms} />
              <Route path="/chatroom/:roomId" Component={ChatRoomView} />
              <Route path="/manage-room/:roomId" element={<ManageRoom />} />
            </Route>
          </Routes>
        </PostsContextProvider>
      </AuthContextProvider>
    </Router>
  );
}

export default App;
