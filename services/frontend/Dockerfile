FROM node:16-alpine
WORKDIR /services/frontend
COPY package.json ./
COPY package-lock.json ./
COPY tsconfig.json ./
COPY ./ ./
RUN npm install
EXPOSE 3000
CMD ["npm", "start"]