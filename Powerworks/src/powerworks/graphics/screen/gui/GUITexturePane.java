package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUITexturePane extends GUIElement {

    protected Texture texture;

    /**
     * Width and height pixels are for stretching the texture to fit
     */
    public GUITexturePane(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.texture = texture;
    }

    public GUITexturePane(ScreenObject parent, int xPixel, int yPixel, int layer, Texture texture) {
	this(parent, xPixel, yPixel, texture.getWidthPixels(), texture.getHeightPixels(), layer, texture);
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderTexture(true, texture, xPixel, yPixel, widthPixels, heightPixels);
	super.render();
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
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public String toString() {
	return "GUI texture pane " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size()
		+ ", texture of " + texture.toString();
    }

    @Override
    public void remove() {
	super.remove();
	texture = null;
    }

}
