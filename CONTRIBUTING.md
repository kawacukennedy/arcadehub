# Contributing to ArcadeHub

We welcome contributions to ArcadeHub! Please take a moment to review this document to ensure a smooth contribution process.

## How Can I Contribute?

### Reporting Bugs

If you find a bug, please open an issue on our GitHub repository. When reporting a bug, please include:

*   A clear and concise description of the bug.
*   Steps to reproduce the behavior.
*   Expected behavior.
*   Screenshots or error messages if applicable.
*   Your operating system and Java version.

### Suggesting Enhancements

If you have an idea for a new feature or an improvement, please open an issue on our GitHub repository. Describe your suggestion in detail, including why you think it would be a good addition to ArcadeHub.

### Code Contributions

1.  **Fork the repository** and clone it to your local machine.
2.  **Create a new branch** for your feature or bug fix: `git checkout -b feature/your-feature-name` or `git checkout -b bugfix/your-bug-fix-name`.
3.  **Set up your development environment**:
    *   Ensure you have Java 17 LTS installed.
    *   This project uses Gradle. You can build the project using `./gradlew build` (Linux/macOS) or `gradlew.bat build` (Windows).
    *   Import the project into your IDE (IntelliJ IDEA is recommended).
4.  **Make your changes**:
    *   Adhere to the existing coding style and conventions.
    *   Write clear, concise, and well-documented code.
    *   Ensure your changes do not break existing functionality.
    *   Write unit tests for new features or bug fixes where appropriate.
5.  **Run tests** to ensure everything is working as expected: `./gradlew test`.
6.  **Commit your changes** with a clear and descriptive commit message.
7.  **Push your branch** to your forked repository.
8.  **Open a Pull Request** to the `main` branch of the original repository. Provide a detailed description of your changes.

## Coding Style

*   Follow standard Java coding conventions.
*   Use meaningful variable and method names.
*   Keep methods concise and focused on a single responsibility.
*   Add Javadoc comments for classes, methods, and complex logic.

## Code of Conduct

We expect all contributors to adhere to our Code of Conduct (to be defined). Please be respectful and considerate in all your interactions.
