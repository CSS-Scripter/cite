import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';
import request from 'supertest';
import { app } from '../app';

declare global {
    namespace NodeJS {
        interface Global {
            signin(email: string, username: string, password: string): Promise<string>;
            validSignin(): Promise<string>;
          }
    }
}

let mongo: MongoMemoryServer;

beforeAll(async () => {
  process.env.JWT_KEY = 'testingPurposes';
  process.env.JWT_EXPIRES_IN = '7d';

  mongo = new MongoMemoryServer();
  const mongoUri = await mongo.getUri();

  await mongoose.connect(mongoUri, {
    useNewUrlParser: true,
    useUnifiedTopology: true
  });
});

beforeEach(async () => {
  jest.clearAllMocks();
  const collections = await mongoose.connection.db.collections();

  for (let collection of collections) {
    collection.deleteMany({});
  }
});

afterAll(async () => {
  await mongoose.connection.close();
  await mongo.stop();
});

global.validSignin = () => {
  return global.signin('valid@email.com', 'validUsername', 'validPassword');
}

global.signin = async (email, username, password) => {
  const { body: { jwt }} = await request(app)
    .post('/api/auth/signup')
    .send({email, username, password})
    .expect(201);

  return jwt
};