package com.app.bank;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtKeyGeneratorTest {

    @Test
    public void generateKey(){

        SecretKey key = Jwts.SIG.HS512.key().build();

        System.out.println("Key: "+ DatatypeConverter.printHexBinary(key.getEncoded()));
    }

}
