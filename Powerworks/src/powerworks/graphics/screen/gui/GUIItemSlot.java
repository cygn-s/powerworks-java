package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.Mouse;
import powerworks.graphics.screen.ScreenObject;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.io.ControlPressType;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIItemSlot extends GUIElement {

    protected Inventory inv;
    protected int index;
    protected boolean isDisplay;
    protected Texture unhigh, high, click, display, current;

    public GUIItemSlot(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Inventory inv, int index, boolean isDisplay, Texture unhigh, Texture high,
	    Texture click, Texture display) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.inv = inv;
	this.index = index;
	this.isDisplay = isDisplay;
	this.unhigh = unhigh;
	this.high = high;
	this.click = click;
	this.display = display;
	current = unhigh;
    }

    public GUIItemSlot(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Inventory inv, int index, boolean isDisplay) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, inv, index, isDisplay, Image.ITEM_SLOT, Image.ITEM_SLOT_HIGHLIGHT, Image.ITEM_SLOT_CLICK, Image.ITEM_SLOT_DISPLAY);
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderTexture(current, xPixel, yPixel);
	Item i = inv.getItem(index);
	if (i != null) {
	    Game.getRenderEngine().renderTexture(i.getTexture(), xPixel, yPixel);
	    Game.getRenderEngine().renderText(i.getQuantity(), xPixel + 1, yPixel + 4);
	}
    }

    @Override
    public void update() {
    }

    @Override
    protected void onOpen() {
    }

    @Override
    protected void onClose() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
	if (isDisplay)
	    return;
	ControlPressType type = mouse.getType();
	switch (type) {
	    case PRESSED:
		current = click;
		Mouse m = Game.getMouse();
		Item i = inv.getItem(index);
		Item mI = m.getHeldItem();
		if (mouse.getButton() == 1) {
		    if (mI != null) {
			if (i != null) {
			    if (mI.getType() != i.getType()) {
				m.setHeldItem(i);
				inv.giveItem(mI);
			    } else {
				if (i.getQuantity() + mI.getQuantity() >= i.getMaxStack()) {
				    int q = i.getQuantity();
				    i.setQuantity(i.getMaxStack());
				    mI.setQuantity(mI.getQuantity() - (i.getMaxStack() - q));
				} else {
				    i.setQuantity(i.getQuantity() + mI.getQuantity());
				    m.setHeldItem(null);
				}
			    }
			} else {
			    inv.giveItem(mI);
			    m.setHeldItem(null);
			}
		    } else {
			if (i != null) {
			    inv.setItem(null, index);
			    m.setHeldItem(i);
			}
		    }
		}
		break;
	    case RELEASED:
		current = high;
		break;
	    default:
		break;
	}
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void onMouseEnter() {
	if (!isDisplay)
	    current = high;
    }

    @Override
    public void onMouseLeave() {
	if (!isDisplay)
	    current = unhigh;
    }

    @Override
    public String toString() {
	return "GUI item slot " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", item: " + inv.getItem(index).toString()
		+ ", is display: " + isDisplay;
    }

    @Override
    public void remove() {
	super.remove();
	inv = null;
	unhigh = high = click = display = current = null;
    }

}
