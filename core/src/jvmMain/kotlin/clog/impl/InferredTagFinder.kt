package clog.impl

class InferredTagFinder(private vararg val internalClassNames: String) {
    fun findCallerClassName(t: Throwable = Throwable()): String? {
        val frames = t.stackTrace
        if (frames.isEmpty()) {
            return null
        }

        // starting with the first frame's class name (this Clog class)
        // keep iterating until a frame of a different class name is found
        return frames
            .asSequence()
            .map {
                // remove inner class names
                it.className.replaceAfter('$', "").removeSuffix("$")
            }
            .firstOrNull {
                // Filter out internal Clog classes
                it !in internalClassNames
            }
            ?.let {
                // Get the simple class name
                it.replaceBeforeLast('.', "").removePrefix(".")
            }
    }
}
