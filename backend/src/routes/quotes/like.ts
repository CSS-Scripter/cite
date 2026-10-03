import express, { Request, Response } from 'express';
import { body } from 'express-validator';
import mongoose from 'mongoose';
import { NotFoundError } from '../../errors/not-found-error';
import { requireAuth } from '../../middleware/require-auth';
import { validateRequest } from '../../middleware/validate-request';
import { Quote } from '../../models/quote';

const router = express.Router();

const validation = [
  body('quoteId')
    .isMongoId()
    .withMessage('quoteId must be defined')
]

router.post('/api/quotes/like', requireAuth, validation, validateRequest, async (req: Request, res: Response) => {

    const { quoteId } = req.body;

    const quoteObject = await Quote.findById(quoteId).populate('quoter');

    if (!quoteObject) {
        throw new NotFoundError();
    }

    const userId = new mongoose.mongo.ObjectId(req.currentUser!.id);

    if(quoteObject.likes.includes(userId)) {
        quoteObject.likes.splice(quoteObject.likes.indexOf(userId), 1)
    } else {
        quoteObject.likes.push(userId)
    }

    await quoteObject.save()

    res.status(201).send({quote: quoteObject});
})

export { router as likeRouter };

