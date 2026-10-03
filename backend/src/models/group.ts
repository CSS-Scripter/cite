import mongoose from 'mongoose';
import { UserDoc } from './user';

interface GroupAttrs {
  name: string,
  owner: UserDoc,
  admins: UserDoc[],
  members: UserDoc[];
}

export interface GroupDoc extends mongoose.Document {
  name: string;
  owner: UserDoc;
  admins: UserDoc[];
  members: UserDoc[];
}

interface GroupModel extends mongoose.Model<GroupDoc> {
  build(attrs: GroupAttrs): GroupDoc;
  isUserAdmin(group: GroupDoc, userId: string): boolean;
  isUserMember(group: GroupDoc, userId: string): boolean;
}

const groupSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  owner: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  },
  admins: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  }],
  members: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  }]
}, {
  toJSON: {
    transform(doc, ret) {
      ret.id = ret._id;
      delete ret._id;
    }
  }
});

groupSchema.statics.build = (attrs: GroupAttrs) => {
  return new Group(attrs);
}

groupSchema.statics.isUserAdmin = (group: GroupDoc, userId: string) => {
  const foundAdmin = group.admins.find((admin) => `${admin}` === userId || admin.id === userId);
  return !!foundAdmin;
}

groupSchema.statics.isUserMember = (group: GroupDoc, userId: string) => {
  const foundUser = group.members.find((member) => `${member}` === userId || member.id === userId);
  return !!foundUser;
}

const Group = mongoose.model<GroupDoc, GroupModel>('Group', groupSchema);

export { Group };