import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import { Group } from '../../models/group';
import { User } from '../../models/user';
import { Request as RequestModel } from '../../models/request';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';
import { BadRequestError } from '../../errors/bad-request-error';

const router = express.Router();

router.post('/api/groups/:groupId/join', requireAuth, async (req: Request, res: Response) => {
  const { groupId } = req.params;
  if (!mongoose.Types.ObjectId.isValid(groupId)) {
    throw new NotFoundError();
  }
  const group = await Group.findById(groupId).populate('members');
  if (!group) {
    throw new NotFoundError();
  }

  if (Group.isUserMember(group, req.currentUser!.id)) {
    throw new BadRequestError('User is already member of group');
  }

  const user = await User.findById(req.currentUser!.id);
  const request = RequestModel.build({ user: user!, group });
  await request.save();

  res.status(201).send({request});
});

export { router as newRequestRouter };
