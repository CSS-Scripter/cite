import express from 'express';
import { signoutRouter } from './signout';
import { signupRouter } from './signup';
import { signinRouter } from './singin';

const router = express.Router();

router.use(signupRouter);
router.use(signinRouter);
router.use(signoutRouter);

export { router as authRouter };