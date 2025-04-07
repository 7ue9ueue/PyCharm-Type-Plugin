#  Python Type Detection Plugin.

Work for PyCharm 2021.3. Will work for 2024.3 in the future (when I upgrade my intellij plugins).
Exported zip file is `build/distributions/PythonPlugin-1.0-SNAPSHOT.zip`.

## Features 

- Shows variable types (e.g., `str`, `int`, `float`, `bool`) as you navigate through your code.
- Uses the Python PSI to infer types from literals and variable references.
- Updates dynamically as you move the caret.


## Installation

1. **Build the Plugin:**
   ```bash
   ./gradlew buildPlugin
   ```
