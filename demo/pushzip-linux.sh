#!/bin/bash
projdir=$1
find "${projdir}/demo/out" \( -name "*.vngame" -o -name "*.vnlang" \) -exec adb push {} "/sdcard/Tsuru2D" \;
