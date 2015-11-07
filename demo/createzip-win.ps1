$projdir=$args[0]
Add-Type -A System.IO.Compression.FileSystem

$dirs = Get-ChildItem "$($projdir)\demo\src"
foreach ($dir in $dirs) {
    Remove-Item "$($projdir)\demo\out\$($dir.BaseName)"
    [IO.Compression.ZipFile]::CreateFromDirectory($dir.FullName, "$($projdir)\demo\out\$($dir.BaseName)")
}
