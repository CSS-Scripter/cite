import { CustomError } from "./custom-error";

export class NotImplementedError extends CustomError {
  statusCode = 501;

  constructor() {
    super('Not Implemented');

    Object.setPrototypeOf(this, NotImplementedError.prototype);
  }

  serializeErrors(): { message: string; field?: string | undefined; }[] {
    return [{ message: this.message }]
  }

}