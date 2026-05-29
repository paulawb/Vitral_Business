export type AnalyticsSummary = {
  visits: number;
  conversions: number;
  whatsappClicks: number;
  topProducts: Array<Record<string, string | number>>;
  peakHours: Array<Record<string, string | number>>;
};

export type AnalyticsEvent = {
  tenantId: string;
  eventType: string;
  resourceId?: string;
  resourceName?: string;
  channel?: string;
};
