import React, {FormEventHandler, useEffect, useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router";
import {Button, Form} from "react-bootstrap";
import {Link} from "react-router-dom";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import Message from "../components/Message";
import Loader from "../components/Loader";
import FormContainer from "../components/FormContainer";
import {selectUserProfile} from "../slice/userProfileSlice";
import {getUserProfileAction} from "../actions/userProfileActions";
import {selectUserInfo} from "../slice/userSlice";
import {selectUserUpdate} from "../slice/userUpdateSlice";
import {resetUserProfileByIdAction, updateUserProfileByIdAction} from "../actions/userUpdateActions";

const UserEditScreen = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { id, page} = useParams();
    const userId = id || '';
    const pageIndex = page || '0';
    const [ firstName, setFirstName ] = useState('');
    const [ lastName, setLastName ] = useState('');
    const [ middleName, setMiddleName ] = useState('');
    const [ email, setEmail ] = useState('');
    const [ username, setUsername ] = useState('');
    const userInfoState = useAppSelector(selectUserInfo);
    const token = userInfoState?.user?.token || '';
    const userProfileState = useAppSelector(selectUserProfile);
    const { loading, user, error } = userProfileState;
    const userUpdateState = useAppSelector(selectUserUpdate);
    const { loading: loadingUpdate, error: errorUpdate, success: successUpdate } = userUpdateState;
    const dispatch = useAppDispatch();
    useEffect(() => {
        (async () => {
            if (successUpdate) {
                await resetUserProfileByIdAction(dispatch);
                navigate(`/admin/userList/${pageIndex}`);
            } else if (!user?.name || user.id !== Number(userId)) {
                await getUserProfileAction(Number(userId), token, dispatch);
            } else {
                setFirstName(user.firstName);
                setLastName(user.lastName);
                setMiddleName(user.middleName);
                setEmail(user.email);
                setUsername(user.name);
            }
        })();
    }, [ dispatch, location, navigate, successUpdate, token, userId, user ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        await updateUserProfileByIdAction(token, { id: Number(userId), email: email, name: username, firstName: firstName, lastName: lastName, middleName: middleName }, dispatch);

    };
    return (
        <>
            <Link to='/admin/userList' className='btn btn-light my-3'>Go back</Link>
            <FormContainer>
                <h1>Edit user</h1>
                { loadingUpdate && <Loader/> }
                { errorUpdate && <Message variant='danger'>{errorUpdate}</Message> }
                { loading
                    ? <Loader/>
                    : error
                        ? <Message variant='danger'>{error}</Message>
                        : (
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
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control type='text' placeholder='Enter Username' value={username} onChange={(e) => setUsername(e.target.value)}/>
                                </Form.Group>
                                <Form.Group controlId='email' className='mb-3'>
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control type='email' placeholder='Enter email' value={email} onChange={(e) => setEmail(e.target.value)}/>
                                </Form.Group>
                                <Button type='submit' variant='primary'>
                                    Update
                                </Button>
                            </Form>
                        )}
            </FormContainer>
        </>
    );
}

export default UserEditScreen;