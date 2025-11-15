//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package haven;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.imageio.ImageIO;

public class Resource {
    static final String SIG = "Haven Resource 1";
    static final byte[] BSIG = new byte[]{72, 97, 118, 101, 110, 32, 82, 101, 115, 111, 117, 114, 99, 101, 32, 49};
    public static String OUT = "dout/";
    private static final String END = "\r\n";
    private static Map<String, Class<? extends Layer>> ltypes = new TreeMap();
    public static Class<Image> imgc = Image.class;
    public static Class<Tile> tile = Tile.class;
    public static Class<Neg> negc = Neg.class;
    public static Class<Anim> animc = Anim.class;
    public static Class<Tileset> tileset = Tileset.class;
    public static Class<Pagina> pagina = Pagina.class;
    public static Class<AButton> action = AButton.class;
    public static Class<Audio> audio = Audio.class;
    public static Class<Tooltip> tooltip = Tooltip.class;
    static int TYPES = 0;
    static final int IMAGE;
    static final int TILE;
    static final int NEG;
    static final int OBST;
    static final int ANIM;
    static final int TILESET;
    static final int PAGINA;
    static final int ABUTTON;
    static final int AUDIO;
    static final int TOOLTIP;
    static final int MUSIC;
    static final int CODE;
    static final int CODEENTRY;
    static final int SOURCES;
    static final int NTOOLTIP;
    static final int MTOOLTIP;
    static final int MAT2;
    static final int TILESET2;
    static final int TEX;
    static final int OVERLAY;

    static final int FONT;
    private Collection<? extends Layer> layers;
    public final String out;
    public final String name;
    public int ver;

    public static Coord cdec(Message buf) {
	return new Coord(buf.int16(), buf.int16());
    }

    public static Coord cdec(byte[] buf, int off) {
	return new Coord(Utils.int16d(buf, off), Utils.int16d(buf, off + 2));
    }

    public static BufferedImage readimage(final InputStream fp) throws IOException {
	try {
	    return (BufferedImage)AccessController.doPrivileged(new PrivilegedExceptionAction<BufferedImage>() {
		public BufferedImage run() throws IOException {
		    BufferedImage ret = ImageIO.read(fp);
		    if (ret == null) {
			throw new ImageReadException();
		    } else {
			return ret;
		    }
		}
	    });
	} catch (PrivilegedActionException var3) {
	    PrivilegedActionException e = var3;
	    Throwable c = e.getCause();
	    if (c instanceof IOException) {
		throw (IOException)c;
	    } else {
		throw new AssertionError(c);
	    }
	}
    }

    public Resource(String full, String name, String out, boolean w) throws Exception {
	this.layers = new LinkedList();
	this.out = out;
	this.name = name;
	if (w) {
	    this.load(new FileInputStream(new File(full)));
	} else {
	    this.loadfromdecode(full);
	}

    }

    public Resource(String full, String name, boolean w) throws Exception {
	this(full, name, OUT, w);
    }

    private void readall(InputStream in, byte[] buf) throws IOException {
	int ret;
	for(int off = 0; off < buf.length; off += ret) {
	    ret = in.read(buf, off, buf.length - off);
	    if (ret < 0) {
		throw new LoadException("Incomplete resource at " + this.name, this);
	    }
	}

    }

    private void load(InputStream in) throws Exception {
	byte[] buf = new byte["Haven Resource 1".length()];
	this.readall(in, buf);
	if (!"Haven Resource 1".equals(new String(buf))) {
	    throw new LoadException("Invalid res signature", this);
	} else {
	    buf = new byte[2];
	    this.readall(in, buf);
	    this.ver = Utils.uint16d(buf, 0);
	    List<Layer> layers = new LinkedList();

	    label63:
	    while(true) {
		StringBuilder tbuf = new StringBuilder();

		int ib;
		while((ib = in.read()) != -1) {
		    int bb = (byte)ib;
		    if (bb == 0) {
			buf = new byte[4];
			this.readall(in, buf);
			bb = Utils.int32d(buf, 0);
			buf = new byte[bb];
			this.readall(in, buf);
			String layerName = tbuf.toString();

			Class<? extends Layer> lc = (Class)ltypes.get(layerName);
			if (lc == null) {
			    System.out.println(String.format("Couldn't find  layer class for '%s'", layerName));
			} else {
			    Constructor cons;
			    try {
				cons = lc.getConstructor(Resource.class, byte[].class);
			    } catch (NoSuchMethodException var12) {
				NoSuchMethodException e = var12;
				throw new LoadException(e, this);
			    }

			    Layer l;
			    try {
				l = (Layer)cons.newInstance(this, buf);
			    } catch (InstantiationException var13) {
				InstantiationException e = var13;
				throw new LoadException(e, this);
			    } catch (InvocationTargetException var14) {
				InvocationTargetException e = var14;
				Throwable c = e.getCause();
				if (c instanceof RuntimeException) {
				    throw (RuntimeException)c;
				}

				throw new LoadException(c, this);
			    } catch (IllegalAccessException var15) {
				IllegalAccessException e = var15;
				throw new LoadException(e, this);
			    }

			    layers.add(l);
			}
			continue label63;
		    }

		    tbuf.append((char)bb);
		}

		if (tbuf.length() != 0) {
		    throw new LoadException("Incomplete resource at " + this.name, this);
		}

		this.layers = layers;
		Iterator var16 = layers.iterator();

		while(var16.hasNext()) {
		    Layer l = (Layer)var16.next();
		    l.init();
		}

		return;
	    }
	}
    }

    public void decodeall() throws Exception {
	String base = this.out + this.name;
	(new File(base)).mkdirs();
	int[] c = new int[TYPES];

	for(int i = 0; i < TYPES; ++i) {
	    c[i] = 0;
	}

	Iterator var5 = this.layers.iterator();

	while(var5.hasNext()) {
	    Layer l = (Layer)var5.next();
	    l.decode(base, c[l.type()]++);
	}

	BufferedWriter bw = new BufferedWriter(new FileWriter(base + "/meta"));
	bw.write("#General info for res " + base + "\r\n");
	bw.write("#int16 ver\r\n");
	bw.write(Integer.toString(this.ver) + "\r\n");
	bw.flush();
	bw.close();
    }

    private void loadfromdecode(String full) throws Exception {
	if (!full.endsWith(".res")) {
	    throw new Exception("Invalid decoded res directory");
	} else {
	    File f = new File(full);
	    if (!f.isDirectory()) {
		throw new Exception("Invalid decoded res directory");
	    } else {
		File[] l = f.listFiles();
		List<Layer> layers = new LinkedList();

		label208:
		for(int i = 0; i < l.length; ++i) {
		    if (l[i].isDirectory()) {
			String n = l[i].getName();
			Class<? extends Layer> lc = (Class)ltypes.get(n);
			if (lc != null) {
			    File[] df = l[i].listFiles();
			    Arrays.sort(df, new Comparator<File>(){
				public int compare(File f1, File f2)
				{
				    return String.valueOf(f1.getName()).compareTo(f2.getName());
				}
			    });
			    int j;
			    Constructor cons;
			    NoSuchMethodException e;
			    switch (n) {
				case "image":
				case "tile":
				    if (df.length % 2 != 0) {
					throw new Exception("Invalid number of decoded files for " + n);
				    }

				    try {
					cons = lc.getConstructor(Resource.class, File.class, File.class);
				    } catch (NoSuchMethodException var19) {
					e = var19;
					throw new LoadException(e, this);
				    }

				    j = 0;

				    for(; j < df.length - 1; ++j) {
					if (df[j].getName().endsWith(".data") || df[j + 1].getName().endsWith(".png")) {
					    layers.add((Layer) cons.newInstance(this, df[j++], df[j]));
					}
				    }
				    break;
				case "code":
				    if (df.length % 2 != 0) {
					throw new Exception("Invalid number of decoded files for " + n);
				    }

				    try {
					cons = lc.getConstructor(Resource.class, File.class, File.class);
				    } catch (NoSuchMethodException var18) {
					e = var18;
					throw new LoadException(e, this);
				    }

				    j = 0;

				    while(true) {
					if (j >= df.length - 1) {
					    continue label208;
					}

					if (df[j].getName().endsWith(".data")) {
					    layers.add((Layer) cons.newInstance(this, df[j], df[j + 1]));
					} else if (df[j].getName().endsWith(".class")) {
					    layers.add((Layer) cons.newInstance(this, df[j + 1], df[j]));
					}

					j += 2;
				    }
				case "neg":
				case "anim":
				case "tooltip":
				case "ntooltip":
				case "markdown":
				case "overlay":
				case "font":
				case "tileset2":
				case "mat2":
				case "tileset":
				case "codeentry":
				case "pagina":
				case "tex":
				case "action":
				    try {
					cons = lc.getConstructor(Resource.class, File.class);
				    } catch (NoSuchMethodException var17) {
					e = var17;
					throw new LoadException(e, this);
				    }

				    j = 0;

				    while(true) {
					if (j >= df.length) {
					    continue label208;
					}

					if (df[j].getName().endsWith(".data") || df[j].getName().endsWith(".ttf")) {
					    layers.add((Layer) cons.newInstance(this, df[j]));
					}

					++j;
				    }
				case "midi":
				    try {
					cons = lc.getConstructor(Resource.class, File.class);
				    } catch (NoSuchMethodException var16) {
					e = var16;
					throw new LoadException(e, this);
				    }

				    j = 0;

				    while(true) {
					if (j >= df.length) {
					    continue label208;
					}

					if (df[j].getName().endsWith(".midi")) {
					    layers.add((Layer) cons.newInstance(this, df[j]));
					}

					++j;
				    }
				case "audio2":
				    try {
					cons = lc.getConstructor(Resource.class, File.class);
				    } catch (NoSuchMethodException var15) {
					e = var15;
					throw new LoadException(e, this);
				    }

				    j = 0;

				    while(true) {
					if (j >= df.length) {
					    continue label208;
					}

					if (df[j].getName().endsWith(".ogg")) {
					    layers.add((Layer) cons.newInstance(this, df[j]));
					}

					++j;
				    }
				case "src":
				    try {
					cons = lc.getConstructor(Resource.class, File.class);
				    } catch (NoSuchMethodException var14) {
					e = var14;
					throw new LoadException(e, this);
				    }

				    for(j = 0; j < df.length; ++j) {
					if (df[j].getName().endsWith(".java")) {
					    layers.add((Layer) cons.newInstance(this, df[j]));
					}
				    }
			    }
			}
		    }
		}

		this.layers = layers;
		BufferedReader br = new BufferedReader(new FileReader(full + "/meta"));
		this.ver = Utils.rnint(br);
		br.close();
	    }
	}
    }

    public void encodeall() throws Exception {
	File f = new File(this.out + this.name);
	f.mkdirs();
	f.delete();
	f.createNewFile();
	FileOutputStream fos = new FileOutputStream(f);
	byte[] buf = BSIG;
	fos.write(buf);
	buf = Utils.byte_int16d(this.ver);
	fos.write(buf);
	Iterator var4 = this.layers.iterator();

	while(var4.hasNext()) {
	    Layer l = (Layer)var4.next();
	    fos.write(l.type_buffer());
	    fos.write(Utils.byte_int32d(l.size()));
	    l.encode(fos);
	}

	fos.flush();
	fos.close();
    }

    static {
	IMAGE = TYPES++;
	TILE = TYPES++;
	NEG = TYPES++;
	OBST = TYPES++;
	ANIM = TYPES++;
	TILESET = TYPES++;
	PAGINA = TYPES++;
	ABUTTON = TYPES++;
	AUDIO = TYPES++;
	TOOLTIP = TYPES++;
	MUSIC = TYPES++;
	CODE = TYPES++;
	CODEENTRY = TYPES++;
	SOURCES = TYPES++;
	NTOOLTIP = TYPES++;
	MTOOLTIP = TYPES++;
	MAT2 = TYPES++;
	TILESET2 = TYPES++;
	TEX = TYPES++;
	OVERLAY = TYPES++;
	FONT = TYPES++;
	ltypes.put("image", Image.class);
	ltypes.put("tooltip", Tooltip.class);
	ltypes.put("tile", Tile.class);
	ltypes.put("neg", Neg.class);
	ltypes.put("obst", Obst.class);
	ltypes.put("anim", Anim.class);
	ltypes.put("tileset", Tileset.class);
	ltypes.put("pagina", Pagina.class);
	ltypes.put("action", AButton.class);
	ltypes.put("code", Code.class);
	ltypes.put("codeentry", CodeEntry.class);
	ltypes.put("audio2", Audio.class);
	ltypes.put("midi", Music.class);
	ltypes.put("src", Sources.class);
	ltypes.put("mat2", NewMat.class);
	ltypes.put("tileset2", TileSet2.class);
	ltypes.put("ntooltip", NTooltip.class);
	ltypes.put("markdown", MTooltip.class);
	ltypes.put("overlay", Overlay.class);
	ltypes.put("tex", Tex.class);
	ltypes.put("font", Font.class);
    }

    public class Font extends Layer {
	private byte[] raw;
	private int version = 1;
	private int type = 0;

	public Font(byte[] buf) {
	    MessageBuf msg = new MessageBuf(buf);
	    version = msg.uint8();
	    if (version != 1) throw new RuntimeException("Unsupported font version");

	    type = msg.uint8();
	    if (type != 0) throw new RuntimeException("Unsupported font type");

	    raw = msg.bytes(); // Читаем оставшиеся данные
	}

	public Font(File ttfFile) throws IOException {
	    try (FileInputStream fis = new FileInputStream(ttfFile)) {
		raw = new byte[(int) ttfFile.length()];
		fis.read(raw);
	    }
	}

	public int size() {
	    return 2 + raw.length; // Версия + тип + данные
	}

	public int type() {
	    return Resource.FONT;
	}

	public byte[] type_buffer() {
	    return new byte[]{102, 111, 110, 116, 0}; // "font" в ASCII
	}

	public void init() {}

	public void decode(String res, int i) throws IOException {
	    File dir = new File(res + "/font/");
	    dir.mkdirs();

	    File output = new File(dir, "font_" + i + ".ttf");
	    try (FileOutputStream fos = new FileOutputStream(output)) {
		fos.write(raw);
	    }
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(version);   // Версия слоя
	    out.write(type);      // Тип шрифта
	    out.write(raw);      // Данные шрифта
	}
    }

    public class Tex extends Layer {
	short m_id;
	private int size = 0;
	Coord m_off;
	Coord m_sz;
	BufferedImage image;
	byte m_minf;
	byte m_magf;
	byte m_ma;
	byte[] mask = null;
	MessageBuf raw;

	public Tex(byte[] buf) throws Exception {
	    super();
	    MessageBuf msg = new MessageBuf(buf);
	    this.raw = msg;
	    this.m_id = msg.int16();
	    this.m_off = msg.coord16();
	    this.m_sz = msg.coord16();

	    while(true) {
		while(msg.rem() > 0) {
		    int m_p = msg.int8();
		    int size;
		    switch (m_p) {
			case 0:
			    size = msg.int32();
			    byte[] buff = new byte[size];

			    for(int ix = 0; ix < size; ++ix) {
				buff[ix] = msg.int8();
			    }

			    ByteArrayInputStream bais = new ByteArrayInputStream(buff);

			    try {
				this.image = ImageIO.read(bais);
				break;
			    } catch (IOException var9) {
				IOException e = var9;
				throw new RuntimeException(e);
			    }
			case 1:
			    this.m_ma = msg.uint8();
			    break;
			case 2:
			    this.m_magf = msg.uint8();
			    break;
			case 3:
			    this.m_minf = msg.uint8();
			    break;
			case 4:
			    size = msg.int32();
			    this.mask = new byte[size];

			    for(int i = 0; i < size; ++i) {
				this.mask[i] = msg.int8();
			    }
			case 5:
		    }
		}

		msg.rem();
		return;
	    }
	}

	public Tex(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.m_id = Short.parseShort(Utils.rnstr(br));
	    this.m_off = Utils.parseCoord(Utils.rnstr(br));
	    this.m_sz = Utils.parseCoord(Utils.rnstr(br));
	    this.m_ma = Byte.parseByte(Utils.rnstr(br));
	    this.m_magf = Byte.parseByte(Utils.rnstr(br));
	    this.m_minf = Byte.parseByte(Utils.rnstr(br));
	    br.close();
	    String str_id = data.getName().substring(data.getName().lastIndexOf("_") + 1, data.getName().lastIndexOf("."));
	    int id = Integer.parseInt(str_id);
	    String path = data.getParent();
	    File img = new File(path, "image_" + id + ".png");
	    this.image = ImageIO.read(img);
	    this.size = this.build().fin().length;
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.TEX;
	}

	public byte[] type_buffer() {
	    return new byte[]{116, 101, 120, 0};
	}

	public void init() {
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/tex/tex_" + i + ".data");
	    (new File(res + "/tex/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TEX LAYER FOR RES " + res + "\r\n");
	    bw.write("#short m_id\r\n");
	    bw.write(this.m_id + "\r\n");
	    bw.write("#Coord m_off\r\n");
	    bw.write(this.m_off + "\r\n");
	    bw.write("#Coord m_sz\r\n");
	    bw.write(this.m_sz + "\r\n");
	    bw.write("#byte m_ma\r\n");
	    bw.write(this.m_ma + "\r\n");
	    bw.write("#byte m_magf\r\n");
	    bw.write(this.m_magf + "\r\n");
	    bw.write("#byte m_minf\r\n");
	    bw.write(this.m_minf + "\r\n");
	    if (this.mask != null) {
		for(int j = 0; j < this.mask.length; ++j) {
		    bw.write(this.mask[j]);
		}
	    }

	    bw.flush();
	    bw.close();
	    String path = res + "tex/image_" + i + ".png";
	    File outputfile = new File(path);
	    ImageIO.write(this.image, "png", outputfile);
	}

	MessageBuf build() {
	    MessageBuf msg = new MessageBuf();
	    msg.adduint16(this.m_id);
	    msg.addcoord16(this.m_off);
	    msg.addcoord16(this.m_sz);
	    msg.addint8((byte)0);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    try {
		ImageIO.write(this.image, "png", baos);
	    } catch (IOException var4) {
		IOException e = var4;
		throw new RuntimeException(e);
	    }

	    byte[] bytes = baos.toByteArray();
	    msg.addint32(bytes.length);
	    msg.addbytes(bytes);
	    if (this.m_ma != 0) {
		msg.addint8((byte)1);
		msg.addint8(this.m_ma);
	    }

	    if (this.m_magf != 0) {
		msg.addint8((byte)2);
		msg.addint8(this.m_magf);
	    }

	    if (this.m_minf != 0) {
		msg.addint8((byte)3);
		msg.addint8(this.m_minf);
	    }

	    return msg;
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.build().fin());
	}
    }

    public class Overlay extends Layer {
	private final int size;
	private byte ver;
	private byte matid;
	private byte omatid;
	private Collection<String> tags;

	public Overlay(byte[] buf) throws Exception {
	    super();
	    this.size = 0;
	    MessageBuf msg = new MessageBuf(buf);
	    this.ver = msg.int8();
	    Collection<String> tags = Collections.emptyList();
	    Object[] var5 = msg.list();
	    int var6 = var5.length;

	    for(int var7 = 0; var7 < var6; ++var7) {
		Object obj = var5[var7];
		Object[] arg = (Object[])((Object[])obj);
		switch ((String)arg[0]) {
		    case "tags":
			ArrayList<String> tbuf = new ArrayList();

			for(int i = 1; i < arg.length; ++i) {
			    tbuf.add(((String)arg[i]).intern());
			}

			tbuf.trimToSize();
			tags = tbuf;
			break;
		    case "mat":
			this.matid = (Byte)arg[1];
			break;
		    case "omat":
			this.omatid = (Byte)arg[1];
		}
	    }

	    this.tags = (Collection)tags;
	}

	public Overlay(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.tags = new ArrayList();
	    this.ver = Byte.parseByte(Utils.rnstr(br));
	    this.matid = Byte.parseByte(Utils.rnstr(br));
	    this.omatid = Byte.parseByte(Utils.rnstr(br));
	    int size = Integer.parseInt(Utils.rnstr(br));

	    for(int i = 0; i < size; ++i) {
		String str = Utils.rnstr(br);
		this.tags.add(str);
	    }

	    br.close();
	    this.size = this.build().fin().length;
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.OVERLAY;
	}

	public byte[] type_buffer() {
	    return new byte[]{111, 118, 101, 114, 108, 97, 121, 0};
	}

	public void init() {
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/overlay/overlay_" + i + ".data");
	    (new File(res + "/overlay/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#OVERLAY LAYER FOR RES " + res + "\r\n");
	    bw.write("#Ver\r\n");
	    bw.write(this.ver + "\r\n");
	    bw.write("#matid\r\n");
	    bw.write(this.matid + "\r\n");
	    bw.write("#omatid\r\n");
	    bw.write(this.omatid + "\r\n");
	    bw.write("#tags size\r\n");
	    bw.write(this.tags.size() + "\r\n");
	    Iterator var5 = this.tags.iterator();

	    while(var5.hasNext()) {
		String tag = (String)var5.next();
		bw.write(tag + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	}

	MessageBuf build() {
	    MessageBuf msg = new MessageBuf();
	    msg.adduint8(this.ver);
	    ArrayList<Object[]> m_ta = new ArrayList();
	    ArrayList<String> m_tags = new ArrayList();
	    m_tags.add("tags");
	    m_tags.addAll(this.tags);
	    m_ta.add(m_tags.toArray());
	    if (this.matid != 0) {
		m_ta.add(new Object[]{"mat", this.matid});
	    }

	    if (this.omatid != 0) {
		m_ta.add(new Object[]{"omat", this.omatid});
	    }

	    Iterator var4 = m_ta.iterator();

	    while(var4.hasNext()) {
		Object obj = var4.next();
		msg.adduint8(8);
		msg.addlist((Object[])((Object[])obj));
		msg.adduint8(0);
	    }

	    return msg;
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.build().fin());
	}
    }

    public class NTooltip extends Layer {
	public final String name;
	public final String req;
	public final String t;
	private int size = 0;

	public NTooltip(byte[] buf) throws Exception {
	    super();
	    int[] off = new int[]{0};
	    this.name = Utils.strd(buf, off);
	    this.req = Utils.strd(buf, off);
	    this.t = Utils.strd(buf, off);
	}

	public NTooltip(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.name = Utils.rnstr(br);
	    this.size = Utils.byte_strd(this.name).length;
	    this.req = Utils.rnstr(br);
	    this.size += Utils.byte_strd(this.req).length;
	    this.t = Utils.rnstr(br);
	    this.size += Utils.byte_strd(this.t).length;
	    br.close();
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.NTOOLTIP;
	}

	public byte[] type_buffer() {
	    return new byte[]{110, 116, 111, 111, 108, 116, 105, 112, 0};
	}

	public void init() {
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/ntooltip/ntooltip_" + i + ".data");
	    (new File(res + "/ntooltip/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#NTOOLTIP LAYER FOR RES " + res + "\r\n");
	    bw.write("#String name\r\n");
	    bw.write(this.name + "\r\n");
	    bw.write("#String req\r\n");
	    bw.write(this.req + "\r\n");
	    bw.write("#String t\r\n");
	    bw.write(this.t.replace("\n", "\\n") + "\r\n");
	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_strd(this.name));
	    out.write(Utils.byte_strd(this.req));
	    out.write(Utils.byte_strd(this.t));
	}
    }

    public class MTooltip extends Layer {
	public final byte[] content;  // Храним весь контент как массив байтов
	private int size = 0;

	// Конструктор для инициализации из byte массива
	public MTooltip(byte[] buf) {
	    super();
	    this.content = buf;  // Просто сохраняем массив байтов
	    this.size = buf.length;  // Размер равен длине массива
	}

	// Конструктор для инициализации из файла
	public MTooltip(File data) throws IOException {
	    super();
	    this.content = readFileToByteArray(data);  // Читаем весь файл как byte[]
	    this.size = this.content.length;  // Размер равен длине контента
	}

	// Метод чтения файла как byte[]
	private byte[] readFileToByteArray(File file) throws IOException {
	    try (FileInputStream fis = new FileInputStream(file)) {
		return fis.readAllBytes();  // Читаем весь файл в byte массив
	    }
	}

	// Метод записи byte[] в файл
	private void writeByteArrayToFile(byte[] data, File file) throws IOException {
	    try (FileOutputStream fos = new FileOutputStream(file)) {
		fos.write(data);  // Записываем массив байтов в файл
	    }
	}

	// Возвращает размер
	public int size() {
	    return this.size;
	}

	// Возвращает тип
	public int type() {
	    return Resource.MTOOLTIP;
	}

	// Возвращает тип в виде байтового массива
	public byte[] type_buffer() {
	    return new byte[]{109, 97, 114, 107, 100, 111, 119, 110, 0};  // "markdown" в ASCII
	}

	// Метод инициализации
	public void init() {
	}

	// Метод декодирования: сохраняем контент в файл
	public void decode(String res, int i) throws IOException {
	    File f = new File(res + "/markdown/markdown" + i + ".data");
	    (new File(res + "/markdown/")).mkdirs();
	    f.createNewFile();
	    writeByteArrayToFile(this.content, f);  // Записываем byte[] в файл
	}

	// Метод кодирования: записываем контент как байты в OutputStream
	public void encode(OutputStream out) throws IOException {
	    out.write(this.content);  // Записываем весь контент в поток
	}
    }

    public class TileSet2 extends Layer {
	private int size = 0;
	MessageBuf raw;
	String m_tn;
	Object[] m_ta;
	short m_flavprob;
	ArrayList<Flavobj> flavobjs = new ArrayList();
	ArrayList<String> m_tags = new ArrayList();

	public void init() {
	}

	public TileSet2(byte[] buf) throws Exception {
	    super();
	    this.size = buf.length;
	    MessageBuf msg = new MessageBuf(buf);
	    this.raw = msg;

	    while(true) {
		label29:
		while(msg.rem() > 0) {
		    int m_p = msg.int8();
		    int i;
		    switch (m_p) {
			case 0:
			    this.m_tn = msg.string();
			    this.m_ta = msg.list();
			    break;
			case 1:
			    int flnum = msg.int16();
			    this.m_flavprob = msg.int16();
			    i = 0;

			    while(true) {
				if (i >= flnum) {
				    continue label29;
				}

				Flavobj f1 = new Flavobj();
				f1.fln = msg.string();
				f1.flv = msg.int16();
				f1.flw = msg.int8();
				this.flavobjs.add(f1);
				++i;
			    }
			case 2:
			    int l = msg.int8();

			    for(i = 0; i < l; ++i) {
				this.m_tags.add(msg.string());
			    }
			    break;
			default:
			    System.out.println("Invalid tileset part " + m_p);
		    }
		}

		msg.rem();
		return;
	    }
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.TILESET2;
	}

	public byte[] type_buffer() {
	    return new byte[]{116, 105, 108, 101, 115, 101, 116, 50, 0};
	}

	public TileSet2(File data) throws Exception {
	    super();
	    this.size = 0;
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.m_tn = Utils.rnstr(br);
	    this.m_flavprob = Short.parseShort(Utils.rnstr(br));
	    int m_ta_size = Integer.parseInt(Utils.rnstr(br));
	    ArrayList<Object[]> amta = new ArrayList();

	    int m_tag_size;
	    for(m_tag_size = 0; m_tag_size < m_ta_size; ++m_tag_size) {
		byte v;
		Object[] objx;
		String key = Utils.rnstr(br);
		switch (key) {
		    case "undefined":
			v = Byte.parseByte(Utils.rnstr(br));
			objx = new Object[]{v};
			amta.add(objx);
			break;
		    case "base":
			v = Byte.parseByte(Utils.rnstr(br));
			objx = new Object[]{key, v};
			amta.add(objx);
			break;
		    case "var":
			v = Byte.parseByte(Utils.rnstr(br));
			float v1x = Float.parseFloat(Utils.rnstr(br));
			Object[] objxx = new Object[]{key, v, v1x};
			amta.add(objxx);
		    case "common-mat":
			break;
		    case "rmat":
			String vx = Utils.rnstr(br);
			byte v1 = Byte.parseByte(Utils.rnstr(br));
			float v2 = Float.parseFloat(Utils.rnstr(br));
			Object[] obj = new Object[]{key, vx, v1, v2};
			amta.add(obj);
			break;
		    default:
			throw new IllegalStateException("Unexpected value: " + key);
		}
	    }

	    this.m_ta = amta.toArray();
	    m_tag_size = Integer.parseInt(Utils.rnstr(br));

	    int flav_size;
	    for(flav_size = 0; flav_size < m_tag_size; ++flav_size) {
		this.m_tags.add(Utils.rnstr(br));
	    }

	    flav_size = Integer.parseInt(Utils.rnstr(br));

	    for(int i = 0; i < flav_size; ++i) {
		Flavobj fl = new Flavobj();
		fl.flv = Short.parseShort(Utils.rnstr(br));
		fl.fln = Utils.rnstr(br);
		fl.flw = Byte.parseByte(Utils.rnstr(br));
		this.flavobjs.add(fl);
	    }

	    br.close();
	    this.size = this.build().fin().length;
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/tileset2/tileset2_" + i + ".data");
	    (new File(res + "/tileset2/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TILESET2 LAYER FOR RES " + res + "\r\n");
	    bw.write("#String m_tn\r\n");
	    bw.write(this.m_tn + "\r\n");
	    bw.write("#Int m_flavprob\r\n");
	    bw.write(this.m_flavprob + "\r\n");
	    bw.write("#Size of m_ta\r\n");
	    bw.write(this.m_ta.length + "\r\n");
	    bw.write("#m_ta value\r\n");
	    Object[] var5 = this.m_ta;
	    int var6 = var5.length;

	    for(int var7 = 0; var7 < var6; ++var7) {
		Object obj = var5[var7];
		String key;
		if (obj instanceof Byte) {
		    key = "undefined";
		} else {
		    key = (String)((Object[])((Object[])obj))[0];
		}

		bw.write(key + "\r\n");
		switch (key) {
		    case "base":
			bw.write(((Object[])((Object[])obj))[1] + "\r\n");
			break;
		    case "var":
			bw.write(((Object[])((Object[])obj))[1] + "\r\n");
			bw.write(((Object[])((Object[])obj))[2] + "\r\n");
		    case "common-mat":
		    default:
			break;
		    case "rmat":
			bw.write(((Object[])((Object[])obj))[1] + "\r\n");
			bw.write(((Object[])((Object[])obj))[2] + "\r\n");
			bw.write(((Object[])((Object[])obj))[3] + "\r\n");
		}
	    }

	    bw.write("#String m_tags size\r\n");
	    bw.write(this.m_tags.size() + "\r\n");
	    bw.write("#M_Tags\r\n");
	    Iterator var12 = this.m_tags.iterator();

	    while(var12.hasNext()) {
		String tag = (String)var12.next();
		bw.write(tag + "\r\n");
	    }

	    bw.write("#Flav objs size\r\n");
	    bw.write(this.flavobjs.size() + "\r\n");
	    bw.write("#Flav objs\r\n");
	    var12 = this.flavobjs.iterator();

	    while(var12.hasNext()) {
		Flavobj flv = (Flavobj)var12.next();
		bw.write(flv.flv + "\r\n");
		bw.write(flv.fln + "\r\n");
		bw.write(flv.flw + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	}

	MessageBuf build() {
	    MessageBuf msg = new MessageBuf();
	    msg.adduint8(0);
	    msg.addstring(this.m_tn);
	    Object[] var2 = this.m_ta;
	    int var3 = var2.length;

	    for(int var4 = 0; var4 < var3; ++var4) {
		Object obj = var2[var4];
		msg.adduint8(8);
		msg.addlist((Object[])((Object[])obj));
		msg.adduint8(0);
	    }

	    msg.adduint8(0);
	    msg.adduint8(1);
	    msg.adduint16(this.flavobjs.size());
	    msg.adduint16(this.m_flavprob);
	    Iterator var6 = this.flavobjs.iterator();

	    while(var6.hasNext()) {
		Flavobj fl = (Flavobj)var6.next();
		msg.addstring(fl.fln);
		msg.addint16(fl.flv);
		msg.addint8(fl.flw);
	    }

	    if (this.m_tags.size() > 0) {
		msg.adduint8(2);
		msg.adduint8(this.m_tags.size());
		var6 = this.m_tags.iterator();

		while(var6.hasNext()) {
		    String tag = (String)var6.next();
		    msg.addstring(tag);
		}
	    }

	    return msg;
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.build().fin());
	}

	class Flavobj {
	    String fln;
	    short flv;
	    byte flw;

	    Flavobj() {
	    }
	}
    }

    public class NewMat extends Layer {
	private short id;
	private int size = 0;
	MessageBuf raw;
	ArrayList<Variant> vars = new ArrayList();

	public void init() {
	}

	public NewMat(byte[] buf) throws Exception {
	    super();
	    MessageBuf msg = new MessageBuf(buf);
	    this.raw = msg;
	    msg.size();
	    this.id = msg.int16();

	    while(msg.rem() > 0) {
		Variant var = new Variant();
		var.name = msg.string();
		var.list = msg.list();
		this.vars.add(var);
	    }

	}

	public NewMat(File file) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	    this.id = Short.parseShort(Utils.rnstr(br));
	    int varsize = Integer.parseInt(Utils.rnstr(br));
	    this.vars = new ArrayList();

	    for(int i = 0; i < varsize; ++i) {
		Color vx;
		Object[] objxxxx;
		String v;
		byte v1x;
		byte v2x;
		String key = Utils.rnstr(br);
		switch (key) {
		    case "tex":
			v = Utils.rnstr(br);
			if (!String.valueOf(Integer.parseInt(v)).equals(v)) {
			    v1x = Byte.parseByte(Utils.rnstr(br));
			    v2x = Byte.parseByte(Utils.rnstr(br));
			    String v3x = Utils.rnstr(br);
			    Object[] obj = new Object[]{v, v1x, v2x, v3x};
			    this.vars.add(new Variant(key, obj));
			} else {
			    objxxxx = new Object[]{0};
			    this.vars.add(new Variant(key, objxxxx));
			}
			break;
		    case "light":
			v = Utils.rnstr(br);
			objxxxx = new Object[]{v};
			this.vars.add(new Variant(key, objxxxx));
			break;
		    case "bump":
			v = Utils.rnstr(br);
			v1x = Byte.parseByte(Utils.rnstr(br));
			v2x = Byte.parseByte(Utils.rnstr(br));
			Object[] objxxx = new Object[]{v, v1x, v2x};
			this.vars.add(new Variant(key, objxxx));
			break;
		    case "col":
			vx = Utils.extractColor(Utils.rnstr(br));
			Color v1 = Utils.extractColor(Utils.rnstr(br));
			Color v2 = Utils.extractColor(Utils.rnstr(br));
			Color v3 = Utils.extractColor(Utils.rnstr(br));
			float v4 = Float.parseFloat(Utils.rnstr(br));
			Object[] objx = new Object[]{vx, v1, v2, v3, v4};
			this.vars.add(new Variant(key, objx));
			break;
		    case "maskdepth":
			Object[] objxx = new Object[0];
			this.vars.add(new Variant(key, objxx));
			break;
		    case "vcol":
			vx = Utils.extractColor(Utils.rnstr(br));
			objxxxx = new Object[]{vx};
			this.vars.add(new Variant(key, objxxxx));
			break;
		    default:
			this.vars.add(new Variant(key, new Object[0]));
		}
	    }

	    this.size = this.build().fin().length;
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.MAT2;
	}

	public byte[] type_buffer() {
	    return new byte[]{109, 97, 116, 50, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/mat2/mat2_" + i + ".data");
	    (new File(res + "/mat2/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TILESET2 LAYER FOR RES " + res + "\r\n");
	    bw.write("#ID \r\n");
	    bw.write(this.id + "\r\n");
	    bw.write("#VarSize \r\n");
	    bw.write(this.vars.size() + "\r\n");
	    bw.write("#Var \r\n");
	    Iterator var5 = this.vars.iterator();

	    while(var5.hasNext()) {
		Variant var = (Variant)var5.next();
		bw.write(var.name + "\r\n");
		Object[] var7 = var.list;
		int var8 = var7.length;

		for(int var9 = 0; var9 < var8; ++var9) {
		    Object obj = var7[var9];
		    if (obj instanceof Color) {
			bw.write(Utils.strcolor((Color)obj) + "\r\n");
		    } else {
			bw.write(obj + "\r\n");
		    }
		}
	    }

	    bw.flush();
	    bw.close();
	}

	MessageBuf build() {
	    MessageBuf msg = new MessageBuf();
	    msg.adduint16(this.id);
	    Iterator var2 = this.vars.iterator();

	    while(var2.hasNext()) {
		Variant var = (Variant)var2.next();
		msg.addstring(var.name);
		msg.addlist(var.list);
		msg.adduint8(0);
	    }

	    return msg;
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.build().fin());
	}

	class Variant {
	    String name;
	    Object[] list;

	    public Variant(String key, Object[] obj) {
		this.name = key;
		this.list = obj;
	    }

	    public Variant() {
	    }
	}
    }

    public class Sources extends Layer {
	byte[] raw;

	public Sources(byte[] buf) {
	    super();
	    this.raw = new byte[buf.length];

	    for(int i = 0; i < buf.length; ++i) {
		this.raw[i] = buf[i];
	    }

	}

	public Sources(File src) throws Exception {
	    super();
	    FileInputStream fis = new FileInputStream(src);
	    this.raw = new byte[(int)src.length()];
	    fis.read(this.raw);
	    fis.close();
	}

	public void init() {
	}

	public int size() {
	    return this.raw.length;
	}

	public int type() {
	    return Resource.SOURCES;
	}

	public byte[] type_buffer() {
	    return new byte[]{115, 114, 99, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/src/src_" + i + ".java");
	    (new File(res + "/src/")).mkdirs();
	    f.createNewFile();
	    FileOutputStream fout = new FileOutputStream(f);
	    fout.write(this.raw);
	    fout.flush();
	    fout.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.raw);
	}
    }

    public class Music extends Layer {
	byte[] raw;

	public Music(byte[] buf) {
	    super();
	    this.raw = new byte[buf.length];

	    for(int i = 0; i < buf.length; ++i) {
		this.raw[i] = buf[i];
	    }

	}

	public Music(File midi) throws Exception {
	    super();
	    FileInputStream fis = new FileInputStream(midi);
	    this.raw = new byte[(int)midi.length()];
	    fis.read(this.raw);
	    fis.close();
	}

	public int size() {
	    return this.raw.length;
	}

	public int type() {
	    return Resource.MUSIC;
	}

	public byte[] type_buffer() {
	    return new byte[]{109, 105, 100, 105, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/midi/midi_" + i + ".midi");
	    (new File(res + "/midi/")).mkdirs();
	    f.createNewFile();
	    FileOutputStream fout = new FileOutputStream(f);
	    fout.write(this.raw);
	    fout.flush();
	    fout.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(this.raw);
	}

	public void init() {
	}
    }

    public class Audio extends Layer {
	public transient byte[] coded;
	public String id = "";
	public double bvol = 1.0;
	int version;

	public Audio(Message buf) {
	    super();
	    this.version = buf.int8();
	    if (this.version >= 1 && this.version <= 2) {
		this.id = buf.string();
		if (this.version >= 2) {
		    this.bvol = (double)buf.uint16() * 0.001;
		}

		this.coded = buf.bytes();
	    }

	}

	public Audio(byte[] buf) {
	    super();
	    MessageBuf msg = new MessageBuf(buf);
	    this.version = msg.int8();
	    if (this.version >= 1 && this.version <= 2) {
		this.id = msg.string();
		if (this.version >= 2) {
		    this.bvol = (double)msg.uint16() * 0.001;
		}

		this.coded = msg.bytes();
	    }

	}

	public Audio(File ogg) throws Exception {
	    super();
	    FileInputStream fis = new FileInputStream(ogg);
	    this.coded = new byte[(int)ogg.length()];
	    this.id = ogg.getPath();
	    fis.read(this.coded);
	    fis.close();
	}

	public int size() {
	    try {
		return this.coded.length + 6;
	    } catch (Exception var2) {
		Exception var2x = var2;
		throw new RuntimeException(var2x);
	    }
	}

	public int type() {
	    return Resource.AUDIO;
	}

	public byte[] type_buffer() {
	    return new byte[]{97, 117, 100, 105, 111, 50, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/audio2/audio2_" + i + ".ogg");
	    (new File(res + "/audio2/")).mkdirs();
	    f.createNewFile();
	    FileOutputStream fout = new FileOutputStream(f);
	    fout.write(this.coded);
	    fout.flush();
	    fout.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(2);
	    out.write(Utils.byte_strd("cl"));
	    out.write(Utils.byte_int16d((int)this.bvol));
	    out.write(this.coded);
	}

	public void init() {
	}
    }

    public class CodeEntry extends Layer {
	private int size = 0;
	private ArrayList<String> key = new ArrayList();
	private ArrayList<String> value = new ArrayList();
	private Map<String, Integer> requires = new HashMap();

	public CodeEntry(byte[] buf) {
	    super();
	    MessageBuf msg = new MessageBuf(buf);

	    while(true) {
		while(!msg.eom()) {
		    int t = msg.uint8();
		    String ln;
		    if (t == 1) {
			while(true) {
			    ln = msg.string();
			    String cn = msg.string();
			    if (ln.length() == 0) {
				break;
			    }

			    this.key.add(ln);
			    this.value.add(cn);
			}
		    } else {
			if (t != 2) {
			    throw new LoadException("Unknown codeentry data type: " + t, Resource.this);
			}

			while(true) {
			    ln = msg.string();
			    if (ln.length() == 0) {
				break;
			    }

			    int ver = msg.uint16();
			    this.requires.put(ln, Integer.valueOf(ver));
			}
		    }
		}

		return;
	    }
	}

	public CodeEntry(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    int s = Utils.rnint(br);
	    String t;
	    if (s > 0) {
		++this.size;

		for(int j = 0; j < s; ++j) {
		    t = Utils.rnstr(br);
		    this.key.add(t);
		    this.size += Utils.byte_strd(t).length;
		    t = Utils.rnstr(br);
		    this.value.add(t);
		    this.size += Utils.byte_strd(t).length;
		}

		this.size += 2;
	    }

	    String len = Utils.rnstr(br);
	    if (len != null) {
		s = Integer.parseInt(len);
		++this.size;

		for(int i = 0; i < s; ++i) {
		    t = Utils.rnstr(br);
		    this.size += Utils.byte_strd(t).length;
		    int v = Utils.rnint(br);
		    this.size += 2;
		    this.requires.put(t, v);
		}

		++this.size;
	    }

	    br.close();
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.CODEENTRY;
	}

	public byte[] type_buffer() {
	    return new byte[]{99, 111, 100, 101, 101, 110, 116, 114, 121, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/codeentry/codeentry_" + i + ".data");
	    (new File(res + "/codeentry/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#CODEENTRY LAYER FOR RES " + res + "\r\n");
	    bw.write("#int32 length\r\n");
	    bw.write(this.key.size() + "\r\n");

	    for(int j = 0; j < this.key.size(); ++j) {
		bw.write("#String key[" + j + "]" + "\r\n");
		bw.write(((String)this.key.get(j)).replace("\n", "\\n") + "\r\n");
		bw.write("#String value[" + j + "]" + "\r\n");
		bw.write(((String)this.value.get(j)).replace("\n", "\\n") + "\r\n");
	    }

	    if (!this.requires.isEmpty()) {
		bw.write("#start of requirements\r\n");
		bw.write("#int32 length\r\n");
		Set<Map.Entry<String, Integer>> entries = this.requires.entrySet();
		bw.write(entries.size() + "\r\n");
		Iterator var6 = entries.iterator();

		while(var6.hasNext()) {
		    Map.Entry<String, Integer> e = (Map.Entry)var6.next();
		    bw.write("#String resource\r\n");
		    bw.write(((String)e.getKey()).replace("\n", "\\n") + "\r\n");
		    bw.write("#uint16 version\r\n");
		    bw.write(e.getValue() + "\r\n");
		}
	    }

	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    if (this.key.size() > 0) {
		out.write(1);

		for(int i = 0; i < this.key.size(); ++i) {
		    out.write(Utils.byte_strd((String)this.key.get(i)));
		    out.write(Utils.byte_strd((String)this.value.get(i)));
		}

		out.write(0);
		out.write(0);
	    }

	    if (!this.requires.isEmpty()) {
		out.write(2);
		Iterator var4 = this.requires.entrySet().iterator();

		while(var4.hasNext()) {
		    Map.Entry<String, Integer> e = (Map.Entry)var4.next();
		    out.write(Utils.byte_strd((String)e.getKey()));
		    out.write(Utils.byte_int16d((Integer)e.getValue()));
		}

		out.write(0);
	    }

	}

	public void init() {
	}
    }

    public class Code extends Layer {
	public final String name;
	public final transient byte[] data;
	private int size = 0;

	public Code(byte[] buf) {
	    super();
	    int[] off = new int[]{0};
	    this.name = Utils.strd(buf, off);
	    this.data = new byte[buf.length - off[0]];
	    System.arraycopy(buf, off[0], this.data, 0, this.data.length);
	}

	public Code(File dat, File clas) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dat), "UTF-8"));
	    this.name = Utils.rnstr(br);
	    this.size = Utils.byte_strd(this.name).length;
	    byte[] tmp = Utils.readBytes(clas);
	    if (!Utils.isJavaClass(tmp)) {
		clas = new File(clas.getParentFile() + File.separator + new String(tmp));
		tmp = Utils.readBytes(clas);
	    }

	    this.data = tmp;
	    br.close();
	}

	public int size() {
	    return this.size + this.data.length;
	}

	public int type() {
	    return Resource.CODE;
	}

	public byte[] type_buffer() {
	    return new byte[]{99, 111, 100, 101, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/code/code_" + i + ".data");
	    (new File(res + "/code/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#CODE LAYER FOR RES " + res + "\r\n");
	    bw.write("#String class_name\r\n");
	    bw.write("#Note: the .class file will have the same name as this file\r\n");
	    bw.write(this.name.replace("\n", "\\n") + "\r\n");
	    bw.flush();
	    bw.close();
	    f = new File(res + "/code/code_" + i + ".class");
	    FileOutputStream fout = new FileOutputStream(f);
	    fout.write(this.data);
	    fout.flush();
	    fout.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_strd(this.name));
	    out.write(this.data);
	}

	public void init() {
	}
    }

    public class AButton extends Layer {
	public final String name;
	public final String preq;
	public final char hk;
	public final String[] ad;
	int adl;
	int pver;
	String pr;
	int size = 0;

	public AButton(byte[] buf) {
	    super();
	    int[] off = new int[]{0};
	    this.pr = Utils.strd(buf, off);
	    this.pver = Utils.uint16d(buf, off[0]);
	    off[0] += 2;
	    this.name = Utils.strd(buf, off);
	    this.preq = Utils.strd(buf, off);
	    this.hk = (char)Utils.uint16d(buf, off[0]);
	    off[0] += 2;
	    this.ad = new String[this.adl = Utils.uint16d(buf, off[0])];
	    off[0] += 2;

	    for(int i = 0; i < this.ad.length; ++i) {
		this.ad[i] = Utils.strd(buf, off);
	    }

	}

	public AButton(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.pr = Utils.rnstr(br);
	    this.size = Utils.byte_strd(this.pr).length;
	    this.pver = Utils.rnint(br);
	    this.name = Utils.rnstr(br);
	    this.size += Utils.byte_strd(this.name).length;
	    this.preq = Utils.rnstr(br);
	    this.size += Utils.byte_strd(this.preq).length;
	    this.hk = (char)Utils.rnint(br);
	    this.ad = new String[Utils.rnint(br)];

	    for(int j = 0; j < this.ad.length; ++j) {
		this.ad[j] = Utils.rnstr(br);
		this.size += Utils.byte_strd(this.ad[j]).length;
	    }

	    br.close();
	}

	public int size() {
	    return this.size + 6;
	}

	public int type() {
	    return Resource.ABUTTON;
	}

	public byte[] type_buffer() {
	    return new byte[]{97, 99, 116, 105, 111, 110, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/action/action_" + i + ".data");
	    (new File(res + "/action/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#ABUTTON LAYER FOR RES " + res + "\r\n");
	    bw.write("#String pr\r\n");
	    bw.write(this.pr.replace("\n", "\\n") + "\r\n");
	    bw.write("#uint16 pver\r\n");
	    bw.write(Integer.toString(this.pver) + "\r\n");
	    bw.write("#String name\r\n");
	    bw.write(this.name.replace("\n", "\\n") + "\r\n");
	    bw.write("#String preq\r\n");
	    bw.write(this.preq.replace("\n", "\\n") + "\r\n");
	    bw.write("#uint16 hk\r\n");
	    bw.write(Integer.toString(this.hk) + "\r\n");
	    bw.write("#uint16 ad length\r\n");
	    bw.write(Integer.toString(this.adl) + "\r\n");

	    for(int j = 0; j < this.adl; ++j) {
		bw.write("#String ad[" + j + "]" + "\r\n");
		bw.write(this.ad[j].replace("\n", "\\n") + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_strd(this.pr));
	    out.write(Utils.byte_int16d(this.pver));
	    out.write(Utils.byte_strd(this.name));
	    out.write(Utils.byte_strd(this.preq));
	    out.write(Utils.byte_int16d(this.hk));
	    out.write(Utils.byte_int16d(this.ad.length));

	    for(int j = 0; j < this.ad.length; ++j) {
		out.write(Utils.byte_strd(this.ad[j]));
	    }

	}

	public void init() {
	}
    }

    public class Pagina extends Layer {
	public final String text;
	private int size = 0;

	public Pagina(byte[] buf) {
	    super();

	    try {
		this.text = new String(buf, "UTF-8");
	    } catch (UnsupportedEncodingException var4) {
		UnsupportedEncodingException e = var4;
		throw new LoadException(e, Resource.this);
	    }
	}

	public Pagina(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.text = Utils.rnstr(br);
	    this.size = Utils.byte_strd(this.text).length;
	    br.close();
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.PAGINA;
	}

	public byte[] type_buffer() {
	    return new byte[]{112, 97, 103, 105, 110, 97, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/pagina/pagina_" + i + ".data");
	    (new File(res + "/pagina/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#PAGINA LAYER FOR RES " + res + "\r\n");
	    bw.write("#String text\r\n");
	    bw.write(this.text.replace("\n", "\\n") + "\r\n");
	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_strd(this.text));
	}

	public void init() {
	}
    }

    public class Tileset extends Layer {
	private int fl;
	private String[] fln;
	private int[] flv;
	private int[] flw;
	int flnum;
	int flavprob;
	private int size = 0;

	public Tileset(byte[] buf) {
	    super();
	    int[] off = new int[]{0};
	    int var10005 = off[0];
	    int var10002 = off[0];
	    off[0] = var10005 + 1;
	    this.fl = Utils.ub(buf[var10002]);
	    this.flnum = Utils.uint16d(buf, off[0]);
	    off[0] += 2;
	    this.flavprob = Utils.uint16d(buf, off[0]);
	    off[0] += 2;
	    this.fln = new String[this.flnum];
	    this.flv = new int[this.flnum];
	    this.flw = new int[this.flnum];

	    for(int i = 0; i < this.flnum; ++i) {
		this.fln[i] = Utils.strd(buf, off);
		this.flv[i] = Utils.uint16d(buf, off[0]);
		off[0] += 2;
		int var10006 = off[0];
		int var10003 = off[0];
		off[0] = var10006 + 1;
		this.flw[i] = Utils.ub(buf[var10003]);
	    }

	}

	public Tileset(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.fl = Utils.rnint(br);
	    this.flnum = Utils.rnint(br);
	    this.flavprob = Utils.rnint(br);
	    this.fln = new String[this.flnum];
	    this.flv = new int[this.flnum];
	    this.flw = new int[this.flnum];
	    this.size = 5;

	    for(int j = 0; j < this.flnum; ++j) {
		this.fln[j] = Utils.rnstr(br);
		this.size += Utils.byte_strd(this.fln[j]).length;
		this.flv[j] = Utils.rnint(br);
		this.flw[j] = Utils.rnint(br);
		this.size += 3;
	    }

	    br.close();
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.TILESET;
	}

	public byte[] type_buffer() {
	    return new byte[]{116, 105, 108, 101, 115, 101, 116, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/tileset/tileset_" + i + ".data");
	    (new File(res + "/tileset/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TILESET LAYER FOR RES " + res + "\r\n");
	    bw.write("#Byte fl\r\n");
	    bw.write(Integer.toString(this.fl) + "\r\n");
	    bw.write("#uint16 flnum\r\n");
	    bw.write(Integer.toString(this.flnum) + "\r\n");
	    bw.write("#uint16 flavprob\r\n");
	    bw.write(Integer.toString(this.flavprob) + "\r\n");

	    for(int j = 0; j < this.flnum; ++j) {
		bw.write("#String fln[" + j + "]" + "\r\n");
		bw.write(this.fln[j].replace("\n", "\\n") + "\r\n");
		bw.write("#uint16d flv[" + j + "]" + "\r\n");
		bw.write(Integer.toString(this.flv[j]) + "\r\n");
		bw.write("#byte flw[" + j + "]" + "\r\n");
		bw.write(Integer.toString(this.flw[j]) + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(new byte[]{(byte)(this.fl & 255)});
	    out.write(Utils.byte_int16d(this.flnum));
	    out.write(Utils.byte_int16d(this.flavprob));

	    for(int j = 0; j < this.flnum; ++j) {
		out.write(Utils.byte_strd(this.fln[j]));
		out.write(Utils.byte_int16d(this.flv[j]));
		out.write(new byte[]{(byte)(this.flw[j] & 255)});
	    }

	}

	public void init() {
	}
    }

    public class Anim extends Layer {
	private int[] ids;
	public int id;
	public int d;
	public Image[][] f;

	public Anim(byte[] buf) {
	    super();
	    this.id = Utils.int16d(buf, 0);
	    this.d = Utils.uint16d(buf, 2);
	    this.ids = new int[Utils.uint16d(buf, 4)];
	    if (buf.length - 6 != this.ids.length * 2) {
		throw new LoadException("Invalid anim descriptor in " + Resource.this.name, Resource.this);
	    } else {
		for(int i = 0; i < this.ids.length; ++i) {
		    this.ids[i] = Utils.int16d(buf, 6 + i * 2);
		}

	    }
	}

	public Anim(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.id = Utils.rnint(br);
	    this.d = Utils.rnint(br);
	    this.ids = new int[Utils.rnint(br)];

	    for(int j = 0; j < this.ids.length; ++j) {
		this.ids[j] = Utils.rnint(br);
	    }

	    br.close();
	}

	public int size() {
	    int s = 6;
	    s += 2 * this.ids.length;
	    return s;
	}

	public int type() {
	    return Resource.ANIM;
	}

	public byte[] type_buffer() {
	    return new byte[]{97, 110, 105, 109, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/anim/anim_" + i + ".data");
	    (new File(res + "/anim/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#ANIM LAYER FOR RES " + res + "\r\n");
	    bw.write("#int16 id [keep -1]\r\n");
	    bw.write(Integer.toString(this.id) + "\r\n");
	    bw.write("#uint16 d [duration of animation]\r\n");
	    bw.write(Integer.toString(this.d) + "\r\n");
	    bw.write("#uint16 ids [length]\r\n");
	    bw.write(Integer.toString(this.ids.length) + "\r\n");

	    for(int j = 0; j < this.ids.length; ++j) {
		bw.write("#uint16 ids[" + j + "]" + "\r\n");
		bw.write(Integer.toString(this.ids[j]) + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_int16d(this.id));
	    out.write(Utils.byte_int16d(this.d));
	    out.write(Utils.byte_int16d(this.ids.length));

	    for(int i = 0; i < this.ids.length; ++i) {
		out.write(Utils.byte_int16d(this.ids[i]));
	    }

	}

	public void init() {
	}
    }

    public class Obst extends Layer {
	final int version;
	final String id;
	final List<Coord2d[]> polygons;

	public Obst(byte[] buf) {
	    super();
	    MessageBuf msg = new MessageBuf(buf);
	    this.version = msg.int8();
	    if (this.version >= 2) {
		this.id = msg.string();
	    } else {
		this.id = "";
	    }

	    int polygonCount = msg.int8();
	    this.polygons = new LinkedList();
	    int[] polygonSizes = new int[polygonCount];

	    int i;
	    for(i = 0; i < polygonCount; ++i) {
		polygonSizes[i] = msg.int8();
	    }

	    for(i = 0; i < polygonCount; ++i) {
		int points = polygonSizes[i];
		Coord2d[] polygon = new Coord2d[points];

		for(int j = 0; j < points; ++j) {
		    polygon[j] = new Coord2d((double)msg.float16(), (double)msg.float16());
		}

		this.polygons.add(polygon);
	    }

	    System.out.println("LL");
	}

	public Obst(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.version = 1;
	    this.id = "";
	    this.polygons = new LinkedList();
	    br.close();
	}

	public int size() {
	    return 0;
	}

	public int type() {
	    return Resource.OBST;
	}

	public byte[] type_buffer() {
	    return new byte[]{111, 98, 115, 116, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/obst/obst_" + i + ".data");
	    (new File(res + "/obst/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	}

	public void init() {
	}
    }

    public class Neg extends Layer {
	public Coord cc;
	public Coord bc;
	public Coord bs;
	public Coord sz;
	public Coord[][] ep;
	public int en;
	public ArrayList<Integer> cns = new ArrayList();
	public ArrayList<Integer> epds = new ArrayList();

	public Neg(byte[] buf) {
	    super();
	    this.cc = Resource.cdec(buf, 0);
	    this.bc = Resource.cdec(buf, 4);
	    this.bs = Resource.cdec(buf, 8);
	    this.sz = Resource.cdec(buf, 12);
	    this.ep = new Coord[8][0];
	    this.en = buf[16];
	    int off = 17;

	    for(int i = 0; i < this.en; ++i) {
		int epid = buf[off];
		int cn = Utils.uint16d(buf, off + 1);
		this.epds.add(Integer.valueOf(epid));
		this.cns.add(cn);
		off += 3;
		this.ep[epid] = new Coord[cn];

		for(int o = 0; o < cn; ++o) {
		    this.ep[epid][o] = Resource.cdec(buf, off);
		    off += 4;
		}
	    }

	}

	public Neg(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.cc = new Coord(Utils.rnint(br), Utils.rnint(br));
	    this.bc = new Coord(Utils.rnint(br), Utils.rnint(br));
	    this.bs = new Coord(Utils.rnint(br), Utils.rnint(br));
	    this.sz = new Coord(Utils.rnint(br), Utils.rnint(br));
	    this.ep = new Coord[8][0];
	    this.en = Utils.rnint(br);

	    for(int i = 0; i < this.en; ++i) {
		int epid = Utils.rnint(br);
		int cn = Utils.rnint(br);
		this.epds.add(epid);
		this.cns.add(cn);
		this.ep[epid] = new Coord[cn];

		for(int o = 0; o < cn; ++o) {
		    this.ep[epid][o] = new Coord(Utils.rnint(br), Utils.rnint(br));
		}
	    }

	    br.close();
	}

	public int size() {
	    int s = 17;

	    for(int i = 0; i < this.cns.size(); ++i) {
		s += 3;

		for(int o = 0; o < (Integer)this.cns.get(i); ++o) {
		    s += 4;
		}
	    }

	    return s;
	}

	public int type() {
	    return Resource.NEG;
	}

	public byte[] type_buffer() {
	    return new byte[]{110, 101, 103, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/neg/neg_" + i + ".data");
	    (new File(res + "/neg/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#NEG LAYER FOR RES: " + res + "\r\n");
	    bw.write("#Coord cc\r\n");
	    bw.write(Integer.toString(this.cc.x) + "\r\n");
	    bw.write(Integer.toString(this.cc.y) + "\r\n");
	    bw.write("#Coord bc\r\n");
	    bw.write(Integer.toString(this.bc.x) + "\r\n");
	    bw.write(Integer.toString(this.bc.y) + "\r\n");
	    bw.write("#Coord bs\r\n");
	    bw.write(Integer.toString(this.bs.x) + "\r\n");
	    bw.write(Integer.toString(this.bs.y) + "\r\n");
	    bw.write("#Coord sz\r\n");
	    bw.write(Integer.toString(this.sz.x) + "\r\n");
	    bw.write(Integer.toString(this.sz.y) + "\r\n");
	    bw.write("#Byte en\r\n");
	    bw.write(Integer.toString(this.en) + "\r\n");

	    for(int j = 0; j < this.cns.size(); ++j) {
		bw.write("#Byte epid\r\n");
		bw.write(Integer.toString((Integer)this.epds.get(j)) + "\r\n");
		bw.write("#uint16 cn\r\n");
		bw.write(Integer.toString((Integer)this.cns.get(j)) + "\r\n");

		for(int o = 0; o < (Integer)this.cns.get(j); ++o) {
		    bw.write("#Coord ep[" + this.epds.get(j) + "][" + o + "]" + "\r\n");
		    bw.write(Integer.toString(this.ep[(Integer)this.epds.get(j)][o].x) + "\r\n");
		    bw.write(Integer.toString(this.ep[(Integer)this.epds.get(j)][o].y) + "\r\n");
		}
	    }

	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_int16d(this.cc.x));
	    out.write(Utils.byte_int16d(this.cc.y));
	    out.write(Utils.byte_int16d(this.bc.x));
	    out.write(Utils.byte_int16d(this.bc.y));
	    out.write(Utils.byte_int16d(this.bs.x));
	    out.write(Utils.byte_int16d(this.bs.y));
	    out.write(Utils.byte_int16d(this.sz.x));
	    out.write(Utils.byte_int16d(this.sz.y));
	    out.write(new byte[]{(byte)(this.en & 255)});

	    for(int j = 0; j < this.cns.size(); ++j) {
		out.write(new byte[]{(byte)((Integer)this.epds.get(j) & 255)});
		out.write(Utils.byte_int16d((Integer)this.cns.get(j)));

		for(int o = 0; o < (Integer)this.cns.get(j); ++o) {
		    out.write(Utils.byte_int16d(this.ep[(Integer)this.epds.get(j)][o].x));
		    out.write(Utils.byte_int16d(this.ep[(Integer)this.epds.get(j)][o].y));
		}
	    }

	}

	public void init() {
	}
    }

    public class Tile extends Layer {
	transient BufferedImage img;
	byte[] raw;
	public int id;
	int w;
	char t;

	public Tile(byte[] buf) {
	    super();
	    this.t = (char)Utils.ub(buf[0]);
	    this.id = Utils.ub(buf[1]);
	    this.w = Utils.uint16d(buf, 2);

	    try {
		this.img = ImageIO.read(new ByteArrayInputStream(buf, 4, buf.length - 4));
	    } catch (IOException var4) {
		IOException e = var4;
		throw new LoadException(e, Resource.this);
	    }

	    if (this.img == null) {
		throw new LoadException("Invalid image data in " + Resource.this.name, Resource.this);
	    }
	}

	public Tile(File data, File png) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.t = (char)Utils.rnint(br);
	    this.id = Utils.rnint(br);
	    this.w = Utils.rnint(br);
	    this.img = ImageIO.read(png);
	    br.close();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(this.img, "png", baos);
	    baos.flush();
	    this.raw = baos.toByteArray();
	    baos.close();
	}

	public int size() {
	    int s = 4;
	    s += this.raw.length;
	    return s;
	}

	public int type() {
	    return Resource.TILE;
	}

	public byte[] type_buffer() {
	    return new byte[]{116, 105, 108, 101, 0};
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/tile/tile_" + i + ".data");
	    (new File(res + "/tile/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TILE LAYER FOR RES " + res + "\r\n");
	    bw.write("#Byte t\r\n");
	    bw.write(Integer.toString(this.t) + "\r\n");
	    bw.write("#Byte id\r\n");
	    bw.write(Integer.toString(this.id) + "\r\n");
	    bw.write("#uint16 w\r\n");
	    bw.write(Integer.toString(this.w) + "\r\n");
	    bw.flush();
	    bw.close();
	    ImageIO.write(this.img, "png", new File(res + "/tile/tile_" + i + ".png"));
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(new byte[]{(byte)(this.t & 255)});
	    out.write(new byte[]{(byte)(this.id & 255)});
	    out.write(Utils.byte_int16d(this.w));
	    out.write(this.raw);
	}

	public void init() {
	}
    }

    public class Tooltip extends Layer {
	public final String t;
	private int size = 0;

	public Tooltip(byte[] buf) {
	    super();

	    try {
		this.t = new String(buf, "UTF-8");
	    } catch (UnsupportedEncodingException var4) {
		UnsupportedEncodingException e = var4;
		throw new LoadException(e, Resource.this);
	    }
	}

	public Tooltip(File data) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.t = Utils.rstr(br);
	    this.size = Utils.byte_str(this.t).length;
	    br.close();
	}

	public int size() {
	    return this.size;
	}

	public int type() {
	    return Resource.TOOLTIP;
	}

	public byte[] type_buffer() {
	    return new byte[]{116, 111, 111, 108, 116, 105, 112, 0};
	}

	public void init() {
	}

	public void decode(String res, int i) throws Exception {
	    File f = new File(res + "/tooltip/tooltip_" + i + ".data");
	    (new File(res + "/tooltip/")).mkdirs();
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#TOOLTIP LAYER FOR RES " + res + "\r\n");
	    bw.write("#String tooltip\r\n");
	    bw.write(this.t.replace("\n", "\\n") + "\r\n");
	    bw.flush();
	    bw.close();
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_str(this.t));
	}
    }

    public class Image extends Layer {
	public transient BufferedImage img;
	public byte[] raw;
	public final int z;
	public final int subz;
	public final boolean nooff;
	public final boolean custom;
	public final int id;
	private float scale = 1.0F;
	public Coord sz;
	public Coord o;
	public Coord tsz;

	public Image(byte[] bytes) {
	    super();
	    MessageBuf buf = new MessageBuf(bytes);
	    this.z = buf.int16();
	    this.subz = buf.int16();
	    int fl = buf.uint8();
	    this.nooff = (fl & 2) != 0;
	    this.id = buf.int16();
	    this.o = Resource.cdec(buf);
	    this.custom = (fl & 4) != 0;
	    if (this.custom) {
		while(true) {
		    String key = buf.string();
		    if (key.equals("")) {
			break;
		    }

		    int len = buf.uint8();
		    if ((len & 128) != 0) {
			len = buf.int32();
		    }

		    Message val = new MessageBuf(buf.bytes(len));
		    if (key.equals("tsz")) {
			this.tsz = ((Message)val).coord();
		    } else if (key.equals("scale")) {
			this.scale = ((Message)val).float32();
		    }
		}
	    }

	    try {
		this.img = Resource.readimage(new MessageInputStream(buf));
	    } catch (IOException var8) {
		IOException e = var8;
		throw new LoadException(e, Resource.this);
	    }

	    this.sz = Utils.imgsz(this.img);
	    if (this.tsz == null) {
		this.tsz = this.sz;
	    }

	    if (this.img == null) {
		throw new LoadException("Invalid image data in " + Resource.this.name, Resource.this);
	    }
	}

	public Image(File data, File png) throws Exception {
	    super();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "UTF-8"));
	    this.z = Utils.rnint(br);
	    this.subz = Utils.rnint(br);
	    int fl = Utils.rnint(br);
	    this.nooff = (fl & 2) != 0;
	    this.id = Utils.rnint(br);
	    this.o = new Coord(Utils.rnint(br), Utils.rnint(br));
	    this.tsz = this.sz;
	    this.scale = 1.0F;
	    boolean tmp = false;

	    while(true) {
		String k = Utils.rnstr(br);
		if (k == null) {
		    this.custom = tmp;
		    this.img = ImageIO.read(png);
		    br.close();
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(this.img, "png", baos);
		    baos.flush();
		    this.raw = baos.toByteArray();
		    baos.close();
		    return;
		}

		if ("tsz".equals(k)) {
		    this.tsz = new Coord(Utils.rnint(br), Utils.rnint(br));
		} else if ("scale".equals(k)) {
		    this.scale = Utils.rfloat(br);
		}

		tmp = true;
	    }
	}

	public int size() {
	    int s = 11;
	    if (this.custom) {
		if (this.scale != 1.0F) {
		    s += 6;
		    ++s;
		    s += 4;
		}

		if (this.tsz != this.sz) {
		    s += 4;
		    ++s;
		    s += 8;
		}

		++s;
	    }

	    s += this.raw.length;
	    return s;
	}

	public int type() {
	    return Resource.IMAGE;
	}

	public byte[] type_buffer() {
	    return new byte[]{105, 109, 97, 103, 101, 0};
	}

	public void init() {
	}

	public void decode(String res, int i) throws Exception {
	    (new File(res + "/image/")).mkdirs();
	    File f = new File(res + "/image/image_" + i + ".data");
	    f.createNewFile();
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
	    bw.write("#IMAGE LAYER FOR RES " + res + "\r\n");
	    bw.write("#int16 z\r\n");
	    bw.write(Integer.toString(this.z) + "\r\n");
	    bw.write("#int16 subz\r\n");
	    bw.write(Integer.toString(this.subz) + "\r\n");
	    bw.write("#Byte nooff\r\n");
	    bw.write(Integer.toString(this.nooff ? 1 : 0) + "\r\n");
	    bw.write("#int16 id\r\n");
	    bw.write(Integer.toString(this.id) + "\r\n");
	    bw.write("#Coord o\r\n");
	    bw.write(Integer.toString(this.o.x) + "\r\n");
	    bw.write(Integer.toString(this.o.y) + "\r\n");
	    if (this.tsz != this.sz) {
		bw.write("tsz\r\n");
		bw.write(Integer.toString(this.tsz.x) + "\r\n");
		bw.write(Integer.toString(this.tsz.y) + "\r\n");
	    }

	    if (this.scale != 1.0F) {
		bw.write("scale\r\n");
		bw.write(Float.toString(this.scale) + "\r\n");
	    }

	    bw.flush();
	    bw.close();
	    ImageIO.write(this.img, "png", new File(res + "/image/image_" + i + ".png"));
	}

	public void encode(OutputStream out) throws Exception {
	    out.write(Utils.byte_int16d(this.z));
	    out.write(Utils.byte_int16d(this.subz));
	    out.write(new byte[]{(byte)((this.nooff ? 2 : 0) | (this.custom ? 4 : 0))});
	    out.write(Utils.byte_int16d(this.id));
	    out.write(Utils.byte_int16d(this.o.x));
	    out.write(Utils.byte_int16d(this.o.y));
	    if (this.scale != 1.0F) {
		out.write(Utils.byte_strd("scale"));
		out.write(4);
		out.write(Utils.byte_float32d(this.scale));
	    }

	    if (this.tsz != this.sz) {
		out.write(Utils.byte_strd("tsz"));
		out.write(32);
		out.write(Utils.byte_int16d(this.tsz.x));
		out.write(Utils.byte_int16d(this.tsz.y));
	    }

	    if (this.custom) {
		out.write(Utils.byte_strd(""));
	    }

	    out.write(this.raw);
	}
    }

    public static class ImageReadException extends IOException {
	public final String[] supported = ImageIO.getReaderMIMETypes();

	public ImageReadException() {
	    super("Could not decode image data");
	}
    }

    public abstract class Layer implements Serializable {
	public Layer() {
	}

	public abstract void init();

	public abstract int size();

	public abstract int type();

	public abstract byte[] type_buffer();

	public abstract void decode(String var1, int var2) throws Exception;

	public abstract void encode(OutputStream var1) throws Exception;
    }

    public static class LoadException extends RuntimeException {
	public Resource res;

	public LoadException(String msg, Resource res) {
	    super(msg);
	    this.res = res;
	}

	public LoadException(String msg, Throwable cause, Resource res) {
	    super(msg, cause);
	    this.res = res;
	}

	public LoadException(Throwable cause, Resource res) {
	    super("Load error in resource " + res.toString() + "\n" + cause + "\n");
	    this.res = res;
	}
    }
}
