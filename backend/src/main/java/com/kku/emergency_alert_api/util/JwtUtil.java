package com.kku.emergency_alert_api.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/*
 * Utilities คือ Lib หรือ Helper fn ที่ให้ทุกคนมาเรียกใช้ได้ เช่น Controller, Service, Repository
*/

@Component
public class JwtUtil {
    /*
     * กำหนด secret key
     * 
     * หมายเหตุ @Value คือ การดึงค่าจาก application.properties
     * ex @Value("${<key>:<defaultValue>}")
     * defaultValue คือ ค่าเริ่มต้น ถ้าใน application.properties ไม่ได้กำหนด
     */
    @Value("${jwt.secret}")
    private String secret;

    /*
     * กำหนด วันหมดอายุ
     * ex. expiration = 86400000 // คือ 24 ชัวโมง
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    // เข้ารหัส secret key
    private SecretKey getSigninKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // สร้าง token
    public String generateToken(String data) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(data) // ข้อมูลที่ต้องการเก็บ
                .issuedAt(now) // เวลาปัจจุบัน
                .expiration(expiryDate) // วันหมดอายุ
                .signWith(getSigninKey()) // secret key
                .compact(); // return
    }

    // get payload
    public String getDataFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // validate token
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // เช็คว่า token หมดอายุยัง
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
