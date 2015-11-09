$projdir=$args[0]

$zips = Get-ChildItem "$($projdir)\demo\out\*" -Include *.vngame, *.vnlang
foreach ($zip in $zips) {
    cmd /c "adb push $zip /sdcard/Tsuru2D 2>&1"
}
