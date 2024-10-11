//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.io.Serializable;

public class Coord2d implements Comparable<Coord2d>, Serializable {
    public double x;
    public double y;
    public static final Coord2d z = new Coord2d(0.0, 0.0);

    public Coord2d(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public Coord2d(Coord c) {
	this((double)c.x, (double)c.y);
    }

    public Coord2d(Coord3f c) {
	this((double)c.x, (double)c.y);
    }

    public Coord2d() {
	this(0.0, 0.0);
    }

    public boolean equals(double X, double Y) {
	return this.x == X && this.y == Y;
    }

    public boolean equals(Object o) {
	if (!(o instanceof Coord2d)) {
	    return false;
	} else {
	    Coord2d c = (Coord2d)o;
	    return this.equals(c.x, c.y);
	}
    }

    public int hashCode() {
	long X = Double.doubleToLongBits(this.x);
	long Y = Double.doubleToLongBits(this.y);
	return (int)(X ^ X >>> 32) * 31 + (int)(Y ^ Y >>> 32);
    }

    public int compareTo(Coord2d c) {
	if (c.y < this.y) {
	    return -1;
	} else if (c.y > this.y) {
	    return 1;
	} else if (c.x < this.x) {
	    return -1;
	} else {
	    return c.y > this.y ? 1 : 0;
	}
    }

    public Coord2d add(double X, double Y) {
	return new Coord2d(this.x + X, this.y + Y);
    }

    public Coord2d add(Coord2d b) {
	return this.add(b.x, b.y);
    }

    public Coord2d inv() {
	return new Coord2d(-this.x, -this.y);
    }

    public Coord2d sub(double X, double Y) {
	return new Coord2d(this.x - X, this.y - Y);
    }

    public Coord2d sub(Coord2d b) {
	return this.sub(b.x, b.y);
    }

    public Coord2d mul(double f) {
	return new Coord2d(this.x * f, this.y * f);
    }

    public Coord2d mul(double X, double Y) {
	return new Coord2d(this.x * X, this.y * Y);
    }

    public Coord2d mul(Coord2d b) {
	return this.mul(b.x, b.y);
    }

    public Coord2d div(double f) {
	return new Coord2d(this.x / f, this.y / f);
    }

    public Coord2d div(double X, double Y) {
	return new Coord2d(this.x / X, this.y / Y);
    }

    public Coord2d div(Coord2d b) {
	return this.div(b.x, b.y);
    }

    public Coord round() {
	return new Coord((int)Math.round(this.x), (int)Math.round(this.y));
    }

    public Coord floor() {
	return new Coord((int)Math.floor(this.x), (int)Math.floor(this.y));
    }

    public Coord floor(double X, double Y) {
	return new Coord((int)Math.floor(this.x / X), (int)Math.floor(this.y / Y));
    }

    public Coord floor(Coord2d f) {
	return this.floor(f.x, f.y);
    }

    public Coord2d mod() {
	return new Coord2d(this.x - Math.floor(this.x), this.y - Math.floor(this.y));
    }

    public Coord2d mod(double X, double Y) {
	return new Coord2d(this.x - Math.floor(this.x / X) * X, this.y - Math.floor(this.y / Y) * Y);
    }

    public Coord2d mod(Coord2d f) {
	return this.mod(f.x, f.y);
    }

    public double angle(Coord2d o) {
	return Math.atan2(o.y - this.y, o.x - this.x);
    }

    public double dist(Coord2d o) {
	return Math.hypot(this.x - o.x, this.y - o.y);
    }

    public double abs() {
	return Math.hypot(this.x, this.y);
    }

    public static Coord2d sc(double a, double r) {
	return new Coord2d(Math.cos(a) * r, Math.sin(a) * r);
    }

    public String toString() {
	return "(" + this.x + ", " + this.y + ")";
    }
}
