package powerworks.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import powerworks.main.Game;
import powerworks.main.State;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {

    static boolean[] keysDown = new boolean[156];
    private static boolean mouseOutside = false;
    static int modifier, mouseX, mouseY, mouseXPixel, mouseYPixel, mouseLevelYPixel, mouseLevelXPixel;
    static int mouseButton = -1;
    static boolean mouseMoved = false, mouseMovedRelativeToLevel = false;
    static MouseEvent mouseClick, mouseRelease;
    static MouseWheelEvent mouseWheel;
    static KeyControlOption keyBinding = null;
    static MouseControlOption mouseBinding = null;
    static LinkedList<ControlPress> queue = new LinkedList<ControlPress>();
    static HashMap<ControlMap, HashMap<KeyControlHandler, KeyControlOption[]>> keyHandlers = new HashMap<ControlMap, HashMap<KeyControlHandler, KeyControlOption[]>>();
    static HashMap<ControlMap, HashMap<MouseControlHandler, MouseControlOption[]>> mouseHandlers = new HashMap<ControlMap, HashMap<MouseControlHandler, MouseControlOption[]>>();
    static HashMap<ControlMap, HashMap<MouseWheelControlHandler, MouseWheelControlOption[]>> mouseWheelHandlers = new HashMap<ControlMap, HashMap<MouseWheelControlHandler, MouseWheelControlOption[]>>();
    static TextListener textListener = null;
    static ControlMap map = ControlMap.MAIN_MENU;

    public static void registerKeyControlHandler(KeyControlHandler h, ControlMap map, KeyControlOption... wantedControls) {
	keyHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedKeyControls(KeyControlHandler h, ControlMap map, KeyControlOption... wantedControls) {
	keyHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeKeyControlHandler(KeyControlHandler h, ControlMap map) {
	keyHandlers.get(map).remove(h);
    }

    public static void registerMouseControlHandler(MouseControlHandler h, ControlMap map, MouseControlOption... wantedControls) {
	mouseHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedMouseControls(MouseControlHandler h, ControlMap map, MouseControlOption... wantedControls) {
	mouseHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeMouseControlHandler(MouseControlHandler h, ControlMap map) {
	mouseHandlers.get(map).remove(h);
    }

    public static void registerMouseWheelControlHandler(MouseWheelControlHandler h, ControlMap map, MouseWheelControlOption... wantedControls) {
	mouseWheelHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedMouseWheelControls(MouseWheelControlHandler h, ControlMap map, MouseWheelControlOption... wantedControls) {
	mouseWheelHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeMouseWheelControlHandler(MouseWheelControlHandler h, ControlMap map) {
	mouseWheelHandlers.get(map).remove(h);
    }

    /**
     * Registers it for every mapping in the ControlMap
     */
    public static void registerMouseWheelControlHandler(MouseWheelControlHandler h, ControlMap map) {
	mouseWheelHandlers.get(map).put(h, (MouseWheelControlOption[]) map.mouseWheelBinds.values().toArray());
    }

    /**
     * Registers it for every mapping in the ControlMap
     */
    public static void registerMouseControlHandler(MouseControlHandler h, ControlMap map) {
	mouseHandlers.get(map).put(h, (MouseControlOption[]) map.mouseBinds.values().toArray());
    }

    /**
     * Registers it for every mapping in the ControlMap
     */
    public static void registerKeyControlHandler(KeyControlHandler h, ControlMap map) {
	keyHandlers.get(map).put(h, (KeyControlOption[]) map.keyBinds.values().toArray());
    }

    public static void funnelKeys(TextListener t) {
	textListener = t;
    }

    public static void stopFunneling() {
	textListener = null;
	for (int i = 0; i < keysDown.length; i++)
	    keysDown[i] = false;
    }

    public static ControlMap getMapping() {
	return map;
    }

    public static void setMapping(ControlMap map) {
	InputManager.map = map;
    }

    /*
     * What's happening: Mouse release: mouseRelease gets set to MouseEvent sent
     * thru function to switch threads over then, in update, if mouseRelease is
     * not null, it updates the current modifiers it then checks to make sure
     * that the mouse was being held down so as to prevent multiple releases
     * from going off without multiple clicks
     * 
     */
    public static void update() {
	if (queue.size() != 0)
	    try {
		for (Iterator<ControlPress> it = queue.iterator(); it.hasNext();) {
		    ControlPress press = it.next();
		    if (press instanceof KeyPress) {
			KeyPress keyPress = (KeyPress) press;
			for (Entry<KeyControlHandler, KeyControlOption[]> e : keyHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), keyPress.getOption())) {
				e.getKey().handleKeyControlPress(keyPress);
			    }
			}
		    } else if (press instanceof MousePress) {
			MousePress mousePress = (MousePress) press;
			for (Entry<MouseControlHandler, MouseControlOption[]> e : mouseHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), mousePress.getOption())) {
				e.getKey().handleMouseControlPress(mousePress);
			    }
			}
		    } else {
			MouseWheelPress mouseWheelPress = (MouseWheelPress) press;
			for (Entry<MouseWheelControlHandler, MouseWheelControlOption[]> e : mouseWheelHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), mouseWheelPress.getOption())) {
				e.getKey().handleMouseWheelPress(mouseWheelPress);
			    }
			}
		    }
		}
	    } catch (ConcurrentModificationException | NullPointerException e) {
		e.printStackTrace();
	    }
	queue.clear();
	if (mouseButton != -1 && Game.getScreenManager().getClickableScreenObjectsAt(mouseXPixel, mouseYPixel).size() == 0) {
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    if (option != null)
		queue.add(new MousePress(ControlPressType.REPEAT, option));
	}
	if (textListener == null) {
	    for (int i = 0; i < keysDown.length; i++)
		if (keysDown[i]) {
		    KeyControlOption option = map.getKeyControl(i);
		    if (option != null)
			queue.add(new KeyPress(ControlPressType.REPEAT, option));
		}
	}
	if (mouseClick != null && !mouseOutside) {
	    modifier = mouseClick.getModifiers();
	    if (mouseBinding != null) {
		map.setMouseBind(mouseClick.getButton(), modifier, mouseBinding);
		Game.getLogger().log("Bound mouse button " + modifier + ":" + mouseClick.getButton() + " to " + mouseBinding);
		Game.getChatManager().sendMessage("Bound mouse button " + modifier + ":" + mouseClick.getButton() + " to " + mouseBinding);
		mouseBinding = null;
	    } else if (mouseButton == -1) {
		mouseButton = mouseClick.getButton();
		if (!Game.getScreenManager().onMouseAction(new powerworks.io.MouseEvent(mouseXPixel, mouseYPixel, mouseButton, ControlPressType.PRESSED))
			&& (State.getState() == State.INGAME && !Game.getLevelManager().onMouseAction(new powerworks.io.MouseEvent(mouseXPixel, mouseYPixel, mouseButton, ControlPressType.PRESSED)))) {
		    MouseControlOption option = map.getMouseControl(mouseButton);
		    MousePress press = new MousePress(ControlPressType.PRESSED, option);
		    if (option != null && !queue.contains(press))
			queue.add(press);
		}
	    }
	    mouseClick = null;
	}
	if (mouseRelease != null) {
	    modifier = mouseRelease.getModifiers();
	    if (mouseButton != -1) {
		Game.getScreenManager().onMouseAction(new powerworks.io.MouseEvent(mouseXPixel, mouseYPixel, mouseButton, ControlPressType.RELEASED));
		if (State.getState() == State.INGAME)
		    Game.getLevelManager().onMouseAction(new powerworks.io.MouseEvent(mouseXPixel, mouseYPixel, mouseButton, ControlPressType.RELEASED));
		MouseControlOption option = map.getMouseControl(mouseButton);
		mouseButton = -1;
		MousePress press = new MousePress(ControlPressType.RELEASED, option);
		if (option != null && !queue.contains(press))
		    queue.add(press);
	    }
	    mouseRelease = null;
	}
	if (mouseWheel != null) {
	    modifier = mouseWheel.getModifiers();
	    int rotation = mouseWheel.getWheelRotation();
	    if (rotation == 1 || rotation == -1) {
		if (!Game.getScreenManager().onMouseScroll(rotation * 5)) {
		    MouseWheelControlOption option = map.getMouseWheelControl(rotation);
		    MouseWheelPress press = new MouseWheelPress(ControlPressType.PRESSED, option);
		    if (option != null && !queue.contains(press))
			queue.add(press);
		}
	    }
	    mouseWheel = null;
	}
    }

    public static void screenMoved() {
	mouseLevelXPixel = (int) ((mouseXPixel / Game.getRenderEngine().getZoom()) + Game.getRenderEngine().getXPixelOffset());
	mouseLevelYPixel = (int) (mouseYPixel / Game.getRenderEngine().getZoom() + Game.getRenderEngine().getYPixelOffset());
    }

    static boolean containsControlOption(ControlOption[] options, ControlOption option) {
	for (int i = 0; i < options.length; i++)
	    if (options[i].equals(option))
		return true;
	return false;
    }

    public static int getMouseButton() {
	return mouseButton;
    }

    public static int getModifier() {
	return modifier;
    }

    public static int getMouseXPixel() {
	return mouseXPixel;
    }

    public static int getMouseYPixel() {
	return mouseYPixel;
    }

    public static int getMouseLevelXPixel() {
	return mouseLevelXPixel;
    }

    public static int getMouseLevelYPixel() {
	return mouseLevelYPixel;
    }

    public static void enterMouseBindMode(MouseControlOption option) {
	mouseBinding = option;
    }

    public static void enterKeyBindMode(KeyControlOption option) {
	keyBinding = option;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	if (mouseOutside)
	    return;
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.getScreenScale();
	mouseXPixel = mouseX / Game.getScreenScale();
	mouseLevelXPixel = (int) ((mouseXPixel / Game.getRenderEngine().getZoom()) + Game.getRenderEngine().getXPixelOffset());
	mouseLevelYPixel = (int) (mouseYPixel / Game.getRenderEngine().getZoom() + Game.getRenderEngine().getYPixelOffset());
	mouseMoved = true;
	mouseMovedRelativeToLevel = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	if (mouseOutside)
	    return;
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.getScreenScale();
	mouseXPixel = mouseX / Game.getScreenScale();
	mouseLevelXPixel = (int) ((mouseXPixel / Game.getRenderEngine().getZoom()) + Game.getRenderEngine().getXPixelOffset());
	mouseLevelYPixel = (int) (mouseYPixel / Game.getRenderEngine().getZoom() + Game.getRenderEngine().getYPixelOffset());
	mouseMoved = true;
	mouseMovedRelativeToLevel = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	Game.getInstance().clearMouseIcon();
	mouseOutside = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
	Game.getInstance().resetMouseIcon();
	mouseOutside = true;
	mouseRelease = e;
    }

    @Override
    public void mousePressed(MouseEvent e) {
	mouseClick = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	mouseRelease = e;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	mouseWheel = e;
    }

    @Override
    public void keyPressed(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (keyBinding != null) {
	    map.setKeyBind(code, modifier, keyBinding);
	    Game.getLogger().log("Bound key " + modifier + ":" + e.getKeyCode() + " to " + keyBinding);
	    Game.getChatManager().sendMessage("Bound key " + modifier + ":" + code + " to " + keyBinding);
	    keyBinding = null;
	} else if (textListener != null) {
	    textListener.handleChar(e.getKeyChar());
	} else if (code < keysDown.length && !keysDown[code]) {
	    keysDown[code] = true;
	    KeyControlOption option = map.getKeyControl(code);
	    KeyPress press = new KeyPress(ControlPressType.PRESSED, option);
	    if (option != null && !queue.contains(press))
		queue.add(press);
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (textListener != null)
	    return;
	if (code < keysDown.length && keysDown[code]) {
	    keysDown[code] = false;
	    KeyControlOption option = map.getKeyControl(code);
	    KeyPress press = new KeyPress(ControlPressType.RELEASED, option);
	    if (option != null && !queue.contains(press))
		queue.add(press);
	}
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}