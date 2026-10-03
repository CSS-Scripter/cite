import express, { Request, Response } from 'express';
import { jwt } from '../../services/jwt';
import { body } from 'express-validator';
import { validateRequest } from '../../middleware/validate-request';
import { BadRequestError } from '../../errors/bad-request-error';
import { User } from '../../models/user';

const router = express.Router();

const validation = [
  body('email')
    .isEmail()
    .withMessage('Email must be valid'),
  body('username')
    .not()
    .isEmpty()
    .withMessage('Username must be defined'),
  body('password')
    .trim()
    .isLength({min: 4, max: 20})
    .withMessage('Password must be between 4 and 20 characters')
]

router.post('/api/auth/signup', validation, validateRequest, async (req: Request, res: Response) => {
    const { email, username, password } = req.body;

    const existingUser = await User.findOne({ email });

    if (existingUser) {
        throw new BadRequestError('Email in use');
    }

    const user = User.build({email, username, password, favoriteGroups: []});
    await user.save();

    const jwtToken = jwt.sign({
        id: user.id,
        email: user.email,
        username: user.username
    });

    const responseData = { user, jwt: jwtToken };

    res.status(201).send(responseData);
})

export { router as signupRouter };