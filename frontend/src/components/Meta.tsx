import React from 'react';
import {Helmet} from "react-helmet-async";
import {APP_DESCRIPTION, APP_KEYWORDS, APP_TITLE} from "../constants";

type MetaInputType = { description?: string, title?: string, keywords?: string }

const Meta = ({ description = APP_DESCRIPTION, title = APP_TITLE, keywords = APP_KEYWORDS }: MetaInputType) => {
    return (
        <Helmet>
            <title>{title}</title>
            <meta name='description' content={description}/>
            <meta name='keywords' content={keywords}/>
        </Helmet>
    );
};
export default Meta;