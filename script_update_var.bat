@ECHO off
heroku run env > teste.txt
FOR /F "tokens=1,2,3,4,5 delims==" %%a IN (teste.txt) DO (	
	if "%%a" == "JDBC_DATABASE_USERNAME" (
		ECHO Find JDBC_DATABASE_USERNAME
		SETX %%a %%b
	)	
	
	if "%%a" == "JDBC_DATABASE_URL" (
		ECHO %%a "==" %%b=%%c=%%d==%%e
		ECHO Find JDBC_DATABASE_URL
		SETX %%a %%b=%%c=%%d=%%e
	)
	
	if "%%a" == "JDBC_DATABASE_PASSWORD" (
		ECHO Find JDBC_DATABASE_PASSWORD
		SETX %%a %%b
	)
)
@ECHO on
