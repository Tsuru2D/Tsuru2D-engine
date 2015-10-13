#!/bin/bash
projdir=$1
cd "${projdir}/android/assets"
for f in *; do
    if [ -d "$f" ]; then
        cd "${f}"
        rm -f "${projdir}/desktop/${f}"
        zip -r "${projdir}/desktop/${f}" *
        cd ..
    fi
done
