package qrsoft.information.identity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import qrsoft.information.shared.dto.vo.R;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
	public static final long EXP_TIME = 120L;
	private static final String SECRET = "photovoltaic-solarweb-jwt-secret-key-2024-qrsoft";
	private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	public static String genToken(Map<String, Object> payload) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
				.setClaims(payload)
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + EXP_TIME * 60 * 1000))
				.signWith(KEY, SignatureAlgorithm.HS256)
				.compact();
	}

	public static R valid(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(KEY)
					.build()
					.parseClaimsJws(token)
					.getBody();
			Map<String, Object> map = new HashMap<>(claims);
			return R.ok(map);
		} catch (Exception e) {
			return R.fail(e.getMessage());
		}
	}
}
