import React, {FormEventHandler, useEffect} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {Container, Nav, Navbar} from 'react-bootstrap';
import {LinkContainer} from "react-router-bootstrap";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {userLogoutAction} from "../actions/userActions";
import {useNavigate} from "react-router";
import {APP_TITLE} from "../constants";

const Header = () => {
    const navigate = useNavigate();
    const userState: UserState = useAppSelector(selectUserInfo);
    const dispatch = useAppDispatch();
    const logoutHandler: FormEventHandler = async () => {
        await userLogoutAction(dispatch);
        navigate('/');
    };
    useEffect(() => {

    }, [userState]);
    return (
        <header>
            <Navbar expand="lg" bg="dark" variant="dark" collapseOnSelect>
                <Container>
                    <LinkContainer to='/'><Navbar.Brand>{APP_TITLE}</Navbar.Brand></LinkContainer>
                    { userState.user && (<Navbar.Text className="bi bi-person">{userState.user.name}</Navbar.Text>) }
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ml-auto">
                            { userState.user && <LinkContainer to='/profile'><Nav.Link>Profile</Nav.Link></LinkContainer> }
                            { userState.user && userState?.user?.admin && <LinkContainer to='/admin/userList'><Nav.Link>Users</Nav.Link></LinkContainer> }
                            { userState.user && <Nav.Link onClick={logoutHandler}>Logout</Nav.Link> }
                            { !userState.user && <LinkContainer to='/login'><Nav.Link><i className="fas fa-user"/>Log in</Nav.Link></LinkContainer> }
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </header>
    );
}

export default Header
