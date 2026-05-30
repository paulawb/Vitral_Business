export function EmptyState({ title, description }: { title: string; description: string }) {
  return (
    <div className="panel flex min-h-48 flex-col items-center justify-center p-8 text-center">
      <h3 className="text-lg font-semibold text-ink">{title}</h3>
      <p className="mt-2 max-w-md text-sm text-slate">{description}</p>
    </div>
  );
}
