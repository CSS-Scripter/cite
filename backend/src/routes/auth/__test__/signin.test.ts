import request from 'supertest';
import { app } from '../../../app';

it('responds with an 400 when an email that does not exist is supplied', async() => {
  return request(app)
    .post('/api/auth/signin')
    .send({
      email: 'unknown@email.com',
      password: 'validpassword'
    })
    .expect(400);
})

it('responds with an 400 when an incorrect password is supplied', async() => {
  await request(app)
    .post('/api/auth/signup')
    .send({
      email: 'valid@email.com',
      username: 'validUsername',
      password: 'validpassword'
    })
    .expect(201);

  return request(app)
    .post('/api/auth/signin')
    .send({
      email: 'valid@email.com',
      password: 'invalidpassword'
    })
    .expect(400);
})

it('responds with an user and jwt token when given valid credentials', async() => {
  await request(app)
    .post('/api/auth/signup')
    .send({
      email: 'valid@email.com',
      username: 'validUsername',
      password: 'validpassword'
    })
    .expect(201);

  const { body: { user, jwt }} = await request(app)
    .post('/api/auth/signin')
    .send({
      email: 'valid@email.com',
      password: 'validpassword'
    })
    .expect(200);

  expect(user).toBeDefined();
  expect(jwt).toBeDefined();
})