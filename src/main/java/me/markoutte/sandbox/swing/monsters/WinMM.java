package me.markoutte.sandbox.swing.monsters;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

// https://docs.microsoft.com/en-us/windows/win32/api/timeapi/nf-timeapi-timebeginperiod
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface WinMM extends StdCallLibrary {

    WinMM lib = Native.load("winmm", WinMM.class);

    int timeBeginPeriod(int period);

    static void begin(int period) {
        lib.timeBeginPeriod(period);
    }

    int timeEndPeriod(int period);

    static void end(int period) {
        lib.timeEndPeriod(period);
    }

}
