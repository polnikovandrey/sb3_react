import React, {FormEventHandler, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Button, Col, Form, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import Loader from "../components/Loader";
import {userLoginAction} from "../actions/userActions";
import FormContainer from "../components/FormContainer";
import {UserState} from "../store/types";
import {selectUserInfo} from "../slice/userSlice";
import {useLocation, useNavigate} from "react-router";

const LoginScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [ email, setEmail ] = useState('');
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
        await userLoginAction(email, password, dispatch);
    }
    return (
        <FormContainer>
            <h1>Log in</h1>
            { userState?.error && <Message variant='danger'>{userState.error}</Message> }
            { userState?.loading && <Loader/> }
            <Form onSubmit={submitHandler}>
                <Form.Group controlId='email' className='mb-3'>
                    <Form.Label>Email Address</Form.Label>
                    <Form.Control type='email' placeholder='Enter email' value={email} onChange={(e) => setEmail(e.target.value)}>

                    </Form.Control>
                </Form.Group>
                <Form.Group controlId='password' className='mb-4'>
                    <Form.Label>Password</Form.Label>
                    <Form.Control type='password' placeholder='Enter password' value={password} onChange={(e) => setPassword(e.target.value)}>

                    </Form.Control>
                </Form.Group>
                <Button type='submit' variant='primary'>
                    Log in
                </Button>
            </Form>
            <Row className='py-3'>
                <Col>
                    New customer? <Link to={redirect ? `/register?redirect=${redirect}` : '/register'}>Register</Link>
                </Col>
            </Row>
        </FormContainer>
    );
}

export default LoginScreen;