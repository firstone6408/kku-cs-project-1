"use client";
import { vars } from "nativewind";

// KKU Emergency Pin-Alert — Design System Theme
// Primary Brand Color: Orange #E8711A

export const config = {
  light: vars({
    // ─── Primary (Orange Brand) ───────────────────────────────────────────
    "--color-primary-0": "254 243 232", // #FEF3E8 — lightest orange tint
    "--color-primary-50": "253 224 196", // #FDE0C4
    "--color-primary-100": "251 195 139", // #FBC38B
    "--color-primary-200": "248 162 90", // #F8A25A
    "--color-primary-300": "244 133 55", // #F48537
    "--color-primary-400": "240 113 32", // #F07120
    "--color-primary-500": "232 113 26", // #E8711A — Brand Orange (Main)
    "--color-primary-600": "196 93 16", // #C45D10 — Orange Dark
    "--color-primary-700": "160 74 10", // #A04A0A
    "--color-primary-800": "122 56 8", // #7A3808
    "--color-primary-900": "85 39 6", // #552706
    "--color-primary-950": "52 24 4", // #341804

    // ─── Secondary (Neutral Gray) ──────────────────────────────────────────
    "--color-secondary-0": "253 253 253", // #FDFDFD
    "--color-secondary-50": "249 250 251", // #F9FAFB
    "--color-secondary-100": "243 244 246", // #F3F4F6
    "--color-secondary-200": "229 231 235", // #E5E7EB
    "--color-secondary-300": "209 213 219", // #D1D5DB
    "--color-secondary-400": "156 163 175", // #9CA3AF
    "--color-secondary-500": "107 114 128", // #6B7280
    "--color-secondary-600": "75 85 99", // #4B5563
    "--color-secondary-700": "55 65 81", // #374151
    "--color-secondary-800": "31 41 55", // #1F2937
    "--color-secondary-900": "17 24 39", // #111827
    "--color-secondary-950": "9 14 23", // #090E17

    // ─── Tertiary (Orange Soft — backgrounds/accents) ──────────────────────
    "--color-tertiary-0": "255 250 245", // #FFFAF5
    "--color-tertiary-50": "254 240 228", // #FEF0E4 — Orange Soft BG
    "--color-tertiary-100": "253 219 183", // #FDDBB7 — Orange Mid
    "--color-tertiary-200": "251 195 139", // #FBC38B
    "--color-tertiary-300": "248 162 90", // #F8A25A
    "--color-tertiary-400": "245 137 58", // #F5893A
    "--color-tertiary-500": "232 113 26", // #E8711A
    "--color-tertiary-600": "196 93 16", // #C45D10
    "--color-tertiary-700": "160 74 10", // #A04A0A
    "--color-tertiary-800": "122 56 8", // #7A3808
    "--color-tertiary-900": "85 39 6", // #552706
    "--color-tertiary-950": "52 24 4", // #341804

    // ─── Error (Red Alert) ─────────────────────────────────────────────────
    "--color-error-0": "255 245 245", // #FFF5F5
    "--color-error-50": "254 226 226", // #FEE2E2
    "--color-error-100": "254 202 202", // #FECACA
    "--color-error-200": "252 165 165", // #FCA5A5
    "--color-error-300": "248 113 113", // #F87171
    "--color-error-400": "239 68 68", // #EF4444
    "--color-error-500": "229 62 62", // #E53E3E — Red Alert
    "--color-error-600": "220 38 38", // #DC2626
    "--color-error-700": "185 28 28", // #B91C1C
    "--color-error-800": "153 27 27", // #991B1B
    "--color-error-900": "127 29 29", // #7F1D1D
    "--color-error-950": "83 19 19", // #531313

    // ─── Success (Green) ───────────────────────────────────────────────────
    "--color-success-0": "240 255 244", // #F0FFF4
    "--color-success-50": "198 246 213", // #C6F6D5
    "--color-success-100": "154 230 180", // #9AE6B4
    "--color-success-200": "104 211 145", // #68D391
    "--color-success-300": "72 187 120", // #48BB78
    "--color-success-400": "56 161 105", // #38A169 — Green Success
    "--color-success-500": "47 133 90", // #2F855A
    "--color-success-600": "39 103 73", // #276749
    "--color-success-700": "34 84 61", // #22543D
    "--color-success-800": "23 55 42", // #17372A
    "--color-success-900": "18 43 34", // #122B22
    "--color-success-950": "10 26 20", // #0A1A14

    // ─── Warning (Yellow/Amber) ────────────────────────────────────────────
    "--color-warning-0": "255 251 235", // #FFFBEB
    "--color-warning-50": "254 243 199", // #FEF3C7
    "--color-warning-100": "253 230 138", // #FDE68A
    "--color-warning-200": "252 211 77", // #FCD34D
    "--color-warning-300": "251 191 36", // #FBBF24
    "--color-warning-400": "245 158 11", // #F59E0B
    "--color-warning-500": "214 158 46", // #D69E2E — Yellow Warning
    "--color-warning-600": "180 126 34", // #B47E22
    "--color-warning-700": "146 94 22", // #925E16
    "--color-warning-800": "112 63 12", // #703F0C
    "--color-warning-900": "78 44 8", // #4E2C08
    "--color-warning-950": "48 27 5", // #301B05

    // ─── Info (Blue) ───────────────────────────────────────────────────────
    "--color-info-0": "235 248 255", // #EBF8FF
    "--color-info-50": "190 227 248", // #BEE3F8
    "--color-info-100": "144 205 244", // #90CDF4
    "--color-info-200": "99 183 240", // #63B7F0
    "--color-info-300": "66 153 225", // #4299E1
    "--color-info-400": "49 130 206", // #3182CE — Blue Info
    "--color-info-500": "43 108 176", // #2B6CB0
    "--color-info-600": "44 82 130", // #2C5282
    "--color-info-700": "42 67 101", // #2A4365
    "--color-info-800": "26 54 93", // #1A365D
    "--color-info-900": "17 36 60", // #11243C
    "--color-info-950": "9 20 34", // #091422

    // ─── Typography ────────────────────────────────────────────────────────
    "--color-typography-0": "255 255 255", // white
    "--color-typography-50": "249 250 251", // #F9FAFB
    "--color-typography-100": "243 244 246", // #F3F4F6
    "--color-typography-200": "229 231 235", // #E5E7EB
    "--color-typography-300": "209 213 219", // #D1D5DB
    "--color-typography-400": "156 163 175", // #9CA3AF
    "--color-typography-500": "107 114 128", // #6B7280
    "--color-typography-600": "75 85 99", // #4B5563
    "--color-typography-700": "55 65 81", // #374151
    "--color-typography-800": "31 41 55", // #1F2937
    "--color-typography-900": "17 24 39", // #111827
    "--color-typography-950": "9 14 23", // #090E17

    // ─── Outline ───────────────────────────────────────────────────────────
    "--color-outline-0": "255 255 255", // white
    "--color-outline-50": "249 250 251", // #F9FAFB
    "--color-outline-100": "243 244 246", // #F3F4F6
    "--color-outline-200": "229 231 235", // #E5E7EB
    "--color-outline-300": "209 213 219", // #D1D5DB
    "--color-outline-400": "156 163 175", // #9CA3AF
    "--color-outline-500": "107 114 128", // #6B7280
    "--color-outline-600": "75 85 99", // #4B5563
    "--color-outline-700": "55 65 81", // #374151
    "--color-outline-800": "31 41 55", // #1F2937
    "--color-outline-900": "17 24 39", // #111827
    "--color-outline-950": "9 14 23", // #090E17

    // ─── Background ────────────────────────────────────────────────────────
    "--color-background-0": "255 255 255", // #FFFFFF — Card / Surface
    "--color-background-50": "249 250 251", // #F9FAFB — App Background
    "--color-background-100": "243 244 246", // #F3F4F6 — Input Background
    "--color-background-200": "229 231 235", // #E5E7EB
    "--color-background-300": "209 213 219", // #D1D5DB
    "--color-background-400": "156 163 175", // #9CA3AF
    "--color-background-500": "107 114 128", // #6B7280
    "--color-background-600": "75 85 99", // #4B5563
    "--color-background-700": "55 65 81", // #374151
    "--color-background-800": "31 41 55", // #1F2937
    "--color-background-900": "17 24 39", // #111827
    "--color-background-950": "9 14 23", // #090E17

    // ─── Background Special ────────────────────────────────────────────────
    "--color-background-error": "255 245 245", // #FFF5F5 — Red soft bg
    "--color-background-warning": "255 251 235", // #FFFBEB — Yellow soft bg
    "--color-background-success": "240 255 244", // #F0FFF4 — Green soft bg
    "--color-background-muted": "249 250 251", // #F9FAFB — Muted gray
    "--color-background-info": "235 248 255", // #EBF8FF — Blue soft bg

    // ─── Focus Ring Indicator ──────────────────────────────────────────────
    "--color-indicator-primary": "232 113 26", // #E8711A — Orange brand
    "--color-indicator-info": "49 130 206", // #3182CE — Blue
    "--color-indicator-error": "229 62 62", // #E53E3E — Red
  }),

  dark: vars({
    // ─── Primary (Orange Brand — inverted for dark) ────────────────────────
    "--color-primary-0": "52 24 4", // darkest
    "--color-primary-50": "85 39 6",
    "--color-primary-100": "122 56 8",
    "--color-primary-200": "160 74 10",
    "--color-primary-300": "196 93 16", // #C45D10
    "--color-primary-400": "220 100 20",
    "--color-primary-500": "232 113 26", // #E8711A — Brand Orange
    "--color-primary-600": "240 130 50",
    "--color-primary-700": "245 160 85", // #F5A055
    "--color-primary-800": "251 195 139", // #FBC38B
    "--color-primary-900": "253 224 196", // #FDE0C4
    "--color-primary-950": "254 243 232", // lightest tint

    // ─── Secondary (Dark neutral) ──────────────────────────────────────────
    "--color-secondary-0": "9 14 23",
    "--color-secondary-50": "17 24 39", // #111827
    "--color-secondary-100": "31 41 55", // #1F2937
    "--color-secondary-200": "55 65 81", // #374151
    "--color-secondary-300": "75 85 99", // #4B5563
    "--color-secondary-400": "107 114 128", // #6B7280
    "--color-secondary-500": "156 163 175", // #9CA3AF
    "--color-secondary-600": "209 213 219", // #D1D5DB
    "--color-secondary-700": "229 231 235", // #E5E7EB
    "--color-secondary-800": "243 244 246", // #F3F4F6
    "--color-secondary-900": "249 250 251", // #F9FAFB
    "--color-secondary-950": "255 255 255", // white

    // ─── Tertiary ─────────────────────────────────────────────────────────
    "--color-tertiary-0": "52 24 4",
    "--color-tertiary-50": "85 39 6",
    "--color-tertiary-100": "122 56 8",
    "--color-tertiary-200": "160 74 10",
    "--color-tertiary-300": "196 93 16",
    "--color-tertiary-400": "232 113 26",
    "--color-tertiary-500": "245 137 58",
    "--color-tertiary-600": "248 162 90",
    "--color-tertiary-700": "251 195 139",
    "--color-tertiary-800": "253 219 183",
    "--color-tertiary-900": "254 240 228",
    "--color-tertiary-950": "255 250 245",

    // ─── Error ────────────────────────────────────────────────────────────
    "--color-error-0": "83 19 19",
    "--color-error-50": "127 29 29",
    "--color-error-100": "153 27 27",
    "--color-error-200": "185 28 28",
    "--color-error-300": "220 38 38",
    "--color-error-400": "229 62 62", // #E53E3E
    "--color-error-500": "239 68 68",
    "--color-error-600": "248 113 113",
    "--color-error-700": "252 165 165",
    "--color-error-800": "254 202 202",
    "--color-error-900": "254 226 226",
    "--color-error-950": "255 245 245",

    // ─── Success ──────────────────────────────────────────────────────────
    "--color-success-0": "10 26 20",
    "--color-success-50": "18 43 34",
    "--color-success-100": "23 55 42",
    "--color-success-200": "34 84 61",
    "--color-success-300": "39 103 73",
    "--color-success-400": "47 133 90",
    "--color-success-500": "56 161 105", // #38A169
    "--color-success-600": "72 187 120",
    "--color-success-700": "104 211 145",
    "--color-success-800": "154 230 180",
    "--color-success-900": "198 246 213",
    "--color-success-950": "240 255 244",

    // ─── Warning ──────────────────────────────────────────────────────────
    "--color-warning-0": "48 27 5",
    "--color-warning-50": "78 44 8",
    "--color-warning-100": "112 63 12",
    "--color-warning-200": "146 94 22",
    "--color-warning-300": "180 126 34",
    "--color-warning-400": "214 158 46", // #D69E2E
    "--color-warning-500": "245 158 11",
    "--color-warning-600": "251 191 36",
    "--color-warning-700": "252 211 77",
    "--color-warning-800": "253 230 138",
    "--color-warning-900": "254 243 199",
    "--color-warning-950": "255 251 235",

    // ─── Info ─────────────────────────────────────────────────────────────
    "--color-info-0": "9 20 34",
    "--color-info-50": "17 36 60",
    "--color-info-100": "26 54 93",
    "--color-info-200": "42 67 101",
    "--color-info-300": "44 82 130",
    "--color-info-400": "43 108 176",
    "--color-info-500": "49 130 206", // #3182CE
    "--color-info-600": "66 153 225",
    "--color-info-700": "99 183 240",
    "--color-info-800": "144 205 244",
    "--color-info-900": "190 227 248",
    "--color-info-950": "235 248 255",

    // ─── Typography ───────────────────────────────────────────────────────
    "--color-typography-0": "9 14 23",
    "--color-typography-50": "17 24 39",
    "--color-typography-100": "31 41 55",
    "--color-typography-200": "55 65 81",
    "--color-typography-300": "107 114 128",
    "--color-typography-400": "156 163 175",
    "--color-typography-500": "209 213 219",
    "--color-typography-600": "229 231 235",
    "--color-typography-700": "243 244 246",
    "--color-typography-800": "249 250 251",
    "--color-typography-900": "255 255 255",
    "--color-typography-950": "255 255 255",

    // ─── Outline ──────────────────────────────────────────────────────────
    "--color-outline-0": "9 14 23",
    "--color-outline-50": "17 24 39",
    "--color-outline-100": "31 41 55",
    "--color-outline-200": "55 65 81",
    "--color-outline-300": "107 114 128",
    "--color-outline-400": "156 163 175",
    "--color-outline-500": "209 213 219",
    "--color-outline-600": "229 231 235",
    "--color-outline-700": "243 244 246",
    "--color-outline-800": "249 250 251",
    "--color-outline-900": "253 253 253",
    "--color-outline-950": "255 255 255",

    // ─── Background ───────────────────────────────────────────────────────
    "--color-background-0": "17 24 39", // #111827 — App dark bg
    "--color-background-50": "31 41 55", // #1F2937 — Card dark
    "--color-background-100": "55 65 81", // #374151 — Input dark bg
    "--color-background-200": "75 85 99", // #4B5563
    "--color-background-300": "107 114 128", // #6B7280
    "--color-background-400": "156 163 175", // #9CA3AF
    "--color-background-500": "209 213 219", // #D1D5DB
    "--color-background-600": "229 231 235", // #E5E7EB
    "--color-background-700": "243 244 246", // #F3F4F6
    "--color-background-800": "249 250 251", // #F9FAFB
    "--color-background-900": "253 253 253", // near white
    "--color-background-950": "255 255 255", // white

    // ─── Background Special ────────────────────────────────────────────────
    "--color-background-error": "66 20 20", // dark red tint
    "--color-background-warning": "60 38 10", // dark amber tint
    "--color-background-success": "15 38 25", // dark green tint
    "--color-background-muted": "31 41 55", // dark gray card
    "--color-background-info": "15 30 50", // dark blue tint

    // ─── Focus Ring Indicator ──────────────────────────────────────────────
    "--color-indicator-primary": "245 160 85", // lighter orange for dark bg
    "--color-indicator-info": "99 183 240", // lighter blue
    "--color-indicator-error": "248 113 113", // lighter red
  }),
};
