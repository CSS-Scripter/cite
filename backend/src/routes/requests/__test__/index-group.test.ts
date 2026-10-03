import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

it('returns a 200 and list of requests on valid request', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroup' })
    .expect(201);

  await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);
  
  const { body: { requests } } = await request(app)
    .get(`/api/groups/${group.id}/requests`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);
  
  expect(requests.length).toBe(1);
});

it('returns a 401 on unauthorized request', async () => {
  const jwt = await global.validSignin();

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroup' })
    .expect(201);
  
  await request(app)
    .get(`/api/groups/${group.id}/requests`)
    .send()
    .expect(401);
});

it('returns a 403 when the user is a member, but not an admin', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');
  const jwt3 = await global.signin('someother@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroup' })
    .expect(201);

  const { body: { request: jr } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);

  await request(app)
    .post(`/api/groups/${group.id}/requests/${jr.id}`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(200);

  await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt3, { type: 'bearer' })
    .send()
    .expect(201);
  
  await request(app)
    .get(`/api/groups/${group.id}/requests`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(403);
});

it('returns a 401 on unauthorized request', async () => {
  const jwt = await global.validSignin();

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroup' })
    .expect(201);
  
  await request(app)
    .get(`/api/groups/${group.id}/requests`)
    .send()
    .expect(401);
});

it('returns a 404 when the given group id is invalid', async () => {
  const jwt = await global.validSignin();
  const invalidId = 'InvalidID'

  await request(app)
    .get(`/api/groups/${invalidId}/requests`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when the given group id does not represent a group', async () => {
  const jwt = await global.validSignin();
  const nonExistingId = new mongoose.Types.ObjectId();

  await request(app)
    .get(`/api/groups/${nonExistingId}/requests`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when the user is not member of the group', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroup' })
    .expect(201);

  await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);
  
  await request(app)
    .get(`/api/groups/${group.id}/requests`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(404);
});
