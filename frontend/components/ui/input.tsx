"use client";

import { InputHTMLAttributes, forwardRef } from "react";

type Props = InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  error?: string;
};

export const Input = forwardRef<HTMLInputElement, Props>(
  ({ label, error, className = "", ...props }, ref) => {
    return (
      <label className="grid gap-2">
        <span className="text-sm font-medium text-ink">{label}</span>
        <input
          ref={ref}
          className={`rounded-md border border-slate-200 bg-white px-3 py-3 text-sm text-ink placeholder:text-slate focus:outline-none focus:ring-2 focus:ring-ink/20 ${className}`}
          {...props}
        />
        {error ? <span className="text-sm text-coral">{error}</span> : null}
      </label>
    );
  }
);
