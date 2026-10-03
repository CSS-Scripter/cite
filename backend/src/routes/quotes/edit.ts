import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import { ForbiddenError } from '../../errors/forbidden-error';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Quote } from '../../models/quote';

const router = express.Router();

const validation = [
  body('quoteId')
    .isMongoId()
    .withMessage('quoteId must be defined'),
  body('newQuoteText')
    .not()
    .isEmpty()
    .withMessage('newQuoteText is required'),
  body('newQuotee')
    .not()
    .isEmpty()
    .withMessage('newQuotee is required'),  
]

router.post('/api/quotes/edit', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {

    const { quoteId, newQuoteText, newQuotee } = req.body;

    const quoteObject = await Quote.findById(quoteId).populate('quoter');

    if (!quoteObject) {
        throw new NotFoundError();
    }
    
    if(`${req.currentUser!._id}` !== `${quoteObject.quoter._id}`) {
      throw new ForbiddenError();
    }

    quoteObject.set({
      quote: newQuoteText,
      quotee: newQuotee
    });
    
    await quoteObject.save();

    res.status(201).send({ quote: quoteObject });
})

export { router as editQuoteRouter };

