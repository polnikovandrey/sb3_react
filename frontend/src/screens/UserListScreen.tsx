import React, {useEffect} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {selectUserList} from "../slice/userListSlice";
import {userDeleteAction, userListAction} from "../actions/userActions";
import Loader from "../components/Loader";
import {Button, Table} from "react-bootstrap";
import Message from "../components/Message";
import {LinkContainer} from "react-router-bootstrap";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserDelete} from "../slice/userDeleteSlice";
import {useLocation, useNavigate} from "react-router";

const UserListScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const dispatch = useAppDispatch();
    const userListState = useAppSelector(selectUserList);
    const { loading, users, error } = userListState;
    const userInfoState = useAppSelector(selectUserInfo);
    const token = userInfoState?.user?.token || '';
    const admin = userInfoState.user?.admin;
    const userDeleteState = useAppSelector(selectUserDelete);
    const { success: successDelete } = userDeleteState;
    useEffect(() => {
        (async () => {
            if (admin) {
                await userListAction(token, dispatch);
            } else {
                navigate('/login');
            }
        })();
    }, [ admin, dispatch, location, navigate, successDelete, token ]);
    async function deleteHandler(userId: string) {
        if (window.confirm('Are you sure?')) {
            await userDeleteAction(userId, token, dispatch);
        }
    }
    return (
        <>
            <h1>Users</h1>
            {loading
                ? <Loader/>
                : error
                    ? <Message variant='danger'>{error}</Message>
                    : (
                        <Table striped bordered hover responsive className='table-sm'>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>NAME</th>
                                <th>EMAIL</th>
                                <th>ADMIN</th>
                                <th/>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                users?.map(user => (
                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.name}</td>
                                        <td><a href={`mailto:${user.email}`}>{user.email}</a></td>
                                        <td>
                                            {user.admin
                                                ? (<i className='fas fa-check' style={{color: "green"}}/>)
                                                : (<i className='fas fa-times' style={{color: "red"}}/>)}
                                        </td>
                                        <td>
                                            <LinkContainer to={`user/${user.id}/edit`}>
                                                <Button variant='light' className='btn-sm'>
                                                    <i className='fas fa-edit'/>
                                                </Button>
                                            </LinkContainer>
                                            <Button variant='danger' className='btn-sm' onClick={() => deleteHandler(user.id)}>
                                                <i className='fas fa-trash'/>
                                            </Button>
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </Table>
                    )}
        </>
    );
};

export default UserListScreen;