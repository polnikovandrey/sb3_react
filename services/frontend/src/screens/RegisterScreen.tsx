import React, {FormEventHandler, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import {userRegisterAction} from "../actions/userActions";
import FormContainer from "../components/FormContainer";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {useLocation, useNavigate} from "react-router";
import {validateUserFormData} from "../validation/userFormValidator";

const RegisterScreen = () => {
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

    const userState: UserState = useAppSelector(selectUserInfo);
    const dispatch = useAppDispatch();

    const redirect: string = location.search ? location.search.split('=')[1] : '/';

    useEffect(() => {
        if (userState?.user) {
            navigate(redirect);
        }
    }, [ location, navigate, redirect, userState ]);

    useEffect(() => {
        if (messages.length !== 0) {
            window.scrollTo({top: 0, behavior: "smooth"});
        }
    }, [messages]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        const messages = validateUserFormData(firstName, lastName, middleName, username, email, password, confirmPassword);
        setMessages(messages);
        if (messages.length === 0) {
            await userRegisterAction(firstName, lastName, middleName, username, email, password, dispatch);
        }
    };
    return (
        <FormContainer>
            <h1>Register</h1>
            { messages.length !== 0 && messages.map((message, idx) => <Message variant='danger' key={idx}>{message}</Message>) }
            { userState?.error && <Message variant='danger'>{userState.error}</Message> }
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
                    <Form.Label>Email</Form.Label>
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
                    Register
                </Button>
            </Form>
            <Row className='py-3'>
                <Col>
                    Have an account? <Link to={redirect ? `/login?redirect=${redirect}` : '/login'}>Log in</Link>
                </Col>
            </Row>
        </FormContainer>
    );
}

export default RegisterScreen;