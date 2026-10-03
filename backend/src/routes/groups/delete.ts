import express, { Request, Response } from 'express';
import { ForbiddenError } from '../../errors/forbidden-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { Group } from '../../models/group';
import mongoose from 'mongoose';

const router = express.Router();

router.delete('/api/groups/:groupId', requireAuth, async (req: Request, res: Response) => {
  const { groupId } = req.params;
  if (!mongoose.Types.ObjectId.isValid(groupId)) {
    throw new NotFoundError();
  }

  const group = await Group.findById(groupId).populate('owner');
  if(!group) {
    throw new NotFoundError();
  }
  if(group.owner.id !== req.currentUser!.id) {
    throw new ForbiddenError();
  }

  await group.delete();

  res.status(200).send({});
});

export { router as deleteGroupRouter };