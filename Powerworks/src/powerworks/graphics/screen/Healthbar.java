package powerworks.graphics.screen;

import powerworks.graphics.Texture;

public class Healthbar extends ScreenObject {

    protected Healthbar(int xPixel, int yPixel) {
	super(xPixel, yPixel, 1);
	open = true;
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
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
	// TODO Auto-generated method stub
    }
}
