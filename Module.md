# Module angelos-project-conc
# Package org.angproj.conc

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