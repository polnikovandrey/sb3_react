import React, {FormEventHandler, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import Loader from "../components/Loader";
import {userRegisterAction} from "../actions/userActions";
import FormContainer from "../components/FormContainer";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {useLocation, useNavigate} from "react-router";

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
    const [ message, setMessage ] = useState('');

    const userState: UserState = useAppSelector(selectUserInfo);
    const dispatch = useAppDispatch();

    const redirect: string = location.search ? location.search.split('=')[1] : '/';

    useEffect(() => {
        if (userState?.user) {
            navigate(redirect);
        }
    }, [ location, navigate, redirect, userState ]);

    useEffect(() => {
        if (message !== '') {
            window.scrollTo({top: 0, behavior: "smooth"});
        }
    }, [message]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        const firstNameMinLength = 4;
        const firstNameMaxLength = 20;
        const lastNameMinLength = 4;
        const lastNameMaxLength = 20;
        const middleNameMaxLength = 20;
        const usernameMinLength = 3;
        const usernameMaxLength = 15;
        const emailMaxLength = 40;
        const passwordMinLength = 6;
        const passwordMaxLength = 20;
        if (firstName.length < firstNameMinLength || firstName.length > firstNameMaxLength) {
            setMessage(`First name should have from ${firstNameMinLength} to ${firstNameMaxLength} characters`);
        } else if (lastName.length < lastNameMinLength || lastName.length > lastNameMaxLength) {
            setMessage(`Last name should have from ${lastNameMinLength} to ${lastNameMaxLength} characters`);
        } else if (middleName.length > middleNameMaxLength) {
            setMessage(`Middle name should have maximum ${middleNameMaxLength} characters`);
        } else if (username.length < usernameMinLength || username.length > usernameMaxLength) {
            setMessage(`Username should have from ${usernameMinLength} to ${usernameMaxLength} characters`);
        } else if (email.length == 0 || email.length > emailMaxLength) {
            setMessage(`Email is required and should have maximum ${emailMaxLength} characters`);
        } else if (password.length < passwordMinLength || password.length > passwordMaxLength) {
            setMessage(`Password  should have from ${passwordMinLength} to ${passwordMaxLength} characters`);
        } else  if (password !== confirmPassword) {
            setMessage('Passwords do not match.');
        } else {
            await userRegisterAction(firstName, lastName, middleName, username, email, password, dispatch);
        }
    };
    return (
        <FormContainer>
            <h1>Register</h1>
            { message && <Message variant='danger'>{message}</Message> }
            { userState?.error && <Message variant='danger'>{userState.error}</Message> }
            { userState?.loading && <Loader/> }
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
                    <Form.Control type='password' autoComplete='off' placeholder='Repeat Password' value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}/>
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