import React from "react";
import {BrowserRouter, Route} from "react-router-dom";
import {Container} from 'react-bootstrap';
import Header from "./components/Header";
import Footer from "./components/Footer";
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import RegisterScreen from "./screens/RegisterScreen";
import ProfileScreen from "./screens/ProfileScreen";
import UserListScreen from "./screens/UserListScreen";
import UserEditScreen from "./screens/UserEditScreen";
import {Routes} from "react-router";
import {HelmetProvider} from "react-helmet-async";

const App = () => {
    return (
        <BrowserRouter>
            <HelmetProvider>
                <Header/>
                <main className="py-3">
                    <Container>
                        <Routes>
                            <Route path='/login' element={<LoginScreen/>}/>
                            <Route path='/register' element={<RegisterScreen/>}/>
                            <Route path='/profile' element={<ProfileScreen/>}/>
                            <Route path='/admin/userList' element={<UserListScreen/>}/>
                            <Route path='/admin/user/:id/edit' element={<UserEditScreen/>}/>
                            <Route path='/' element={<HomeScreen/>}/>
                        </Routes>
                    </Container>
                </main>
                <Footer/>
            </HelmetProvider>
        </BrowserRouter>
    );
}

export default App;
