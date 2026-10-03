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

it('returns a 200 on succesfull deletion', async () => {
  const jwt = await global.validSignin();
  const group = await createGroup(jwt);

  await request(app)
    .delete(`/api/groups/${group.id}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);
  
  const { body: { groups } } = await request(app)
    .get('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);
  
  expect(groups.length).toEqual(0);
});

it('returns a 401 on unauthorized request', async () => {
  const jwt = await global.validSignin();
  const group = await createGroup(jwt);

  await request(app)
    .delete(`/api/groups/${group.id}`)
    .send()
    .expect(401);
});

it('returns a 404 when the supplies id is invalid', async () => {
  const jwt = await global.validSignin();
  const invalidId = 'InvalidID';

  await request(app)
    .delete(`/api/groups/${invalidId}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 404 when the supplies id does not exist', async () => {
  const jwt = await global.validSignin();
  const nonExistingId = new mongoose.Types.ObjectId();

  await request(app)
    .delete(`/api/groups/${nonExistingId}`)
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(404);
});

it('returns a 403 when user is not the group owner', async () => {
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('some@email.com', 'validusername', 'validpassword');
  const group = await createGroup(jwt);

  await request(app)
    .delete(`/api/groups/${group.id}`)
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(403);
});
