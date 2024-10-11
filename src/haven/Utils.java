//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public Utils() {
    }

    public static String sessdate(long sess) {
	return (new SimpleDateFormat("yyyy-MM-dd HH.mm.ss")).format(new Date(sess));
    }

    public static Color extractColor(String res) {
	String sub = res.substring(res.lastIndexOf("[") + 1, res.lastIndexOf("]"));
	String[] arr = sub.split(",");
	return new Color(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
    }

    public static String timestamp() {
	return (new SimpleDateFormat("[HH:mm] ")).format(new Date());
    }

    static Coord imgsz(BufferedImage img) {
	return img != null ? new Coord(img.getWidth(), img.getHeight()) : new Coord(0, 0);
    }

    public static Coord parseCoord(String val) {
	String[] data = val.substring(val.lastIndexOf("(") + 1, val.lastIndexOf(")")).split(", ");
	return new Coord(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    static int ub(byte b) {
	return b < 0 ? 256 + b : b;
    }

    static byte sb(int b) {
	return b > 127 ? (byte)(-256 + b) : (byte)b;
    }

    static int uint16d(byte[] buf, int off) {
	return ub(buf[off]) + ub(buf[off + 1]) * 256;
    }

    static int int16d(byte[] buf, int off) {
	int u = uint16d(buf, off);
	return u > 32767 ? -65536 + u : u;
    }

    static byte[] byte_int16d(int value) {
	return new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255)};
    }

    static byte[] byte_float32d(float value) {
	byte[] buf = new byte[4];
	float32e(value, buf, 0);
	return buf;
    }

    static long uint32d(byte[] buf, int off) {
	return (long)(ub(buf[off]) + ub(buf[off + 1]) * 256 + ub(buf[off + 2]) * 65536 + ub(buf[off + 3]) * 16777216);
    }

    static void uint32e(long num, byte[] buf, int off) {
	buf[off] = sb((int)(num & 255L));
	buf[off + 1] = sb((int)((num & 65280L) >> 8));
	buf[off + 2] = sb((int)((num & 16711680L) >> 16));
	buf[off + 3] = sb((int)((num & -16777216L) >> 24));
    }

    static int int32d(byte[] buf, int off) {
	long u = uint32d(buf, off);
	return u > 2147483647L ? (int)(-4294967296L - u) : (int)u;
    }

    public static long int64d(byte[] buf, int off) {
	long b = 0L;

	for(int i = 0; i < 8; ++i) {
	    b |= (long)ub(buf[off + i]) << i * 8;
	}

	return b;
    }

    public static void int64e(long num, byte[] buf, int off) {
	for(int i = 0; i < 8; ++i) {
	    buf[off++] = (byte)((int)(num & 255L));
	    num >>>= 8;
	}

    }

    static byte[] byte_int32d(int value) {
	return new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255), (byte)(value >> 16 & 255), (byte)(value >> 24 & 255)};
    }

    static void int32e(int num, byte[] buf, int off) {
	if (num < 0) {
	    uint32e(4294967296L + (long)num, buf, off);
	} else {
	    uint32e((long)num, buf, off);
	}

    }

    static void uint16e(int num, byte[] buf, int off) {
	buf[off] = sb(num & 255);
	buf[off + 1] = sb((num & '\uff00') >> 8);
    }

    public static void int16e(short num, byte[] buf, int off) {
	uint16e(num & '\uffff', buf, off);
    }

    public static double floatd(byte[] buf, int off) {
	int e = buf[off];
	long t = uint32d(buf, off + 1);
	int m = (int)(t & 2147483647L);
	boolean s = (t & 2147483648L) != 0L;
	if (e == -128) {
	    if (m == 0) {
		return 0.0;
	    } else {
		throw new RuntimeException("Invalid special float encoded (" + m + ")");
	    }
	} else {
	    double v = (double)m / 2.147483648E9 + 1.0;
	    if (s) {
		v = -v;
	    }

	    return Math.pow(2.0, (double)e) * v;
	}
    }

    public static float float32d(byte[] buf, int off) {
	return Float.intBitsToFloat(int32d(buf, off));
    }

    public static double float64d(byte[] buf, int off) {
	return Double.longBitsToDouble(int64d(buf, off));
    }

    public static void float32e(float num, byte[] buf, int off) {
	int32e(Float.floatToIntBits(num), buf, off);
    }

    public static void float64e(double num, byte[] buf, int off) {
	int64e(Double.doubleToLongBits(num), buf, off);
    }

    public static void float9995d(int word, float[] ret) {
	int xb = (word & 2139095040) >> 23;
	int xs = (word & Integer.MIN_VALUE) >> 31 & 1;
	int yb = (word & 4177920) >> 14;
	int ys = (word & 4194304) >> 22 & 1;
	int zb = (word & 8160) >> 5;
	int zs = (word & 8192) >> 13 & 1;
	int me = (word & 31) - 15;
	int xe = Integer.numberOfLeadingZeros(xb) - 24;
	int ye = Integer.numberOfLeadingZeros(yb) - 24;
	int ze = Integer.numberOfLeadingZeros(zb) - 24;
	if (xe == 8) {
	    ret[0] = 0.0F;
	} else {
	    ret[0] = Float.intBitsToFloat(xs << 31 | me - xe + 127 << 23 | xb << xe + 16 & 8388607);
	}

	if (ye == 8) {
	    ret[1] = 0.0F;
	} else {
	    ret[1] = Float.intBitsToFloat(ys << 31 | me - ye + 127 << 23 | yb << ye + 16 & 8388607);
	}

	if (ze == 8) {
	    ret[2] = 0.0F;
	} else {
	    ret[2] = Float.intBitsToFloat(zs << 31 | me - ze + 127 << 23 | zb << ze + 16 & 8388607);
	}

    }

    public static float hfdec(short bits) {
	int b = ((int) bits) & 0xffff;
	int e = (b & 0x7c00) >> 10;
	int m = b & 0x03ff;
	int ee;
	if(e == 0) {
	    if(m == 0) {
		ee = 0;
	    } else {
		int n = Integer.numberOfLeadingZeros(m) - 22;
		ee = (-15 - n) + 127;
		m = (m << (n + 1)) & 0x03ff;
	    }
	} else if(e == 0x1f) {
	    ee = 0xff;
	} else {
	    ee = e - 15 + 127;
	}
	int f32 = ((b & 0x8000) << 16) |
	    (ee << 23) |
	    (m << 13);
	return (Float.intBitsToFloat(f32));
    }

    public static short hfenc(float f) {
	int b = Float.floatToIntBits(f);
	int e = (b & 0x7f800000) >> 23;
	int m = b & 0x007fffff;
	int ee;
	if(e == 0) {
	    ee = 0;
	    m = 0;
	} else if(e == 0xff) {
	    ee = 0x1f;
	} else if(e < 127 - 14) {
	    ee = 0;
	    m = (m | 0x00800000) >> ((127 - 14) - e);
	} else if(e > 127 + 15) {
	    return (((b & 0x80000000) == 0) ? ((short) 0x7c00) : ((short) 0xfc00));
	} else {
	    ee = e - 127 + 15;
	}
	int f16 = ((b >> 16) & 0x8000) |
	    (ee << 10) |
	    (m >> 13);
	return ((short) f16);
    }

    public static float mfdec(byte bits) {
	int b = bits & 255;
	int e = (b & 120) >> 3;
	int m = b & 7;
	int ee;
	int f32;
	if (e == 0) {
	    if (m == 0) {
		ee = 0;
	    } else {
		f32 = Integer.numberOfLeadingZeros(m) - 29;
		ee = -7 - f32 + 127;
		m = m << f32 + 1 & 7;
	    }
	} else if (e == 15) {
	    ee = 255;
	} else {
	    ee = e - 7 + 127;
	}

	f32 = (b & 128) << 24 | ee << 23 | m << 20;
	return Float.intBitsToFloat(f32);
    }

    public static byte mfenc(float f) {
	int b = Float.floatToIntBits(f);
	int e = (b & 2139095040) >> 23;
	int m = b & 8388607;
	int ee;
	if (e == 0) {
	    ee = 0;
	    m = 0;
	} else if (e == 255) {
	    ee = 15;
	} else if (e < 121) {
	    ee = 0;
	    m = (m | 8388608) >> 121 - e;
	} else {
	    if (e > 134) {
		return (byte)((b & Integer.MIN_VALUE) == 0 ? 120 : -8);
	    }

	    ee = e - 127 + 7;
	}

	int f8 = b >> 24 & 128 | ee << 3 | m >> 20;
	return (byte)f8;
    }

    public static double clip(double d, double min, double max) {
	if (d < min) {
	    return min;
	} else {
	    return d > max ? max : d;
	}
    }

    public static float clip(float d, float min, float max) {
	if (d < min) {
	    return min;
	} else {
	    return d > max ? max : d;
	}
    }

    public static int clip(int i, int min, int max) {
	if (i < min) {
	    return min;
	} else {
	    return i > max ? max : i;
	}
    }

    public static String strd(byte[] buf, int[] off) {
	int i;
	for(i = off[0]; buf[i] != 0; ++i) {
	}

	String ret;
	try {
	    ret = new String(buf, off[0], i - off[0], "utf-8");
	} catch (UnsupportedEncodingException var5) {
	    UnsupportedEncodingException e = var5;
	    throw new IllegalArgumentException(e);
	}

	off[0] = i + 1;
	return ret;
    }

    static byte[] byte_strd(String s) throws Exception {
	byte[] utf8 = s.getBytes("UTF-8");
	byte[] b = new byte[utf8.length + 1];

	for(int i = 0; i < utf8.length; ++i) {
	    b[i] = utf8[i];
	}

	b[utf8.length] = 0;
	return b;
    }

    static byte[] byte_str(String s) throws Exception {
	return s.getBytes("UTF-8");
    }

    static char num2hex(int num) {
	return num < 10 ? (char)(48 + num) : (char)(65 + num - 10);
    }

    static int hex2num(char hex) {
	if (hex >= '0' && hex <= '9') {
	    return hex - 48;
	} else if (hex >= 'a' && hex <= 'f') {
	    return hex - 97 + 10;
	} else if (hex >= 'A' && hex <= 'F') {
	    return hex - 65 + 10;
	} else {
	    throw new RuntimeException();
	}
    }

    static String byte2hex(byte[] in) {
	StringBuilder buf = new StringBuilder();
	byte[] var2 = in;
	int var3 = in.length;

	for(int var4 = 0; var4 < var3; ++var4) {
	    byte b = var2[var4];
	    buf.append(num2hex((b & 240) >> 4));
	    buf.append(num2hex(b & 15));
	}

	return buf.toString();
    }

    static byte[] hex2byte(String hex) {
	if (hex.length() % 2 != 0) {
	    throw new RuntimeException("Invalid hex-encoded string");
	} else {
	    byte[] ret = new byte[hex.length() / 2];
	    int i = 0;

	    for(int o = 0; i < hex.length(); ++o) {
		ret[o] = (byte)(hex2num(hex.charAt(i)) << 4 | hex2num(hex.charAt(i + 1)));
		i += 2;
	    }

	    return ret;
	}
    }

    static int atoi(String a) {
	try {
	    return Integer.parseInt(a);
	} catch (NumberFormatException var2) {
	    return 0;
	}
    }

    static void readtileof(InputStream in) throws IOException {
	byte[] buf = new byte[4096];

	while(in.read(buf, 0, buf.length) >= 0) {
	}

    }

    static byte[] readall(InputStream in) throws IOException {
	byte[] buf = new byte[4096];
	int off = 0;

	while(true) {
	    if (off == buf.length) {
		byte[] n = new byte[buf.length * 2];
		System.arraycopy(buf, 0, n, 0, buf.length);
		buf = n;
	    }

	    int ret = in.read(buf, off, buf.length - off);
	    if (ret < 0) {
		byte[] n = new byte[off];
		System.arraycopy(buf, 0, n, 0, off);
		return n;
	    }

	    off += ret;
	}
    }

    public static int rnint(BufferedReader br) throws Exception {
	return Integer.parseInt(rnstr(br));
    }

    public static float rfloat(BufferedReader br) throws Exception {
	return Float.parseFloat(rnstr(br));
    }

    public static String rnstr(BufferedReader br) throws Exception {
	String n = "";

	while((n = br.readLine()) != null && n.length() > 0) {
	    if (n.charAt(0) != '#' && !n.startsWith("п»ї")) {
		break;
	    }
	}

	return n != null ? n.replace("\\n", "\n") : null;
    }

    public static String rstr(BufferedReader br) throws Exception {
	String n;
	while((n = br.readLine()) != null && n.length() > 0 && (n.charAt(0) == '#' || n.startsWith("п»ї"))) {
	}

	return n != null ? n.trim() : null;
    }

    public static boolean isJavaClass(byte[] bytes) {
	return bytes.length >= 4 && bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66;
    }

    public static byte[] readBytes(File file) throws IOException {
	byte[] tmp = new byte[(int)file.length()];
	FileInputStream fis = new FileInputStream(file);
	fis.read(tmp);
	fis.close();
	return tmp;
    }

    public static int floordiv(int a, int b) {
	return a < 0 ? (a + 1) / b - 1 : a / b;
    }

    public static int floormod(int a, int b) {
	int r = a % b;
	if (r < 0) {
	    r += b;
	}

	return r;
    }

    public static int floordiv(float a, float b) {
	return (int)Math.floor((double)(a / b));
    }

    public static float floormod(float a, float b) {
	float r = a % b;
	if (r < 0.0F) {
	    r += b;
	}

	return r;
    }

    public static void serialize(Object obj, OutputStream out) throws IOException {
	ObjectOutputStream oout = new ObjectOutputStream(out);
	oout.writeObject(obj);
	oout.flush();
    }

    public static byte[] serialize(Object obj) {
	ByteArrayOutputStream out = new ByteArrayOutputStream();

	try {
	    serialize(obj, out);
	} catch (IOException var3) {
	    IOException e = var3;
	    throw new RuntimeException(e);
	}

	return out.toByteArray();
    }

    public static Object deserialize(InputStream in) throws IOException {
	ObjectInputStream oin = new ObjectInputStream(in);

	try {
	    return oin.readObject();
	} catch (ClassNotFoundException var3) {
	    return null;
	}
    }

    public static Object deserialize(byte[] buf) {
	if (buf == null) {
	    return null;
	} else {
	    InputStream in = new ByteArrayInputStream(buf);

	    try {
		return deserialize((InputStream)in);
	    } catch (IOException var3) {
		return null;
	    }
	}
    }

    public static String strcolor(Color obj) {
	return "[" + obj.getRed() + "," + obj.getGreen() + "," + obj.getBlue() + "," + obj.getAlpha() + "]";
    }
}
