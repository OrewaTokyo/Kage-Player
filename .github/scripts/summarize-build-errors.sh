#!/usr/bin/env bash
# Turns a Gradle log into something readable: red annotations on the Actions run page
# and a short "what went wrong" section in the job summary.
# Usage: summarize-build-errors.sh <gradle-log> [title]
set -uo pipefail

LOG="${1:?usage: summarize-build-errors.sh <gradle-log> [title]}"
TITLE="${2:-Build failed}"
ROOT="${GITHUB_WORKSPACE:-$PWD}"
SUMMARY="${GITHUB_STEP_SUMMARY:-/dev/stdout}"

[ -f "$LOG" ] || { echo "No log file at $LOG"; exit 0; }

# Kotlin compiler errors, both formats:
#   e: file:///path/File.kt:12:34 Message
#   e: /path/File.kt: (12, 34): Message
KOTLIN=$(grep -E '^e: ' "$LOG" | sed -E "s#file://##; s#$ROOT/##" | sort -u | head -40)

# Gradle's own explanation ("* What went wrong:" ... "* Try:")
WRONG=$(awk '/^\* What went wrong:/{f=1;next} /^\* Try:/{f=0} f' "$LOG" | sed '/^$/d' | head -20)

# Resource, Java and R8 problems
OTHER=$(grep -E 'AAPT: error|error: |^ERROR: |Missing class|Execution failed for task' "$LOG" \
  | grep -v '^e: ' | sed -E "s#file://##; s#$ROOT/##" | sort -u | head -20)

# Red annotations (these show on the run page without opening the log)
printf '%s\n' "$KOTLIN" | sed -nE 's#^e: ([^:]+):([0-9]+):([0-9]+) (.*)#::error file=\1,line=\2::\4#p'
printf '%s\n' "$KOTLIN" | sed -nE 's#^e: ([^:]+): \(([0-9]+), ([0-9]+)\): (.*)#::error file=\1,line=\2::\4#p'
[ -n "$WRONG" ] && echo "::error title=$TITLE::$(printf '%s' "$WRONG" | head -3 | tr '\n' ' ')"

{
  echo "## $TITLE"
  if [ -n "$KOTLIN" ]; then
    echo "### Kotlin errors"
    echo '```'
    printf '%s\n' "$KOTLIN"
    echo '```'
  fi
  if [ -n "$WRONG" ]; then
    echo "### What went wrong"
    echo '```'
    printf '%s\n' "$WRONG"
    echo '```'
  fi
  if [ -n "$OTHER" ]; then
    echo "### Other errors"
    echo '```'
    printf '%s\n' "$OTHER"
    echo '```'
  fi
  if [ -z "$KOTLIN$WRONG$OTHER" ]; then
    echo "No known error pattern found. Download the **gradle-log** artifact from this run and search it for \`FAILED\`."
  fi
} >> "$SUMMARY"
exit 0
