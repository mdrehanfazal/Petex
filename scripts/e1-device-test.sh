#!/bin/sh
# Run inside an already booted Android emulator. Preserve the test status while
# still attempting to retrieve any screenshots produced before a failure.
set +e
./gradlew :app:connectedDebugAndroidTest > e1-test.log 2>&1
result=$?
cat e1-test.log
if [ "$result" -ne 0 ]; then
    tail -60 e1-test.log | while IFS= read -r line; do
        echo "::error::$line"
    done
fi
mkdir -p e1-screenshots
index=0
for state in dormant attending listening thinking speaking acting alarmed; do
    index=$((index + 1))
    name="$index-$state.png"
    adb exec-out cat "/sdcard/Pictures/PetexE1/$name" > "e1-screenshots/$name"
    if [ "$?" -ne 0 ] || [ "$(head -c 8 "e1-screenshots/$name" | od -An -tx1 | tr -d " \n")" != "89504e470d0a1a0a" ]; then
        rm -f "e1-screenshots/$name"
        echo "::error::Could not retrieve screenshot $name."
        result=1
    fi
done
exit "$result"
