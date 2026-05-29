import type { Metadata, Viewport } from "next";
import "./globals.css";
import { AppProviders } from "@/components/app-providers";
import { PwaProvider } from "@/components/pwa-provider";

export const metadata: Metadata = {
  title: "Vitral",
  description: "SaaS multi-tenant para catalogo, reservas y analitica",
  manifest: "/manifest.webmanifest"
};

export const viewport: Viewport = {
  themeColor: "#0B1220"
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="es">
      <body>
        <AppProviders>
          <PwaProvider />
          {children}
        </AppProviders>
      </body>
    </html>
  );
}
