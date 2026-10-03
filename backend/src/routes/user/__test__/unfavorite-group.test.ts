import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';

const createGroup = async (jwt: string, groupName: string) => {
    const { body: { group }} = await request(app)
        .post('/api/groups')
        .auth(jwt, { type: 'bearer' })
        .send({ name: groupName })
        .expect(201);
    return group;
};

const unfavoriteRequest = () => {
    return request(app)
        .post('/api/me/groups/unfavorite')
}

it('returns a 200 on succesfull unfavorite of a group', async () => {
    const jwt = await global.validSignin();
    const group = await createGroup(jwt, "groupName");

    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(200);

    
    await unfavoriteRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(200);

    const { body: { currentUser }} = await request(app)
        .get('/api/me')
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(200);

    expect(currentUser.favoriteGroups).toEqual([]);
});

it('returns a 401 on missing or invalid authorization', async () => {
    await unfavoriteRequest()
        .send({ groupId: 'someId'})
        .expect(401);
});


it('returns a 400 on missing groupId', async () => {
    const jwt = await global.validSignin();

    await unfavoriteRequest()
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(400);
});

it('returns a 400 on invalid groupId', async () => {
    const jwt = await global.validSignin();

    await unfavoriteRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: 'invalidGroupId' })
        .expect(400);
});

it('returns a 404 on non existing groupId', async () => {
    const jwt = await global.validSignin();
    const groupId = new mongoose.Types.ObjectId();

    await unfavoriteRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ groupId })
        .expect(404);
});

it('returns a 400 on groupId of group that is not in favorites', async () => {
    const jwt = await global.validSignin();
    const group = await createGroup(jwt, "groupName");
    
    await unfavoriteRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(400);
});
