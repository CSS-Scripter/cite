import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import { Group } from '../../models/group';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';

const router = express.Router();

router.get('/api/groups/:groupId', requireAuth, async (req: Request, res: Response) => {
  const { groupId } = req.params;
  if (!mongoose.Types.ObjectId.isValid(groupId)) {
    throw new NotFoundError();
  }
   
  const group = await Group.findById(groupId).populate('members').populate('admins').populate('owner');
  if (!group) {
    throw new NotFoundError();
  }

  // Is current user is not member of the group
  if (!Group.isUserMember(group, req.currentUser!.id)) {
    throw new NotFoundError();
  }

  res.status(200).send({group});
});

export { router as showGroupRouter };