//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

public class Coord3f {
    public float x;
    public float y;
    public float z;
    public static Coord3f o = new Coord3f(0.0F, 0.0F, 0.0F);
    public static Coord3f xu = new Coord3f(1.0F, 0.0F, 0.0F);
    public static Coord3f yu = new Coord3f(0.0F, 1.0F, 0.0F);
    public static Coord3f zu = new Coord3f(0.0F, 0.0F, 1.0F);

    public Coord3f(float x, float y, float z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public Coord3f(Coord3f c) {
	this(c.x, c.y, c.z);
    }

    public Coord3f(Coord c) {
	this((float)c.x, (float)c.y, 0.0F);
    }

    public boolean equals(Coord3f o) {
	return o.x == this.x && o.y == this.y && o.z == this.z;
    }

    public boolean equals(Object o) {
	return o instanceof Coord3f && this.equals((Coord3f)o);
    }

    public Coord3f add(float ax, float ay, float az) {
	return new Coord3f(this.x + ax, this.y + ay, this.z + az);
    }

    public Coord3f add(Coord3f b) {
	return this.add(b.x, b.y, b.z);
    }

    public Coord3f sadd(float e, float a, float r) {
	return this.add((float)Math.cos((double)a) * (float)Math.cos((double)e) * r, (float)Math.sin((double)a) * (float)Math.cos((double)e) * r, (float)Math.sin((double)e) * r);
    }

    public Coord3f neg() {
	return new Coord3f(-this.x, -this.y, -this.z);
    }

    public Coord3f sub(float ax, float ay, float az) {
	return new Coord3f(this.x - ax, this.y - ay, this.z - az);
    }

    public Coord3f sub(Coord3f b) {
	return this.sub(b.x, b.y, b.z);
    }

    public Coord3f mul(float f) {
	return new Coord3f(this.x * f, this.y * f, this.z * f);
    }

    public Coord3f mul(float X, float Y, float Z) {
	return new Coord3f(this.x * X, this.y * Y, this.z * Z);
    }

    public Coord3f mul(Coord3f b) {
	return this.mul(b.x, b.y, b.z);
    }

    public Coord3f div(float f) {
	return new Coord3f(this.x / f, this.y / f, this.z / f);
    }

    public Coord3f inv() {
	return new Coord3f(-this.x, -this.y, -this.z);
    }

    public float dmul(float X, float Y, float Z) {
	return this.x * X + this.y * Y + this.z * Z;
    }

    public float dmul(Coord3f b) {
	return this.dmul(b.x, b.y, b.z);
    }

    public Coord3f cmul(float X, float Y, float Z) {
	return new Coord3f(this.y * Z - this.z * Y, this.z * X - this.x * Z, this.x * Y - this.y * X);
    }

    public Coord3f cmul(Coord3f b) {
	return this.cmul(b.x, b.y, b.z);
    }

    public Coord3f rot(Coord3f p, float a) {
	float c = (float)Math.cos((double)a);
	float s = (float)Math.sin((double)a);
	float C = 1.0F - c;
	float ax = p.x;
	float ay = p.y;
	float az = p.z;
	return new Coord3f(this.x * (ax * ax * C + c) + this.y * (ay * ax * C - az * s) + this.z * (az * ax * C + ay * s), this.x * (ax * ay * C + az * s) + this.y * (ay * ay * C + c) + this.z * (az * ay * C - ax * s), this.x * (ax * az * C - ay * s) + this.y * (ay * az * C + ax * s) + this.z * (az * az * C + c));
    }

    public float abs() {
	return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public Coord3f norm() {
	float a = this.abs();
	return (double)a == 0.0 ? new Coord3f(0.0F, 0.0F, 0.0F) : this.div(a);
    }

    public float dist(Coord3f o) {
	float dx = o.x - this.x;
	float dy = o.y - this.y;
	float dz = o.z - this.z;
	return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public float xyangle(Coord3f o) {
	Coord3f c = o.sub(this);
	if (c.x == 0.0F) {
	    return c.y < 0.0F ? -1.5707964F : 1.5707964F;
	} else if (c.x < 0.0F) {
	    return c.y < 0.0F ? (float)(-3.141592653589793 + Math.atan((double)(c.y / c.x))) : (float)(Math.PI + Math.atan((double)(c.y / c.x)));
	} else {
	    return (float)Math.atan((double)(c.y / c.x));
	}
    }

    public float[] to3a() {
	return new float[]{this.x, this.y, this.z};
    }

    public float[] to4a(float w) {
	return new float[]{this.x, this.y, this.z, w};
    }

    public Coord round2() {
	return new Coord(Math.round(this.x), Math.round(this.y));
    }

    public String toString() {
	return String.format("(%f, %f, %f)", this.x, this.y, this.z);
    }
}
