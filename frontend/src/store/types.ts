export type UserInfo = { _id: string, name: string, email: string, admin: boolean, token: string };
export type UserProfile = { _id?: string, name: string, email: string, password?: string, admin?: boolean };
export type UserState = { loading?: boolean, user?: UserInfo, error?: string }
export type UserProfileState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserUpdateState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserListInfo = { _id: string, name: string, email: string, admin: boolean };
export type UserListState = { loading?: boolean, users?: UserListInfo[], error?: string }
export type UserDeleteStata = { loading?: boolean, success?: boolean, error?: string }