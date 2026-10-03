import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';
import { User } from '../../../models/user';
import { Group } from '../../../models/group';

const createGroup = async (jwt: string, name: string) => {
  const { body: { group } } = await request(app)
    .post('/api/groups')
    .auth(jwt, { type: 'bearer' })
    .send({ name })
    .expect(201);
  
  return group;
}

it('returns 200 on succesfull edit', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quoteId: quoteRes.id, newQuoteText: 'sambal', newQuotee: 'sambal'
    })
    .expect(201);
});

it('returns 400 on missing request body parameters', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao222');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: quoteRes.id })
    .expect(400);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: quoteRes.id, newQuoteText: 'TestingPurposes' })
    .expect(400);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: quoteRes.id, newQuotee: 'TestingPurposes' })
    .expect(400);
});

it('returns 400 on invalid or missing quote id', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao222');

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ newQuoteText: 'TestingPurposes', newQuotee: 'TestingPurposes' })
    .expect(400);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: 'InvalidId', newQuoteText: 'TestingPurposes', newQuotee: 'TestingPurposes' })
    .expect(400);
});

it('returns 404 when the quote id does not represent a quote', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const fakeQuoteId = new mongoose.Types.ObjectId();
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao222');

  await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({
      quote, quotee, group: group.id
    })
    .expect(201);

  await request(app)
    .post('/api/quotes/edit')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quoteId: fakeQuoteId, newQuoteText: 'TestingPurposes', newQuotee: 'TestingPurposes' })
    .expect(404);
});

it('returns 401 on missing authorization', async () => {
  const quote = 'hello from jasminelmao';
  const quotee = 'jasminelmao';
  const bearerToken = await global.validSignin();
  const group = await createGroup(bearerToken, 'jasminelmao');

  const { body: { quote: quoteRes } } = await request(app)
    .post('/api/quotes/quote')
    .auth(bearerToken, { type: 'bearer' })
    .send({ quote, quotee, group: group.id })
    .expect(201);

  await request(app)
    .post('/api/quotes/edit')
    .send({ quoteId: quoteRes.id, newQuoteText: 'sambal', newQuotee: 'sambal' })
    .expect(401);
});

it('returns 403 when user is not quote owner', async () => {
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
    .post('/api/quotes/edit')
    .auth(jwt2, { type: 'bearer' })
    .send({ quoteId: quoteResponse.id, newQuoteText: 'sambal', newQuotee: 'sambal' })
    .expect(403);
});