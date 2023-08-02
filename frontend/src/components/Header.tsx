import React, {FormEventHandler} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap';
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
    return (
        <header>
            <Navbar bg="dark" variant="dark" expand="lg" collapseOnSelect>
                <Container>
                    <LinkContainer to='/'>
                        <Navbar.Brand>{APP_TITLE}</Navbar.Brand>
                    </LinkContainer>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ml-auto">
                            { userState.user ? (
                                <NavDropdown title={userState.user.name} id='username'>
                                    <LinkContainer to='/profile'>
                                        <NavDropdown.Item>Profile</NavDropdown.Item>
                                    </LinkContainer>
                                    <NavDropdown.Item onClick={logoutHandler}>Logout</NavDropdown.Item>
                                </NavDropdown>
                            ) : (
                                <LinkContainer to='/login'>
                                    <Nav.Link><i className="fas fa-user"/> Log in</Nav.Link>
                                </LinkContainer>
                            )}
                            {
                                userState?.user?.admin
                                && (
                                    <NavDropdown title='Admin' id='adminMenu'>
                                        <LinkContainer to='/admin/userList'>
                                            <NavDropdown.Item>Users</NavDropdown.Item>
                                        </LinkContainer>
                                    </NavDropdown>
                                )
                            }
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </header>
    );
}

export default Header
