FROM node:16-alpine AS builder
WORKDIR /services/frontend
COPY package.json package.json
COPY tsconfig.json tsconfig.json
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf *
COPY --from=builder /services/frontend/build .
ENTRYPOINT ["nginx", "-g", "daemon off;"]