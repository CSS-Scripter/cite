import { Mongoose } from 'mongoose';
import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

const createGroup = async (jwt: string, name: string) => {
  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name })
    .expect(201);

  return group;
}

it('returns a 200 on succesfull get', async () => {
  const jwt = await global.validSignin();

  const group = await createGroup(jwt, 'ValidGroupName');

  const { body: { group: fetchedGroup } } = await request(app)
    .get(`/api/groups/${group.id}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);

  expect(fetchedGroup.id).toEqual(group.id);
  expect(fetchedGroup.name).toEqual(group.name);
});

it('returns a 404 on invalid id', async () => {
  const jwt = await global.validSignin();
  const invalidId = 'InvalidID';

  await request(app)
    .get(`/api/groups/${invalidId}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 on non existing id', async () => {
  const jwt = await global.validSignin();
  const nonExistingId = new mongoose.Types.ObjectId();

  await request(app)
    .get(`/api/groups/${nonExistingId}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when authorized user is not member of the group', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('another@email.com', 'anotherUsername', 'validpassword');

  const group = await createGroup(jwt, 'ValidGroupName');

  await request(app)
    .get(`/api/groups/${group.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(404);
});