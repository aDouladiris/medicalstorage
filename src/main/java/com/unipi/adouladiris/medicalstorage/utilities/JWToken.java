package com.unipi.adouladiris.medicalstorage.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JWToken {

    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String ISSUER = "authServer.medicalstorage.adouladiris.unipi.com";
    private static final String SECRET_KEY = "test_web_server"; // For verifying signature

    private String encodedHeader;
    private JSONObject payload = new JSONObject();
    private String signature;

    // Construct headers with standard content of type: JwT and signature algorithm HS-256.
    private JWToken() {this.encodedHeader = encode(new JSONObject(JWT_HEADER));}

    public JWToken(Authentication authenticatedUser, String expMinutes) throws NoSuchAlgorithmException, InvalidKeyException {
        this();
        this.payload.put("iss", ISSUER);
        this.payload.put("sub", authenticatedUser.getName());
        this.payload.put("aud", collectionToJson(authenticatedUser.getAuthorities()));
        this.payload.put("exp", LocalDateTime.now().plusMinutes(Long.parseLong(expMinutes, 10)).toEpochSecond(ZoneOffset.UTC));
        this.payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        this.payload.put("jti", UUID.randomUUID().toString());
        this.signature = hmacSha256(this.encodedHeader + "." + encode(payload) );
    }

    // Parse an already generated token string from Bearer in Auth Headers to a newly constructed JwToken.
    public JWToken(String token) throws NoSuchAlgorithmException {
        this();
        String[] parts = token.split("\\.");
        // JwT must be three parts separated by dots.
        if (parts.length != 3) {throw new IllegalArgumentException("Invalid Token format");}
        // Check for standard headers context as created above => JWT_HEADER after base64 encoding.
        // Decoding headers => parts[0] will result at {"typ":"JWT","alg":"HS256"}, but JWT_HEADER string is containing escaping slashes \.
        // So we are going to encode JWT_HEADER again and compare them with the already base64 encoded headers.
        if (this.encodedHeader.equals(parts[0])) this.encodedHeader = parts[0];
        // If headers are different, throw exception as we only defined algorithm HS-256 encryption and JwT type Token.
        else throw new NoSuchAlgorithmException("JWT Header is Incorrect: " + parts[0]);
        // Extract claims from payload after base64 decoding.
        payload = new JSONObject(decode(parts[1]));
        if (payload.isEmpty()) throw new JSONException("Payload is Empty: ");
        if (!payload.has("exp")) throw new JSONException("Payload doesn't contain expiry " + payload);
        // Extract signature.
        signature = parts[2];
    }

    public boolean isValid() throws NoSuchAlgorithmException, InvalidKeyException {
        return payload.getLong("exp") > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) &&//token not expired
               signature.equals(hmacSha256(this.encodedHeader + "." + encode(payload) )); //signature matched
    }

    private String encode(byte[] bytes) {return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);}
    private String encode(JSONObject obj) {return encode(obj.toString().getBytes(StandardCharsets.UTF_8));}
    private String decode(String encodedString) {return new String(Base64.getUrlDecoder().decode(encodedString));}

    private String hmacSha256(String data) throws InvalidKeyException, NoSuchAlgorithmException {
            byte[] hash = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
    }

    @Override
    public String toString() {return encodedHeader + "." + encode(payload) + "." + signature;}

    private JSONArray collectionToJson(Collection collection) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        collection.forEach( role -> jsonArray.put(role) );
        return jsonArray;
    }

    public String getSubject() {return payload.getString("sub");}

    public Collection<GrantedAuthority> getAudience() {
        JSONArray arr = payload.getJSONArray("aud");
        StringBuilder audience = new StringBuilder();
        for (int i = 0; i < arr.length(); i++) {
            audience.append(arr.getString(i));
            audience.append(", ");
        }
        String audsToAppend = audience.toString().trim().replace(',', ' ').trim();
        return AuthorityUtils.commaSeparatedStringToAuthorityList(audsToAppend);
    }


}
