import React from "react";
import {BrowserRouter as Router, Route} from "react-router-dom";
import {Container} from 'react-bootstrap';
import Header from "./components/Header";
import Footer from "./components/Footer";
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import RegisterScreen from "./screens/RegisterScreen";
import ProfileScreen from "./screens/ProfileScreen";
import UserListScreen from "./screens/UserListScreen";
import UserEditScreen from "./screens/UserEditScreen";

const App = () => {
    return (
        <Router>
            <Route element={<Header/>}/>
            <main className="py-3">
                <Container>
                    <Route path='/login' element={<LoginScreen/>}/>
                    <Route path='/register' element={<RegisterScreen/>}/>
                    <Route path='/profile' element={<ProfileScreen/>}/>
                    <Route path='/admin/userList' element={<UserListScreen/>}/>
                    <Route path='/admin/user/:id/edit' element={<UserEditScreen/>}/>
                    <Route path='/' element={<HomeScreen/>}/>
                </Container>
            </main>
            <Footer/>
        </Router>
    );
}

export default App;
