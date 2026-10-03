import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import mongoose from 'mongoose';
import { BadRequestError } from '../../errors/bad-request-error';
import { ForbiddenError } from '../../errors/forbidden-error';
import { NotFoundError } from '../../errors/not-found-error';
import { UnauthorizedError } from '../../errors/unauthorized-error';
import { currentUser } from '../../middleware/current-user';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Group } from '../../models/group';
import { Quote } from '../../models/quote';
import { User } from '../../models/user';

const router = express.Router();

const validation = [
  body('quote')
    .not()
    .isEmpty()
    .withMessage('Quote must be defined'),
  body('quotee')
    .not()
    .isEmpty()
    .withMessage('Quotee must be defined'),
  body('group')
    .isMongoId()
    .withMessage('Group must be a valid ID'),
]

router.post('/api/quotes/quote', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {

    const { quote, quotee, group } = req.body;

    const groupObject = await Group.findById(group);

    if (!groupObject) {
        throw new NotFoundError();
    }

    const exisitingQuote = await Quote.findOne({ quote, quotee, group: groupObject._id });

    if (exisitingQuote) {
        throw new BadRequestError('Quote already exist');
    }

    if(!Group.isUserMember(groupObject, `${req.currentUser!._id}`)) {
      throw new ForbiddenError();
    }
    
    const quoteObject = Quote.build({quote, quotee, group: groupObject._id, quoter: req.currentUser!});
    await quoteObject.save();

    res.status(201).send({quote: quoteObject});
})

export { router as quoteRouter };

