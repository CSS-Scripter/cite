import request from 'supertest';
import { app } from '../../../app';
import mongoose from 'mongoose';
import { User } from '../../../models/user';
import { Group } from '../../../models/group';

const email = 'testing@purposes.com';

let jwt: string, jwt2: string, group: any;

beforeEach(async () => {
    jwt = await global.validSignin();
    jwt2 = await global.signin(email, 'testingPurposes', 'testing');
    group = await createGroup(jwt, 'testingPurposes');
    
    const secondUser = await User.findOne({ email });
    const groupDoc = await Group.findById(group.id);
    groupDoc!.members.push(secondUser!);
    await groupDoc!.save();
})

const createGroup = async (jwt: string, name: string) => {
    const { body: { group }} = await request(app)
        .post('/api/groups')
        .auth(jwt, { type: 'bearer' })
        .send({ name })
        .expect(201);

    return group;
}

const createLeaveGroupRequest = () => {
    return request(app)
        .post('/api/me/groups/leave');
}

const makeSecondUserAdmin = async () => {
    const userDoc = await User.findOne({ email });
    const groupDoc = await Group.findById(group.id);
    groupDoc!.admins.push(userDoc!);
    await groupDoc!.save();
}

it('returns 204 on succesfull request from member', async () => {
    await createLeaveGroupRequest()
        .auth(jwt2, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(204);
    
    const groupDoc = await Group.findById(group.id);
    expect(groupDoc!.members.length).toBe(1);
    expect(groupDoc!.admins.length).toBe(1);
});

it('returns 200 on succesfull request from admin', async () => {
    await makeSecondUserAdmin();
    
    await createLeaveGroupRequest()
        .auth(jwt2, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(204);

    const groupDoc = await Group.findById(group.id);
    expect(groupDoc!.members.length).toBe(1);
    expect(groupDoc!.admins.length).toBe(1);
});

it('returns 401 on missin authorization headers', async () => {
    await createLeaveGroupRequest()
        .send({ groupId: group.id })
        .expect(401);
});

it('returns 400 on missing or invalid group id', async () => {
    await createLeaveGroupRequest()
        .auth(jwt2, { type: 'bearer' })
        .send({ groupId: 'InvalidGroupId' })
        .expect(400);

    await createLeaveGroupRequest()
        .auth(jwt2, { type: 'bearer' })
        .send()
        .expect(400);
});

it('returns 404 when the groupId does not represent a group', async () => {
    const fakeGroupId = new mongoose.Types.ObjectId();

    await createLeaveGroupRequest()
        .auth(jwt2, { type: 'bearer' })
        .send({ groupId: fakeGroupId })
        .expect(404);
});

it('returns 400 when the user is the group owner', async () => {
    await createLeaveGroupRequest()
        .auth(jwt, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(400);
});

it('returns 400 when the user is not a member of the group', async () => {
    const jwt3 = await global.signin('random@email.com', 'random', 'random');
    
    await createLeaveGroupRequest()
        .auth(jwt3, { type: 'bearer' })
        .send({ groupId: group.id })
        .expect(400);
})