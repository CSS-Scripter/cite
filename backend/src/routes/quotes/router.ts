import express from 'express';
import { deleteQuoteRouter } from './delete';
import { editQuoteRouter } from './edit';
import { likeRouter } from './like';
import { listQuoteRouter } from './list';
import { quoteRouter } from './quote';

const router = express.Router();

router.use(quoteRouter);
router.use(deleteQuoteRouter);
router.use(editQuoteRouter);
router.use(likeRouter);
router.use(listQuoteRouter);


export { router as quotesRouter };

