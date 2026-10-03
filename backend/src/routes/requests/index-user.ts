import express, { Request, Response } from 'express';
import { NotImplementedError } from '../../errors/not-implemented-error';
import { requireAuth } from '../../middleware/require-auth';
import { Request as RequestModel } from '../../models/request';
import { User } from '../../models/user';

const router = express.Router();

router.get('/api/users/me/requests', requireAuth, async (req: Request, res: Response) => {
  const user = await User.findById(req.currentUser!.id);
  const requests = await RequestModel.find({user: user!}).populate('group').populate('user');
  res.status(200).send({requests});
});

export { router as indexUserRequestRouter };
