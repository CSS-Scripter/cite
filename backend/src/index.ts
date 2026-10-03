require('dotenv').config()
import mongoose from 'mongoose';
import { app } from './app';
const requiredEnvVars = [
  'JWT_KEY',
  'JWT_EXPIRES_IN',
  'MONGO_URI'
];

const start = async () => {
  process.env.JWT_KEY = 'qwerty';
  process.env.JWT_EXPIRES_IN = '7d';
  process.env.MONGO_URI = 'mongodb://localhost:27017/quotes';

  for (let envVar of requiredEnvVars) {
    if (!process.env[envVar]) {
      throw new Error(`${envVar} must be defined`);
    }
  }
  
  try {
    await mongoose.connect(process.env.MONGO_URI!, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      useCreateIndex: true,
    });
    console.log('Connected to MongoDB');
  } catch (err) {
    console.error(err);
  }

  app.listen(3000, () => {
    console.log('Listening on port 3000');
  });
};

start();