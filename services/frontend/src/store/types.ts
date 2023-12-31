export type UserInfo = { id: number, name: string, email: string, admin: boolean, token: string };
export type UserProfileData = { id: number, email: string, name: string, firstName: string, lastName: string, middleName: string, admin: boolean };
export type UserProfile = { id?: number, email: string, name: string, firstName: string, lastName: string, middleName: string, password?: string, admin?: boolean };
export type UserListPage = { content: UserProfileData[], page: number, size: number, totalElements: number, totalPages: number, last: boolean }

export type UserListState = { loading?: boolean, users?: UserListPage, error?: string }
export type UserState = { loading?: boolean, user?: UserInfo, error?: string }
export type UserProfileState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserUpdateState = { loading?: boolean, success?: boolean, user?: UserProfile, error?: string }
export type UserDeleteStata = { loading?: boolean, success?: boolean, error?: string }