import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

let jwt: string, group: any;

beforeEach(async () => {
    jwt = await global.validSignin();
    group = await createGroup();
    await createQuoteForGroup();
})

const createGroup = async () => {
    const { body: { group } } = await request(app)
        .post('/api/groups')
        .auth(jwt, { type: 'bearer' })
        .send({ name: 'teasting' })
        .expect(201);
    return group;
}

const createQuoteForGroup = async () => {
    await request(app)
        .post('/api/quotes/quote')
        .auth(jwt, { type: 'bearer' })
        .send({
            quote: 'testing',
            quotee: 'testing',
            group: group.id
        })
        .expect(201);
}

const createGetAllQuotesRequest = (groupId = group.id) => {
    return request(app)
        .get(`/api/quotes/${groupId}`)
}

it('returns 200 on succesful request', async () => {
    const { body: { quotes } } = await createGetAllQuotesRequest()
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(200);

    expect(quotes.length).toBe(1);
});

it('returns 401 on missing authorization', async () => {
    await createGetAllQuotesRequest()
        .send()
        .expect(401);
});

it('returns 400 on invalid group id', async () => {
    await createGetAllQuotesRequest('InvalidID')
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(400);
});

it('returns 404 on group id that does not represent a group', async () => {
    const fakeGroupId = new mongoose.Types.ObjectId();
    await createGetAllQuotesRequest(`${fakeGroupId}`)
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(404);
});

it('returns 403 when the user is not a member of the group', async () => {
    const jwt2 = await global.signin('testing@purposes.com', 'testing', 'testing');
    
    await createGetAllQuotesRequest()
    .auth(jwt2, { type: 'bearer' })
    .send()
    .expect(403);
});