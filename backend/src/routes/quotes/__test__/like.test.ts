import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

let jwt: string, group: any, quote: any;

beforeEach(async () => {
    jwt = await global.validSignin();
    group = await createGroup(jwt, 'testingPurposes');
    quote = await createQuote(jwt, group.id);
});

const createGroup = async (jwt: string, name: string) => {
    const { body: { group } } = await request(app)
      .post('/api/groups')
      .auth(jwt, { type: 'bearer' })
      .send({ name })
      .expect(201);
    
    return group;
  };

const createQuote = async (jwt: string, groupId: string) => {
    const quote = 'TestingPurposes';
    const quotee = 'Testing';
  
    const { body: { quote: quoteRes } } = await request(app)
      .post('/api/quotes/quote')
      .auth(jwt, { type: 'bearer' })
      .send({
        quote, quotee, group: groupId
      })
      .expect(201);
    
    return quoteRes;
}

const createLikeRequest = () => {
    return request(app)
        .post('/api/quotes/like')
}

it('returns a 201 on valid request', async () => {
    const { body: { quote: quoteAfterLike } } = await createLikeRequest()
      .auth(jwt, { type: 'bearer' })
      .send({ quoteId: quote.id })
      .expect(201);
    
    const { body: { quote: quoteAfterUnlike } } = await createLikeRequest()
      .auth(jwt, { type: 'bearer' })
      .send({ quoteId: quote.id })
      .expect(201);
    
    expect(quoteAfterLike.likes.length).toEqual(1);
    expect(quoteAfterUnlike.likes.length).toEqual(0);
});

it('returns 400 on missing or invalid quote id', async () => {
    await createLikeRequest()
      .auth(jwt, { type: 'bearer' })
      .send()
      .expect(400);

    await createLikeRequest()
      .auth(jwt, { type: 'bearer' })
      .send({ quoteId: 'InvalidId' })
      .expect(400);
});

it('returns 401 on unauthorized request', async () => {
    await createLikeRequest()
      .send({ quoteId: quote.id })
      .expect(401);
});

it('returns 404 on quote id that does not represent a quote', async () => {
    const fakeQuoteId = new mongoose.Types.ObjectId();

    await createLikeRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ quoteId: fakeQuoteId})
        .expect(404);
});
