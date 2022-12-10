#!/bin/bash
ignore_files=( .git .idea out .github )
re="$(printf '%s\n' "${ignore_files[@]}" | paste -sd '|')"
for D in $(find ./ -mindepth 1 -maxdepth 1 -type d);
do
    if [[ ! (${D:2} =~ $re) ]]; then
      (cd ${D:2} && zip -r ../${D:2}.zip . -x '*.iml');
    fi
done