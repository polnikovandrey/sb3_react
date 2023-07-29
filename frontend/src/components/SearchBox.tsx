import React, {FormEventHandler, useState} from 'react';
import {Button, Form} from "react-bootstrap";
import {useNavigate} from "react-router";

const SearchBox = () => {
    const navigate = useNavigate();
    const [ keyword, setKeyword ] = useState('');
    const submitHandler: FormEventHandler = async (event) => {
        event.preventDefault()
        if (keyword.trim()) {
            navigate(`/search/${keyword}`);
        } else {
            navigate('/');
        }
    };
    return (
        <div>
            <Form onSubmit={submitHandler} className='d-flex'>
                <Form.Control type='text' name='q' onChange={(e) => setKeyword(e.target.value)} placeholder='Search products...' className='mr-sm-2 ml-sm-5'/>
                <Button type='submit' variant='outline-success' className='p-2'>Search</Button>
            </Form>
        </div>
    );
};

export default SearchBox;