import express, { Request, Response } from 'express';
import { param } from 'express-validator';
import { ForbiddenError } from '../../errors/forbidden-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';
import { Quote } from '../../models/quote';

const router = express.Router();

const validation = [
  param('groupId')
    .isMongoId()
    .withMessage('Group Id must be a valid ID')
]

router.get('/api/quotes/:groupId', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {

    const { groupId } = req.params;
    
    const groupObject = await Group.findById(groupId);

    if (!groupObject) {
        throw new NotFoundError();
    }

    if(!Group.isUserMember(groupObject, `${req.currentUser!._id}`)) {
      throw new ForbiddenError()
    }
    
    const quotes = await Quote.find({group: groupObject._id}).populate('quoter').sort({createdAt: -1})

    res.status(200).send({quotes});
})

export { router as listQuoteRouter };

