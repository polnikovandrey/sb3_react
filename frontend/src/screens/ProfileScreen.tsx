import React, {FormEventHandler, useEffect, useState} from "react";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserProfile} from "../slice/userProfileSlice";
import {getUserProfileAction, updateUserProfileAction, updateUserProfileResetAction} from "../actions/userProfileActions";
import {useLocation, useNavigate} from "react-router";
import {validateUserFormData} from "../validation/userFormValidator";

const ProfileScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [ firstName, setFirstName ] = useState('');
    const [ lastName, setLastName ] = useState('');
    const [ middleName, setMiddleName ] = useState('');
    const [ username, setUsername ] = useState('');
    const [ email, setEmail ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ confirmPassword, setConfirmPassword ] = useState('');
    const [ messages, setMessages ] = useState<string[]>([]);

    const userProfileState = useAppSelector(selectUserProfile);
    const { user: userProfileInfo } = userProfileState;
    const userInfoState = useAppSelector(selectUserInfo);
    const { user: userStateInfo } = userInfoState;
    const dispatch = useAppDispatch();

    useEffect(() => {
        (async () => {
            if (userStateInfo) {
                if (userProfileInfo?.name && userProfileInfo?.id === userStateInfo.id) {
                    setFirstName(userProfileInfo.firstName);
                    setLastName(userProfileInfo.lastName);
                    setMiddleName(userProfileInfo.middleName);
                    setUsername(userProfileInfo.name);
                    setEmail(userProfileInfo.email);
                } else {
                    await updateUserProfileResetAction(dispatch);
                    await getUserProfileAction(userStateInfo.id, userStateInfo.token, dispatch);
                }
            } else {
                navigate('/login');
            }
        })();
    }, [ dispatch, location, navigate, userStateInfo, userProfileInfo ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        const userInfo = userInfoState.user;
        const messages = validateUserFormData(firstName, lastName, middleName, username, email, password, confirmPassword);
        setMessages(messages);
        if (messages.length == 0 && userInfo && userProfileState.user) {
            await updateUserProfileAction(userInfo.token, { id: userInfo.id, email: email, name: username, firstName: firstName, lastName: lastName, middleName: middleName, password: password }, dispatch);
            navigate('/');
        }
    };
    return (
        <>
            <h1>User Profile</h1>
            <Row>
                <Col md={3}>
                    { messages.length != 0 && messages.map((message, idx) => <Message variant='danger' key={idx}>{message}</Message>) }
                    { userProfileState?.error && <Message variant='danger'>{userProfileState.error}</Message> }
                    <Form onSubmit={submitHandler}>
                        <Form.Group controlId='firstName' className='mb-3'>
                            <Form.Label>First Name</Form.Label>
                            <Form.Control type='text' placeholder='Enter First Name' value={firstName} onChange={(e) => setFirstName(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='lastName' className='mb-3'>
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control type='text' placeholder='Enter Last Name' value={lastName} onChange={(e) => setLastName(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='middleName' className='mb-3'>
                            <Form.Label>Middle Name</Form.Label>
                            <Form.Control type='text' placeholder='Enter Middle Name' value={middleName} onChange={(e) => setMiddleName(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='username' className='mb-3'>
                            <Form.Label>Username</Form.Label>
                            <Form.Control type='text' placeholder='Enter Username' value={username} onChange={(e) => setUsername(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='email' className='mb-3'>
                            <Form.Label>Email Address</Form.Label>
                            <Form.Control type='email' placeholder='Enter Email' value={email} onChange={(e) => setEmail(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='password' className='mb-3'>
                            <Form.Label>Password</Form.Label>
                            <Form.Control type='password' autoComplete='off' placeholder='Enter Password' value={password} onChange={(e) => setPassword(e.target.value)}/>
                        </Form.Group>
                        <Form.Group controlId='confirmPassword' className='mb-4'>
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control type='password' autoComplete='off' placeholder='Confirm Password' value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}/>
                        </Form.Group>
                        <Button type='submit' variant='primary'>
                            Update
                        </Button>
                    </Form>
                </Col>
            </Row>
        </>
    );
}

export default ProfileScreen;