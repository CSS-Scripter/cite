import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import { jwt } from '../../services/jwt';

const router = express.Router();

router.post('/api/auth/signout', requireAuth, async (req: Request, res: Response) => {
  const token = req.headers.authorization!.split(' ')[1];
  await jwt.signout(token);
  
  res.status(200).send({});
})

export { router as signoutRouter };