import express from 'express';
import { indexGroupRouter } from '.';
import { deleteGroupRouter } from './delete';
import { newGroupRouter } from './new';
import { showGroupRouter } from './show';

const router = express.Router();

router.use(newGroupRouter);
router.use(showGroupRouter);
router.use(indexGroupRouter);
router.use(deleteGroupRouter);

export { router as groupRouter };