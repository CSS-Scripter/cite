import request from 'supertest';
import { app } from '../app';

it('returns a 404 on invalid path', async () => {
  await request(app)
    .get('/this/is/not/a/valid/path')
    .send()
    .expect(404);
});
