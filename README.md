# CS 360 – Mobile Architecture and Programming  
## Project Three – Portfolio Reflection

## Summarize the project and what problem it was solving

In this project, I built a working Android mobile app that helps users keep track of their inventory in a simple and straightforward way. The issue the app addresses is the challenge of managing items, quantities, and updates consistently—especially when this information is handled manually using notes or spreadsheets. This mobile app combines storage functionality with persistent memory, allowing users to store and manage inventory wherever and whenever they need.
The application allows users to create an account, log in securely, and store inventory data using a local SQLite database. It also includes optional SMS notifications for certain conditions, such as running low on goods. Overall, this project illustrates how a mobile app can meet a real organizational need through practical design and reliable functionality.

## What did you do particularly well?

As the developer, I did particularly well in implementing persistent data storage alongside user authentication. The SQLite database was specifically designed to store both login credentials and inventory information, ensuring that user and item data remain available even after the app is closed. I also successfully implemented full CRUD functionality, allowing users to easily create, view, update, and delete inventory items.
Another strong aspect of the project was handling SMS permissions correctly. The app requests permission at runtime and adjusts its behavior based on the user’s response. If permission is denied, the app continues to function normally without crashes or performance issues, while simply disabling the SMS feature. This helped reinforce good programming habits and the importance of respecting user choice.


## Where could you enhance your code? How would these improvements make your code more efficient, secure, or maintainable?

If this app were further developed, there are various areas where it could be improved. One key enhancement would be better separation of responsibilities within the code, such as moving database logic into dedicated helper or repository classes. This would make the code easier to read, maintain, and extend as the application grows.
From a security standpoint, a significant improvement would be implementing password hashing instead of storing credentials in plain text. While this was outside the scope of the course, it would be essential for a real-world application. Additionally, improving input validation and refining the user interface would help prevent invalid data and provide a smoother overall user experience.


## Did you find writing any piece of this code challenging, and how did you overcome this challenge? What tools or resources did you use?

The most challenging part of this project was integrating the login system with the SQLite database while correctly handling both new and returning users. Verifying credentials properly and ensuring that accounts were created only when necessary required careful logic and testing.
To work through these challenges, I made extensive use of Android Studio’s debugging tools and the Android Emulator to test different scenarios. I also relied on Android developer documentation and course materials to better understand SQLite queries and runtime permissions. Breaking the problem into smaller pieces helped me troubleshoot issues more effectively.
What skills from this project will be particularly transferable to other projects or coursework?
This project helped me develop several skills that will be valuable in future coursework and professional projects. These include working with persistent databases, implementing user authentication, handling runtime permissions, and testing applications using emulators. I also became more confident in writing clean, organized code with consistent naming conventions and meaningful in-line comments.
Most importantly, this project reinforced the idea that building an application is about more than just making the code work. By thinking through usability, security, permissions, and launch considerations, I gained a better understanding of how real-world mobile applications are designed, developed, and maintained.
