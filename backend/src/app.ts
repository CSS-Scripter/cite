import express from 'express';
require('express-async-errors');
import { json } from 'body-parser';
import cors from 'cors';
import { NotFoundError } from './errors/not-found-error';
import { currentUser } from './middleware/current-user';
import { errorHandler } from './middleware/error-handler';
import { authRouter } from './routes/auth/router';
import { groupRouter } from './routes/groups/router';
import { quotesRouter } from './routes/quotes/router';
import { requestRouter } from './routes/requests/router';
import { userRouter } from './routes/user/router';

const app = express();
app.use(cors())
app.set('trust proxy', true);
app.use(json());
app.use(currentUser);

app.use(authRouter);
app.use(groupRouter);
app.use(requestRouter);
app.use(quotesRouter);
app.use(userRouter);

app.all('*', () => {
    throw new NotFoundError();
});

app.use(errorHandler);

export { app };

