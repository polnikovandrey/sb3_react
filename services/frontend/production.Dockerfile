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
EXPOSE 80
HEALTHCHECK --interval=60s --retries=5 --start-period=5s --timeout=10s CMD wget --no-verbose --tries=1 --spider localhost:80 || exit 1
ENTRYPOINT ["nginx", "-g", "daemon off;"]