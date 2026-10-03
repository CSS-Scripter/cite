import express from 'express';
import { requireAuth } from '../../middleware/require-auth';

const router = express.Router();

router.get('/api/me', requireAuth, (req, res) => {
    res.send({ currentUser: req.currentUser! })
})

export { router as currentUserRouter };