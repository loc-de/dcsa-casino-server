package com.polis.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@Tag("security")
public class PasswordServiceTest
{

    @Test
    void testHashIsNotNullAndNotEqualToPlain()
    {
        String plain = "mypassword123";
        String hashed = PasswordService.hash(plain);

        assertNotNull(hashed);
        assertNotEquals(plain, hashed);
    }

    @Test
    void testVerifyCorrectPassword()
    {
        String plain = "securePass";
        String hashed = PasswordService.hash(plain);

        assertTrue(PasswordService.verify(plain, hashed));
    }

    @Test
    void testVerifyIncorrectPassword()
    {
        String hashed = PasswordService.hash("originalPassword");

        assertFalse(PasswordService.verify("notPassword", hashed));
    }
}