package dev.adamko.geedeecee.internal


/**
 * Parses space-separated arguments and returns a list of strings.
 *
 * Arguments surrounded by quotation marks `"` will be joined into a single arg.
 *
 * @param args the space-separated arguments to parse
 * @return a list of strings representing the individual arguments
 *
 * @throws IllegalStateException if there is an unmatched quotation mark
 */
internal fun parseSpaceSeparatedArgs(
  vararg args: String
): List<String> {
  val parsedArgs = mutableListOf<String>()
  var inQuotes = false
  val currentArg = StringBuilder()

  fun saveArg(wasInQuotes: Boolean) {
    if (wasInQuotes || currentArg.isNotBlank()) {
      parsedArgs.add(currentArg.toString())
      currentArg.clear()
    }
  }

  args
    .joinToString(" ")
    .forEach { char ->
      when {
        char == '"'              -> {
          inQuotes = !inQuotes
          // Save value that was quoted
          if (!inQuotes) {
            saveArg(true)
          }
        }

        char == ' ' && !inQuotes -> {
          // Space is a separator
          saveArg(false)
        }

        else                     -> {
          currentArg.append(char)
        }
      }
    }

  if (inQuotes) {
    error("No close-quote was found in '$currentArg'")
  } else {
    saveArg(false)
    return parsedArgs
  }
}
