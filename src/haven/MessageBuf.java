//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.nio.ByteBuffer;

public class MessageBuf extends Message {
    public static final MessageBuf nil = new MessageBuf();
    private final int oh;

    public MessageBuf(byte[] blob, int off, int len) {
	if (blob == null) {
	    throw new NullPointerException("blob");
	} else {
	    this.rbuf = blob;
	    this.rh = this.oh = off;
	    this.rt = off + len;
	}
    }

    public MessageBuf(byte[] blob) {
	this(blob, 0, blob.length);
    }

    public MessageBuf() {
	this.oh = 0;
    }

    public MessageBuf(Message from) {
	if (from instanceof MessageBuf) {
	    MessageBuf fb = (MessageBuf)from;
	    this.rbuf = fb.rbuf;
	    this.rh = this.oh = fb.rh;
	    this.rt = fb.rt;
	    this.wbuf = fb.wbuf;
	    this.wh = this.wt = fb.wh;
	} else {
	    this.rbuf = from.bytes();
	    this.rh = this.oh = 0;
	    this.rt = this.rbuf.length;
	}

    }

    public boolean underflow(int hint) {
	return false;
    }

    public void overflow(int min) {
	int cl;
	for(cl = this.wt == 0 ? 32 : this.wt; cl - this.wh < min; cl *= 2) {
	}

	byte[] n = new byte[cl];
	System.arraycopy(this.wbuf, 0, n, 0, this.wh);
	this.wbuf = n;
	this.wt = cl;
    }

    public boolean equals(Object o2) {
	if (!(o2 instanceof MessageBuf)) {
	    return false;
	} else {
	    MessageBuf m2 = (MessageBuf)o2;
	    if (m2.rt - m2.oh != this.rt - this.oh) {
		return false;
	    } else {
		int i = this.oh;

		for(int o = m2.oh; i < this.rt; ++o) {
		    if (m2.rbuf[o] != this.rbuf[i]) {
			return false;
		    }

		    ++i;
		}

		return true;
	    }
	}
    }

    public int hashCode() {
	int ret = 192581;

	for(int i = this.oh; i < this.rt; ++i) {
	    ret = ret * 31 + this.rbuf[i];
	}

	return ret;
    }

    public int rem() {
	return this.rt - this.rh;
    }

    public void rewind() {
	this.rh = this.oh;
    }

    public MessageBuf clone() {
	return new MessageBuf(this.rbuf, this.oh, this.rt - this.oh);
    }

    public int size() {
	return this.wh;
    }

    public byte[] fin() {
	byte[] ret = new byte[this.wh];
	System.arraycopy(this.wbuf, 0, ret, 0, this.wh);
	return ret;
    }

    public void fin(byte[] buf, int off) {
	System.arraycopy(this.wbuf, 0, buf, off, Math.min(this.wh, buf.length - off));
    }

    public void fin(ByteBuffer buf) {
	buf.put(this.wbuf, 0, this.wh);
    }

    public String toString() {
	StringBuilder buf = new StringBuilder();
	buf.append("Message(");

	for(int i = this.oh; i < this.rt; ++i) {
	    if (i > 0) {
		buf.append(' ');
	    }

	    if (i == this.rh) {
		buf.append('>');
	    }

	    buf.append(String.format("%02x", this.rbuf[i] & 255));
	}

	buf.append(")");
	return buf.toString();
    }
}
