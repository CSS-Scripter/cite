import express, { Request, response, Response } from 'express';
import { body } from 'express-validator';
import { jwt } from '../../services/jwt';
import { InvalidCredentialsError } from '../../errors/invalid-credentials-error';
import { validateRequest } from '../../middleware/validate-request';

import { User } from '../../models/user';
import { Password } from '../../services/password';

const router = express.Router();

const validation = [
  body('email')
    .isEmail()
    .withMessage('Email must be valid'),
  body('password')
    .trim()
    .notEmpty()
    .withMessage('You must supply a password')
];

router.post('/api/auth/signin', validation, validateRequest, async (req: Request, res: Response) => {
  const { email, password } = req.body;

  const existingUser = await User.findOne({ email })
  if (!existingUser) {
    throw new InvalidCredentialsError();
  }
  
  const passwordsMatch = await Password.compare(existingUser.password, password)
  if (!passwordsMatch) {
    throw new InvalidCredentialsError();
  }

  const userJwt = jwt.sign({
    id: existingUser.id,
    email: existingUser.email,
    username: existingUser.username
  });

  const responseData = { user: existingUser, jwt: userJwt };

  res.status(200).send(responseData);
});

export { router as signinRouter };