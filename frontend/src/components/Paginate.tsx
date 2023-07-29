import React from 'react';
import {Pagination} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";

const Paginate = ({ pages, page, admin = false, keyword = '' }: { pages: number, page: number, admin: boolean, keyword: string }) => {
    return (
        <>
            { pages > 1 && (
                <Pagination>
                    {[...Array(pages).keys()].map(x => (
                        <LinkContainer key={x + 1} to={
                            admin
                                ? `/admin/productList/${x + 1}`
                                : keyword
                                    ? `/search/${keyword}/page/${x + 1}`
                                    : `/page/${x + 1}`
                        }>
                            <Pagination.Item active={x + 1 === page} activeLabel=''>{x + 1}</Pagination.Item>
                        </LinkContainer>
                    ))}
                </Pagination>
            )}
        </>
    );
};

export default Paginate;