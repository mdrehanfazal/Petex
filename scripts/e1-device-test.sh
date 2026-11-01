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
adb pull /sdcard/Android/data/com.petex/files/e1/. e1-screenshots/
pull_result=$?
if [ "$result" -eq 0 ] && [ "$pull_result" -ne 0 ]; then
    result=$pull_result
fi
if [ "$result" -eq 0 ] && [ "$(find e1-screenshots -name '*.png' | wc -l)" -ne 7 ]; then
    echo '::error::Expected seven captured orb screenshots.'
    result=1
fi
exit "$result"
