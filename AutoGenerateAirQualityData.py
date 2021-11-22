import pyrebase
import random
import time
import os

firebaseConfig = {
    "apiKey": "AIzaSyCa19dCnLgPD_8zLr0c5E8VPQrt5WrtvLI",
    "authDomain": "pbl4-airquality.firebaseapp.com",
    "databaseURL": "https://pbl4-airquality-default-rtdb.firebaseio.com",
    "projectId": "pbl4-airquality",
    "storageBucket": "pbl4-airquality.appspot.com",
    "messagingSenderId": "799878169716",
    "appId": "1:799878169716:web:f37c5f621f82e2c83e3d21",
    "measurementId": "G-NKL2WZJ3J5"
}

firebase = pyrebase.initialize_app(firebaseConfig)

database = firebase.database()


def randomValues():
    # ["PM2.5", "PM10", "NO", "NO2", "NOx", "NH3", "CO", "SO2", "O3", "Benzene", "Toluene", "Xylene"]

    # PM2.5
    pm25_range = random.choices(
        [(0, 200), (200, 1000)], weights=(95, 5), k=1)
    pm25 = random.uniform(*pm25_range[0])

    # PM10
    pm10_range = random.choices(
        [(0, 200), (200, 1000)], weights=(90, 10), k=1)
    pm10 = random.uniform(*pm10_range[0])

    # NO
    no_range = random.choices(
        [(0, 50), (50, 100), (100, 500)], weights=(85, 10, 5), k=1)
    no = random.uniform(*no_range[0])

    # NO2
    no2_range = random.choices(
        [(0, 50), (50, 100), (100, 500)], weights=(85, 10, 5), k=1)
    no2 = random.uniform(*no2_range[0])

    # NOx
    nox_range = random.choices(
        [(0, 50), (50, 100), (100, 500)], weights=(85, 10, 5), k=1)
    nox = random.uniform(*nox_range[0])

    # NH3
    nh3_range = random.choices(
        [(0, 100), (100, 500)], weights=(95, 5), k=1)
    nh3 = random.uniform(*nh3_range[0])

    # CO
    co_range = random.choices(
        [(0, 100), (100, 500)], weights=(95, 5), k=1)
    co = random.uniform(*co_range[0])

    # so2
    so2_range = random.choices(
        [(0, 100), (100, 500)], weights=(95, 5), k=1)
    so2 = random.uniform(*so2_range[0])

    # o3
    o3_range = random.choices(
        [(0, 50), (50, 100), (100, 500)], weights=(70, 25, 5), k=1)
    o3 = random.uniform(*o3_range[0])

    # Benzene
    benzene_range = random.choices(
        [(0, 20), (20, 500)], weights=(95, 5), k=1)
    benzene = random.uniform(*benzene_range[0])

    # Toluene
    toluene_range = random.choices(
        [(0, 20), (20, 500)], weights=(95, 5), k=1)
    toluene = random.uniform(*toluene_range[0])

    # Xylene
    xylene_range = random.choices(
        [(0, 20), (20, 500)], weights=(95, 5), k=1)
    xylene = random.uniform(*xylene_range[0])

    return pm25, pm10, no, no2, nox, nh3, co, so2, o3, benzene, toluene, xylene


def addNewHourlyData(current_day, current_time):
    for locationID in range(1, 10):
        pm25, pm10, no, no2, nox, nh3, co, so2, o3, benzene, toluene, xylene = randomValues()
        data = {
            "LocationID": locationID,
            "Datetime": current_day + " " + current_time,
            "PM25": pm25,
            "PM10": pm10,
            "NO": no,
            "NO2": no2,
            "NOx": nox,
            "NH3": nh3,
            "CO": co,
            "SO2": so2,
            "O3": o3,
            "Benzene": benzene,
            "Toluene": toluene,
            "Xylene": xylene
        }
        database.child('HourlyAirQuality').push(data)
    print("Post data successfully!")


print("Server runing...")

# Change time zone to Ho Chi Minh city
os.environ['TZ'] = 'Asia/Ho_Chi_Minh'
time.tzset()

print("Start time: ", time.strftime("%d/%m/%Y %H:%M:%S"))

while True:
    current_time = time.strftime("%H:%M:%S")
    if (current_time == "23:45:00"):
        database.child('HourlyAirQuality').remove()
        print("Clearing today's data...")
        time.sleep(2)

    if (current_time == current_time.split(':')[0]+":00:00"):
        current_day = time.strftime("%d/%m/%Y", time.localtime())
        print(current_day, current_time)

        addNewHourlyData(current_day, current_time)

        time.sleep(60*60-2)
