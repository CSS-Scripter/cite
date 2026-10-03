import request from 'supertest';
import express from 'express';
import { errorHandler } from '../error-handler';

console.error = jest.fn();
beforeEach(() => {
    jest.clearAllMocks();
});

const path = '/testing/unknown-error';
const errorMessage = 'Unknown error';

const app = express();
app.get(path, () => {
    throw new Error(errorMessage);
});
app.use(errorHandler);

it('returns 500 with the error message on unknown error', async () => {
    const { body: { errors } } = await request(app)
        .get(path)
        .send()
        .expect(500);

    expect(errors.length).toBe(1);
    expect(errors[0].message).toBe(errorMessage);
});