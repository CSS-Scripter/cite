import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

const createGroup = async (jwt: string) => {
  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name: 'ValidGroupName' })
    .expect(201);

  return group;
}

it('returns a 201 on succesfull request creation', async () => {
  const jwt = await global.validSignin();
  const group = await createGroup(jwt);

  const jwt2 = await global.signin('some@email.com', 'someUsername', 'validpassword');
  const { body: { request: jr } } = await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(201);
  
  expect(jr).toBeDefined();
  expect(jr.user).toBeDefined();
  expect(jr.group.id).toEqual(group.id);
});

it('returns a 401 on unauthorized request', async () => {
  const jwt = await global.validSignin();
  const group = await createGroup(jwt);

  await request(app)
    .post(`/api/groups/${group.id}/join`)
    .send()
    .expect(401);
});

it('returns a 404 on invalid group id', async () => {
  const invalidId = 'InvalidId';

  const jwt = await global.validSignin();
  await request(app)
    .post(`/api/groups/${invalidId}/join`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 on non existing group id', async () => {
  const nonExistingId = new mongoose.Types.ObjectId();

  const jwt = await global.validSignin();
  await request(app)
    .post(`/api/groups/${nonExistingId}/join`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 400 on request from user that is already a member', async () => {
  const jwt = await global.validSignin();
  const group = await createGroup(jwt);

  await request(app)
    .post(`/api/groups/${group.id}/join`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(400);
});