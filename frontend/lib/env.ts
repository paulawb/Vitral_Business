export const env = {
  authApi: process.env.NEXT_PUBLIC_AUTH_API_URL ?? "http://localhost:8080",
  catalogApi: process.env.NEXT_PUBLIC_CATALOG_API_URL ?? "http://localhost:8081",
  businessApi: process.env.NEXT_PUBLIC_BUSINESS_API_URL ?? "http://localhost:8082",
  bookingApi: process.env.NEXT_PUBLIC_BOOKING_API_URL ?? "http://localhost:8083",
  whatsappApi: process.env.NEXT_PUBLIC_WHATSAPP_API_URL ?? "http://localhost:8084",
  analyticsApi: process.env.NEXT_PUBLIC_ANALYTICS_API_URL ?? "http://localhost:8085"
};
