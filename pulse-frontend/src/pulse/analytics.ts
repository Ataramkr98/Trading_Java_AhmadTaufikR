export type PulseEvent = "demo_started" | "order_previewed" | "order_filled" | "order_placed_pending" | "copy_followed";

export function trackPulseEvent(event: PulseEvent, properties?: Record<string, string | number | boolean>) {
  if (import.meta.env.DEV) {
    console.info(`[pulse:event] ${event}`, properties ?? {});
  }
}
