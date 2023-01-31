set PARENT_WORKSPACE=%CD%
set VERSION=%1

CD %PARENT_WORKSPACE%
CALL %PARENT_WORKSPACE%\mvnw.cmd clean package

REM 1. Build order-service
CD order-service
CALL docker login
CALL docker build -t order-service .
CALL docker tag order-service toannguyenpersonify/order-service:%VERSION%
CALL docker push toannguyenpersonify/order-service:%VERSION%

REM 2. Build payment-service
CD ..\payment-service
CALL docker login
CALL docker build -t payment-service .
CALL docker tag payment-service toannguyenpersonify/payment-service:%VERSION%
CALL docker push toannguyenpersonify/payment-service:%VERSION%

REM 3. Build stock-service
CD ..\stock-service
CALL docker login
CALL docker build -t stock-service .
CALL docker tag stock-service toannguyenpersonify/stock-service:%VERSION%
CALL docker push toannguyenpersonify/stock-service:%VERSION%