"use client";

import { useState } from "react";
import { Input } from "@/components/ui/input";

const PRESET_COLORS = [
  "#3B82F6", // blue
  "#10B981", // emerald
  "#F59E0B", // amber
  "#EF4444", // red
  "#8B5CF6", // violet
  "#EC4899", // pink
  "#06B6D4", // cyan
  "#84CC16", // lime
  "#F97316", // orange
  "#6366F1", // indigo
];

interface ColorPickerProps {
  value?: string;
  onChange?: (color: string) => void;
  label?: string;
}

export function ColorPicker({ value, onChange, label }: ColorPickerProps) {
  const [customColor, setCustomColor] = useState(value || "#3B82F6");

  const handleColorSelect = (color: string) => {
    setCustomColor(color);
    onChange?.(color);
  };

  const handleCustomChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const color = e.target.value;
    setCustomColor(color);
    onChange?.(color);
  };

  return (
    <div className="space-y-2">
      {label && <label className="text-sm font-medium text-ink">{label}</label>}
      <div className="flex flex-wrap gap-2">
        {PRESET_COLORS.map((color) => (
          <button
            key={color}
            type="button"
            onClick={() => handleColorSelect(color)}
            className={`h-8 w-8 rounded-full border-2 transition ${
              customColor === color ? "border-ink scale-110" : "border-transparent"
            }`}
            style={{ backgroundColor: color }}
            aria-label={`Seleccionar color ${color}`}
          />
        ))}
      </div>
      <Input
        type="color"
        value={customColor}
        onChange={handleCustomChange}
        className="h-10 w-20"
      />
    </div>
  );
}