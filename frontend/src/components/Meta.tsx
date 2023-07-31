import React from 'react';
import {Helmet} from "react-helmet-async";

type MetaInputType = { description?: string, title?: string, keywords?: string }

const Meta = ({ description = 'We sell best products for cheep', title = 'Welcome to ProShop', keywords = 'electronics, buy electronics, cheap electronics' }: MetaInputType) => {
    return (
        <Helmet>
            <title>{title}</title>
            <meta name='description' content={description}/>
            <meta name='keywords' content={keywords}/>
        </Helmet>
    );
};
export default Meta;