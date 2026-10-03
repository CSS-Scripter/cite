import request from 'supertest';
import { app } from '../../../app';

it('returns a 201 on succesfull signup', async () => {
  const email = 'valid@email.com';
  const username = 'ValidUsername';
  const password = 'ValidPassword'

  const { body: { user, jwt }} = await request(app)
    .post('/api/auth/signup')
    .send({ email, username, password })
    .expect(201);
  
  expect(jwt).toBeDefined();
  expect(user).toBeDefined();
  expect(user.email).toEqual(email);
  expect(user.username).toEqual(username);
});

it('returns a 400 with an invalid or missing email', async () => {
  const invalidEmail = "invalid@email";
  const { body: { errors: invalidErrors } } = await request(app)
    .post('/api/auth/signup')
    .send({
      email: invalidEmail,
      username: 'Valid Username',
      password: 'Valid Password'
    })
    .expect(400);
  
  const { body: { errors: missingErrors } } = await request(app)
    .post('/api/auth/signup')
    .send({
      username: 'Valid Username',
      password: 'Valid Password'
    })
    .expect(400);
  
  expect(invalidErrors.length).toEqual(1);
  expect(invalidErrors[0].field).toEqual('email');

  expect(missingErrors.length).toEqual(1);
  expect(missingErrors[0].field).toEqual('email');
});

it('returns a 400 with a missing username', async () => {
  const { body: { errors } } = await request(app)
    .post('/api/auth/signup')
    .send({
      email: 'Valid@email.com',
      password: 'Valid Password'
    })
    .expect(400);
  
  expect(errors.length).toEqual(1);
  expect(errors[0].field).toEqual('username');
});

it('returns a 400 with a invalid or missing password', async () => {
  const invalidPassword = "not";
  const { body: { errors: invalidErrors } } = await request(app)
    .post('/api/auth/signup')
    .send({
      email: 'Valid@email.com',
      username: 'Valid Username',
      password: invalidPassword
    })
    .expect(400);
  
  const { body: { errors: missingErrors } } = await request(app)
    .post('/api/auth/signup')
    .send({
      email: 'Valid@email.com',
      username: 'Valid Username'
    })
    .expect(400);
  
  expect(invalidErrors.length).toEqual(1);
  expect(invalidErrors[0].field).toEqual('password');

  expect(missingErrors.length).toEqual(1);
  expect(missingErrors[0].field).toEqual('password');
});

it('returns a 400 with an duplicate email', async () => {
  const email = 'valid@email.com';
  const username = 'ValidUsername';
  const password = 'ValidPassword'

  await request(app)
    .post('/api/auth/signup')
    .send({ email, username, password })
    .expect(201);
  
  await request(app)
    .post('/api/auth/signup')
    .send({ email, username, password })
    .expect(400);
});
