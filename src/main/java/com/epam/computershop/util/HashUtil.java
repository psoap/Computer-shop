package com.epam.computershop.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class HashUtil {
    private HashUtil() {
    }

    public static String getSha1(String string) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(string.getBytes());
        byte[] digest = sha.digest();
        return DatatypeConverter.printHexBinary(digest);
    }

    public static String getRandomMd5() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(System.currentTimeMillis());
        md.update(buffer.array());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
    }
}