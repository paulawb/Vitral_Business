import { Skeleton } from "@/components/ui/skeleton";
 
export default function Loading() {
  return (
    <div className="container-app py-8">
      <Skeleton className="h-64 w-full" />
    </div>
  );
}
