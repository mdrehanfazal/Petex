"""Fast source-level guardrails; these are not Android rendering tests."""
import pathlib
import re
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
SOURCE = ROOT / "app/src/main/java/com/petex"


class E1SourceChecks(unittest.TestCase):
    def test_hex_palette_lives_only_in_theme(self):
        for source in (ROOT / "app/src").rglob("*.kt"):
            if source.name != "PetexTheme.kt":
                self.assertNotRegex(source.read_text(), r"(?i)0x[0-9a-f]{6,8}\b", str(source))

    def test_all_seven_visual_states_exist(self):
        state = (SOURCE / "surface/orb/OrbState.kt").read_text()
        self.assertEqual(re.findall(r"data object (\w+) : OrbState", state),
                         ["Dormant", "Attending", "Listening", "Thinking", "Speaking", "Acting", "Alarmed"])
        self.assertIn("sealed interface OrbState", state)

    def test_exact_palette_values(self):
        theme = (SOURCE / "surface/theme/PetexTheme.kt").read_text()
        for value in ("06080C", "0E131A", "4FD1FF", "A78BFA", "FFB454", "6EE7B7", "FF5A5F", "E8F1F8", "7C8B9B"):
            self.assertIn("0xFF" + value, theme)

    def test_motion_tokens(self):
        tokens = (SOURCE / "surface/motion/MotionTokens.kt").read_text()
        for token, value in (("MICRO_MS", 120), ("STANDARD_MS", 240), ("AMBIENT_MS", 420), ("HERO_MS", 760)):
            self.assertRegex(tokens, rf"const val {token}\s*=\s*{value}\b")
        for token in ("0.82f", "420f", "0.62f", "130f", "1.0f", "300f"):
            self.assertIn(token, tokens)

    def test_visual_study_does_not_request_mic_permission(self):
        manifest = (ROOT / "app/src/main/AndroidManifest.xml").read_text()
        self.assertNotIn("RECORD_AUDIO", manifest)
        activity = (SOURCE / "MainActivity.kt").read_text()
        self.assertIn("BuildConfig.DEBUG", activity)
        self.assertIn("No microphone, speech or actions are active", activity)


if __name__ == "__main__":
    unittest.main(verbosity=2)
