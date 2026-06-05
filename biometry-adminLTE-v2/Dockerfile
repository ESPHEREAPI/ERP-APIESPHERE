# Stage 1: Build Angular 18
FROM node:20-alpine AS build

WORKDIR /app

# Installer les dépendances système nécessaires
RUN apk add --no-cache python3 make g++

# Copier package files
COPY package*.json ./

# Installer les dépendances (Angular 18 nécessite Node 18+)
RUN npm ci --legacy-peer-deps

# Copier le code source
COPY . .

# Build production
RUN npm run build -- --configuration=production

# Stage 2: Nginx
FROM nginx:alpine

# Copier configuration Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Supprimer le contenu par défaut
RUN rm -rf /usr/share/nginx/html/*

# Copier les fichiers buildés
COPY --from=build /app/dist/biometry-admin-lte-v2/browser /usr/share/nginx/html

# Créer un utilisateur non-root
RUN addgroup -g 1001 -S nodejs && \
    adduser -S angular -u 1001 && \
    chown -R angular:nodejs /usr/share/nginx/html && \
    chown -R angular:nodejs /var/cache/nginx && \
    chown -R angular:nodejs /var/log/nginx && \
    chown -R angular:nodejs /etc/nginx/conf.d

RUN touch /var/run/nginx.pid && \
    chown -R angular:nodejs /var/run/nginx.pid

USER angular

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/ || exit 1

CMD ["nginx", "-g", "daemon off;"]