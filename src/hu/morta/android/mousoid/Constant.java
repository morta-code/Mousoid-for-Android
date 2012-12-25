package hu.morta.android.mousoid;

public interface Constant {
	/// Command header parts
    public static final byte HEADER = 66;

    public static final byte WHO_ARE_YOU = -128;
    public static final byte KEYCOMMAND = 1;
    public static final byte KEYPRESS = 2;
    public static final byte KEYRELEASE = 3;
    public static final byte MOUSEMOVE = 4;
    public static final byte MOUSEBUTTON = 5;
    public static final byte NAME = 8;


    /// Command value parts
    /// Keycodes
    public static final byte KEY_UP = 0;
    public static final byte KEY_DOWN = 1;
    public static final byte KEY_LEFT = 2;
    public static final byte KEY_RIGHT = 3;
    public static final byte KEY_PGUP = 4;
    public static final byte KEY_PGDN = 5;
    public static final byte KEY_SPACE = 6;
    public static final byte KEY_ESC = 7;
    public static final byte KEY_ALT = 8;
    public static final byte KEY_ALTGR = 9;
    public static final byte KEY_CTRL = 10;
    public static final byte KEY_SUPER = 11;
    public static final byte KEY_SHIFT = 12;
    public static final byte KEY_ENTER = 13;
    public static final byte KEY_INSERT = 14;
    public static final byte KEY_DELETE = 15;
    public static final byte KEY_HOME = 16;
    public static final byte KEY_END = 17;
    public static final byte KEY_TAB = 18;
    public static final byte KEY_F1 = 19;
    public static final byte KEY_F2 = 20;
    public static final byte KEY_F3 = 21;
    public static final byte KEY_F4 = 22;
    public static final byte KEY_F5 = 23;
    public static final byte KEY_F6 = 24;
    public static final byte KEY_F7 = 25;
    public static final byte KEY_F8 = 26;
    public static final byte KEY_F9 = 27;
    public static final byte KEY_F10 = 28;
    public static final byte KEY_F11 = 29;
    public static final byte KEY_F12 = 30;
    public static final byte KEY_BACKSPACE = 31;
    public static final byte KEY_A = -1;
    public static final byte KEY_B = -2;
    public static final byte KEY_C = -3;
    public static final byte KEY_D = -4;
    public static final byte KEY_E = -5;
    public static final byte KEY_F = -6;
    public static final byte KEY_G = -7;
    public static final byte KEY_H = -8;
    public static final byte KEY_I = -9;
    public static final byte KEY_J = -10;
    public static final byte KEY_K = -11;
    public static final byte KEY_L = -12;
    public static final byte KEY_M = -13;
    public static final byte KEY_N = -14;
    public static final byte KEY_O = -15;
    public static final byte KEY_P = -16;
    public static final byte KEY_Q = -17;
    public static final byte KEY_R = -18;
    public static final byte KEY_S = -19;
    public static final byte KEY_T = -20;
    public static final byte KEY_U = -21;
    public static final byte KEY_V = -22;
    public static final byte KEY_W = -23;
    public static final byte KEY_X = -24;
    public static final byte KEY_Y = -25;
    public static final byte KEY_Z = -26;
    public static final byte KEY_DOT = -27;
    public static final byte KEY_COMMA = -28;
    public static final byte KEY_SEMICOLON = -29;
    public static final byte KEY_COLON = -30;
    public static final byte KEY_DASH = -31;
    public static final byte KEY_SLASH = -32;
    
    /// Mouse codes
    public static final byte CLICK = -1;
    public static final byte PRESS = -2;
    public static final byte RELEASE = -3;
    public static final byte DOUBLE_CLICK = -4;
    public static final byte SCROLL_VERTICAL = -5;
    public static final byte SCROLL_HORIZONTAL = -6;
    
    public static final byte MOUSE_LEFT = 1;
    public static final byte MOUSE_MIDDLE = 4;
    public static final byte MOUSE_RIGHT = 2;
}
