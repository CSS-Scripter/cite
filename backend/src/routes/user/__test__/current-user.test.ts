import request from 'supertest';
import { app } from '../../../app';

it('returns a 200 with user details on succesfull retrieval', async () => {
  const email = 'Valid@email.com';
  const username = 'ValidUsername';
  const password = 'ValidPassword';
  const bearerToken = await global.signin(email, username, password);

  const { body: { currentUser }} = await request(app)
    .get('/api/me')
    .auth(bearerToken, { type: 'bearer'})
    .send()
    .expect(200);
  
  expect(currentUser.email).toEqual(email);
  expect(currentUser.username).toEqual(username);
});

it('returns a 401 when the user is not authenticated', async () => {
  await request(app)
    .get('/api/me')
    .send()
    .expect(401);
});

it('returns a 401 when the user sends an invalid bearer token', async () => {
  await request(app)
    .get('/api/me')
    .auth('InvalidToken', { type: 'bearer' })
    .send()
    .expect(401);
});