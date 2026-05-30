"use client";

import { AnimatePresence, motion } from "framer-motion";
import { useEffect } from "react";
import { useToastStore } from "@/store/toast-store";

export function ToastViewport() {
  const items = useToastStore((state) => state.items);
  const remove = useToastStore((state) => state.remove);

  useEffect(() => {
    if (!items.length) {
      return;
    }
    const timers = items.map((item) => window.setTimeout(() => remove(item.id), 3600));
    return () => timers.forEach(window.clearTimeout);
  }, [items, remove]);

  return (
    <div className="pointer-events-none fixed right-4 top-4 z-50 grid gap-3">
      <AnimatePresence>
        {items.map((item) => (
          <motion.div
            key={item.id}
            initial={{ opacity: 0, y: -12 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -12 }}
            className={`pointer-events-auto w-[320px] rounded-lg border px-4 py-3 shadow-panel ${
              item.tone === "error" ? "border-coral/30 bg-white" : "border-mint/20 bg-white"
            }`}
          >
            <div className="text-sm font-semibold text-ink">{item.title}</div>
            {item.description ? <div className="mt-1 text-sm text-slate">{item.description}</div> : null}
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
}
