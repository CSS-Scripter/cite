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

it('returns 201 on succesfull request', async () => {
  const quote = 'hello from jasmine';
  const quotee = 'jasmine';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'JasminesGroup');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

    expect(quoteRes.quote).toEqual(quote)
    expect(quoteRes.quotee).toEqual(quotee)
    expect(quoteRes.group).toEqual(group.id)
});

it('returns 401 on unauthorized request', async () => {
  const quote = 'hello from jasmine';
  const quotee = 'jasmine';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'JasminesGroup');

  await request(app)
    .post('/api/quotes/quote')
    .send({quote, quotee, group: group.id})
    .expect(401)
});

it('returns a 400 on missing or invalid request arguments', async () => {
  const quote = '';
  const quotee = '';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'JasminesGroup');

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(400);

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({ group: group.id })
    .expect(400);
});

it('returns a 400 on existing quote', async () => {
  const quote = 'hello from jasmine';
  const quotee = 'jasmine';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'JasminesGroup');

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(400);
});

it('returns a 400 on invalid or missing', async () => {
  const quote = 'hello from jasmine';
  const quotee = 'jasmine';
  const bearerToken = await global.validSignin();

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee
    })
    .expect(400);

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: 'InvalidId'
    })
    .expect(400);
});

it('returns 404 on group id that does not represent a group', async () => {
  const quote = 'hello from jasmine';
  const quotee = 'jasmine';
  const fakeGroupId = new mongoose.Types.ObjectId();
  const bearerToken = await global.validSignin();
    
  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: fakeGroupId
    })
    .expect(404);
});

it('returns a 403 when user is not member of the group', async () => {
  const quote = 'quote';
  const quotee = 'quotee';
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('testing@purposes.com', 'username', 'password');
  const group = await createGroup(jwt, 'Testing Purposes');

  await request(app)
    .post('/api/quotes/quote')
    .auth(jwt2, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(403);
});
