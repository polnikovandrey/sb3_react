import React, {FormEventHandler, useEffect} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {Container, Nav, Navbar, ProgressBar} from 'react-bootstrap';
import {selectUserInfo} from "../slice/userSlice";
import {userLogoutAction} from "../actions/userActions";
import {useLocation, useNavigate} from "react-router";
import {APP_TITLE} from "../constants";
import {NavLink} from "react-router-dom";
import {UserState} from "../store/types";
import {selectUserList} from "../slice/userListSlice";
import {selectUserProfile} from "../slice/userProfileSlice";
import {selectUserUpdate} from "../slice/userUpdateSlice";
import {selectUserDelete} from "../slice/userDeleteSlice";
import UserNotificationPanel from "./UserNotificationPanel";

const Header = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const userState: UserState = useAppSelector(selectUserInfo);
    const { loading: userListLoading } = useAppSelector(selectUserList);
    const { loading: userProfileLoading } = useAppSelector(selectUserProfile);
    const { loading: userUpdateLoading } = useAppSelector(selectUserUpdate);
    const { loading: userDeleteLoading } = useAppSelector(selectUserDelete);
    const dispatch = useAppDispatch();
    const logoutHandler: FormEventHandler = async () => {
        await userLogoutAction(dispatch);
        navigate('/');
    };
    const loading = userState.loading || userListLoading || userProfileLoading || userUpdateLoading || userDeleteLoading;
    useEffect(() => {

    }, [userListLoading, userState]);
    return (
        <header>
            <Navbar expand="lg" bg="dark" variant="dark" collapseOnSelect>
                <Container>
                    <Navbar.Brand as={NavLink} to='/'>{APP_TITLE}</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav activeKey={location.pathname} className="ml-auto">
                            <Nav.Link as={NavLink} eventKey='/' to='/' className="d-none">Home</Nav.Link>
                            { userState.user && <Nav.Link as={NavLink} to='/profile'>Profile</Nav.Link> }
                            { userState.user && userState?.user?.admin && <Nav.Link as={NavLink} to='/admin/userList'>Users</Nav.Link> }
                            { userState.user && <Nav.Link onClick={logoutHandler}><i className="bi bi-person pe-1"/>Logout {userState.user.name}</Nav.Link> }
                            { !userState.user && <Nav.Link as={NavLink} to='/login'><i className="bi bi-person pe-1"/>Log in</Nav.Link> }
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            { userState.user && <UserNotificationPanel/> }
            <ProgressBar className={`progress_page-load ${loading ? 'opacity-100' : 'opacity-0'}`} animated={true} now={100} striped={true} variant={'info'} />

        </header>
    );
}

export default Header
