//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.awt.Dimension;
import java.io.Serializable;

public class Coord implements Comparable<Coord>, Serializable {
    public int x;
    public int y;
    public static Coord z = new Coord(0, 0);

    public Coord(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Coord(Coord c) {
	this(c.x, c.y);
    }

    public Coord() {
	this(0, 0);
    }

    public Coord(Dimension d) {
	this(d.width, d.height);
    }

    public static Coord sc(double a, double r) {
	return new Coord((int)(Math.cos(a) * r), -((int)(Math.sin(a) * r)));
    }

    public boolean equals(Object o) {
	if (!(o instanceof Coord)) {
	    return false;
	} else {
	    Coord c = (Coord)o;
	    return c.x == this.x && c.y == this.y;
	}
    }

    public int compareTo(Coord c) {
	if (c.y != this.y) {
	    return c.y - this.y;
	} else {
	    return c.x != this.x ? c.x - this.x : 0;
	}
    }

    public Coord add(int ax, int ay) {
	return new Coord(this.x + ax, this.y + ay);
    }

    public Coord add(Coord b) {
	return this.add(b.x, b.y);
    }

    public Coord sub(int ax, int ay) {
	return new Coord(this.x - ax, this.y - ay);
    }

    public Coord sub(Coord b) {
	return this.sub(b.x, b.y);
    }

    public Coord mul(int f) {
	return new Coord(this.x * f, this.y * f);
    }

    public Coord mul(double f) {
	return new Coord((int)((double)this.x * f), (int)((double)this.y * f));
    }

    public Coord inv() {
	return new Coord(-this.x, -this.y);
    }

    public Coord mul(Coord f) {
	return new Coord(this.x * f.x, this.y * f.y);
    }

    public Coord div(Coord d) {
	int v = (this.x < 0 ? this.x + 1 : this.x) / d.x;
	int w = (this.y < 0 ? this.y + 1 : this.y) / d.y;
	if (this.x < 0) {
	    --v;
	}

	if (this.y < 0) {
	    --w;
	}

	return new Coord(v, w);
    }

    public Coord div(int d) {
	return this.div(new Coord(d, d));
    }

    public Coord mod(Coord d) {
	int v = this.x % d.x;
	int w = this.y % d.y;
	if (v < 0) {
	    v += d.x;
	}

	if (w < 0) {
	    w += d.y;
	}

	return new Coord(v, w);
    }

    public boolean isect(Coord c, Coord s) {
	return this.x >= c.x && this.y >= c.y && this.x < c.x + s.x && this.y < c.y + s.y;
    }

    public String toString() {
	return "(" + this.x + ", " + this.y + ")";
    }

    public double angle(Coord o) {
	Coord c = o.add(this.inv());
	if (c.x == 0) {
	    return c.y < 0 ? -1.5707963267948966 : 1.5707963267948966;
	} else if (c.x < 0) {
	    return c.y < 0 ? -3.141592653589793 + Math.atan((double)c.y / (double)c.x) : Math.PI + Math.atan((double)c.y / (double)c.x);
	} else {
	    return Math.atan((double)c.y / (double)c.x);
	}
    }

    public double dist(Coord o) {
	long dx = (long)(o.x - this.x);
	long dy = (long)(o.y - this.y);
	return Math.sqrt((double)(dx * dx + dy * dy));
    }
}
