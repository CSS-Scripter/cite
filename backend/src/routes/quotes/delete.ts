import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { ForbiddenError } from '../../errors/forbidden-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group, GroupDoc } from '../../models/group';
import { Quote, QuoteDoc } from '../../models/quote';
import { UserDoc } from '../../models/user';

const router = express.Router();

const validation = [
  body('quoteId')
    .isMongoId()
    .withMessage('quoteId must be defined')
]

router.post('/api/quotes/delete', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {

    const { quoteId } = req.body;
    
    const quoteObject = await Quote.findById(quoteId);

    if (!quoteObject) {
        throw new NotFoundError();
    }

    const group = await Group.findById(quoteObject.group);

    if(!isUserQuoteOwner(quoteObject, req.currentUser!) && !isUserGroupAdmin(group!, req.currentUser!)) {
      throw new ForbiddenError();
    }
    
    await quoteObject.remove()

    res.status(204).send();
});

function isUserQuoteOwner(quote: QuoteDoc, user: UserDoc) {
  return `${user._id}` === `${quote.quoter._id}`;
}

function isUserGroupAdmin(group: GroupDoc, user: UserDoc) {
  return Group.isUserAdmin(group, `${user._id}`);
}

export { router as deleteQuoteRouter };

