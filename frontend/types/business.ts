export type Schedule = {
  dayOfWeek: string;
  openAt: string;
  closeAt: string;
  closed: boolean;
};

export type Business = {
  tenantId: string;
  slug: string;
  nombre: string;
  descripcion?: string;
  logoUrl?: string;
  primaryColor?: string;
  secondaryColor?: string;
  businessType?: string;
  vertical?: string;
  phone?: string;
  email?: string;
  whatsappNumber?: string;
  instagramUrl?: string;
  facebookUrl?: string;
  tiktokUrl?: string;
  timezone?: string;
  active?: boolean;
  schedules?: Schedule[];
};

export type BusinessInput = Omit<Business, "tenantId" | "slug"> & {
  slug?: string;
};
