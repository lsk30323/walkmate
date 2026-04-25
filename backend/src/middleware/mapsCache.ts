import { LRUCache } from 'lru-cache';

const SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;
const ONE_DAY = 24 * 60 * 60 * 1000;

export const geocodeCache = new LRUCache<string, unknown>({
  max: 5000,
  ttl: SEVEN_DAYS,
});

export const reverseGeocodeCache = new LRUCache<string, unknown>({
  max: 10_000,
  ttl: ONE_DAY,
});

/** 좌표를 약 11m 격자(소수점 4자리)로 라운딩하여 캐시 hit rate 향상 */
export function reverseGeocodeKey(lat: number, lng: number): string {
  return `${lat.toFixed(4)},${lng.toFixed(4)}`;
}
