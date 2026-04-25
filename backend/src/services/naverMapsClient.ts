import axios, { AxiosInstance } from 'axios';

const BASE_URL = 'https://maps.apigw.ntruss.com';

export interface GeocodeAddress {
  roadAddress: string;
  jibunAddress: string;
  englishAddress: string;
  lat: number;
  lng: number;
}

export interface ReverseGeocodeResult {
  roadAddress: string | null;
  jibunAddress: string | null;
  region: string | null;
}

class NaverMapsClient {
  private http: AxiosInstance;
  private dailyCount = 0;
  private dailyResetAt = startOfNextDayKST();
  private readonly dailyQuota: number;

  constructor() {
    const id = process.env.NAVER_CLIENT_ID;
    const secret = process.env.NAVER_CLIENT_SECRET;
    if (!id || !secret) throw new Error('NAVER_CLIENT_ID / NAVER_CLIENT_SECRET not set');

    this.dailyQuota = Number(process.env.NAVER_DAILY_QUOTA ?? 30_000);
    this.http = axios.create({
      baseURL: BASE_URL,
      headers: {
        'X-NCP-APIGW-API-KEY-ID': id,
        'X-NCP-APIGW-API-KEY': secret,
      },
      timeout: 5000,
    });
  }

  async geocode(query: string): Promise<GeocodeAddress[]> {
    this.consumeQuota();
    const { data } = await this.http.get('/map-geocode/v2/geocode', { params: { query } });
    return (data.addresses ?? []).map((a: any) => ({
      roadAddress: a.roadAddress,
      jibunAddress: a.jibunAddress,
      englishAddress: a.englishAddress,
      lat: Number(a.y),
      lng: Number(a.x),
    }));
  }

  async reverseGeocode(lat: number, lng: number): Promise<ReverseGeocodeResult> {
    this.consumeQuota();
    const { data } = await this.http.get('/map-reversegeocode/v2/gc', {
      params: {
        coords: `${lng},${lat}`,
        orders: 'roadaddr,addr',
        output: 'json',
      },
    });
    const results = data.results ?? [];
    const road = results.find((r: any) => r.name === 'roadaddr');
    const addr = results.find((r: any) => r.name === 'addr');
    return {
      roadAddress: road ? formatAddress(road) : null,
      jibunAddress: addr ? formatAddress(addr) : null,
      region: addr?.region?.area1?.name ?? null,
    };
  }

  /** 오늘 호출량 / 한도 모니터링 */
  stats() {
    return {
      dailyCount: this.dailyCount,
      dailyQuota: this.dailyQuota,
      usageRatio: this.dailyCount / this.dailyQuota,
      resetAt: this.dailyResetAt.toISOString(),
    };
  }

  private consumeQuota() {
    if (Date.now() >= this.dailyResetAt.getTime()) {
      this.dailyCount = 0;
      this.dailyResetAt = startOfNextDayKST();
    }
    if (this.dailyCount >= this.dailyQuota) {
      const err = new Error('quota_exceeded');
      (err as any).status = 429;
      throw err;
    }
    this.dailyCount += 1;
  }
}

function formatAddress(r: any): string {
  const land = r.land ?? {};
  const name = r.region?.area1?.name ?? '';
  const a2 = r.region?.area2?.name ?? '';
  const a3 = r.region?.area3?.name ?? '';
  const num = land.number1 ? (land.number2 ? `${land.number1}-${land.number2}` : land.number1) : '';
  const road = land.name ? ` ${land.name}` : '';
  return [name, a2, a3, road, num].filter(Boolean).join(' ').trim();
}

function startOfNextDayKST(): Date {
  const now = new Date();
  const kstOffset = 9 * 60;
  const kstNow = new Date(now.getTime() + (kstOffset - now.getTimezoneOffset()) * 60_000);
  kstNow.setHours(24, 0, 0, 0);
  return new Date(kstNow.getTime() - (kstOffset - now.getTimezoneOffset()) * 60_000);
}

export const naverMapsClient = new NaverMapsClient();
