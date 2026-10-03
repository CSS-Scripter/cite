import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';
import { Request as RequestModel } from '../../models/request';

const router = express.Router();

router.delete('/api/users/me/requests/:requestId', requireAuth, async (req: Request, res: Response) => {
  const { requestId } = req.params;
  if (!mongoose.Types.ObjectId.isValid(requestId)) {
    throw new NotFoundError();
  }

  const request = await RequestModel.findById(requestId).populate('user');
  if (!request) {
    throw new NotFoundError();
  }

  if(request.user.id !== req.currentUser!.id) {
    throw new NotFoundError();
  }

  await request.delete();
  res.status(200).send({});
});

export { router as deleteRequestRouter };
