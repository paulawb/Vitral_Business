"use client";

import { TextareaHTMLAttributes, forwardRef } from "react";

type Props = TextareaHTMLAttributes<HTMLTextAreaElement> & {
  label: string;
  error?: string;
};

export const Textarea = forwardRef<HTMLTextAreaElement, Props>(
  ({ label, error, className = "", ...props }, ref) => {
    return (
      <label className="grid gap-2">
        <span className="text-sm font-medium text-ink">{label}</span>
        <textarea
          ref={ref}
          className={`focus-ring min-h-28 rounded-md border border-slate-200 bg-white px-3 py-3 text-sm text-ink placeholder:text-slate ${className}`}
          {...props}
        />
        {error ? <span className="text-sm text-coral">{error}</span> : null}
      </label>
    );
  }
);
