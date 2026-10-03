import { Request, Response, NextFunction } from 'express';
import { jwt } from '../services/jwt';
import { UserDoc } from '../models/user';

declare global {
    namespace Express {
        interface Request {
            currentUser?: UserDoc
        }
    }
}

export const currentUser = async (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const authHeader = req.headers.authorization
    if (!authHeader) {
        return next();
    }

    try {
        const token = authHeader && authHeader.split(' ')[1]
        const payload = await jwt.verify(token);
        req.currentUser = payload || undefined;
    } catch(err) {}

    next();
}