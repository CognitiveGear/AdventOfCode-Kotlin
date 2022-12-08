# AdventOfCode-Kotlin (2022)

My attempt at the [Advent of Code][aoc] in Kotlin. Originally based on using Jetbrain's
[Advent of Code Kotlin Template][template], but a lot has changed since then. Each [AdventDay][ad] class can
now download the appropriate day input (IF the time is right, AND you've provided a session token inside '
sessionToken.txt' inside $WORKING-DIRECTORY$/data, AND if that day's input does not already exist inside that same data
subdirectory).

The kotlin/ktor web-scraper used to grab the input can also be run < 100 seconds before release, and it will wait for
the clock to hit midnight EST before grabbing them. The downloader has a small semi-random additional delay,
to limit initial AoC server congestion.

Run configurations are stored in .run (which should be detected by IntelliJ), otherwise you must set up
the main classes + configurations yourself.

### TODO

- Automatic submission of answers.
- All the rest of the days.

[aoc]: https://adventofcode.com
[ad]: https://github.com/CognitiveGear/AdventOfCode-Kotlin/tree/common/src/main/kotlin/AdventDay.kt
[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
