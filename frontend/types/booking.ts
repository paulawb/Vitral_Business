export type Booking = {
  bookingId: string;
  tenantId: string;
  businessSlug: string;
  productId?: number;
  serviceName: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  bookingDate: string;
  startTime: string;
  endTime: string;
  status: string;
  notes?: string;
};

export type AvailabilitySlot = {
  startTime: string;
  endTime: string;
  available: boolean;
};
