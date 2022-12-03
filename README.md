# AdventOfCode-Kotlin

My attempt at the [Advent of Code][aoc] in Kotlin. Originally based on using Jetbrain's
[Advent of Code Kotlin Template][template], but a lot has changed since then. Each [AdventDay][ad] class can
now download the appropriate day input (IF the time is right, AND it doesn't already exist, AND you've provided a
session token inside 'sessionToken.txt' inside ~Working Directory~/data, AND if that day's input doesn't already exist
inside that same data subdirectory).

The kotlin/ktor web-scraper used to grab the input can also be run < 100 seconds before release, upon which it will
wait for your clock to hit midnight EST, and then grab the input for you automatically (it has a small semi-random delay
to limit AoC server congestion).

Run configurations are stored in .run, which should be detected by IntelliJ, otherwise you'll have to correctly set up
the main classes + configurations yourself.

### TODO

- Automatic submission of answers.
- All the rest of the days.

[aoc]: https://adventofcode.com
[ad]: https://github.com/CognitiveGear/AdventOfCode-Kotlin/tree/common/src/main/kotlin/AdventDay.kt
[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
