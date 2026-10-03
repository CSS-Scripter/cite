import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

it('returns a 200 on succesfull request. Adds user to group with an accepted value of true', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .get(`/api/groups/${group.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(200);
});

it('returns a 200 on succesfull request. Doesn\'t add user to the group with an accepted value of false', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .send({ accepted: false })
    .expect(200);
  
  await request(app)
    .get(`/api/groups/${group.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 401 if the user is not authorized', async () => {
  const fakeGroupId = new mongoose.Types.ObjectId();
  const fakeRequestId = new mongoose.Types.ObjectId();
  await request(app)
    .post(`/api/groups/${fakeGroupId}/requests/${fakeRequestId}`)
    .send({ accepted: true })
    .expect(401);
});

it('returns a 403 when the user is member, but not admin', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');
  const jwt3 = await global.signin('someOther@email.com', 'validusername', 'validpassword');

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

  const { body: { request: jr2 } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt3, { type: 'bearer' })
    .send()
    .expect(201);
  
  await request(app)
    .post(`/api/groups/${group.id}/requests/${jr.id}`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(200);
  
  await request(app)
    .post(`/api/groups/${group.id}/requests/${jr2.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send({ accepted: true })
    .expect(403);
});

it('returns a 404 when the user is not a member', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .auth(jwt2, { type: 'bearer' })
    .send({ accepted: true })
    .expect(404);
});

it('returns a 404 when the provided group id is invalid', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .post(`/api/groups/INVALIDGROUPID/requests/${jr.id}`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(404);
});

it('returns a 404 when the provided group id does not represent a group', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');
  const nonExistingGroupId = new mongoose.Types.ObjectId();

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
    .post(`/api/groups/${nonExistingGroupId}/requests/${jr.id}`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(404);
});

it('returns a 404 when the provided request id is invalid', async () => {
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
    .post(`/api/groups/${group.id}/requests/INVALIDREQUESTID`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(404);
});

it('returns a 404 when the provided request id does not represent a request', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');
  const nonExistingRequestId = new mongoose.Types.ObjectId();

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
    .post(`/api/groups/${group.id}/requests/${nonExistingRequestId}`)
    .auth(jwt, { type: 'bearer' })
    .send({ accepted: true })
    .expect(404);
});

it('returns a 400 when there was no accepted value provided', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .send({ })
    .expect(400);
});

it('returns a 400 when the accepted value is invalid', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');

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
    .send({ accepted: 12 })
    .expect(400);
});
