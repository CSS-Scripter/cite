import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

it('returns a 200 on succesful deletion of the request', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('another@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'MockGroup' })
    .expect(201);

  const { body: { request: jr } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);

  await request(app)
    .delete(`/api/users/me/requests/${jr.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(200);

  const { body: { requests: jrs } } = await request(app)
    .get('/api/users/me/requests')
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(200)
  
  expect(jrs.length).toEqual(0);
});

it('returns a 401 when the user is not authenticated', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('another@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'MockGroup' })
    .expect(201);

  const { body: { request: jr } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);
  
  await request(app)
    .delete(`/api/users/me/requests/${jr.id}`)
    .send()
    .expect(401);
});

it('returns a 404 when the user is not the owner of the request', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('another@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'MockGroup' })
    .expect(201);

  const { body: { request: jr } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);

  await request(app)
    .delete(`/api/users/me/requests/${jr.id}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when the provided request id is invalid', async () => {
  const jwt = await global.validSignin();

  await request(app)
    .delete('/api/users/me/requests/INVALIDID')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when the provided request id does not represent a request', async () => {
  const jwt = await global.validSignin();
  const fakeId = new mongoose.Types.ObjectId();
  
  await request(app)
    .delete(`/api/users/me/requests/${fakeId}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});