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

it('returns a 200 on succesfull request, and adds group to users favorites', async () => {
    const jwt = await global.validSignin();
    const group = await createGroup(jwt, 'testGroup')

    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(200);

    const { body: { currentUser }} = await request(app)
        .get('/api/me')
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(200);

    expect(currentUser.favoriteGroups).toEqual([group.id]);
});

it('returns a 401 on missing authorisation', async () => {
    const groupId = new mongoose.Types.ObjectId();
    await request(app)
        .post('/api/me/groups/favorite')
        .send({ groupId })
        .expect(401);
});

it('returns a 400 on missing request body', async () => {
    const jwt = await global.validSignin();
    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send()
        .expect(400);
});

it('returns a 404 when the user is not a member of the group', async () => {
    const jwt = await global.validSignin();
    const group = await createGroup(jwt, 'testGroup');

    const jwt2 = await global.signin('hi@test.nl', 'username', 'password');
    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt2, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(404);
});

it('returns a 404 when the group does not exist', async () => {
    const jwt = await global.validSignin();
    const groupId = new mongoose.Types.ObjectId();

    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send({ groupId })
        .expect(404);
});

it('returns a 400 when the group is already favorited', async () => {
    const jwt = await global.validSignin();
    const group = await createGroup(jwt, 'testGroup')

    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(200);
        
    await request(app)
        .post('/api/me/groups/favorite')
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(400);
});
