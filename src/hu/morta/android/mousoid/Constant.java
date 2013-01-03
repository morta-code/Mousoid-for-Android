package hu.morta.android.mousoid;

public interface Constant {
	/// Command header parts
    public static final byte HEADER = 66;

    public static final byte WHO_ARE_YOU = -128;
    public static final byte KEYCOMMAND = 1;
    public static final byte SYSTEM = 2;
    public static final byte MOUSEMOVE = 4;
    public static final byte MOUSEBUTTON = 5;
    public static final byte NAME = 8;


    /// Command value parts
    
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

    /// Portable keycodes
    public static final int Key_Escape = 0x01000000;                // misc keys
    public static final int Key_Tab = 0x01000001;
    public static final int Key_Backtab = 0x01000002;
    public static final int Key_Backspace = 0x01000003;
    public static final int Key_Return = 0x01000004;
    public static final int Key_Enter = 0x01000005;
    public static final int Key_Insert = 0x01000006;
    public static final int Key_Delete = 0x01000007;
    public static final int Key_Pause = 0x01000008;
    public static final int Key_Print = 0x01000009;
    public static final int Key_SysReq = 0x0100000a;
    public static final int Key_Clear = 0x0100000b;
    public static final int Key_Home = 0x01000010;                // cursor movement
    public static final int Key_End = 0x01000011;
    public static final int Key_Left = 0x01000012;
    public static final int Key_Up = 0x01000013;
    public static final int Key_Right = 0x01000014;
    public static final int Key_Down = 0x01000015;
    public static final int Key_PageUp = 0x01000016;
    public static final int Key_PageDown = 0x01000017;
    public static final int Key_Shift = 0x01000020;                // modifiers
    public static final int Key_Control = 0x01000021;
    public static final int Key_Meta = 0x01000022;
    public static final int Key_Alt = 0x01000023;
    public static final int Key_CapsLock = 0x01000024;
    public static final int Key_NumLock = 0x01000025;
    public static final int Key_ScrollLock = 0x01000026;
    public static final int Key_F1 = 0x01000030;                // function keys
    public static final int Key_F2 = 0x01000031;
    public static final int Key_F3 = 0x01000032;
    public static final int Key_F4 = 0x01000033;
    public static final int Key_F5 = 0x01000034;
    public static final int Key_F6 = 0x01000035;
    public static final int Key_F7 = 0x01000036;
    public static final int Key_F8 = 0x01000037;
    public static final int Key_F9 = 0x01000038;
    public static final int Key_F10 = 0x01000039;
    public static final int Key_F11 = 0x0100003a;
    public static final int Key_F12 = 0x0100003b;
    public static final int Key_Super_L = 0x01000053;                 // extra keys
    public static final int Key_Super_R = 0x01000054;
    public static final int Key_Menu = 0x01000055;
    public static final int Key_Back  = 0x01000061;
    public static final int Key_Forward  = 0x01000062;
    public static final int Key_Stop  = 0x01000063;
    public static final int Key_Refresh  = 0x01000064;
    public static final int Key_VolumeDown = 0x01000070;
    public static final int Key_VolumeMute  = 0x01000071;
    public static final int Key_VolumeUp = 0x01000072;
    public static final int Key_MediaPlay  = 0x01000080;
    public static final int Key_MediaStop  = 0x01000081;
    public static final int Key_MediaPrevious  = 0x01000082;
    public static final int Key_MediaNext  = 0x01000083;
    public static final int Key_MediaRecord = 0x01000084;
    public static final int Key_MediaPause = 0x1000085;
    public static final int Key_MediaTogglePlayPause = 0x1000086;
    public static final int Key_AltGr               = 0x01001103;
    public static final int Key_Multi_key           = 0x01001120;
    public static final int Key_MonBrightnessUp = 0x010000b2;
    public static final int Key_MonBrightnessDown = 0x010000b3;
    public static final int Key_PowerOff = 0x010000b7;
    public static final int Key_Eject = 0x010000b9;
    public static final int Key_WWW = 0x010000bb;
    public static final int Key_History = 0x010000bf;
    public static final int Key_AddFavorite = 0x010000c0;
    public static final int Key_HotLinks = 0x010000c1;
    public static final int Key_BrightnessAdjust = 0x010000c2;
    public static final int Key_AudioRewind = 0x010000c5;
    public static final int Key_BackForward = 0x010000c6;
    public static final int Key_ClearGrab = 0x010000cd;
    public static final int Key_Close = 0x010000ce;
    public static final int Key_Copy = 0x010000cf;
    public static final int Key_Cut = 0x010000d0;
    public static final int Key_Display = 0x010000d1;
    public static final int Key_Paste = 0x010000e2;
    public static final int Key_Reload = 0x010000e6;
    public static final int Key_Save = 0x010000ea;
    public static final int Key_Send = 0x010000eb;
    public static final int Key_Spell = 0x010000ec;
    public static final int Key_SplitScreen = 0x010000ed;
    public static final int Key_Support = 0x010000ee;
    public static final int Key_TaskPane = 0x010000ef;
    public static final int Key_Terminal = 0x010000f0;
    public static final int Key_View = 0x01000109;
    public static final int Key_TopMenu = 0x0100010a;
}
