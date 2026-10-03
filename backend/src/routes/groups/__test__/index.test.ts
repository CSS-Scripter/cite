import request from 'supertest';
import { app } from '../../../app';

it('returns a 200 on succesfull retrieval', async () => {
  const jwt = await global.validSignin();

  const name = 'ValidGroupName';

  const {body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name })
    .expect(201);

  const { body: { groups } } = await request(app)
    .get('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);
  
  expect(groups[0].id).toEqual(group.id);
  expect(groups[0].name).toEqual(group.name);
});

it('returns a 401 when not authorized', async () => {
  await request(app)
    .get('/api/groups')
    .send()
    .expect(401);
})