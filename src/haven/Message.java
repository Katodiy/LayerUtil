//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class Message {
    public static final int T_END = 0;
    public static final int T_INT = 1;
    public static final int T_STR = 2;
    public static final int T_COORD = 3;
    public static final int T_UINT8 = 4;
    public static final int T_UINT16 = 5;
    public static final int T_COLOR = 6;
    public static final int T_TTOL = 8;
    public static final int T_INT8 = 9;
    public static final int T_INT16 = 10;
    public static final int T_NIL = 12;
    public static final int T_UID = 13;
    public static final int T_BYTES = 14;
    public static final int T_FLOAT32 = 15;
    public static final int T_FLOAT64 = 16;
    public static final int T_FCOORD32 = 18;
    public static final int T_FCOORD64 = 19;
    private static final byte[] empty = new byte[0];
    public int rh = 0;
    public int rt = 0;
    public int wh = 0;
    public int wt = 0;
    public byte[] rbuf;
    public byte[] wbuf;
    public static final Message nil = new Message() {
	public boolean underflow(int hint) {
	    return false;
	}

	public void overflow(int min) {
	    throw new RuntimeException("nil message is not writable");
	}

	public String toString() {
	    return "Message(nil)";
	}
    };

    public Message() {
	this.rbuf = empty;
	this.wbuf = empty;
    }

    public abstract boolean underflow(int var1);

    private void rensure(int len) {
	while(true) {
	    if (len > this.rt - this.rh) {
		if (this.underflow(this.rh + len - this.rt)) {
		    continue;
		}

		throw new EOF("Required " + len + " bytes, got only " + (this.rt - this.rh));
	    }

	    return;
	}
    }

    private int rget(int len) {
	this.rensure(len);
	int co = this.rh;
	this.rh += len;
	return co;
    }

    public boolean eom() {
	return this.rh >= this.rt && !this.underflow(1);
    }

    public byte int8() {
	this.rensure(1);
	return this.rbuf[this.rh++];
    }

    public byte uint8() {
	return (byte)(this.int8() & 255);
    }

    public short int16() {
	int off = this.rget(2);
	return (short)Utils.int16d(this.rbuf, off);
    }

    public short uint16() {
	int off = this.rget(2);
	return (short)Utils.uint16d(this.rbuf, off);
    }

    public int int32() {
	int off = this.rget(4);
	return Utils.int32d(this.rbuf, off);
    }

    public long uint32() {
	int off = this.rget(4);
	return Utils.uint32d(this.rbuf, off);
    }

    public long int64() {
	int off = this.rget(8);
	return Utils.int64d(this.rbuf, off);
    }

    public String string() {
	int l;
	for(l = 0; l < this.rt - this.rh || this.underflow(256); ++l) {
	    if (this.rbuf[l + this.rh] == 0) {
		String ret = new String(this.rbuf, this.rh, l, StandardCharsets.UTF_8);
		this.rh += l + 1;
		return ret;
	    }
	}

	throw new EOF("Found no NUL (at length " + l + ")");
    }

    public void skip(int n) {
	while(n > 0) {
	    if (this.rh >= this.rt && !this.underflow(Math.min(n, 1024))) {
		throw new EOF("Out of bytes to skip");
	    }

	    int s = Math.min(n, this.rt - this.rh);
	    this.rh += s;
	    n -= s;
	}

    }

    public void skip() {
	do {
	    this.rh = this.rt;
	} while(this.underflow(1024));

    }

    public byte[] bytes(int n) {
	byte[] ret = new byte[n];
	this.rensure(n);
	System.arraycopy(this.rbuf, this.rh, ret, 0, n);
	this.rh += n;
	return ret;
    }

    public byte[] bytes() {
	while(this.underflow(65536)) {
	}

	return this.bytes(this.rt - this.rh);
    }

    public void bytes(byte[] b, int off, int len) {
	int r;
	for(int olen = len; len > 0; len -= r) {
	    if (this.rh >= this.rt && !this.underflow(Math.min(len, 1024))) {
		throw new EOF("Required " + olen + " bytes, got only " + (olen - len));
	    }

	    r = Math.min(len, this.rt - this.rh);
	    System.arraycopy(this.rbuf, this.rh, b, off, r);
	    this.rh += r;
	    off += r;
	}

    }

    public void bytes(byte[] b) {
	this.bytes(b, 0, b.length);
    }

    public Coord coord() {
	return new Coord(this.int32(), this.int32());
    }

    public Coord coord16() {
	return new Coord(this.uint16(), this.uint16());
    }

    public Color color() {
	return new Color(this.uint8() & 255, this.uint8() & 255, this.uint8() & 255, this.uint8() & 255);
    }

    public float float8() {
	return Utils.mfdec(this.int8());
    }

    public float float16() {
	return Utils.hfdec(this.int16());
    }

    public float float32() {
	int off = this.rget(4);
	return Utils.float32d(this.rbuf, off);
    }

    public double float64() {
	int off = this.rget(8);
	return Utils.float64d(this.rbuf, off);
    }

    public double cpfloat() {
	int off = this.rget(5);
	return Utils.floatd(this.rbuf, off);
    }

    public float snorm8() {
	return (float)Utils.clip(this.int8(), -127, 127) / 127.0F;
    }

    public float unorm8() {
	return (float)this.uint8() / 255.0F;
    }

    public float snorm16() {
	return (float)Utils.clip(this.int16(), -32767, 32767) / 32767.0F;
    }

    public float unorm16() {
	return (float)this.uint16() / 65535.0F;
    }

    public double snorm32() {
	return (double)Utils.clip(this.int32(), -2147483647, Integer.MAX_VALUE) / 2.147483647E9;
    }

    public double unorm32() {
	return (double)this.uint32() / 4.294967295E9;
    }

    public Object[] list() {
	ArrayList<Object> ret = new ArrayList();

	while(true) {
	    if (!this.eom()) {
		int t = this.uint8();
		switch (t) {
		    case 0:
			break;
		    case 1:
			ret.add(this.int32());
			continue;
		    case 2:
			ret.add(this.string());
			continue;
		    case 3:
			ret.add(this.coord());
			continue;
		    case 4:
			ret.add(this.uint8());
			continue;
		    case 5:
			ret.add(this.uint16());
			continue;
		    case 6:
			ret.add(this.color());
			continue;
		    case 7:
		    case 11:
		    case 17:
		    default:
			throw new FormatError("Encountered unknown type " + t + " in TTO list.");
		    case 8:
			ret.add(this.list());
			continue;
		    case 9:
			ret.add(this.int8());
			continue;
		    case 10:
			ret.add(this.int16());
			continue;
		    case 12:
			ret.add((Object)null);
			continue;
		    case 13:
			ret.add(this.int64());
			continue;
		    case 14:
			int len = this.uint8();
			if ((len & 128) != 0) {
			    len = this.int32();
			}

			ret.add(this.bytes(len));
			continue;
		    case 15:
			ret.add(this.float32());
			continue;
		    case 16:
			ret.add(this.float64());
			continue;
		    case 18:
			ret.add(new Coord2d((double)this.float32(), (double)this.float32()));
			continue;
		    case 19:
			ret.add(new Coord2d(this.float64(), this.float64()));
			continue;
		}
	    }

	    return ret.toArray();
	}
    }

    public abstract void overflow(int var1);

    private void wensure(int len) {
	if (len > this.wt - this.wh) {
	    this.overflow(len);
	}

    }

    private int wget(int len) {
	this.wensure(len);
	int co = this.wh;
	this.wh += len;
	return co;
    }

    public Message addbytes(byte[] src, int off, int len) {
	this.wensure(len);
	System.arraycopy(src, off, this.wbuf, this.wh, len);
	this.wh += len;
	return this;
    }

    public Message addbytes(byte[] src) {
	this.addbytes(src, 0, src.length);
	return this;
    }

    public Message addint8(byte num) {
	this.wensure(1);
	this.wbuf[this.wh++] = num;
	return this;
    }

    public Message adduint8(int num) {
	this.wensure(1);
	this.wbuf[this.wh++] = (byte)num;
	return this;
    }

    public Message addint16(short num) {
	int off = this.wget(2);
	Utils.int16e(num, this.wbuf, off);
	return this;
    }

    public Message adduint16(int num) {
	int off = this.wget(2);
	Utils.uint16e(num, this.wbuf, off);
	return this;
    }

    public Message addint32(int num) {
	int off = this.wget(4);
	Utils.int32e(num, this.wbuf, off);
	return this;
    }

    public Message adduint32(long num) {
	int off = this.wget(4);
	Utils.uint32e(num, this.wbuf, off);
	return this;
    }

    public Message addint64(long num) {
	int off = this.wget(8);
	Utils.int64e(num, this.wbuf, off);
	return this;
    }

    public Message addstring2(String str) {
	this.addbytes(str.getBytes(StandardCharsets.UTF_8));
	return this;
    }

    public Message addstring(String str) {
	this.addstring2(str);
	this.adduint8(0);
	return this;
    }

    public Message addcoord(Coord c) {
	this.addint32(c.x);
	this.addint32(c.y);
	return this;
    }

    public Message addcoord16(Coord c) {
	this.addint16((short)c.x);
	this.addint16((short)c.y);
	return this;
    }

    public Message addcolor(Color color) {
	this.adduint8(color.getRed());
	this.adduint8(color.getGreen());
	this.adduint8(color.getBlue());
	this.adduint8(color.getAlpha());
	return this;
    }

    public Message addfloat8(float num) {
	return this.addint8(Utils.mfenc(num));
    }

    public Message addfloat16(float num) {
	return this.addint16(Utils.hfenc(num));
    }

    public Message addfloat32(float num) {
	int off = this.wget(4);
	Utils.float32e(num, this.wbuf, off);
	return this;
    }

    public Message addfloat64(double num) {
	int off = this.wget(8);
	Utils.float64e(num, this.wbuf, off);
	return this;
    }

    public Message addlist(Object... args) {
	Object[] var2 = args;
	int var3 = args.length;

	for(int var4 = 0; var4 < var3; ++var4) {
	    Object o = var2[var4];
	    if (o == null) {
		this.adduint8(12);
	    } else if (o instanceof Byte) {
		this.adduint8(4);
		this.adduint8((Byte)o);
	    } else if (o instanceof Short) {
		this.adduint8(5);
		this.adduint16((Short)o);
	    } else if (o instanceof Integer) {
		this.adduint8(1);
		this.addint32((Integer)o);
	    } else if (o instanceof String) {
		this.adduint8(2);
		this.addstring((String)o);
	    } else if (o instanceof Coord) {
		this.adduint8(3);
		this.addcoord((Coord)o);
	    } else if (o instanceof byte[]) {
		byte[] b = (byte[])((byte[])o);
		this.adduint8(14);
		if (b.length < 128) {
		    this.adduint8(b.length);
		} else {
		    this.adduint8(128);
		    this.addint32(b.length);
		}

		this.addbytes(b);
	    } else if (o instanceof Color) {
		this.adduint8(6);
		this.addcolor((Color)o);
	    } else if (o instanceof Float) {
		this.adduint8(15);
		this.addfloat32((Float)o);
	    } else if (o instanceof Double) {
		this.adduint8(16);
		this.addfloat64((double)((Double)o).floatValue());
	    } else {
		if (!(o instanceof Coord2d)) {
		    throw new RuntimeException("Cannot encode a " + o.getClass() + " as TTO");
		}

		this.adduint8(19);
		this.addfloat64(((Coord2d)o).x);
		this.addfloat64(((Coord2d)o).y);
	    }
	}

	return this;
    }

    public boolean same(Object obj) {
	if (obj instanceof Message) {
	    Message other = (Message)obj;
	    if (this.rbuf.length != other.rbuf.length) {
		return false;
	    } else {
		for(int i = 0; i < this.rbuf.length; ++i) {
		    if (this.rbuf[i] != other.rbuf[i]) {
			return false;
		    }
		}

		return true;
	    }
	} else {
	    return false;
	}
    }

    public static class FormatError extends BinError {
	public FormatError(String message) {
	    super(message);
	}

	public FormatError(String message, Throwable cause) {
	    super(message, cause);
	}
    }

    public static class EOF extends BinError {
	public EOF(String message) {
	    super(message);
	}
    }

    public static class BinError extends RuntimeException {
	public BinError(String message) {
	    super(message);
	}

	public BinError(String message, Throwable cause) {
	    super(message, cause);
	}

	public BinError(Throwable cause) {
	    super(cause);
	}
    }
}
