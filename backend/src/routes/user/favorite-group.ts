import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { BadRequestError } from '../../errors/bad-request-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';
import { User } from '../../models/user';

const router = express.Router();

const validation = [
    body('groupId')
        .isMongoId()
        .withMessage("Group id is required")
]

router.post('/api/me/groups/favorite', validation, validateRequest, requireAuth, async (req: Request, res: Response) => {
    const { groupId } = req.body;
    const group = await Group.findById(groupId);
    if (!group || !Group.isUserMember(group, req.currentUser!.id)) {
        throw new NotFoundError();
    }

    if (req.currentUser!.favoriteGroups.indexOf(group._id) > -1) {
        throw new BadRequestError("Group already in favorites");
    }
    req.currentUser!.favoriteGroups.push(group);
    await req.currentUser!.save();


    const newUser = await User.findById(req.currentUser!.id)

    res.status(200).send({favoriteGroups: newUser!.favoriteGroups});
});

export { router as favoriteGroupRouter };

