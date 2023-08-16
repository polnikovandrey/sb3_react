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
HEALTHCHECK --start-period=5s --interval=30s --timeout=10s --retries=5 CMD wget --no-verbose --tries=1 --spider localhost:80 || exit 1
ENTRYPOINT ["nginx", "-g", "daemon off;"]