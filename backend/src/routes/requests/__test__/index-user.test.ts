import request from 'supertest';
import { app } from '../../../app';

it('returns a 200 with a list of request on valid request', async () => {
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
    .get('/api/users/me/requests')
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(200);
  
  expect(requests.length).toEqual(1);
  expect(requests[0].group.id).toEqual(group.id);
});

it('returns a 401 when the user is not validated', async () => {
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
    .get('/api/users/me/requests')
    .send()
    .expect(401);
});
