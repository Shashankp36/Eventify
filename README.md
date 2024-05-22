# Eventify

Eventify is an Android application developed using Kotlin, designed to streamline event management by providing a platform for both vendors and users to interact. The app utilizes Firebase for data storage and offers two separate dashboards: one for users and another for vendors. Vendors can showcase their services and packages, while users can explore these offerings, make bookings, and contact vendors for personalization.

## Features

- **User Dashboard**: 
  - Users can explore a list of vendors and their services.
  - Users can view details of services and packages offered by vendors.
  - Users can make bookings for services provided by vendors.
  - Users can contact vendors for customization and inquiries.

- **Vendor Dashboard**:
  - Vendors can add and manage their services and packages.
  - Vendors can update service details, pricing, and availability.
  - Vendors can view and respond to booking requests from users.
  - Vendors can communicate with users regarding event customization and other inquiries.

## Technologies Used

- Kotlin: Kotlin is the primary programming language used for developing the Android application.
- Firebase: Firebase is utilized for backend services including data storage, authentication, and real-time database functionality.

## Installation

1. Clone the repository:
-https://github.com/Shashankp36/Eventify

2. Open the project in Android Studio.

3. Configure Firebase:
- Create a Firebase project on the Firebase console (https://console.firebase.google.com/).
- Add your Android app to the Firebase project.
- Download the `google-services.json` file and place it in the `app` directory of your project.
- Enable Firebase Authentication and Firebase Realtime Database for your project.
- Update the Firebase configuration in the `google-services.json` file with your own Firebase project details.

4. Build and run the project on your Android device or emulator.

## Contributing

Contributions to Eventify are welcome! If you find any bugs or have suggestions for improvements, feel free to open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
