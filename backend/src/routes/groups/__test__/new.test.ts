import request from 'supertest';
import { app } from '../../../app';

it('returns a 200 on succesfull creation', async () => {
  const email = 'Valid@email.com';
  const username = 'ValidUsername';
  const password = 'ValidPassword'
  const jwt = await global.signin(email, username, password);
  
  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({
      name: 'Valid Group Name',
    })
    .expect(201);
  
  expect(group.owner.email).toEqual(email);
  expect(group.owner.username).toEqual(username);

  expect(group.admins.length).toEqual(1);
  expect(group.admins[0].email).toEqual(email);
  expect(group.admins[0].username).toEqual(username);

  expect(group.members.length).toEqual(1);
  expect(group.members[0].email).toEqual(email);
  expect(group.members[0].username).toEqual(username);
});

it('returns a 400 on request with empty or missing name', async () => {
  const jwt = await global.validSignin();
  await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({
      name: ''
    })
    .expect(400);

  await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(400);
});

it('returns a 401 on request without authentication', async () => {
  await request(app)
    .post('/api/groups')
    .send({
      name: 'Valid Group Name'
    })
    .expect(401);
});
