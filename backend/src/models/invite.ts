import mongoose from 'mongoose';
import { GroupDoc } from './group';

interface InviteAttrs { 
  key: string,
  group: GroupDoc,
  validFor: number
}

interface InviteDoc extends mongoose.Document {
  key: string;
  group: GroupDoc;
  validFor: number;
}

interface InviteModel extends mongoose.Model<InviteDoc> {
  build(attrs: InviteAttrs): InviteDoc;
}

const inviteSchema = new mongoose.Schema({
  key: {
    type: String,
    required: true,
  },
  group: {
    type: mongoose.Types.ObjectId,
    ref: 'Group'
  },
  validFor: {
    type: Number,
    required: true
  }
}, {
  toJSON: {
    transform(doc, ret) {
      ret.id = ret._id;
      delete ret._id;
    }
  }
});

inviteSchema.statics.build = (attrs: InviteAttrs) => {
  return new Invite(attrs);
}

const Invite = mongoose.model<InviteDoc, InviteModel>('Invite', inviteSchema);

export { Invite };