package powerworks.block;

import java.lang.reflect.InvocationTargetException;
import powerworks.collidable.Hitbox;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.graphics.Texture;

public enum BlockType {
    
    ERROR(Hitbox.TILE, StaticTexture.ERROR, StaticTexture.ERROR, StaticTexture.ERROR, 1, 1, "Error", 0, false, ErrorBlock.class),
    
    CONVEYOR_BELT_CONNECTED_UP(Hitbox.TILE, SynchronizedAnimatedTexture.CONVEYOR_BELT_CONNECTED_UP, StaticTexture.CONVEYOR_BELT_PLACEABLE, StaticTexture.CONVEYOR_BELT_NOT_PLACEABLE, 1, 1, "Conveyor Belt", 1, true, ConveyorBeltBlock.class),
    
    ORE_MINER(Hitbox.TILE, StaticTexture.ERROR, StaticTexture.ERROR, StaticTexture.ERROR, 1, 1, "Ore Miner", 2, true, OreMinerBlock.class);
    
    Hitbox hitbox;
    Texture texture;
    StaticTexture notPlaceableTexture;
    StaticTexture placeableTexture;
    int width, height;
    String name;
    boolean placeable;
    boolean defaultRequiresUpdate = true;
    int id;
    Class<? extends Block> instantiator;

    /**
     * Creates a BlockType constant
     * 
     * @param hitbox
     *            the hitbox
     * @param textures
     *            the textures
     * @param width
     *            the width, in tiles
     * @param height
     *            the height, in tiles
     * @param id
     *            the id
     */
    private BlockType(Hitbox hitbox, Texture texture, StaticTexture placeableTexture, StaticTexture notPlaceableTexture, int width, int height, String name, int id, boolean placeable, Class<? extends Block> instantiator) {
	this.hitbox = hitbox;
	this.texture = texture;
	this.width = width;
	this.height = height;
	this.name = name;
	this.placeableTexture = placeableTexture;
	this.notPlaceableTexture = notPlaceableTexture;
	this.placeable = placeable;
	this.id = id;
	this.instantiator = instantiator;
    }
    
    public boolean defaultRequiresUpdate() {
	return defaultRequiresUpdate;
    }
    
    public Texture getTexture() {
	return texture;
    }
    
    public int getWidthTiles() {
	return width;
    }
    
    public int getHeightTiles() {
	return height;
    }
    
    public String getName() {
	return name;
    }
    
    public boolean isPlaceable() {
	return placeable;
    }
    
    public int getID() {
	return id;
    }
    
    public Hitbox getHitbox() {
	return hitbox;
    }
    
    public Texture getPlaceableTexture() {
	return placeableTexture;
    }
    
    public Texture getNotPlaceableTexture() {
	return notPlaceableTexture;
    }
    
    public boolean isSolid() {
	return hitbox.solid;
    }
    
    /**
     * Creates an instance of a Block based on the BlockType using the correct
     * class - note: this does not add it to the level
     * 
     * @param xTile
     *            the x tile to create the block at
     * @param yTile
     *            the y tile to create the block at
     * @return the Block object (ErrorBlock if class to use is not defined
     */
    public Block createInstance(int xTile, int yTile) {
	try {
	    Block b = null;
	    try {
		b = instantiator.getConstructor(getClass(), int.class, int.class).newInstance(this, xTile, yTile);
	    } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		e.printStackTrace();
	    }
	    return b;
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
