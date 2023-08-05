const firstNameMinLength = 4;
const firstNameMaxLength = 20;
const lastNameMinLength = 4;
const lastNameMaxLength = 20;
const middleNameMaxLength = 20;
const usernameMinLength = 3;
const usernameMaxLength = 15;
const emailMaxLength = 40;
const passwordMinLength = 6;
const passwordMaxLength = 20;

export const validateUserFormData = (firstName : string, lastName : string, middleName: string, username: string, email: string, password: string, confirmPassword: string) : string[] => {
    const result = [];
    if (firstName.length < firstNameMinLength || firstName.length > firstNameMaxLength) {
        result.push(`First name should have from ${firstNameMinLength} to ${firstNameMaxLength} characters`);
    }
    if (lastName.length < lastNameMinLength || lastName.length > lastNameMaxLength) {
        result.push(`Last name should have from ${lastNameMinLength} to ${lastNameMaxLength} characters`);
    }
    if (middleName.length > middleNameMaxLength) {
        result.push(`Middle name should have maximum ${middleNameMaxLength} characters`);
    }
    if (username.length < usernameMinLength || username.length > usernameMaxLength) {
        result.push(`Username should have from ${usernameMinLength} to ${usernameMaxLength} characters`);
    }
    if (email.length === 0 || email.length > emailMaxLength) {
        result.push(`Email is required and should have maximum ${emailMaxLength} characters`);
    }
    if (password.length < passwordMinLength || password.length > passwordMaxLength) {
        result.push(`Password  should have from ${passwordMinLength} to ${passwordMaxLength} characters`);
    }
    if (password !== confirmPassword) {
        result.push('Passwords do not match.');
    }
    return result;
};