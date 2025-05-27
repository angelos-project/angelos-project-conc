# Concurrency Utilities

This library provides a set of concurrency utilities 
designed to simplify the use of concurrent execution and
synchronization in Kotlin for the Angelos Project™.

The utility builds on top of Kotlin's coroutines and provides 
additional lambda methods and classes to handle 
execution of tasks in an idiomatic way.

The library's primary focus is to provide a set of tools that
gives the developer a hassle-free experience without meddling with
the underlying implementation. Methods that can be called from 
any context whether suspending or blocking. Which doesn't use
any features that are experimental, and which is available across
all stable Kotlin build targets.

# Features
- **Dispenser**: A synchronization primitive class that encapsulates a shared resource, using a higher-order function wrapped in a `Mutex` to dispense the resource to a single coroutine at a time.
- **Clock**: A function that executes a higher-order function at a specified interval, allowing for periodic execution of a defined lambda.
- **Schedule**: A function that executes a higher-order function after a specific delay, one time.
- **Task**: A function that executes a higher-order lambda as a coroutine, can be called from any synchronous block, executing asynchronously.
- **Loop**: A function that executes a higher-order function in a loop, allowing for repeated execution, yielding to the corotuine event loop for each iteration.
- **Call**: A function that repeatedly checks another coroutine's state, executing a higher-order lambda if the coroutine is cancelled, allowing for a callback-like behavior.
- **Steward**: A wake up primitive class that executes a higher-order lambda coroutine at wake up, while executing the coroutine, subsequent wake up calls are registered and queued fo faithful execution.
- **Waitress**: A wake up primitive class that executes a higher-order lambda coroutine at wake up, while executing the coroutine, subsequent wake up calls are skipped over until back to sleep.

# Installation
To use the concurrency utilities in your Kotlin project, add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("org.angproj.conc:angelos-project-conc:0.1.2-SNAPSHOT")
}
```

# Contributing
We welcome contributions to this project! If you have an idea for a new feature, bug fix, or improvement, please open an issue or submit a pull request.

# License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
