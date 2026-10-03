import express, { Request, Response } from 'express';
import { requireAuth } from '../../middleware/require-auth';
import { Group } from '../../models/group';
import mongoose from 'mongoose';

const router = express.Router();

router.get('/api/groups', requireAuth, async (req: Request, res: Response) => {
  const currentUserId = req.currentUser!.id;

  const groups = await Group.find({members: { $elemMatch: { $eq: currentUserId }}}).populate('members').populate('admins').populate('owner');

  res.status(200).send({groups});
});

export { router as indexGroupRouter };
