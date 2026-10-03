FROM node:alpine

WORKDIR /app
COPY package.json .
RUN npm install --only=prod
COPY . .

ENV JWT_KEY=qwerty
ENV MONGO_URI=mongoUri

CMD ["npm", "run", "start"]