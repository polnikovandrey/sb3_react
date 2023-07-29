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
    const [ name, setName ] = useState('');
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
    }, [ location, redirect, userState ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        if (password !== confirmPassword) {
            setMessage('Passwords do not match.');
        } else {
            await userRegisterAction(name, email, password, dispatch);
        }
    };
    return (
        <FormContainer>
            <h1>Register</h1>
            { message && <Message variant='danger'>{message}</Message> }
            { userState?.error && <Message variant='danger'>{userState.error}</Message> }
            { userState?.loading && <Loader/> }
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