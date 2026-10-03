import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';
import { User } from '../../models/user';

const router = express.Router();

const validation = [
  body('name')
    .trim()
    .not()
    .isEmpty()
    .withMessage('Name cannot be empty')
    .isLength({ max: 20 })
    .withMessage('Name has to between 1-20 characters.')
];

router.post('/api/groups', validation, validateRequest, requireAuth, async (req: Request, res: Response) => {
  const { name } = req.body;
  const user = await User.findById(req.currentUser!.id);
  const group = Group.build({
    name,
    owner: user!,
    admins: [user!],
    members: [user!]
  });
  await group.save();

  res.status(201).send({group});
});

export { router as newGroupRouter };

