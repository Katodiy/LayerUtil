//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.io.InputStream;

public class MessageInputStream extends InputStream {
    private final Message bk;

    public MessageInputStream(Message from) {
	this.bk = from;
    }

    public int read() {
	while(true) {
	    if (this.bk.rt - this.bk.rh < 1) {
		if (this.bk.underflow(1)) {
		    continue;
		}

		return -1;
	    }

	    return Utils.ub(this.bk.rbuf[this.bk.rh++]);
	}
    }

    public int read(byte[] buf, int off, int len) {
	int read;
	int r;
	for(read = 0; len > 0; read += r) {
	    while(this.bk.rh >= this.bk.rt) {
		if (!this.bk.underflow(Math.min(len, 1024))) {
		    return read > 0 ? read : -1;
		}
	    }

	    r = Math.min(len, this.bk.rt - this.bk.rh);
	    System.arraycopy(this.bk.rbuf, this.bk.rh, buf, off, r);
	    Message var10000 = this.bk;
	    var10000.rh += r;
	    off += r;
	    len -= r;
	}

	return read;
    }
}
