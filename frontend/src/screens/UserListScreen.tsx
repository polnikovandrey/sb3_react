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
                        <Table bordered hover responsive className='table-sm'>
                            <thead className="table-light">
                            <tr>
                                <th>Id</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Middle Name</th>
                                <th>Email</th>
                                <th>Username</th>
                                <th>Admin</th>
                                <th/>
                                <th/>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                users?.content?.map(user => (
                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.firstName}</td>
                                        <td>{user.lastName}</td>
                                        <td>{user.middleName}</td>
                                        <td><a href={`mailto:${user.email}`}>{user.email}</a></td>
                                        <td>{user.name}</td>
                                        <td align={"center"}>
                                            {user.admin
                                                ? (<i className='bi bi-check' style={{color: "green"}}/>)
                                                : (<i className='bi bi-x' style={{color: "red"}}/>)}
                                        </td>
                                        <td>
                                            <LinkContainer to={`user/${user.id}/edit`}>
                                                <Button variant='warning' className='btn-sm w-100'>
                                                    <i className='bi bi-pencil-square'/>
                                                </Button>
                                            </LinkContainer>
                                        </td>
                                        <td>
                                            <Button variant='danger' className='btn-sm w-100' onClick={() => deleteHandler(user.id)}>
                                                <i className='bi bi-trash'/>
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