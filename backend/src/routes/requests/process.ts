import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { requireAuth } from '../../middleware/require-auth';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';
import { Group } from '../../models/group';
import { Request as RequestModel } from '../../models/request';
import { ForbiddenError } from '../../errors/forbidden-error';
import { validateRequest } from '../../middleware/validate-request';
import { User } from '../../models/user';

const router = express.Router();

const validation = [
  body('accepted')
    .isBoolean()
    .withMessage('Accepted is required')
];

router.post('/api/groups/:groupId/requests/:requestId', validation, validateRequest, requireAuth, async (req: Request, res: Response) => {
  const { groupId, requestId } = req.params;
  if (!(mongoose.Types.ObjectId.isValid(groupId) && mongoose.Types.ObjectId.isValid(requestId))) {
    throw new NotFoundError();
  }
  const group = await Group.findById(groupId);
  const request = await RequestModel.findById(requestId).populate('user');

  if (!group || !request || !Group.isUserMember(group, req.currentUser!.id)) {
    throw new NotFoundError();
  }

  if (!Group.isUserAdmin(group, req.currentUser!.id)) {
    throw new ForbiddenError();
  }

  const { accepted } = req.body;

  if (accepted) {
    const newUser = await User.findById(request.user.id);
    group.members.push(newUser!);
    await group.save();
  }

  request.delete();

  res.status(200).send({});
});

export { router as processRequestRouter };
