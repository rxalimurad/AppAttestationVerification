package com.murad;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;
import java.util.Base64;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import co.nstant.in.cbor.CborDecoder;
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

    static byte[] credCertBytes;
    static byte[] caCertBytes;

    static X509Certificate credCert;
    static X509Certificate caCert;

    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        verifyCertificate(
                "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",
                "88ls8hmteK9+odllnpjq+JwbnwV8nlnnkipWngi9xxE=",
                "TDTJJT9YLY.com.example.SaftyNetDemo");

    }

    private static void verifyCertificate(String data, String keyId, String appId) {
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
                    // Step 1
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
                                // Step 2
                                if (x5cList.getDataItems().size() != 2) {
                                    System.out.println("Certificate verification failed");
                                    return;
                                }
                                DataItem credCertStr = x5cList.getDataItems().get(0);
                                DataItem caCertStr = x5cList.getDataItems().get(1);
                                credCertBytes = ((ByteString) credCertStr).getBytes();
                                caCertBytes = ((ByteString) caCertStr).getBytes();
                                credCert = (X509Certificate) certFactory
                                        .generateCertificate(new ByteArrayInputStream(credCertBytes));
                                caCert = (X509Certificate) certFactory
                                        .generateCertificate(new ByteArrayInputStream(caCertBytes));
                                try {
                                    FileInputStream fis = new FileInputStream("Apple_App_Attestation_Root_CA.pem");
                                    X509Certificate appleRootCert = (X509Certificate) certFactory
                                            .generateCertificate(fis);
                                    fis.close();
                                    // Step 3
                                    credCert.verify(caCert.getPublicKey());
                                    // Step 4
                                    caCert.verify(appleRootCert.getPublicKey());
                                    System.out.println("Public Key verification successful");
                                } catch (Exception e) {
                                    System.out.println("Certificate verification failed");
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                    if (key.toString().contains("authData")) {
                        ByteString authData = (ByteString) dataItemMap.get(key);
                        byte[] authDataBytes = authData.getBytes();
                        String octetNodeOIDString = "1.2.840.113635.100.8.2";
                        // Step 5
                        byte[] nonce = nonce("6969".getBytes(), authDataBytes);
                        byte[] oct = credCert.getExtensionValue(octetNodeOIDString);
                        if (!(areEqual(nonce, getLast32Elements(oct)))) {
                            System.out.println("Nonce check failed");
                            System.out.println(nonce.length);
                            System.out.println(getLast32Elements(oct).length);
                            return;
                        } else {
                            System.out.println("Nonce check successful");
                        }
                        // Step 6
                        int counter = 0;
                        int startIndex = 33;
                        int endIndex = 37;
                        for (int i = startIndex; i < endIndex; i++) {
                            counter = (counter << 8) | (authDataBytes[i] & 0xFF); // Using bitwise OR to avoid sign

                        }
                        if (counter != 0) {
                            System.out.println("Counter check failed");
                            return;
                        }
                        // Step 7:AAGUID
                        // Determines if the app attest environment is development or production
                        //
                        // Step 8: credentialId
                        // Verify KeyIdentifier with credentialID

                        byte[] credentialIDByte = extractCredentialID(authDataBytes);
                       
                        byte[] decodedBytes = Base64.getDecoder().decode(keyId);
                      
                        if (!(areEqual(credentialIDByte, decodedBytes))) {
                            System.out.println("Credential ID check failed");
                            return;
                        } else {
                            System.out.println("Credential ID check successful");
                        } 

                        // step 9: rpID
                        // Compare rpID with appID

                        //print authData for debugging
                        // String str = new String(authDataBytes, StandardCharsets.UTF_8); // for UTF-8 encoding
                        // System.out.println(str);

                    }

                }

            }
        } catch (Exception e) {
            System.out.println("Certificate verification failed");
            e.printStackTrace();
        }

    }

    private static byte[] nonce(byte[] challenge, byte[] authenticatorData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Hash the challenge
        byte[] clientDataHash = digest.digest(challenge);

        // Concatenate authenticatorData and clientDataHash
        byte[] concatenatedData = new byte[authenticatorData.length + clientDataHash.length];
        System.arraycopy(authenticatorData, 0, concatenatedData, 0, authenticatorData.length);
        System.arraycopy(clientDataHash, 0, concatenatedData, authenticatorData.length, clientDataHash.length);

        // Hash the concatenated data
        return digest.digest(concatenatedData);
    }

    private static byte[] getLast32Elements(byte[] byteArray) {
        // Check if the array length is less than or equal to 32
        if (byteArray.length <= 32) {
            return byteArray; // Return the entire array
        } else {
            // Create a new byte array to store the last 32 elements
            byte[] last32 = new byte[32];
            // Copy the last 32 elements from the original array to the new array
            System.arraycopy(byteArray, byteArray.length - 32, last32, 0, 32);
            return last32; // Return the last 32 elements
        }
    }

    private static boolean areEqual(byte[] array1, byte[] array2) {
        // Check if both arrays are null
        if (array1 == null && array2 == null) {
            return true; // Both are null, so they are equal
        }
        // Check if one array is null while the other is not
        if (array1 == null || array2 == null) {
            return false; // One is null while the other is not, so they are not equal
        }
        // Check if the lengths of the arrays are equal
        if (array1.length != array2.length) {
            return false; // Lengths are different, so they are not equal
        }
        // Check if the contents of the arrays are equal
        return Arrays.equals(array1, array2);
    }

    private static byte[] extractCredentialID(byte[] bytes) {
        // Retrieve the two bytes that encode the length
        // of the credentialID as a UInt16.
        int length = ((bytes[53] & 0xFF) << 8) | (bytes[54] & 0xFF);

        // Extract the credentialID based on the calculated length
        return Arrays.copyOfRange(bytes, 55, 55 + length);
    }

}
