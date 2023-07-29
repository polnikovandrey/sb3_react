import React, {FormEventHandler, useEffect, useState} from "react";
import {Button, Col, Form, Row, Table} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import Loader from "../components/Loader";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserProfile} from "../slice/userProfileSlice";
import {getUserProfileAction, updateUserProfileAction, updateUserProfileResetAction} from "../actions/userProfileActions";
import {selectOrderUserList} from "../slice/orderUserListSlice";
import {orderUserListAction} from "../actions/orderActions";
import {LinkContainer} from "react-router-bootstrap";
import {useLocation, useNavigate} from "react-router";

const ProfileScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [ name, setName ] = useState('');
    const [ email, setEmail ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ confirmPassword, setConfirmPassword ] = useState('');
    const [ message, setMessage ] = useState('');

    const userProfileState = useAppSelector(selectUserProfile);
    const { user: userProfileInfo } = userProfileState;
    const userInfoState = useAppSelector(selectUserInfo);
    const { user: userStateInfo } = userInfoState;
    const orderUserListState = useAppSelector(selectOrderUserList);
    const { loading: ordersLoading, orders, error: ordersError } = orderUserListState;
    const dispatch = useAppDispatch();

    useEffect(() => {
        (async () => {
            if (userStateInfo) {
                if (userProfileInfo?.name && userProfileInfo?._id === userStateInfo._id) {
                    setName(userProfileInfo.name);
                    setEmail(userProfileInfo.email);
                } else {
                    await updateUserProfileResetAction(dispatch);
                    await getUserProfileAction('profile', userStateInfo.token, dispatch);
                    await orderUserListAction(userStateInfo.token, dispatch);
                }
            } else {
                navigate('/login');
            }
        })();
    }, [ dispatch, location, userStateInfo, userProfileInfo ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        const userInfo = userInfoState.user;
        if (password !== confirmPassword) {
            setMessage('Passwords do not match.');
        } else if (userInfo && userProfileState.user) {
            await updateUserProfileAction(userInfo.token, { _id: userInfo._id, name, email, password }, dispatch);
        }
    };
    return (
        <Row>
            <Col md={3}>
                <h2>User Profile</h2>
                { message && <Message variant='danger'>{message}</Message> }
                { userProfileState?.error && <Message variant='danger'>{userProfileState.error}</Message> }
                { userProfileState.success && <Message variant='success'>Profile updated</Message>}
                { userProfileState?.loading && <Loader/> }
                <Form onSubmit={submitHandler}>
                    <Form.Group controlId='name' className='mb-3'>
                        <Form.Label>Name</Form.Label>
                        <Form.Control type='name' placeholder='Enter name' value={name} onChange={(e) => setName(e.target.value)}/>
                    </Form.Group>
                    <Form.Group controlId='email' className='mb-3'>
                        <Form.Label>Email Address</Form.Label>
                        <Form.Control type='email' placeholder='Enter email' value={email} onChange={(e) => setEmail(e.target.value)}/>
                    </Form.Group>
                    <Form.Group controlId='password' className='mb-3'>
                        <Form.Label>Password</Form.Label>
                        <Form.Control type='password' placeholder='Enter password' value={password} onChange={(e) => setPassword(e.target.value)}/>
                    </Form.Group>
                    <Form.Group controlId='confirmPassword' className='mb-4'>
                        <Form.Label>Confirm Password</Form.Label>
                        <Form.Control type='password' placeholder='Confirm password' value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}/>
                    </Form.Group>
                    <Button type='submit' variant='primary'>
                        Update
                    </Button>
                </Form>
            </Col>
            <Col md={9}>
                <h2>My Orders</h2>
                {ordersLoading
                    ? <Loader />
                    : ordersError
                        ? <Message variant='danger'>{ordersError}</Message>
                        : (
                            <Table striped bordered hover responsive className='table-sm'>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>DATE</th>
                                        <th>TOTAL</th>
                                        <th>PAID</th>
                                        <th>DELIVERED</th>
                                        <th/>
                                    </tr>
                                </thead>
                                <tbody>
                                {orders!.map(order => (
                                    <tr key={order._id}>
                                        <td>{order._id}</td>
                                        <td>{order.createdAt.toString().substring(0, 10)}</td>
                                        <td>{order.totalPrice}</td>
                                        <td>{order.paid ? order.paidAt.toString().substring(0, 10) : (<i className='fas fa-times' style={{color: "red"}}/>)}</td>
                                        <td>{order.delivered ? order.deliveredAt.toString().substring(0, 10) : (<i className='fas fa-times' style={{color: "red"}}/>)}</td>
                                        <td>
                                            <LinkContainer to={`/order/${order._id}`}>
                                                <Button className='btn-sm' variant='light'>Details</Button>
                                            </LinkContainer>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        )}
            </Col>
        </Row>
    );
}

export default ProfileScreen;