import { CustomError } from "./custom-error";

export class InvalidCredentialsError extends CustomError {
    statusCode = 400;

    constructor() {
        super('Invalid credentials');

        Object.setPrototypeOf(this, InvalidCredentialsError.prototype);
    }

    serializeErrors(): { message: string; field?: string; }[] {
        return [{ message: this.message }];
    }

}