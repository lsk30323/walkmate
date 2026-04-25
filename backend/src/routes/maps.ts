import { Router } from 'express';
import { z } from 'zod';
import { naverMapsClient } from '../services/naverMapsClient';
import { geocodeCache, reverseGeocodeCache, reverseGeocodeKey } from '../middleware/mapsCache';

const router = Router();

const geocodeQuery = z.object({ query: z.string().min(1).max(200) });
const reverseQuery = z.object({
  lat: z.coerce.number().min(33).max(43),
  lng: z.coerce.number().min(124).max(132),
});

/** GET /maps/geocode?query=서울특별시 송파구 석촌동 */
router.get('/geocode', async (req, res, next) => {
  try {
    const { query } = geocodeQuery.parse(req.query);
    const cached = geocodeCache.get(query);
    if (cached) return res.json({ cached: true, addresses: cached });
    const addresses = await naverMapsClient.geocode(query);
    geocodeCache.set(query, addresses);
    res.json({ cached: false, addresses });
  } catch (e) { next(e); }
});

/** GET /maps/reverse-geocode?lat=37.5036&lng=127.1037 */
router.get('/reverse-geocode', async (req, res, next) => {
  try {
    const { lat, lng } = reverseQuery.parse(req.query);
    const key = reverseGeocodeKey(lat, lng);
    const cached = reverseGeocodeCache.get(key);
    if (cached) return res.json({ cached: true, ...cached as object });
    const result = await naverMapsClient.reverseGeocode(lat, lng);
    reverseGeocodeCache.set(key, result);
    res.json({ cached: false, ...result });
  } catch (e) { next(e); }
});

/** GET /maps/_stats — Naver API 일일 할당량/사용량 모니터링 */
router.get('/_stats', (_req, res) => {
  res.json(naverMapsClient.stats());
});

export default router;
