# Experience phases

The user's experience specification sets the following phases and acceptance criteria. This checkout did not contain `PETEX_BUILD.md`, `docs/MESSAGING_CONTROL.md`, `docs/PETEX_EXPERIENCE_SPEC.md`, or `AGENTS.md`. The experience specification was provided in the task; the other contracts and Phase 0 implementation must be supplied before their requirements can be validated. Phase 0 messaging has priority over experience work; the E1 study is an isolated visual foundation, not a substitute for it.

| Phase | Scope | Acceptance criterion |
| --- | --- | --- |
| E1 | Theme, tokens and static orb | All six standard states plus Alarmed render; exact colours; no raw hex outside the theme. |
| E2 | Motion, springs, collapse/expand, haptics | Interruptible, reversible transitions without double-settle; specified haptic patterns. |
| E3 | Voice–orb synchronisation | VAD and TTS envelope track within 60 ms; barge-in under 150 ms. |
| E4 | Presence strip, room, intent bar, wake/collapse | Wake visual under 80 ms, collapse after 8 seconds idle, one gesture to talk. |
| E5 | Personality and onboarding | Voice-settable dials; weekly keep/forget review; honesty locked. |
| E6 | Build Anything website | Real preview in under 60 seconds and three live voice edits; E4 must exist first. |
| E7 | Sound and boot | Respect silent mode, sound level limit, 2.4-second boot. |
| E8 | Performance, battery, accessibility | Macrobenchmark green, dormant below 1% per hour, TalkBack reads every state. |

Each phase requires real acceptance output and a draft PR. UI phases also require screenshots or a screen recording. Source checks alone are not evidence that a UI renders or performs correctly.
