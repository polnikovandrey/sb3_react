import * as React from "react";
import {useEffect, useState} from "react";
import {Alert, Fade} from "react-bootstrap";

const FadeInAlert: React.FC<{ i?: number, onClose?: () => void, children?: React.ReactNode }>
    = ({ i, onClose, children }) => {
    const [show, setShow] = useState(false);

    useEffect(() => {
        setTimeout(() => setShow(true), 50);
    }, []);

    return <Alert key={i} show={show} variant={"warning"} transition={Fade} dismissible={true} onClose={onClose}>{children}</Alert>;
};

export default FadeInAlert;