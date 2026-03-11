import { z } from "zod";

// ประเภทของ response จาก API
type ApiResult<T> = {
  error: {
    status: "success" | "error";
    errorMessage: string;
  };
  result: T;
};

// validate response ด้วย zod schema
function validateResponse<T>(data: unknown, schema: z.ZodSchema<T>): T {
  const parsed = schema.safeParse(data);
  if (!parsed.success) {
    console.error("Validation error:", parsed.error.message);
    throw new Error(parsed.error.message);
  }
  return parsed.data;
}

// template สำหรับ validate response จาก backend (format: { ok, status, message, data, timestamp })
export function templateValidateResponse<T>(dataSchema: z.ZodSchema<T>) {
  return z.object({
    ok: z.boolean(),
    status: z.number(),
    message: z.string(),
    data: dataSchema,
    timestamp: z.string(),
  });
}

/**
 * จัดการ API call แบบ centralized — รองรับ error handling + zod validation
 * ปรับจาก NextJS POS ให้ใช้ fetch แทน axios (เหมาะกับ React Native)
 *
 * ตัวอย่างการใช้:
 * const { result, error } = await withApiHandling(
 *   fetch(url, options),
 *   { validateResponse: templateValidateResponse(z.object({ token: z.string() })) }
 * );
 */
export async function withApiHandling<T>(
  request: Promise<Response> | (() => Promise<Response>),
  config?: {
    validateResponse?: z.ZodSchema<T>;
  }
): Promise<ApiResult<T>> {
  let status: ApiResult<T>["error"]["status"] = "success";
  let errorMessage = "No error";
  let result: T = undefined as T;

  try {
    // รองรับทั้ง function หรือ promise
    const response =
      typeof request === "function" ? await request() : await request;

    // ดึง body เป็น JSON
    const body = await response.json();

    // ถ้า HTTP status ไม่ OK → ดึง message จาก backend
    if (!response.ok) {
      throw new Error(body?.message || `HTTP ${response.status}`);
    }

    // validate response ด้วย zod (ถ้ามี schema)
    if (config?.validateResponse) {
      result = validateResponse(body, config.validateResponse);
    } else {
      result = body;
    }
  } catch (error: unknown) {
    status = "error";
    if (error instanceof Error) {
      errorMessage = error.message;
    } else {
      errorMessage = String(error);
    }
  }

  return { result, error: { status, errorMessage } };
}
