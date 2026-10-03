import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';
import { Group } from '../../models/group';
import { ForbiddenError } from '../../errors/forbidden-error';
import { Request as RequestModel } from '../../models/request';

const router = express.Router();

router.get('/api/groups/:groupId/requests', requireAuth, async (req: Request, res: Response) => {
  const { groupId } = req.params;
  if (!mongoose.Types.ObjectId.isValid(groupId)) {
    throw new NotFoundError();
  }

  const group = await Group.findById(groupId);
  if (!group || !Group.isUserMember(group, req.currentUser!.id)) {
    throw new NotFoundError();
  }

  if (!Group.isUserAdmin(group, req.currentUser!.id)) {
    throw new ForbiddenError();
  }

  const requests = await RequestModel.find({ group })
    .populate('group')
    .populate('user');
  res.status(200).send({requests});
});

export { router as indexGroupRequestRouter };
