package com.docscience.pathfinder.font.driver.type1;

/**
 * @author wxin
 *
 */
public abstract class Type1Encryption {

    public static final int ENCRYPT_C1 = 52845;

    public static final int ENCRYPT_C2 = 22719;

    public static final int CHARSTRING_ENCRYPT_KEY = 4330;

    public static final int EXEC_ENCRYPT_KEY = 55665;

    public static final int DEFAULT_LEADING_BYTES_NUMBER = 4;

    /**
     * Encrypt Type1 code.
     * 
     * @return key value after encrypt
     */
    public static int encrypt(byte[] src, byte[] enc, int r) {
        return encrypt(src, 0, enc, 0, src.length, r);
    }
       
    /**
     * Encrypt Type1 code.
     * 
     * @return key value after encrypt
     */
    public static int encrypt(byte[] src, int srcOffset, byte[] enc, int encOffset, int length, int r) {
        assert(srcOffset >= 0);
        assert(encOffset >= 0);
        assert(srcOffset + length <= src.length);
        assert(encOffset + length <= enc.length);
        for (int i = srcOffset, j = encOffset; i < srcOffset + length; ++i, ++j) {
            int t = (r >> 8) & 0xff;
            enc[j] = (byte) (src[i] ^ t);
            r = (((enc[j] & 0xff) + r) * ENCRYPT_C1 + ENCRYPT_C2) % 65536;
        }
        return r;
    }
    
    /**
     * Decrypt Type1 code.
     * 
     * @return key value after decrypt
     */
    public static int decrypt(byte[] enc, byte[] src, int r) {
        return decrypt(enc, 0, src, 0, enc.length, r);
    }
    
    /**
     * Decrypt Type1 code.
     * 
     * @return key value after decrypt
     */
    public static int decrypt(byte[] enc, int encOffset, byte[] src, int srcOffset, int length, int r) {
        assert(srcOffset >= 0);
        assert(encOffset >= 0);
        assert(srcOffset + length <= src.length);
        assert(encOffset + length <= enc.length);
        for (int i = srcOffset, j = encOffset; i < srcOffset + length; ++i, ++j) {
            int t = (r >> 8) & 0xff;
            src[i] = (byte) (enc[j] ^ t);
            r = (((enc[j] & 0xff) + r) * ENCRYPT_C1 + ENCRYPT_C2) % 65536;
        }
        return r;
    }

    /**
     * Generate a valid Type1 exec encrypt leading bytes.
     * 
     * @return 4 elements' byte array.
     */
    public static byte[] generateExecLeadingBytes() {
        byte[] leading = new byte[DEFAULT_LEADING_BYTES_NUMBER];
        byte[] encrypt = new byte[DEFAULT_LEADING_BYTES_NUMBER];
        boolean ok = false;
        while (!ok) {
            // Step 1. generate random leading bytes
            for (int i = 0; i < leading.length; ++i) {
                leading[i] = (byte) (Math.random() * 255);
            }
            // Step 2. encrypt bytes
            int r = EXEC_ENCRYPT_KEY;
            for (int i = 0; i < leading.length; ++i) {
                int t = (r >> 8) & 0xff;
                encrypt[i] = (byte) (leading[i] ^ t);
                r = ((encrypt[i] + r) * ENCRYPT_C1 + ENCRYPT_C2) >> 16;
            }
            // Step 3. check the restriction.
            int c = encrypt[0] & 0xff;
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            	ok = false;
            	continue;
            }
            for (int i = 0; i < encrypt.length; ++i) {
                c = encrypt[i] & 0xff;
            	if (!((c >= '0' && c <= '9') && (c >= 'a' && c <= 'f')
                        && (c >= 'A' && c <= 'F'))) {
            		ok = true;
            		break;
                }
            }
        }
        return leading;
    }

    /**
     * Generate a Type1 CharString leading bytes.
     * 
     * @param n
     *            bytes number of leading.
     * @return n elements' byte array.
     */
    public static byte[] generateCharStringLeadingBytes(int n) {
        byte[] leading = new byte[n];
        for (int i = 0; i < leading.length; ++i) {
            leading[i] = (byte) (Math.random() * 255);
        }
        return leading;
    }
    
    public static void encryptExec(byte[] src, byte[] enc, byte[] leading) {
    	int r = EXEC_ENCRYPT_KEY;
    	r = encrypt(leading, 0, enc, 0, leading.length, r);
    	r = encrypt(src, 0, enc, leading.length, src.length, r);
    }

    /**
     * Encrypt Type1 exec code. Length of enc must be length of src plus 4.
     * 
     * @param src
     *            source exec code.
     * @param enc
     *            encrypted exec code.
     */
    public static void encryptExec(byte[] src, byte[] enc) {
        byte[] leading = generateExecLeadingBytes();
        int r = EXEC_ENCRYPT_KEY;
        r = encrypt(leading, 0, enc, 0, leading.length, r);
        r = encrypt(src, 0, enc, leading.length, src.length, r);
    }

    /**
     * Decrypt Type1 exec code. Length of src must be length of enc minus 4.
     * 
     * @param enc
     *            encrypted exec code.
     * @param src
     *            source exec code.
     */
    public static void decryptExec(byte[] enc, byte[] src) {
        byte[] leading = new byte[DEFAULT_LEADING_BYTES_NUMBER];
        int r = EXEC_ENCRYPT_KEY;
        r = decrypt(enc, 0, leading, 0, leading.length, r);
        r = decrypt(enc, leading.length, src, 0, enc.length - leading.length, r);
    }

    /**
     * Encrypt Type1 CharString code with 4 bytes leading. Length of enc must be
     * length of src plus 4.
     * 
     * @param src
     *            source CharString code.
     * @param enc
     *            encrypted CharString code.
     */
    public static void encryptCharString(byte[] src, byte[] enc) {
        encryptCharString(src, enc, DEFAULT_LEADING_BYTES_NUMBER);
    }

    /**
     * Decrypt Type1 CharString code with 4 bytes leading. Length of src must be
     * length of enc minus 4.
     * 
     * @param enc
     *            encrypted CharString code.
     * @param src
     *            source CharString code.
     */
    public static void decryptCharString(byte[] enc, byte[] src) {
        decryptCharString(enc, src, DEFAULT_LEADING_BYTES_NUMBER);
    }

    /**
     * Encrypt Type1 CharString code with specified number of leading bytes.
     * Length of enc must be length of src plus n.
     * 
     * @param src
     *            source CharString code.
     * @param enc
     *            encrypted CharString code.
     * @param n
     *            number of leading bytes.
     */
    public static void encryptCharString(byte[] src, byte[] enc, int n) {
        byte[] leading = generateCharStringLeadingBytes(n);
        int r = CHARSTRING_ENCRYPT_KEY;
        r = encrypt(leading, 0, enc, 0, leading.length, r);
        r = encrypt(src, 0, enc, leading.length, src.length, r);
    }

    /**
     * Encrypt Type1 CharString code with specified leading bytes.
     * Length of enc must be length of src plus length of leading.
     * 
     * @param src
     *            source CharString code.
     * @param enc
     *            encrypted CharString code.
     * @param leading
     *            leading bytes.
     */
    public static void encryptCharString(byte[] src, byte[] enc, byte[] leading) {
        int r = CHARSTRING_ENCRYPT_KEY;
        r = encrypt(leading, 0, enc, 0, leading.length, r);
        r = encrypt(src, 0, enc, leading.length, src.length, r);
    }
    
    /**
     * Decrypt Type1 CharString code with specified number of leading bytes.
     * Length of src must be length of enc minus n.
     * 
     * @param enc
     *            encrypted CharString code.
     * @param src
     *            source CharString code.
     * @param n
     *            number of leading bytes.
     */
    public static void decryptCharString(byte[] enc, byte[] src, int n) {
        byte[] leading = new byte[n];
        int r = CHARSTRING_ENCRYPT_KEY;
        r = decrypt(enc, 0, leading, 0, leading.length, r);
        r = decrypt(enc, leading.length, src, 0, enc.length - leading.length, r);
    }

    
}
