import './App.scss';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import NavigationBar from "./components/NavigationBar/NavigationBar";
import {AuthProvider} from "./contexts/authcontext";
import Home from "./components/Home/Home";
import PrivateRoute from "./contexts/privateRoute";
import Login from "./components/Authentification/Login";
import Register from "./components/Authentification/Register";
import Dashboard from './components/Dashboard/Dashboard';

function App() {
  return (
      <AuthProvider>
          <Router>
              <div className="App">
                  <NavigationBar />
                  <div className="App-body">
                    <div className="App-body-inner">
                        <Routes>
                            <Route path="/" element={<Home/>} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/Dashboard" element={<PrivateRoute><Dashboard/></PrivateRoute>}/>
                            <Route path="/Help" element={<PrivateRoute><Home/></PrivateRoute>}/>
                        </Routes>
                    </div>
                  </div>
              </div>
          </Router>
      </AuthProvider>
  );
}

export default App;
