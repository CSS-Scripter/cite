import mongoose from 'mongoose';

interface InvalidTokenAttrs {
  token: string
}

interface InvalidTokenDoc extends mongoose.Document {
  token: string;
}

interface InvalidTokenModel extends mongoose.Model<InvalidTokenDoc> {
  build(attrs: InvalidTokenAttrs): InvalidTokenDoc;
}

const invalidTokenSchema = new mongoose.Schema({
  token: {
    type: String,
    required: true,
    unique: true
  },
  createdAt: {
    type: Date,
    expires: process.env.JWT_EXPIRES_IN,
    default: Date.now
  }
});

invalidTokenSchema.statics.build = (attrs: InvalidTokenAttrs) => {
  return new InvalidToken(attrs);
}

const InvalidToken = mongoose.model<InvalidTokenDoc, InvalidTokenModel>('InvalidToken', invalidTokenSchema);

export { InvalidToken };
