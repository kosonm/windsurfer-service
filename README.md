# Windsurfing Weather Service

## Project Description

The Windsurfing Weather Service project is an application that allows users to check the best location for windsurfing based on weather conditions. The application integrates with the external Weatherbit API to fetch current weather data for various locations. Based on this data, it evaluates which location has the best conditions for windsurfing on a given day.

## Available Endpoints

### GET /api/weather/best-location

**Description**: Retrieves the best location for windsurfing based on weather conditions.

**Request Parameters**:
- `date` (required): The date for which to fetch the weather conditions (format: `yyyy-MM-dd`).

**Response**:
- `200 OK`: Returns the best location with weather conditions.
    ```json
    {
        "location": "Location1",
        "temperature": 25.0,
        "windSpeed": 10.0
    }
    ```
- `204 No Content`: No suitable location found.
- `500 Internal Server Error`: An error occurred while fetching weather data.

## How to Build and Run the Project

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- An IDE or text editor of your choice

### Steps to Build and Run

1. **Set up the Weatherbit API key and URL**:
    - Create an `application.yml` file in the `src/main/resources` directory with the following content:
    ```yaml
    weatherbit:
      api:
        key: YOUR_API_KEY_HERE
        url: https://api.weatherbit.io/v2.0/forecast/daily
    ```

2. **Set up the Locations configuration**:
    - Create a `locations.yml` file in the `src/main/resources` directory with the following content:
    ```yaml
    locations:
      locations:
        - name: Location1
          latitude: 50.0
          longitude: 19.0
        - name: Location2
          latitude: 51.0
          longitude: 20.0
    ```

3. **Build the project**:
    ```sh
    mvn clean install
    ```

4. **Run the project**:
    ```sh
    mvn spring-boot:run
    ```

5. **Access the application**:
    - Open a browser and go to `http://localhost:8080/api/weather/best-location?date=2023-07-01` to see the best windsurfing location for the specified date.

## Additional Information

### Testing the Application

The project includes unit and integration tests to ensure the functionality of the application. To run the tests, use the following command:

```sh
$ mvn clean test
```

To generate the JaCoCo code coverage report, use:
```sh
$ mvn clean test jacoco:report
```