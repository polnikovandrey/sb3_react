export type CartItem = { productId: string, name: string, image: string, price: number, countInStock: number, quantity: number };
export type CartState = { items: CartItem[], shippingAddress: ShippingAddress, paymentMethod: string };
export type Order = CartState & { _id?: string, itemsPrice: number, shippingPrice: number, taxPrice: number, totalPrice: number };
export type OrderDetail = Order & { user: UserInfo, createdAt: Date, paid: boolean, paidAt: Date, delivered: boolean, deliveredAt: Date }
export type Review = {
    _id: string,
    name: string,
    rating: number,
    comment: string,
    user: UserInfo,
    createdAt: Date
}
export type ProductItemBase = {
    _id: string,
    name: string,
    image: string,
    description: string,
    brand: string,
    category: string,
    price: number,
    countInStock: number
}
export type ProductItem = ProductItemBase & { rating: number, numReviews: number, reviews: Review[] };
export type ProductRatingItem = { rating: number, numReviews?: number };
export type UserInfo = { _id: string, name: string, email: string, admin: boolean, token: string };
export type UserProfile = { _id?: string, name: string, email: string, password?: string, admin?: boolean };
export type ShippingAddress = { address: string, city: string, postalCode: string, country: string };

export type OrderState = { loading?: boolean, order?: Order, error?: string };
export type OrderDeliverState = { loading?: boolean, success?: boolean, error?: string }
export type OrderDetailState = { loading?: boolean, order?: OrderDetail, error?: string }
export type OrderListState = { loading?: boolean, orders?: OrderDetail[], error?: string }
export type OrderPayState = { loading?: boolean, success?: boolean, error?: string }
export type PaymentResult = { id: string, status: string, updateTime: string, email_address: string };

export type ProductCreateState = { loading?: boolean, product?: ProductItem, success?: boolean, error?: string };
export type ProductDeleteState = { loading?: boolean, success?: boolean, error?: string };
export type ProductsDetailsState = { loading: boolean, item?: ProductItem, error?: string };
export type ProductListLoadResultDto = { products: ProductItem[], page: number, pages: number };
export type ProductsListState = { loading: boolean, result?: ProductListLoadResultDto, error?: string };
export type ProductsTopState = { loading: boolean, products?: ProductItem[], error?: string };
export type ProductUpdateState = { loading?: boolean, product?: ProductItem, success?: boolean, error?: string };

export type CreateReviewDto = { rating: number, comment: string };
export type ReviewCreateState = { loading?: boolean, success?: boolean, error?: string };

export type UserState = { loading?: boolean, user?: UserInfo, error?: string }
export type UserProfileState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserUpdateState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserListInfo = { _id: string, name: string, email: string, admin: boolean };
export type UserListState = { loading?: boolean, users?: UserListInfo[], error?: string }
export type UserDeleteStata = { loading?: boolean, success?: boolean, error?: string }

export const numberToPriceString: (num: number) => string = num => (Math.round(num * 100) / 100).toFixed(2);
export const numberToPrice: (num: number) => number = num => Number(numberToPriceString(num));