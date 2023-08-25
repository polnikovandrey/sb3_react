import React, {useEffect} from 'react';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {selectUserList} from "../slice/userListSlice";
import {userDeleteAction, userListAction} from "../actions/userActions";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import Message from "../components/Message";
import {LinkContainer} from "react-router-bootstrap";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserDelete} from "../slice/userDeleteSlice";
import {useLocation, useNavigate, useParams} from "react-router";

// TODO + email confirmed icon

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
    const { pageParam = '0' } = useParams();
    const page = Number(pageParam);
    useEffect(() => {
        (async () => {
            if (admin) {
                await userListAction(page, token, dispatch);
            } else {
                navigate('/login');
            }
        })();
    }, [ admin, dispatch, location, navigate, page, successDelete, token ]);
    async function deleteHandler(userId: number, nextPage: number) {
        if (window.confirm('Are you sure?')) {
            await userDeleteAction(userId, token, dispatch);
            navigate(`/admin/userList/${nextPage}`);
        }
    }
    return (
        <>
            <h1>Users</h1>
            { error && <Message variant='danger'>{error}</Message> }
            { !loading && (
                <Container>
                    { users && (
                        <Row className='mb-3'>
                            <Col>
                                <LinkContainer to={`/admin/userList/${users.page - 1}`}>
                                    <Button className={`btn btn-primary ${users.page <= 0 ? 'disabled' : ''}`}>Prev page</Button>
                                </LinkContainer>
                            </Col>
                            <Col className="col d-flex justify-content-center ">
                                Page<span className="mx-1">{users.page + 1}</span>of<span className="mx-1">{users.totalPages}</span>
                            </Col>
                            <Col className="col d-flex justify-content-end">
                                <LinkContainer to={`/admin/userList/${users.page + 1}`}>
                                    <Button className={`btn btn-primary ${users.page + 1 >= users.totalPages ? 'disabled' : ''}`}>Next page</Button>
                                </LinkContainer>
                            </Col>
                        </Row>
                    )}
                    <Row>
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
                                            {
                                                user.id !== 1 && (
                                                    <LinkContainer to={`/admin/user/${user.id}/${users?.page}/edit`}>
                                                        <Button variant='warning' className='btn-sm w-100'>
                                                            <i className='bi bi-pencil-square'/>
                                                        </Button>
                                                    </LinkContainer>
                                                )
                                            }
                                        </td>
                                        <td>
                                            {
                                                user.id !== 1 && (
                                                    <Button variant='danger' className='btn-sm w-100' onClick={() => deleteHandler(user.id, users?.content.length > 1 ? users?.page : (users?.page - 1))}>
                                                        <i className='bi bi-trash'/>
                                                    </Button>
                                                )
                                            }
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </Table>
                    </Row>
                </Container>
            )}
        </>
    );
};

export default UserListScreen;