import mongoose from 'mongoose';
import { GroupDoc } from './group';
import { UserDoc } from './user';

interface RequestAttrs {
  user: UserDoc,
  group: GroupDoc;
}

interface RequestDoc extends mongoose.Document {
  user: UserDoc;
  group: GroupDoc;
}

interface RequestModel extends mongoose.Model<RequestDoc> {
  build(attrs: RequestAttrs): RequestDoc;
}

const requestSchema = new mongoose.Schema({
  user: {
    type: mongoose.Types.ObjectId,
    ref: 'User'
  },
  group: {
    type: mongoose.Types.ObjectId,
    ref: 'Group'
  }
}, {
  toJSON: {
    transform(doc, ret) {
      ret.id = ret._id;
      delete ret._id;
    }
  }
});

requestSchema.statics.build = (attrs: RequestAttrs) => {
  return new Request(attrs);
}

const Request = mongoose.model<RequestDoc, RequestModel>('Request', requestSchema);

export { Request };