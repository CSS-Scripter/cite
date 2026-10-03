import request from "supertest";
import { app } from "../../../app";

it('returns a 200 and invalidates the token', async () => {
  const jwt = await global.validSignin();

  await request(app)
    .get('/api/me')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);

  await request(app)
    .post('/api/auth/signout')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(200);

  await request(app)
    .get('/api/me')
    .auth(jwt, { type: 'bearer' })
    .send()
    .expect(401);
});