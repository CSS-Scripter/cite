import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';
import mongoose from 'mongoose';
import { BadRequestError } from '../../errors/bad-request-error';

const router = express.Router();

const validation = [
    body('groupId')
        .isMongoId()
        .withMessage('Group ID must be a valid id')
];

router.post('/api/me/groups/leave', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {
    const { groupId } = req.body;

    const group = await Group.findById(groupId);

    if (!group) {
        throw new NotFoundError();
    }

    const currentUserId = `${req.currentUser!._id}`

    if (`${group.owner._id}` === currentUserId) {
        throw new BadRequestError('Owner is not allowed to leave the group');
    }

    const isUserMember = Group.isUserMember(group, currentUserId);
    const isUserAdmin = Group.isUserAdmin(group, currentUserId);

    if (isUserMember) group.members = group.members.filter((member) => `${member._id}` !== currentUserId);
    else throw new BadRequestError('User is not member of this group');
    if (isUserAdmin) group.admins = group.admins.filter((admin) => `${admin._id}` !== currentUserId);

    await group.save();

    res.status(204).send();
});

export { router as leaveGroupRouter }