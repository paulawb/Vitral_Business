"use client";

import { ButtonHTMLAttributes, ReactNode } from "react";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  children: ReactNode;
  busy?: boolean;
  variant?: "primary" | "secondary" | "ghost";
  block?: boolean;
};

export function Button({ children, busy, variant = "primary", block, className = "", ...props }: Props) {
  const variants = {
    primary: "bg-ink text-white hover:bg-night",
    secondary: "bg-white/14 text-white border border-white/20 hover:bg-white/18",
    ghost: "bg-slate-50 text-ink hover:bg-slate-100"
  };

  return (
    <button
      className={`focus-ring inline-flex items-center justify-center gap-2 rounded-md px-4 py-3 text-sm font-medium transition duration-200 disabled:cursor-not-allowed disabled:opacity-60 ${block ? "w-full" : ""} ${variants[variant]} ${className}`}
      disabled={busy || props.disabled}
      {...props}
    >
      {busy ? <span className="size-4 animate-spin rounded-full border-2 border-current border-t-transparent" /> : null}
      {children}
    </button>
  );
}
