#!/bin/bash
projdir=$1
cd "${projdir}/demo/src/"
for f in *; do
    if [ -d "$f" ]; then
        cd "${f}"
        rm -f "${projdir}/demo/out/${f}"
        zip -r "${projdir}/demo/out/${f}" *
        cd ..
    fi
done
