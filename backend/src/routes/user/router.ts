import express from 'express';
import { currentUserRouter } from './current-user';
import { favoriteGroupRouter } from './favorite-group';
import { leaveGroupRouter } from './leave-group';
import { unfavoriteGroupRouter } from './unfavorite-group';

const router = express.Router();

router.use(favoriteGroupRouter);
router.use(unfavoriteGroupRouter)
router.use(currentUserRouter);
router.use(leaveGroupRouter);

export { router as userRouter };