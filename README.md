# Test project LAB CFT SHIFT 2024 

![CFT](https://team.cft.ru/sites/job/SectionImages/cft_shift.png)

## The short description of app
This app is a simple and user-friendly note-taking app that helps people organize thoughts and ideas. With it, you can quickly jot down important notes, create drafts, and find the notes you need in seconds.
___

## Used libraries

![LifecycleViewModel](https://img.shields.io/badge/lifecycle-2.8.6-brown) 
![JetpackNavigation](https://img.shields.io/badge/jetpackNavigation-2.8.2-blue)
![Dagger2](https://img.shields.io/badge/dagger2-2.52-green)
![Room](https://img.shields.io/badge/room-2.6.0-red)

## Libraries Description
Lifecycle and ViewModel (androidx.lifecycle): The Lifecycle and ViewModel components manage 
UI-related data in a lifecycle-aware way. Lifecycle tracks component states, while ViewModel 
retains data across configuration changes, promoting cleaner and more maintainable code.

Jetpack Navigation (androidx.navigation): Jetpack Navigation simplifies in-app navigation by
providing a structured way to manage fragment transitions. It integrates seamlessly with Lifecycle
and ViewModel, ensuring smooth navigation while respecting component lifecycle states.
Room (androidx.room): Room is a persistent library that provides an abstraction layer over SQLite,
making it easier to work with databases in Android applications. It helps define database entities
and access patterns using annotations, while also providing compile-time verification of SQL queries,
reducing runtime errors.

Dagger 2 (com.google.dagger): Dagger 2 is a powerful dependency injection framework for Java and Android. 
It enables the implementation of dependency injection in a straightforward and efficient manner, 
promoting modular design and improving testability by managing dependencies at compile time.
