#!/bin/sh
# Run inside an already booted Android emulator. Preserve the test status while
# still attempting to retrieve any screenshots produced before a failure.
set +e
./gradlew :app:connectedDebugAndroidTest > e1-test.log 2>&1
result=$?
cat e1-test.log
if [ "$result" -ne 0 ]; then
    python3 -c 'from pathlib import Path; text="\n".join(Path("e1-test.log").read_text(errors="replace").splitlines()[-100:]); print("::error::" + text.replace("%", "%25").replace("\r", "%0D").replace("\n", "%0A"))'
fi
mkdir -p e1-screenshots
for name in 0-room.png 1-dormant.png 2-attending.png 3-listening.png 4-thinking.png 5-speaking.png 6-acting.png 7-alarmed.png 8-snapshot.png; do
    adb exec-out cat "/sdcard/Pictures/PetexE1/$name" > "e1-screenshots/$name"
    if [ "$?" -ne 0 ] || [ "$(head -c 8 "e1-screenshots/$name" | od -An -tx1 | tr -d " \n")" != "89504e470d0a1a0a" ]; then
        rm -f "e1-screenshots/$name"
        echo "::error::Could not retrieve screenshot $name."
        result=1
    fi
done
exit "$result"
