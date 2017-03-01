package powerworks.block;

import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.main.Game;

public class Block implements TexturedObject, Collidable {

    int rotation;
    int xPixel, yPixel;
    BlockType type;
    boolean requiresUpdate = true;
    
    public Block(BlockType type, int xTile, int yTile) {
	this.xPixel = xTile << 4;
	this.yPixel = yTile << 4;
	this.type = type;
	requiresUpdate = type.defaultRequiresUpdate;
	if (type.hitbox.solid)
	    Collidable.collidables.add(this);
    }

    public void render() {
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes())
	    Screen.screen.renderHitbox(this);
    }

    public int getXTile() {
	return xPixel >> 4;
    }

    public int getYTile() {
	return yPixel >> 4;
    }

    public void update() {
    }

    public boolean isPlaceable() {
	return type.placeable;
    }
    
    public boolean isSolid() {
	return type.hitbox.solid;
    }

    @Override
    public Texture getTexture() {
	return type.texture;
    }

    public Texture getPlaceableTexture() {
	return type.placeableTexture;
    }

    public Texture getNotPlaceableTexture() {
	return type.notPlaceableTexture;
    }

    @Override
    public String toString() {
	return type.name;
    }

    @Override
    public Hitbox getHitbox() {
	return type.hitbox;
    }

    @Override
    public int getXPixel() {
	return xPixel;
    }

    @Override
    public int getYPixel() {
	return yPixel;
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }

    public boolean hasTransparency() {
	return type.texture.hasTransparency();
    }

    public boolean requiresUpdate() {
	return requiresUpdate;
    }

    @Override
    public double getScale() {
	return 1;
    }
}
