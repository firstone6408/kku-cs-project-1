# System Architecture — ระบบปักหมุดแจ้งเหตุฉุกเฉิน มข.

> Real-time Emergency Pin-Alert Mobile Application for Khon Kaen University

---

## 1. High-Level Architecture

```mermaid
graph TB
    subgraph Clients
        MOBILE["📱 Mobile App<br/>(React Native / Expo)<br/>ผู้แจ้งเหตุ + พนักงาน"]
        WEB["🖥️ Admin Web<br/>(Next.js)<br/>ผู้ดูแลระบบ"]
    end

    subgraph Backend ["☁️ Cloud VPS Server"]
        API["⚙️ Spring Boot API<br/>(REST + WebSocket)"]
        DB[("🗄️ PostgreSQL<br/>Database")]
    end

    subgraph External ["🌐 External Services"]
        GMAP["🗺️ Google Maps API<br/>แผนที่ + Geocoding"]
        IMGKIT["📸 ImageKit<br/>เก็บรูปภาพ / วิดีโอ"]
    end

    MOBILE -- "HTTPS (REST API)" --> API
    MOBILE -- "WSS (WebSocket)" --> API
    MOBILE -- "Maps SDK" --> GMAP
    WEB -- "HTTPS (REST API)" --> API
    API -- "JDBC" --> DB
    API -- "Upload API" --> IMGKIT
    IMGKIT -- "CDN URL" --> MOBILE
    IMGKIT -- "CDN URL" --> WEB
```

---

## 2. Tech Stack Summary

| Layer                 | Technology               | Version | หน้าที่                        |
| --------------------- | ------------------------ | ------- | ------------------------------ |
| **Mobile**            | React Native (Expo)      | SDK 55  | แอปผู้แจ้งเหตุ + พนักงาน       |
| **Mobile UI**         | NativeWind (TailwindCSS) | v4      | Styling                        |
| **Mobile Navigation** | Expo Router              | v55     | File-based routing             |
| **Admin Web**         | Next.js                  | v15     | Admin Dashboard                |
| **Web UI**            | Tailwind CSS             | v4      | Styling                        |
| **Backend**           | Spring Boot              | v3.5.11 | REST API + WebSocket Server    |
| **Language**          | Java                     | v21     | Backend language               |
| **Database**          | PostgreSQL               | latest  | ข้อมูลหลัก                     |
| **ORM**               | Spring Data JPA          | —       | Database access                |
| **Auth**              | JWT (jjwt)               | v0.12.3 | Authentication / Authorization |
| **API Docs**          | Swagger (springdoc)      | v2.8.13 | API Documentation              |
| **Maps**              | Google Maps API          | —       | แผนที่ + ปักหมุด + Geocoding   |
| **Media Storage**     | ImageKit                 | —       | เก็บรูปภาพ / วิดีโอ (CDN)      |
| **Utilities**         | Lombok                   | —       | Auto getter/setter             |
| **Image Processing**  | Thumbnailator            | v0.4.20 | Resize รูปก่อน upload          |

---

## 3. Architecture Pattern

```mermaid
graph LR
    subgraph "Mobile / Web (Client)"
        UI["UI Layer<br/>(Screens / Pages)"]
        SVC_CLIENT["Service Layer<br/>(API Calls)"]
        STATE["State Management<br/>(Context / Zustand)"]
    end

    subgraph "Spring Boot (Server)"
        CTRL["Controller<br/>(REST Endpoints)"]
        SVC["Service<br/>(Business Logic)"]
        REPO["Repository<br/>(JPA)"]
        ENT["Entity<br/>(DB Models)"]
        DTO["DTO<br/>(Request/Response)"]
    end

    subgraph "Data"
        PG[("PostgreSQL")]
    end

    UI --> STATE
    STATE --> SVC_CLIENT
    SVC_CLIENT -- "HTTP / WS" --> CTRL
    CTRL --> DTO
    CTRL --> SVC
    SVC --> REPO
    REPO --> ENT
    ENT --> PG
```

### Backend Layer Pattern (Spring Boot)

| Layer          | Package       | หน้าที่                                               |
| -------------- | ------------- | ----------------------------------------------------- |
| **Controller** | `controller/` | รับ HTTP Request, validate input, ส่งต่อ Service      |
| **DTO**        | `dto/`        | กำหนดรูปแบบ Request/Response (ไม่เปิดเผย Entity ตรงๆ) |
| **Service**    | `service/`    | Business Logic ทั้งหมด                                |
| **Repository** | `repository/` | Query ฐานข้อมูล (Spring Data JPA)                     |
| **Entity**     | `entity/`     | Java class ที่ map กับ table ใน DB                    |
| **Config**     | `config/`     | Security, WebSocket, CORS, ImageKit config            |
| **Exception**  | `exception/`  | Custom error handling                                 |

---

## 4. Authentication & Authorization Flow

```mermaid
sequenceDiagram
    participant C as 📱 Mobile / 🖥️ Web
    participant API as ⚙️ Spring Boot
    participant DB as 🗄️ PostgreSQL

    Note over C,DB: 🔐 Login Flow
    C->>API: POST /api/auth/login {email, password}
    API->>DB: Find user by email
    DB-->>API: User data
    API->>API: Verify password (BCrypt)
    API-->>C: {accessToken, refreshToken}

    Note over C,DB: 🔑 Authenticated Request
    C->>API: GET /api/incidents<br/>Header: Authorization: Bearer {token}
    API->>API: Validate JWT + Extract role
    API->>DB: Query data
    DB-->>API: Results
    API-->>C: Response (based on role)
```

### Role-Based Access

| Endpoint กลุ่ม                             | REPORTER | STAFF  | ADMIN  |
| ------------------------------------------ | :------: | :----: | :----: |
| `POST /api/incidents` (แจ้งเหตุ)           |    ✅    |   ❌   |   ❌   |
| `GET /api/incidents` (ดูรายการ)            |  ✅ own  | ✅ all | ✅ all |
| `POST /api/incidents/{id}/accept` (รับงาน) |    ❌    |   ✅   |   ❌   |
| `GET /api/teams` (จัดการทีม)               |    ❌    |   ✅   |   ✅   |
| `POST /api/reports` (report ผู้ใช้)        |    ✅    |   ✅   |   ❌   |
| `PUT /api/admin/users/{id}/block`          |    ❌    |   ❌   |   ✅   |
| `CRUD /api/admin/incident-types`           |    ❌    |   ❌   |   ✅   |

---

## 5. Real-time Communication (WebSocket)

```mermaid
sequenceDiagram
    participant R as 📱 ผู้แจ้งเหตุ
    participant API as ⚙️ Spring Boot<br/>(WebSocket)
    participant S as 📱 พนักงาน

    Note over R,S: 🆘 เมื่อมีการแจ้งเหตุใหม่
    R->>API: POST /api/incidents (สร้างรายการ)
    API-->>S: WS: NEW_INCIDENT (แจ้งเตือนทีม)

    Note over R,S: 💬 แชทในรายการแจ้งเหตุ
    R->>API: WS: SEND_MESSAGE {incidentId, message}
    API-->>S: WS: NEW_MESSAGE (broadcast ถึงทุกคนในห้อง)
    S->>API: WS: SEND_MESSAGE {incidentId, message}
    API-->>R: WS: NEW_MESSAGE

    Note over R,S: 🔄 อัปเดตสถานะ
    S->>API: PUT /api/incidents/{id}/status
    API-->>R: WS: STATUS_CHANGED
    API-->>S: WS: STATUS_CHANGED (broadcast)
```

### WebSocket ใช้ในกรณี:

1. **แจ้งเตือนเหตุใหม่** → ส่งไปทุกพนักงานที่ online
2. **แชท** → broadcast ข้อความในห้องแชทของรายการนั้น
3. **อัปเดตสถานะ** → แจ้งทุกฝ่ายเมื่อสถานะเปลี่ยน
4. **ขอทีมเพิ่ม** → แจ้งเตือนทีมอื่นว่ามีงานต้อง support

---

## 6. Media Upload Flow (ImageKit)

```mermaid
sequenceDiagram
    participant C as 📱 Mobile
    participant API as ⚙️ Spring Boot
    participant TH as 🖼️ Thumbnailator
    participant IK as 📸 ImageKit

    C->>API: POST /api/chat/upload<br/>(multipart file)
    API->>TH: Resize image (ถ้าเป็นรูป)
    TH-->>API: Resized image
    API->>IK: Upload file via ImageKit API
    IK-->>API: {fileId, url, thumbnailUrl}
    API-->>C: {fileUrl, fileType, fileSize}

    Note over C: แสดงรูป/วิดีโอจาก ImageKit CDN URL
    C->>IK: GET image/video via CDN URL
```

### ทำไมใช้ ImageKit?

- **CDN** — โหลดเร็วทั่วโลก
- **Image Transformation** — resize, crop on-the-fly ผ่าน URL params
- **Free tier** — 20GB bandwidth/เดือน (เพียงพอสำหรับโปรเจคจบ)
- **ไม่ต้องเก็บไฟล์ใน server** — ประหยัดพื้นที่ VPS

---

## 7. Google Maps Integration

```mermaid
graph LR
    subgraph "📱 Mobile App"
        MAP["react-native-maps<br/>(Google Maps SDK)"]
        LOC["expo-location<br/>(GPS)"]
    end

    subgraph "🌐 Google APIs"
        MAPS_SDK["Maps JavaScript/SDK"]
        GEOCODE["Geocoding API"]
    end

    subgraph "⚙️ Backend"
        API_INCIDENT["Incident Service"]
        DB_LOC[("lat, lng<br/>in incidents table")]
    end

    LOC -- "ดึงพิกัดปัจจุบัน" --> MAP
    MAP -- "แสดงแผนที่ + หมุด" --> MAPS_SDK
    MAP -- "ส่งพิกัด" --> API_INCIDENT
    API_INCIDENT -- "เก็บพิกัด" --> DB_LOC
    API_INCIDENT -- "แปลงพิกัดเป็นชื่อสถานที่" --> GEOCODE
```

### Google Maps ใช้ในกรณี:

1. **ปักหมุดตำแหน่งเหตุฉุกเฉิน** — ผู้แจ้งเหตุกดปักหมุด ส่ง lat/lng
2. **แสดงตำแหน่งเหตุบนแผนที่** — พนักงานเห็นหมุดเหตุทั้งหมด
3. **Geocoding** — แปลง lat/lng เป็นชื่อสถานที่ (เช่น "ตึก EN06 มข.")
4. **Direction** — (อนาคต) นำทางไปยังจุดเกิดเหตุ

---

## 8. Deployment Architecture

```mermaid
graph TB
    subgraph "☁️ Cloud VPS (590 บาท/เดือน)"
        NGINX["🌐 Nginx<br/>(Reverse Proxy)"]
        SPRING["⚙️ Spring Boot<br/>(Port 8080)"]
        NEXT["🖥️ Next.js<br/>(Port 3000)"]
        POSTGRES[("🗄️ PostgreSQL<br/>(Port 5432)")]
    end

    subgraph "External"
        IMGKIT2["📸 ImageKit CDN"]
        GMAP2["🗺️ Google Maps"]
    end

    USER_MOBILE["📱 Mobile App"] -- "HTTPS :443" --> NGINX
    USER_WEB["🖥️ Admin Browser"] -- "HTTPS :443" --> NGINX

    NGINX -- "/api/*" --> SPRING
    NGINX -- "/ws/*" --> SPRING
    NGINX -- "/*" --> NEXT
    SPRING --> POSTGRES
    SPRING --> IMGKIT2
    USER_MOBILE --> GMAP2
```

### Deployment Stack:

| Component       | ทำงานบน                                                                   |
| --------------- | ------------------------------------------------------------------------- |
| **Nginx**       | Reverse proxy, SSL termination, route `/api` → Spring Boot, `/` → Next.js |
| **Spring Boot** | Run as JAR (port 8080)                                                    |
| **Next.js**     | `npm run start` (port 3000)                                               |
| **PostgreSQL**  | ติดตั้งบน VPS เดียวกัน                                                    |
| **ImageKit**    | External CDN (ไม่ต้อง host เอง)                                           |
| **Google Maps** | External API (ใช้ผ่าน SDK ฝั่ง client)                                    |

---

## 9. Project Structure

```
FinalYearProject/
├── 📂 backend/                        (Spring Boot API)
│   └── src/main/java/com/kku/emergency_alert_api/
│       ├── config/                    Security, WebSocket, CORS, ImageKit
│       ├── controller/                REST API endpoints
│       ├── dto/                       Request / Response objects
│       ├── entity/                    JPA Entities (ตาม ER Diagram)
│       ├── exception/                 Custom exceptions
│       ├── repository/                JPA Repositories
│       ├── service/                   Business logic
│       └── util/                      JWT, file helpers
│
├── 📂 mobile/                         (React Native / Expo)
│   ├── app/                           Screens (file-based routing)
│   │   ├── (auth)/                    Login, Register
│   │   ├── (reporter)/                หน้าจอผู้แจ้งเหตุ
│   │   └── (staff)/                   หน้าจอพนักงาน
│   ├── components/                    Reusable UI components
│   ├── services/                      API calls + WebSocket
│   ├── hooks/                         Custom hooks
│   ├── context/                       Auth context, Socket context
│   ├── constants/                     Colors, API URLs
│   └── utils/                         Helpers
│
├── 📂 web/                            (Next.js Admin)
│   └── src/app/
│       ├── dashboard/                 Overview + statistics
│       ├── incidents/                 จัดการรายการแจ้งเหตุ
│       ├── users/                     จัดการ reporters + staff
│       ├── incident-types/            จัดการประเภทเหตุ
│       └── reports/                   Review user reports
│
└── 📂 docs/                           Documentation
    ├── er-diagram.md
    └── system-architecture.md
```
