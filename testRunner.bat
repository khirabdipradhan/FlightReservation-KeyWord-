Set projectLocation=C:\Selenium\workspace\FlightReservation(KeyWord)
cd %projectLocation%
Set classPath=%projectLocation%\bin;%projectLocation%/libs/*;
java org.testng.TestNG testng.xml
pause