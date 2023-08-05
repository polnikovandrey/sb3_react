import React, {FormEventHandler, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import {userLoginAction} from "../actions/userActions";
import FormContainer from "../components/FormContainer";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {useLocation, useNavigate} from "react-router";

const LoginScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [ usernameOrEmail, setUsernameOrEmail ] = useState('');
    const [ password, setPassword ] = useState('');

    const userState: UserState = useAppSelector(selectUserInfo);
    const dispatch = useAppDispatch();

    const redirect: string = location.search ? location.search.split('=')[1] : '/';

    useEffect(() => {
        if (userState?.user) {
            navigate(redirect);
        }
    }, [ location, navigate, redirect, userState ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        await userLoginAction(usernameOrEmail, password, dispatch);
    }
    return (
        <FormContainer>
            <h1>Log in</h1>
            { userState?.error && <Message variant='danger'>{userState.error}</Message> }
            <Form onSubmit={submitHandler}>
                <Form.Group controlId='email' className='mb-3'>
                    <Form.Label>Username or email</Form.Label>
                    <Form.Control type='input' autoComplete='off' placeholder='Enter username or email' value={usernameOrEmail} onChange={(e) => setUsernameOrEmail(e.target.value)}/>
                </Form.Group>
                <Form.Group controlId='password' className='mb-4'>
                    <Form.Label>Password</Form.Label>
                    <Form.Control type='password' autoComplete='off' placeholder='Enter password' value={password} onChange={(e) => setPassword(e.target.value)}/>
                </Form.Group>
                <Button type='submit' variant='primary'>Log in</Button>
            </Form>
            <Row className='py-3'>
                <Col>
                    Don't have an account? <Link to={redirect ? `/register?redirect=${redirect}` : '/register'}>Register</Link>
                </Col>
            </Row>
        </FormContainer>
    );
}

export default LoginScreen;