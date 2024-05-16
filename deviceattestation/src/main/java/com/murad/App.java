package com.murad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.sound.sampled.AudioFormat.Encoding;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.PublicKey;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        verifyCertificate(
                "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");

    }

    public static void verifyCertificate(String data) {
        byte[] encodedBytes = java.util.Base64.getDecoder().decode(data);
        System.out.println("Verifying certificate");
        ByteArrayInputStream bais = new ByteArrayInputStream(encodedBytes);
        try {
            List<DataItem> dataItems = new CborDecoder(bais).decode();
            for (DataItem dataItem : dataItems) {
                if (dataItem.getMajorType().compareTo(MajorType.MAP) != 0) {
                    System.out.println("Certificate verification failed");
                    return;
                }
                if (!(dataItem instanceof Map)) {
                    System.out.println("Certificate verification failed");
                    return;
                }
                Map dataItemMap = (Map) dataItem;
                for (DataItem key : dataItemMap.getKeys()) {
                    if (key.toString().contains("fmt") && !dataItemMap.get(key).toString().contains("appattest")) {
                        System.out.println("fmt should be appattest");
                        return;
                    }
                    if (key.toString().contains("attStmt")) {
                        Map attStmtMap = (Map) dataItemMap.get(key);

                        for (DataItem attStmtKey : attStmtMap.getKeys()) {
                            if (attStmtKey.toString().contains("x5c")) {
                                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                                Array x5cList = (Array) attStmtMap.get(attStmtKey);
                                for (DataItem x5c : x5cList.getDataItems()) {
                                    byte[] x5cBytes = ((ByteString) x5c).getBytes();
                                    X509Certificate cert = (X509Certificate) certFactory
                                            .generateCertificate(new ByteArrayInputStream(x5cBytes));
                                    if (!hasPublicKey(cert)) {
                                        System.out.println("Certificate verification failed");
                                        return;
                                    }
                                    // System.out.println(cert);
                                    // ,,.. store_ctx.verify_certificate()

                                }

                            }
                        }

                    }

                    if (key.toString().contains("authData")) {
                        ByteString authData = (ByteString) dataItemMap.get(key);
                        byte[] authDataBytes = authData.getBytes();

                        int startIndex = 33;
                        int endIndex = 37;

                        // Initialize the accumulator
                        int counter = 0;

                        // Iterate over the bytes in the specified range
                        for (int i = startIndex; i < endIndex; i++) {
                            counter = (counter << 8) | (authDataBytes[i] & 0xFF); // Using bitwise OR to avoid sign
                                                                                  // extension
                        }
                        int length = 0;
                        for (int i = 53; i < 55; i++) {
                            length = (length << 8) | (authDataBytes[i] & 0xFF); // Using bitwise OR to avoid sign extension
                        }

                        // Extract the credentialID based on the calculated length
                        byte[] credentialID = new byte[length];
                        for (int i = 0; i < length; i++) {
                            credentialID[i] = authDataBytes[55 + i];
                        }
                        String rpID = new String(authDataBytes, 0, 32, StandardCharsets.UTF_8);
                        String aaguid = new String(authDataBytes, 37, 53, StandardCharsets.UTF_8);
                        System.err.println("counter: " + counter);
                        System.err.println("rpID: " + rpID);
                        System.err.println("aaguid: " + aaguid); 
                        System.err.println("credentialID: " + new String(credentialID, StandardCharsets.UTF_8)); 
                        System.err.println("Octet: " + "Pending"); 
                        System.err.println("Nounce: " + "Pending");
                        
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("Certificate verification failed");
            e.printStackTrace();
        }

    }

    public static boolean hasPublicKey(X509Certificate certificate) {
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey != null;
    }

}