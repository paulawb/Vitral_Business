import type { Config } from "tailwindcss";
 
const config: Config = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",
    "./hooks/**/*.{js,ts,jsx,tsx}",
    "./store/**/*.{js,ts,jsx,tsx}",
    "./utils/**/*.{js,ts,jsx,tsx}",
    "./styles/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        ink: "#101828",
        slate: {
          DEFAULT: "#344054",
          50: "#F8FAFC",
          200: "#E4E7EC"
        },
        sand: "#F8FAFC",
        mint: "#12B76A",
        coral: "#F97066",
        gold: "#FDB022",
        night: "#0B1220"
      },
      boxShadow: {
        panel: "0 18px 45px rgba(16, 24, 40, 0.12)",
        glow: "0 8px 24px rgba(15, 23, 42, 0.22)"
      },
      backgroundImage: {
        "hero-overlay":
          "linear-gradient(180deg, rgba(11,18,32,0.82) 0%, rgba(11,18,32,0.68) 35%, rgba(11,18,32,0.88) 100%)"
      }
    }
  },
  plugins: []
};

export default config;
