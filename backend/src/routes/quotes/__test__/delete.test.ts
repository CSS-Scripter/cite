import request from 'supertest';
import { app } from '../../../app';
import { Group } from '../../../models/group';
import { User } from '../../../models/user';
import mongoose from 'mongoose';

const createGroup = async (jwt: string, name: string) => {
  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name })
    .expect(201);
  
  return group;
};

it('returns a 204 status on succesfull deletion as both quote owner and group admin', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao3');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

    await request(app)
      .post('/api/quotes/delete')
      .auth(bearerToken, { type: 'bearer' })
      .send({ quoteId: quoteRes.id })
      .expect(204);
});

it('returns a 204 as just quote owner', async () => {
  const quote = 'quote';
  const quotee = 'quotee';
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('testing@purposes.com', 'username', 'password');
  const group = await createGroup(jwt, 'Testing Purposes');

  const secondUser = await User.findOne({ email: 'testing@purposes.com' });
  const groupDoc = await Group.findById(group.id);
  groupDoc!.members.push(secondUser!);
  await groupDoc!.save();

  const { body: { quote: quoteResponse } } = await request(app)
    .post('/api/quotes/quote')
    .auth(jwt2, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);

  await request(app)
    .post('/api/quotes/delete')
    .auth(jwt2, { type: 'bearer' })
    .send({ quoteId: quoteResponse.id })
    .expect(204);
});

it('returns a 204 as just group admin', async () => {
  const quote = 'quote';
  const quotee = 'quotee';
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('testing@purposes.com', 'username', 'password');
  const group = await createGroup(jwt, 'Testing Purposes');

  const secondUser = await User.findOne({ email: 'testing@purposes.com' });
  const groupDoc = await Group.findById(group.id);
  groupDoc!.members.push(secondUser!);
  await groupDoc!.save();

  const { body: { quote: quoteResponse } } = await request(app)
    .post('/api/quotes/quote')
    .auth(jwt2, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);

  await request(app)
    .post('/api/quotes/delete')
    .auth(jwt, { type: 'bearer' })
    .send({ quoteId: quoteResponse.id })
    .expect(204);
});

it('returns 403 when the user is not admin or quote owner', async () => {
  const quote = 'quote';
  const quotee = 'quotee';
  const jwt = await global.validSignin();
  const jwt2 = await global.signin('testing@purposes.com', 'username', 'password');
  const group = await createGroup(jwt, 'Testing Purposes');

  const secondUser = await User.findOne({ email: 'testing@purposes.com' });
  const groupDoc = await Group.findById(group.id);
  groupDoc!.members.push(secondUser!);
  await groupDoc!.save();

  const { body: { quote: quoteResponse } } = await request(app)
    .post('/api/quotes/quote')
    .auth(jwt, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);

  await request(app)
    .post('/api/quotes/delete')
    .auth(jwt2, { type: 'bearer' })
    .send({ quoteId: quoteResponse.id })
    .expect(403);
});

it('returns 400 when the quote id is invalid or missing', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao3');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);
  
  await request(app)
    .post('/api/quotes/delete')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: 'invalidMongoId' })
    .expect(400);

  await request(app)
  .post('/api/quotes/delete')
  .auth(bearerToken, { type: 'bearer' })
  .send()
  .expect(400);
});

it('returns 404 when the quote id does not represent a quote', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const fakeQuoteId = new mongoose.Types.ObjectId();
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao3');

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);
  
  await request(app)
    .post('/api/quotes/delete')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: fakeQuoteId })
    .expect(404);
});


it('returns 401 when the user is not authenticated', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao3');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);
  
    await request(app)
      .post('/api/quotes/delete')
      .send({ quoteId: quoteRes.id })
      .expect(401);
});

