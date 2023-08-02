import React from "react";
import {Helmet} from "react-helmet-async";
import Meta from "../components/Meta";
import {APP_DESCRIPTION, APP_KEYWORDS, APP_NAME} from "../constants";

const HomeScreen = () => {
    return (
        <>
            <Meta/>
            <Helmet>
                <title>{APP_NAME}</title>
                <meta name='description' content={APP_DESCRIPTION}/>
                <meta name='keywords' content={APP_KEYWORDS}/>
            </Helmet>
            <h1>Home Page</h1>
        </>
    )
};
export default HomeScreen;