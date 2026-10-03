import { Group, GroupDoc } from "../group";
import { User, UserDoc } from "../user";

let user: UserDoc;
let group: GroupDoc;

beforeEach(async () => {
    const email = 'testing@purposes.com';
    const username = 'testing';
    const password = 'testing';
    user = await User.build({ email, username, password, favoriteGroups: []}).save();
    group = await Group.build({ name: 'Testing', owner: user, admins: [user], members: [user] }).save();
});

describe('function isUserMember', () => {
    it('returns true when the user is a member', () => {
        expect(Group.isUserMember(group, `${user!._id}`)).toBe(true);
    });

    it('returns false when the user is not a member', async () => {
        const email = 'testing2@purposes.com';
        const username = 'testing2';
        const password = 'testing2';
        const user2 = await User.build({ email, username, password, favoriteGroups: []}).save();
        
        expect(Group.isUserMember(group, `${user2._id}`)).toBe(false);
    });
});


describe('function isUserAdmin', () => {
    it('returns true when the user is an admin', () => {
        expect(Group.isUserAdmin(group, `${user!._id}`)).toBe(true);
    });

    it('returns false when the user is not an admin', async () => {
        const email = 'testing2@purposes.com';
        const username = 'testing2';
        const password = 'testing2';
        const user2 = await User.build({ email, username, password, favoriteGroups: []}).save();
        
        expect(Group.isUserAdmin(group, `${user2._id}`)).toBe(false);
    });
});