import React, {FormEventHandler, useEffect, useState} from "react";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import Loader from "../components/Loader";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserProfile} from "../slice/userProfileSlice";
import {getUserProfileAction, updateUserProfileAction, updateUserProfileResetAction} from "../actions/userProfileActions";
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
    const dispatch = useAppDispatch();

    useEffect(() => {
        (async () => {
            if (userStateInfo) {
                if (userProfileInfo?.name && userProfileInfo?.id === userStateInfo.id) {
                    setName(userProfileInfo.name);
                    setEmail(userProfileInfo.email);
                } else {
                    await updateUserProfileResetAction(dispatch);
                    await getUserProfileAction('profile', userStateInfo.token, dispatch);
                }
            } else {
                navigate('/login');
            }
        })();
    }, [ dispatch, location, navigate, userStateInfo, userProfileInfo ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        const userInfo = userInfoState.user;
        if (password !== confirmPassword) {
            setMessage('Passwords do not match.');
        } else if (userInfo && userProfileState.user) {
            await updateUserProfileAction(userInfo.token, { id: userInfo.id, name, email, password }, dispatch);
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
        </Row>
    );
}

export default ProfileScreen;