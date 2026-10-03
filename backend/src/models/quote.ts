import mongoose from 'mongoose';
import { GroupDoc } from "./group";
import { UserDoc } from "./user";

interface QuoteAttrs {
  quote: string,
  quotee: string,
  quoter: UserDoc,
  group: GroupDoc
}

export interface QuoteDoc extends mongoose.Document {
  quote: string;
  quotee: string;
  quoter: UserDoc;
  group: GroupDoc;
  likes: mongoose.Types.ObjectId[];
}

interface QuoteModel extends mongoose.Model<QuoteDoc> {
  build(attrs: QuoteAttrs): QuoteDoc;
}

const quoteSchema = new mongoose.Schema({
  quote: {
    type: String,
    required: true
  },
  quotee: {
    type: String,
    required: true
  },
  quoter: {
    type: mongoose.Types.ObjectId,
    ref: 'User'
  },
  group: {
    type: mongoose.Types.ObjectId,
    ref: 'Group'
  },
  likes: [{
    type: mongoose.Types.ObjectId,
    ref: 'User'
  }]
}, {
  toJSON: {
    transform(doc, ret) {
      ret.id = ret._id;
      delete ret._id;
    }
  },
  timestamps: true
});

quoteSchema.statics.build = (attrs: QuoteAttrs) => {
  return new Quote(attrs);
}

const Quote = mongoose.model<QuoteDoc, QuoteModel>('Quote', quoteSchema);

export { Quote };

