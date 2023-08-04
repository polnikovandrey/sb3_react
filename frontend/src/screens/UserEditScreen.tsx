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
    const { id} = useParams();
    const userId = id || '';
    const [ name, setName ] = useState('');
    const [ email, setEmail ] = useState('');
    const [ admin, setAdmin ] = useState(false);
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
                navigate('/admin/userList')
            } else if (!user?.name || user.id !== Number(userId) ) {
                await getUserProfileAction(Number(userId), token, dispatch);
            } else {
                setName(user.name);
                setEmail(user.email);
                setAdmin(user.admin!);
            }
        })();
    }, [ dispatch, location, navigate, successUpdate, token, userId, user ]);

    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault();
        // TODO firstName, lastName, middleName
        await updateUserProfileByIdAction(token, { id: Number(userId), email: email, name: name, firstName: '', lastName: '', middleName: '', admin: admin }, dispatch);
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
                                <Form.Group controlId='name' className='mb-3'>
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control type='name' placeholder='Enter name' value={name} onChange={(e) => setName(e.target.value)}/>
                                </Form.Group>
                                <Form.Group controlId='email' className='mb-3'>
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control type='email' placeholder='Enter email' value={email} onChange={(e) => setEmail(e.target.value)}/>
                                </Form.Group>
                                <Form.Group controlId='admin' className='mb-4'>
                                    <Form.Check type='checkbox' label='Admin' checked={admin} onChange={(e) => setAdmin(e.target.checked)}/>
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