#!/bin/bash

DELIM='-'

WORDS="$(xkcdpass --numwords=2  --delimiter="$DELIM" --valid-chars='\w' --case='first')"

NUMS=$(( (RANDOM + 10) % 100 ))

echo "Generated ID $WORDS$DELIM$NUMS"

echo "$WORDS$DELIM$NUMS"

exit 0