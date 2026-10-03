import express from 'express';
import { deleteRequestRouter } from './delete';
import { indexGroupRequestRouter } from './index-group';
import { indexUserRequestRouter } from './index-user';
import { newRequestRouter } from './new';
import { processRequestRouter } from './process';

const router = express.Router();

router.use(newRequestRouter);
router.use(indexUserRequestRouter);
router.use(indexGroupRequestRouter);
router.use(deleteRequestRouter);
router.use(processRequestRouter);

export { router as requestRouter };
