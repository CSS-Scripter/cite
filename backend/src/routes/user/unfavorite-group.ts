import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { BadRequestError } from '../../errors/bad-request-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';

const router = express.Router();

const validation = [
    body('groupId')
        .isMongoId()
        .withMessage('GroupId is required')
];

router.post('/api/me/groups/unfavorite', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {
    const { groupId } = req.body;
    const group = await Group.findById(groupId);
    if (!group) {
        throw new NotFoundError();
    }

    if (req.currentUser!.favoriteGroups.indexOf(group._id) === -1) {
        throw new BadRequestError('Group was not favorited');
    }

    const index = req.currentUser!.favoriteGroups.indexOf(group._id);
    req.currentUser!.favoriteGroups.splice(index, 1);
    await req.currentUser!.save();

    res.status(200).send({ favoriteGroups: req.currentUser!.favoriteGroups });
});

export { router as unfavoriteGroupRouter };
