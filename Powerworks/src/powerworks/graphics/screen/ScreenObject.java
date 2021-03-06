package powerworks.graphics.screen;

import java.util.ArrayList;
import java.util.List;
import powerworks.graphics.Texture;
import powerworks.main.Game;

public abstract class ScreenObject {

    static class NoParent extends ScreenObject {

	protected NoParent() {
	    children = new ArrayList<ScreenObject>();
	    this.parent = null;
	    this.layer = 0;
	    this.xPixel = 0;
	    this.yPixel = 0;
	    this.relXPixel = 0;
	    this.relYPixel = 0;
	}

	@Override
	public void setRelXPixel(int xPixel) {
	    this.xPixel = xPixel;
	    children.forEach(ScreenObject::onParentMove);
	}

	@Override
	public void setRelYPixel(int yPixel) {
	    this.yPixel = yPixel;
	    children.forEach(ScreenObject::onParentMove);
	}

	@Override
	public float getScale() {
	    return 1;
	}

	@Override
	public float getWidthScale() {
	    return 1;
	}

	@Override
	public float getHeightScale() {
	    return 1;
	}

	@Override
	public int getRotation() {
	    return 0;
	}

	@Override
	public void update() {
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onClose() {
	}

	@Override
	public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
	}

	@Override
	public String toString() {
	    return "Default screen object parent";
	}
    }

    public static void print() {
	System.out.println(noParent.p(""));
    }

    private static ScreenObject noParent = new NoParent();
    private static int nextId = 0;

    static NoParent getNoParentObject() {
	return (NoParent) noParent;
    }

    /**
     * Used to determine whether or not to change position when the screen size
     * changes.
     */
    boolean adjustPosition;
    /**
     * Relative to screen, however, constructor values are relative to parent
     */
    protected int xPixel, yPixel;
    /**
     * Relative to parent
     */
    protected int relXPixel, relYPixel;
    protected int layer;
    protected int id;
    protected ScreenObject parent;
    protected List<ScreenObject> children;
    protected boolean open = false;

    /**
     * Instantiates a ScreenObject and automatically adds it to be rendered and
     * updated each tick
     * 
     * @param parent
     *            the parent to be relative to. Use null for no parent
     * @param xPixel
     *            the x pixel relative to the parent (the instance itself only
     *            stores relative to screen)
     * @param yPixel
     *            the y pixel relative to the parent (the instance itself only
     *            stores relative to screen)
     */
    protected ScreenObject(ScreenObject parent, int xPixel, int yPixel, int layer) {
	children = new ArrayList<ScreenObject>();
	this.parent = (parent == null) ? noParent : parent;
	this.relXPixel = xPixel;
	this.relYPixel = yPixel;
	this.xPixel = xPixel + this.parent.xPixel;
	this.yPixel = yPixel + this.parent.yPixel;
	this.layer = layer;
	this.parent.children.add(this);
	id = nextId++;
	Game.getScreenManager().getScreenObjects().add(this);
    }

    /**
     * Instantiates a ScreenObject and automatically adds it to be rendered and
     * updated each tick
     * 
     * @param xPixel
     *            the x pixel, relative to the screen
     * @param yPixel
     *            the y pixel, relative to the screen
     */
    protected ScreenObject(int xPixel, int yPixel, int layer) {
	this(noParent, xPixel, yPixel, layer);
    }

    private ScreenObject() {
    }

    public void setParent(ScreenObject parent) {
	this.parent.children.remove(this);
	this.parent = parent;
	parent.children.add(this);
	// System.out.println("moving " + getClass().getSimpleName() + " to " +
	// this.parent.getClass().getSimpleName());
	onParentMove();
	if (parent.open && !open)
	    open();
	else if (!parent.open && open)
	    close();
    }

    public ScreenObject getParent() {
	return parent;
    }

    public List<ScreenObject> getChildren() {
	return children;
    }

    /**
     * A unique number that identifies this object
     */
    public int getID() {
	return id;
    }

    public boolean isOpen() {
	return open;
    }

    public void open() {
	open = true;
	onOpen();
	children.forEach(ScreenObject::open);
    }

    public void close() {
	open = false;
	onClose();
	children.forEach(ScreenObject::close);
    }

    public void toggle() {
	if (open)
	    close();
	else
	    open();
    }

    public int getLayer() {
	return layer;
    }

    /**
     * Sets the layer for this. If doChildren is true it will change the layer
     * for children relative to this layer change
     **/
    public void setLayer(int layer, boolean doChildren) {
	this.layer = layer;
    }

    /**
     * @param xPixel
     *            relative to parent
     */
    public void setRelXPixel(int xPixel) {
	this.relXPixel = xPixel;
	this.xPixel = parent.getXPixel() + xPixel;
	children.forEach(ScreenObject::onParentMove);
    }

    /**
     * @param yPixel
     *            relative to parent
     */
    public void setRelYPixel(int yPixel) {
	this.relYPixel = yPixel;
	this.yPixel = parent.getYPixel() + yPixel;
	children.forEach(ScreenObject::onParentMove);
    }

    /**
     * 
     * @param xPixel
     *            relative to screen
     */
    public void setXPixel(int xPixel) {
	this.xPixel = xPixel;
	children.forEach(ScreenObject::onParentMove);
    }

    /**
     * 
     * @param yPixel
     *            relative to screen
     */
    public void setYPixel(int yPixel) {
	this.yPixel = yPixel;
	children.forEach(ScreenObject::onParentMove);
    }

    /**
     * Relative to screen
     */
    public int getXPixel() {
	return xPixel;
    }

    /**
     * Relative to screen
     */
    public int getYPixel() {
	return yPixel;
    }

    /**
     * Relative to parent
     */
    public int getRelXPixel() {
	return relXPixel;
    }

    /**
     * Relative to parent
     */
    public int getRelYPixel() {
	return relYPixel;
    }

    /**
     * Should be overridden if you want to modify and also should take into
     * account parent value By default is the parent's value
     */
    public float getScale() {
	return parent.getScale();
    }

    /**
     * Should be overridden if you want to modify and also should take into
     * account parent value By default is the parent's value
     */
    public float getWidthScale() {
	return parent.getWidthScale();
    }

    /**
     * Should be overridden if you want to modify and also should take into
     * account parent value By default is the parent's value
     */
    public float getHeightScale() {
	return parent.getHeightScale();
    }

    /**
     * Should be overridden if you want to modify (in degrees) and also should
     * take into account parent value (use GeometryHelper.addDegrees) By default
     * is the parent's value
     */
    public int getRotation() {
	return parent.getRotation();
    }

    /**
     * Rendering will not be called by any automatic means (i.e. <i> not </i>
     * thru Renderer) Automatically calls the render methods for all children of
     * this object in order of their layers
     */
    public void render() {
	children.stream().filter(ScreenObject::isOpen).sorted((ScreenObject obj, ScreenObject obj2) -> obj.getLayer() > obj2.getLayer() ? 1 : obj.getLayer() == obj2.getLayer() ? 0 : -1)
		.forEach(ScreenObject::render);
    }

    /**
     * Updating is automatically done by Renderer
     */
    public abstract void update();

    protected abstract void onOpen();

    protected abstract void onClose();

    protected void onParentMove() {
	this.xPixel = parent.getXPixel() + relXPixel;
	this.yPixel = parent.getYPixel() + relYPixel;
	children.forEach(ScreenObject::onParentMove);
    }

    private String p(String space) {
	String ret = space + "open: " + open + ", " + toString();
	if (children.size() != 0)
	    ret += ":";
	System.out.println(ret);
	space += "  ";
	for (ScreenObject e : children) {
	    ret += "\n" + space + e.p(space);
	}
	return ret;
    }

    /**
     * Closes and frees up memory of this and all children
     */
    public void remove() {
	close();
	Game.getScreenManager().getScreenObjects().remove(this);
	children.forEach(ScreenObject::remove);
	children = null;
	parent = null;
    }

    /**
     * Used for keeping objects that should remain at a certain position
     * relative to the screen where they should be, i.e. the hotbar
     */
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
	if (adjustPosition) {
	    int width = Game.getScreenWidth();
	    int height = Game.getScreenHeight();
	    this.xPixel = (int) (width * ((float) xPixel / oldWidthPixels));
	    this.yPixel = (int) (height * ((float) yPixel / oldHeightPixels));
	}
    }
}
