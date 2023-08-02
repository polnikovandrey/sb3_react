import React from 'react'
import {Col, Container, Row} from 'react-bootstrap';
import {COPYRIGHT_AUTHOR} from "../constants";

const Footer = () => {
    return (
        <footer>
            <Container>
                <Row>
                    <Col className='text-center py-3'>
                        Copyright &copy; {COPYRIGHT_AUTHOR}
                    </Col>
                </Row>
            </Container>
        </footer>
    );
}

export default Footer