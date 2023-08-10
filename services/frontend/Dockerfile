FROM node:17-alpine
COPY ["package.json", "package-lock.json*", "./"]
RUN npm install
COPY . .
EXPOSE 3000
CMD pwd
CMD ["npm", "start"]