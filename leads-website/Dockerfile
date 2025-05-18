FROM node:22.11.0-alpine

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./
COPY . .
RUN npm install
RUN npm run build

EXPOSE 3000

CMD ["npm", "start"]
