import React, {FormEventHandler, useEffect} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap';
import {LinkContainer} from "react-router-bootstrap";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {userLogoutAction} from "../actions/userActions";
import {Route} from "react-router-dom";
import SearchBox from "./SearchBox";
import {useLocation, useNavigate} from "react-router";

const Header = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const userState: UserState = useAppSelector(selectUserInfo);
    const dispatch = useAppDispatch();
    const logoutHandler: FormEventHandler = async () => {
        await userLogoutAction(dispatch);
        navigate('/');
    };
    const pathname = location.pathname;
    const showSearch = pathname === '/' || pathname.startsWith('/page/') || pathname.startsWith('/search/');
    useEffect(() => {

    }, [ showSearch ]);
    return <header>
        <Navbar bg="dark" variant="dark" expand="lg" collapseOnSelect>
            <Container>
                <LinkContainer to='/'>
                    <Navbar.Brand>ProShop</Navbar.Brand>
                </LinkContainer>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                { showSearch && (
                    <Route element={<SearchBox/>}/>
                )}
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        <LinkContainer to='/cart'>
                            <Nav.Link><i className="fas fa-shopping-cart"/> Cart</Nav.Link>
                        </LinkContainer>
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
                                    <LinkContainer to='/admin/productList'>
                                        <NavDropdown.Item>Products</NavDropdown.Item>
                                    </LinkContainer>
                                    <LinkContainer to='/admin/orderList'>
                                        <NavDropdown.Item>Orders</NavDropdown.Item>
                                    </LinkContainer>
                                </NavDropdown>
                            )
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    </header>;
}

export default Header
