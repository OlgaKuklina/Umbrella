# Umbrella
![sfumbrella](https://cloud.githubusercontent.com/assets/6971421/24835421/f9dd3adc-1cb6-11e7-994d-426e4309b056.jpg)

Overview
======
Implemented a simple Android app to display current weather conditions. 
The work included consistent and meaningful use of Material Design UI, ensuring errors were handled gracefully.

The app displays the following weather conditions:
- Temperature in Celsius and Fahrenheit
- Wind speed.
- An umbrella icon if the cloudiness percentage is greater than 50%.
- Provides a button that when tapped, fetches the weather for the next 5 days, and displays the standard deviation of the temperature.
The app handles configuration and orientation changes without hitting network again.

Application min API Level: 22, Android 5.1 ( LOLLIPOP_MR1 ).

Used libraries
======
- CommonsIO/CommonsHttpClient for accessing GitHub REST api.
